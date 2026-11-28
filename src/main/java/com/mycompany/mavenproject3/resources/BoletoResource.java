/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3.resources;

import com.mycompany.mavenproject3.dao.BoletoDAO;
import com.mycompany.mavenproject3.dao.ClienteDAO;
import com.mycompany.mavenproject3.dao.VistaDAO;
import com.mycompany.mavenproject3.dao.VistaDAO.Fila;
import com.mycompany.mavenproject3.modelo.Boleto;
import com.mycompany.mavenproject3.modelo.Cliente;
import com.mycompany.mavenproject3.modelo.Empleado;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Path("boletos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BoletoResource {

    private final BoletoDAO boletoDAO = new BoletoDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final VistaDAO vistaDAO = new VistaDAO();

    public static class VentaRequest {
        public int idHorario;
        public int idCliente;
        public int idDisponibilidad;
        public Integer idAsiento;
        public BigDecimal precio;
        public String metodoPago;
        public String referencia;
    }

    public static class ClienteDTO {
        public int idCliente;
        public String nombre;
        public String apellido;
        public String identidad;
        public String estado;
    }

    @GET
    public Response listarDatosBoletos() {
        try {
            List<Fila> boletos = vistaDAO.boletos(100);
            List<Cliente> clientesRaw = clienteDAO.listarTodos();
            List<ClienteDTO> clientes = new ArrayList<>();

            for (Cliente c : clientesRaw) {
                ClienteDTO dto = new ClienteDTO();
                dto.idCliente = c.getIdCliente();
                dto.nombre = c.getNombre();
                dto.apellido = c.getApellido();
                dto.identidad = c.getIdentidad();
                dto.estado = c.getEstado();
                clientes.add(dto);
            }

            List<Fila> horarios = vistaDAO.horariosEnVenta();

            Map<String, Object> data = new HashMap<>();
            data.put("boletos", boletos);
            data.put("clientes", clientes);
            data.put("horarios", horarios);

            return Response.ok(data).build();
        } catch (SQLException e) {
            return errorJson(Response.Status.INTERNAL_SERVER_ERROR, "Error de BD al cargar boletos: " + e.getMessage());
        }
    }

    @GET
    @Path("asientos")
    public Response listarAsientos(@QueryParam("idHorario") int idHorario) {
        if (idHorario <= 0) {
            return errorJson(Response.Status.BAD_REQUEST, "Debe indicar un horario válido.");
        }
        try {
            List<Fila> asientos = vistaDAO.asientosDeHorario(idHorario);
            return Response.ok(asientos).build();
        } catch (SQLException e) {
            return errorJson(Response.Status.INTERNAL_SERVER_ERROR, "Error de BD al cargar asientos: " + e.getMessage());
        }
    }

    @POST
    public Response venderBoleto(VentaRequest req, @Context HttpServletRequest request) {
        int dispId = (req != null)
                ? (req.idDisponibilidad > 0 ? req.idDisponibilidad : (req.idAsiento != null ? req.idAsiento : 0))
                : 0;

        if (req == null || req.idCliente <= 0 || dispId <= 0 || req.precio == null || req.precio.compareTo(BigDecimal.ZERO) <= 0) {
            return errorJson(Response.Status.BAD_REQUEST, "Completa horario, cliente, asiento y un precio válido.");
        }

        try {
            Boleto b = new Boleto();
            String codigo = "BOL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            b.setCodigoBoleto(codigo);
            b.setIdCliente(req.idCliente);
            b.setIdDisponibilidad(dispId);

            Integer idEmpleado = null;
            if (request != null) {
                HttpSession sesion = request.getSession(false);
                if (sesion != null && sesion.getAttribute("usuario") instanceof Empleado) {
                    idEmpleado = ((Empleado) sesion.getAttribute("usuario")).getIdEmpleado();
                }
            }
            b.setIdEmpleado(idEmpleado);
            b.setFechaCompra(LocalDateTime.now());
            b.setPrecio(req.precio);
            b.setMetodoCompra(req.metodoPago != null && !req.metodoPago.isBlank() ? req.metodoPago.trim() : "Efectivo");
            b.setEstado("Pagado");

            boolean ok = boletoDAO.registrarBoletoConAsiento(b, "Ocupado");
            if (!ok) {
                return errorJson(Response.Status.BAD_REQUEST, "No se pudo emitir el boleto o actualizar el asiento.");
            }

            Map<String, Object> resp = new HashMap<>();
            resp.put("mensaje", "Boleto " + codigo + " emitido correctamente.");
            resp.put("codigoBoleto", codigo);
            return Response.status(Response.Status.CREATED).entity(resp).build();
        } catch (SQLException e) {
            return errorJson(Response.Status.INTERNAL_SERVER_ERROR, "Error de BD al emitir boleto: " + e.getMessage());
        }
    }

    private Response errorJson(Response.Status status, String mensaje) {
        Map<String, String> err = new HashMap<>();
        err.put("error", mensaje);
        return Response.status(status).entity(err).build();
    }
}