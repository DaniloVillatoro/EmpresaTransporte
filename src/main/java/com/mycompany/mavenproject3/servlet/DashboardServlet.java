package com.mycompany.mavenproject3.servlet;

import com.mycompany.mavenproject3.dao.MetodoPagoDAO;
import com.mycompany.mavenproject3.dao.VistaDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/** Panel principal: indicadores, proximas salidas y formulario rapido de venta. */
@WebServlet("/panel")
public class DashboardServlet extends BaseServlet {

    private final VistaDAO vistaDAO = new VistaDAO();
    private final MetodoPagoDAO metodoPagoDAO = new MetodoPagoDAO();

    @Override
    protected void doGet(HttpServletRequest rq, HttpServletResponse rs) throws ServletException, IOException {
        try {
            rq.setAttribute("ind", vistaDAO.indicadores());
            rq.setAttribute("salidas", vistaDAO.proximasSalidas(6));
            rq.setAttribute("semana", vistaDAO.ventasSemana());
            rq.setAttribute("horariosVenta", vistaDAO.horariosEnVenta());
            rq.setAttribute("metodos", metodoPagoDAO.listarTodos());
            vista(rq, rs, "dashboard");
        } catch (SQLException e) {
            falloBD(rq, rs, e);
        }
    }
}
