package ec.edu.monster.vista;

/**
 * Utilidad para operaciones de consola
 * @author CLICON
 */
public class ConsolaUtil {
    
    /**
     * Limpia la pantalla de la consola según el sistema operativo
     */
    public static void limpiarPantalla() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            // Si falla, imprimir líneas en blanco
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }
    
    /**
     * Pausa para que el usuario presione Enter
     */
    public static void presionarEnter() {
        System.out.print("\n➤ Presione Enter para continuar...");
        try {
            System.in.read();
        } catch (Exception e) {
            // Ignorar
        }
    }
}
