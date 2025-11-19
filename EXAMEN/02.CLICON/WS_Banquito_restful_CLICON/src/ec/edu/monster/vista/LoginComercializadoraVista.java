package ec.edu.monster.vista;

import ec.edu.monster.controlador.AuthControlador;
import ec.edu.monster.modelo.UsuarioModelo;

import java.io.IOException;
import java.util.Scanner;

public class LoginComercializadoraVista {

    private final Scanner scanner;
    private final AuthControlador authControlador;

    public LoginComercializadoraVista() {
        this.scanner = new Scanner(System.in);
        // 🔴 AJUSTA ESTA URL AL ENDPOINT REAL DE LA COMERCIALIZADORA
        String loginUrlComercializadora = "http://localhost:8080/WS_JAVA_REST_Comercializadora/api/usuarios/login";
        this.authControlador = new AuthControlador(loginUrlComercializadora);
    }

    /**
     * Muestra el login de la Comercializadora
     * @return true si el login es exitoso, false si el usuario vuelve/cancela
     */
    public boolean mostrarLogin() {
        while (true) {
            limpiarPantalla();

            System.out.println("\n");
            System.out.println("  ╔═══════════════════════════════════════════════════════╗");
            System.out.println("  ║            🔐 LOGIN COMERCIALIZADORA                 ║");
            System.out.println("  ╚═══════════════════════════════════════════════════════╝");
            System.out.println("  1. Iniciar sesión");
            System.out.println("  0. Volver a selección de aplicación");
            System.out.print("\n  ➤ Seleccione una opción [0-1]: ");

            String linea = scanner.nextLine();
            int opcion;
            try {
                opcion = Integer.parseInt(linea);
            } catch (NumberFormatException e) {
                System.out.println("\n  ❌ Ingrese un número válido.");
                presionarEnter();
                continue;
            }

            if (opcion == 0) {
                return false; // vuelve a selección
            } else if (opcion == 1) {
                System.out.print("\n  Usuario: ");
                String username = scanner.nextLine().trim();

                System.out.print("  Contraseña: ");
                String password = scanner.nextLine().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    System.out.println("\n  ❌ Usuario y contraseña son obligatorios.");
                    presionarEnter();
                    continue;
                }

                try {
                    UsuarioModelo usuario = authControlador.login(username, password);
                    if (usuario == null) {
                        System.out.println("\n  ❌ Credenciales incorrectas o usuario inactivo.");
                        presionarEnter();
                    } else {
                        System.out.println("\n  ✅ Login exitoso.");
                        System.out.println("  Usuario: " + usuario.getUsername());
                        System.out.println("  Rol    : " + usuario.getRol());
                        presionarEnter();
                        return true; // el main mostrará HomeComercializadoraVista
                    }
                } catch (IOException e) {
                    System.out.println("\n  ❌ Error al conectar con el servidor.");
                    System.out.println("     Detalle: " + e.getMessage());
                    presionarEnter();
                }
            } else {
                System.out.println("\n  ❌ Opción inválida.");
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
