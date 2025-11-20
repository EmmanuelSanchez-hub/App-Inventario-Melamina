package com.cesarcort.inventario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicación CesarCort
 * Sistema de Inventario de Melamina y Madera
 * 
 * @author CesarCort Team
 * @version 1.0.0
 */
@SpringBootApplication
public class CesarCortApplication {

    public static void main(String[] args) {
        SpringApplication.run(CesarCortApplication.class, args);
        System.out.println("===========================================");
        System.out.println("   🚀 CesarCort Inventario Iniciado");
        System.out.println("   📦 Sistema de Gestión de Melamina");
        System.out.println("   🌐 http://localhost:8080");
        System.out.println("===========================================");
    }
}