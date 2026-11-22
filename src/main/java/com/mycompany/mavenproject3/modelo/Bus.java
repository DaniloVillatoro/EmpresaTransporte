/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3.modelo;
import java.time.LocalDate;

/**
 *
 * @author Dell
 */

public class Bus {
    private int idBus;
    private String placa;
    private String marca;
    private String modelo;
    private Integer anio;
    private String tipoBus;
    private int capacidad;
    private LocalDate fechaAdquisicion;
    private String estado;

    public Bus() {}

    public Bus(int idBus, String placa, String marca, String modelo, Integer anio,
               String tipoBus, int capacidad, LocalDate fechaAdquisicion, String estado) {
        this.idBus = idBus;
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.tipoBus = tipoBus;
        this.capacidad = capacidad;
        this.fechaAdquisicion = fechaAdquisicion;
        this.estado = estado;
    }

    public int getIdBus() { return idBus; }
    public void setIdBus(int idBus) { this.idBus = idBus; }
    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }
    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public Integer getAnio() { return anio; }
    public void setAnio(Integer anio) { this.anio = anio; }
    public String getTipoBus() { return tipoBus; }
    public void setTipoBus(String tipoBus) { this.tipoBus = tipoBus; }
    public int getCapacidad() { return capacidad; }
    public void setCapacidad(int capacidad) { this.capacidad = capacidad; }
    public LocalDate getFechaAdquisicion() { return fechaAdquisicion; }
    public void setFechaAdquisicion(LocalDate fechaAdquisicion) { this.fechaAdquisicion = fechaAdquisicion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public String toString() {
        return placa + " - " + marca + " " + modelo + " [" + estado + "]";
    }
}