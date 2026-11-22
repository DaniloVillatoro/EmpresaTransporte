/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3.modelo;

/**
 *
 * @author Dell
 */
import java.math.BigDecimal;
import java.time.LocalDate;

public class Empleado {
    private int idEmpleado;
    private String nombre;
    private String apellido;
    private String identidad;
    private String telefono;
    private String correo;
    private String cargo;
    private String usuario;
    private String contrasenaHash;
    private LocalDate fechaContratacion;
    private BigDecimal salario;
    private String estado;

    public Empleado() {}

    public Empleado(int idEmpleado, String nombre, String apellido, String identidad,
                    String telefono, String correo, String cargo, String usuario,
                    String contrasenaHash, LocalDate fechaContratacion,
                    BigDecimal salario, String estado) {
        this.idEmpleado = idEmpleado;
        this.nombre = nombre;
        this.apellido = apellido;
        this.identidad = identidad;
        this.telefono = telefono;
        this.correo = correo;
        this.cargo = cargo;
        this.usuario = usuario;
        this.contrasenaHash = contrasenaHash;
        this.fechaContratacion = fechaContratacion;
        this.salario = salario;
        this.estado = estado;
    }

    public int getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(int idEmpleado) { this.idEmpleado = idEmpleado; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getIdentidad() { return identidad; }
    public void setIdentidad(String identidad) { this.identidad = identidad; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }
    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public String getContrasenaHash() { return contrasenaHash; }
    public void setContrasenaHash(String contrasenaHash) { this.contrasenaHash = contrasenaHash; }
    public LocalDate getFechaContratacion() { return fechaContratacion; }
    public void setFechaContratacion(LocalDate fechaContratacion) { this.fechaContratacion = fechaContratacion; }
    public BigDecimal getSalario() { return salario; }
    public void setSalario(BigDecimal salario) { this.salario = salario; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
