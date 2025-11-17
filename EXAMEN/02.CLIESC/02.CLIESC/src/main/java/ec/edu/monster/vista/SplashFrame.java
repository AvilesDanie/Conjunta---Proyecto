package ec.edu.monster.vista;

import ec.edu.monster.util.ColorPalette;

import javax.swing.*;
import java.awt.*;

/**
 * Pantalla de inicio (Splash Screen)
 * Se muestra por 2 segundos antes de ir a AppSelectionFrame
 */
public class SplashFrame extends JFrame {
    
    public SplashFrame() {
        initComponents();
        
        // Después de 2 segundos, mostrar AppSelectionFrame
        Timer timer = new Timer(2000, e -> {
            new AppSelectionFrame().setVisible(true);
            dispose();
        });
        timer.setRepeats(false);
        timer.start();
    }
    
    private void initComponents() {
        setTitle("BanQuito & ElectroQuito");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);
        
        // Panel principal con gradiente
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                // Gradiente azul
                GradientPaint gradient = new GradientPaint(
                    0, 0, ColorPalette.AZUL_PRIMARIO,
                    0, getHeight(), ColorPalette.AZUL_PRIMARIO_MEDIO
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new GridBagLayout());
        
        // Panel de contenido
        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        
        // Título
        JLabel titleLabel = new JLabel("BanQuito & ElectroQuito");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Espaciador
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(30));
        
        // Indicador de carga
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setMaximumSize(new Dimension(300, 10));
        progressBar.setPreferredSize(new Dimension(300, 10));
        progressBar.setForeground(Color.WHITE);
        progressBar.setBackground(ColorPalette.conAlpha(Color.WHITE, 0.3f));
        progressBar.setBorderPainted(false);
        contentPanel.add(progressBar);
        
        mainPanel.add(contentPanel);
        add(mainPanel);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new SplashFrame().setVisible(true);
        });
    }
}
