package ec.edu.monster.vista;

import ec.edu.monster.util.ColorPalette;

import javax.swing.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Pantalla de selección de aplicación
 * Permite elegir entre BanQuito y Comercializadora
 */
public class AppSelectionFrame extends JFrame {
    
    public AppSelectionFrame() {
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Selección de Aplicación");
        // Configurar pantalla completa y deshabilitar cambio de tamaño
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Panel principal con gradiente
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                // Gradiente vertical multi-color
                int h = getHeight();
                Color[] colors = {
                    ColorPalette.AZUL_PRIMARIO_OSCURO,
                    ColorPalette.AZUL_PRIMARIO,
                    ColorPalette.AZUL_PRIMARIO_MEDIO,
                    ColorPalette.AZUL_MUY_CLARO
                };
                
                for (int i = 0; i < colors.length - 1; i++) {
                    GradientPaint gp = new GradientPaint(
                        0, h * i / (colors.length - 1), colors[i],
                        0, h * (i + 1) / (colors.length - 1), colors[i + 1]
                    );
                    g2d.setPaint(gp);
                    g2d.fillRect(0, h * i / (colors.length - 1), getWidth(), 
                               h / (colors.length - 1) + 1);
                }
            }
        };
        mainPanel.setLayout(new BorderLayout());
        
        // Panel superior
        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0));
        
        // Icono circular (simulado con label)
        JLabel iconLabel = new JLabel("🏦");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        iconLabel.setForeground(Color.WHITE);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Título de bienvenida
        JLabel welcomeLabel = new JLabel("¡Bienvenido!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 42));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Subtítulo
        JLabel subtitleLabel = new JLabel("Sistema Integrado de Gestión");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        subtitleLabel.setForeground(ColorPalette.conAlpha(Color.WHITE, 0.9f));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        topPanel.add(iconLabel);
        topPanel.add(Box.createVerticalStrut(20));
        topPanel.add(welcomeLabel);
        topPanel.add(Box.createVerticalStrut(10));
        topPanel.add(subtitleLabel);
        
        // Panel central con las cards
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new GridBagLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 80, 80, 80));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        
        // Card BanQuito
        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(createAppCard(
            "/images/logoBanquito.png",
            "BanQuito",
            "Sistema Bancario Integral",
            new Color(41, 98, 255),
            new Color(41, 98, 255),
            () -> {
                new LoginBanquitoFrame().setVisible(true);
                dispose();
            }
        ), gbc);
        
        // Card Comercializadora
        gbc.gridx = 0;
        gbc.gridy = 1;
        centerPanel.add(createAppCard(
            "/images/logoPerfilElectroQuito.png",
            "ElectroQuito",
            "Comercializadora Digital",
            new Color(255, 87, 34),
            new Color(255, 87, 34),
            () -> {
                new LoginComercializadoraFrame().setVisible(true);
                dispose();
            }
        ), gbc);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    /**
     * Crea una card de selección de aplicación
     */
    private JPanel createAppCard(String imagePath, String title, String description, 
                                 Color cardColor, Color buttonColor, Runnable onAcceder) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(20, 20));
        card.setBackground(cardColor);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(cardColor.darker(), 2),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        
        // Panel izquierdo con icono
        JPanel leftPanel = new JPanel();
        leftPanel.setOpaque(false);
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        
        JLabel iconLabel = new JLabel();
        try {
            BufferedImage img = ImageIO.read(getClass().getResourceAsStream(imagePath));
            if (img != null) {
                Image scaledImg = img.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                iconLabel.setIcon(new ImageIcon(scaledImg));
            }
        } catch (IOException e) {
            iconLabel.setText("📦");
            iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 56));
        }
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(iconLabel);
        
        // Panel central con textos
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        descLabel.setForeground(ColorPalette.conAlpha(Color.WHITE, 0.9f));
        
        centerPanel.add(titleLabel);
        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.add(descLabel);
        
        // Panel derecho con botón
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setOpaque(false);
        
        JButton accederBtn = new JButton("Acceder →");
        accederBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        accederBtn.setForeground(Color.WHITE);
        accederBtn.setBackground(buttonColor);
        accederBtn.setFocusPainted(false);
        accederBtn.setBorderPainted(false);
        accederBtn.setPreferredSize(new Dimension(140, 45));
        accederBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        accederBtn.addActionListener(e -> onAcceder.run());
        
        // Efecto hover
        accederBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                accederBtn.setBackground(buttonColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                accederBtn.setBackground(buttonColor);
            }
        });
        
        rightPanel.add(accederBtn);
        
        card.add(leftPanel, BorderLayout.WEST);
        card.add(centerPanel, BorderLayout.CENTER);
        card.add(rightPanel, BorderLayout.EAST);
        
        return card;
    }
}
