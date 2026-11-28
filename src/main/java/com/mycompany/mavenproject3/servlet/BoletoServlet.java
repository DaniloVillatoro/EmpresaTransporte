package com.mycompany.mavenproject3.servlet;

import com.mycompany.mavenproject3.controlador.BoletoControlador;
import com.mycompany.mavenproject3.dao.MetodoPagoDAO;
import com.mycompany.mavenproject3.dao.VentaDAO;
import com.mycompany.mavenproject3.dao.VentaException;
import com.mycompany.mavenproject3.dao.VistaDAO;
import com.mycompany.mavenproject3.modelo.Boleto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

/**
 * Boletos: listado, venta en taquilla, anulacion y mapa de asientos (JSON para el panel).
 *   GET  /boletos                       -> listado
 *   GET  /boletos?accion=asientos&idHorario=N -> JSON con asientos
 *   POST /boletos  accion=vender | anular
 */
@WebServlet("/boletos")
public class BoletoServlet extends BaseServlet {

    private final VistaDAO vistaDAO = new VistaDAO();
    private final VentaDAO ventaDAO = new VentaDAO();
    private final MetodoPagoDAO metodoPagoDAO = new MetodoPagoDAO();
    private final BoletoControlador validador = new BoletoControlador();

    @Override
    protected void doGet(HttpServletRequest rq, HttpServletResponse rs) throws ServletException, IOException {
        try {
            if ("asientos".equals(rq.getParameter("accion"))) {
                enviarAsientosJson(rq, rs);
                return;
            }
            rq.setAttribute("boletos", vistaDAO.boletos(200));
            rq.setAttribute("horariosVenta", vistaDAO.horariosEnVenta());
            rq.setAttribute("metodos", metodoPagoDAO.listarTodos());
            vista(rq, rs, "boletos");
        } catch (SQLException e) {
            falloBD(rq, rs, e);
        }
    }

    private void enviarAsientosJson(HttpServletRequest rq, HttpServletResponse rs) throws SQLException, IOException {
        List<VistaDAO.Fila> asientos = vistaDAO.asientosDeHorario(entero(rq, "idHorario"));
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < asientos.size(); i++) {
            VistaDAO.Fila a = asientos.get(i);
            if (i > 0) {
                sb.append(',');
            }
            sb.append("{\"id\":").append(a.get("id"))
              .append(",\"numero\":").append(a.get("numero"))
              .append(",\"estado\":\"").append(String.valueOf(a.get("estado")).replace("\"", "")).append("\"}");
        }
        sb.append(']');
        rs.setContentType("application/json;charset=UTF-8");
        rs.getWriter().write(sb.toString());
    }

    @Override
    protected void doPost(HttpServletRequest rq, HttpServletResponse rs) throws ServletException, IOException {
        String accion = texto(rq, "accion");
        String volver = "/boletos".equals(texto(rq, "volver")) ? "/boletos" : "/panel";
        try {
            if ("anular".equals(accion)) {
                ventaDAO.anularBoleto(entero(rq, "idBoleto"), usuario(rq).getIdEmpleado());
                flash(rq, "ok", "Boleto anulado. El asiento volvió a quedar disponible y se registró el reembolso.");
                redirigir(rq, rs, "/boletos");
                return;
            }
            // ---- vender ----
            String identidad = texto(rq, "identidad");
            int idDisp = entero(rq, "idDisponibilidad");
            int idMetodo = entero(rq, "idMetodoPago");
            BigDecimal precio = decimal(rq, "precio");

            Boleto b = new Boleto();
            b.setIdCliente(1);                 // se resuelve en VentaDAO; solo para reutilizar la validacion
            b.setIdDisponibilidad(idDisp);
            b.setPrecio(precio);
            String error = identidad == null ? "Escribe la identidad del cliente."
                    : idMetodo <= 0 ? "Selecciona el método de pago."
                    : validador.validarBoleto(b);
            if (error != null) {
                flash(rq, "error", error);
                redirigir(rq, rs, volver);
                return;
            }
            VentaDAO.Resultado r = ventaDAO.registrarVenta(identidad, texto(rq, "nombre"), texto(rq, "apellido"),
                    idDisp, usuario(rq).getIdEmpleado(), idMetodo, precio);
            flash(rq, "ok", "Venta registrada. Boleto " + r.getCodigo() + ".");
            rq.getSession().setAttribute("ultimoBoleto", r.getIdBoleto());   // para ofrecer "Imprimir"
            redirigir(rq, rs, volver);
        } catch (VentaException e) {
            flash(rq, "error", e.getMessage());
            redirigir(rq, rs, volver);
        } catch (SQLException e) {
            falloBD(rq, rs, e);
        }
    }
}