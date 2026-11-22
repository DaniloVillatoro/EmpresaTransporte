/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3.resources;
import com.mycompany.mavenproject3.controlador.BusControlador;
import com.mycompany.mavenproject3.dao.BusDAO;
import com.mycompany.mavenproject3.modelo.Bus;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 *
 * @author Dell
 */


@Path("buses")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BusResource {

    private final BusControlador controlador = new BusControlador();
    private final BusDAO busDAO = new BusDAO();

    // DTO simple para recibir/enviar JSON limpio desde el frontend
    public static class BusDTO {
        public int idBus;
        public String placa;
        public String marca;
        public String modelo;
        public Integer anio;
        public String tipoBus;
        public int capacidad;
        public String fechaAdquisicion;
        public String estado;
    }

    @GET
    public Response listarBuses() {
        try {
            List<Bus> buses = busDAO.listarTodos();
            List<BusDTO> salida = new ArrayList<>();

            for (Bus b : buses) {
                BusDTO dto = new BusDTO();
                dto.idBus = b.getIdBus();
                dto.placa = b.getPlaca();
                dto.marca = b.getMarca();
                dto.modelo = b.getModelo();
                dto.anio = b.getAnio();
                dto.tipoBus = b.getTipoBus();
                dto.capacidad = b.getCapacidad();
                dto.fechaAdquisicion = b.getFechaAdquisicion() != null ? b.getFechaAdquisicion().toString() : null;
                dto.estado = b.getEstado();
                salida.add(dto);
            }

            return Response.ok(salida).build();
        } catch (SQLException e) {
            return errorJson(Response.Status.INTERNAL_SERVER_ERROR, "Error de BD al listar buses: " + e.getMessage());
        }
    }

    @POST
    public Response crearBus(BusDTO dto) {
        if (dto == null) {
            return errorJson(Response.Status.BAD_REQUEST, "No se recibieron datos del bus.");
        }

        Bus bus = new Bus();
        bus.setPlaca(dto.placa);
        bus.setMarca(dto.marca);
        bus.setModelo(dto.modelo);
        bus.setAnio(dto.anio);
        bus.setTipoBus(dto.tipoBus);
        bus.setCapacidad(dto.capacidad);
        bus.setEstado(dto.estado);

        if (dto.fechaAdquisicion != null && !dto.fechaAdquisicion.trim().isEmpty()) {
            try {
                bus.setFechaAdquisicion(LocalDate.parse(dto.fechaAdquisicion.trim()));
            } catch (Exception ex) {
                return errorJson(Response.Status.BAD_REQUEST, "Formato de fecha inválido (usa YYYY-MM-DD).");
            }
        }

        String errorValidacion = controlador.validarBus(bus);
        if (errorValidacion != null) {
            return errorJson(Response.Status.BAD_REQUEST, errorValidacion);
        }

        bus = controlador.prepararParaRegistro(bus);

        try {
            boolean guardado = busDAO.insertar(bus);
            if (guardado) {
                Map<String, Object> resp = new HashMap<>();
                resp.put("mensaje", "Bus registrado correctamente.");
                resp.put("placa", bus.getPlaca());
                return Response.status(Response.Status.CREATED).entity(resp).build();
            }
            return errorJson(Response.Status.INTERNAL_SERVER_ERROR, "No se pudo insertar el bus.");
        } catch (SQLException e) {
            return errorJson(Response.Status.INTERNAL_SERVER_ERROR, "Error en SQL Server: " + e.getMessage());
        }
    }

    private Response errorJson(Response.Status status, String mensaje) {
        Map<String, String> err = new HashMap<>();
        err.put("error", mensaje);
        return Response.status(status).entity(err).build();
    }
}