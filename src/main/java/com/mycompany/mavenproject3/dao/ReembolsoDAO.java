/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3.dao;
import com.mycompany.mavenproject3.modelo.Reembolso;
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


public class ReembolsoDAO {

    public List<Reembolso> listarTodos() throws SQLException {
        List<Reembolso> lista = new ArrayList<>();
        String sql = "SELECT IdReembolso, IdPago, IdEmpleado, IdMetodoPago, FechaReembolso, Monto, Motivo, Estado FROM Reembolso";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Reembolso r = new Reembolso();
                r.setIdReembolso(rs.getInt("IdReembolso"));
                r.setIdPago(rs.getInt("IdPago"));
                r.setIdEmpleado(rs.getInt("IdEmpleado"));
                r.setIdMetodoPago(rs.getInt("IdMetodoPago"));

                Timestamp ts = rs.getTimestamp("FechaReembolso");
                r.setFechaReembolso(ts != null ? ts.toLocalDateTime() : null);

                r.setMonto(rs.getBigDecimal("Monto"));
                r.setMotivo(rs.getString("Motivo"));
                r.setEstado(rs.getString("Estado"));
                lista.add(r);
            }
        }
        return lista;
    }

    public boolean insertar(Reembolso reembolso) throws SQLException {
        String sql = "INSERT INTO Reembolso (IdPago, IdEmpleado, IdMetodoPago, FechaReembolso, Monto, Motivo, Estado) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, reembolso.getIdPago());
            ps.setInt(2, reembolso.getIdEmpleado());
            ps.setInt(3, reembolso.getIdMetodoPago());
            ps.setTimestamp(4, Timestamp.valueOf(
                    reembolso.getFechaReembolso() != null ? reembolso.getFechaReembolso() : LocalDateTime.now()));
            ps.setBigDecimal(5, reembolso.getMonto());
            ps.setString(6, reembolso.getMotivo());
            ps.setString(7, reembolso.getEstado() != null ? reembolso.getEstado() : "Solicitado");

            return ps.executeUpdate() > 0;
        }
    }
}