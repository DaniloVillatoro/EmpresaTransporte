/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3.dao;
import com.mycompany.mavenproject3.modelo.Horario;
import java.sql.Connection;
import java.sql.Date;
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


public class HorarioDAO {

    public List<Horario> listarTodos() throws SQLException {
        List<Horario> lista = new ArrayList<>();
        String sql = "SELECT IdHorario, IdRuta, IdBus, IdConductor, FechaSalida, HoraSalida, FechaLlegada, HoraLlegada, Estado FROM Horario";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Horario h = new Horario();
                h.setIdHorario(rs.getInt("IdHorario"));
                h.setIdRuta(rs.getInt("IdRuta"));
                h.setIdBus(rs.getInt("IdBus"));
                h.setIdConductor(rs.getInt("IdConductor"));

                Date fSalida = rs.getDate("FechaSalida");
                h.setFechaSalida(fSalida != null ? fSalida.toLocalDate() : null);

                Time hSalida = rs.getTime("HoraSalida");
                h.setHoraSalida(hSalida != null ? hSalida.toLocalTime() : null);

                Date fLlegada = rs.getDate("FechaLlegada");
                h.setFechaLlegada(fLlegada != null ? fLlegada.toLocalDate() : null);

                Time hLlegada = rs.getTime("HoraLlegada");
                h.setHoraLlegada(hLlegada != null ? hLlegada.toLocalTime() : null);

                h.setEstado(rs.getString("Estado"));
                lista.add(h);
            }
        }
        return lista;
    }

    public boolean insertar(Horario horario) throws SQLException {
        String sql = "INSERT INTO Horario (IdRuta, IdBus, IdConductor, FechaSalida, HoraSalida, FechaLlegada, HoraLlegada, Estado) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, horario.getIdRuta());
            ps.setInt(2, horario.getIdBus());
            ps.setInt(3, horario.getIdConductor());
            ps.setDate(4, Date.valueOf(horario.getFechaSalida()));
            ps.setTime(5, Time.valueOf(horario.getHoraSalida()));
            ps.setDate(6, Date.valueOf(horario.getFechaLlegada()));
            ps.setTime(7, Time.valueOf(horario.getHoraLlegada()));
            ps.setString(8, horario.getEstado() != null ? horario.getEstado() : "Programado");

            return ps.executeUpdate() > 0;
        }
    }
    
    public boolean actualizarEstado(int id, String nuevoEstado) throws SQLException {
        String sql = "UPDATE Horario SET Estado = ? WHERE IdHorario = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Inserta el horario y, en la MISMA transaccion, crea la disponibilidad de
     * cada asiento activo del bus (es lo que luego se vende en taquilla).
     * @return el IdHorario generado
     */
    public int insertarConDisponibilidad(Horario h) throws SQLException {
        String sqlH = "INSERT INTO Horario (IdRuta, IdBus, IdConductor, FechaSalida, HoraSalida, FechaLlegada, HoraLlegada, Estado) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlD = "INSERT INTO Disponibilidad_Asiento (IdHorario, IdAsiento, Estado, FechaActualizacion) "
                    + "SELECT ?, IdAsiento, 'Disponible', GETDATE() FROM Asiento WHERE IdBus = ? AND Estado = 'Disponible'";
        try (Connection conn = ConexionBD.obtenerConexion()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sqlH, java.sql.Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, h.getIdRuta());
                ps.setInt(2, h.getIdBus());
                ps.setInt(3, h.getIdConductor());
                ps.setDate(4, java.sql.Date.valueOf(h.getFechaSalida()));
                ps.setTime(5, java.sql.Time.valueOf(h.getHoraSalida()));
                ps.setDate(6, java.sql.Date.valueOf(h.getFechaLlegada()));
                ps.setTime(7, java.sql.Time.valueOf(h.getHoraLlegada()));
                ps.setString(8, h.getEstado() != null ? h.getEstado() : "Programado");
                ps.executeUpdate();
                int id;
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (!keys.next()) {
                        throw new SQLException("No se obtuvo el IdHorario generado.");
                    }
                    id = keys.getInt(1);
                }
                try (PreparedStatement pd = conn.prepareStatement(sqlD)) {
                    pd.setInt(1, id);
                    pd.setInt(2, h.getIdBus());
                    pd.executeUpdate();
                }
                conn.commit();
                return id;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }
}