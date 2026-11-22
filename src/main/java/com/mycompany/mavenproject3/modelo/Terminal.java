/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3.modelo;

/**
 *
 * @author Dell
 */
public class Terminal {
    private int idTerminal;
    private String nombre;
    private String ciudad;
    private String direccion;
    private String telefono;
    private String correo;
    private String estado;

    public Terminal() {}

    public Terminal(int idTerminal, String nombre, String ciudad, String direccion,
                    String telefono, String correo, String estado) {
        this.idTerminal = idTerminal;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.direccion = direccion;
        this.telefono = telefono;
        this.correo = correo;
        this.estado = estado;
    }

    public int getIdTerminal() { return idTerminal; }
    public void setIdTerminal(int idTerminal) { this.idTerminal = idTerminal; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public String toString() {
        return nombre + " (" + ciudad + ") - " + estado;
    }
}