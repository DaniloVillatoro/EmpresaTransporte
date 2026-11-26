package com.mycompany.mavenproject3.servlet;

import com.mycompany.mavenproject3.controlador.RutaControlador;
import com.mycompany.mavenproject3.controlador.TerminalControlador;
import com.mycompany.mavenproject3.dao.RutaDAO;
import com.mycompany.mavenproject3.dao.TerminalDAO;
import com.mycompany.mavenproject3.dao.VistaDAO;
import com.mycompany.mavenproject3.modelo.Ruta;
import com.mycompany.mavenproject3.modelo.Terminal;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/** Rutas y terminales (las terminales son los extremos de cada ruta). */
@WebServlet("/rutas")
public class RutaServlet extends BaseServlet {

    private final RutaDAO rutaDAO = new RutaDAO();
    private final TerminalDAO terminalDAO = new TerminalDAO();
    private final VistaDAO vistaDAO = new VistaDAO();
    private final RutaControlador validadorRuta = new RutaControlador();
    private final TerminalControlador validadorTerminal = new TerminalControlador();

    @Override
    protected void doGet(HttpServletRequest rq, HttpServletResponse rs) throws ServletException, IOException {
        try {
            rq.setAttribute("rutas", vistaDAO.rutas());
            rq.setAttribute("terminales", terminalDAO.listarTodos());
            vista(rq, rs, "rutas");
        } catch (SQLException e) {
            falloBD(rq, rs, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest rq, HttpServletResponse rs) throws ServletException, IOException {
        String accion = texto(rq, "accion");
        try {
            if ("estado".equals(accion)) {
                rutaDAO.actualizarEstado(entero(rq, "id"), texto(rq, "estado"));
                flash(rq, "ok", "Estado de la ruta actualizado.");
            } else if ("terminal".equals(accion)) {
                Terminal t = new Terminal();
                t.setNombre(texto(rq, "nombre"));
                t.setCiudad(texto(rq, "ciudad"));
                t.setDireccion(texto(rq, "direccion"));
                t.setTelefono(texto(rq, "telefono"));
                t.setCorreo(texto(rq, "correo"));
                t.setEstado("Activa");
                String error = validadorTerminal.validarTerminal(t);
                if (error != null) {
                    flash(rq, "error", error);
                } else {
                    terminalDAO.insertar(validadorTerminal.prepararParaRegistro(t));
                    flash(rq, "ok", "Terminal registrada.");
                }
            } else {
                Ruta r = new Ruta();
                r.setNombreRuta(texto(rq, "nombreRuta"));
                r.setTerminalOrigen(entero(rq, "terminalOrigen"));
                r.setTerminalDestino(entero(rq, "terminalDestino"));
                r.setDistanciaKm(decimal(rq, "distanciaKm"));
                r.setDuracionEstimada(hora(rq, "duracionEstimada"));
                r.setEstado("Activa");
                String error = validadorRuta.validarRuta(r);
                if (error != null) {
                    flash(rq, "error", error);
                } else {
                    rutaDAO.insertar(validadorRuta.prepararParaRegistro(r));
                    flash(rq, "ok", "Ruta registrada.");
                }
            }
            redirigir(rq, rs, "/rutas");
        } catch (SQLException e) {
            falloBD(rq, rs, e);
        }
    }
}
