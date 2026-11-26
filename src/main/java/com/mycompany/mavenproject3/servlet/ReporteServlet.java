package com.mycompany.mavenproject3.servlet;

import com.mycompany.mavenproject3.dao.ConexionBD;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;

/**
 * Genera reportes con JasperReports a partir de las plantillas /WEB-INF/reports/*.jrxml.
 *   GET /reportes                                   -> pantalla con los reportes disponibles
 *   GET /reportes?tipo=boletos&formato=pdf&fechaIni=2026-11-01&fechaFin=2026-11-30
 *   GET /reportes?tipo=boleto&id=15                 -> boleto individual (imprimible)
 * Formatos: pdf (se abre en el navegador) o csv (se abre en Excel).
 */
@WebServlet("/reportes")
public class ReporteServlet extends BaseServlet {

    /** tipo -> archivo .jrxml */
    private static final Map<String, String> PLANTILLAS = Map.of(
            "boletos", "boletos_vendidos.jrxml",
            "ventas_ruta", "ventas_por_ruta.jrxml",
            "mantenimiento", "mantenimiento_buses.jrxml",
            "boleto", "boleto_individual.jrxml");

    /** Cache: la plantilla se compila una sola vez. */
    private final Map<String, JasperReport> cache = new ConcurrentHashMap<>();

    @Override
    protected void doGet(HttpServletRequest rq, HttpServletResponse rs) throws ServletException, IOException {
        String tipo = texto(rq, "tipo");
        if (tipo == null) {
            rq.setAttribute("hoy", LocalDate.now());
            rq.setAttribute("inicioMes", LocalDate.now().withDayOfMonth(1));
            vista(rq, rs, "reportes");
            return;
        }
        if (!PLANTILLAS.containsKey(tipo)) {
            rs.sendError(HttpServletResponse.SC_BAD_REQUEST, "Reporte desconocido.");
            return;
        }
        boolean csv = "csv".equalsIgnoreCase(texto(rq, "formato"));
        try (Connection conn = ConexionBD.obtenerConexion()) {
            Map<String, Object> params = new HashMap<>();
            LocalDate ini = fecha(rq, "fechaIni");
            LocalDate fin = fecha(rq, "fechaFin");
            if (ini == null) {
                ini = LocalDate.now().withDayOfMonth(1);
            }
            if (fin == null) {
                fin = LocalDate.now();
            }
            params.put("FECHA_INI", Timestamp.valueOf(ini.atStartOfDay()));
            params.put("FECHA_FIN", Timestamp.valueOf(fin.atStartOfDay()));
            params.put("ID_BOLETO", entero(rq, "id"));
            params.put("USUARIO", usuario(rq).getNombre() + " " + usuario(rq).getApellido());
            params.put("REPORT_LOCALE", Locale.forLanguageTag("es-HN"));

            JasperPrint print = JasperFillManager.fillReport(plantilla(tipo), params, conn);
            if (print.getPages().isEmpty()) {
                rs.setContentType("text/plain;charset=UTF-8");
                rs.getWriter().write("No hay datos para el reporte seleccionado.");
                return;
            }
            if (csv) {
                rs.setContentType("text/csv;charset=UTF-8");
                rs.setHeader("Content-Disposition", "attachment; filename=\"" + tipo + ".csv\"");
                JRCsvExporter exp = new JRCsvExporter();
                exp.setExporterInput(new SimpleExporterInput(print));
                exp.setExporterOutput(new SimpleWriterExporterOutput(rs.getOutputStream(), "UTF-8"));
                exp.exportReport();
            } else {
                rs.setContentType("application/pdf");
                rs.setHeader("Content-Disposition", "inline; filename=\"" + tipo + ".pdf\"");
                JasperExportManager.exportReportToPdfStream(print, rs.getOutputStream());
            }
        } catch (Exception e) {
            LOG.log(java.util.logging.Level.SEVERE, "Error generando reporte " + tipo, e);
            if (!rs.isCommitted()) {
                rs.reset();
                rs.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "No se pudo generar el reporte: " + e.getMessage());
            }
        }
    }

    private JasperReport plantilla(String tipo) throws JRException, IOException {
        JasperReport r = cache.get(tipo);
        if (r == null) {
            try (InputStream in = getServletContext().getResourceAsStream("/WEB-INF/reports/" + PLANTILLAS.get(tipo))) {
                if (in == null) {
                    throw new IOException("No se encontro la plantilla " + PLANTILLAS.get(tipo));
                }
                r = JasperCompileManager.compileReport(in);
            }
            cache.put(tipo, r);
        }
        return r;
    }
}
