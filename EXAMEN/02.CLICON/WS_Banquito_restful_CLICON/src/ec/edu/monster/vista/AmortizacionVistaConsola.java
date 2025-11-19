package ec.edu.monster.vista;

import ec.edu.monster.controlador.CuotasControlador;
import ec.edu.monster.modelo.CuotaAmortizacionModelo;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class AmortizacionVistaConsola {

    private final Scanner scanner;
    private final CuotasControlador controlador;

    public AmortizacionVistaConsola() {
        this.scanner = new Scanner(System.in);
        this.controlador = new CuotasControlador();
    }

    public void mostrarMenu() {
        while (true) {
            limpiarPantalla();

            System.out.println("\n");
            System.out.println("  ╔═══════════════════════════════════════╗");
            System.out.println("  ║     📊 TABLA DE AMORTIZACIÓN         ║");
            System.out.println("  ╚═══════════════════════════════════════╝");
            System.out.println("  1. Ver tabla por ID de crédito");
            System.out.println("  0. Volver al menú principal");

            System.out.print("\n  ➤ Seleccione una opción [0-1]: ");
            String linea = scanner.nextLine();
            int opcion;
            try {
                opcion = Integer.parseInt(linea);
            } catch (NumberFormatException e) {
                System.out.println("\n  ❌ Debe ingresar un número.");
                presionarEnter();
                continue;
            }

            if (opcion == 0) {
                return;
            } else if (opcion == 1) {
                mostrarTablaPorCredito();
            } else {
                System.out.println("\n  ❌ Opción inválida.");
                presionarEnter();
            }
        }
    }

    private void mostrarTablaPorCredito() {
        limpiarPantalla();
        System.out.println("\n  📊 TABLA DE AMORTIZACIÓN POR CRÉDITO");
        System.out.println("  -------------------------------------");

        System.out.print("  ID del crédito: ");
        String txtId = scanner.nextLine().trim();
        Long idCredito;
        try {
            idCredito = Long.parseLong(txtId);
        } catch (NumberFormatException e) {
            System.out.println("\n  ❌ ID inválido.");
            presionarEnter();
            return;
        }

        try {
            List<CuotaAmortizacionModelo> cuotas = controlador.listarPorCredito(idCredito);
            if (cuotas.isEmpty()) {
                System.out.println("\n  (No se encontraron cuotas para este crédito)");
                presionarEnter();
                return;
            }

            System.out.println();
            System.out.printf("  %-5s %-12s %-12s %-12s %-12s %-12s %-10s%n",
                    "N°", "Vencimiento", "Cuota", "Interés", "Capital", "Saldo", "Estado");
            System.out.println("  ---------------------------------------------------------------------------");

            for (CuotaAmortizacionModelo c : cuotas) {
                System.out.printf("  %-5d %-12s %-12s %-12s %-12s %-12s %-10s%n",
                        c.getNumeroCuota(),
                        nulo(c.getFechaVencimiento()),
                        money(c.getValorCuota()),
                        money(c.getInteresPagado()),
                        money(c.getCapitalPagado()),
                        money(c.getSaldo()),
                        nulo(c.getEstado()));
            }

        } catch (IOException e) {
            System.out.println("\n  ❌ Error al obtener la tabla.");
            System.out.println("     Detalle: " + e.getMessage());
        }

        presionarEnter();
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
