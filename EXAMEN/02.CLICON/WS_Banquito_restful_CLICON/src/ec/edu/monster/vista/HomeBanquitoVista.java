package ec.edu.monster.vista;

import java.util.Scanner;

/**
 * Menú principal de BanQuito después del login
 * @author CLICON
 */
public class HomeBanquitoVista {
    private final Scanner scanner;
    private final AmortizacionVistaConsola amortizacionVista;
    private final ClientesVista clientesVista;
    private final CuentasVista cuentasVista;
    private final CreditosVista creditosVista;
    private final UsuariosVista usuariosVista;
    private final MovimientosVista movimientosVista;
    private final CuotasVista cuotasVista;
    
    public HomeBanquitoVista() {
        this.scanner = new Scanner(System.in);
        this.amortizacionVista = new AmortizacionVistaConsola();
        this.clientesVista = new ClientesVista();
        this.cuentasVista = new CuentasVista();
        this.creditosVista = new CreditosVista();
        this.usuariosVista = new UsuariosVista("banquito");
        this.movimientosVista = new MovimientosVista();
        this.cuotasVista = new CuotasVista();
    }
    
    /**
     * Muestra el menú principal de BanQuito
     * @return false cuando el usuario desea cerrar sesión
     */
    public boolean mostrarMenu() {
        while (true) {
            limpiarPantalla();
            
            System.out.println("\n");
            System.out.println("  ╔═══════════════════════════════════════════════════════╗");
            System.out.println("  ║                                                       ║");
            System.out.println("  ║              🏦 MENÚ PRINCIPAL BANQUITO 🏦            ║");
            System.out.println("  ║                   Sistema Bancario                    ║");
            System.out.println("  ║                                                       ║");
            System.out.println("  ╚═══════════════════════════════════════════════════════╝");
            System.out.println();
            System.out.println("  ┌───────────────────────────────────────────────────────┐");
            System.out.println("  │  1. 👥 Gestionar Clientes                              │");
            System.out.println("  │  2. 💳 Gestionar Cuentas                               │");
            System.out.println("  │  3. 💰 Gestionar Créditos                              │");
            System.out.println("  │  4. 📊 Consultar Tabla de Amortización                 │");
            System.out.println("  │  5. 👤 Gestionar Usuarios                              │");
            System.out.println("  │  6. 📈 Ver Movimientos                                 │");
            System.out.println("  │  7. 📅 Gestionar Cuotas                                │");
            System.out.println("  ├───────────────────────────────────────────────────────┤");
            System.out.println("  │  8. 🔙 Cerrar Sesión                                   │");
            System.out.println("  │  9. ❌ Salir del Sistema                               │");
            System.out.println("  └───────────────────────────────────────────────────────┘");
            
            try {
                System.out.print("\n  ➤ Seleccione una opción [1-9]: ");
                int opcion = Integer.parseInt(scanner.nextLine());
                
                switch (opcion) {
                    case 1:
                        clientesVista.mostrarMenu();
                        break;
                    case 2:
                        cuentasVista.mostrarMenu();
                        break;
                    case 3:
                        creditosVista.mostrarMenu();
                        break;
                    case 4:
                        amortizacionVista.mostrarMenu();
                        break;
                    case 5:
                        usuariosVista.mostrarMenu();
                        break;
                    case 6:
                        movimientosVista.mostrarMenu();
                        break;
                    case 7:
                        cuotasVista.mostrarMenu();
                        break;
                    case 8:
                        System.out.println("\n  🔓 Cerrando sesión...");
                        presionarEnter();
                        return false; // Volver a selección
                    case 9:
                        return true; // Salir del sistema
                    default:
                        System.out.println("\n  ❌ Opción inválida.");
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
