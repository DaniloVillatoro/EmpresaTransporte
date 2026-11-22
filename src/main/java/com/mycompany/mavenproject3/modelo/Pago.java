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

public class Pago {
    private int idPago;
    private int idBoleto;
    private int idMetodoPago;
    private BigDecimal monto;
    private LocalDateTime fechaPago;
    private String referenciaPago;
    private String estadoPago;

    public Pago() {}

    public Pago(int idPago, int idBoleto, int idMetodoPago, BigDecimal monto,
                LocalDateTime fechaPago, String referenciaPago, String estadoPago) {
        this.idPago = idPago;
        this.idBoleto = idBoleto;
        this.idMetodoPago = idMetodoPago;
        this.monto = monto;
        this.fechaPago = fechaPago;
        this.referenciaPago = referenciaPago;
        this.estadoPago = estadoPago;
    }

    public int getIdPago() { return idPago; }
    public void setIdPago(int idPago) { this.idPago = idPago; }
    public int getIdBoleto() { return idBoleto; }
    public void setIdBoleto(int idBoleto) { this.idBoleto = idBoleto; }
    public int getIdMetodoPago() { return idMetodoPago; }
    public void setIdMetodoPago(int idMetodoPago) { this.idMetodoPago = idMetodoPago; }
    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    public LocalDateTime getFechaPago() { return fechaPago; }
    public void setFechaPago(LocalDateTime fechaPago) { this.fechaPago = fechaPago; }
    public String getReferenciaPago() { return referenciaPago; }
    public void setReferenciaPago(String referenciaPago) { this.referenciaPago = referenciaPago; }
    public String getEstadoPago() { return estadoPago; }
    public void setEstadoPago(String estadoPago) { this.estadoPago = estadoPago; }
}
