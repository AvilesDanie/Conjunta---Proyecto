package ec.edu.monster.vista;

import ec.edu.monster.controlador.FacturasControlador;
import ec.edu.monster.modelo.FacturaModelo;
import ec.edu.monster.modelo.FacturaProductoRequestModelo;
import ec.edu.monster.modelo.FacturaRequestModelo;
import ec.edu.monster.modelo.DetalleFacturaModelo;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ComprasVistaConsola {

    private final Scanner scanner;
    private final FacturasControlador controlador;

    public ComprasVistaConsola() {
        this.scanner = new Scanner(System.in);
        this.controlador = new FacturasControlador();
    }

    public void mostrarMenu() {
        while (true) {
            limpiarPantalla();

            System.out.println("\n");
            System.out.println("  ╔═══════════════════════════════════════╗");
            System.out.println("  ║            🛒 COMPRAS                ║");
            System.out.println("  ╚═══════════════════════════════════════╝");
            System.out.println("  1. Realizar nueva compra");
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

            switch (opcion) {
                case 1:
                    realizarCompra();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("\n  ❌ Opción inválida.");
                    presionarEnter();
                    break;
            }
        }
    }

    private void realizarCompra() {
        limpiarPantalla();
        System.out.println("\n  🛒 NUEVA COMPRA");
        System.out.println("  ----------------");

        FacturaRequestModelo req = new FacturaRequestModelo();
        List<FacturaProductoRequestModelo> items = new ArrayList<FacturaProductoRequestModelo>();

        System.out.print("  Cédula cliente (10 dígitos): ");
        req.cedulaCliente = scanner.nextLine().trim();

        System.out.print("  Nombre cliente: ");
        req.nombreCliente = scanner.nextLine().trim();

        // Agregar productos
        while (true) {
            System.out.print("\n  ID de electrodoméstico: ");
            String txtId = scanner.nextLine().trim();
            Long idElectro;
            try {
                idElectro = Long.parseLong(txtId);
            } catch (NumberFormatException e) {
                System.out.println("  ❌ ID inválido, intente de nuevo.");
                continue;
            }

            System.out.print("  Cantidad: ");
            String txtCant = scanner.nextLine().trim();
            int cant;
            try {
                cant = Integer.parseInt(txtCant);
                if (cant <= 0) {
                    System.out.println("  ❌ La cantidad debe ser mayor a 0.");
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("  ❌ Cantidad inválida.");
                continue;
            }

            FacturaProductoRequestModelo p = new FacturaProductoRequestModelo();
            p.idElectrodomestico = idElectro;
            p.cantidad = cant;
            items.add(p);

            System.out.print("  ¿Agregar otro producto? (s/N): ");
            String mas = scanner.nextLine().trim().toLowerCase();
            if (!"s".equals(mas)) {
                break;
            }
        }

        if (items.isEmpty()) {
            System.out.println("\n  ❌ No se agregó ningún producto. Compra cancelada.");
            presionarEnter();
            return;
        }

        req.productos = items;

        // Forma de pago
        String forma;
        while (true) {
            System.out.print("\n  Forma de pago (EFECTIVO / CREDITO): ");
            forma = scanner.nextLine().trim().toUpperCase();
            if ("EFECTIVO".equals(forma) || "CREDITO".equals(forma)) {
                break;
            }
            System.out.println("  ❌ Valor inválido. Use EFECTIVO o CREDITO.");
        }
        req.formaPago = forma;

        if ("CREDITO".equals(forma)) {
            // plazoMeses
            while (true) {
                System.out.print("  Plazo en meses: ");
                String txtPlazo = scanner.nextLine().trim();
                try {
                    req.plazoMeses = Integer.parseInt(txtPlazo);
                    if (req.plazoMeses <= 0) {
                        System.out.println("  ❌ El plazo debe ser mayor a 0.");
                        continue;
                    }
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("  ❌ Plazo inválido.");
                }
            }

            System.out.print("  Número de cuenta en BanQuito para el crédito: ");
            req.numCuentaCredito = scanner.nextLine().trim();
        }

        // Llamar al servicio
        try {
            FacturaModelo factura = controlador.crearFactura(req);
            System.out.println("\n  ✅ Compra registrada correctamente. Factura generada:");
            imprimirFacturaDetalle(factura);
        } catch (IOException e) {
            System.out.println("\n  ❌ Error al registrar la compra.");
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
