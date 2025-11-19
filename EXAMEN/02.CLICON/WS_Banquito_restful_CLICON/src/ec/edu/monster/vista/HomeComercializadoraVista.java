package ec.edu.monster.vista;

import java.util.Scanner;

/**
 * Menú principal de Comercializadora después del login
 * @author CLICON
 */
public class HomeComercializadoraVista {
    private final Scanner scanner;
    private final ElectrodomesticoVistaConsola electrodomesticosVista;
    private final ComprasVistaConsola comprasVista;
    private final FacturasVista facturasVista;
    private final UsuariosVista usuariosVista;
    
    public HomeComercializadoraVista() {
        this.scanner = new Scanner(System.in);
        this.electrodomesticosVista = new ElectrodomesticoVistaConsola();
        this.comprasVista = new ComprasVistaConsola();
        this.facturasVista = new FacturasVista();
        this.usuariosVista = new UsuariosVista("comercializadora");
    }
    
    /**
     * Muestra el menú principal de Comercializadora
     * @return false cuando el usuario desea cerrar sesión
     */
    public boolean mostrarMenu() {
        while (true) {
            limpiarPantalla();
            mostrarCabecera();
            
            System.out.println("\n╔═══════════════════════════════════════════════════════╗");
            System.out.println("║           MENÚ PRINCIPAL COMERCIALIZADORA             ║");
            System.out.println("╠═══════════════════════════════════════════════════════╣");
            System.out.println("║                                                       ║");
            System.out.println("║  1. 📦 Gestionar Electrodomésticos                    ║");
            System.out.println("║  2. 🛒 Realizar Compras                               ║");
            System.out.println("║  3. 🧾 Ver Facturas                                   ║");
            System.out.println("║  4. 👤 Gestionar Usuarios                            ║");
            System.out.println("║                                                       ║");
            System.out.println("║  5. 🔙 Cerrar Sesión                                 ║");
            System.out.println("║  6. ❌ Salir del Sistema                             ║");
            System.out.println("║                                                       ║");
            System.out.println("╚═══════════════════════════════════════════════════════╝");
            
            try {
                System.out.print("\n➤ Seleccione una opción: ");
                int opcion = Integer.parseInt(scanner.nextLine());
                
                switch (opcion) {
                    case 1:
                        electrodomesticosVista.mostrarMenu();
                        break;
                    case 2:
                        comprasVista.mostrarMenu();
                        break;
                    case 3:
                        facturasVista.mostrarMenu();
                        break;
                    case 4:
                        usuariosVista.mostrarMenu();
                        break;
                    case 5:
                        System.out.println("\n🔓 Cerrando sesión...");
                        return false; // Volver a selección
                    case 6:
                        return true; // Salir del sistema
                    default:
                        System.out.println("❌ Opción inválida.");
                        presionarEnter();
                }
                
            } catch (NumberFormatException e) {
                System.out.println("❌ Por favor ingrese un número válido.");
                presionarEnter();
            }
        }
    }
    
    private void mostrarCabecera() {
        System.out.println("\n╔═══════════════════════════════════════════════════════╗");
        System.out.println("║                🏪 ELECTROQUITO 🏪                     ║");
        System.out.println("║                  Comercializadora                     ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝");
    }
    
    private void limpiarPantalla() {
        for (int i = 0; i < 2; i++) {
            System.out.println();
        }
    }
    
    private void presionarEnter() {
        System.out.print("\n  Presione ENTER para continuar...");
        scanner.nextLine();
    }


}
