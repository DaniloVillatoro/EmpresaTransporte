/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3.dao;
import com.mycompany.mavenproject3.modelo.Mantenimiento;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Dell
 */


public class MantenimientoDAO {

    public List<Mantenimiento> listarTodos() throws SQLException {
        List<Mantenimiento> lista = new ArrayList<>();
        String sql = "SELECT IdMantenimiento, IdBus, IdEmpleado, Fecha, Tipo, Descripcion, "
                   + "Kilometraje, Costo, Estado FROM Mantenimiento";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Mantenimiento m = new Mantenimiento();
                m.setIdMantenimiento(rs.getInt("IdMantenimiento"));
                m.setIdBus(rs.getInt("IdBus"));

                int idEmp = rs.getInt("IdEmpleado");
                m.setIdEmpleado(rs.wasNull() ? null : idEmp);

                Date fecha = rs.getDate("Fecha");
                m.setFecha(fecha != null ? fecha.toLocalDate() : null);

                m.setTipo(rs.getString("Tipo"));
                m.setDescripcion(rs.getString("Descripcion"));

                int km = rs.getInt("Kilometraje");
                m.setKilometraje(rs.wasNull() ? null : km);

                m.setCosto(rs.getBigDecimal("Costo"));
                m.setEstado(rs.getString("Estado"));
                lista.add(m);
            }
        }
        return lista;
    }

    public boolean insertar(Mantenimiento m) throws SQLException {
        String sql = "INSERT INTO Mantenimiento (IdBus, IdEmpleado, Fecha, Tipo, Descripcion, "
                   + "Kilometraje, Costo, Estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, m.getIdBus());

            if (m.getIdEmpleado() != null) {
                ps.setInt(2, m.getIdEmpleado());
            } else {
                ps.setNull(2, Types.INTEGER);
            }

            ps.setDate(3, Date.valueOf(m.getFecha()));
            ps.setString(4, m.getTipo());
            ps.setString(5, m.getDescripcion());

            if (m.getKilometraje() != null) {
                ps.setInt(6, m.getKilometraje());
            } else {
                ps.setNull(6, Types.INTEGER);
            }

            ps.setBigDecimal(7, m.getCosto() != null ? m.getCosto() : BigDecimal.ZERO);
            ps.setString(8, m.getEstado() != null ? m.getEstado() : "Pendiente");

            return ps.executeUpdate() > 0;
        }
    }
    
    public boolean cambiarEstado(int idMantenimiento, String nuevoEstado) throws SQLException {
        try (Connection conn = ConexionBD.obtenerConexion()) {
            conn.setAutoCommit(false);
            try {
                int filas;
                try (PreparedStatement ps = conn.prepareStatement(
                        "UPDATE Mantenimiento SET Estado = ? WHERE IdMantenimiento = ?")) {
                    ps.setString(1, nuevoEstado);
                    ps.setInt(2, idMantenimiento);
                    filas = ps.executeUpdate();
                }
                String estadoBus = "En proceso".equals(nuevoEstado) ? "Mantenimiento"
                                 : "Finalizado".equals(nuevoEstado) ? "Disponible" : null;
                if (estadoBus != null) {
                    try (PreparedStatement pb = conn.prepareStatement(
                            "UPDATE Bus SET Estado = ? WHERE IdBus = (SELECT IdBus FROM Mantenimiento WHERE IdMantenimiento = ?)")) {
                        pb.setString(1, estadoBus);
                        pb.setInt(2, idMantenimiento);
                        pb.executeUpdate();
                    }
                }
                conn.commit();
                return filas > 0;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }
}