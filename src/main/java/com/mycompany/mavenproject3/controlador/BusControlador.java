/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3.controlador;

/**
 *
 * @author Dell
 */
import com.mycompany.mavenproject3.modelo.Bus;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class BusControlador {

    private static final List<String> ESTADOS_VALIDOS = Arrays.asList(
            "Disponible", "En ruta", "Mantenimiento", "Inactivo"
    );

    public String validarBus(Bus bus) {
        if (bus == null) {
            return "Los datos del bus no pueden ser nulos.";
        }
        if (bus.getPlaca() == null || bus.getPlaca().trim().isEmpty()) {
            return "La placa del bus es obligatoria.";
        }
        if (bus.getCapacidad() <= 0) {
            return "La capacidad del bus debe ser mayor que cero.";
        }
        if (bus.getAnio() != null && (bus.getAnio() < 1900 || bus.getAnio() > 2100)) {
            return "El año del bus está fuera del rango permitido (1900-2100).";
        }
        String estado = bus.getEstado();
        if (estado != null && !ESTADOS_VALIDOS.contains(estado)) {
            return "Estado no válido para el bus: " + estado;
        }
        return null; // null significa que todo está válido
    }

    public Bus prepararParaRegistro(Bus bus) {
        if (bus == null) {
            return null;
        }
        bus.setPlaca(bus.getPlaca().trim().toUpperCase());
        if (bus.getEstado() == null || bus.getEstado().trim().isEmpty()) {
            bus.setEstado("Disponible");
        }
        if (bus.getFechaAdquisicion() == null) {
            bus.setFechaAdquisicion(LocalDate.now());
        }
        return bus;
    }

    public boolean puedeCambiarEstado(String estadoActual, String nuevoEstado) {
        if (nuevoEstado == null || !ESTADOS_VALIDOS.contains(nuevoEstado)) {
            return false;
        }
        if ("Inactivo".equalsIgnoreCase(estadoActual) && "En ruta".equalsIgnoreCase(nuevoEstado)) {
            return false;
        }
        return true;
    }
}