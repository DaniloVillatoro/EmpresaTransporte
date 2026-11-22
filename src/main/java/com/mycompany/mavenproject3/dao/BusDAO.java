/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3.dao;
import com.mycompany.mavenproject3.modelo.Bus;
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


public class BusDAO {

    public List<Bus> listarTodos() throws SQLException {
        List<Bus> lista = new ArrayList<>();
        String sql = "SELECT IdBus, Placa, Marca, Modelo, Anio, TipoBus, Capacidad, FechaAdquisicion, Estado FROM Bus";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Bus bus = new Bus();
                bus.setIdBus(rs.getInt("IdBus"));
                bus.setPlaca(rs.getString("Placa"));
                bus.setMarca(rs.getString("Marca"));
                bus.setModelo(rs.getString("Modelo"));

                int anio = rs.getInt("Anio");
                bus.setAnio(rs.wasNull() ? null : anio);

                bus.setTipoBus(rs.getString("TipoBus"));
                bus.setCapacidad(rs.getInt("Capacidad"));

                Date fechaAdq = rs.getDate("FechaAdquisicion");
                bus.setFechaAdquisicion(fechaAdq != null ? fechaAdq.toLocalDate() : null);

                bus.setEstado(rs.getString("Estado"));
                lista.add(bus);
            }
        }
        return lista;
    }

    public boolean insertar(Bus bus) throws SQLException {
        String sql = "INSERT INTO Bus (Placa, Marca, Modelo, Anio, TipoBus, Capacidad, FechaAdquisicion, Estado) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, bus.getPlaca());
            ps.setString(2, bus.getMarca());
            ps.setString(3, bus.getModelo());

            if (bus.getAnio() != null) {
                ps.setInt(4, bus.getAnio());
            } else {
                ps.setNull(4, Types.SMALLINT);
            }

            ps.setString(5, bus.getTipoBus());
            ps.setInt(6, bus.getCapacidad());

            if (bus.getFechaAdquisicion() != null) {
                ps.setDate(7, Date.valueOf(bus.getFechaAdquisicion()));
            } else {
                ps.setNull(7, Types.DATE);
            }

            ps.setString(8, bus.getEstado() != null ? bus.getEstado() : "Disponible");

            return ps.executeUpdate() > 0;
        }
    }
}