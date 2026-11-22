/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3.controlador;
import com.mycompany.mavenproject3.modelo.Empleado;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Dell
 */

public class EmpleadoControlador {

    private static final List<String> ESTADOS_VALIDOS = Arrays.asList("Activo", "Inactivo");

    public String validarEmpleado(Empleado empleado) {
        if (empleado == null) {
            return "Los datos del empleado no pueden ser nulos.";
        }
        if (empleado.getNombre() == null || empleado.getNombre().trim().isEmpty()) {
            return "El nombre del empleado es obligatorio.";
        }
        if (empleado.getApellido() == null || empleado.getApellido().trim().isEmpty()) {
            return "El apellido del empleado es obligatorio.";
        }
        if (empleado.getIdentidad() == null || empleado.getIdentidad().trim().isEmpty()) {
            return "La identidad del empleado es obligatoria.";
        }
        if (empleado.getCargo() == null || empleado.getCargo().trim().isEmpty()) {
            return "El cargo del empleado es obligatorio.";
        }
        if (empleado.getUsuario() == null || empleado.getUsuario().trim().isEmpty()) {
            return "El nombre de usuario es obligatorio.";
        }
        if (empleado.getContrasenaHash() == null || empleado.getContrasenaHash().trim().isEmpty()) {
            return "La contraseña o hash de contraseña es obligatorio.";
        }
        if (empleado.getSalario() != null && empleado.getSalario().compareTo(BigDecimal.ZERO) < 0) {
            return "El salario no puede ser negativo.";
        }
        if (empleado.getEstado() != null && !ESTADOS_VALIDOS.contains(empleado.getEstado())) {
            return "Estado de empleado no válido: " + empleado.getEstado();
        }
        return null;
    }

    public Empleado prepararParaRegistro(Empleado empleado) {
        if (empleado == null) {
            return null;
        }
        empleado.setNombre(empleado.getNombre().trim());
        empleado.setApellido(empleado.getApellido().trim());
        empleado.setIdentidad(empleado.getIdentidad().trim());
        empleado.setCargo(empleado.getCargo().trim());
        empleado.setUsuario(empleado.getUsuario().trim().toLowerCase());
        if (empleado.getCorreo() != null) {
            empleado.setCorreo(empleado.getCorreo().trim().toLowerCase());
        }
        if (empleado.getFechaContratacion() == null) {
            empleado.setFechaContratacion(LocalDate.now());
        }
        if (empleado.getEstado() == null || empleado.getEstado().trim().isEmpty()) {
            empleado.setEstado("Activo");
        }
        return empleado;
    }
}