package ec.edu.monster.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Componente Toast moderno para notificaciones
 * Muestra mensajes elegantes con animación de fade in/out
 */
public class ToastNotification extends JDialog {
    
    // Constantes públicas para compatibilidad
    public static final int SUCCESS = 0;
    public static final int ERROR = 1;
    public static final int INFO = 2;
    public static final int WARNING = 3;
    
    public enum Type {
        SUCCESS, ERROR, INFO, WARNING
    }
    
    private float opacity = 0.0f;
    private Timer fadeInTimer;
    private Timer fadeOutTimer;
    private Timer displayTimer;
    
    private ToastNotification(JFrame parent, String message, Type type, int duration) {
        super(parent, false);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        
        JPanel contentPanel = createContentPanel(message, type);
        setContentPane(contentPanel);
        pack();
        
        // Posicionar en la parte superior central
        if (parent != null) {
            Point parentLocation = parent.getLocationOnScreen();
            Dimension parentSize = parent.getSize();
            Dimension toastSize = getSize();
            setLocation(
                parentLocation.x + (parentSize.width - toastSize.width) / 2,
                parentLocation.y + 80
            );
        } else {
            setLocationRelativeTo(null);
            Point location = getLocation();
            setLocation(location.x, 80);
        }
        
        startAnimation(duration);
    }
    
    private JPanel createContentPanel(String message, Type type) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Sombra
                g2.setColor(new Color(0, 0, 0, 50));
                g2.fillRoundRect(4, 4, getWidth() - 8, getHeight() - 8, 12, 12);
                
                // Fondo con color según tipo
                Color bgColor = getBackgroundColor(type);
                g2.setColor(new Color(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue(), (int)(255 * opacity)));
                g2.fillRoundRect(0, 0, getWidth() - 8, getHeight() - 8, 12, 12);
                
                g2.dispose();
            }
        };
        
        panel.setOpaque(false);
        panel.setLayout(new BorderLayout(15, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        // Icono
        JLabel iconLabel = new JLabel(getIcon(type));
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        iconLabel.setForeground(Color.WHITE);
        panel.add(iconLabel, BorderLayout.WEST);
        
        // Mensaje
        JLabel messageLabel = new JLabel("<html><div style='width:280px;'>" + message + "</div></html>");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageLabel.setForeground(Color.WHITE);
        panel.add(messageLabel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private Color getBackgroundColor(Type type) {
        switch (type) {
            case SUCCESS:
                return new Color(76, 175, 80);  // Verde
            case ERROR:
                return new Color(244, 67, 54);  // Rojo
            case WARNING:
                return new Color(255, 152, 0);  // Naranja
            case INFO:
            default:
                return new Color(33, 150, 243);  // Azul
        }
    }
    
    private String getIcon(Type type) {
        switch (type) {
            case SUCCESS:
                return "✓";
            case ERROR:
                return "✗";
            case WARNING:
                return "⚠";
            case INFO:
            default:
                return "ℹ";
        }
    }
    
    private void startAnimation(int duration) {
        // Fade In
        fadeInTimer = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                opacity += 0.05f;
                if (opacity >= 1.0f) {
                    opacity = 1.0f;
                    fadeInTimer.stop();
                    startDisplayTimer(duration);
                }
                repaint();
            }
        });
        
        setVisible(true);
        fadeInTimer.start();
    }
    
    private void startDisplayTimer(int duration) {
        displayTimer = new Timer(duration, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayTimer.stop();
                startFadeOut();
            }
        });
        displayTimer.setRepeats(false);
        displayTimer.start();
    }
    
    private void startFadeOut() {
        fadeOutTimer = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                opacity -= 0.05f;
                if (opacity <= 0.0f) {
                    opacity = 0.0f;
                    fadeOutTimer.stop();
                    dispose();
                }
                repaint();
            }
        });
        fadeOutTimer.start();
    }
    
    /**
     * Muestra un toast de éxito
     */
    public static void showSuccess(JFrame parent, String message) {
        new ToastNotification(parent, message, Type.SUCCESS, 2500);
    }
    
    /**
     * Muestra un toast de error
     */
    public static void showError(JFrame parent, String message) {
        new ToastNotification(parent, message, Type.ERROR, 3000);
    }
    
    /**
     * Muestra un toast de información
     */
    public static void showInfo(JFrame parent, String message) {
        new ToastNotification(parent, message, Type.INFO, 2500);
    }
    
    /**
     * Muestra un toast de advertencia
     */
    public static void showWarning(JFrame parent, String message) {
        new ToastNotification(parent, message, Type.WARNING, 2500);
    }
    
    /**
     * Método genérico showToast para compatibilidad con código existente
     */
    public static void showToast(JFrame parent, String message, int type) {
        Type toastType;
        switch (type) {
            case SUCCESS:
                toastType = Type.SUCCESS;
                break;
            case ERROR:
                toastType = Type.ERROR;
                break;
            case WARNING:
                toastType = Type.WARNING;
                break;
            case INFO:
            default:
                toastType = Type.INFO;
                break;
        }
        new ToastNotification(parent, message, toastType, type == ERROR ? 3000 : 2500);
    }
}
