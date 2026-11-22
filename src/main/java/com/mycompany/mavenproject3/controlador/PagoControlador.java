/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3.controlador;
import com.mycompany.mavenproject3.modelo.Pago;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
/**
 *
 * @author Dell
 */


public class PagoControlador {

    private static final List<String> ESTADOS_VALIDOS = Arrays.asList(
            "Pendiente", "Aprobado", "Rechazado", "Reembolsado"
    );

    public String validarPago(Pago pago) {
        if (pago == null) {
            return "Los datos del pago no pueden ser nulos.";
        }
        if (pago.getIdBoleto() <= 0) {
            return "Debe asociar un boleto válido al pago.";
        }
        if (pago.getIdMetodoPago() <= 0) {
            return "Debe seleccionar un método de pago válido.";
        }
        if (pago.getMonto() == null || pago.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            return "El monto del pago debe ser mayor que cero.";
        }
        if (pago.getEstadoPago() != null && !ESTADOS_VALIDOS.contains(pago.getEstadoPago())) {
            return "Estado de pago no válido: " + pago.getEstadoPago();
        }
        return null;
    }

    public Pago prepararParaRegistro(Pago pago) {
        if (pago == null) {
            return null;
        }
        if (pago.getFechaPago() == null) {
            pago.setFechaPago(LocalDateTime.now());
        }
        if (pago.getEstadoPago() == null || pago.getEstadoPago().trim().isEmpty()) {
            pago.setEstadoPago("Pendiente");
        }
        return pago;
    }
}