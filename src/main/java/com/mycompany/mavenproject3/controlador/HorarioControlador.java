/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3.controlador;
import com.mycompany.mavenproject3.modelo.Horario;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Dell
 */

public class HorarioControlador {

    private static final List<String> ESTADOS_VALIDOS = Arrays.asList(
            "Programado", "En curso", "Finalizado", "Cancelado"
    );

    public String validarHorario(Horario horario) {
        if (horario == null) {
            return "Los datos del horario no pueden ser nulos.";
        }
        if (horario.getIdRuta() <= 0 || horario.getIdBus() <= 0 || horario.getIdConductor() <= 0) {
            return "Debe asignar una ruta, un bus y un conductor válidos.";
        }
        if (horario.getFechaSalida() == null || horario.getHoraSalida() == null
                || horario.getFechaLlegada() == null || horario.getHoraLlegada() == null) {
            return "Las fechas y horas de salida y llegada son obligatorias.";
        }

        LocalDateTime salida = LocalDateTime.of(horario.getFechaSalida(), horario.getHoraSalida());
        LocalDateTime llegada = LocalDateTime.of(horario.getFechaLlegada(), horario.getHoraLlegada());

        if (!llegada.isAfter(salida)) {
            return "La fecha y hora de llegada deben ser posteriores a la salida.";
        }
        if (horario.getEstado() != null && !ESTADOS_VALIDOS.contains(horario.getEstado())) {
            return "Estado de horario no válido: " + horario.getEstado();
        }
        return null;
    }

    public Horario prepararParaRegistro(Horario horario) {
        if (horario == null) {
            return null;
        }
        if (horario.getEstado() == null || horario.getEstado().trim().isEmpty()) {
            horario.setEstado("Programado");
        }
        return horario;
    }
}