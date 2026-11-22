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

public class Conductor {
    private int idConductor;
    private String nombre;
    private String apellido;
    private String identidad;
    private String telefono;
    private String direccion;
    private String licencia;
    private LocalDate fechaVencimientoLicencia;
    private LocalDate fechaContratacion;
    private String estado;

    public Conductor() {}

    public Conductor(int idConductor, String nombre, String apellido, String identidad,
                     String telefono, String direccion, String licencia,
                     LocalDate fechaVencimientoLicencia, LocalDate fechaContratacion, String estado) {
        this.idConductor = idConductor;
        this.nombre = nombre;
        this.apellido = apellido;
        this.identidad = identidad;
        this.telefono = telefono;
        this.direccion = direccion;
        this.licencia = licencia;
        this.fechaVencimientoLicencia = fechaVencimientoLicencia;
        this.fechaContratacion = fechaContratacion;
        this.estado = estado;
    }

    public int getIdConductor() { return idConductor; }
    public void setIdConductor(int idConductor) { this.idConductor = idConductor; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getIdentidad() { return identidad; }
    public void setIdentidad(String identidad) { this.identidad = identidad; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getLicencia() { return licencia; }
    public void setLicencia(String licencia) { this.licencia = licencia; }
    public LocalDate getFechaVencimientoLicencia() { return fechaVencimientoLicencia; }
    public void setFechaVencimientoLicencia(LocalDate fechaVencimientoLicencia) { this.fechaVencimientoLicencia = fechaVencimientoLicencia; }
    public LocalDate getFechaContratacion() { return fechaContratacion; }
    public void setFechaContratacion(LocalDate fechaContratacion) { this.fechaContratacion = fechaContratacion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public String toString() {
        return nombre + " " + apellido + " (" + licencia + ")";
    }
}