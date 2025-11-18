package ec.edu.monster.vista;

import ec.edu.monster.controlador.CuotaController;
import ec.edu.monster.modelo.Cuota;
import java.util.List;
import java.util.Scanner;

/**
 * Vista de consola para gestión de cuotas
 * @author CLICON
 */
public class CuotasVista {
    private final Scanner scanner = new Scanner(System.in);
    private final CuotaController controller = new CuotaController();

    public void mostrarMenu() {
        while (true) {
            ConsolaUtil.limpiarPantalla();
            System.out.println("\n╔═══════════════════════════════════════╗");
            System.out.println("║      📅 GESTIÓN DE CUOTAS            ║");
            System.out.println("╠═══════════════════════════════════════╣");
            System.out.println("║  1. Ver cuotas de un crédito         ║");
            System.out.println("║  2. Consultar cuota específica       ║");
            System.out.println("║  3. Pagar cuota                      ║");
            System.out.println("║  4. Anular cuota                     ║");
            System.out.println("║  5. Volver al menú principal         ║");
            System.out.println("╚═══════════════════════════════════════╝");
            System.out.print("➤ Seleccione una opción: ");

            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    verCuotasCredito();
                    break;
                case 2:
                    consultarCuota();
                    break;
                case 3:
                    pagarCuota();
                    break;
                case 4:
                    anularCuota();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("❌ Opción inválida");
            }
        }
    }

    private void verCuotasCredito() {
        System.out.print("➤ Ingrese ID del crédito: ");
        Long idCredito = scanner.nextLong();
        scanner.nextLine();

        try {
            List<Cuota> cuotas = controller.listarCuotasPorCredito(idCredito);
            System.out.println("\n┌─────────────────────────────────────────────────────────────────────┐");
            System.out.printf("│           📅 CUOTAS DEL CRÉDITO: %-33d │%n", idCredito);
            System.out.println("├─────────────────────────────────────────────────────────────────────┤");
            
            if (cuotas.isEmpty()) {
                System.out.println("│  ⚠️  No hay cuotas registradas para este crédito                    │");
            } else {
                for (Cuota c : cuotas) {
                    System.out.println("├─────────────────────────────────────────────────────────────────────┤");
                    System.out.printf("│  Cuota #%-3d │ Valor: $%-15.2f │ Estado: %-15s │%n", 
                        c.getNumeroCuota(), c.getValorCuota(), c.getEstado());
                    System.out.printf("│  Vencimiento: %-20s │ Saldo: $%-18.2f │%n", 
                        c.getFechaVencimiento(), c.getSaldoPendiente());
                }
            }
            System.out.println("└─────────────────────────────────────────────────────────────────────┘");
        } catch (Exception e) {
            System.out.println("❌ Error al listar cuotas: " + e.getMessage());
        }
        ConsolaUtil.presionarEnter();
    }

    private void consultarCuota() {
        System.out.print("➤ Ingrese ID de la cuota: ");
        Long id = scanner.nextLong();
        scanner.nextLine();

        try {
            Cuota cuota = controller.obtenerCuota(id);
            if (cuota != null) {
                System.out.println("\n┌─────────────────────────────────────────────────────┐");
                System.out.println("│           📄 INFORMACIÓN DE LA CUOTA                │");
                System.out.println("├─────────────────────────────────────────────────────┤");
                System.out.printf("│  ID: %-46d │%n", cuota.getId());
                System.out.printf("│  Número de Cuota: %-34d │%n", cuota.getNumeroCuota());
                System.out.printf("│  Valor: $%-42.2f │%n", cuota.getValorCuota());
                System.out.printf("│  Fecha Vencimiento: %-30s │%n", cuota.getFechaVencimiento());
                System.out.printf("│  Estado: %-42s │%n", cuota.getEstado());
                System.out.printf("│  Saldo Pendiente: $%-32.2f │%n", cuota.getSaldoPendiente());
                System.out.println("└─────────────────────────────────────────────────────┘");
            } else {
                System.out.println("❌ Cuota no encontrada");
            }
        } catch (Exception e) {
            System.out.println("❌ Error al consultar cuota: " + e.getMessage());
        }
    }

    private void pagarCuota() {
        System.out.print("➤ Ingrese ID de la cuota a pagar: ");
        Long id = scanner.nextLong();
        scanner.nextLine();

        try {
            if (controller.actualizarEstadoCuota(id, "PAGADA")) {
                System.out.println("✅ Cuota pagada exitosamente");
            } else {
                System.out.println("❌ No se pudo procesar el pago");
            }
        } catch (Exception e) {
            System.out.println("❌ Error al pagar cuota: " + e.getMessage());
        }
    }

    private void anularCuota() {
        System.out.print("➤ Ingrese ID de la cuota a anular: ");
        Long id = scanner.nextLong();
        scanner.nextLine();

        System.out.print("⚠️  ¿Está seguro de anular esta cuota? (S/N): ");
        String confirmacion = scanner.nextLine();

        if (confirmacion.equalsIgnoreCase("S")) {
            try {
                if (controller.anularCuota(id)) {
                    System.out.println("✅ Cuota anulada exitosamente");
                } else {
                    System.out.println("❌ No se pudo anular la cuota");
                }
            } catch (Exception e) {
                System.out.println("❌ Error al anular cuota: " + e.getMessage());
            }
        }
    }
}
