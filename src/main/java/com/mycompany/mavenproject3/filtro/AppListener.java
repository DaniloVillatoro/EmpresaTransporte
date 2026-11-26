package com.mycompany.mavenproject3.filtro;

import com.mycompany.mavenproject3.dao.ConexionBD;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/** Cierra el pool de conexiones cuando se detiene la aplicacion. */
@WebListener
public class AppListener implements ServletContextListener {
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ConexionBD.cerrar();
    }
}
