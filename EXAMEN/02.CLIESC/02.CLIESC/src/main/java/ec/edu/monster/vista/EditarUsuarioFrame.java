package ec.edu.monster.vista;

import ec.edu.monster.controlador.UsuarioController;
import ec.edu.monster.modelo.BanquitoModels.*;
import ec.edu.monster.util.ColorPalette;

import javax.swing.*;
import java.awt.*;

/**
 * Pantalla para editar usuario existente
 */
public class EditarUsuarioFrame extends JFrame {
    
    private final UsuarioController usuarioController;
    private final int id;
    private JTextField nombreUsuarioField, passwordField;
    private JComboBox<String> rolCombo;
    private JCheckBox activoCheck;
    private JButton actualizarBtn;
    private UsuarioResponse usuarioActual;
    
    public EditarUsuarioFrame(int id) {
        this.usuarioController = new UsuarioController();
        this.id = id;
        initComponents();
        cargarUsuario();
    }
    
    private void initComponents() {
        setTitle("Editar Usuario - BanQuito");
        setSize(700, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(ColorPalette.FONDO_CLARO);
        
        // TopBar
        JPanel topBar = createTopBar();
        mainPanel.add(topBar, BorderLayout.NORTH);
        
        add(mainPanel);
    }
    
    private JPanel createTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(ColorPalette.AZUL_PRIMARIO_MEDIO);
        topBar.setPreferredSize(new Dimension(0, 60));
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        JButton backBtn = new JButton("← Volver") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 50));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setBorderPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> {
            new UsuariosFrame().setVisible(true);
            dispose();
        });
        
        JLabel titleLabel = new JLabel("Editar Usuario");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        topBar.add(backBtn, BorderLayout.WEST);
        topBar.add(titleLabel, BorderLayout.CENTER);
        
        return topBar;
    }
    
    private void cargarUsuario() {
        SwingWorker<UsuarioResponse, Void> worker = new SwingWorker<>() {
            @Override
            protected UsuarioResponse doInBackground() throws Exception {
                return usuarioController.obtenerUsuario((long) id);
            }
            
            @Override
            protected void done() {
                try {
                    usuarioActual = get();
                    mostrarFormulario();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(EditarUsuarioFrame.this,
                        "Error al cargar usuario: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }
    
    private void mostrarFormulario() {
        // Remover contenido anterior
        Container contentPane = getContentPane();
        contentPane.removeAll();
        
        // Crear mainPanel completo
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(ColorPalette.FONDO_CLARO);
        
        // TopBar
        JPanel topBar = createTopBar();
        mainPanel.add(topBar, BorderLayout.NORTH);
        
        // Content
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(ColorPalette.FONDO_CLARO);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.GRIS_BORDES, 1),
            BorderFactory.createEmptyBorder(25, 30, 25, 30)
        ));
        formPanel.setMaximumSize(new Dimension(600, Integer.MAX_VALUE));
        
        // Título
        JLabel sectionTitle = new JLabel("Editar Datos del Usuario");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sectionTitle.setForeground(ColorPalette.TEXTO_PRINCIPAL_NEGRO);
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(sectionTitle);
        formPanel.add(Box.createVerticalStrut(20));
        
        // Nombre Usuario
        formPanel.add(createFieldLabel("Nombre de Usuario *"));
        nombreUsuarioField = createTextField();
        nombreUsuarioField.setText(usuarioActual.nombreUsuario);
        formPanel.add(nombreUsuarioField);
        formPanel.add(Box.createVerticalStrut(15));
        
        // Password
        formPanel.add(createFieldLabel("Contraseña (dejar vacío para mantener) *"));
        passwordField = createTextField();
        formPanel.add(passwordField);
        formPanel.add(Box.createVerticalStrut(15));
        
        // Rol
        formPanel.add(createFieldLabel("Rol *"));
        String[] roles = {"USER", "ADMIN"};
        rolCombo = new JComboBox<>(roles);
        rolCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        rolCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        rolCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        rolCombo.setSelectedItem(usuarioActual.rol != null ? usuarioActual.rol : "USER");
        formPanel.add(rolCombo);
        formPanel.add(Box.createVerticalStrut(15));
        
        // Activo
        activoCheck = new JCheckBox("Usuario Activo");
        activoCheck.setFont(new Font("Segoe UI", Font.BOLD, 14));
        activoCheck.setForeground(ColorPalette.TEXTO_PRINCIPAL_NEGRO);
        activoCheck.setBackground(Color.WHITE);
        activoCheck.setSelected(usuarioActual.activo);
        activoCheck.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(activoCheck);
        formPanel.add(Box.createVerticalStrut(25));
        
        // Botón Actualizar
        actualizarBtn = new JButton("💾 Actualizar Usuario");
        actualizarBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        actualizarBtn.setForeground(Color.WHITE);
        actualizarBtn.setBackground(ColorPalette.AZUL_PRIMARIO_MEDIO);
        actualizarBtn.setFocusPainted(false);
        actualizarBtn.setBorderPainted(false);
        actualizarBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        actualizarBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        actualizarBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        actualizarBtn.addActionListener(e -> actualizarUsuario());
        formPanel.add(actualizarBtn);
        
        contentPanel.add(formPanel);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel);
        revalidate();
        repaint();
    }
    
    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(ColorPalette.TEXTO_PRINCIPAL_NEGRO);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }
    
    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.GRIS_BORDES, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        return field;
    }
    
    private void actualizarUsuario() {
        String nombreUsuario = nombreUsuarioField.getText().trim();
        String password = passwordField.getText().trim();
        String rol = (String) rolCombo.getSelectedItem();
        boolean activo = activoCheck.isSelected();
        
        if (nombreUsuario.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "El nombre de usuario es obligatorio",
                "Error de Validación",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        actualizarBtn.setEnabled(false);
        actualizarBtn.setText("Actualizando...");
        
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                UsuarioRequest request = new UsuarioRequest();
                request.nombreUsuario = nombreUsuario;
                request.password = password.isEmpty() ? null : password;
                request.rol = rol;
                request.activo = activo;
                
                usuarioController.actualizarUsuario((long) id, request);
                return null;
            }
            
            @Override
            protected void done() {
                try {
                    get();
                    JOptionPane.showMessageDialog(EditarUsuarioFrame.this,
                        "Usuario actualizado exitosamente",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                    new UsuariosFrame().setVisible(true);
                    dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(EditarUsuarioFrame.this,
                        "Error al actualizar usuario: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    actualizarBtn.setEnabled(true);
                    actualizarBtn.setText("💾 Actualizar Usuario");
                }
            }
        };
        worker.execute();
    }
}
