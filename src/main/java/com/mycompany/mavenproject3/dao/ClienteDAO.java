/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3.dao;
import com.mycompany.mavenproject3.modelo.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Dell
 */


public class ClienteDAO {

    public List<Cliente> listarTodos() throws SQLException {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT IdCliente, Nombre, Apellido, Identidad, Telefono, Correo, Direccion, FechaRegistro, Estado FROM Cliente";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Cliente c = new Cliente();
                c.setIdCliente(rs.getInt("IdCliente"));
                c.setNombre(rs.getString("Nombre"));
                c.setApellido(rs.getString("Apellido"));
                c.setIdentidad(rs.getString("Identidad"));
                c.setTelefono(rs.getString("Telefono"));
                c.setCorreo(rs.getString("Correo"));
                c.setDireccion(rs.getString("Direccion"));

                Timestamp ts = rs.getTimestamp("FechaRegistro");
                c.setFechaRegistro(ts != null ? ts.toLocalDateTime() : null);

                c.setEstado(rs.getString("Estado"));
                lista.add(c);
            }
        }
        return lista;
    }

    public boolean insertar(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO Cliente (Nombre, Apellido, Identidad, Telefono, Correo, Direccion, FechaRegistro, Estado) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getApellido());
            ps.setString(3, cliente.getIdentidad());
            ps.setString(4, cliente.getTelefono());
            ps.setString(5, cliente.getCorreo());
            ps.setString(6, cliente.getDireccion());

            if (cliente.getFechaRegistro() != null) {
                ps.setTimestamp(7, Timestamp.valueOf(cliente.getFechaRegistro()));
            } else {
                ps.setTimestamp(7, Timestamp.valueOf(java.time.LocalDateTime.now()));
            }

            ps.setString(8, cliente.getEstado() != null ? cliente.getEstado() : "Activo");

            return ps.executeUpdate() > 0;
        }
    }
    
     public boolean actualizarEstado(int id, String nuevoEstado) throws SQLException {
        String sql = "UPDATE Cliente SET Estado = ? WHERE IdCliente = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        }
    }
}
