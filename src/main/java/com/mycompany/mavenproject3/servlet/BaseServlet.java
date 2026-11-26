package com.mycompany.mavenproject3.servlet;

import com.mycompany.mavenproject3.modelo.Empleado;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase base de los CONTROLADORES (Servlets) del patron MVC.
 * Reune lo comun: mostrar una vista, mensajes flash, redirecciones y lectura de parametros.
 */
public abstract class BaseServlet extends HttpServlet {

    protected static final Logger LOG = Logger.getLogger("TransporteHN");

    /** Muestra un JSP de /WEB-INF/jsp/ (el usuario nunca accede a el directamente). */
    protected void vista(HttpServletRequest rq, HttpServletResponse rs, String jsp)
            throws ServletException, IOException {
        HttpSession s = rq.getSession();
        Object msg = s.getAttribute("flashMsg");
        if (msg != null) {
            rq.setAttribute("flashMsg", msg);
            rq.setAttribute("flashTipo", s.getAttribute("flashTipo"));
            s.removeAttribute("flashMsg");
            s.removeAttribute("flashTipo");
        }
        Object ultimo = s.getAttribute("ultimoBoleto");
        if (ultimo != null) {                       // para mostrar el enlace "Imprimir boleto" una sola vez
            rq.setAttribute("ultimoBoleto", ultimo);
            s.removeAttribute("ultimoBoleto");
        }
        rq.getRequestDispatcher("/WEB-INF/jsp/" + jsp + ".jsp").forward(rq, rs);
    }

    /** Mensaje que se mostrara en la siguiente pagina (patron Post-Redirect-Get). */
    protected void flash(HttpServletRequest rq, String tipo, String mensaje) {
        rq.getSession().setAttribute("flashTipo", tipo);   // ok | error
        rq.getSession().setAttribute("flashMsg", mensaje);
    }

    protected void redirigir(HttpServletRequest rq, HttpServletResponse rs, String ruta) throws IOException {
        rs.sendRedirect(rq.getContextPath() + ruta);
    }

    /** Error de base de datos: se registra en el log y se muestra una pantalla amigable. */
    protected void falloBD(HttpServletRequest rq, HttpServletResponse rs, Exception e)
            throws ServletException, IOException {
        LOG.log(Level.SEVERE, "Error de base de datos", e);
        rq.setAttribute("detalleError", e.getMessage());
        rq.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(rq, rs);
    }

    protected Empleado usuario(HttpServletRequest rq) {
        return (Empleado) rq.getSession().getAttribute("usuario");
    }

    // ---- lectura segura de parametros ----
    protected String texto(HttpServletRequest rq, String nombre) {
        String v = rq.getParameter(nombre);
        return (v == null || v.isBlank()) ? null : v.trim();
    }

    protected int entero(HttpServletRequest rq, String nombre) {
        String v = texto(rq, nombre);
        if (v == null) {
            return 0;
        }
        try {
            return Integer.parseInt(v);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    protected BigDecimal decimal(HttpServletRequest rq, String nombre) {
        String v = texto(rq, nombre);
        if (v == null) {
            return null;
        }
        try {
            return new BigDecimal(v.replace(",", ""));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    protected LocalDate fecha(HttpServletRequest rq, String nombre) {
        String v = texto(rq, nombre);
        try {
            return v == null ? null : LocalDate.parse(v);
        } catch (Exception e) {
            return null;
        }
    }

    protected LocalTime hora(HttpServletRequest rq, String nombre) {
        String v = texto(rq, nombre);
        try {
            return v == null ? null : LocalTime.parse(v);
        } catch (Exception e) {
            return null;
        }
    }
}
