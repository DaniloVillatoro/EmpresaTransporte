package com.mycompany.mavenproject3.servlet;

import com.mycompany.mavenproject3.controlador.MantenimientoControlador;
import com.mycompany.mavenproject3.dao.BusDAO;
import com.mycompany.mavenproject3.dao.MantenimientoDAO;
import com.mycompany.mavenproject3.dao.VistaDAO;
import com.mycompany.mavenproject3.modelo.Mantenimiento;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/mantenimiento")
public class MantenimientoServlet extends BaseServlet {

    private final MantenimientoDAO dao = new MantenimientoDAO();
    private final BusDAO busDAO = new BusDAO();
    private final VistaDAO vistaDAO = new VistaDAO();
    private final MantenimientoControlador validador = new MantenimientoControlador();

    @Override
    protected void doGet(HttpServletRequest rq, HttpServletResponse rs) throws ServletException, IOException {
        try {
            rq.setAttribute("mantenimientos", vistaDAO.mantenimientos());
            rq.setAttribute("buses", busDAO.listarTodos());
            vista(rq, rs, "mantenimiento");
        } catch (SQLException e) {
            falloBD(rq, rs, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest rq, HttpServletResponse rs) throws ServletException, IOException {
        try {
            if ("estado".equals(texto(rq, "accion"))) {
                dao.cambiarEstado(entero(rq, "id"), texto(rq, "estado"));
                flash(rq, "ok", "Mantenimiento actualizado y estado del bus sincronizado.");
            } else {
                Mantenimiento m = new Mantenimiento();
                m.setIdBus(entero(rq, "idBus"));
                m.setIdEmpleado(usuario(rq).getIdEmpleado());
                m.setFecha(fecha(rq, "fecha"));
                m.setTipo(texto(rq, "tipo"));
                m.setDescripcion(texto(rq, "descripcion"));
                int km = entero(rq, "kilometraje");
                m.setKilometraje(km > 0 ? km : null);
                m.setCosto(decimal(rq, "costo"));
                m.setEstado("En proceso");
                String error = validador.validarMantenimiento(m);
                if (error != null) {
                    flash(rq, "error", error);
                } else {
                    dao.insertar(validador.prepararParaRegistro(m));
                    busDAO.actualizarEstado(m.getIdBus(), "Mantenimiento");
                    flash(rq, "ok", "Mantenimiento registrado. El bus quedó fuera de servicio.");
                }
            }
            redirigir(rq, rs, "/mantenimiento");
        } catch (SQLException e) {
            falloBD(rq, rs, e);
        }
    }
}
