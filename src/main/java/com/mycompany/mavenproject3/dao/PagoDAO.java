/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3.dao;
import com.mycompany.mavenproject3.modelo.Pago;
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


public class PagoDAO {

    public List<Pago> listarTodos() throws SQLException {
        List<Pago> lista = new ArrayList<>();
        String sql = "SELECT IdPago, IdBoleto, IdMetodoPago, Monto, FechaPago, ReferenciaPago, EstadoPago FROM Pago";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Pago p = new Pago();
                p.setIdPago(rs.getInt("IdPago"));
                p.setIdBoleto(rs.getInt("IdBoleto"));
                p.setIdMetodoPago(rs.getInt("IdMetodoPago"));
                p.setMonto(rs.getBigDecimal("Monto"));

                Timestamp ts = rs.getTimestamp("FechaPago");
                p.setFechaPago(ts != null ? ts.toLocalDateTime() : null);

                p.setReferenciaPago(rs.getString("ReferenciaPago"));
                p.setEstadoPago(rs.getString("EstadoPago"));
                lista.add(p);
            }
        }
        return lista;
    }

    public boolean insertar(Pago pago) throws SQLException {
        String sql = "INSERT INTO Pago (IdBoleto, IdMetodoPago, Monto, FechaPago, ReferenciaPago, EstadoPago) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, pago.getIdBoleto());
            ps.setInt(2, pago.getIdMetodoPago());
            ps.setBigDecimal(3, pago.getMonto());
            ps.setTimestamp(4, Timestamp.valueOf(
                    pago.getFechaPago() != null ? pago.getFechaPago() : LocalDateTime.now()));
            ps.setString(5, pago.getReferenciaPago());
            ps.setString(6, pago.getEstadoPago() != null ? pago.getEstadoPago() : "Pendiente");

            return ps.executeUpdate() > 0;
        }
    }
}