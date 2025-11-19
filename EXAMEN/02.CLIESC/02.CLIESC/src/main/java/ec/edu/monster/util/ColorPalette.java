package ec.edu.monster.util;

import java.awt.Color;

/**
 * Paleta de colores completa de la aplicación
 * Basada en la documentación de la app móvil
 */
public class ColorPalette {
    
    // ========== COLORES PRINCIPALES ==========
    public static final Color AZUL_PRIMARIO_OSCURO = new Color(13, 71, 161);     // #0D47A1
    public static final Color AZUL_PRIMARIO = new Color(21, 101, 192);           // #1565C0
    public static final Color AZUL_PRIMARIO_MEDIO = new Color(25, 118, 210);     // #1976D2
    public static final Color AZUL_CLARO = new Color(66, 165, 245);              // #42A5F5
    public static final Color AZUL_MUY_CLARO = new Color(227, 242, 253);         // #E3F2FD
    
    // ========== COLORES VERDE ==========
    public static final Color VERDE_EXITO = new Color(46, 125, 50);              // #2E7D32
    public static final Color VERDE_BRILLANTE_SALDO = new Color(102, 187, 106);  // #66BB6A
    public static final Color VERDE_SUAVE_FONDO = new Color(232, 245, 233);      // #E8F5E9
    
    // ========== COLORES ROJO ==========
    public static final Color ROJO_ERROR = new Color(239, 83, 80);               // #EF5350
    public static final Color ROJO_ICONO_ERROR = new Color(255, 107, 107);       // #FF6B6B
    
    // ========== COLORES NARANJA (ElectroQuito) ==========
    public static final Color NARANJA_ELECTRO = new Color(255, 138, 101);        // #FF8A65 - Naranja más claro y suave
    
    // ========== COLORES MORADO Y CYAN ==========
    public static final Color MORADO = new Color(106, 27, 154);                  // #6A1B9A
    public static final Color CYAN_OSCURO = new Color(0, 131, 143);              // #00838F
    
    // ========== COLORES DE TEXTO ==========
    public static final Color TEXTO_PRINCIPAL_NEGRO = new Color(33, 33, 33);     // #212121
    public static final Color TEXTO_GRIS_MEDIO = new Color(117, 117, 117);       // #757575
    public static final Color TEXTO_SECUNDARIO_GRIS = new Color(117, 117, 117);  // #757575
    public static final Color TEXTO_BLANCO = Color.WHITE;                        // #FFFFFF
    public static final Color AMARILLO_ADVERTENCIA = new Color(255, 193, 7);     // #FFC107
    
    // ========== COLORES DE FONDO ==========
    public static final Color BLANCO_PURO = Color.WHITE;                         // #FFFFFF
    public static final Color FONDO_CLARO = new Color(245, 245, 245);            // #F5F5F5
    public static final Color GRIS_BORDES = new Color(224, 224, 224);            // #E0E0E0
    public static final Color FONDO_DISABLED = new Color(245, 245, 245);         // #F5F5F5
    
    // ========== COLORES DE GRADIENTES ==========
    public static final Color VIOLETA_GRADIENTE_1 = new Color(26, 35, 126);      // #1A237E
    public static final Color VIOLETA_GRADIENTE_2 = new Color(40, 53, 147);      // #283593
    public static final Color VIOLETA_GRADIENTE_3 = new Color(48, 63, 159);      // #303F9F
    
    // ========== MÉTODOS AUXILIARES ==========
    
    /**
     * Crea un color con transparencia
     */
    public static Color conAlpha(Color color, float alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), 
                        (int)(alpha * 255));
    }
    
    /**
     * Obtiene el color de saldo según el valor
     */
    public static Color getColorSaldo(double saldo) {
        return saldo >= 0 ? VERDE_BRILLANTE_SALDO : ROJO_ERROR;
    }
    
    /**
     * Obtiene el color de estado de cuota
     */
    public static Color getColorEstadoCuota(String estado) {
        switch (estado.toUpperCase()) {
            case "PENDIENTE": return new Color(255, 152, 0);  // Naranja
            case "PAGADA": return VERDE_EXITO;
            case "VENCIDA": return ROJO_ERROR;
            case "ANULADA": return new Color(158, 158, 158);  // Gris
            default: return TEXTO_GRIS_MEDIO;
        }
    }
}
