package com.mycompany.mavenproject3.servlet;

import com.mycompany.mavenproject3.dao.AuditoriaDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/** Bitacora de cambios (solo Administrador; la llena la propia base de datos con triggers). */
@WebServlet("/auditoria")
public class AuditoriaServlet extends BaseServlet {

    private final AuditoriaDAO dao = new AuditoriaDAO();

    @Override
    protected void doGet(HttpServletRequest rq, HttpServletResponse rs) throws ServletException, IOException {
        try {
            int limite = entero(rq, "limite");
            rq.setAttribute("registros", dao.listarRecientes(limite > 0 ? Math.min(limite, 1000) : 100));
            vista(rq, rs, "auditoria");
        } catch (SQLException e) {
            falloBD(rq, rs, e);
        }
    }
}
