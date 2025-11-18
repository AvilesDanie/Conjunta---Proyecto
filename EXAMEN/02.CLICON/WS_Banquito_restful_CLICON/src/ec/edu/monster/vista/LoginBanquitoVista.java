package ec.edu.monster.vista;

import ec.edu.monster.controlador.AuthController;
import java.util.Scanner;

/**
 * Vista de login para BanQuito
 * @author CLICON
 */
public class LoginBanquitoVista {
    private final Scanner scanner;
    private final AuthController authController;
    
    public LoginBanquitoVista() {
        this.scanner = new Scanner(System.in);
        this.authController = new AuthController();
    }
    
    /**
     * Muestra la pantalla de login y valida credenciales
     * @return true si login exitoso, false si el usuario quiere volver
     */
    public boolean mostrarLogin() {
        while (true) {
            limpiarPantalla();
            
            System.out.println("\n");
            System.out.println("  ╔═══════════════════════════════════════════════════════╗");
            System.out.println("  ║                                                       ║");
            System.out.println("  ║                  🏦 LOGIN BANQUITO 🏦                 ║");
            System.out.println("  ║                   Sistema Bancario                    ║");
            System.out.println("  ║                                                       ║");
            System.out.println("  ╚═══════════════════════════════════════════════════════╝");
            System.out.println();
            
            System.out.print("  👤 Usuario: ");
            String username = scanner.nextLine().trim();
            
            if (username.isEmpty()) {
                System.out.println("\n  ❌ El usuario no puede estar vacío.");
                presionarEnter();
                continue;
            }
            
            // Opción para volver
            if (username.equalsIgnoreCase("volver") || username.equalsIgnoreCase("back")) {
                return false;
            }
            
            System.out.print("  🔒 Contraseña: ");
            String password = scanner.nextLine().trim();
            
            if (password.isEmpty()) {
                System.out.println("\n  ❌ La contraseña no puede estar vacía.");
                presionarEnter();
                continue;
            }
            
            System.out.println("\n  ⏳ Validando credenciales...");
            
            try {
                boolean loginExitoso = authController.loginBanquito(username, password);
                
                if (loginExitoso) {
                    System.out.println("  ✅ ¡Login exitoso! Bienvenido " + username);
                    presionarEnter();
                    return true;
                } else {
                    System.out.println("\n  ❌ Credenciales inválidas o usuario inactivo.");
                    System.out.println("     Por favor, intente nuevamente.");
                    System.out.println("     (Escriba 'volver' para regresar)");
                    presionarEnter();
                }
                
            } catch (Exception e) {
                System.out.println("\n  ❌ Error de conexión: " + e.getMessage());
                System.out.println("     Verifique que el servidor esté activo.");
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
