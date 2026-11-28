/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3.resources;

import com.mycompany.mavenproject3.dao.VistaDAO;
import com.mycompany.mavenproject3.dao.VistaDAO.Fila;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("dashboard")
@Produces(MediaType.APPLICATION_JSON)
public class DashboardResource {

    private final VistaDAO vistaDAO = new VistaDAO();

    @GET
    public Response obtenerPanel() {
        try {
            Fila ind = vistaDAO.indicadores();
            List<Fila> salidas = vistaDAO.proximasSalidas(6);
            List<Fila> semana = vistaDAO.ventasSemana();
            List<Fila> horariosVenta = vistaDAO.horariosEnVenta();

            Map<String, Object> payload = new HashMap<>();
            payload.put("indicadores", ind);
            payload.put("salidas", salidas);
            payload.put("semana", semana);
            payload.put("horariosVenta", horariosVenta);

            return Response.ok(payload).build();
        } catch (SQLException e) {
            Map<String, String> err = new HashMap<>();
            err.put("error", "Error de BD al cargar el panel: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(err).build();
        }
    }
}