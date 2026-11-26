package com.mycompany.mavenproject3.servlet;

import com.mycompany.mavenproject3.controlador.ConductorControlador;
import com.mycompany.mavenproject3.controlador.HorarioControlador;
import com.mycompany.mavenproject3.dao.BusDAO;
import com.mycompany.mavenproject3.dao.ConductorDAO;
import com.mycompany.mavenproject3.dao.HorarioDAO;
import com.mycompany.mavenproject3.dao.VistaDAO;
import com.mycompany.mavenproject3.modelo.Conductor;
import com.mycompany.mavenproject3.modelo.Horario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/horarios")
public class HorarioServlet extends BaseServlet {

    private final HorarioDAO horarioDAO = new HorarioDAO();
    private final VistaDAO vistaDAO = new VistaDAO();
    private final BusDAO busDAO = new BusDAO();
    private final ConductorDAO conductorDAO = new ConductorDAO();
    private final HorarioControlador validador = new HorarioControlador();
    private final ConductorControlador validadorConductor = new ConductorControlador();

    @Override
    protected void doGet(HttpServletRequest rq, HttpServletResponse rs) throws ServletException, IOException {
        try {
            rq.setAttribute("horarios", vistaDAO.horarios());
            rq.setAttribute("rutas", vistaDAO.rutas());
            rq.setAttribute("buses", busDAO.listarTodos());
            rq.setAttribute("conductores", conductorDAO.listarTodos());
            vista(rq, rs, "horarios");
        } catch (SQLException e) {
            falloBD(rq, rs, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest rq, HttpServletResponse rs) throws ServletException, IOException {
        try {
            if ("estado".equals(texto(rq, "accion"))) {
                horarioDAO.actualizarEstado(entero(rq, "id"), texto(rq, "estado"));
                flash(rq, "ok", "Estado del horario actualizado.");
                redirigir(rq, rs, "/horarios");
                return;
            }
            Horario h = new Horario();
            h.setIdRuta(entero(rq, "idRuta"));
            h.setIdBus(entero(rq, "idBus"));
            h.setIdConductor(entero(rq, "idConductor"));
            h.setFechaSalida(fecha(rq, "fechaSalida"));
            h.setHoraSalida(hora(rq, "horaSalida"));
            h.setFechaLlegada(fecha(rq, "fechaLlegada"));
            h.setHoraLlegada(hora(rq, "horaLlegada"));
            h.setEstado("Programado");

            String error = (h.getFechaSalida() == null || h.getHoraSalida() == null
                    || h.getFechaLlegada() == null || h.getHoraLlegada() == null)
                    ? "Completa fecha y hora de salida y de llegada." : validador.validarHorario(h);
            if (error == null) {
                Conductor c = conductorDAO.listarTodos().stream()
                        .filter(x -> x.getIdConductor() == h.getIdConductor()).findFirst().orElse(null);
                if (c != null && !validadorConductor.tieneLicenciaVigente(c, h.getFechaSalida())) {
                    error = "La licencia del conductor está vencida para esa fecha.";
                } else if (vistaDAO.hayConflicto(h.getIdBus(), h.getIdConductor(), h.getFechaSalida(), h.getHoraSalida())) {
                    error = "El bus o el conductor ya tienen un viaje a esa misma hora.";
                }
            }
            if (error != null) {
                flash(rq, "error", error);
            } else {
                horarioDAO.insertarConDisponibilidad(validador.prepararParaRegistro(h));
                flash(rq, "ok", "Horario programado y asientos generados.");
            }
            redirigir(rq, rs, "/horarios");
        } catch (SQLException e) {
            falloBD(rq, rs, e);
        }
    }
}
