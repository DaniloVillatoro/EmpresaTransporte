package com.mycompany.mavenproject3.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class VistaDAO {

    private static final String RUTA_TXT = "(o.Ciudad || ' - ' || d.Ciudad)";

    public static class Fila extends LinkedHashMap<String, Object> {
    }

    private List<Fila> consultar(String sql, Object... params) throws SQLException {
        List<Fila> filas = new ArrayList<>();
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                ResultSetMetaData md = rs.getMetaData();
                int columnas = md.getColumnCount();
                while (rs.next()) {
                    Fila fila = new Fila();
                    for (int c = 1; c <= columnas; c++) {
                        fila.put(md.getColumnLabel(c), rs.getObject(c));
                    }
                    filas.add(fila);
                }
            }
        }
        return filas;
    }

    public Fila indicadores() throws SQLException {
        String sql = "SELECT "
                + "(SELECT COUNT(*) FROM Boleto WHERE Estado = 'Pagado' AND CAST(FechaCompra AS date) = CURRENT_DATE) AS boletoshoy, "
                + "(SELECT COALESCE(SUM(Precio), 0) FROM Boleto WHERE Estado = 'Pagado' AND CAST(FechaCompra AS date) = CURRENT_DATE) AS ventashoy, "
                + "(SELECT COUNT(*) FROM Bus WHERE Estado = 'En ruta') AS busesenruta, "
                + "(SELECT COUNT(*) FROM Bus WHERE Estado = 'Mantenimiento') AS enmantenimiento";
        List<Fila> res = consultar(sql);
        return res.isEmpty() ? new Fila() : res.get(0);
    }

    public List<Fila> ventasSemana() throws SQLException {
        String sql = "SELECT TO_CHAR(d.dia, 'DD/MM') AS dia, COALESCE(SUM(b.Precio), 0) AS total "
                + "FROM generate_series((CURRENT_DATE - INTERVAL '6 days')::date, CURRENT_DATE, INTERVAL '1 day') AS d(dia) "
                + "LEFT JOIN Boleto b ON CAST(b.FechaCompra AS date) = d.dia AND b.Estado = 'Pagado' "
                + "GROUP BY d.dia ORDER BY d.dia";
        return consultar(sql);
    }

    public List<Fila> proximasSalidas(int limite) throws SQLException {
        String sql = "SELECT h.IdHorario AS id, " + RUTA_TXT + " AS ruta, "
                + "TO_CHAR(h.HoraSalida, 'HH24:MI') AS hora, TO_CHAR(h.FechaSalida, 'DD/MM/YYYY') AS fecha, "
                + "b.Placa AS bus, h.Estado AS estado, "
                + "(SELECT COUNT(*) FROM Disponibilidad_Asiento da WHERE da.IdHorario = h.IdHorario AND da.Estado = 'Disponible') AS libres "
                + "FROM Horario h "
                + "JOIN Ruta r ON r.IdRuta = h.IdRuta "
                + "JOIN Terminal o ON o.IdTerminal = r.TerminalOrigen "
                + "JOIN Terminal d ON d.IdTerminal = r.TerminalDestino "
                + "JOIN Bus b ON b.IdBus = h.IdBus "
                + "WHERE h.FechaSalida >= CURRENT_DATE AND h.Estado <> 'Finalizado' "
                + "ORDER BY h.FechaSalida, h.HoraSalida LIMIT ?";
        return consultar(sql, limite);
    }

    public List<Fila> horariosEnVenta() throws SQLException {
        String sql = "SELECT h.IdHorario AS id, " + RUTA_TXT + " || ' · ' || TO_CHAR(h.FechaSalida, 'DD/MM/YYYY') || ' ' "
                + "|| TO_CHAR(h.HoraSalida, 'HH24:MI') AS texto "
                + "FROM Horario h "
                + "JOIN Ruta r ON r.IdRuta = h.IdRuta "
                + "JOIN Terminal o ON o.IdTerminal = r.TerminalOrigen "
                + "JOIN Terminal d ON d.IdTerminal = r.TerminalDestino "
                + "WHERE h.Estado = 'Programado' AND h.FechaSalida >= CURRENT_DATE "
                + "ORDER BY h.FechaSalida, h.HoraSalida";
        return consultar(sql);
    }

    public List<Fila> asientosDeHorario(int idHorario) throws SQLException {
        String sql = "SELECT da.IdDisponibilidad AS id, a.NumeroAsiento AS numero, da.Estado AS estado "
                + "FROM Disponibilidad_Asiento da "
                + "JOIN Asiento a ON a.IdAsiento = da.IdAsiento "
                + "WHERE da.IdHorario = ? ORDER BY a.NumeroAsiento";
        return consultar(sql, idHorario);
    }

    public List<Fila> boletos(int limite) throws SQLException {
        String sql = "SELECT bo.IdBoleto AS id, bo.CodigoBoleto AS codigo, "
                + "c.Nombre || ' ' || c.Apellido AS cliente, c.Identidad AS identidad, " + RUTA_TXT + " AS ruta, "
                + "TO_CHAR(h.FechaSalida, 'DD/MM/YYYY') || ' ' || TO_CHAR(h.HoraSalida, 'HH24:MI') AS salida, "
                + "a.NumeroAsiento AS asiento, bo.Precio AS precio, bo.Estado AS estado, "
                + "TO_CHAR(bo.FechaCompra, 'YYYY-MM-DD HH24:MI') AS compra "
                + "FROM Boleto bo "
                + "JOIN Cliente c ON c.IdCliente = bo.IdCliente "
                + "JOIN Disponibilidad_Asiento da ON da.IdDisponibilidad = bo.IdDisponibilidad "
                + "JOIN Asiento a ON a.IdAsiento = da.IdAsiento "
                + "JOIN Horario h ON h.IdHorario = da.IdHorario "
                + "JOIN Ruta r ON r.IdRuta = h.IdRuta "
                + "JOIN Terminal o ON o.IdTerminal = r.TerminalOrigen "
                + "JOIN Terminal d ON d.IdTerminal = r.TerminalDestino "
                + "ORDER BY bo.IdBoleto DESC LIMIT ?";
        return consultar(sql, limite);
    }

    public List<Fila> horarios() throws SQLException {
        String sql = "SELECT h.IdHorario AS id, " + RUTA_TXT + " AS ruta, b.Placa AS bus, "
                + "co.Nombre || ' ' || co.Apellido AS conductor, "
                + "TO_CHAR(h.FechaSalida, 'DD/MM/YYYY') || ' ' || TO_CHAR(h.HoraSalida, 'HH24:MI') AS salida, "
                + "TO_CHAR(h.FechaLlegada, 'DD/MM/YYYY') || ' ' || TO_CHAR(h.HoraLlegada, 'HH24:MI') AS llegada, "
                + "h.Estado AS estado, "
                + "(SELECT COUNT(*) FROM Disponibilidad_Asiento da WHERE da.IdHorario = h.IdHorario AND da.Estado = 'Ocupado') AS ocupados, "
                + "b.Capacidad AS capacidad "
                + "FROM Horario h "
                + "JOIN Ruta r ON r.IdRuta = h.IdRuta "
                + "JOIN Terminal o ON o.IdTerminal = r.TerminalOrigen "
                + "JOIN Terminal d ON d.IdTerminal = r.TerminalDestino "
                + "JOIN Bus b ON b.IdBus = h.IdBus "
                + "JOIN Conductor co ON co.IdConductor = h.IdConductor "
                + "ORDER BY h.FechaSalida DESC, h.HoraSalida DESC";
        return consultar(sql);
    }

    public boolean hayConflicto(int idBus, int idConductor, LocalDate fecha, LocalTime hora) throws SQLException {
        String sql = "SELECT COUNT(*) AS n FROM Horario WHERE FechaSalida = ? AND HoraSalida = ? "
                + "AND Estado IN ('Programado','En curso') AND (IdBus = ? OR IdConductor = ?)";
        List<Fila> res = consultar(sql, Date.valueOf(fecha), Time.valueOf(hora), idBus, idConductor);
        if (res.isEmpty()) {
            return false;
        }
        Object n = res.get(0).get("n");
        return n != null && ((Number) n).intValue() > 0;
    }

    public List<Fila> rutas() throws SQLException {
        String sql = "SELECT r.IdRuta AS id, r.NombreRuta AS nombre, o.Ciudad AS origen, d.Ciudad AS destino, "
                + "r.Distancia_Km AS km, TO_CHAR(r.Duracion_Estimada, 'HH24:MI') AS duracion, r.Estado AS estado "
                + "FROM Ruta r "
                + "JOIN Terminal o ON o.IdTerminal = r.TerminalOrigen "
                + "JOIN Terminal d ON d.IdTerminal = r.TerminalDestino "
                + "ORDER BY r.NombreRuta";
        return consultar(sql);
    }

    public List<Fila> pagos(int limite) throws SQLException {
        String sql = "SELECT p.IdPago AS id, bo.CodigoBoleto AS boleto, m.Nombre AS metodo, p.Monto AS monto, "
                + "TO_CHAR(p.FechaPago, 'YYYY-MM-DD HH24:MI') AS fecha, p.ReferenciaPago AS referencia, p.EstadoPago AS estado "
                + "FROM Pago p "
                + "JOIN Boleto bo ON bo.IdBoleto = p.IdBoleto "
                + "JOIN Metodo_Pago m ON m.IdMetodoPago = p.IdMetodoPago "
                + "ORDER BY p.IdPago DESC LIMIT ?";
        return consultar(sql, limite);
    }

    public List<Fila> mantenimientos() throws SQLException {
        String sql = "SELECT m.IdMantenimiento AS id, b.Placa AS bus, TO_CHAR(m.Fecha, 'DD/MM/YYYY') AS fecha, "
                + "m.Tipo AS tipo, m.Descripcion AS descripcion, m.Kilometraje AS km, m.Costo AS costo, m.Estado AS estado "
                + "FROM Mantenimiento m "
                + "JOIN Bus b ON b.IdBus = m.IdBus "
                + "ORDER BY m.Fecha DESC, m.IdMantenimiento DESC";
        return consultar(sql);
    }
    
    public List<Fila> conductores() throws SQLException {
        String sql = "SELECT idconductor AS id, "
                + "(nombre || ' ' || apellido) AS nombre, "
                + "identidad, telefono, direccion, licencia, "
                + "TO_CHAR(fechavencimientolicencia, 'DD/MM/YYYY') AS vence, "
                + "TO_CHAR(fechacontratacion, 'DD/MM/YYYY') AS contratado, "
                + "estado "
                + "FROM conductor "
                + "ORDER BY nombre, apellido";
        return consultar(sql);
    }
}