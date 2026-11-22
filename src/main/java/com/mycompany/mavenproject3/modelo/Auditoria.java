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

public class Auditoria {
    private long idAuditoria;
    private String tablaAfectada;
    private long idRegistro;
    private String accion;
    private String valorAnterior;
    private String valorNuevo;
    private String usuario;
    private LocalDateTime fechaCambio;
    private String equipo;
    private String aplicacion;

    public Auditoria() {}

    public Auditoria(long idAuditoria, String tablaAfectada, long idRegistro, String accion,
                     String valorAnterior, String valorNuevo, String usuario,
                     LocalDateTime fechaCambio, String equipo, String aplicacion) {
        this.idAuditoria = idAuditoria;
        this.tablaAfectada = tablaAfectada;
        this.idRegistro = idRegistro;
        this.accion = accion;
        this.valorAnterior = valorAnterior;
        this.valorNuevo = valorNuevo;
        this.usuario = usuario;
        this.fechaCambio = fechaCambio;
        this.equipo = equipo;
        this.aplicacion = aplicacion;
    }

    public long getIdAuditoria() { return idAuditoria; }
    public void setIdAuditoria(long idAuditoria) { this.idAuditoria = idAuditoria; }
    public String getTablaAfectada() { return tablaAfectada; }
    public void setTablaAfectada(String tablaAfectada) { this.tablaAfectada = tablaAfectada; }
    public long getIdRegistro() { return idRegistro; }
    public void setIdRegistro(long idRegistro) { this.idRegistro = idRegistro; }
    public String getAccion() { return accion; }
    public void setAccion(String accion) { this.accion = accion; }
    public String getValorAnterior() { return valorAnterior; }
    public void setValorAnterior(String valorAnterior) { this.valorAnterior = valorAnterior; }
    public String getValorNuevo() { return valorNuevo; }
    public void setValorNuevo(String valorNuevo) { this.valorNuevo = valorNuevo; }
    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public LocalDateTime getFechaCambio() { return fechaCambio; }
    public void setFechaCambio(LocalDateTime fechaCambio) { this.fechaCambio = fechaCambio; }
    public String getEquipo() { return equipo; }
    public void setEquipo(String equipo) { this.equipo = equipo; }
    public String getAplicacion() { return aplicacion; }
    public void setAplicacion(String aplicacion) { this.aplicacion = aplicacion; }
}