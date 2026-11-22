/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3.controlador;
import com.mycompany.mavenproject3.modelo.Terminal;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Dell
 */

public class TerminalControlador {

    private static final List<String> ESTADOS_VALIDOS = Arrays.asList("Activa", "Inactiva");

    public String validarTerminal(Terminal terminal) {
        if (terminal == null) {
            return "Los datos de la terminal no pueden ser nulos.";
        }
        if (terminal.getNombre() == null || terminal.getNombre().trim().isEmpty()) {
            return "El nombre de la terminal es obligatorio.";
        }
        if (terminal.getCiudad() == null || terminal.getCiudad().trim().isEmpty()) {
            return "La ciudad de la terminal es obligatoria.";
        }
        if (terminal.getEstado() != null && !ESTADOS_VALIDOS.contains(terminal.getEstado())) {
            return "Estado de terminal no válido: " + terminal.getEstado();
        }
        return null;
    }

    public Terminal prepararParaRegistro(Terminal terminal) {
        if (terminal == null) {
            return null;
        }
        terminal.setNombre(terminal.getNombre().trim());
        terminal.setCiudad(terminal.getCiudad().trim());
        if (terminal.getCorreo() != null) {
            terminal.setCorreo(terminal.getCorreo().trim().toLowerCase());
        }
        if (terminal.getEstado() == null || terminal.getEstado().trim().isEmpty()) {
            terminal.setEstado("Activa");
        }
        return terminal;
    }
}
