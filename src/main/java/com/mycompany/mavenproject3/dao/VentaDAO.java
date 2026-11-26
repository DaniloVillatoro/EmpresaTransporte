package com.mycompany.mavenproject3.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

/**
 * Operaciones transaccionales que tocan varias tablas a la vez.
 * Si cualquier paso falla se hace ROLLBACK y no queda nada a medias.
 */
public class VentaDAO {

    /** Resultado de una venta: id y codigo del boleto emitido. */
    public static class Resultado {
        public final int idBoleto;
        public final String codigo;

        public Resultado(int idBoleto, String codigo) {
            this.idBoleto = idBoleto;
            this.codigo = codigo;
        }

        public int getIdBoleto() { return idBoleto; }
        public String getCodigo() { return codigo; }
    }

    /**
     * Vende un boleto en taquilla:
     *  1) bloquea el asiento y verifica que siga Disponible
     *  2) busca al cliente por identidad (o lo crea si se dieron nombre y apellido)
     *  3) inserta Boleto (Pagado)  4) marca el asiento Ocupado  5) inserta Pago (Aprobado)
     */
    public Resultado registrarVenta(String identidad, String nombre, String apellido,
                                    int idDisponibilidad, int idEmpleado, int idMetodoPago,
                                    BigDecimal precio) throws SQLException, VentaException {
        try (Connection conn = ConexionBD.obtenerConexion()) {
            conn.setAutoCommit(false);
            try {
                // 1) asiento
                try (PreparedStatement ps = conn.prepareStatement(
                        "SELECT Estado FROM Disponibilidad_Asiento WITH (UPDLOCK, ROWLOCK) WHERE IdDisponibilidad = ?")) {
                    ps.setInt(1, idDisponibilidad);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new VentaException("El asiento seleccionado no existe.");
                        }
                        if (!"Disponible".equals(rs.getString(1))) {
                            throw new VentaException("Ese asiento ya fue vendido o esta reservado. Elige otro.");
                        }
                    }
                }

                // 2) cliente
                int idCliente = -1;
                try (PreparedStatement ps = conn.prepareStatement("SELECT IdCliente FROM Cliente WHERE Identidad = ?")) {
                    ps.setString(1, identidad);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            idCliente = rs.getInt(1);
                        }
                    }
                }
                if (idCliente < 0) {
                    if (nombre == null || nombre.isBlank() || apellido == null || apellido.isBlank()) {
                        throw new VentaException("Cliente nuevo: escribe nombre y apellido para registrarlo.");
                    }
                    try (PreparedStatement ps = conn.prepareStatement(
                            "INSERT INTO Cliente (Nombre, Apellido, Identidad, Telefono, Correo, Direccion, FechaRegistro, Estado) "
                          + "VALUES (?, ?, ?, NULL, NULL, NULL, GETDATE(), 'Activo')", Statement.RETURN_GENERATED_KEYS)) {
                        ps.setString(1, nombre.trim());
                        ps.setString(2, apellido.trim());
                        ps.setString(3, identidad);
                        ps.executeUpdate();
                        try (ResultSet k = ps.getGeneratedKeys()) {
                            k.next();
                            idCliente = k.getInt(1);
                        }
                    }
                }

                // 3) boleto
                String codigo = "BOL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
                int idBoleto;
                try (PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO Boleto (CodigoBoleto, IdCliente, IdDisponibilidad, IdEmpleado, FechaCompra, Precio, MetodoCompra, Estado) "
                      + "VALUES (?, ?, ?, ?, GETDATE(), ?, 'Taquilla', 'Pagado')", Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, codigo);
                    ps.setInt(2, idCliente);
                    ps.setInt(3, idDisponibilidad);
                    ps.setInt(4, idEmpleado);
                    ps.setBigDecimal(5, precio);
                    ps.executeUpdate();
                    try (ResultSet k = ps.getGeneratedKeys()) {
                        k.next();
                        idBoleto = k.getInt(1);
                    }
                }

                // 4) asiento ocupado
                try (PreparedStatement ps = conn.prepareStatement(
                        "UPDATE Disponibilidad_Asiento SET Estado = 'Ocupado', FechaActualizacion = GETDATE() WHERE IdDisponibilidad = ?")) {
                    ps.setInt(1, idDisponibilidad);
                    ps.executeUpdate();
                }

                // 5) pago
                try (PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO Pago (IdBoleto, IdMetodoPago, Monto, FechaPago, ReferenciaPago, EstadoPago) "
                      + "VALUES (?, ?, ?, GETDATE(), ?, 'Aprobado')")) {
                    ps.setInt(1, idBoleto);
                    ps.setInt(2, idMetodoPago);
                    ps.setBigDecimal(3, precio);
                    ps.setString(4, "PAG-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
                    ps.executeUpdate();
                }

                conn.commit();
                return new Resultado(idBoleto, codigo);
            } catch (SQLException | VentaException | RuntimeException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    /**
     * Anula un boleto pagado: boleto -> Cancelado, asiento -> Disponible,
     * pago -> Reembolsado y crea el registro de Reembolso (Procesado).
     */
    public void anularBoleto(int idBoleto, int idEmpleado) throws SQLException, VentaException {
        try (Connection conn = ConexionBD.obtenerConexion()) {
            conn.setAutoCommit(false);
            try {
                int idDisp;
                BigDecimal precio;
                try (PreparedStatement ps = conn.prepareStatement(
                        "SELECT IdDisponibilidad, Precio, Estado FROM Boleto WITH (UPDLOCK) WHERE IdBoleto = ?")) {
                    ps.setInt(1, idBoleto);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new VentaException("El boleto no existe.");
                        }
                        if (!"Pagado".equals(rs.getString("Estado"))) {
                            throw new VentaException("Solo se pueden anular boletos en estado Pagado.");
                        }
                        idDisp = rs.getInt("IdDisponibilidad");
                        precio = rs.getBigDecimal("Precio");
                    }
                }
                try (PreparedStatement ps = conn.prepareStatement("UPDATE Boleto SET Estado = 'Cancelado' WHERE IdBoleto = ?")) {
                    ps.setInt(1, idBoleto);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = conn.prepareStatement(
                        "UPDATE Disponibilidad_Asiento SET Estado = 'Disponible', FechaActualizacion = GETDATE() WHERE IdDisponibilidad = ?")) {
                    ps.setInt(1, idDisp);
                    ps.executeUpdate();
                }
                int idPago = -1;
                int idMetodo = -1;
                try (PreparedStatement ps = conn.prepareStatement(
                        "SELECT TOP 1 IdPago, IdMetodoPago FROM Pago WHERE IdBoleto = ? ORDER BY IdPago DESC")) {
                    ps.setInt(1, idBoleto);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            idPago = rs.getInt(1);
                            idMetodo = rs.getInt(2);
                        }
                    }
                }
                if (idPago > 0) {
                    try (PreparedStatement ps = conn.prepareStatement("UPDATE Pago SET EstadoPago = 'Reembolsado' WHERE IdPago = ?")) {
                        ps.setInt(1, idPago);
                        ps.executeUpdate();
                    }
                    try (PreparedStatement ps = conn.prepareStatement(
                            "INSERT INTO Reembolso (IdPago, IdEmpleado, IdMetodoPago, FechaReembolso, Monto, Motivo, Estado) "
                          + "VALUES (?, ?, ?, GETDATE(), ?, 'Anulacion de boleto en taquilla', 'Procesado')")) {
                        ps.setInt(1, idPago);
                        ps.setInt(2, idEmpleado);
                        ps.setInt(3, idMetodo);
                        ps.setBigDecimal(4, precio);
                        ps.executeUpdate();
                    }
                }
                conn.commit();
            } catch (SQLException | VentaException | RuntimeException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }
}
