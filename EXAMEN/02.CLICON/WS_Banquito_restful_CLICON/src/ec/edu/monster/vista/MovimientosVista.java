package ec.edu.monster.vista;

import ec.edu.monster.controlador.MovimientoController;
import ec.edu.monster.modelo.Movimiento;
import java.util.List;
import java.util.Scanner;

/**
 * Vista de consola para gestión de movimientos bancarios
 * @author CLICON
 */
public class MovimientosVista {
    private final Scanner scanner = new Scanner(System.in);
    private final MovimientoController controller = new MovimientoController();

    public void mostrarMenu() {
        while (true) {
            ConsolaUtil.limpiarPantalla();
            System.out.println("\n╔═══════════════════════════════════════╗");
            System.out.println("║      💰 MOVIMIENTOS BANCARIOS        ║");
            System.out.println("╠═══════════════════════════════════════╣");
            System.out.println("║  1. Ver movimientos de una cuenta    ║");
            System.out.println("║  2. Realizar depósito                ║");
            System.out.println("║  3. Realizar retiro                  ║");
            System.out.println("║  4. Realizar transferencia           ║");
            System.out.println("║  5. Volver al menú principal         ║");
            System.out.println("╚═══════════════════════════════════════╝");
            System.out.print("➤ Seleccione una opción: ");

            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    verMovimientos();
                    break;
                case 2:
                    realizarDeposito();
                    break;
                case 3:
                    realizarRetiro();
                    break;
                case 4:
                    realizarTransferencia();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("❌ Opción inválida");
            }
        }
    }

    private void verMovimientos() {
        System.out.print("➤ Ingrese número de cuenta: ");
        String numCuenta = scanner.nextLine();

        try {
            List<Movimiento> movimientos = controller.listarMovimientosPorCuenta(numCuenta);
            System.out.println("\n┌─────────────────────────────────────────────────────────────────────┐");
            System.out.printf("│           💰 MOVIMIENTOS DE LA CUENTA: %-26s │%n", numCuenta);
            System.out.println("├─────────────────────────────────────────────────────────────────────┤");
            
            if (movimientos.isEmpty()) {
                System.out.println("│  ⚠️  No hay movimientos registrados                                 │");
            } else {
                for (Movimiento m : movimientos) {
                    System.out.println("├─────────────────────────────────────────────────────────────────────┤");
                    System.out.printf("│  Tipo: %-20s │ Monto: $%-20.2f │%n", m.getTipo(), m.getMonto());
                    System.out.printf("│  Fecha: %-25s │ ID: %-22d │%n", m.getFecha(), m.getId());
                    System.out.printf("│  Descripción: %-53s │%n", m.getDescripcion());
                }
            }
            System.out.println("└─────────────────────────────────────────────────────────────────────┘");
        } catch (Exception e) {
            System.out.println("❌ Error al consultar movimientos: " + e.getMessage());
        }
        ConsolaUtil.presionarEnter();
    }

    private void realizarDeposito() {
        System.out.println("\n┌──────────────────────────────────────┐");
        System.out.println("│      💵 REALIZAR DEPÓSITO            │");
        System.out.println("└──────────────────────────────────────┘");

        System.out.print("➤ Número de cuenta: ");
        String numCuenta = scanner.nextLine();

        System.out.print("➤ Monto a depositar: ");
        double monto = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("➤ Descripción: ");
        String descripcion = scanner.nextLine();

        try {
            if (controller.crearMovimiento(numCuenta, "DEPOSITO", monto, descripcion)) {
                System.out.println("✅ Depósito realizado exitosamente");
            } else {
                System.out.println("❌ No se pudo realizar el depósito");
            }
        } catch (Exception e) {
            System.out.println("❌ Error al realizar depósito: " + e.getMessage());
        }
    }

    private void realizarRetiro() {
        System.out.println("\n┌──────────────────────────────────────┐");
        System.out.println("│      💸 REALIZAR RETIRO              │");
        System.out.println("└──────────────────────────────────────┘");

        System.out.print("➤ Número de cuenta: ");
        String numCuenta = scanner.nextLine();

        System.out.print("➤ Monto a retirar: ");
        double monto = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("➤ Descripción: ");
        String descripcion = scanner.nextLine();

        try {
            if (controller.crearMovimiento(numCuenta, "RETIRO", monto, descripcion)) {
                System.out.println("✅ Retiro realizado exitosamente");
            } else {
                System.out.println("❌ No se pudo realizar el retiro");
            }
        } catch (Exception e) {
            System.out.println("❌ Error al realizar retiro: " + e.getMessage());
        }
    }

    private void realizarTransferencia() {
        System.out.println("\n┌──────────────────────────────────────┐");
        System.out.println("│      🔄 REALIZAR TRANSFERENCIA       │");
        System.out.println("└──────────────────────────────────────┘");

        System.out.print("➤ Número de cuenta origen: ");
        String numCuentaOrigen = scanner.nextLine();

        System.out.print("➤ Monto a transferir: ");
        double monto = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("➤ Número de cuenta destino: ");
        String numCuentaDestino = scanner.nextLine();

        System.out.print("➤ Descripción: ");
        String descripcion = scanner.nextLine();

        try {
            String descRetiro = "TRANSFERENCIA a " + numCuentaDestino + " - " + descripcion;
            String descDeposito = "TRANSFERENCIA desde " + numCuentaOrigen + " - " + descripcion;
            
            if (controller.crearMovimiento(numCuentaOrigen, "RETIRO", monto, descRetiro) &&
                controller.crearMovimiento(numCuentaDestino, "DEPOSITO", monto, descDeposito)) {
                System.out.println("✅ Transferencia realizada exitosamente");
            } else {
                System.out.println("❌ No se pudo completar la transferencia");
            }
        } catch (Exception e) {
            System.out.println("❌ Error al realizar transferencia: " + e.getMessage());
        }
    }
}
