/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3.modelo;

/**
 *
 * @author Dell
 */
public class Asiento {
    private int idAsiento;
    private int idBus;
    private int numeroAsiento;
    private String tipo;
    private String ubicacion;
    private String estado;

    public Asiento() {}

    public Asiento(int idAsiento, int idBus, int numeroAsiento, String tipo,
                   String ubicacion, String estado) {
        this.idAsiento = idAsiento;
        this.idBus = idBus;
        this.numeroAsiento = numeroAsiento;
        this.tipo = tipo;
        this.ubicacion = ubicacion;
        this.estado = estado;
    }

    public int getIdAsiento() { return idAsiento; }
    public void setIdAsiento(int idAsiento) { this.idAsiento = idAsiento; }
    public int getIdBus() { return idBus; }
    public void setIdBus(int idBus) { this.idBus = idBus; }
    public int getNumeroAsiento() { return numeroAsiento; }
    public void setNumeroAsiento(int numeroAsiento) { this.numeroAsiento = numeroAsiento; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}