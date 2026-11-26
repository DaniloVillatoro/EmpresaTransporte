package com.mycompany.mavenproject3.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Consultas de LECTURA con JOIN para mostrar en pantalla (dashboard, tablas, listas).
 * Devuelve filas como Map para que el JSP las lea directo con EL: ${fila.ruta}.
 * Las fechas y horas se formatean en SQL (dd/MM/yyyy y HH:mm).
 */
public class VistaDAO {

    private static final String RUTA_TXT = "(o.Ciudad + ' - ' + d.Ciudad)";

    private List<Map<String, Object>> consultar(String sql, Object... params) throws SQLException {
        List<Map<String, Object>> filas = new ArrayList<>();
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                ResultSetMetaData md = rs.getMetaData();
                while (rs.next()) {
                    Map<String, Object> fila = new LinkedHashMap<>();
                    for (int c = 1; c <= md.getColumnCount(); c++) {
                        fila.put(md.getColumnLabel(c), rs.getObject(c));
                    }
                    filas.add(fila);
                }
            }
        }
        return filas;
    }

    /** Las 4 tarjetas superiores del panel. */
    public Map<String, Object> indicadores() throws SQLException {
        String sql = "SELECT "
            + "(SELECT COUNT(*) FROM Boleto WHERE Estado = 'Pagado' AND CAST(FechaCompra AS date) = CAST(GETDATE() AS date)) AS boletosHoy, "
            + "(SELECT COALESCE(SUM(Precio), 0) FROM Boleto WHERE Estado = 'Pagado' AND CAST(FechaCompra AS date) = CAST(GETDATE() AS date)) AS ventasHoy, "
            + "(SELECT COUNT(*) FROM Bus WHERE Estado = 'En ruta') AS busesEnRuta, "
            + "(SELECT COUNT(*) FROM Bus WHERE Estado = 'Mantenimiento') AS enMantenimiento";
        return consultar(sql).get(0);
    }

    /** Ventas de los ultimos 7 dias (para la grafica del panel). */
    public List<Map<String, Object>> ventasSemana() throws SQLException {
        String sql = "SELECT CONVERT(varchar(5), d.dia, 103) AS dia, COALESCE(SUM(b.Precio), 0) AS total "
            + "FROM (SELECT CAST(DATEADD(day, -n, GETDATE()) AS date) AS dia FROM (VALUES (6),(5),(4),(3),(2),(1),(0)) v(n)) d "
            + "LEFT JOIN Boleto b ON CAST(b.FechaCompra AS date) = d.dia AND b.Estado = 'Pagado' "
            + "GROUP BY d.dia ORDER BY d.dia";
        return consultar(sql);
    }

    public List<Map<String, Object>> proximasSalidas(int limite) throws SQLException {
        String sql = "SELECT TOP (?) h.IdHorario AS id, " + RUTA_TXT + " AS ruta, "
            + "CONVERT(varchar(5), h.HoraSalida, 108) AS hora, CONVERT(varchar(10), h.FechaSalida, 103) AS fecha, "
            + "b.Placa AS bus, h.Estado AS estado, "
            + "(SELECT COUNT(*) FROM Disponibilidad_Asiento da WHERE da.IdHorario = h.IdHorario AND da.Estado = 'Disponible') AS libres "
            + "FROM Horario h JOIN Ruta r ON r.IdRuta = h.IdRuta "
            + "JOIN Terminal o ON o.IdTerminal = r.TerminalOrigen JOIN Terminal d ON d.IdTerminal = r.TerminalDestino "
            + "JOIN Bus b ON b.IdBus = h.IdBus "
            + "WHERE h.FechaSalida >= CAST(GETDATE() AS date) AND h.Estado <> 'Finalizado' "
            + "ORDER BY h.FechaSalida, h.HoraSalida";
        return consultar(sql, limite);
    }

    /** Horarios vendibles para el combo "Horario" del formulario de venta. */
    public List<Map<String, Object>> horariosEnVenta() throws SQLException {
        String sql = "SELECT h.IdHorario AS id, " + RUTA_TXT + " + '  ·  ' + CONVERT(varchar(10), h.FechaSalida, 103) + ' ' "
            + "+ CONVERT(varchar(5), h.HoraSalida, 108) AS texto "
            + "FROM Horario h JOIN Ruta r ON r.IdRuta = h.IdRuta "
            + "JOIN Terminal o ON o.IdTerminal = r.TerminalOrigen JOIN Terminal d ON d.IdTerminal = r.TerminalDestino "
            + "WHERE h.Estado = 'Programado' AND h.FechaSalida >= CAST(GETDATE() AS date) "
            + "ORDER BY h.FechaSalida, h.HoraSalida";
        return consultar(sql);
    }

    /** Mapa de asientos de un horario (para pintar la cuadricula). */
    public List<Map<String, Object>> asientosDeHorario(int idHorario) throws SQLException {
        String sql = "SELECT da.IdDisponibilidad AS id, a.NumeroAsiento AS numero, da.Estado AS estado "
            + "FROM Disponibilidad_Asiento da JOIN Asiento a ON a.IdAsiento = da.IdAsiento "
            + "WHERE da.IdHorario = ? ORDER BY a.NumeroAsiento";
        return consultar(sql, idHorario);
    }

    public List<Map<String, Object>> boletos(int limite) throws SQLException {
        String sql = "SELECT TOP (?) bo.IdBoleto AS id, bo.CodigoBoleto AS codigo, "
            + "c.Nombre + ' ' + c.Apellido AS cliente, c.Identidad AS identidad, " + RUTA_TXT + " AS ruta, "
            + "CONVERT(varchar(10), h.FechaSalida, 103) + ' ' + CONVERT(varchar(5), h.HoraSalida, 108) AS salida, "
            + "a.NumeroAsiento AS asiento, bo.Precio AS precio, bo.Estado AS estado, "
            + "CONVERT(varchar(16), bo.FechaCompra, 120) AS compra "
            + "FROM Boleto bo JOIN Cliente c ON c.IdCliente = bo.IdCliente "
            + "JOIN Disponibilidad_Asiento da ON da.IdDisponibilidad = bo.IdDisponibilidad "
            + "JOIN Asiento a ON a.IdAsiento = da.IdAsiento JOIN Horario h ON h.IdHorario = da.IdHorario "
            + "JOIN Ruta r ON r.IdRuta = h.IdRuta "
            + "JOIN Terminal o ON o.IdTerminal = r.TerminalOrigen JOIN Terminal d ON d.IdTerminal = r.TerminalDestino "
            + "ORDER BY bo.IdBoleto DESC";
        return consultar(sql, limite);
    }

    public List<Map<String, Object>> horarios() throws SQLException {
        String sql = "SELECT h.IdHorario AS id, " + RUTA_TXT + " AS ruta, b.Placa AS bus, "
            + "co.Nombre + ' ' + co.Apellido AS conductor, "
            + "CONVERT(varchar(10), h.FechaSalida, 103) + ' ' + CONVERT(varchar(5), h.HoraSalida, 108) AS salida, "
            + "CONVERT(varchar(10), h.FechaLlegada, 103) + ' ' + CONVERT(varchar(5), h.HoraLlegada, 108) AS llegada, "
            + "h.Estado AS estado, "
            + "(SELECT COUNT(*) FROM Disponibilidad_Asiento da WHERE da.IdHorario = h.IdHorario AND da.Estado = 'Ocupado') AS ocupados, "
            + "b.Capacidad AS capacidad "
            + "FROM Horario h JOIN Ruta r ON r.IdRuta = h.IdRuta "
            + "JOIN Terminal o ON o.IdTerminal = r.TerminalOrigen JOIN Terminal d ON d.IdTerminal = r.TerminalDestino "
            + "JOIN Bus b ON b.IdBus = h.IdBus JOIN Conductor co ON co.IdConductor = h.IdConductor "
            + "ORDER BY h.FechaSalida DESC, h.HoraSalida DESC";
        return consultar(sql);
    }

    /** True si el bus o el conductor ya tienen un viaje activo ese mismo dia y hora. */
    public boolean hayConflicto(int idBus, int idConductor, java.time.LocalDate fecha, java.time.LocalTime hora) throws SQLException {
        String sql = "SELECT COUNT(*) AS n FROM Horario WHERE FechaSalida = ? AND HoraSalida = ? "
            + "AND Estado IN ('Programado','En curso') AND (IdBus = ? OR IdConductor = ?)";
        Object n = consultar(sql, java.sql.Date.valueOf(fecha), java.sql.Time.valueOf(hora), idBus, idConductor).get(0).get("n");
        return ((Number) n).intValue() > 0;
    }

    public List<Map<String, Object>> rutas() throws SQLException {
        String sql = "SELECT r.IdRuta AS id, r.NombreRuta AS nombre, o.Ciudad AS origen, d.Ciudad AS destino, "
            + "r.Distancia_Km AS km, CONVERT(varchar(5), r.Duracion_Estimada, 108) AS duracion, r.Estado AS estado "
            + "FROM Ruta r JOIN Terminal o ON o.IdTerminal = r.TerminalOrigen JOIN Terminal d ON d.IdTerminal = r.TerminalDestino "
            + "ORDER BY r.NombreRuta";
        return consultar(sql);
    }

    public List<Map<String, Object>> pagos(int limite) throws SQLException {
        String sql = "SELECT TOP (?) p.IdPago AS id, bo.CodigoBoleto AS boleto, m.Nombre AS metodo, p.Monto AS monto, "
            + "CONVERT(varchar(16), p.FechaPago, 120) AS fecha, p.ReferenciaPago AS referencia, p.EstadoPago AS estado "
            + "FROM Pago p JOIN Boleto bo ON bo.IdBoleto = p.IdBoleto JOIN Metodo_Pago m ON m.IdMetodoPago = p.IdMetodoPago "
            + "ORDER BY p.IdPago DESC";
        return consultar(sql, limite);
    }

    public List<Map<String, Object>> mantenimientos() throws SQLException {
        String sql = "SELECT m.IdMantenimiento AS id, b.Placa AS bus, CONVERT(varchar(10), m.Fecha, 103) AS fecha, "
            + "m.Tipo AS tipo, m.Descripcion AS descripcion, m.Kilometraje AS km, m.Costo AS costo, m.Estado AS estado "
            + "FROM Mantenimiento m JOIN Bus b ON b.IdBus = m.IdBus ORDER BY m.Fecha DESC, m.IdMantenimiento DESC";
        return consultar(sql);
    }
}
