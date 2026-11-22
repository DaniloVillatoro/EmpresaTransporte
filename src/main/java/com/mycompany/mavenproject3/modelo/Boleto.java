/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3.modelo;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *
 * @author Dell
 */

public class Boleto {
    private int idBoleto;
    private String codigoBoleto;
    private int idCliente;
    private int idDisponibilidad;
    private Integer idEmpleado;
    private LocalDateTime fechaCompra;
    private BigDecimal precio;
    private String metodoCompra;
    private String estado;

    public Boleto() {}

    public Boleto(int idBoleto, String codigoBoleto, int idCliente, int idDisponibilidad,
                  Integer idEmpleado, LocalDateTime fechaCompra, BigDecimal precio,
                  String metodoCompra, String estado) {
        this.idBoleto = idBoleto;
        this.codigoBoleto = codigoBoleto;
        this.idCliente = idCliente;
        this.idDisponibilidad = idDisponibilidad;
        this.idEmpleado = idEmpleado;
        this.fechaCompra = fechaCompra;
        this.precio = precio;
        this.metodoCompra = metodoCompra;
        this.estado = estado;
    }

    public int getIdBoleto() { return idBoleto; }
    public void setIdBoleto(int idBoleto) { this.idBoleto = idBoleto; }
    public String getCodigoBoleto() { return codigoBoleto; }
    public void setCodigoBoleto(String codigoBoleto) { this.codigoBoleto = codigoBoleto; }
    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }
    public int getIdDisponibilidad() { return idDisponibilidad; }
    public void setIdDisponibilidad(int idDisponibilidad) { this.idDisponibilidad = idDisponibilidad; }
    public Integer getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(Integer idEmpleado) { this.idEmpleado = idEmpleado; }
    public LocalDateTime getFechaCompra() { return fechaCompra; }
    public void setFechaCompra(LocalDateTime fechaCompra) { this.fechaCompra = fechaCompra; }
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    public String getMetodoCompra() { return metodoCompra; }
    public void setMetodoCompra(String metodoCompra) { this.metodoCompra = metodoCompra; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}