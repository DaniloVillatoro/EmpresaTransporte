/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3.dao;
import com.mycompany.mavenproject3.modelo.Boleto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dell
 */

public class BoletoDAO {

    public List<Boleto> listarTodos() throws SQLException {
        List<Boleto> lista = new ArrayList<>();
        String sql = "SELECT IdBoleto, CodigoBoleto, IdCliente, IdDisponibilidad, IdEmpleado, "
                   + "FechaCompra, Precio, MetodoCompra, Estado FROM Boleto";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Boleto b = new Boleto();
                b.setIdBoleto(rs.getInt("IdBoleto"));
                b.setCodigoBoleto(rs.getString("CodigoBoleto"));
                b.setIdCliente(rs.getInt("IdCliente"));
                b.setIdDisponibilidad(rs.getInt("IdDisponibilidad"));

                int idEmp = rs.getInt("IdEmpleado");
                b.setIdEmpleado(rs.wasNull() ? null : idEmp);

                Timestamp ts = rs.getTimestamp("FechaCompra");
                b.setFechaCompra(ts != null ? ts.toLocalDateTime() : null);

                b.setPrecio(rs.getBigDecimal("Precio"));
                b.setMetodoCompra(rs.getString("MetodoCompra"));
                b.setEstado(rs.getString("Estado"));
                lista.add(b);
            }
        }
        return lista;
    }

    public boolean registrarBoletoConAsiento(Boleto boleto, String nuevoEstadoAsiento) throws SQLException {
        String sqlBoleto = "INSERT INTO Boleto (CodigoBoleto, IdCliente, IdDisponibilidad, IdEmpleado, "
                       + "FechaCompra, Precio, MetodoCompra, Estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlDisp = "UPDATE Disponibilidad_Asiento SET Estado = ?, FechaActualizacion = ? "
                       + "WHERE IdDisponibilidad = ?";

        Connection conn = null;
        try {
            conn = ConexionBD.obtenerConexion();
            conn.setAutoCommit(false);

            try (PreparedStatement psBoleto = conn.prepareStatement(sqlBoleto);
                 PreparedStatement psDisp = conn.prepareStatement(sqlDisp)) {

                psBoleto.setString(1, boleto.getCodigoBoleto());
                psBoleto.setInt(2, boleto.getIdCliente());
                psBoleto.setInt(3, boleto.getIdDisponibilidad());

                if (boleto.getIdEmpleado() != null) {
                    psBoleto.setInt(4, boleto.getIdEmpleado());
                } else {
                    psBoleto.setNull(4, Types.INTEGER);
                }

                psBoleto.setTimestamp(5, Timestamp.valueOf(
                        boleto.getFechaCompra() != null ? boleto.getFechaCompra() : LocalDateTime.now()));
                psBoleto.setBigDecimal(6, boleto.getPrecio());
                psBoleto.setString(7, boleto.getMetodoCompra() != null ? boleto.getMetodoCompra() : "Taquilla");
                psBoleto.setString(8, boleto.getEstado() != null ? boleto.getEstado() : "Pendiente");

                int filasBoleto = psBoleto.executeUpdate();

                psDisp.setString(1, nuevoEstadoAsiento != null ? nuevoEstadoAsiento : "Reservado");
                psDisp.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                psDisp.setInt(3, boleto.getIdDisponibilidad());

                int filasDisp = psDisp.executeUpdate();

                if (filasBoleto > 0 && filasDisp > 0) {
                    conn.commit();
                    return true;
                } else {
                    conn.rollback();
                    return false;
                }
            }
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    e.addSuppressed(ex);
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }
}