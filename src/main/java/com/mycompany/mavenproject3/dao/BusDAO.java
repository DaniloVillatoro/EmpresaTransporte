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
    
    public boolean actualizarEstado(int id, String nuevoEstado) throws SQLException {
        String sql = "UPDATE Bus SET Estado = ? WHERE IdBus = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Inserta el bus y genera automaticamente sus asientos (1..capacidad)
     * en una sola transaccion. Ubicacion: columnas 1 y 4 = Ventana, 2 y 3 = Pasillo.
     * @return el IdBus generado
     */
    public int insertarConAsientos(Bus bus) throws SQLException {
        String sqlB = "INSERT INTO Bus (Placa, Marca, Modelo, Anio, TipoBus, Capacidad, FechaAdquisicion, Estado) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlA = "INSERT INTO Asiento (IdBus, NumeroAsiento, Tipo, Ubicacion, Estado) VALUES (?, ?, 'Normal', ?, 'Disponible')";
        try (Connection conn = ConexionBD.obtenerConexion()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sqlB, java.sql.Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, bus.getPlaca());
                ps.setString(2, bus.getMarca());
                ps.setString(3, bus.getModelo());
                if (bus.getAnio() != null) {
                    ps.setInt(4, bus.getAnio());
                } else {
                    ps.setNull(4, java.sql.Types.INTEGER);
                }
                ps.setString(5, bus.getTipoBus());
                ps.setInt(6, bus.getCapacidad());
                if (bus.getFechaAdquisicion() != null) {
                    ps.setDate(7, java.sql.Date.valueOf(bus.getFechaAdquisicion()));
                } else {
                    ps.setNull(7, java.sql.Types.DATE);
                }
                ps.setString(8, bus.getEstado() != null ? bus.getEstado() : "Disponible");
                ps.executeUpdate();
                int id;
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (!keys.next()) {
                        throw new SQLException("No se obtuvo el IdBus generado.");
                    }
                    id = keys.getInt(1);
                }
                try (PreparedStatement pa = conn.prepareStatement(sqlA)) {
                    for (int n = 1; n <= bus.getCapacidad(); n++) {
                        int col = (n - 1) % 4;
                        pa.setInt(1, id);
                        pa.setInt(2, n);
                        pa.setString(3, (col == 0 || col == 3) ? "Ventana" : "Pasillo");
                        pa.addBatch();
                    }
                    pa.executeBatch();
                }
                conn.commit();
                return id;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }
}