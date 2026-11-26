/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3.dao;
import com.mycompany.mavenproject3.modelo.Empleado;
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


public class EmpleadoDAO {

    public List<Empleado> listarTodos() throws SQLException {
        List<Empleado> lista = new ArrayList<>();
        String sql = "SELECT IdEmpleado, Nombre, Apellido, Identidad, Telefono, Correo, "
                   + "Cargo, Usuario, ContrasenaHash, FechaContratacion, Salario, Estado FROM Empleado";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Empleado e = new Empleado();
                e.setIdEmpleado(rs.getInt("IdEmpleado"));
                e.setNombre(rs.getString("Nombre"));
                e.setApellido(rs.getString("Apellido"));
                e.setIdentidad(rs.getString("Identidad"));
                e.setTelefono(rs.getString("Telefono"));
                e.setCorreo(rs.getString("Correo"));
                e.setCargo(rs.getString("Cargo"));
                e.setUsuario(rs.getString("Usuario"));
                e.setContrasenaHash(rs.getString("ContrasenaHash"));

                Date fCont = rs.getDate("FechaContratacion");
                e.setFechaContratacion(fCont != null ? fCont.toLocalDate() : null);

                e.setSalario(rs.getBigDecimal("Salario"));
                e.setEstado(rs.getString("Estado"));
                lista.add(e);
            }
        }
        return lista;
    }

    public boolean insertar(Empleado empleado) throws SQLException {
        String sql = "INSERT INTO Empleado (Nombre, Apellido, Identidad, Telefono, Correo, "
                   + "Cargo, Usuario, ContrasenaHash, FechaContratacion, Salario, Estado) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, empleado.getNombre());
            ps.setString(2, empleado.getApellido());
            ps.setString(3, empleado.getIdentidad());
            ps.setString(4, empleado.getTelefono());
            ps.setString(5, empleado.getCorreo());
            ps.setString(6, empleado.getCargo());
            ps.setString(7, empleado.getUsuario());
            ps.setString(8, empleado.getContrasenaHash());

            if (empleado.getFechaContratacion() != null) {
                ps.setDate(9, Date.valueOf(empleado.getFechaContratacion()));
            } else {
                ps.setNull(9, Types.DATE);
            }

            if (empleado.getSalario() != null) {
                ps.setBigDecimal(10, empleado.getSalario());
            } else {
                ps.setNull(10, Types.DECIMAL);
            }

            ps.setString(11, empleado.getEstado() != null ? empleado.getEstado() : "Activo");

            return ps.executeUpdate() > 0;
        }
    }
    
    public Empleado buscarPorUsuario(String usuario) throws SQLException {
        String sql = "SELECT IdEmpleado, Nombre, Apellido, Identidad, Telefono, Correo, "
                   + "Cargo, Usuario, ContrasenaHash, FechaContratacion, Salario, Estado FROM Empleado WHERE Usuario = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, usuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                Empleado e = new Empleado();
                e.setIdEmpleado(rs.getInt("IdEmpleado"));
                e.setNombre(rs.getString("Nombre"));
                e.setApellido(rs.getString("Apellido"));
                e.setIdentidad(rs.getString("Identidad"));
                e.setTelefono(rs.getString("Telefono"));
                e.setCorreo(rs.getString("Correo"));
                e.setCargo(rs.getString("Cargo"));
                e.setUsuario(rs.getString("Usuario"));
                e.setContrasenaHash(rs.getString("ContrasenaHash"));
                java.sql.Date f = rs.getDate("FechaContratacion");
                e.setFechaContratacion(f != null ? f.toLocalDate() : null);
                e.setSalario(rs.getBigDecimal("Salario"));
                e.setEstado(rs.getString("Estado"));
                return e;
            }
        }
    }
}