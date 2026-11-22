/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3.controlador;
import com.mycompany.mavenproject3.modelo.Cliente;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Dell
 */

public class ClienteControlador {

    private static final List<String> ESTADOS_VALIDOS = Arrays.asList("Activo", "Inactivo");

    public String validarCliente(Cliente cliente) {
        if (cliente == null) {
            return "Los datos del cliente no pueden ser nulos.";
        }
        if (cliente.getNombre() == null || cliente.getNombre().trim().isEmpty()) {
            return "El nombre del cliente es obligatorio.";
        }
        if (cliente.getApellido() == null || cliente.getApellido().trim().isEmpty()) {
            return "El apellido del cliente es obligatorio.";
        }
        if (cliente.getEstado() != null && !ESTADOS_VALIDOS.contains(cliente.getEstado())) {
            return "Estado de cliente no válido: " + cliente.getEstado();
        }
        return null;
    }

    public Cliente prepararParaRegistro(Cliente cliente) {
        if (cliente == null) {
            return null;
        }
        cliente.setNombre(cliente.getNombre().trim());
        cliente.setApellido(cliente.getApellido().trim());
        if (cliente.getIdentidad() != null) {
            cliente.setIdentidad(cliente.getIdentidad().trim());
        }
        if (cliente.getCorreo() != null) {
            cliente.setCorreo(cliente.getCorreo().trim().toLowerCase());
        }
        if (cliente.getFechaRegistro() == null) {
            cliente.setFechaRegistro(LocalDateTime.now());
        }
        if (cliente.getEstado() == null || cliente.getEstado().trim().isEmpty()) {
            cliente.setEstado("Activo");
        }
        return cliente;
    }
}
