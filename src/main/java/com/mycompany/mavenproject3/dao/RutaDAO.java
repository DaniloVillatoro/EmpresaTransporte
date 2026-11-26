/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3.dao;
import com.mycompany.mavenproject3.modelo.Ruta;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Dell
 */


public class RutaDAO {

    public List<Ruta> listarTodas() throws SQLException {
        List<Ruta> lista = new ArrayList<>();
        String sql = "SELECT IdRuta, NombreRuta, TerminalOrigen, TerminalDestino, Distancia_Km, Duracion_Estimada, Estado FROM Ruta";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Ruta r = new Ruta();
                r.setIdRuta(rs.getInt("IdRuta"));
                r.setNombreRuta(rs.getString("NombreRuta"));
                r.setTerminalOrigen(rs.getInt("TerminalOrigen"));
                r.setTerminalDestino(rs.getInt("TerminalDestino"));
                r.setDistanciaKm(rs.getBigDecimal("Distancia_Km"));

                Time duracion = rs.getTime("Duracion_Estimada");
                r.setDuracionEstimada(duracion != null ? duracion.toLocalTime() : null);

                r.setEstado(rs.getString("Estado"));
                lista.add(r);
            }
        }
        return lista;
    }

    public boolean insertar(Ruta ruta) throws SQLException {
        String sql = "INSERT INTO Ruta (NombreRuta, TerminalOrigen, TerminalDestino, Distancia_Km, Duracion_Estimada, Estado) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, ruta.getNombreRuta());
            ps.setInt(2, ruta.getTerminalOrigen());
            ps.setInt(3, ruta.getTerminalDestino());
            ps.setBigDecimal(4, ruta.getDistanciaKm());
            ps.setTime(5, Time.valueOf(ruta.getDuracionEstimada()));
            ps.setString(6, ruta.getEstado() != null ? ruta.getEstado() : "Activa");

            return ps.executeUpdate() > 0;
        }
    }
    
    public boolean actualizarEstado(int id, String nuevoEstado) throws SQLException {
        String sql = "UPDATE Ruta SET Estado = ? WHERE IdRuta = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        }
    }
}