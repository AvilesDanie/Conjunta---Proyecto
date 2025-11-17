package ec.edu.monster.vista;

import ec.edu.monster.controlador.UsuarioController;
import ec.edu.monster.modelo.BanquitoModels.UsuarioResponse;
import ec.edu.monster.util.ColorPalette;
import ec.edu.monster.util.SessionManager;
import ec.edu.monster.util.ToastNotification;

import javax.swing.*;
import java.awt.*;

/**
 * Pantalla de login para BanQuito
 */
public class LoginBanquitoFrame extends JFrame {
    
    private final UsuarioController usuarioController;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginBtn;
    private boolean passwordVisible = false;
    
    public LoginBanquitoFrame() {
        this.usuarioController = new UsuarioController();
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Login - BanQuito");
        setSize(600, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Panel principal con gradiente
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                // Gradiente vertical azul
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(25, 118, 210),
                    0, getHeight(), new Color(66, 165, 245)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout());
        
        // Panel contenedor centrado
        JPanel containerPanel = new JPanel(new GridBagLayout());
        containerPanel.setOpaque(false);
        containerPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(5, 0, 5, 0);
        
        // Botón volver en la parte superior
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 0, 0));
        
        JButton backBtn = new JButton("← Volver") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Dibujar fondo redondeado semi-transparente
                g2d.setColor(new Color(255, 255, 255, 50));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        backBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setBorderPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setPreferredSize(new Dimension(120, 38));
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> {
            new AppSelectionFrame().setVisible(true);
            dispose();
        });
        
        // Efecto hover para el botón volver
        backBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                backBtn.setBackground(new Color(255, 255, 255, 100));
                backBtn.repaint();
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                backBtn.setBackground(new Color(255, 255, 255, 50));
                backBtn.repaint();
            }
        });
        
        topPanel.add(backBtn);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        // Espaciado inicial
        gbc.gridy = 0;
        containerPanel.add(Box.createVerticalStrut(30), gbc);
        
        // Logo sin fondo circular
        JPanel logoPanel = new JPanel();
        logoPanel.setOpaque(false);
        logoPanel.setPreferredSize(new Dimension(180, 180));
        logoPanel.setMaximumSize(new Dimension(180, 180));
        logoPanel.setLayout(new GridBagLayout());
        
        // Cargar imagen del logo
        JLabel logoLabel = new JLabel();
        try {
            java.net.URL imageURL = getClass().getClassLoader().getResource("images/logoBanquito.png");
            if (imageURL != null) {
                ImageIcon icon = new ImageIcon(imageURL);
                Image img = icon.getImage().getScaledInstance(160, 160, Image.SCALE_SMOOTH);
                logoLabel.setIcon(new ImageIcon(img));
                logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
                logoLabel.setVerticalAlignment(SwingConstants.CENTER);
                System.out.println("✓ Logo BanQuito cargado correctamente desde: " + imageURL);
            } else {
                System.err.println("✗ No se encontró la imagen: images/logoBanquito.png");
                // Si no encuentra la imagen, usar emoji
                logoLabel.setText("🏦");
                logoLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 95));
                logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
            }
        } catch (Exception e) {
            System.err.println("✗ Error al cargar logo BanQuito: " + e.getMessage());
            e.printStackTrace();
            // Si falla la carga, usar emoji como fallback
            logoLabel.setText("🏦");
            logoLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 95));
            logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        }
        logoPanel.add(logoLabel);
        
        gbc.gridy++;
        containerPanel.add(logoPanel, gbc);
        
        // Título
        JLabel titleLabel = new JLabel("BanQuito", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 42));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridy++;
        gbc.insets = new Insets(20, 0, 5, 0);
        containerPanel.add(titleLabel, gbc);
        
        // Subtítulo
        JLabel subtitleLabel = new JLabel("Sistema Bancario", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        subtitleLabel.setForeground(new Color(255, 255, 255, 230));
        gbc.gridy++;
        gbc.insets = new Insets(5, 0, 35, 0);
        containerPanel.add(subtitleLabel, gbc);
        
        // Panel del formulario (Card blanco)
        JPanel formPanel = createFormPanel();
        gbc.gridy++;
        gbc.insets = new Insets(5, 0, 20, 0);
        containerPanel.add(formPanel, gbc);
        
        mainPanel.add(containerPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createFormPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 45, 40, 45));
        panel.setPreferredSize(new Dimension(400, 330));
        panel.setMaximumSize(new Dimension(400, 330));
        
        // Campo Usuario
        JLabel userLabel = new JLabel("Usuario");
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        userLabel.setForeground(ColorPalette.TEXTO_PRINCIPAL_NEGRO);
        userLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        usernameField = new JTextField();
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        usernameField.setPreferredSize(new Dimension(310, 42));
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        panel.add(userLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(usernameField);
        panel.add(Box.createVerticalStrut(20));
        
        // Campo Contraseña
        JLabel passLabel = new JLabel("Contraseña");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        passLabel.setForeground(ColorPalette.TEXTO_PRINCIPAL_NEGRO);
        passLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel passwordPanel = new JPanel(new BorderLayout(5, 0));
        passwordPanel.setBackground(Color.WHITE);
        passwordPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        passwordPanel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        passwordField.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 8));
        
        JButton togglePasswordBtn = new JButton("👁");
        togglePasswordBtn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        togglePasswordBtn.setFocusPainted(false);
        togglePasswordBtn.setBorderPainted(false);
        togglePasswordBtn.setContentAreaFilled(false);
        togglePasswordBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        togglePasswordBtn.addActionListener(e -> {
            passwordVisible = !passwordVisible;
            if (passwordVisible) {
                passwordField.setEchoChar((char) 0);
                togglePasswordBtn.setText("🙈");
            } else {
                passwordField.setEchoChar('●');
                togglePasswordBtn.setText("👁");
            }
        });
        
        passwordPanel.add(passwordField, BorderLayout.CENTER);
        passwordPanel.add(togglePasswordBtn, BorderLayout.EAST);
        
        panel.add(passLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(passwordPanel);
        panel.add(Box.createVerticalStrut(25));
        
        // Botón Login
        loginBtn = new JButton("Iniciar Sesión");
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setBackground(new Color(25, 118, 210));
        loginBtn.setFocusPainted(false);
        loginBtn.setBorderPainted(false);
        loginBtn.setPreferredSize(new Dimension(310, 46));
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginBtn.addActionListener(e -> handleLogin());
        
        panel.add(loginBtn);
        
        return panel;
    }
    
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        
        if (username.isEmpty() || password.isEmpty()) {
            ToastNotification.showError(this, "Por favor ingrese usuario y contraseña");
            return;
        }
        
        loginBtn.setEnabled(false);
        loginBtn.setText("Iniciando sesión...");
        
        SwingWorker<UsuarioResponse, Void> worker = new SwingWorker<>() {
            @Override
            protected UsuarioResponse doInBackground() throws Exception {
                return usuarioController.login(username, password);
            }
            
            @Override
            protected void done() {
                try {
                    UsuarioResponse user = get();
                    
                    // Guardar sesión
                    SessionManager.getInstance().login(
                        user.id, user.username, user.rol, "BANQUITO"
                    );
                    
                    ToastNotification.showSuccess(LoginBanquitoFrame.this, 
                        "¡Bienvenido " + user.username + "!");
                    
                    // Esperar un poco para que se vea el toast
                    Timer timer = new Timer(1000, e -> {
                        new HomeBanquitoFrame().setVisible(true);
                        dispose();
                    });
                    timer.setRepeats(false);
                    timer.start();
                    
                } catch (Exception ex) {
                    ToastNotification.showError(LoginBanquitoFrame.this, 
                        "Error al iniciar sesión: " + ex.getMessage());
                } finally {
                    loginBtn.setEnabled(true);
                    loginBtn.setText("Iniciar Sesión");
                }
            }
        };
        worker.execute();
    }
}
