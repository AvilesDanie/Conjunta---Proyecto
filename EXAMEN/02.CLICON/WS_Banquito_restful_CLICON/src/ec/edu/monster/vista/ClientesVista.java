/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.monster.vista;

/**
 *
 * @author danie
 */
import ec.edu.monster.controlador.ClientesControlador;
import ec.edu.monster.modelo.ClienteModelo;
import ec.edu.monster.modelo.ClienteCrearRequest;
import ec.edu.monster.modelo.ClienteActualizarRequest;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class ClientesVista {

    private final Scanner scanner;
    private final ClientesControlador controlador;

    public ClientesVista() {
        this.scanner = new Scanner(System.in);
        this.controlador = new ClientesControlador();
    }

    public void mostrarMenu() {
        while (true) {
            limpiarPantalla();

            System.out.println("\n");
            System.out.println("  ╔═══════════════════════════════════════╗");
            System.out.println("  ║          👥 GESTIÓN DE CLIENTES      ║");
            System.out.println("  ╚═══════════════════════════════════════╝");
            System.out.println("  1. Listar todos los clientes");
            System.out.println("  2. Buscar cliente por cédula");
            System.out.println("  3. Crear nuevo cliente");
            System.out.println("  4. Actualizar cliente");
            System.out.println("  5. Eliminar cliente");
            System.out.println("  0. Volver al menú principal");

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
                        listarClientes();
                        break;
                    case 2:
                        buscarPorCedula();
                        break;
                    case 3:
                        crearCliente();
                        break;
                    case 4:
                        actualizarCliente();
                        break;
                    case 5:
                        eliminarCliente();
                        break;
                    case 0:
                        // volver al HomeBanquitoVista
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

    // ==================== Opciones ====================
    private void listarClientes() throws IOException {
        limpiarPantalla();
        System.out.println("\n  📋 LISTA DE CLIENTES");
        System.out.println("  ---------------------");

        List<ClienteModelo> clientes = controlador.listarClientes();
        if (clientes.isEmpty()) {
            System.out.println("\n  (No hay clientes registrados)");
        } else {
            for (ClienteModelo c : clientes) {
                imprimirCliente(c);
                System.out.println("  --------------------------------------------");
            }
        }
        presionarEnter();
    }

    private void buscarPorCedula() throws IOException {
        limpiarPantalla();
        System.out.println("\n  🔍 BUSCAR CLIENTE POR CÉDULA");
        System.out.println("  -----------------------------");
        System.out.print("  ➤ Ingrese cédula (10 dígitos): ");
        String cedula = scanner.nextLine().trim();

        if (cedula.isEmpty()) {
            System.out.println("\n  ❌ La cédula es obligatoria.");
            presionarEnter();
            return;
        }

        try {
            ClienteModelo c = controlador.obtenerCliente(cedula);
            System.out.println();
            imprimirCliente(c);
        } catch (IOException e) {
            System.out.println("\n  ❌ No se pudo encontrar el cliente.");
            System.out.println("     Detalle: " + e.getMessage());
        }

        presionarEnter();
    }

    private void crearCliente() throws IOException {
        limpiarPantalla();
        System.out.println("\n  ➕ CREAR NUEVO CLIENTE");
        System.out.println("  ----------------------");

        ClienteCrearRequest req = new ClienteCrearRequest();

        System.out.print("  Cédula (10 dígitos): ");
        req.cedula = scanner.nextLine().trim();

        System.out.print("  Nombre completo: ");
        req.nombre = scanner.nextLine().trim();

        System.out.print("  Fecha de nacimiento (yyyy-MM-dd) [opcional]: ");
        String fecha = scanner.nextLine().trim();
        req.fechaNacimiento = fecha.isEmpty() ? null : fecha;

        System.out.print("  Estado civil (SOLTERO, CASADO, etc.) [opcional]: ");
        String estadoCivil = scanner.nextLine().trim();
        req.estadoCivil = estadoCivil.isEmpty() ? null : estadoCivil;

        System.out.print("  Tipo de cuenta inicial (AHORROS/CORRIENTE/etc.): ");
        req.tipoCuentaInicial = scanner.nextLine().trim();

        System.out.print("  Saldo inicial [opcional, por defecto 0]: ");
        String saldoTxt = scanner.nextLine().trim();
        if (!saldoTxt.isEmpty()) {
            try {
                req.saldoInicial = new BigDecimal(saldoTxt);
            } catch (NumberFormatException e) {
                System.out.println("\n  ⚠ Saldo inválido, se usará 0.");
                req.saldoInicial = null;
            }
        }

        try {
            ClienteModelo creado = controlador.crearCliente(req);
            System.out.println("\n  ✅ Cliente creado correctamente:");
            imprimirCliente(creado);
        } catch (IOException e) {
            System.out.println("\n  ❌ Error al crear el cliente.");
            System.out.println("     Detalle: " + e.getMessage());
        }

        presionarEnter();
    }

    private void actualizarCliente() throws IOException {
        limpiarPantalla();
        System.out.println("\n  ✏ ACTUALIZAR CLIENTE");
        System.out.println("  ---------------------");

        System.out.print("  Cédula del cliente a actualizar: ");
        String cedula = scanner.nextLine().trim();

        ClienteActualizarRequest req = new ClienteActualizarRequest();

        System.out.print("  Nuevo nombre [ENTER para dejar igual]: ");
        String nombre = scanner.nextLine().trim();
        req.nombre = nombre.isEmpty() ? null : nombre;

        System.out.print("  Nuevo estado civil [ENTER para dejar igual]: ");
        String estadoCivil = scanner.nextLine().trim();
        req.estadoCivil = estadoCivil.isEmpty() ? null : estadoCivil;

        try {
            ClienteModelo actualizado = controlador.actualizarCliente(cedula, req);
            System.out.println("\n  ✅ Cliente actualizado:");
            imprimirCliente(actualizado);
        } catch (IOException e) {
            System.out.println("\n  ❌ Error al actualizar el cliente.");
            System.out.println("     Detalle: " + e.getMessage());
        }

        presionarEnter();
    }

    private void eliminarCliente() throws IOException {
        limpiarPantalla();
        System.out.println("\n  🗑 ELIMINAR CLIENTE");
        System.out.println("  -------------------");

        System.out.print("  Cédula del cliente a eliminar: ");
        String cedula = scanner.nextLine().trim();

        System.out.print("\n  ⚠ ¿Está seguro que desea eliminarlo? (s/N): ");
        String conf = scanner.nextLine().trim().toLowerCase();

        if (!conf.equals("s")) {
            System.out.println("\n  Operación cancelada.");
            presionarEnter();
            return;
        }

        try {
            controlador.eliminarCliente(cedula);
            System.out.println("\n  ✅ Cliente eliminado correctamente.");
        } catch (IOException e) {
            System.out.println("\n  ❌ Error al eliminar el cliente.");
            System.out.println("     Detalle: " + e.getMessage());
        }

        presionarEnter();
    }

    // ==================== Helpers de impresión ====================
    private void imprimirCliente(ClienteModelo c) {
        System.out.println("  Cédula        : " + nulo(c.getCedula()));
        System.out.println("  Nombre        : " + nulo(c.getNombre()));
        System.out.println("  Fecha Nac.    : " + nulo(c.getFechaNacimiento()));
        System.out.println("  Estado Civil  : " + nulo(c.getEstadoCivil()));
        System.out.println("  Nº Cuenta Ini.: " + nulo(c.getNumCuentaInicial()));
        System.out.println("  Tipo Cuenta   : " + nulo(c.getTipoCuentaInicial()));
    }

    private String nulo(String s) {
        return (s == null || s.isBlank()) ? "-" : s;
    }

    // ==================== Utilitarios ====================
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
