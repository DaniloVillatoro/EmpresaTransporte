package com.mycompany.mavenproject3.servlet;

import com.mycompany.mavenproject3.controlador.ClienteControlador;
import com.mycompany.mavenproject3.dao.ClienteDAO;
import com.mycompany.mavenproject3.modelo.Cliente;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/clientes")
public class ClienteServlet extends BaseServlet {

    private final ClienteDAO dao = new ClienteDAO();
    private final ClienteControlador validador = new ClienteControlador();

    @Override
    protected void doGet(HttpServletRequest rq, HttpServletResponse rs) throws ServletException, IOException {
        try {
            rq.setAttribute("clientes", dao.listarTodos());
            vista(rq, rs, "clientes");
        } catch (SQLException e) {
            falloBD(rq, rs, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest rq, HttpServletResponse rs) throws ServletException, IOException {
        try {
            if ("estado".equals(texto(rq, "accion"))) {
                dao.actualizarEstado(entero(rq, "id"), texto(rq, "estado"));
                flash(rq, "ok", "Estado del cliente actualizado.");
            } else {
                Cliente c = new Cliente();
                c.setNombre(texto(rq, "nombre"));
                c.setApellido(texto(rq, "apellido"));
                c.setIdentidad(texto(rq, "identidad"));
                c.setTelefono(texto(rq, "telefono"));
                c.setCorreo(texto(rq, "correo"));
                c.setDireccion(texto(rq, "direccion"));
                c.setEstado("Activo");
                String error = validador.validarCliente(c);
                if (error != null) {
                    flash(rq, "error", error);
                } else {
                    dao.insertar(validador.prepararParaRegistro(c));
                    flash(rq, "ok", "Cliente registrado.");
                }
            }
            redirigir(rq, rs, "/clientes");
        } catch (SQLException e) {
            falloBD(rq, rs, e);
        }
    }
}
