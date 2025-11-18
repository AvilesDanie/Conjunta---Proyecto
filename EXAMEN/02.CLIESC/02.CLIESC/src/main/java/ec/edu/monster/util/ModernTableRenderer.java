package ec.edu.monster.util;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Renderizador moderno para tablas con estilo web
 */
public class ModernTableRenderer extends DefaultTableCellRenderer {
    
    private final Color alternateColor = new Color(248, 250, 252);
    private final Color hoverColor = new Color(237, 245, 255);
    private final Color selectedColor = new Color(187, 222, 251);
    private final Color selectedTextColor = new Color(13, 60, 108);
    private final Color borderColor = new Color(230, 235, 242);
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        // Aplicar colores modernos
        if (isSelected) {
            c.setBackground(selectedColor);
            c.setForeground(selectedTextColor);
        } else {
            if (row % 2 == 0) {
                c.setBackground(Color.WHITE);
            } else {
                c.setBackground(alternateColor);
            }
            c.setForeground(new Color(33, 33, 33));
        }
        
        // Mejorar padding
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, borderColor),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        
        // Aplicar fuente moderna
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        return c;
    }
}
