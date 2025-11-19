package ec.edu.monster.vista;

import ec.edu.monster.controlador.FacturasControlador;
import ec.edu.monster.modelo.DetalleFacturaModelo;
import ec.edu.monster.modelo.FacturaModelo;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class FacturasVista {

    private final Scanner scanner;
    private final FacturasControlador controlador;

    public FacturasVista() {
        this.scanner = new Scanner(System.in);
        this.controlador = new FacturasControlador();
    }

    public void mostrarMenu() {
        while (true) {
            limpiarPantalla();

            System.out.println("\n");
            System.out.println("  ╔═══════════════════════════════════════╗");
            System.out.println("  ║            🧾 FACTURAS              ║");
            System.out.println("  ╚═══════════════════════════════════════╝");
            System.out.println("  1. Listar facturas");
            System.out.println("  2. Ver detalle de factura");
            System.out.println("  0. Volver al menú principal");

            System.out.print("\n  ➤ Seleccione una opción [0-2]: ");
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
                        listarFacturas();
                        break;
                    case 2:
                        verDetalleFactura();
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

    private void listarFacturas() throws IOException {
        limpiarPantalla();
        System.out.println("\n  📋 LISTA DE FACTURAS");
        System.out.println("  ---------------------");

        List<FacturaModelo> facturas = controlador.listar();
        if (facturas.isEmpty()) {
            System.out.println("\n  (No hay facturas registradas)");
        } else {
            for (FacturaModelo f : facturas) {
                System.out.printf("  ID:%-4d | Fecha:%-10s | Cliente:%-20s | Forma:%-8s | Total:%s%n",
                        f.getId(),
                        nulo(f.getFecha()),
                        cortar(f.getNombreCliente(), 20),
                        nulo(f.getFormaPago()),
                        money(f.getTotalNeto()));
            }
        }

        presionarEnter();
    }

    private void verDetalleFactura() throws IOException {
        limpiarPantalla();
        System.out.println("\n  🔍 DETALLE DE FACTURA");
        System.out.println("  ----------------------");

        System.out.print("  ID de factura: ");
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
            FacturaModelo f = controlador.obtenerPorId(id);
            imprimirFacturaDetalle(f);
        } catch (IOException e) {
            System.out.println("\n  ❌ No se pudo obtener la factura.");
            System.out.println("     Detalle: " + e.getMessage());
        }

        presionarEnter();
    }

    private void imprimirFacturaDetalle(FacturaModelo f) {
        System.out.println("  ID Factura     : " + f.getId());
        System.out.println("  Fecha          : " + f.getFecha());
        System.out.println("  Cliente        : " + f.getNombreCliente() + " (" + f.getCedulaCliente() + ")");
        System.out.println("  Forma Pago     : " + f.getFormaPago());
        System.out.println("  Total Bruto    : " + money(f.getTotalBruto()));
        System.out.println("  Descuento      : " + money(f.getDescuento()));
        System.out.println("  Total Neto     : " + money(f.getTotalNeto()));
        if (f.getIdCreditoBanquito() != null) {
            System.out.println("  ID Crédito BanQuito: " + f.getIdCreditoBanquito());
        }

        System.out.println("\n  Detalles:");
        System.out.println("  ------------------------------------------------------------");
        if (f.getDetalles() != null) {
            for (DetalleFacturaModelo d : f.getDetalles()) {
                System.out.printf("  %s - %s | Cant: %d | P.Unit: %s | Subtotal: %s%n",
                        d.getCodigoElectro(),
                        d.getNombreElectro(),
                        d.getCantidad(),
                        money(d.getPrecioUnitario()),
                        money(d.getSubtotal()));
            }
        }
    }

    private String nulo(String s) {
        return (s == null || s.trim().isEmpty()) ? "-" : s;
    }

    private String cortar(String s, int max) {
        if (s == null) return "-";
        if (s.length() <= max) return s;
        return s.substring(0, max - 3) + "...";
    }

    private String money(BigDecimal b) {
        return b == null ? "-" : b.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
    }

    private void limpiarPantalla() {
        for (int i = 0; i < 2; i++) {
            System.out.println();
        }
    }

    private void presionarEnter() {
        System.out.print("\n  Presione ENTER para continuar...");
        scanner.nextLine();
    }
}
