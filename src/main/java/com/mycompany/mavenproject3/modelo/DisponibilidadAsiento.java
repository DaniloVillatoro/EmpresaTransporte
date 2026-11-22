/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3.modelo;
import java.time.LocalDateTime;

/**
 *
 * @author Dell
 */

public class DisponibilidadAsiento {
    private int idDisponibilidad;
    private int idHorario;
    private int idAsiento;
    private String estado;
    private LocalDateTime fechaActualizacion;

    public DisponibilidadAsiento() {}

    public DisponibilidadAsiento(int idDisponibilidad, int idHorario, int idAsiento,
                                 String estado, LocalDateTime fechaActualizacion) {
        this.idDisponibilidad = idDisponibilidad;
        this.idHorario = idHorario;
        this.idAsiento = idAsiento;
        this.estado = estado;
        this.fechaActualizacion = fechaActualizacion;
    }

    public int getIdDisponibilidad() { return idDisponibilidad; }
    public void setIdDisponibilidad(int idDisponibilidad) { this.idDisponibilidad = idDisponibilidad; }
    public int getIdHorario() { return idHorario; }
    public void setIdHorario(int idHorario) { this.idHorario = idHorario; }
    public int getIdAsiento() { return idAsiento; }
    public void setIdAsiento(int idAsiento) { this.idAsiento = idAsiento; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}