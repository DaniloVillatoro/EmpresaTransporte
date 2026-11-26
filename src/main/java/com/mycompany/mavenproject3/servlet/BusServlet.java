package com.mycompany.mavenproject3.servlet;

import com.mycompany.mavenproject3.controlador.BusControlador;
import com.mycompany.mavenproject3.dao.BusDAO;
import com.mycompany.mavenproject3.modelo.Bus;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/buses")
public class BusServlet extends BaseServlet {

    private final BusDAO busDAO = new BusDAO();
    private final BusControlador validador = new BusControlador();

    @Override
    protected void doGet(HttpServletRequest rq, HttpServletResponse rs) throws ServletException, IOException {
        try {
            rq.setAttribute("buses", busDAO.listarTodos());
            vista(rq, rs, "buses");
        } catch (SQLException e) {
            falloBD(rq, rs, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest rq, HttpServletResponse rs) throws ServletException, IOException {
        try {
            if ("estado".equals(texto(rq, "accion"))) {
                busDAO.actualizarEstado(entero(rq, "id"), texto(rq, "estado"));
                flash(rq, "ok", "Estado del bus actualizado.");
                redirigir(rq, rs, "/buses");
                return;
            }
            Bus b = new Bus();
            b.setPlaca(texto(rq, "placa") == null ? null : texto(rq, "placa").toUpperCase());
            b.setMarca(texto(rq, "marca"));
            b.setModelo(texto(rq, "modelo"));
            int anio = entero(rq, "anio");
            b.setAnio(anio > 0 ? anio : null);
            b.setTipoBus(texto(rq, "tipoBus"));
            b.setCapacidad(entero(rq, "capacidad"));
            b.setFechaAdquisicion(fecha(rq, "fechaAdquisicion"));
            b.setEstado("Disponible");
            String error = validador.validarBus(b);
            if (error != null) {
                flash(rq, "error", error);
            } else {
                busDAO.insertarConAsientos(validador.prepararParaRegistro(b));
                flash(rq, "ok", "Bus registrado con sus " + b.getCapacidad() + " asientos.");
            }
            redirigir(rq, rs, "/buses");
        } catch (SQLException e) {
            falloBD(rq, rs, e);
        }
    }
}
