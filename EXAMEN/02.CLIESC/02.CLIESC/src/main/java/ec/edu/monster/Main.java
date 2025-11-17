/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ec.edu.monster;

import ec.edu.monster.vista.SplashFrame;

import javax.swing.*;

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
        // Configurar Look and Feel del sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Lanzar la aplicación en el Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            SplashFrame splash = new SplashFrame();
            splash.setVisible(true);
        });
    }
}
