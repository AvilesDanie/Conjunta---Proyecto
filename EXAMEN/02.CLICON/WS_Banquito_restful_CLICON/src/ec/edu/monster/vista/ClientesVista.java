package ec.edu.monster.vista;

import ec.edu.monster.controlador.ClienteController;
import ec.edu.monster.modelo.Cliente;
import java.util.List;
import java.util.Scanner;

/**
 * Vista para gestión de clientes
 * @author CLICON
 */
public class ClientesVista {
    private final Scanner scanner;
    private final ClienteController controller;
    
    public ClientesVista() {
        this.scanner = new Scanner(System.in);
        this.controller = new ClienteController();
    }
    
    public void mostrarMenu() {
        while (true) {
            limpiarPantalla();
            
            System.out.println("\n");
            System.out.println("  ╔═══════════════════════════════════════════════════════╗");
            System.out.println("  ║                                                       ║");
            System.out.println("  ║              👥 GESTIÓN DE CLIENTES 👥                ║");
            System.out.println("  ║                                                       ║");
            System.out.println("  ╚═══════════════════════════════════════════════════════╝");
            System.out.println();
            System.out.println("  ┌───────────────────────────────────────────────────────┐");
            System.out.println("  │  1. 📋 Ver todos los clientes                        │");
            System.out.println("  │  2. 🔍 Buscar cliente por cédula                     │");
            System.out.println("  │  3. ➕ Crear nuevo cliente                           │");
            System.out.println("  │  4. ✏️  Actualizar cliente                            │");
            System.out.println("  │  5. 🗑️  Eliminar cliente                              │");
            System.out.println("  ├───────────────────────────────────────────────────────┤");
            System.out.println("  │  6. 🔙 Volver al menú principal                      │");
            System.out.println("  └───────────────────────────────────────────────────────┘");
            
            try {
                System.out.print("\n  ➤ Seleccione una opción [1-6]: ");
                int opcion = Integer.parseInt(scanner.nextLine());
                
                switch (opcion) {
                    case 1:
                        listarClientes();
                        break;
                    case 2:
                        buscarCliente();
                        break;
                    case 3:
                        crearCliente();
                        break;
                    case 4:
                        actualizarCliente();
                        break;
                    case 5:
                        eliminarCliente();
                        break;
                    case 6:
                        return;
                    default:
                        System.out.println("\n  ❌ Opción inválida.");
                        presionarEnter();
                }
                
            } catch (NumberFormatException e) {
                System.out.println("\n  ❌ Por favor ingrese un número válido.");
                presionarEnter();
            }
        }
    }
    
    private void listarClientes() {
        try {
            List<Cliente> clientes = controller.listarClientes();
            
            limpiarPantalla();
            System.out.println("\n  ╔═══════════════════════════════════════════════════════╗");
            System.out.println("  ║              📋 LISTA DE CLIENTES                     ║");
            System.out.println("  ╚═══════════════════════════════════════════════════════╝\n");
            
            if (clientes.isEmpty()) {
                System.out.println("  ⚠️  No hay clientes registrados.\n");
            } else {
                for (Cliente c : clientes) {
                    System.out.println("  ┌───────────────────────────────────────────────────┐");
                    System.out.printf("  │ 🆔 Cédula: %-38s │\n", c.getCedula());
                    System.out.printf("  │ 👤 Nombre: %-38s │\n", c.getNombre());
                    System.out.printf("  │ 📅 F.Nacimiento: %-32s │\n", c.getFechaNacimiento());
                    System.out.printf("  │ 💍 Estado Civil: %-32s │\n", c.getEstadoCivil());
                    if (c.getNumCuentaInicial() != null) {
                        System.out.printf("  │ 🏦 Cuenta Inicial: %-30s │\n", c.getNumCuentaInicial());
                    }
                    System.out.println("  └───────────────────────────────────────────────────┘\n");
                }
                System.out.println("  Total: " + clientes.size() + " cliente(s)");
            }
            
        } catch (Exception e) {
            System.out.println("\n  ❌ Error: " + e.getMessage());
        }
        presionarEnter();
    }
    
    private void buscarCliente() {
        System.out.print("\n  📝 Ingrese la cédula: ");
        String cedula = scanner.nextLine();
        
        try {
            Cliente cliente = controller.obtenerCliente(cedula);
            
            if (cliente != null) {
                System.out.println("\n  ✅ Cliente encontrado:");
                System.out.println("  ┌───────────────────────────────────────────────────┐");
                System.out.printf("  │ 🆔 Cédula: %-38s │\n", cliente.getCedula());
                System.out.printf("  │ 👤 Nombre: %-38s │\n", cliente.getNombre());
                System.out.printf("  │ 📅 F.Nacimiento: %-32s │\n", cliente.getFechaNacimiento());
                System.out.printf("  │ 💍 Estado Civil: %-32s │\n", cliente.getEstadoCivil());
                if (cliente.getNumCuentaInicial() != null) {
                    System.out.printf("  │ 🏦 Cuenta: %-38s │\n", cliente.getNumCuentaInicial());
                    System.out.printf("  │ 💳 Tipo Cuenta: %-33s │\n", cliente.getTipoCuentaInicial());
                }
                System.out.println("  └───────────────────────────────────────────────────┘");
            } else {
                System.out.println("\n  ❌ Cliente no encontrado.");
            }
            
        } catch (Exception e) {
            System.out.println("\n  ❌ Error: " + e.getMessage());
        }
        presionarEnter();
    }
    
    private void crearCliente() {
        System.out.println("\n  ╔═══════════════════════════════════════════════════════╗");
        System.out.println("  ║              ➕ CREAR NUEVO CLIENTE                   ║");
        System.out.println("  ╚═══════════════════════════════════════════════════════╝\n");
        
        Cliente cliente = new Cliente();
        
        System.out.print("  📝 Cédula: ");
        cliente.setCedula(scanner.nextLine());
        
        System.out.print("  👤 Nombre completo: ");
        cliente.setNombre(scanner.nextLine());
        
        System.out.print("  📅 Fecha nacimiento (YYYY-MM-DD): ");
        cliente.setFechaNacimiento(scanner.nextLine());
        
        System.out.print("  💍 Estado civil (SOLTERO/CASADO/DIVORCIADO/VIUDO): ");
        cliente.setEstadoCivil(scanner.nextLine().toUpperCase());
        
        System.out.print("  💳 Tipo cuenta inicial (AHORROS/CORRIENTE): ");
        cliente.setTipoCuentaInicial(scanner.nextLine().toUpperCase());
        
        System.out.print("  💰 Saldo inicial (opcional, Enter para 0): ");
        String saldo = scanner.nextLine();
        cliente.setSaldoInicial(saldo.isEmpty() ? 0.0 : Double.parseDouble(saldo));
        
        try {
            System.out.println("\n  ⏳ Creando cliente...");
            if (controller.crearCliente(cliente)) {
                System.out.println("  ✅ Cliente creado exitosamente!");
            } else {
                System.out.println("  ❌ Error al crear cliente.");
            }
        } catch (Exception e) {
            System.out.println("\n  ❌ Error: " + e.getMessage());
        }
        presionarEnter();
    }
    
    private void actualizarCliente() {
        System.out.print("\n  📝 Ingrese la cédula del cliente a actualizar: ");
        String cedula = scanner.nextLine();
        
        try {
            Cliente cliente = controller.obtenerCliente(cedula);
            if (cliente == null) {
                System.out.println("\n  ❌ Cliente no encontrado.");
                presionarEnter();
                return;
            }
            
            System.out.println("\n  Cliente actual: " + cliente.getNombre());
            System.out.println("  (Presione Enter para mantener el valor actual)\n");
            
            System.out.print("  👤 Nuevo nombre [" + cliente.getNombre() + "]: ");
            String nombre = scanner.nextLine();
            if (!nombre.isEmpty()) cliente.setNombre(nombre);
            
            System.out.print("  📅 Nueva fecha nacimiento [" + cliente.getFechaNacimiento() + "]: ");
            String fecha = scanner.nextLine();
            if (!fecha.isEmpty()) cliente.setFechaNacimiento(fecha);
            
            System.out.print("  💍 Nuevo estado civil [" + cliente.getEstadoCivil() + "]: ");
            String estado = scanner.nextLine();
            if (!estado.isEmpty()) cliente.setEstadoCivil(estado.toUpperCase());
            
            System.out.println("\n  ⏳ Actualizando cliente...");
            if (controller.actualizarCliente(cedula, cliente)) {
                System.out.println("  ✅ Cliente actualizado exitosamente!");
            } else {
                System.out.println("  ❌ Error al actualizar cliente.");
            }
            
        } catch (Exception e) {
            System.out.println("\n  ❌ Error: " + e.getMessage());
        }
        presionarEnter();
    }
    
    private void eliminarCliente() {
        System.out.print("\n  📝 Ingrese la cédula del cliente a eliminar: ");
        String cedula = scanner.nextLine();
        
        System.out.print("  ⚠️  ¿Está seguro? (S/N): ");
        String confirmacion = scanner.nextLine();
        
        if (confirmacion.equalsIgnoreCase("S")) {
            try {
                System.out.println("\n  ⏳ Eliminando cliente...");
                if (controller.eliminarCliente(cedula)) {
                    System.out.println("  ✅ Cliente eliminado exitosamente!");
                } else {
                    System.out.println("  ❌ Error al eliminar cliente.");
                }
            } catch (Exception e) {
                System.out.println("\n  ❌ Error: " + e.getMessage());
            }
        } else {
            System.out.println("\n  ℹ️  Operación cancelada.");
        }
        presionarEnter();
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
