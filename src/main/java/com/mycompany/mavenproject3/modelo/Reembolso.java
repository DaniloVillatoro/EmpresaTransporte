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

public class Reembolso {
    private int idReembolso;
    private int idPago;
    private int idEmpleado;
    private int idMetodoPago;
    private LocalDateTime fechaReembolso;
    private BigDecimal monto;
    private String motivo;
    private String estado;

    public Reembolso() {}

    public Reembolso(int idReembolso, int idPago, int idEmpleado, int idMetodoPago,
                     LocalDateTime fechaReembolso, BigDecimal monto, String motivo, String estado) {
        this.idReembolso = idReembolso;
        this.idPago = idPago;
        this.idEmpleado = idEmpleado;
        this.idMetodoPago = idMetodoPago;
        this.fechaReembolso = fechaReembolso;
        this.monto = monto;
        this.motivo = motivo;
        this.estado = estado;
    }

    public int getIdReembolso() { return idReembolso; }
    public void setIdReembolso(int idReembolso) { this.idReembolso = idReembolso; }
    public int getIdPago() { return idPago; }
    public void setIdPago(int idPago) { this.idPago = idPago; }
    public int getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(int idEmpleado) { this.idEmpleado = idEmpleado; }
    public int getIdMetodoPago() { return idMetodoPago; }
    public void setIdMetodoPago(int idMetodoPago) { this.idMetodoPago = idMetodoPago; }
    public LocalDateTime getFechaReembolso() { return fechaReembolso; }
    public void setFechaReembolso(LocalDateTime fechaReembolso) { this.fechaReembolso = fechaReembolso; }
    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
