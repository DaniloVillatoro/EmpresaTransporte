package com.mycompany.mavenproject3.servlet;

import com.mycompany.mavenproject3.controlador.ConductorControlador;
import com.mycompany.mavenproject3.dao.ConductorDAO;
import com.mycompany.mavenproject3.modelo.Conductor;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

@WebServlet("/conductores")
public class ConductorServlet extends BaseServlet {

    private final ConductorDAO dao = new ConductorDAO();
    private final ConductorControlador validador = new ConductorControlador();

    @Override
    protected void doGet(HttpServletRequest rq, HttpServletResponse rs) throws ServletException, IOException {
        try {
            rq.setAttribute("conductores", dao.listarTodos());
            rq.setAttribute("hoy", LocalDate.now());
            vista(rq, rs, "conductores");
        } catch (SQLException e) {
            falloBD(rq, rs, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest rq, HttpServletResponse rs) throws ServletException, IOException {
        try {
            if ("estado".equals(texto(rq, "accion"))) {
                dao.actualizarEstado(entero(rq, "id"), texto(rq, "estado"));
                flash(rq, "ok", "Estado del conductor actualizado.");
            } else {
                Conductor c = new Conductor();
                c.setNombre(texto(rq, "nombre"));
                c.setApellido(texto(rq, "apellido"));
                c.setIdentidad(texto(rq, "identidad"));
                c.setTelefono(texto(rq, "telefono"));
                c.setDireccion(texto(rq, "direccion"));
                c.setLicencia(texto(rq, "licencia"));
                c.setFechaVencimientoLicencia(fecha(rq, "fechaVencimientoLicencia"));
                c.setFechaContratacion(fecha(rq, "fechaContratacion"));
                c.setEstado("Activo");
                String error = validador.validarConductor(c);
                if (error != null) {
                    flash(rq, "error", error);
                } else {
                    dao.insertar(validador.prepararParaRegistro(c));
                    flash(rq, "ok", "Conductor registrado.");
                }
            }
            redirigir(rq, rs, "/conductores");
        } catch (SQLException e) {
            falloBD(rq, rs, e);
        }
    }
}
