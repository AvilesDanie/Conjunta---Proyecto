package ec.edu.monster.vista;

import ec.edu.monster.controlador.CuentaController;
import ec.edu.monster.modelo.Cuenta;
import java.util.List;
import java.util.Scanner;

/**
 * Vista de consola para gestión de cuentas bancarias
 * @author CLICON
 */
public class CuentasVista {
    private final Scanner scanner = new Scanner(System.in);
    private final CuentaController controller = new CuentaController();

    public void mostrarMenu() {
        while (true) {
            ConsolaUtil.limpiarPantalla();
            System.out.println("\n╔═══════════════════════════════════════╗");
            System.out.println("║      📋 GESTIÓN DE CUENTAS           ║");
            System.out.println("╠═══════════════════════════════════════╣");
            System.out.println("║  1. Listar todas las cuentas         ║");
            System.out.println("║  2. Buscar cuenta por número         ║");
            System.out.println("║  3. Ver cuentas de un cliente        ║");
            System.out.println("║  4. Crear nueva cuenta               ║");
            System.out.println("║  5. Volver al menú principal         ║");
            System.out.println("╚═══════════════════════════════════════╝");
            System.out.print("➤ Seleccione una opción: ");

            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    listarCuentas();
                    break;
                case 2:
                    buscarCuenta();
                    break;
                case 3:
                    verCuentasCliente();
                    break;
                case 4:
                    crearCuenta();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("❌ Opción inválida");
            }
        }
    }

    private void listarCuentas() {
        try {
            List<Cuenta> cuentas = controller.listarCuentas();
            System.out.println("\n┌─────────────────────────────────────────────────────────────────────┐");
            System.out.println("│                    📋 LISTADO DE CUENTAS                            │");
            System.out.println("├─────────────────────────────────────────────────────────────────────┤");
            
            if (cuentas.isEmpty()) {
                System.out.println("│  ⚠️  No hay cuentas registradas                                     │");
            } else {
                for (Cuenta c : cuentas) {
                    System.out.println("├─────────────────────────────────────────────────────────────────────┤");
                    System.out.printf("│  Número: %-20s │ Cédula: %-15s │%n", c.getNumCuenta(), c.getCedula());
                    System.out.printf("│  Tipo: %-22s │ Saldo: $%-14.2f │%n", c.getTipoCuenta(), c.getSaldo());
                    System.out.printf("│  Estado: %-52s │%n", c.getEstado());
                }
            }
            System.out.println("└─────────────────────────────────────────────────────────────────────┘");
        } catch (Exception e) {
            System.out.println("❌ Error al listar cuentas: " + e.getMessage());
        }
        ConsolaUtil.presionarEnter();
    }

    private void buscarCuenta() {
        System.out.print("➤ Ingrese número de cuenta: ");
        String numCuenta = scanner.nextLine();

        try {
            Cuenta cuenta = controller.obtenerCuenta(numCuenta);
            if (cuenta != null) {
                System.out.println("\n┌─────────────────────────────────────────────────────┐");
                System.out.println("│           📄 INFORMACIÓN DE LA CUENTA               │");
                System.out.println("├─────────────────────────────────────────────────────┤");
                System.out.printf("│  Número: %-42s │%n", cuenta.getNumCuenta());
                System.out.printf("│  Cédula: %-42s │%n", cuenta.getCedula());
                System.out.printf("│  Tipo: %-44s │%n", cuenta.getTipoCuenta());
                System.out.printf("│  Saldo: $%-41.2f │%n", cuenta.getSaldo());
                System.out.printf("│  Estado: %-42s │%n", cuenta.getEstado());
                System.out.println("└─────────────────────────────────────────────────────┘");
            } else {
                System.out.println("❌ Cuenta no encontrada");
            }
        } catch (Exception e) {
            System.out.println("❌ Error al buscar cuenta: " + e.getMessage());
        }
    }

    private void verCuentasCliente() {
        System.out.print("➤ Ingrese cédula del cliente: ");
        String cedula = scanner.nextLine();

        try {
            List<Cuenta> cuentas = controller.listarCuentasPorCliente(cedula);
            System.out.println("\n┌─────────────────────────────────────────────────────────────────────┐");
            System.out.printf("│           📋 CUENTAS DEL CLIENTE: %-30s │%n", cedula);
            System.out.println("├─────────────────────────────────────────────────────────────────────┤");
            
            if (cuentas.isEmpty()) {
                System.out.println("│  ⚠️  Este cliente no tiene cuentas registradas                      │");
            } else {
                for (Cuenta c : cuentas) {
                    System.out.println("├─────────────────────────────────────────────────────────────────────┤");
                    System.out.printf("│  Número: %-20s │ Tipo: %-20s │%n", c.getNumCuenta(), c.getTipoCuenta());
                    System.out.printf("│  Saldo: $%-25.2f │ Estado: %-15s │%n", c.getSaldo(), c.getEstado());
                }
            }
            System.out.println("└─────────────────────────────────────────────────────────────────────┘");
        } catch (Exception e) {
            System.out.println("❌ Error al consultar cuentas: " + e.getMessage());
        }
    }

    private void crearCuenta() {
        System.out.println("\n┌──────────────────────────────────────┐");
        System.out.println("│      ➕ CREAR NUEVA CUENTA           │");
        System.out.println("└──────────────────────────────────────┘");

        System.out.print("➤ Cédula del cliente: ");
        String cedula = scanner.nextLine();

        System.out.print("➤ Tipo de cuenta (AHORROS/CORRIENTE): ");
        String tipo = scanner.nextLine();

        System.out.print("➤ Saldo inicial: ");
        double saldo = scanner.nextDouble();
        scanner.nextLine();

        Cuenta cuenta = new Cuenta();
        cuenta.setCedula(cedula);
        cuenta.setTipoCuenta(tipo);
        cuenta.setSaldo(saldo);
        cuenta.setEstado("ACTIVA");

        try {
            if (controller.crearCuenta(cuenta)) {
                System.out.println("✅ Cuenta creada exitosamente");
            } else {
                System.out.println("❌ No se pudo crear la cuenta");
            }
        } catch (Exception e) {
            System.out.println("❌ Error al crear cuenta: " + e.getMessage());
        }
    }
}
