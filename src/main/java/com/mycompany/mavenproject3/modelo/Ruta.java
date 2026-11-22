/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3.modelo;
import java.math.BigDecimal;
import java.time.LocalTime;

/**
 *
 * @author Dell
 */

public class Ruta {
    private int idRuta;
    private String nombreRuta;
    private int terminalOrigen;
    private int terminalDestino;
    private BigDecimal distanciaKm;
    private LocalTime duracionEstimada;
    private String estado;

    public Ruta() {}

    public Ruta(int idRuta, String nombreRuta, int terminalOrigen, int terminalDestino,
                BigDecimal distanciaKm, LocalTime duracionEstimada, String estado) {
        this.idRuta = idRuta;
        this.nombreRuta = nombreRuta;
        this.terminalOrigen = terminalOrigen;
        this.terminalDestino = terminalDestino;
        this.distanciaKm = distanciaKm;
        this.duracionEstimada = duracionEstimada;
        this.estado = estado;
    }

    public int getIdRuta() { return idRuta; }
    public void setIdRuta(int idRuta) { this.idRuta = idRuta; }
    public String getNombreRuta() { return nombreRuta; }
    public void setNombreRuta(String nombreRuta) { this.nombreRuta = nombreRuta; }
    public int getTerminalOrigen() { return terminalOrigen; }
    public void setTerminalOrigen(int terminalOrigen) { this.terminalOrigen = terminalOrigen; }
    public int getTerminalDestino() { return terminalDestino; }
    public void setTerminalDestino(int terminalDestino) { this.terminalDestino = terminalDestino; }
    public BigDecimal getDistanciaKm() { return distanciaKm; }
    public void setDistanciaKm(BigDecimal distanciaKm) { this.distanciaKm = distanciaKm; }
    public LocalTime getDuracionEstimada() { return duracionEstimada; }
    public void setDuracionEstimada(LocalTime duracionEstimada) { this.duracionEstimada = duracionEstimada; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public String toString() {
        return nombreRuta + " (" + distanciaKm + " km)";
    }
}