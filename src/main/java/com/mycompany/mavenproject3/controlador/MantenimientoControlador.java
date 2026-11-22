/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3.controlador;
import com.mycompany.mavenproject3.modelo.Mantenimiento;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
/**
 *
 * @author Dell
 */


public class MantenimientoControlador {

    private static final List<String> ESTADOS_VALIDOS = Arrays.asList(
            "Pendiente", "En proceso", "Finalizado"
    );

    public String validarMantenimiento(Mantenimiento mantenimiento) {
        if (mantenimiento == null) {
            return "Los datos del mantenimiento no pueden ser nulos.";
        }
        if (mantenimiento.getIdBus() <= 0) {
            return "Debe seleccionar un bus válido para el mantenimiento.";
        }
        if (mantenimiento.getFecha() == null) {
            return "La fecha del mantenimiento es obligatoria.";
        }
        if (mantenimiento.getTipo() == null || mantenimiento.getTipo().trim().isEmpty()) {
            return "El tipo de mantenimiento es obligatorio.";
        }
        if (mantenimiento.getKilometraje() != null && mantenimiento.getKilometraje() < 0) {
            return "El kilometraje no puede ser negativo.";
        }
        if (mantenimiento.getCosto() != null && mantenimiento.getCosto().compareTo(BigDecimal.ZERO) < 0) {
            return "El costo del mantenimiento no puede ser negativo.";
        }
        if (mantenimiento.getEstado() != null && !ESTADOS_VALIDOS.contains(mantenimiento.getEstado())) {
            return "Estado de mantenimiento no válido: " + mantenimiento.getEstado();
        }
        return null;
    }

    public Mantenimiento prepararParaRegistro(Mantenimiento mantenimiento) {
        if (mantenimiento == null) {
            return null;
        }
        mantenimiento.setTipo(mantenimiento.getTipo().trim());
        if (mantenimiento.getFecha() == null) {
            mantenimiento.setFecha(LocalDate.now());
        }
        if (mantenimiento.getCosto() == null) {
            mantenimiento.setCosto(BigDecimal.ZERO);
        }
        if (mantenimiento.getEstado() == null || mantenimiento.getEstado().trim().isEmpty()) {
            mantenimiento.setEstado("Pendiente");
        }
        return mantenimiento;
    }
}