package ec.edu.monster.vista;

import ec.edu.monster.controlador.ElectrodomesticosControlador;
import ec.edu.monster.modelo.ElectrodomesticoModelo;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class ElectrodomesticoVistaConsola {

    private final Scanner scanner;
    private final ElectrodomesticosControlador controlador;

    public ElectrodomesticoVistaConsola() {
        this.scanner = new Scanner(System.in);
        this.controlador = new ElectrodomesticosControlador();
    }

    public void mostrarMenu() {
        while (true) {
            limpiarPantalla();

            System.out.println("\n");
            System.out.println("  ╔═══════════════════════════════════════╗");
            System.out.println("  ║   📦 GESTIÓN DE ELECTRODOMÉSTICOS    ║");
            System.out.println("  ╚═══════════════════════════════════════╝");
            System.out.println("  1. Listar electrodomésticos");
            System.out.println("  2. Crear electrodoméstico");
            System.out.println("  3. Actualizar electrodoméstico");
            System.out.println("  4. Eliminar electrodoméstico");
            System.out.println("  0. Volver al menú principal");

            System.out.print("\n  ➤ Seleccione una opción [0-4]: ");
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
                        listar();
                        break;
                    case 2:
                        crear();
                        break;
                    case 3:
                        actualizar();
                        break;
                    case 4:
                        eliminar();
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

    // ====== Opciones ======

    private void listar() throws IOException {
        limpiarPantalla();
        System.out.println("\n  📋 LISTA DE ELECTRODOMÉSTICOS");
        System.out.println("  ------------------------------");

        List<ElectrodomesticoModelo> lista = controlador.listar();
        if (lista.isEmpty()) {
            System.out.println("\n  (No hay electrodomésticos activos)");
        } else {
            for (ElectrodomesticoModelo e : lista) {
                imprimirLinea(e);
            }
        }

        presionarEnter();
    }

    private void crear() throws IOException {
        limpiarPantalla();
        System.out.println("\n  ➕ CREAR ELECTRODOMÉSTICO");
        System.out.println("  --------------------------");

        System.out.print("  Código: ");
        String codigo = scanner.nextLine().trim();
        if (codigo.isEmpty()) {
            System.out.println("\n  ❌ El código es obligatorio.");
            presionarEnter();
            return;
        }

        System.out.print("  Nombre: ");
        String nombre = scanner.nextLine().trim();
        if (nombre.isEmpty()) {
            System.out.println("\n  ❌ El nombre es obligatorio.");
            presionarEnter();
            return;
        }

        System.out.print("  Precio de venta: ");
        String txtPrecio = scanner.nextLine().trim();
        BigDecimal precio;
        try {
            precio = new BigDecimal(txtPrecio);
            if (precio.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("\n  ❌ El precio debe ser mayor a 0.");
                presionarEnter();
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("\n  ❌ Precio inválido.");
            presionarEnter();
            return;
        }

        System.out.print("  Ruta de la imagen (ej: C:\\img\\tv.jpg): ");
        String ruta = scanner.nextLine().trim();
        File imagen = new File(ruta);
        if (!imagen.exists() || !imagen.isFile()) {
            System.out.println("\n  ❌ La imagen no existe o no es un archivo válido.");
            presionarEnter();
            return;
        }

        try {
            ElectrodomesticoModelo creado = controlador.crear(codigo, nombre, precio, imagen);
            System.out.println("\n  ✅ Electrodoméstico creado:");
            imprimirDetalle(creado);
        } catch (IOException e) {
            System.out.println("\n  ❌ Error al crear el electrodoméstico.");
            System.out.println("     Detalle: " + e.getMessage());
        }

        presionarEnter();
    }

    private void actualizar() throws IOException {
        limpiarPantalla();
        System.out.println("\n  ✏ ACTUALIZAR ELECTRODOMÉSTICO");
        System.out.println("  ------------------------------");

        System.out.print("  ID del electrodoméstico: ");
        String txtId = scanner.nextLine().trim();
        Long id;
        try {
            id = Long.parseLong(txtId);
        } catch (NumberFormatException e) {
            System.out.println("\n  ❌ ID inválido.");
            presionarEnter();
            return;
        }

        System.out.print("  Nuevo código: ");
        String codigo = scanner.nextLine().trim();
        if (codigo.isEmpty()) {
            System.out.println("\n  ❌ El código es obligatorio.");
            presionarEnter();
            return;
        }

        System.out.print("  Nuevo nombre: ");
        String nombre = scanner.nextLine().trim();
        if (nombre.isEmpty()) {
            System.out.println("\n  ❌ El nombre es obligatorio.");
            presionarEnter();
            return;
        }

        System.out.print("  Nuevo precio de venta: ");
        String txtPrecio = scanner.nextLine().trim();
        BigDecimal precio;
        try {
            precio = new BigDecimal(txtPrecio);
            if (precio.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("\n  ❌ El precio debe ser mayor a 0.");
                presionarEnter();
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("\n  ❌ Precio inválido.");
            presionarEnter();
            return;
        }

        System.out.print("  Nueva ruta de imagen [ENTER para mantener la actual]: ");
        String ruta = scanner.nextLine().trim();
        File imagen = null;
        if (!ruta.isEmpty()) {
            imagen = new File(ruta);
            if (!imagen.exists() || !imagen.isFile()) {
                System.out.println("\n  ❌ La imagen no existe o no es un archivo válido.");
                presionarEnter();
                return;
            }
        }

        try {
            ElectrodomesticoModelo actualizado =
                    controlador.actualizar(id, codigo, nombre, precio, imagen);
            System.out.println("\n  ✅ Electrodoméstico actualizado:");
            imprimirDetalle(actualizado);
        } catch (IOException e) {
            System.out.println("\n  ❌ Error al actualizar el electrodoméstico.");
            System.out.println("     Detalle: " + e.getMessage());
        }

        presionarEnter();
    }

    private void eliminar() throws IOException {
        limpiarPantalla();
        System.out.println("\n  🗑 ELIMINAR ELECTRODOMÉSTICO");
        System.out.println("  -----------------------------");

        System.out.print("  ID del electrodoméstico: ");
        String txtId = scanner.nextLine().trim();
        Long id;
        try {
            id = Long.parseLong(txtId);
        } catch (NumberFormatException e) {
            System.out.println("\n  ❌ ID inválido.");
            presionarEnter();
            return;
        }

        System.out.print("\n  ⚠ ¿Seguro que desea eliminarlo (baja lógica)? (s/N): ");
        String conf = scanner.nextLine().trim().toLowerCase();
        if (!"s".equals(conf)) {
            System.out.println("\n  Operación cancelada.");
            presionarEnter();
            return;
        }

        try {
            controlador.eliminar(id);
            System.out.println("\n  ✅ Electrodoméstico eliminado (activo = false).");
        } catch (IOException e) {
            System.out.println("\n  ❌ Error al eliminar el electrodoméstico.");
            System.out.println("     Detalle: " + e.getMessage());
        }

        presionarEnter();
    }

    // ===== Helpers =====

    private void imprimirLinea(ElectrodomesticoModelo e) {
        System.out.printf("  ID:%-4d | Cod:%-10s | Nombre:%-25s | Precio:%-10s%n",
                e.getId(),
                e.getCodigo(),
                e.getNombre(),
                e.getPrecioVenta() == null ? "-" : e.getPrecioVenta().toPlainString());
    }

    private void imprimirDetalle(ElectrodomesticoModelo e) {
        System.out.println("  ID           : " + e.getId());
        System.out.println("  Código       : " + e.getCodigo());
        System.out.println("  Nombre       : " + e.getNombre());
        System.out.println("  Precio Venta : " +
                (e.getPrecioVenta() == null ? "-" : e.getPrecioVenta().toPlainString()));
        System.out.println("  Imagen URL   : " + (e.getImagenUrl() == null ? "-" : e.getImagenUrl()));
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
