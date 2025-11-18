/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.monster.vista;

import ec.edu.monster.controlador.ConsultarAmortizacionController;
import ec.edu.monster.modelo.Amortizacion;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author ckan1
 */
public class AmortizacionVistaConsola {
    private final Scanner scanner = new Scanner(System.in);
    private final ConsultarAmortizacionController controller = new ConsultarAmortizacionController();

    public void mostrarMenu() {
        System.out.println("\n╔══════════════════════════════════════════╗");
        System.out.println("║     CONSULTAR TABLA DE AMORTIZACIÓN      ║");
        System.out.println("╚══════════════════════════════════════════╝");
        
        System.out.print("➤ Ingrese el ID del crédito: ");
        String inputCredito = scanner.nextLine().trim();

        try {
            Long idCredito = Long.parseLong(inputCredito);
            
            System.out.println("\n⏳ Consultando tabla de amortización...");
            List<Amortizacion> tablaAmortizacion = controller.consultarAmortizacion(idCredito);
            
            if (tablaAmortizacion == null || tablaAmortizacion.isEmpty()) {
                System.out.println("❌ No se encontró información de amortización para este crédito.");
                return;
            }
            
            // Mostrar tabla
            System.out.println("\n┌──────────────────────────────────────────────────────────────────────────────────────┐");
            System.out.println("│                        TABLA DE AMORTIZACIÓN                                         │");
            System.out.println("└──────────────────────────────────────────────────────────────────────────────────────┘");
            System.out.println(String.format("%-8s %-15s %-12s %-12s %-12s %-12s %-12s",
                "CUOTA", "F.VENCIMIENTO", "V.CUOTA", "CAPITAL", "INTERÉS", "SALDO", "ESTADO"));
            System.out.println("────────────────────────────────────────────────────────────────────────────────────────");
            
            double totalCapital = 0;
            double totalInteres = 0;
            double totalCuota = 0;
            
            for (Amortizacion a : tablaAmortizacion) {
                System.out.printf("%-8d %-15s $%-11.2f $%-11.2f $%-11.2f $%-11.2f %-12s\n",
                    a.getNumeroCuota(),
                    a.getFechaVencimiento(),
                    a.getValorCuota(),
                    a.getCapitalPagado(),
                    a.getInteresPagado(),
                    a.getSaldo(),
                    a.getEstado());
                
                totalCapital += a.getCapitalPagado();
                totalInteres += a.getInteresPagado();
                totalCuota += a.getValorCuota();
            }
            
            System.out.println("────────────────────────────────────────────────────────────────────────────────────────");
            System.out.printf("%-24s TOTALES: $%-11.2f $%-11.2f $%-11.2f\n",
                "", totalCuota, totalCapital, totalInteres);
            System.out.println("────────────────────────────────────────────────────────────────────────────────────────");
            System.out.println("Total de cuotas: " + tablaAmortizacion.size());
            
        } catch (NumberFormatException e) {
            System.out.println("❌ El ID del crédito debe ser un número válido.");
        } catch (Exception e) {
            System.out.println("❌ Error al consultar la amortización: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
