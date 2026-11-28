package com.mycompany.mavenproject3.filtro;

import com.mycompany.mavenproject3.modelo.Empleado;
import com.mycompany.mavenproject3.util.Seguridad;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest rq = (HttpServletRequest) req;
        HttpServletResponse rs = (HttpServletResponse) res;
        rq.setCharacterEncoding("UTF-8");

        String ruta = rq.getRequestURI().substring(rq.getContextPath().length());
        HttpSession sesion = rq.getSession(true);
        if (sesion.getAttribute("csrf") == null) {
            sesion.setAttribute("csrf", Seguridad.tokenAleatorio());
        }

        boolean estatico = ruta.startsWith("/css/")
                || ruta.startsWith("/js/")
                || ruta.startsWith("/img/")
                || ruta.endsWith(".html");
        if (estatico) {
            chain.doFilter(req, res);
            return;
        }

        if (ruta.equals("/resources/login") && "POST".equalsIgnoreCase(rq.getMethod())) {
            chain.doFilter(req, res);
            return;
        }

        if (ruta.startsWith("/resources/")) {
            chain.doFilter(req, res);
            return;
        }

        if ("POST".equalsIgnoreCase(rq.getMethod())) {
            String token = rq.getParameter("_csrf");
            if (token == null || !token.equals(sesion.getAttribute("csrf"))) {
                rs.sendError(HttpServletResponse.SC_FORBIDDEN, "Token CSRF invalido. Recarga la pagina.");
                return;
            }
        }

        if (ruta.equals("/login")) {
            chain.doFilter(req, res);
            return;
        }

        Empleado usuario = (Empleado) sesion.getAttribute("usuario");
        if (usuario == null) {
            rs.sendRedirect(rq.getContextPath() + "/login");
            return;
        }

        boolean soloAdmin = ruta.startsWith("/auditoria")
                || (ruta.startsWith("/reportes") && !"boleto".equals(rq.getParameter("tipo")));
        if (soloAdmin && !"Administrador".equalsIgnoreCase(usuario.getCargo())) {
            rs.sendError(HttpServletResponse.SC_FORBIDDEN, "Esta seccion es solo para Administradores.");
            return;
        }
        rs.setHeader("Cache-Control", "no-store");
        chain.doFilter(req, res);
    }
}