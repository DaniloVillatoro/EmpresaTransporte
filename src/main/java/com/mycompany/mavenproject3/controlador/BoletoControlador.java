/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3.controlador;
import com.mycompany.mavenproject3.modelo.Boleto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
/**
 *
 * @author Dell
 */


public class BoletoControlador {

    private static final List<String> METODOS_COMPRA_VALIDOS = Arrays.asList("Taquilla", "En linea");
    private static final List<String> ESTADOS_VALIDOS = Arrays.asList(
            "Pendiente", "Pagado", "Cancelado", "Reembolsado"
    );

    public String validarBoleto(Boleto boleto) {
        if (boleto == null) {
            return "Los datos del boleto no pueden ser nulos.";
        }
        if (boleto.getIdCliente() <= 0) {
            return "Debe asociar un cliente válido al boleto.";
        }
        if (boleto.getIdDisponibilidad() <= 0) {
            return "Debe seleccionar una disponibilidad de asiento válida.";
        }
        if (boleto.getPrecio() == null || boleto.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
            return "El precio del boleto debe ser mayor que cero.";
        }
        String metodo = boleto.getMetodoCompra();
        if (metodo != null && !METODOS_COMPRA_VALIDOS.contains(metodo)) {
            return "Método de compra no válido: " + metodo;
        }
        String estado = boleto.getEstado();
        if (estado != null && !ESTADOS_VALIDOS.contains(estado)) {
            return "Estado de boleto no válido: " + estado;
        }
        return null;
    }

    public Boleto prepararParaRegistro(Boleto boleto) {
        if (boleto == null) {
            return null;
        }
        if (boleto.getCodigoBoleto() == null || boleto.getCodigoBoleto().trim().isEmpty()) {
            String sufijo = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            boleto.setCodigoBoleto("BOL-" + sufijo);
        } else {
            boleto.setCodigoBoleto(boleto.getCodigoBoleto().trim().toUpperCase());
        }
        if (boleto.getFechaCompra() == null) {
            boleto.setFechaCompra(LocalDateTime.now());
        }
        if (boleto.getMetodoCompra() == null || boleto.getMetodoCompra().trim().isEmpty()) {
            boleto.setMetodoCompra("Taquilla");
        }
        if (boleto.getEstado() == null || boleto.getEstado().trim().isEmpty()) {
            boleto.setEstado("Pendiente");
        }
        return boleto;
    }
}