package ec.edu.monster.vista;

import ec.edu.monster.controlador.UsuarioController;
import java.util.List;
import java.util.Scanner;

/**
 * Vista de consola para gestión de usuarios
 * @author CLICON
 */
public class UsuariosVista {
    private final Scanner scanner = new Scanner(System.in);
    private final UsuarioController controller = new UsuarioController();
    private final String servidor;

    public UsuariosVista(String servidor) {
        this.servidor = servidor;
    }

    public void mostrarMenu() {
        while (true) {
            ConsolaUtil.limpiarPantalla();
            String titulo = servidor.equals("banquito") ? "👥 USUARIOS BANQUITO" : "👥 USUARIOS COMERCIALIZADORA";
            System.out.println("\n╔═══════════════════════════════════════╗");
            System.out.printf("║  %-35s  ║%n", titulo);
            System.out.println("╠═══════════════════════════════════════╣");
            System.out.println("║  1. Listar usuarios                  ║");
            System.out.println("║  2. Consultar usuario                ║");
            System.out.println("║  3. Crear usuario                    ║");
            System.out.println("║  4. Eliminar usuario                 ║");
            System.out.println("║  5. Volver al menú principal         ║");
            System.out.println("╚═══════════════════════════════════════╝");
            System.out.print("➤ Seleccione una opción: ");

            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    listarUsuarios();
                    break;
                case 2:
                    consultarUsuario();
                    break;
                case 3:
                    crearUsuario();
                    break;
                case 4:
                    eliminarUsuario();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("❌ Opción inválida");
            }
        }
    }

    private void listarUsuarios() {
        try {
            List<String> usuarios = controller.listarUsuarios(servidor);
            System.out.println("\n┌─────────────────────────────────────────────────────────────────────┐");
            System.out.println("│                    👥 LISTADO DE USUARIOS                           │");
            System.out.println("├─────────────────────────────────────────────────────────────────────┤");
            
            if (usuarios.isEmpty()) {
                System.out.println("│  ⚠️  No hay usuarios registrados                                    │");
            } else {
                for (String u : usuarios) {
                    System.out.println("├─────────────────────────────────────────────────────────────────────┤");
                    System.out.printf("│  %s%n", u);
                }
            }
            System.out.println("└─────────────────────────────────────────────────────────────────────┘");
        } catch (Exception e) {
            System.out.println("❌ Error al listar usuarios: " + e.getMessage());
        }
        ConsolaUtil.presionarEnter();
    }

    private void consultarUsuario() {
        System.out.print("➤ Ingrese ID del usuario: ");
        Long id = scanner.nextLong();
        scanner.nextLine();

        try {
            String usuario = controller.obtenerUsuario(servidor, id);
            if (usuario != null) {
                System.out.println("\n┌─────────────────────────────────────────────────────┐");
                System.out.println("│           📄 INFORMACIÓN DEL USUARIO                │");
                System.out.println("├─────────────────────────────────────────────────────┤");
                System.out.println("│  " + usuario);
                System.out.println("└─────────────────────────────────────────────────────┘");
            } else {
                System.out.println("❌ Usuario no encontrado");
            }
        } catch (Exception e) {
            System.out.println("❌ Error al consultar usuario: " + e.getMessage());
        }
    }

    private void crearUsuario() {
        System.out.println("\n┌──────────────────────────────────────┐");
        System.out.println("│      ➕ CREAR NUEVO USUARIO          │");
        System.out.println("└──────────────────────────────────────┘");

        System.out.print("➤ Nombre de usuario: ");
        String username = scanner.nextLine();

        System.out.print("➤ Contraseña: ");
        String password = scanner.nextLine();

        System.out.print("➤ Rol (ADMIN/USER): ");
        String rol = scanner.nextLine();

        try {
            if (controller.crearUsuario(servidor, username, password, rol)) {
                System.out.println("✅ Usuario creado exitosamente");
            } else {
                System.out.println("❌ No se pudo crear el usuario");
            }
        } catch (Exception e) {
            System.out.println("❌ Error al crear usuario: " + e.getMessage());
        }
    }

    private void eliminarUsuario() {
        System.out.print("➤ Ingrese ID del usuario a eliminar: ");
        Long id = scanner.nextLong();
        scanner.nextLine();

        System.out.print("⚠️  ¿Está seguro de eliminar este usuario? (S/N): ");
        String confirmacion = scanner.nextLine();

        if (confirmacion.equalsIgnoreCase("S")) {
            try {
                if (controller.eliminarUsuario(servidor, id)) {
                    System.out.println("✅ Usuario eliminado exitosamente");
                } else {
                    System.out.println("❌ No se pudo eliminar el usuario");
                }
            } catch (Exception e) {
                System.out.println("❌ Error al eliminar usuario: " + e.getMessage());
            }
        }
    }
}
