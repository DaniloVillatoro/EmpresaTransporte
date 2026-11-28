/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3.resources;

import com.mycompany.mavenproject3.dao.ClienteDAO;
import com.mycompany.mavenproject3.dao.ConexionBD;
import com.mycompany.mavenproject3.dao.TerminalDAO;
import com.mycompany.mavenproject3.dao.VistaDAO;
import com.mycompany.mavenproject3.modelo.Cliente;
import com.mycompany.mavenproject3.modelo.Terminal;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Path("operaciones")
@Produces(MediaType.APPLICATION_JSON)
public class OperacionesResource {

    private final VistaDAO vistaDAO = new VistaDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final TerminalDAO terminalDAO = new TerminalDAO();

    public static class TerminalItem {
        public int idTerminal;
        public String nombre;
        public String ciudad;
        public String direccion;
        public String telefono;
        public String correo;
        public String estado;
    }

    public static class ClienteItem {
        public int idCliente;
        public String nombre;
        public String apellido;
        public String identidad;
        public String telefono;
        public String correo;
        public String direccion;
        public String estado;
    }

    @GET
    public Response obtenerModulo(@QueryParam("modulo") String modulo) {
        String mod = modulo != null ? modulo.trim().toLowerCase() : "todos";
        Map<String, Object> data = new HashMap<>();

        try {
            switch (mod) {
                case "horarios":
                    data.put("horarios", vistaDAO.horarios());
                    data.put("rutas", vistaDAO.rutas());
                    data.put("buses", listarBuses());
                    data.put("conductores", vistaDAO.conductores());
                    break;
                case "rutas":
                    data.put("rutas", vistaDAO.rutas());
                    data.put("terminales", mapearTerminales());
                    break;
                case "terminales":
                    data.put("terminales", mapearTerminales());
                    break;
                case "buses":
                    data.put("buses", listarBuses());
                    break;
                case "conductores":
                    data.put("conductores", vistaDAO.conductores());
                    break;
                case "clientes":
                    data.put("clientes", mapearClientes());
                    break;
                case "pagos":
                    data.put("pagos", vistaDAO.pagos(100));
                    break;
                case "mantenimiento":
                case "mantenimientos":
                    data.put("mantenimientos", vistaDAO.mantenimientos());
                    data.put("buses", listarBuses());
                    break;
                default:
                    data.put("horarios", vistaDAO.horarios());
                    data.put("rutas", vistaDAO.rutas());
                    data.put("terminales", mapearTerminales());
                    data.put("buses", listarBuses());
                    data.put("conductores", vistaDAO.conductores());
                    data.put("clientes", mapearClientes());
                    data.put("pagos", vistaDAO.pagos(100));
                    data.put("mantenimientos", vistaDAO.mantenimientos());
                    break;
            }
            return Response.ok(data).build();
        } catch (SQLException e) {
            Map<String, String> err = new HashMap<>();
            err.put("error", "Error de BD en módulo " + mod + ": " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(err).build();
        }
    }

    private List<TerminalItem> mapearTerminales() throws SQLException {
        List<Terminal> raw = terminalDAO.listarTodos();
        List<TerminalItem> out = new ArrayList<>();
        for (Terminal t : raw) {
            TerminalItem item = new TerminalItem();
            item.idTerminal = t.getIdTerminal();
            item.nombre = t.getNombre();
            item.ciudad = t.getCiudad();
            item.direccion = t.getDireccion();
            item.telefono = t.getTelefono();
            item.correo = t.getCorreo();
            item.estado = t.getEstado();
            out.add(item);
        }
        return out;
    }

    private List listarBuses() throws SQLException {
        List out = new ArrayList();
        String sql = "SELECT idbus AS id, placa AS placa, modelo AS modelo, capacidad AS capacidad, estado AS estado "
                   + "FROM bus ORDER BY placa";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map b = new LinkedHashMap();
                b.put("id", rs.getObject("id"));
                b.put("placa", rs.getObject("placa"));
                b.put("modelo", rs.getObject("modelo"));
                b.put("capacidad", rs.getObject("capacidad"));
                b.put("estado", rs.getObject("estado"));
                out.add(b);
            }
        }
        return out;
    }

    private List<ClienteItem> mapearClientes() throws SQLException {
        List<Cliente> raw = clienteDAO.listarTodos();
        List<ClienteItem> out = new ArrayList<>();
        for (Cliente c : raw) {
            ClienteItem item = new ClienteItem();
            item.idCliente = c.getIdCliente();
            item.nombre = c.getNombre();
            item.apellido = c.getApellido();
            item.identidad = c.getIdentidad();
            item.telefono = c.getTelefono();
            item.correo = c.getCorreo();
            item.direccion = c.getDireccion();
            item.estado = c.getEstado();
            out.add(item);
        }
        return out;
    }
}