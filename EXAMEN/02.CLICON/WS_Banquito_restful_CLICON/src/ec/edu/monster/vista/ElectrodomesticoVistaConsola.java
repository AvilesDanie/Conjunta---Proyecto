package ec.edu.monster.vista;

import ec.edu.monster.controlador.ElectrodomesticoController;
import ec.edu.monster.modelo.Electrodomestico;
import java.util.List;
import java.util.Scanner;

/**
 * Vista de consola para gestión de electrodomésticos
 * @author CLICON
 */
public class ElectrodomesticoVistaConsola {
    private final Scanner scanner = new Scanner(System.in);
    private final ElectrodomesticoController controller = new ElectrodomesticoController();

    public void mostrarMenu() {
        while (true) {
            System.out.println("\n╔══════════════════════════════════════════╗");
            System.out.println("║   GESTIÓN DE ELECTRODOMÉSTICOS           ║");
            System.out.println("╚══════════════════════════════════════════╝");
            System.out.println("1. Ver lista de electrodomésticos");
            System.out.println("2. Agregar nuevo electrodoméstico");
            System.out.println("3. Volver al menú principal");
            System.out.print("➤ Seleccione una opción: ");

            int opcion;
            try {
                opcion = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("❌ Ingrese un número válido.");
                continue;
            }

            switch (opcion) {
                case 1:
                    listarElectrodomesticos();
                    break;
                case 2:
                    agregarElectrodomestico();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("❌ Opción inválida, intente nuevamente.");
            }
        }
    }

    private void listarElectrodomesticos() {
        try {
            List<Electrodomestico> electrodomesticos = controller.obtenerElectrodomesticos();
            
            if (electrodomesticos == null || electrodomesticos.isEmpty()) {
                System.out.println("❌ No hay electrodomésticos registrados.");
                return;
            }
            
            System.out.println("\n┌────────────────────────────────────────────────────────┐");
            System.out.println("│         LISTA DE ELECTRODOMÉSTICOS                     │");
            System.out.println("└────────────────────────────────────────────────────────┘");
            System.out.println(String.format("%-5s %-35s %-15s", "ID", "NOMBRE", "PRECIO"));
            System.out.println("──────────────────────────────────────────────────────────");
            
            for (Electrodomestico electro : electrodomesticos) {
                System.out.printf("%-5d %-35s $%-14.2f\n",
                    electro.getId(), 
                    electro.getNombre(), 
                    electro.getPrecio());
            }
            System.out.println("──────────────────────────────────────────────────────────");
            System.out.println("Total: " + electrodomesticos.size() + " electrodomésticos");
            
        } catch (Exception e) {
            System.out.println("❌ Error al obtener los electrodomésticos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void agregarElectrodomestico() {
        System.out.println("\n╔══════════════════════════════════════════╗");
        System.out.println("║    AGREGAR NUEVO ELECTRODOMÉSTICO        ║");
        System.out.println("╚══════════════════════════════════════════╝");
        
        System.out.print("➤ Nombre: ");
        String nombre = scanner.nextLine().trim();
        
        if (nombre.isEmpty()) {
            System.out.println("❌ El nombre no puede estar vacío.");
            return;
        }
        
        System.out.print("➤ Precio: $");
        double precio;
        try {
            precio = Double.parseDouble(scanner.nextLine());
            if (precio <= 0) {
                System.out.println("❌ El precio debe ser mayor a 0.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ El precio debe ser un número válido.");
            return;
        }

        System.out.print("➤ URL de imagen (opcional): ");
        String imagenUrl = scanner.nextLine().trim();

        Electrodomestico nuevoElectro = new Electrodomestico();
        nuevoElectro.setNombre(nombre);
        nuevoElectro.setPrecio(precio);
        nuevoElectro.setImagenUrl(imagenUrl.isEmpty() ? null : imagenUrl);

        try {
            System.out.println("\n⏳ Guardando electrodoméstico...");
            boolean creado = controller.crearElectrodomestico(nuevoElectro);
            
            if (creado) {
                System.out.println("✅ Electrodoméstico agregado exitosamente.");
            } else {
                System.out.println("❌ Error al agregar el electrodoméstico.");
            }
        } catch (Exception e) {
            System.out.println("❌ Error al conectar con el servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
