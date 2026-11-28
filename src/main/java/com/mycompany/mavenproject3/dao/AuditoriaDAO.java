/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3.dao;

import com.mycompany.mavenproject3.modelo.Auditoria;
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
public class AuditoriaDAO {

    public List<Auditoria> listarRecientes(int limite) throws SQLException {
        List<Auditoria> lista = new ArrayList<>();
        String sql = "SELECT IdAuditoria, TablaAfectada, IdRegistro, Accion, ValorAnterior, "
                   + "ValorNuevo, Usuario, FechaCambio, Equipo, Aplicacion "
                   + "FROM Auditoria ORDER BY IdAuditoria DESC LIMIT ?";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, limite > 0 ? limite : 50);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Auditoria a = new Auditoria();
                    a.setIdAuditoria(rs.getLong("IdAuditoria"));
                    a.setTablaAfectada(rs.getString("TablaAfectada"));
                    a.setIdRegistro(rs.getLong("IdRegistro"));
                    a.setAccion(rs.getString("Accion"));
                    a.setValorAnterior(rs.getString("ValorAnterior"));
                    a.setValorNuevo(rs.getString("ValorNuevo"));
                    a.setUsuario(rs.getString("Usuario"));

                    Timestamp ts = rs.getTimestamp("FechaCambio");
                    a.setFechaCambio(ts != null ? ts.toLocalDateTime() : null);

                    a.setEquipo(rs.getString("Equipo"));
                    a.setAplicacion(rs.getString("Aplicacion"));
                    lista.add(a);
                }
            }
        }
        return lista;
    }

    public boolean registrar(Auditoria a) throws SQLException {
        String sql = "INSERT INTO Auditoria (TablaAfectada, IdRegistro, Accion, ValorAnterior, "
                   + "ValorNuevo, Usuario, FechaCambio, Equipo, Aplicacion) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, a.getTablaAfectada());
            ps.setLong(2, a.getIdRegistro());
            ps.setString(3, a.getAccion());
            ps.setString(4, a.getValorAnterior());
            ps.setString(5, a.getValorNuevo());
            ps.setString(6, a.getUsuario());
            ps.setTimestamp(7, Timestamp.valueOf(
                    a.getFechaCambio() != null ? a.getFechaCambio() : LocalDateTime.now()));
            ps.setString(8, a.getEquipo());
            ps.setString(9, a.getAplicacion());

            return ps.executeUpdate() > 0;
        }
    }
}