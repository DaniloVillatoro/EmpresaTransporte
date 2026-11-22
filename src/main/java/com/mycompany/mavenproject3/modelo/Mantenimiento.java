/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3.modelo;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 *
 * @author Dell
 */

public class Mantenimiento {
    private int idMantenimiento;
    private int idBus;
    private Integer idEmpleado;
    private LocalDate fecha;
    private String tipo;
    private String descripcion;
    private Integer kilometraje;
    private BigDecimal costo;
    private String estado;

    public Mantenimiento() {}

    public Mantenimiento(int idMantenimiento, int idBus, Integer idEmpleado, LocalDate fecha, String tipo, String descripcion, Integer kilometraje, BigDecimal costo, String estado) {
        this.idMantenimiento = idMantenimiento;
        this.idBus = idBus;
        this.idEmpleado = idEmpleado;
        this.fecha = fecha;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.kilometraje = kilometraje;
        this.costo = costo;
        this.estado = estado;
    }

    

    public int getIdMantenimiento() { return idMantenimiento; }
    public void setIdMantenimiento(int idMantenimiento) { this.idMantenimiento = idMantenimiento; }
    public int getIdBus() { return idBus; }
    public void setIdBus(int idBus) { this.idBus = idBus; }
    public Integer getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(Integer idEmpleado) { this.idEmpleado = idEmpleado; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Integer getKilometraje() { return kilometraje; }
    public void setKilometraje(Integer kilometraje) { this.kilometraje = kilometraje; }
    public BigDecimal getCosto() { return costo; }
    public void setCosto(BigDecimal costo) { this.costo = costo; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
