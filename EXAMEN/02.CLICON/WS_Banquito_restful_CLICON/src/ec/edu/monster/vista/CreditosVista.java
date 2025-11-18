package ec.edu.monster.vista;

import ec.edu.monster.controlador.CreditoController;
import java.util.Scanner;

/**
 * Vista de consola para gestión de créditos
 * @author CLICON
 */
public class CreditosVista {
    private final Scanner scanner = new Scanner(System.in);
    private final CreditoController controller = new CreditoController();

    public void mostrarMenu() {
        while (true) {
            ConsolaUtil.limpiarPantalla();
            System.out.println("\n╔═══════════════════════════════════════╗");
            System.out.println("║      💳 GESTIÓN DE CRÉDITOS          ║");
            System.out.println("╠═══════════════════════════════════════╣");
            System.out.println("║  1. Evaluar crédito                  ║");
            System.out.println("║  2. Solicitar crédito                ║");
            System.out.println("║  3. Consultar crédito                ║");
            System.out.println("║  4. Volver al menú principal         ║");
            System.out.println("╚═══════════════════════════════════════╝");
            System.out.print("➤ Seleccione una opción: ");

            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    evaluarCredito();
                    break;
                case 2:
                    solicitarCredito();
                    break;
                case 3:
                    consultarCredito();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("❌ Opción inválida");
            }
        }
    }

    private void evaluarCredito() {
        System.out.println("\n┌──────────────────────────────────────┐");
        System.out.println("│      🔍 EVALUAR CRÉDITO              │");
        System.out.println("└──────────────────────────────────────┘");

        System.out.print("➤ Cédula del cliente: ");
        String cedula = scanner.nextLine();

        System.out.print("➤ Monto solicitado: ");
        double monto = scanner.nextDouble();

        System.out.print("➤ Plazo en meses: ");
        int plazo = scanner.nextInt();
        scanner.nextLine();

        try {
            String resultado = controller.evaluarCredito(cedula, monto, plazo);
            System.out.println("\n┌─────────────────────────────────────────────────────┐");
            System.out.println("│           📊 RESULTADO DE LA EVALUACIÓN             │");
            System.out.println("├─────────────────────────────────────────────────────┤");
            System.out.println("│  " + resultado);
            System.out.println("└─────────────────────────────────────────────────────┘");
        } catch (Exception e) {
            System.out.println("❌ Error al evaluar crédito: " + e.getMessage());
        }
        ConsolaUtil.presionarEnter();
    }

    private void solicitarCredito() {
        System.out.println("\n┌──────────────────────────────────────┐");
        System.out.println("│      📝 SOLICITAR CRÉDITO            │");
        System.out.println("└──────────────────────────────────────┘");

        System.out.print("➤ Cédula del cliente: ");
        String cedula = scanner.nextLine();

        System.out.print("➤ Monto solicitado: ");
        double monto = scanner.nextDouble();

        System.out.print("➤ Plazo en meses: ");
        int plazo = scanner.nextInt();
        scanner.nextLine();

        System.out.print("➤ Número de cuenta para desembolso: ");
        String numCuenta = scanner.nextLine();

        try {
            String resultado = controller.crearCredito(cedula, monto, plazo, numCuenta);
            if (resultado != null && !resultado.contains("rechazado")) {
                System.out.println("✅ Crédito aprobado exitosamente");
                System.out.println("📄 Detalles: " + resultado);
            } else {
                System.out.println("❌ Crédito rechazado");
                System.out.println("ℹ️  Razón: " + resultado);
            }
        } catch (Exception e) {
            System.out.println("❌ Error al solicitar crédito: " + e.getMessage());
        }
    }

    private void consultarCredito() {
        System.out.print("➤ Ingrese ID del crédito: ");
        Long id = scanner.nextLong();
        scanner.nextLine();

        try {
            String credito = controller.obtenerCredito(id);
            if (credito != null) {
                System.out.println("\n┌─────────────────────────────────────────────────────┐");
                System.out.println("│           📄 INFORMACIÓN DEL CRÉDITO                │");
                System.out.println("├─────────────────────────────────────────────────────┤");
                System.out.println("│  " + credito);
                System.out.println("└─────────────────────────────────────────────────────┘");
            } else {
                System.out.println("❌ Crédito no encontrado");
            }
        } catch (Exception e) {
            System.out.println("❌ Error al consultar crédito: " + e.getMessage());
        }
    }
}
