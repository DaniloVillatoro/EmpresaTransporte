/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3.controlador;
import com.mycompany.mavenproject3.modelo.Ruta;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
/**
 *
 * @author Dell
 */


public class RutaControlador {

    private static final List<String> ESTADOS_VALIDOS = Arrays.asList("Activa", "Inactiva");

    public String validarRuta(Ruta ruta) {
        if (ruta == null) {
            return "Los datos de la ruta no pueden ser nulos.";
        }
        if (ruta.getNombreRuta() == null || ruta.getNombreRuta().trim().isEmpty()) {
            return "El nombre de la ruta es obligatorio.";
        }
        if (ruta.getTerminalOrigen() <= 0 || ruta.getTerminalDestino() <= 0) {
            return "Debe seleccionar una terminal de origen y una de destino válidas.";
        }
        if (ruta.getTerminalOrigen() == ruta.getTerminalDestino()) {
            return "La terminal de origen y la terminal de destino no pueden ser la misma.";
        }
        if (ruta.getDistanciaKm() == null || ruta.getDistanciaKm().compareTo(BigDecimal.ZERO) <= 0) {
            return "La distancia en kilómetros debe ser mayor que cero.";
        }
        if (ruta.getDuracionEstimada() == null) {
            return "La duración estimada de la ruta es obligatoria.";
        }
        if (ruta.getEstado() != null && !ESTADOS_VALIDOS.contains(ruta.getEstado())) {
            return "Estado de ruta no válido: " + ruta.getEstado();
        }
        return null;
    }

    public Ruta prepararParaRegistro(Ruta ruta) {
        if (ruta == null) {
            return null;
        }
        ruta.setNombreRuta(ruta.getNombreRuta().trim());
        if (ruta.getEstado() == null || ruta.getEstado().trim().isEmpty()) {
            ruta.setEstado("Activa");
        }
        return ruta;
    }
}