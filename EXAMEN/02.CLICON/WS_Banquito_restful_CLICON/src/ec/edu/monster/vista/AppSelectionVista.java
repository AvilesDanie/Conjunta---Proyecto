package ec.edu.monster.vista;

import java.util.Scanner;

/**
 * Vista de selección de aplicación
 * @author CLICON
 */
public class AppSelectionVista {
    private final Scanner scanner;
    
    public AppSelectionVista() {
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * Muestra el menú de selección
     * @return 1 para BanQuito, 2 para Comercializadora, 0 para salir
     */
    public int mostrarSeleccion() {
        while (true) {
            limpiarPantalla();
            
            System.out.println("\n");
            System.out.println("  ╔═══════════════════════════════════════════════════════╗");
            System.out.println("  ║                                                       ║");
            System.out.println("  ║            🏢 SELECCIONE UNA APLICACIÓN 🏢            ║");
            System.out.println("  ║                                                       ║");
            System.out.println("  ╚═══════════════════════════════════════════════════════╝");
            System.out.println();
            System.out.println("  ┌───────────────────────────────────────────────────────┐");
            System.out.println("  │  1. 🏦 BanQuito - Sistema Bancario                   │");
            System.out.println("  │     • Gestión de Clientes y Cuentas                   │");
            System.out.println("  │     • Créditos y Tabla de Amortización                │");
            System.out.println("  └───────────────────────────────────────────────────────┘");
            System.out.println();
            System.out.println("  ┌───────────────────────────────────────────────────────┐");
            System.out.println("  │  2. 🏪 ElectroQuito - Comercializadora               │");
            System.out.println("  │     • Catálogo de Electrodomésticos                   │");
            System.out.println("  │     • Sistema de Ventas y Facturación                 │");
            System.out.println("  └───────────────────────────────────────────────────────┘");
            System.out.println();
            System.out.println("  ┌───────────────────────────────────────────────────────┐");
            System.out.println("  │  3. ❌ Salir del Sistema                             │");
            System.out.println("  └───────────────────────────────────────────────────────┘");
            
            try {
                System.out.print("\n  ➤ Seleccione una opción [1-3]: ");
                int opcion = Integer.parseInt(scanner.nextLine());
                
                if (opcion >= 1 && opcion <= 3) {
                    if (opcion == 3) return 0; // Salir
                    return opcion;
                } else {
                    System.out.println("\n  ❌ Opción inválida. Debe seleccionar 1, 2 o 3.");
                    presionarEnter();
                }
                
            } catch (NumberFormatException e) {
                System.out.println("\n  ❌ Por favor ingrese un número válido.");
                presionarEnter();
            }
        }
    }
    
    private void limpiarPantalla() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            for (int i = 0; i < 3; i++) {
                System.out.println();
            }
        }
    }
    
    private void presionarEnter() {
        System.out.print("\n  Presione ENTER para continuar...");
        scanner.nextLine();
    }
}
