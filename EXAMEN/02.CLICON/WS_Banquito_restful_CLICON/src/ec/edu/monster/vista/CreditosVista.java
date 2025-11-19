package ec.edu.monster.vista;

import ec.edu.monster.controlador.CreditosControlador;
import ec.edu.monster.modelo.CreditoModelo;
import ec.edu.monster.modelo.ResultadoEvaluacionModelo;
import ec.edu.monster.modelo.SolicitudCreditoModelo;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Scanner;

public class CreditosVista {

    private final Scanner scanner;
    private final CreditosControlador controlador;

    public CreditosVista() {
        this.scanner = new Scanner(System.in);
        this.controlador = new CreditosControlador();
    }

    public void mostrarMenu() {
        while (true) {
            limpiarPantalla();

            System.out.println("\n");
            System.out.println("  ╔═══════════════════════════════════════╗");
            System.out.println("  ║           💰 GESTIÓN DE CRÉDITOS     ║");
            System.out.println("  ╚═══════════════════════════════════════╝");
            System.out.println("  1. Evaluar crédito");
            System.out.println("  2. Evaluar y crear crédito");
            System.out.println("  3. Consultar crédito por ID");
            System.out.println("  0. Volver al menú principal");

            System.out.print("\n  ➤ Seleccione una opción [0-3]: ");
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
                        evaluarCredito(false);
                        break;
                    case 2:
                        evaluarCredito(true);
                        break;
                    case 3:
                        consultarCreditoPorId();
                        break;
                    case 0:
                        return; // volver al HomeBanquitoVista
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

    private void evaluarCredito(boolean crearSiAprueba) throws IOException {
        limpiarPantalla();
        System.out.println("\n  📝 EVALUACIÓN DE CRÉDITO");
        System.out.println("  -------------------------");

        SolicitudCreditoModelo solicitud = leerSolicitud();

        if (solicitud == null) {
            return;
        }

        ResultadoEvaluacionModelo res = controlador.evaluar(solicitud);

        System.out.println("\n  ✅ Resultado de evaluación:");
        System.out.println("  Sujeto de crédito : " + (res.sujetoCredito ? "Sí" : "No"));
        System.out.println("  Monto máximo      : " +
                (res.montoMaximo == null ? "-" : res.montoMaximo.toPlainString()));
        System.out.println("  Aprobado          : " + (res.aprobado ? "Sí" : "No"));
        System.out.println("  Motivo            : " + (res.motivo == null ? "-" : res.motivo));

        if (!crearSiAprueba || !res.aprobado) {
            presionarEnter();
            return;
        }

        System.out.print("\n  ¿Desea crear el crédito con estos datos? (s/N): ");
        String conf = scanner.nextLine().trim().toLowerCase();
        if (!"s".equals(conf)) {
            System.out.println("\n  Operación cancelada.");
            presionarEnter();
            return;
        }

        try {
            CreditoModelo credito = controlador.crearCredito(solicitud);
            System.out.println("\n  ✅ Crédito creado correctamente:");
            imprimirCredito(credito);
        } catch (IOException e) {
            System.out.println("\n  ❌ Error al crear el crédito.");
            System.out.println("     Detalle: " + e.getMessage());
        }

        presionarEnter();
    }

    private void consultarCreditoPorId() throws IOException {
        limpiarPantalla();
        System.out.println("\n  🔍 CONSULTAR CRÉDITO POR ID");
        System.out.println("  ----------------------------");

        System.out.print("  ID del crédito: ");
        String txtId = scanner.nextLine().trim();
        Long id;
        try {
            id = Long.parseLong(txtId);
        } catch (NumberFormatException e) {
            System.out.println("\n  ❌ ID inválido.");
            presionarEnter();
            return;
        }

        try {
            CreditoModelo credito = controlador.obtenerPorId(id);
            System.out.println();
            imprimirCredito(credito);
        } catch (IOException e) {
            System.out.println("\n  ❌ No se pudo obtener el crédito.");
            System.out.println("     Detalle: " + e.getMessage());
        }

        presionarEnter();
    }

    // ========= Helpers =========

    private SolicitudCreditoModelo leerSolicitud() {
        SolicitudCreditoModelo solicitud = new SolicitudCreditoModelo();

        System.out.print("  Cédula del cliente: ");
        solicitud.cedula = scanner.nextLine().trim();
        if (solicitud.cedula.isEmpty()) {
            System.out.println("\n  ❌ La cédula es obligatoria.");
            presionarEnter();
            return null;
        }

        System.out.print("  Precio del producto (monto del crédito): ");
        String txtMonto = scanner.nextLine().trim();
        try {
            solicitud.precioProducto = new BigDecimal(txtMonto);
            if (solicitud.precioProducto.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("\n  ❌ El monto debe ser mayor a cero.");
                presionarEnter();
                return null;
            }
        } catch (NumberFormatException e) {
            System.out.println("\n  ❌ Monto inválido.");
            presionarEnter();
            return null;
        }

        System.out.print("  Plazo en meses: ");
        String txtPlazo = scanner.nextLine().trim();
        try {
            solicitud.plazoMeses = Integer.parseInt(txtPlazo);
            if (solicitud.plazoMeses <= 0) {
                System.out.println("\n  ❌ El plazo debe ser mayor a cero.");
                presionarEnter();
                return null;
            }
        } catch (NumberFormatException e) {
            System.out.println("\n  ❌ Plazo inválido.");
            presionarEnter();
            return null;
        }

        System.out.print("  Nº de cuenta asociada al crédito: ");
        solicitud.numCuentaCredito = scanner.nextLine().trim();
        if (solicitud.numCuentaCredito.isEmpty()) {
            System.out.println("\n  ❌ La cuenta asociada es obligatoria.");
            presionarEnter();
            return null;
        }

        return solicitud;
    }

    private void imprimirCredito(CreditoModelo c) {
        System.out.println("  ID Crédito      : " + c.getId());
        System.out.println("  Cliente (cédula): " + c.getCedulaCliente());
        System.out.println("  Cliente (nombre): " + c.getNombreCliente());
        System.out.println("  Monto           : " + (c.getMonto() == null ? "-" : c.getMonto().toPlainString()));
        System.out.println("  Plazo (meses)   : " + c.getPlazoMeses());
        System.out.println("  Tasa anual      : " + (c.getTasaAnual() == null ? "-" : c.getTasaAnual().toPlainString()) + " %");
        System.out.println("  Fecha aprobación: " + (c.getFechaAprobacion() == null ? "-" : c.getFechaAprobacion()));
        System.out.println("  Estado          : " + c.getEstado());
        System.out.println("  Cuenta asociada : " + c.getNumCuentaAsociada());
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
