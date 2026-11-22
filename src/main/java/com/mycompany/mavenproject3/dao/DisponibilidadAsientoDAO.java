/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3.dao;
import com.mycompany.mavenproject3.modelo.DisponibilidadAsiento;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Dell
 */


public class DisponibilidadAsientoDAO {

    public List<DisponibilidadAsiento> listarPorHorario(int idHorario) throws SQLException {
        List<DisponibilidadAsiento> lista = new ArrayList<>();
        String sql = "SELECT IdDisponibilidad, IdHorario, IdAsiento, Estado, FechaActualizacion "
                   + "FROM Disponibilidad_Asiento WHERE IdHorario = ?";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idHorario);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DisponibilidadAsiento d = new DisponibilidadAsiento();
                    d.setIdDisponibilidad(rs.getInt("IdDisponibilidad"));
                    d.setIdHorario(rs.getInt("IdHorario"));
                    d.setIdAsiento(rs.getInt("IdAsiento"));
                    d.setEstado(rs.getString("Estado"));

                    Timestamp ts = rs.getTimestamp("FechaActualizacion");
                    d.setFechaActualizacion(ts != null ? ts.toLocalDateTime() : null);

                    lista.add(d);
                }
            }
        }
        return lista;
    }

    public boolean insertar(DisponibilidadAsiento disp) throws SQLException {
        String sql = "INSERT INTO Disponibilidad_Asiento (IdHorario, IdAsiento, Estado, FechaActualizacion) "
                   + "VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, disp.getIdHorario());
            ps.setInt(2, disp.getIdAsiento());
            ps.setString(3, disp.getEstado() != null ? disp.getEstado() : "Disponible");
            ps.setTimestamp(4, Timestamp.valueOf(
                    disp.getFechaActualizacion() != null ? disp.getFechaActualizacion() : LocalDateTime.now()));

            return ps.executeUpdate() > 0;
        }
    }

    public boolean actualizarEstado(int idDisponibilidad, String nuevoEstado) throws SQLException {
        String sql = "UPDATE Disponibilidad_Asiento SET Estado = ?, FechaActualizacion = ? "
                   + "WHERE IdDisponibilidad = ?";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nuevoEstado);
            ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(3, idDisponibilidad);

            return ps.executeUpdate() > 0;
        }
    }
}