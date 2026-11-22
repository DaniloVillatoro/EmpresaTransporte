/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3.modelo;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author Dell
 */

public class Horario {
    private int idHorario;
    private int idRuta;
    private int idBus;
    private int idConductor;
    private LocalDate fechaSalida;
    private LocalTime horaSalida;
    private LocalDate fechaLlegada;
    private LocalTime horaLlegada;
    private String estado;

    public Horario() {}

    public Horario(int idHorario, int idRuta, int idBus, int idConductor,
                   LocalDate fechaSalida, LocalTime horaSalida, LocalDate fechaLlegada,
                   LocalTime horaLlegada, String estado) {
        this.idHorario = idHorario;
        this.idRuta = idRuta;
        this.idBus = idBus;
        this.idConductor = idConductor;
        this.fechaSalida = fechaSalida;
        this.horaSalida = horaSalida;
        this.fechaLlegada = fechaLlegada;
        this.horaLlegada = horaLlegada;
        this.estado = estado;
    }

    public int getIdHorario() { return idHorario; }
    public void setIdHorario(int idHorario) { this.idHorario = idHorario; }
    public int getIdRuta() { return idRuta; }
    public void setIdRuta(int idRuta) { this.idRuta = idRuta; }
    public int getIdBus() { return idBus; }
    public void setIdBus(int idBus) { this.idBus = idBus; }
    public int getIdConductor() { return idConductor; }
    public void setIdConductor(int idConductor) { this.idConductor = idConductor; }
    public LocalDate getFechaSalida() { return fechaSalida; }
    public void setFechaSalida(LocalDate fechaSalida) { this.fechaSalida = fechaSalida; }
    public LocalTime getHoraSalida() { return horaSalida; }
    public void setHoraSalida(LocalTime horaSalida) { this.horaSalida = horaSalida; }
    public LocalDate getFechaLlegada() { return fechaLlegada; }
    public void setFechaLlegada(LocalDate fechaLlegada) { this.fechaLlegada = fechaLlegada; }
    public LocalTime getHoraLlegada() { return horaLlegada; }
    public void setHoraLlegada(LocalTime horaLlegada) { this.horaLlegada = horaLlegada; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
