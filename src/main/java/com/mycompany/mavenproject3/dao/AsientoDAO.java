/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3.dao;
import com.mycompany.mavenproject3.modelo.Asiento;
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

public class AsientoDAO {

    public List<Asiento> listarPorBus(int idBus) throws SQLException {
        List<Asiento> lista = new ArrayList<>();
        String sql = "SELECT IdAsiento, IdBus, NumeroAsiento, Tipo, Ubicacion, Estado "
                   + "FROM Asiento WHERE IdBus = ? ORDER BY NumeroAsiento";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idBus);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Asiento a = new Asiento();
                    a.setIdAsiento(rs.getInt("IdAsiento"));
                    a.setIdBus(rs.getInt("IdBus"));
                    a.setNumeroAsiento(rs.getInt("NumeroAsiento"));
                    a.setTipo(rs.getString("Tipo"));
                    a.setUbicacion(rs.getString("Ubicacion"));
                    a.setEstado(rs.getString("Estado"));
                    lista.add(a);
                }
            }
        }
        return lista;
    }

    public boolean insertar(Asiento asiento) throws SQLException {
        String sql = "INSERT INTO Asiento (IdBus, NumeroAsiento, Tipo, Ubicacion, Estado) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, asiento.getIdBus());
            ps.setInt(2, asiento.getNumeroAsiento());
            ps.setString(3, asiento.getTipo() != null ? asiento.getTipo() : "Normal");
            ps.setString(4, asiento.getUbicacion());
            ps.setString(5, asiento.getEstado() != null ? asiento.getEstado() : "Disponible");

            return ps.executeUpdate() > 0;
        }
    }
}
