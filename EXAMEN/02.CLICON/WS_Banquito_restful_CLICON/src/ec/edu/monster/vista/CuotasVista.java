package ec.edu.monster.vista;

import ec.edu.monster.controlador.CuotasControlador;
import ec.edu.monster.modelo.CuotaAmortizacionModelo;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class CuotasVista {

    private final Scanner scanner;
    private final CuotasControlador controlador;

    public CuotasVista() {
        this.scanner = new Scanner(System.in);
        this.controlador = new CuotasControlador();
    }

    public void mostrarMenu() {
        while (true) {
            limpiarPantalla();

            System.out.println("\n");
            System.out.println("  ╔═══════════════════════════════════════╗");
            System.out.println("  ║        📅 GESTIÓN DE CUOTAS          ║");
            System.out.println("  ╚═══════════════════════════════════════╝");
            System.out.println("  1. Listar cuotas por crédito");
            System.out.println("  2. Ver detalle de una cuota");
            System.out.println("  3. Actualizar estado de una cuota");
            System.out.println("  4. Anular cuota");
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
                        listarPorCredito();
                        break;
                    case 2:
                        verCuota();
                        break;
                    case 3:
                        actualizarEstadoCuota();
                        break;
                    case 4:
                        anularCuota();
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

    private void listarPorCredito() throws IOException {
        limpiarPantalla();
        System.out.println("\n  📋 CUOTAS POR CRÉDITO");
        System.out.println("  ----------------------");

        Long idCredito = leerLong("  ID del crédito: ");
        if (idCredito == null) {
            return;
        }

        List<CuotaAmortizacionModelo> cuotas = controlador.listarPorCredito(idCredito);
        if (cuotas.isEmpty()) {
            System.out.println("\n  (No se encontraron cuotas para este crédito)");
        } else {
            System.out.println();
            for (CuotaAmortizacionModelo c : cuotas) {
                imprimirLineaCuota(c);
            }
        }

        presionarEnter();
    }

    private void verCuota() throws IOException {
        limpiarPantalla();
        System.out.println("\n  🔍 DETALLE DE CUOTA");
        System.out.println("  --------------------");

        Long idCuota = leerLong("  ID de la cuota: ");
        if (idCuota == null) {
            return;
        }

        try {
            CuotaAmortizacionModelo c = controlador.obtenerPorId(idCuota);
            System.out.println();
            imprimirDetalleCuota(c);
        } catch (IOException e) {
            System.out.println("\n  ❌ No se pudo obtener la cuota.");
            System.out.println("     Detalle: " + e.getMessage());
        }

        presionarEnter();
    }

    private void actualizarEstadoCuota() throws IOException {
        limpiarPantalla();
        System.out.println("\n  ✏ ACTUALIZAR ESTADO DE CUOTA");
        System.out.println("  -----------------------------");

        Long idCuota = leerLong("  ID de la cuota: ");
        if (idCuota == null) {
            return;
        }

        System.out.println("\n  Estados posibles: PAGADA, VENCIDA, ANULADA");
        System.out.print("  Nuevo estado: ");
        String estado = scanner.nextLine().trim().toUpperCase();

        if (estado.isEmpty()) {
            System.out.println("\n  ❌ El estado es obligatorio.");
            presionarEnter();
            return;
        }

        // fechaPago opcional
        System.out.print("  Fecha de pago (yyyy-MM-dd) [ENTER para omitir]: ");
        String fechaPago = scanner.nextLine().trim();
        if (fechaPago.isEmpty()) {
            fechaPago = null;
        }

        try {
            CuotaAmortizacionModelo c = controlador.actualizarEstado(idCuota, estado, fechaPago);
            System.out.println("\n  ✅ Cuota actualizada:");
            imprimirDetalleCuota(c);
        } catch (IOException e) {
            System.out.println("\n  ❌ Error al actualizar la cuota.");
            System.out.println("     Detalle: " + e.getMessage());
        }

        presionarEnter();
    }

    private void anularCuota() throws IOException {
        limpiarPantalla();
        System.out.println("\n  🗑 ANULAR CUOTA");
        System.out.println("  ---------------");

        Long idCuota = leerLong("  ID de la cuota: ");
        if (idCuota == null) {
            return;
        }

        System.out.print("\n  ⚠ ¿Seguro que desea anular la cuota? (s/N): ");
        String conf = scanner.nextLine().trim().toLowerCase();
        if (!"s".equals(conf)) {
            System.out.println("\n  Operación cancelada.");
            presionarEnter();
            return;
        }

        try {
            controlador.anular(idCuota);
            System.out.println("\n  ✅ Cuota anulada correctamente.");
        } catch (IOException e) {
            System.out.println("\n  ❌ Error al anular la cuota.");
            System.out.println("     Detalle: " + e.getMessage());
        }

        presionarEnter();
    }

    // ========= Helpers =========

    private Long leerLong(String mensaje) {
        System.out.print(mensaje);
        String txt = scanner.nextLine().trim();
        try {
            return Long.parseLong(txt);
        } catch (NumberFormatException e) {
            System.out.println("\n  ❌ Valor numérico inválido.");
            presionarEnter();
            return null;
        }
    }

    private void imprimirLineaCuota(CuotaAmortizacionModelo c) {
        System.out.printf("  ID:%-5d | Credito:%-5d | N°:%-3d | Vence:%-10s | Cuota:%-10s | Saldo:%-10s | Estado:%s%n",
                c.getId(),
                c.getIdCredito(),
                c.getNumeroCuota(),
                nulo(c.getFechaVencimiento()),
                money(c.getValorCuota()),
                money(c.getSaldo()),
                nulo(c.getEstado()));
    }

    private void imprimirDetalleCuota(CuotaAmortizacionModelo c) {
        System.out.println("  ID Cuota        : " + c.getId());
        System.out.println("  ID Crédito      : " + c.getIdCredito());
        System.out.println("  Nº Cuota        : " + c.getNumeroCuota());
        System.out.println("  Valor Cuota     : " + money(c.getValorCuota()));
        System.out.println("  Interés         : " + money(c.getInteresPagado()));
        System.out.println("  Capital         : " + money(c.getCapitalPagado()));
        System.out.println("  Saldo           : " + money(c.getSaldo()));
        System.out.println("  Vencimiento     : " + nulo(c.getFechaVencimiento()));
        System.out.println("  Estado          : " + nulo(c.getEstado()));
    }

    private String nulo(String s) {
        return (s == null || s.trim().isEmpty()) ? "-" : s;
    }

    private String money(java.math.BigDecimal b) {
        return b == null ? "-" : b.toPlainString();
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
