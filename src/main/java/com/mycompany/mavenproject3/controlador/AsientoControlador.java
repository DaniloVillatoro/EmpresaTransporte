/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3.controlador;
import com.mycompany.mavenproject3.modelo.Asiento;
import java.util.Arrays;
import java.util.List;
/**
 *
 * @author Dell
 */


public class AsientoControlador {

    private static final List<String> TIPOS_VALIDOS = Arrays.asList("Normal", "VIP", "Preferencial");
    private static final List<String> ESTADOS_VALIDOS = Arrays.asList("Disponible", "Inactivo");

    public String validarAsiento(Asiento asiento) {
        if (asiento == null) {
            return "Los datos del asiento no pueden ser nulos.";
        }
        if (asiento.getIdBus() <= 0) {
            return "Debe asignar un bus válido al asiento.";
        }
        if (asiento.getNumeroAsiento() <= 0) {
            return "El número de asiento debe ser mayor que cero.";
        }
        if (asiento.getTipo() != null && !TIPOS_VALIDOS.contains(asiento.getTipo())) {
            return "Tipo de asiento no válido: " + asiento.getTipo();
        }
        if (asiento.getEstado() != null && !ESTADOS_VALIDOS.contains(asiento.getEstado())) {
            return "Estado de asiento no válido: " + asiento.getEstado();
        }
        return null;
    }

    public Asiento prepararParaRegistro(Asiento asiento) {
        if (asiento == null) {
            return null;
        }
        if (asiento.getTipo() == null || asiento.getTipo().trim().isEmpty()) {
            asiento.setTipo("Normal");
        }
        if (asiento.getEstado() == null || asiento.getEstado().trim().isEmpty()) {
            asiento.setEstado("Disponible");
        }
        return asiento;
    }
}