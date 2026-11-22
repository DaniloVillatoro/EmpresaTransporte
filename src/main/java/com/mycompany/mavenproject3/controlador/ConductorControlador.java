/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3.controlador;
import com.mycompany.mavenproject3.modelo.Conductor;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
/**
 *
 * @author Dell
 */


public class ConductorControlador {

    private static final List<String> ESTADOS_VALIDOS = Arrays.asList("Activo", "Inactivo");

    public String validarConductor(Conductor conductor) {
        if (conductor == null) {
            return "Los datos del conductor no pueden ser nulos.";
        }
        if (conductor.getNombre() == null || conductor.getNombre().trim().isEmpty()) {
            return "El nombre del conductor es obligatorio.";
        }
        if (conductor.getApellido() == null || conductor.getApellido().trim().isEmpty()) {
            return "El apellido del conductor es obligatorio.";
        }
        if (conductor.getIdentidad() == null || conductor.getIdentidad().trim().isEmpty()) {
            return "La identidad del conductor es obligatoria.";
        }
        if (conductor.getLicencia() == null || conductor.getLicencia().trim().isEmpty()) {
            return "El número de licencia del conductor es obligatorio.";
        }
        if (conductor.getFechaVencimientoLicencia() == null) {
            return "La fecha de vencimiento de la licencia es obligatoria.";
        }
        if (conductor.getEstado() != null && !ESTADOS_VALIDOS.contains(conductor.getEstado())) {
            return "Estado de conductor no válido: " + conductor.getEstado();
        }
        return null;
    }

    public boolean tieneLicenciaVigente(Conductor conductor, LocalDate fechaReferencia) {
        if (conductor == null || conductor.getFechaVencimientoLicencia() == null) {
            return false;
        }
        LocalDate base = fechaReferencia != null ? fechaReferencia : LocalDate.now();
        return !conductor.getFechaVencimientoLicencia().isBefore(base);
    }

    public Conductor prepararParaRegistro(Conductor conductor) {
        if (conductor == null) {
            return null;
        }
        conductor.setNombre(conductor.getNombre().trim());
        conductor.setApellido(conductor.getApellido().trim());
        conductor.setIdentidad(conductor.getIdentidad().trim());
        conductor.setLicencia(conductor.getLicencia().trim().toUpperCase());
        if (conductor.getFechaContratacion() == null) {
            conductor.setFechaContratacion(LocalDate.now());
        }
        if (conductor.getEstado() == null || conductor.getEstado().trim().isEmpty()) {
            conductor.setEstado("Activo");
        }
        return conductor;
    }
}