/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3.resources;

import com.mycompany.mavenproject3.dao.EmpleadoDAO;
import com.mycompany.mavenproject3.modelo.Empleado;
import com.mycompany.mavenproject3.util.Seguridad;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.sql.SQLException;

@Path("login")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LoginResource {

    private final EmpleadoDAO empleadoDAO = new EmpleadoDAO();

    public static class LoginRequest {
        public String usuario;
        public String clave;
    }

    public static class LoginResponse {
        public int idEmpleado;
        public String usuario;
        public String nombre;
        public String apellido;
        public String cargo;
    }

    public static class ErrorResponse {
        public String error;

        public ErrorResponse(String error) {
            this.error = error;
        }
    }

    @POST
    public Response login(LoginRequest req, @Context HttpServletRequest request) {
        String usuario = (req != null && req.usuario != null) ? req.usuario.trim() : null;
        String clave = (req != null) ? req.clave : null;

        if (usuario == null || usuario.isEmpty() || clave == null || clave.isEmpty()) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new ErrorResponse("Usuario o contraseña incorrectos."))
                    .build();
        }

        try {
            Empleado e = empleadoDAO.buscarPorUsuario(usuario);
            boolean ok = e != null
                    && "Activo".equalsIgnoreCase(e.getEstado())
                    && Seguridad.verificar(clave, e.getContrasenaHash());

            if (!ok) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(new ErrorResponse("Usuario o contraseña incorrectos."))
                        .build();
            }

            HttpSession sesion = request.getSession(true);
            e.setContrasenaHash(null);
            sesion.setAttribute("usuario", e);

            LoginResponse resp = new LoginResponse();
            resp.idEmpleado = e.getIdEmpleado();
            resp.usuario = e.getUsuario() != null ? e.getUsuario() : "";
            resp.nombre = e.getNombre() != null ? e.getNombre() : "";
            resp.apellido = e.getApellido() != null ? e.getApellido() : "";
            resp.cargo = e.getCargo() != null ? e.getCargo() : "";

            return Response.ok(resp).build();
        } catch (SQLException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Error de base de datos al iniciar sesión."))
                    .build();
        }
    }
}