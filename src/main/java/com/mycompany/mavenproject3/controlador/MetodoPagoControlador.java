/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3.controlador;
import com.mycompany.mavenproject3.modelo.MetodoPago;
import java.util.Arrays;
import java.util.List;
/**
 *
 * @author Dell
 */


public class MetodoPagoControlador {

    private static final List<String> ESTADOS_VALIDOS = Arrays.asList("Activo", "Inactivo");

    public String validarMetodoPago(MetodoPago metodoPago) {
        if (metodoPago == null) {
            return "Los datos del método de pago no pueden ser nulos.";
        }
        if (metodoPago.getNombre() == null || metodoPago.getNombre().trim().isEmpty()) {
            return "El nombre del método de pago es obligatorio.";
        }
        if (metodoPago.getEstado() != null && !ESTADOS_VALIDOS.contains(metodoPago.getEstado())) {
            return "Estado de método de pago no válido: " + metodoPago.getEstado();
        }
        return null;
    }

    public MetodoPago prepararParaRegistro(MetodoPago metodoPago) {
        if (metodoPago == null) {
            return null;
        }
        metodoPago.setNombre(metodoPago.getNombre().trim());
        if (metodoPago.getEstado() == null || metodoPago.getEstado().trim().isEmpty()) {
            metodoPago.setEstado("Activo");
        }
        return metodoPago;
    }
}
