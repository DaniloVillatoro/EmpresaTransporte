package com.mycompany.mavenproject3.servlet;

import com.mycompany.mavenproject3.dao.MetodoPagoDAO;
import com.mycompany.mavenproject3.dao.VistaDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/** Pagos (solo consulta: los pagos se generan al vender o anular un boleto). */
@WebServlet("/pagos")
public class PagoServlet extends BaseServlet {

    private final VistaDAO vistaDAO = new VistaDAO();
    private final MetodoPagoDAO metodoDAO = new MetodoPagoDAO();

    @Override
    protected void doGet(HttpServletRequest rq, HttpServletResponse rs) throws ServletException, IOException {
        try {
            rq.setAttribute("pagos", vistaDAO.pagos(300));
            rq.setAttribute("metodos", metodoDAO.listarTodos());
            vista(rq, rs, "pagos");
        } catch (SQLException e) {
            falloBD(rq, rs, e);
        }
    }
}
