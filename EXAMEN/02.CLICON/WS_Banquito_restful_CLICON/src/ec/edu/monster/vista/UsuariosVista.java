package ec.edu.monster.vista;

import ec.edu.monster.controlador.UsuariosControlador;
import ec.edu.monster.modelo.ActualizarUsuarioModelo;
import ec.edu.monster.modelo.CrearUsuarioModelo;
import ec.edu.monster.modelo.UsuarioModelo;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class UsuariosVista {

    private final Scanner scanner;
    private final UsuariosControlador controlador;
    private final String sistema;

    public UsuariosVista(String sistema) {
        this.scanner = new Scanner(System.in);
        this.sistema = sistema;
        this.controlador = new UsuariosControlador(sistema);
    }

    public void mostrarMenu() {
        while (true) {
            limpiarPantalla();

            System.out.println("\n");
            System.out.println("  ╔═══════════════════════════════════════╗");
            System.out.println("  ║          👤 GESTIÓN DE USUARIOS      ║");
            System.out.println("  ╚═══════════════════════════════════════╝");
            System.out.println("  Backend: " + sistema.toUpperCase());
            System.out.println("  ----------------------------------------");
            System.out.println("  1. Listar usuarios");
            System.out.println("  2. Buscar usuario por ID");
            System.out.println("  3. Crear nuevo usuario");
            System.out.println("  4. Actualizar usuario");
            System.out.println("  5. Eliminar usuario");
            System.out.println("  0. Volver al menú anterior");

            System.out.print("\n  ➤ Seleccione una opción [0-5]: ");
            String linea = scanner.nextLine();
            int opcion;
            try {
                opcion = Integer.parseInt(linea);
            } catch (NumberFormatException e) {
                System.out.println("\n  ❌ Debe ingresar un número.");
                presionarEnter();
                continue;
            }

            try {
                switch (opcion) {
                    case 1:
                        listarUsuarios();
                        break;
                    case 2:
                        buscarPorId();
                        break;
                    case 3:
                        crearUsuario();
                        break;
                    case 4:
                        actualizarUsuario();
                        break;
                    case 5:
                        eliminarUsuario();
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("\n  ❌ Opción inválida.");
                        presionarEnter();
                        break;
                }
            } catch (IOException ex) {
                System.out.println("\n  ❌ Error llamando al servicio: " + ex.getMessage());
                presionarEnter();
            }
        }
    }

    // ========= Opciones =========

    private void listarUsuarios() throws IOException {
        limpiarPantalla();
        System.out.println("\n  📋 LISTA DE USUARIOS");
        System.out.println("  ---------------------");

        List<UsuarioModelo> usuarios = controlador.listar();
        if (usuarios.isEmpty()) {
            System.out.println("\n  (No hay usuarios registrados)");
        } else {
            for (UsuarioModelo u : usuarios) {
                imprimirUsuarioLinea(u);
            }
        }
        presionarEnter();
    }

    private void buscarPorId() throws IOException {
        limpiarPantalla();
        System.out.println("\n  🔍 BUSCAR USUARIO POR ID");
        System.out.println("  -------------------------");

        Long id = leerLong("  ID de usuario: ");
        if (id == null) {
            return;
        }

        try {
            UsuarioModelo u = controlador.obtenerPorId(id);
            System.out.println();
            imprimirUsuarioDetalle(u);
        } catch (IOException e) {
            System.out.println("\n  ❌ No se pudo obtener el usuario.");
            System.out.println("     Detalle: " + e.getMessage());
        }

        presionarEnter();
    }

    private void crearUsuario() throws IOException {
        limpiarPantalla();
        System.out.println("\n  ➕ CREAR NUEVO USUARIO");
        System.out.println("  -----------------------");

        CrearUsuarioModelo req = new CrearUsuarioModelo();

        System.out.print("  Username: ");
        req.username = scanner.nextLine().trim();
        if (req.username.isEmpty()) {
            System.out.println("\n  ❌ El username es obligatorio.");
            presionarEnter();
            return;
        }

        System.out.print("  Password (mínimo 6 caracteres): ");
        req.password = scanner.nextLine().trim();
        if (req.password.length() < 6) {
            System.out.println("\n  ❌ La contraseña debe tener al menos 6 caracteres.");
            presionarEnter();
            return;
        }

        System.out.print("  Rol (SUPERADMIN / ADMIN / USER): ");
        req.rol = scanner.nextLine().trim();
        if (req.rol.isEmpty()) {
            System.out.println("\n  ❌ El rol es obligatorio.");
            presionarEnter();
            return;
        }

        try {
            UsuarioModelo creado = controlador.crear(req);
            System.out.println("\n  ✅ Usuario creado correctamente:");
            imprimirUsuarioDetalle(creado);
        } catch (IOException e) {
            System.out.println("\n  ❌ Error al crear el usuario.");
            System.out.println("     Detalle: " + e.getMessage());
        }

        presionarEnter();
    }

    private void actualizarUsuario() throws IOException {
        limpiarPantalla();
        System.out.println("\n  ✏ ACTUALIZAR USUARIO");
        System.out.println("  ---------------------");

        Long id = leerLong("  ID de usuario a actualizar: ");
        if (id == null) {
            return;
        }

        ActualizarUsuarioModelo req = new ActualizarUsuarioModelo();

        System.out.print("  Nuevo password [ENTER para no cambiar]: ");
        String pass = scanner.nextLine().trim();
        if (!pass.isEmpty()) {
            if (pass.length() < 6) {
                System.out.println("\n  ❌ La contraseña debe tener al menos 6 caracteres.");
                presionarEnter();
                return;
            }
            req.password = pass;
        }

        System.out.print("  Nuevo rol [ENTER para no cambiar]: ");
        String rol = scanner.nextLine().trim();
        if (!rol.isEmpty()) {
            req.rol = rol;
        }

        System.out.print("  Cambiar estado activo? (S=activo, N=inactivo, ENTER=sin cambio): ");
        String act = scanner.nextLine().trim().toUpperCase();
        if ("S".equals(act)) {
            req.activo = Boolean.TRUE;
        } else if ("N".equals(act)) {
            req.activo = Boolean.FALSE;
        } // si ENTER, queda null y no se cambia

        try {
            UsuarioModelo actualizado = controlador.actualizar(id, req);
            System.out.println("\n  ✅ Usuario actualizado:");
            imprimirUsuarioDetalle(actualizado);
        } catch (IOException e) {
            System.out.println("\n  ❌ Error al actualizar el usuario.");
            System.out.println("     Detalle: " + e.getMessage());
        }

        presionarEnter();
    }

    private void eliminarUsuario() throws IOException {
        limpiarPantalla();
        System.out.println("\n  🗑 ELIMINAR USUARIO");
        System.out.println("  --------------------");

        Long id = leerLong("  ID de usuario a eliminar: ");
        if (id == null) {
            return;
        }

        System.out.print("\n  ⚠ ¿Seguro que desea eliminar este usuario? (s/N): ");
        String conf = scanner.nextLine().trim().toLowerCase();
        if (!"s".equals(conf)) {
            System.out.println("\n  Operación cancelada.");
            presionarEnter();
            return;
        }

        try {
            controlador.eliminar(id);
            System.out.println("\n  ✅ Usuario eliminado correctamente.");
        } catch (IOException e) {
            System.out.println("\n  ❌ Error al eliminar el usuario.");
            System.out.println("     Detalle: " + e.getMessage());
        }

        presionarEnter();
    }

    // ========= Helpers de impresión =========

    private void imprimirUsuarioLinea(UsuarioModelo u) {
        System.out.printf("  ID:%-4d | User:%-15s | Rol:%-10s | Activo:%s%n",
                u.getId(),
                u.getUsername(),
                u.getRol(),
                u.isActivo() ? "Sí" : "No");
    }

    private void imprimirUsuarioDetalle(UsuarioModelo u) {
        System.out.println("  ID       : " + u.getId());
        System.out.println("  Username : " + u.getUsername());
        System.out.println("  Rol      : " + u.getRol());
        System.out.println("  Activo   : " + (u.isActivo() ? "Sí" : "No"));
    }

    private Long leerLong(String msg) {
        System.out.print(msg);
        String txt = scanner.nextLine().trim();
        try {
            return Long.parseLong(txt);
        } catch (NumberFormatException e) {
            System.out.println("\n  ❌ Valor numérico inválido.");
            presionarEnter();
            return null;
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
