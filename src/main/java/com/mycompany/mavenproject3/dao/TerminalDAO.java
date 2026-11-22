/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3.dao;
import com.mycompany.mavenproject3.modelo.Terminal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dell
 */

public class TerminalDAO {

    public List<Terminal> listarTodos() throws SQLException {
        List<Terminal> lista = new ArrayList<>();
        String sql = "SELECT IdTerminal, Nombre, Ciudad, Direccion, Telefono, Correo, Estado FROM Terminal";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Terminal t = new Terminal();
                t.setIdTerminal(rs.getInt("IdTerminal"));
                t.setNombre(rs.getString("Nombre"));
                t.setCiudad(rs.getString("Ciudad"));
                t.setDireccion(rs.getString("Direccion"));
                t.setTelefono(rs.getString("Telefono"));
                t.setCorreo(rs.getString("Correo"));
                t.setEstado(rs.getString("Estado"));
                lista.add(t);
            }
        }
        return lista;
    }

    public boolean insertar(Terminal terminal) throws SQLException {
        String sql = "INSERT INTO Terminal (Nombre, Ciudad, Direccion, Telefono, Correo, Estado) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, terminal.getNombre());
            ps.setString(2, terminal.getCiudad());
            ps.setString(3, terminal.getDireccion());
            ps.setString(4, terminal.getTelefono());
            ps.setString(5, terminal.getCorreo());
            ps.setString(6, terminal.getEstado() != null ? terminal.getEstado() : "Activa");

            return ps.executeUpdate() > 0;
        }
    }
}