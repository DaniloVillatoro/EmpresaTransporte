/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public final class ConexionBD {

    private static HikariDataSource pool;

    private static final String DEFAULT_URL =
            "jdbc:postgresql://ep-lingering-scene-bdo52v5s-pooler.c-8.us-east-2.aws.neon.tech:5432/neondb?sslmode=require";
    private static final String DEFAULT_USER = "neondb_owner";
    private static final String DEFAULT_PASS = "DB_PASSWORD";

    private ConexionBD() { }

    private static synchronized HikariDataSource pool() throws SQLException {
        if (pool == null) {
            Properties p = new Properties();
            try (InputStream in = ConexionBD.class.getResourceAsStream("/db.properties")) {
                if (in != null) {
                    p.load(in);
                }
            } catch (IOException e) {
                throw new SQLException("No se pudo leer db.properties", e);
            }

            HikariConfig cfg = new HikariConfig();
            cfg.setDriverClassName("org.postgresql.Driver");
            cfg.setJdbcUrl(valor("DB_URL", p.getProperty("db.url", DEFAULT_URL)));
            cfg.setUsername(valor("DB_USER", p.getProperty("db.user", DEFAULT_USER)));
            cfg.setPassword(valor("DB_PASSWORD", p.getProperty("db.password", DEFAULT_PASS)));
            cfg.setMaximumPoolSize(Integer.parseInt(p.getProperty("db.pool.max", "10")));
            cfg.setConnectionTimeout(8000);
            cfg.setPoolName("Neon-Pool");
            pool = new HikariDataSource(cfg);
        }
        return pool;
    }

    private static String valor(String env, String porDefecto) {
        String v = System.getenv(env);
        return (v != null && !v.isBlank()) ? v : porDefecto;
    }

    public static Connection obtenerConexion() throws SQLException {
        return pool().getConnection();
    }

    public static synchronized void cerrar() {
        if (pool != null) {
            pool.close();
            pool = null;
        }
    }
}