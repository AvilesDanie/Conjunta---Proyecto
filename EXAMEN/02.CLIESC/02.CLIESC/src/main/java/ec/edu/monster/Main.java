/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ec.edu.monster;

import ec.edu.monster.vista.SplashFrame;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLaf;

import javax.swing.*;
import java.awt.*;

/**
 * Clase principal del sistema BanQuito & ElectroQuito
 * Aplicación de escritorio Java
 * @author DETPC
 */
public class Main {

    /**
     * Punto de entrada de la aplicación
     * @param args argumentos de línea de comandos
     */
    public static void main(String[] args) {
        // Configurar FlatLaf Look and Feel moderno
        try {
            // Configurar propiedades personalizadas de FlatLaf
            FlatLaf.registerCustomDefaultsSource("ec.edu.monster.themes");
            
            // Personalizar colores globales
            UIManager.put("Button.arc", 8);
            UIManager.put("Component.arc", 8);
            UIManager.put("TextComponent.arc", 8);
            UIManager.put("ScrollBar.trackArc", 999);
            UIManager.put("ScrollBar.thumbArc", 999);
            UIManager.put("ScrollBar.trackInsets", new Insets(2, 4, 2, 4));
            UIManager.put("ScrollBar.thumbInsets", new Insets(2, 2, 2, 2));
            UIManager.put("ScrollBar.track", new Color(245, 245, 245));
            
            // Configurar tabla
            UIManager.put("Table.showHorizontalLines", true);
            UIManager.put("Table.showVerticalLines", true);
            UIManager.put("Table.intercellSpacing", new Dimension(1, 1));
            UIManager.put("Table.rowHeight", 55);
            UIManager.put("Table.selectionBackground", new Color(187, 222, 251));
            UIManager.put("Table.selectionForeground", new Color(13, 60, 108));
            UIManager.put("Table.alternateRowColor", new Color(248, 250, 252));
            
            // Aplicar tema FlatIntelliJ (estilo moderno)
            FlatIntelliJLaf.setup();
            
        } catch (Exception e) {
            e.printStackTrace();
            // Fallback al Look and Feel del sistema
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        
        // Lanzar la aplicación en el Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            SplashFrame splash = new SplashFrame();
            splash.setVisible(true);
        });
    }
}
