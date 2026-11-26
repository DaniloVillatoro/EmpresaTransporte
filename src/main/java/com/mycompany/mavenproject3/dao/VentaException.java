package com.mycompany.mavenproject3.dao;

/** Error de regla de negocio durante una venta (asiento ocupado, cliente incompleto, etc.). */
public class VentaException extends Exception {
    public VentaException(String mensaje) {
        super(mensaje);
    }
}
