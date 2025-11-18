package ec.edu.monster.vista;

import ec.edu.monster.controlador.ElectrodomesticoController;
import ec.edu.monster.controlador.ComprarController;
import ec.edu.monster.controlador.FacturaController;
import ec.edu.monster.modelo.Electrodomestico;
import ec.edu.monster.modelo.ElectrodomesticoSeleccionado;
import ec.edu.monster.modelo.Factura;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Vista de consola para realizar compras de electrodomésticos
 * @author CLICON
 */
public class ComprasVistaConsola {
    private final Scanner scanner = new Scanner(System.in);
    private final ElectrodomesticoController electroController = new ElectrodomesticoController();
    private final ComprarController comprarController = new ComprarController();
    private final FacturaController facturaController = new FacturaController();
    private final List<ElectrodomesticoSeleccionado> carrito = new ArrayList<>();

    public void mostrarMenu() {
        // Limpiar carrito al inicio
        carrito.clear();
        
        ConsolaUtil.limpiarPantalla();
        System.out.println("\n╔══════════════════════════════════════════╗");
        System.out.println("║     COMPRAR ELECTRODOMÉSTICOS            ║");
        System.out.println("╚══════════════════════════════════════════╝");
        
        try {
            // Obtener electrodomésticos disponibles
            List<Electrodomestico> electrodomesticosDisponibles = electroController.obtenerElectrodomesticos();
            
            if (electrodomesticosDisponibles == null || electrodomesticosDisponibles.isEmpty()) {
                System.out.println("❌ No hay electrodomésticos disponibles para la compra.");
                return;
            }
            
            // Selección de productos
            boolean continuarComprando = true;
            while (continuarComprando) {
                ConsolaUtil.limpiarPantalla();
                System.out.println("\n╔══════════════════════════════════════════╗");
                System.out.println("║     🛒 CARRITO DE COMPRAS                ║");
                System.out.println("╚══════════════════════════════════════════╝");
                
                // Mostrar carrito actual
                if (!carrito.isEmpty()) {
                    System.out.println("\n📦 Productos en el carrito:");
                    for (int i = 0; i < carrito.size(); i++) {
                        ElectrodomesticoSeleccionado item = carrito.get(i);
                        Electrodomestico electro = electrodomesticosDisponibles.stream()
                            .filter(e -> e.getId().equals(item.getIdElectrodomestico()))
                            .findFirst()
                            .orElse(null);
                        if (electro != null) {
                            System.out.printf("  %d. %s x%d - $%.2f\n", 
                                i + 1,
                                electro.getNombre(), 
                                item.getCantidad(),
                                electro.getPrecio() * item.getCantidad());
                        }
                    }
                    System.out.println("─────────────────────────────────────────");
                }
                
                System.out.println("\n┌─────────────────────────────────────────┐");
                System.out.println("│    ELECTRODOMÉSTICOS DISPONIBLES        │");
                System.out.println("└─────────────────────────────────────────┘");
                
                for (int i = 0; i < electrodomesticosDisponibles.size(); i++) {
                    Electrodomestico electro = electrodomesticosDisponibles.get(i);
                    System.out.printf("%d. %-30s $%.2f\n", 
                        i + 1, 
                        electro.getNombre(), 
                        electro.getPrecio());
                }
                System.out.println("\n0. ✅ FINALIZAR Y PROCEDER AL PAGO");
                
                System.out.print("\n➤ Seleccione un electrodoméstico (0 para finalizar): ");
                int seleccion;
                try {
                    seleccion = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("❌ Debe ingresar un número válido.");
                    ConsolaUtil.presionarEnter();
                    continue;
                }
                
                if (seleccion == 0) {
                    if (carrito.isEmpty()) {
                        System.out.println("⚠️  El carrito está vacío. Agregue al menos un producto.");
                        ConsolaUtil.presionarEnter();
                        continue;
                    }
                    continuarComprando = false;
                    continue;
                }
                
                if (seleccion < 1 || seleccion > electrodomesticosDisponibles.size()) {
                    System.out.println("❌ Selección inválida.");
                    ConsolaUtil.presionarEnter();
                    continue;
                }
                
                Electrodomestico electroSeleccionado = electrodomesticosDisponibles.get(seleccion - 1);
                
                System.out.print("➤ Ingrese la cantidad: ");
                int cantidad;
                try {
                    cantidad = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("❌ Debe ingresar un número válido.");
                    ConsolaUtil.presionarEnter();
                    continue;
                }
                
                if (cantidad <= 0) {
                    System.out.println("❌ La cantidad debe ser mayor a 0.");
                    ConsolaUtil.presionarEnter();
                    continue;
                }
                
                // Agregar al carrito
                ElectrodomesticoSeleccionado item = new ElectrodomesticoSeleccionado();
                item.setIdElectrodomestico(electroSeleccionado.getId());
                item.setCantidad(cantidad);
                carrito.add(item);
                
                System.out.println("✅ Producto agregado al carrito.");
                System.out.println("ℹ️  Presione Enter para continuar comprando o seleccione 0 para finalizar...");
                scanner.nextLine();
            }
            
            if (carrito.isEmpty()) {
                System.out.println("❌ No se seleccionó ningún electrodoméstico.");
                return;
            }
            
            // Calcular total
            double totalCompra = 0;
            System.out.println("\n┌─────────────────────────────────────────┐");
            System.out.println("│         RESUMEN DEL CARRITO             │");
            System.out.println("└─────────────────────────────────────────┘");
            
            for (ElectrodomesticoSeleccionado item : carrito) {
                Electrodomestico electro = electrodomesticosDisponibles.stream()
                    .filter(e -> e.getId().equals(item.getIdElectrodomestico()))
                    .findFirst()
                    .orElse(null);
                
                if (electro != null) {
                    double subtotal = electro.getPrecio() * item.getCantidad();
                    totalCompra += subtotal;
                    System.out.printf("• %s x%d = $%.2f\n", 
                        electro.getNombre(), 
                        item.getCantidad(), 
                        subtotal);
                }
            }
            
            System.out.println("─────────────────────────────────────────");
            System.out.printf("TOTAL: $%.2f\n", totalCompra);
            System.out.println("─────────────────────────────────────────");
            
            // Selección de método de pago
            System.out.println("\n┌─────────────────────────────────────────┐");
            System.out.println("│        MÉTODO DE PAGO                   │");
            System.out.println("└─────────────────────────────────────────┘");
            System.out.println("1. Crédito");
            System.out.println("2. Efectivo (33% de descuento)");
            System.out.print("➤ Opción: ");
            int metodoPago = Integer.parseInt(scanner.nextLine());
            
            if (metodoPago == 1) {
                // Compra a crédito
                procesarCompraCredito(totalCompra);
            } else if (metodoPago == 2) {
                // Compra en efectivo
                procesarCompraEfectivo(totalCompra);
            } else {
                System.out.println("❌ Método de pago inválido.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("❌ Error: Ingrese un número válido.");
        } catch (Exception e) {
            System.out.println("❌ Error al procesar la compra: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void procesarCompraCredito(double totalCompra) {
        try {
            System.out.println("\n╔══════════════════════════════════════════╗");
            System.out.println("║        💳 COMPRA A CRÉDITO               ║");
            System.out.println("╚══════════════════════════════════════════╝");
            
            System.out.print("\n➤ Ingrese su número de cédula: ");
            String cedula = scanner.nextLine().trim();

            if (cedula.isEmpty()) {
                System.out.println("❌ La cédula no puede estar vacía.");
                ConsolaUtil.presionarEnter();
                return;
            }
            
            // Obtener datos del cliente
            System.out.print("➤ Ingrese su nombre completo: ");
            String nombreCliente = scanner.nextLine();
            
            if (nombreCliente.isEmpty()) {
                System.out.println("❌ El nombre no puede estar vacío.");
                ConsolaUtil.presionarEnter();
                return;
            }
            
            System.out.print("➤ Ingrese el plazo en meses (3-24): ");
            int plazoMeses;
            try {
                plazoMeses = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("❌ Debe ingresar un número válido.");
                ConsolaUtil.presionarEnter();
                return;
            }
            
            if (plazoMeses < 3 || plazoMeses > 24) {
                System.out.println("❌ El plazo debe estar entre 3 y 24 meses.");
                ConsolaUtil.presionarEnter();
                return;
            }
            
            System.out.print("➤ Ingrese el número de cuenta para el crédito: ");
            String numCuentaCredito = scanner.nextLine().trim();
            
            if (numCuentaCredito.isEmpty()) {
                System.out.println("❌ El número de cuenta no puede estar vacío.");
                ConsolaUtil.presionarEnter();
                return;
            }

            // PASO 1: EVALUAR si es sujeto de crédito (solo consulta)
            System.out.println("\n╔══════════════════════════════════════════╗");
            System.out.println("║   ⏳ EVALUANDO SOLICITUD DE CRÉDITO...   ║");
            System.out.println("╚══════════════════════════════════════════╝");
            System.out.println("   💰 Monto: $" + String.format("%.2f", totalCompra));
            System.out.println("   📅 Plazo: " + plazoMeses + " meses");
            System.out.println("   🏦 Cuenta: " + numCuentaCredito);
            System.out.println("");
            
            boolean aprobado = comprarController.esSujetoCredito(cedula, totalCompra, plazoMeses, numCuentaCredito);
            
            if (!aprobado) {
                System.out.println("\n╔══════════════════════════════════════════╗");
                System.out.println("║   ❌ CRÉDITO RECHAZADO                   ║");
                System.out.println("╚══════════════════════════════════════════╝");
                String razon = comprarController.obtenerRazonRechazo(cedula, totalCompra, plazoMeses, numCuentaCredito);
                System.out.println("\n📋 Razón del rechazo:");
                System.out.println("   " + razon);
                System.out.println("\n💡 Sugerencias:");
                System.out.println("   • Verifique que tenga una cuenta activa");
                System.out.println("   • Asegúrese de tener movimientos recientes");
                System.out.println("   • Intente con un monto menor o plazo diferente");
                ConsolaUtil.presionarEnter();
                return;
            }

            // PASO 2: CRÉDITO APROBADO - Mostrar resultado y confirmar
            System.out.println("\n╔══════════════════════════════════════════╗");
            System.out.println("║   ✅ CRÉDITO APROBADO                    ║");
            System.out.println("╚══════════════════════════════════════════╝");
            System.out.println("\n📊 Detalles del crédito:");
            System.out.println("   💰 Monto total: $" + String.format("%.2f", totalCompra));
            System.out.println("   📅 Plazo: " + plazoMeses + " meses");
            System.out.println("   📈 Tasa de interés: 16% anual");
            
            // Calcular cuota aproximada
            double tasaMensual = 0.16 / 12;
            double cuotaMensual = totalCompra * (tasaMensual * Math.pow(1 + tasaMensual, plazoMeses)) / 
                                  (Math.pow(1 + tasaMensual, plazoMeses) - 1);
            System.out.println("   💳 Cuota mensual aproximada: $" + String.format("%.2f", cuotaMensual));
            System.out.println("   💵 Total a pagar: $" + String.format("%.2f", cuotaMensual * plazoMeses));
            
            System.out.println("\n─────────────────────────────────────────");
            System.out.print("¿Desea aprobar y crear el crédito? (S/N): ");
            String confirmacion = scanner.nextLine().trim().toUpperCase();
            
            if (!confirmacion.equals("S")) {
                System.out.println("\n⚠️  Operación cancelada por el usuario.");
                ConsolaUtil.presionarEnter();
                return;
            }
            
            // PASO 3: CREAR EL CRÉDITO Y LA FACTURA
            System.out.println("\n⏳ Procesando crédito y generando factura...");
            
            // Crear factura - Por ahora tomar el primer electrodoméstico del carrito
            // En una versión mejorada se debería enviar todos los items
            ElectrodomesticoSeleccionado primerItem = carrito.get(0);
            
            Factura factura = facturaController.crearFactura(
                cedula, 
                nombreCliente, 
                primerItem.getIdElectrodomestico(), 
                primerItem.getCantidad(), 
                "CREDITO", 
                plazoMeses, 
                numCuentaCredito
            );
            
            if (factura != null) {
                System.out.println("\n╔══════════════════════════════════════════╗");
                System.out.println("║   ✅ COMPRA REALIZADA CON ÉXITO          ║");
                System.out.println("╚══════════════════════════════════════════╝");
                System.out.println("Factura ID: " + factura.getId());
                System.out.println("Crédito ID: " + factura.getIdCredito());
                System.out.println("Total: $" + factura.getTotal());
                System.out.println("Plazo: " + plazoMeses + " meses");
                carrito.clear();
            } else {
                System.out.println("❌ Error al procesar la compra a crédito.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("❌ Error: Ingrese un número válido.");
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void procesarCompraEfectivo(double totalCompra) {
        try {
            double descuento = totalCompra * 0.33;
            double totalConDescuento = totalCompra - descuento;
            
            System.out.println("\n┌─────────────────────────────────────────┐");
            System.out.println("│      COMPRA EN EFECTIVO                 │");
            System.out.println("└─────────────────────────────────────────┘");
            System.out.printf("Subtotal:   $%.2f\n", totalCompra);
            System.out.printf("Descuento:  -$%.2f (33%%)\n", descuento);
            System.out.println("─────────────────────────────────────────");
            System.out.printf("TOTAL:      $%.2f\n", totalConDescuento);
            System.out.println("─────────────────────────────────────────");
            
            System.out.print("\n➤ Confirmar compra (S/N): ");
            String confirmacion = scanner.nextLine().trim().toUpperCase();
            
            if (confirmacion.equals("S")) {
                // Obtener datos del cliente
                System.out.print("\n➤ Ingrese su cédula: ");
                String cedulaCliente = scanner.nextLine();
                System.out.print("➤ Ingrese su nombre: ");
                String nombreCliente = scanner.nextLine();
                
                // Crear factura sin crédito - tomar el primer electrodoméstico
                ElectrodomesticoSeleccionado primerItem = carrito.get(0);
                
                System.out.println("\n⏳ Procesando compra en efectivo...");
                Factura factura = facturaController.crearFactura(
                    cedulaCliente,
                    nombreCliente,
                    primerItem.getIdElectrodomestico(),
                    primerItem.getCantidad(),
                    "EFECTIVO",
                    null,
                    null
                );
                
                if (factura != null) {
                    System.out.println("\n╔══════════════════════════════════════════╗");
                    System.out.println("║   ✅ COMPRA REALIZADA CON ÉXITO          ║");
                    System.out.println("╚══════════════════════════════════════════╝");
                    System.out.println("Factura ID: " + factura.getId());
                    System.out.printf("Total pagado: $%.2f\n", totalConDescuento);
                    carrito.clear();
                } else {
                    System.out.println("❌ Error al procesar la compra en efectivo.");
                }
            } else {
                System.out.println("❌ Compra cancelada.");
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
