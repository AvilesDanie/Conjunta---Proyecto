package ec.edu.monster.vista;

import ec.edu.monster.util.ColorPalette;
import ec.edu.monster.util.SessionManager;

import javax.swing.*;
import java.awt.*;

/**
 * Menú principal de BanQuito con grid de opciones
 */
public class HomeBanquitoFrame extends JFrame {
    
    public HomeBanquitoFrame() {
        initComponents();
    }
    
    private void initComponents() {
        setTitle("BanQuito - Sistema Bancario");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(ColorPalette.FONDO_CLARO);
        
        // TopBar
        JPanel topBar = createTopBar();
        mainPanel.add(topBar, BorderLayout.NORTH);
        
        // Contenido (Grid de opciones)
        JPanel contentPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        contentPanel.setBackground(ColorPalette.FONDO_CLARO);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Agregar cards de menú
        contentPanel.add(createMenuCard("👥", "Clientes", ColorPalette.AZUL_PRIMARIO, () -> {
            new ClientesFrame().setVisible(true);
            dispose();
        }));
        
        contentPanel.add(createMenuCard("➕", "Nuevo Cliente", ColorPalette.VERDE_EXITO, () -> {
            new CrearClienteFrame().setVisible(true);
            dispose();
        }));
        
        contentPanel.add(createMenuCard("🏦", "Cuentas", ColorPalette.MORADO, () -> {
            new TodasCuentasFrame().setVisible(true);
            dispose();
        }));
        
        contentPanel.add(createMenuCard("💳", "Créditos", ColorPalette.AZUL_PRIMARIO, () -> {
            new CreditosFrame().setVisible(true);
            dispose();
        }));
        
        contentPanel.add(createMenuCard("📋", "Cuotas", ColorPalette.CYAN_OSCURO, () -> {
            mostrarDialogoCuotas();
        }));
        
        contentPanel.add(createMenuCard("👤", "Usuarios", ColorPalette.VERDE_EXITO, () -> {
            new UsuariosFrame().setVisible(true);
            dispose();
        }));
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Footer
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
        footer.setBackground(ColorPalette.AZUL_PRIMARIO);
        footer.setPreferredSize(new Dimension(0, 80));
        
        // Logo de BanQuito en el footer
        JLabel footerLogoLabel = new JLabel();
        try {
            java.net.URL imageURL = getClass().getClassLoader().getResource("images/logoBanquito.png");
            if (imageURL != null) {
                ImageIcon icon = new ImageIcon(imageURL);
                Image img = icon.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH);
                footerLogoLabel.setIcon(new ImageIcon(img));
            }
        } catch (Exception e) {
            // Si falla, no mostrar logo
        }
        
        JLabel footerLabel = new JLabel("BanQuito © 2024");
        footerLabel.setForeground(Color.WHITE);
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        footer.add(footerLogoLabel);
        footer.add(footerLabel);
        mainPanel.add(footer, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(ColorPalette.AZUL_PRIMARIO);
        topBar.setPreferredSize(new Dimension(0, 90));
        topBar.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        // Panel izquierdo con logo de usuario y título
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        leftPanel.setOpaque(false);
        
        // Logo de usuario
        JLabel userIconLabel = new JLabel();
        try {
            java.net.URL imageURL = getClass().getClassLoader().getResource("images/LogoUsuario.png");
            if (imageURL != null) {
                ImageIcon icon = new ImageIcon(imageURL);
                Image img = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                userIconLabel.setIcon(new ImageIcon(img));
            } else {
                userIconLabel.setText("");
                userIconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 35));
            }
        } catch (Exception e) {
            userIconLabel.setText("");
            userIconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 35));
        }
        
        // Panel de texto (título y usuario)
        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        
        JLabel titleLabel = new JLabel("BanQuito");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        
        String username = SessionManager.getInstance().getUsername();
        JLabel userLabel = new JLabel("Bienvenido, " + username);
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userLabel.setForeground(ColorPalette.conAlpha(Color.WHITE, 0.9f));
        
        textPanel.add(titleLabel);
        textPanel.add(userLabel);
        
        leftPanel.add(userIconLabel);
        leftPanel.add(textPanel);
        
        // Lado derecho (botón cerrar sesión)
        final JButton[] logoutBtnRef = new JButton[1];
        JButton logoutBtn = new JButton("Cerrar Sesión") {
            private Color bgColor = ColorPalette.conAlpha(Color.WHITE, 0.2f);
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Dibujar fondo
                g2d.setColor(bgColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                
                // Dibujar texto
                g2d.setColor(getForeground());
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2d.drawString(getText(), x, y);
                
                g2d.dispose();
            }
            
            public void updateBackground(Color color) {
                this.bgColor = color;
                repaint();
            }
        };
        logoutBtnRef[0] = logoutBtn;
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setContentAreaFilled(false);
        logoutBtn.setPreferredSize(new Dimension(130, 40));
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                JButton btn = (JButton)evt.getSource();
                try {
                    btn.getClass().getMethod("updateBackground", Color.class).invoke(btn, ColorPalette.conAlpha(Color.WHITE, 0.35f));
                } catch (Exception e) {}
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                JButton btn = (JButton)evt.getSource();
                try {
                    btn.getClass().getMethod("updateBackground", Color.class).invoke(btn, ColorPalette.conAlpha(Color.WHITE, 0.2f));
                } catch (Exception e) {}
            }
        });
        logoutBtn.addActionListener(e -> {
            SessionManager.getInstance().logout();
            new AppSelectionFrame().setVisible(true);
            dispose();
        });
        
        topBar.add(leftPanel, BorderLayout.WEST);
        topBar.add(logoutBtn, BorderLayout.EAST);
        
        return topBar;
    }
    
    private JPanel createMenuCard(String emoji, String title, Color color, Runnable onClick) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                GradientPaint gp = new GradientPaint(0, 0, color, getWidth(), 0, color.brighter());
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        card.setLayout(new GridBagLayout());
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setPreferredSize(new Dimension(200, 120));
        
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                onClick.run();
            }
        });
        
        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        
        JLabel iconLabel = new JLabel(emoji);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        content.add(iconLabel);
        content.add(Box.createVerticalStrut(8));
        content.add(titleLabel);
        
        card.add(content);
        
        return card;
    }
    
    private void mostrarDialogoCuotas() {
        JTextField idField = new JTextField(10);
        JPanel panel = new JPanel(new GridLayout(2, 1, 5, 5));
        panel.add(new JLabel("ID del Crédito:"));
        panel.add(idField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, 
            "Consultar Cuotas", JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            String idText = idField.getText().trim();
            if (!idText.isEmpty()) {
                try {
                    Long idCredito = Long.parseLong(idText);
                    new CuotasFrame(idCredito).setVisible(true);
                    dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, 
                        "ID inválido", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}
