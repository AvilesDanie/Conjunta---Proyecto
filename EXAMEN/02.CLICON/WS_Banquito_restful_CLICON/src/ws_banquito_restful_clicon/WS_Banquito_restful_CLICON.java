package ws_banquito_restful_clicon;

import ec.edu.monster.vista.*;

/**
 * Cliente de consola para consumir servicios RESTful de BanQuito y Comercializadora
 * Sistema integrado con autenticación y menús específicos
 * @author CLICON
 */
public class WS_Banquito_restful_CLICON {
    
    private static final AppSelectionVista appSelectionVista = new AppSelectionVista();
    private static final LoginBanquitoVista loginBanquitoVista = new LoginBanquitoVista();
    private static final LoginComercializadoraVista loginComercializadoraVista = new LoginComercializadoraVista();
    private static final HomeBanquitoVista homeBanquitoVista = new HomeBanquitoVista();
    private static final HomeComercializadoraVista homeComercializadoraVista = new HomeComercializadoraVista();
    
    /**
     * Método principal que inicia el sistema
     * @param args argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        mostrarSplash();
        
        // Bucle principal del sistema
        while (true) {
            // 1. Selección de aplicación
            int seleccion = appSelectionVista.mostrarSeleccion();
            
            if (seleccion == 0) {
                // Usuario eligió salir
                mostrarDespedida();
                break;
            }
            
            boolean loginExitoso = false;
            
            // 2. Login según la aplicación seleccionada
            if (seleccion == 1) {
                // Login BanQuito
                loginExitoso = loginBanquitoVista.mostrarLogin();
            } else if (seleccion == 2) {
                // Login Comercializadora
                loginExitoso = loginComercializadoraVista.mostrarLogin();
            }
            
            // 3. Si login exitoso, mostrar menú correspondiente
            if (loginExitoso) {
                boolean salirDelSistema = false;
                
                if (seleccion == 1) {
                    // Menú BanQuito
                    salirDelSistema = homeBanquitoVista.mostrarMenu();
                } else if (seleccion == 2) {
                    // Menú Comercializadora
                    salirDelSistema = homeComercializadoraVista.mostrarMenu();
                }
                
                // Si el usuario eligió salir del sistema completamente
                if (salirDelSistema) {
                    mostrarDespedida();
                    break;
                }
                // Si no, vuelve al ciclo (cerró sesión, regresa a selección)
            }
            // Si login falló o usuario eligió volver, regresa a selección
        }
    }
    
    /**
     * Muestra la pantalla splash de inicio
     */
    private static void mostrarSplash() {
        limpiarConsola();
        System.out.println("\n");
        System.out.println("  ╔═══════════════════════════════════════════════════════╗");
        System.out.println("  ║                                                       ║");
        System.out.println("  ║        🏢 SISTEMA BANQUITO & ELECTROQUITO 🏢          ║");
        System.out.println("  ║                                                       ║");
        System.out.println("  ║          Electrodomésticos con Financiamiento         ║");
        System.out.println("  ║                                                       ║");
        System.out.println("  ╚═══════════════════════════════════════════════════════╝");
        System.out.println("\n     📡 Servidor: 10.40.18.255:8080");
        System.out.println("     📦 Versión: 1.0 - Sistema Integrado");
        System.out.println("     ⏳ Iniciando...\n");
        
        // Simular carga
        try {
            Thread.sleep(1200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Limpia la consola
     */
    private static void limpiarConsola() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            // Si falla, imprimir líneas vacías
            for (int i = 0; i < 3; i++) {
                System.out.println();
            }
        }
    }
    
    /**
     * Muestra el mensaje de despedida del sistema
     */
    private static void mostrarDespedida() {
        limpiarConsola();
        System.out.println("\n\n");
        System.out.println("  ╔═══════════════════════════════════════════════════════╗");
        System.out.println("  ║                                                       ║");
        System.out.println("  ║        ¡Gracias por usar el Sistema Integrado!       ║");
        System.out.println("  ║               BanQuito & ElectroQuito                 ║");
        System.out.println("  ║                                                       ║");
        System.out.println("  ║                  Hasta pronto 👋                      ║");
        System.out.println("  ║                                                       ║");
        System.out.println("  ╚═══════════════════════════════════════════════════════╝");
        System.out.println("\n");
    }
}
