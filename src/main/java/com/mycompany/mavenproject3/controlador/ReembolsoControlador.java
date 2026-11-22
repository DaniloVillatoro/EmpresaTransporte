/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3.controlador;
import com.mycompany.mavenproject3.modelo.Reembolso;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
/**
 *
 * @author Dell
 */


public class ReembolsoControlador {

    private static final List<String> ESTADOS_VALIDOS = Arrays.asList(
            "Solicitado", "Aprobado", "Rechazado", "Procesado"
    );

    public String validarReembolso(Reembolso reembolso) {
        if (reembolso == null) {
            return "Los datos del reembolso no pueden ser nulos.";
        }
        if (reembolso.getIdPago() <= 0) {
            return "Debe asociar un pago válido al reembolso.";
        }
        if (reembolso.getIdEmpleado() <= 0) {
            return "Debe indicar el empleado que registra el reembolso.";
        }
        if (reembolso.getIdMetodoPago() <= 0) {
            return "Debe indicar el método de pago para el reembolso.";
        }
        if (reembolso.getMonto() == null || reembolso.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            return "El monto del reembolso debe ser mayor que cero.";
        }
        if (reembolso.getMotivo() == null || reembolso.getMotivo().trim().isEmpty()) {
            return "El motivo del reembolso es obligatorio.";
        }
        if (reembolso.getEstado() != null && !ESTADOS_VALIDOS.contains(reembolso.getEstado())) {
            return "Estado de reembolso no válido: " + reembolso.getEstado();
        }
        return null;
    }

    public Reembolso prepararParaRegistro(Reembolso reembolso) {
        if (reembolso == null) {
            return null;
        }
        reembolso.setMotivo(reembolso.getMotivo().trim());
        if (reembolso.getFechaReembolso() == null) {
            reembolso.setFechaReembolso(LocalDateTime.now());
        }
        if (reembolso.getEstado() == null || reembolso.getEstado().trim().isEmpty()) {
            reembolso.setEstado("Solicitado");
        }
        return reembolso;
    }
}