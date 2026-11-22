/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3.dao;
import com.mycompany.mavenproject3.modelo.Conductor;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Dell
 */


public class ConductorDAO {

    public List<Conductor> listarTodos() throws SQLException {
        List<Conductor> lista = new ArrayList<>();
        String sql = "SELECT IdConductor, Nombre, Apellido, Identidad, Telefono, Direccion, "
                   + "Licencia, FechaVencimientoLicencia, FechaContratacion, Estado FROM Conductor";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Conductor c = new Conductor();
                c.setIdConductor(rs.getInt("IdConductor"));
                c.setNombre(rs.getString("Nombre"));
                c.setApellido(rs.getString("Apellido"));
                c.setIdentidad(rs.getString("Identidad"));
                c.setTelefono(rs.getString("Telefono"));
                c.setDireccion(rs.getString("Direccion"));
                c.setLicencia(rs.getString("Licencia"));

                Date fVenc = rs.getDate("FechaVencimientoLicencia");
                c.setFechaVencimientoLicencia(fVenc != null ? fVenc.toLocalDate() : null);

                Date fCont = rs.getDate("FechaContratacion");
                c.setFechaContratacion(fCont != null ? fCont.toLocalDate() : null);

                c.setEstado(rs.getString("Estado"));
                lista.add(c);
            }
        }
        return lista;
    }

    public boolean insertar(Conductor conductor) throws SQLException {
        String sql = "INSERT INTO Conductor (Nombre, Apellido, Identidad, Telefono, Direccion, "
                   + "Licencia, FechaVencimientoLicencia, FechaContratacion, Estado) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, conductor.getNombre());
            ps.setString(2, conductor.getApellido());
            ps.setString(3, conductor.getIdentidad());
            ps.setString(4, conductor.getTelefono());
            ps.setString(5, conductor.getDireccion());
            ps.setString(6, conductor.getLicencia());
            ps.setDate(7, Date.valueOf(conductor.getFechaVencimientoLicencia()));
            ps.setDate(8, conductor.getFechaContratacion() != null
                    ? Date.valueOf(conductor.getFechaContratacion())
                    : Date.valueOf(java.time.LocalDate.now()));
            ps.setString(9, conductor.getEstado() != null ? conductor.getEstado() : "Activo");

            return ps.executeUpdate() > 0;
        }
    }
}