package ec.edu.monster.vista;

import ec.edu.monster.controlador.FacturaController;
import ec.edu.monster.modelo.Factura;
import java.util.List;
import java.util.Scanner;

/**
 * Vista de consola para gestión de facturas
 * @author CLICON
 */
public class FacturasVista {
    private final Scanner scanner = new Scanner(System.in);
    private final FacturaController controller = new FacturaController();

    public void mostrarMenu() {
        while (true) {
            ConsolaUtil.limpiarPantalla();
            System.out.println("\n╔═══════════════════════════════════════╗");
            System.out.println("║      🧾 GESTIÓN DE FACTURAS          ║");
            System.out.println("╠═══════════════════════════════════════╣");
            System.out.println("║  1. Listar todas las facturas        ║");
            System.out.println("║  2. Buscar factura por ID            ║");
            System.out.println("║  3. Ver facturas por cliente         ║");
            System.out.println("║  4. Volver al menú principal         ║");
            System.out.println("╚═══════════════════════════════════════╝");
            System.out.print("➤ Seleccione una opción: ");

            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    listarFacturas();
                    break;
                case 2:
                    buscarFactura();
                    break;
                case 3:
                    verFacturasCliente();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("❌ Opción inválida");
            }
        }
    }

    private void listarFacturas() {
        try {
            List<Factura> facturas = controller.listarFacturas();
            System.out.println("\n┌─────────────────────────────────────────────────────────────────────┐");
            System.out.println("│                    🧾 LISTADO DE FACTURAS                           │");
            System.out.println("├─────────────────────────────────────────────────────────────────────┤");
            
            if (facturas.isEmpty()) {
                System.out.println("│  ⚠️  No hay facturas registradas                                    │");
            } else {
                for (Factura f : facturas) {
                    System.out.println("├─────────────────────────────────────────────────────────────────────┤");
                    System.out.printf("│  ID: %-15d │ Fecha: %-25s │%n", f.getId(), f.getFecha());
                    System.out.printf("│  Cliente: %-20s │ Total: $%-20.2f │%n", f.getCedula(), f.getTotal());
                    System.out.printf("│  Crédito: %-55d │%n", f.getIdCredito());
                }
            }
            System.out.println("└─────────────────────────────────────────────────────────────────────┘");
        } catch (Exception e) {
            System.out.println("❌ Error al listar facturas: " + e.getMessage());
        }
        ConsolaUtil.presionarEnter();
    }

    private void buscarFactura() {
        System.out.print("➤ Ingrese ID de la factura: ");
        Long id = scanner.nextLong();
        scanner.nextLine();

        try {
            Factura factura = controller.obtenerFactura(id);
            if (factura != null) {
                System.out.println("\n┌─────────────────────────────────────────────────────┐");
                System.out.println("│           📄 INFORMACIÓN DE LA FACTURA              │");
                System.out.println("├─────────────────────────────────────────────────────┤");
                System.out.printf("│  ID: %-46d │%n", factura.getId());
                System.out.printf("│  Fecha: %-42s │%n", factura.getFecha());
                System.out.printf("│  Cliente: %-40s │%n", factura.getCedula());
                System.out.printf("│  Total: $%-41.2f │%n", factura.getTotal());
                System.out.printf("│  ID Crédito: %-37d │%n", factura.getIdCredito());
                System.out.println("└─────────────────────────────────────────────────────┘");
            } else {
                System.out.println("❌ Factura no encontrada");
            }
        } catch (Exception e) {
            System.out.println("❌ Error al buscar factura: " + e.getMessage());
        }
    }

    private void verFacturasCliente() {
        System.out.print("➤ Ingrese cédula del cliente: ");
        String cedula = scanner.nextLine();

        try {
            List<Factura> facturas = controller.listarFacturasPorCliente(cedula);
            System.out.println("\n┌─────────────────────────────────────────────────────────────────────┐");
            System.out.printf("│           🧾 FACTURAS DEL CLIENTE: %-31s │%n", cedula);
            System.out.println("├─────────────────────────────────────────────────────────────────────┤");
            
            if (facturas.isEmpty()) {
                System.out.println("│  ⚠️  Este cliente no tiene facturas registradas                     │");
            } else {
                for (Factura f : facturas) {
                    System.out.println("├─────────────────────────────────────────────────────────────────────┤");
                    System.out.printf("│  ID: %-15d │ Fecha: %-25s │%n", f.getId(), f.getFecha());
                    System.out.printf("│  Total: $%-25.2f │ Crédito: %-20d │%n", f.getTotal(), f.getIdCredito());
                }
            }
            System.out.println("└─────────────────────────────────────────────────────────────────────┘");
        } catch (Exception e) {
            System.out.println("❌ Error al consultar facturas: " + e.getMessage());
        }
    }
}
