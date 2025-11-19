package ec.edu.monster.vista;

import ec.edu.monster.controlador.MovimientosControlador;
import ec.edu.monster.modelo.MovimientoModelo;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class MovimientosVista {

    private final Scanner scanner;
    private final MovimientosControlador controlador;

    public MovimientosVista() {
        this.scanner = new Scanner(System.in);
        this.controlador = new MovimientosControlador();
    }

    public void mostrarMenu() {
        while (true) {
            limpiarPantalla();

            System.out.println("\n");
            System.out.println("  ╔═══════════════════════════════════════╗");
            System.out.println("  ║         📈 MOVIMIENTOS BANCARIOS     ║");
            System.out.println("  ╚═══════════════════════════════════════╝");
            System.out.println("  1. Listar movimientos por cuenta");
            System.out.println("  2. Registrar DEPÓSITO");
            System.out.println("  3. Registrar RETIRO");
            System.out.println("  4. Registrar TRANSFERENCIA");
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
                        listarPorCuenta();
                        break;
                    case 2:
                        registrarDeposito();
                        break;
                    case 3:
                        registrarRetiro();
                        break;
                    case 4:
                        registrarTransferencia();
                        break;
                    case 0:
                        return; // Volver al HomeBanquitoVista
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

    private void listarPorCuenta() throws IOException {
        limpiarPantalla();
        System.out.println("\n  📋 MOVIMIENTOS POR CUENTA");
        System.out.println("  --------------------------");
        System.out.print("  ➤ Ingrese número de cuenta: ");
        String numCuenta = scanner.nextLine().trim();

        if (numCuenta.isEmpty()) {
            System.out.println("\n  ❌ El número de cuenta es obligatorio.");
            presionarEnter();
            return;
        }

        List<MovimientoModelo> lista = controlador.listarPorCuenta(numCuenta);
        if (lista.isEmpty()) {
            System.out.println("\n  (No hay movimientos registrados para esta cuenta)");
        } else {
            System.out.println();
            for (MovimientoModelo m : lista) {
                imprimirMovimiento(m);
                System.out.println("  --------------------------------------------");
            }
        }

        presionarEnter();
    }

    private void registrarDeposito() throws IOException {
        limpiarPantalla();
        System.out.println("\n  ➕ REGISTRAR DEPÓSITO");
        System.out.println("  ----------------------");

        System.out.print("  Nº de cuenta: ");
        String numCuenta = scanner.nextLine().trim();

        BigDecimal valor = leerValor();
        if (valor == null) {
            return;
        }

        String fecha = leerFechaOpcional();

        try {
            MovimientoModelo m = controlador.registrarDeposito(numCuenta, valor, fecha);
            System.out.println("\n  ✅ Depósito registrado:");
            imprimirMovimiento(m);
        } catch (IOException e) {
            System.out.println("\n  ❌ Error al registrar depósito.");
            System.out.println("     Detalle: " + e.getMessage());
        }

        presionarEnter();
    }

    private void registrarRetiro() throws IOException {
        limpiarPantalla();
        System.out.println("\n  ➖ REGISTRAR RETIRO");
        System.out.println("  --------------------");

        System.out.print("  Nº de cuenta: ");
        String numCuenta = scanner.nextLine().trim();

        BigDecimal valor = leerValor();
        if (valor == null) {
            return;
        }

        String fecha = leerFechaOpcional();

        try {
            MovimientoModelo m = controlador.registrarRetiro(numCuenta, valor, fecha);
            System.out.println("\n  ✅ Retiro registrado:");
            imprimirMovimiento(m);
        } catch (IOException e) {
            System.out.println("\n  ❌ Error al registrar retiro.");
            System.out.println("     Detalle: " + e.getMessage());
        }

        presionarEnter();
    }

    private void registrarTransferencia() throws IOException {
        limpiarPantalla();
        System.out.println("\n  🔁 REGISTRAR TRANSFERENCIA");
        System.out.println("  ---------------------------");

        System.out.print("  Nº cuenta ORIGEN: ");
        String origen = scanner.nextLine().trim();

        System.out.print("  Nº cuenta DESTINO: ");
        String destino = scanner.nextLine().trim();

        BigDecimal valor = leerValor();
        if (valor == null) {
            return;
        }

        String fecha = leerFechaOpcional();

        try {
            MovimientoModelo m = controlador.registrarTransferencia(origen, destino, valor, fecha);
            System.out.println("\n  ✅ Transferencia registrada (movimiento origen):");
            imprimirMovimiento(m);
        } catch (IOException e) {
            System.out.println("\n  ❌ Error al registrar transferencia.");
            System.out.println("     Detalle: " + e.getMessage());
        }

        presionarEnter();
    }

    // ========= Helpers =========

    private BigDecimal leerValor() {
        System.out.print("  Valor (ej. 100.50): ");
        String txt = scanner.nextLine().trim();
        if (txt.isEmpty()) {
            System.out.println("\n  ❌ El valor es obligatorio.");
            presionarEnter();
            return null;
        }
        try {
            BigDecimal valor = new BigDecimal(txt);
            if (valor.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("\n  ❌ El valor debe ser mayor a cero.");
                presionarEnter();
                return null;
            }
            return valor;
        } catch (NumberFormatException e) {
            System.out.println("\n  ❌ Valor inválido.");
            presionarEnter();
            return null;
        }
    }

    private String leerFechaOpcional() {
        System.out.print("  Fecha (yyyy-MM-dd) [ENTER para hoy]: ");
        String fecha = scanner.nextLine().trim();
        return fecha.isEmpty() ? null : fecha;
    }

    private void imprimirMovimiento(MovimientoModelo m) {
        System.out.println("  ID Movimiento   : " + m.getId());
        System.out.println("  Nº Cuenta       : " + m.getNumCuenta());
        System.out.println("  Tipo            : " + m.getTipo());
        System.out.println("  Naturaleza      : " + m.getNaturaleza());
        System.out.println("  Interno Transf. : " + (m.isInternoTransferencia() ? "Sí" : "No"));
        System.out.println("  Valor           : " + (m.getValor() == null ? "-" : m.getValor().toPlainString()));
        System.out.println("  Fecha           : " + (m.getFecha() == null ? "-" : m.getFecha()));
        System.out.println("  Saldo Cuenta    : " + (m.getSaldoCuenta() == null ? "-" : m.getSaldoCuenta().toPlainString()));
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
