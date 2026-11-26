package com.mycompany.mavenproject3.servlet;

import com.mycompany.mavenproject3.dao.EmpleadoDAO;
import com.mycompany.mavenproject3.modelo.Empleado;
import com.mycompany.mavenproject3.util.Seguridad;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(urlPatterns = {"/login", "/logout"})
public class LoginServlet extends BaseServlet {

    private final EmpleadoDAO empleadoDAO = new EmpleadoDAO();

    @Override
    protected void doGet(HttpServletRequest rq, HttpServletResponse rs) throws ServletException, IOException {
        if (rq.getServletPath().equals("/logout")) {
            rq.getSession().invalidate();
            redirigir(rq, rs, "/login");
            return;
        }
        if (usuario(rq) != null) {
            redirigir(rq, rs, "/panel");
            return;
        }
        rq.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(rq, rs);
    }

    @Override
    protected void doPost(HttpServletRequest rq, HttpServletResponse rs) throws ServletException, IOException {
        String user = texto(rq, "usuario");
        String clave = rq.getParameter("clave");
        try {
            Empleado e = user == null ? null : empleadoDAO.buscarPorUsuario(user);
            boolean ok = e != null && "Activo".equalsIgnoreCase(e.getEstado())
                    && Seguridad.verificar(clave, e.getContrasenaHash());
            if (!ok) {
                rq.setAttribute("error", "Usuario o contraseña incorrectos.");
                rq.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(rq, rs);
                return;
            }
            rq.changeSessionId();                       // evita fijacion de sesion
            e.setContrasenaHash(null);                  // no guardar el hash en la sesion
            rq.getSession().setAttribute("usuario", e);
            redirigir(rq, rs, "/panel");
        } catch (SQLException ex) {
            falloBD(rq, rs, ex);
        }
    }
}
