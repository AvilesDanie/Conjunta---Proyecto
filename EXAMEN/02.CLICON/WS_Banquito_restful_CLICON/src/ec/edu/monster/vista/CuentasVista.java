package ec.edu.monster.vista;

import ec.edu.monster.controlador.CuentasControlador;
import ec.edu.monster.modelo.CuentaCrearRequest;
import ec.edu.monster.modelo.CuentaModelo;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class CuentasVista {

    private final Scanner scanner;
    private final CuentasControlador controlador;

    public CuentasVista() {
        this.scanner = new Scanner(System.in);
        this.controlador = new CuentasControlador();
    }

    public void mostrarMenu() {
        while (true) {
            limpiarPantalla();

            System.out.println("\n");
            System.out.println("  ╔═══════════════════════════════════════╗");
            System.out.println("  ║           💳 GESTIÓN DE CUENTAS      ║");
            System.out.println("  ╚═══════════════════════════════════════╝");
            System.out.println("  1. Listar todas las cuentas");
            System.out.println("  2. Buscar cuenta por número");
            System.out.println("  3. Listar cuentas por cédula de cliente");
            System.out.println("  4. Crear nueva cuenta");
            System.out.println("  0. Volver al menú principal");

            System.out.print("\n  ➤ Seleccione una opción [0-4]: ");
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
                        listarTodas();
                        break;
                    case 2:
                        buscarPorNumero();
                        break;
                    case 3:
                        listarPorCedulaCliente();
                        break;
                    case 4:
                        crearCuenta();
                        break;
                    case 0:
                        return; // volver al menú principal (HomeBanquitoVista)
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

    // ========== Opciones ==========

    private void listarTodas() throws IOException {
        limpiarPantalla();
        System.out.println("\n  📋 LISTA DE CUENTAS");
        System.out.println("  --------------------");

        List<CuentaModelo> cuentas = controlador.listarTodas();
        if (cuentas.isEmpty()) {
            System.out.println("\n  (No hay cuentas registradas)");
        } else {
            for (CuentaModelo c : cuentas) {
                imprimirCuenta(c);
                System.out.println("  --------------------------------------------");
            }
        }
        presionarEnter();
    }

    private void buscarPorNumero() throws IOException {
        limpiarPantalla();
        System.out.println("\n  🔍 BUSCAR CUENTA POR NÚMERO");
        System.out.println("  ----------------------------");
        System.out.print("  ➤ Ingrese número de cuenta: ");
        String num = scanner.nextLine().trim();

        if (num.isEmpty()) {
            System.out.println("\n  ❌ El número de cuenta es obligatorio.");
            presionarEnter();
            return;
        }

        try {
            CuentaModelo c = controlador.obtenerPorNumero(num);
            System.out.println();
            imprimirCuenta(c);
        } catch (IOException e) {
            System.out.println("\n  ❌ No se pudo encontrar la cuenta.");
            System.out.println("     Detalle: " + e.getMessage());
        }

        presionarEnter();
    }

    private void listarPorCedulaCliente() throws IOException {
        limpiarPantalla();
        System.out.println("\n  👥 LISTAR CUENTAS POR CLIENTE");
        System.out.println("  ------------------------------");
        System.out.print("  ➤ Ingrese cédula del cliente: ");
        String cedula = scanner.nextLine().trim();

        if (cedula.isEmpty()) {
            System.out.println("\n  ❌ La cédula es obligatoria.");
            presionarEnter();
            return;
        }

        List<CuentaModelo> cuentas = controlador.listarPorCedulaCliente(cedula);
        if (cuentas.isEmpty()) {
            System.out.println("\n  (El cliente no tiene cuentas registradas o no existe)");
        } else {
            System.out.println();
            for (CuentaModelo c : cuentas) {
                imprimirCuenta(c);
                System.out.println("  --------------------------------------------");
            }
        }
        presionarEnter();
    }

    private void crearCuenta() throws IOException {
        limpiarPantalla();
        System.out.println("\n  ➕ CREAR NUEVA CUENTA");
        System.out.println("  ----------------------");

        CuentaCrearRequest req = new CuentaCrearRequest();

        System.out.print("  Cédula del cliente: ");
        req.cedulaCliente = scanner.nextLine().trim();

        System.out.print("  Tipo de cuenta (AHORROS/CORRIENTE/etc.): ");
        req.tipoCuenta = scanner.nextLine().trim();

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
            CuentaModelo creada = controlador.crearCuenta(req);
            System.out.println("\n  ✅ Cuenta creada correctamente:");
            imprimirCuenta(creada);
        } catch (IOException e) {
            System.out.println("\n  ❌ Error al crear la cuenta.");
            System.out.println("     Detalle: " + e.getMessage());
        }

        presionarEnter();
    }

    // ========== Helpers de impresión ==========

    private void imprimirCuenta(CuentaModelo c) {
        System.out.println("  Nº Cuenta     : " + nulo(c.getNumCuenta()));
        System.out.println("  Cédula        : " + nulo(c.getCedulaCliente()));
        System.out.println("  Cliente       : " + nulo(c.getNombreCliente()));
        System.out.println("  Tipo Cuenta   : " + nulo(c.getTipoCuenta()));
        System.out.println("  Saldo         : " + (c.getSaldo() == null ? "-" : c.getSaldo().toPlainString()));
    }

    private String nulo(String s) {
        return (s == null || s.trim().isEmpty()) ? "-" : s;
    }

    // ========== Utilitarios ==========

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
