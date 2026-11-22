/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3.dao;
import com.mycompany.mavenproject3.modelo.MetodoPago;
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


public class MetodoPagoDAO {

    public List<MetodoPago> listarTodos() throws SQLException {
        List<MetodoPago> lista = new ArrayList<>();
        String sql = "SELECT IdMetodoPago, Nombre, Descripcion, Estado FROM Metodo_Pago";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                MetodoPago m = new MetodoPago();
                m.setIdMetodoPago(rs.getInt("IdMetodoPago"));
                m.setNombre(rs.getString("Nombre"));
                m.setDescripcion(rs.getString("Descripcion"));
                m.setEstado(rs.getString("Estado"));
                lista.add(m);
            }
        }
        return lista;
    }

    public boolean insertar(MetodoPago metodo) throws SQLException {
        String sql = "INSERT INTO Metodo_Pago (Nombre, Descripcion, Estado) VALUES (?, ?, ?)";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, metodo.getNombre());
            ps.setString(2, metodo.getDescripcion());
            ps.setString(3, metodo.getEstado() != null ? metodo.getEstado() : "Activo");

            return ps.executeUpdate() > 0;
        }
    }
}
