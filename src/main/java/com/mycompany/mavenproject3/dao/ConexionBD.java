/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    private static final String URL = System.getenv("DB_URL") != null
            ? System.getenv("DB_URL")
            : "jdbc:sqlserver://DESKTOP-QUBFGRV\\SQLEXPRESS;databaseName=EmpresaTransporte;encrypt=true;trustServerCertificate=true;";

    private static final String USUARIO = System.getenv("DB_USER") != null
            ? System.getenv("DB_USER")
            : "sa";

    private static final String CLAVE = System.getenv("DB_PASSWORD") != null
            ? System.getenv("DB_PASSWORD")
            : "sqlvillatoro";

    public static Connection obtenerConexion() {
        Connection cn = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            cn = DriverManager.getConnection(URL, USUARIO, CLAVE);
            System.out.println("¡Conexion establecida con exito!");
        } catch (ClassNotFoundException e) {
            System.err.println("Error: Driver JDBC no encontrado. " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error de SQL al conectar: " + e.getMessage());
        }
        return cn;
    }
}