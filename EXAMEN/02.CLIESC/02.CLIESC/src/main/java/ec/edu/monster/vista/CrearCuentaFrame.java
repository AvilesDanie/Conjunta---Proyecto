package ec.edu.monster.vista;

import ec.edu.monster.controlador.CuentaController;
import ec.edu.monster.modelo.BanquitoModels.*;
import ec.edu.monster.util.ColorPalette;

import javax.swing.*;
import java.awt.*;

/**
 * Pantalla para crear una nueva cuenta
 */
public class CrearCuentaFrame extends JFrame {
    
    private final CuentaController cuentaController;
    private final String cedula;
    private JTextField cedulaField, numCuentaField, saldoField;
    private JComboBox<String> tipoCuentaCombo;
    private JButton guardarBtn;
    
    public CrearCuentaFrame(String cedulaCliente) {
        this.cuentaController = new CuentaController();
        this.cedula = cedulaCliente;
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Nueva Cuenta - BanQuito");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(ColorPalette.FONDO_CLARO);
        
        // TopBar
        JPanel topBar = createTopBar();
        mainPanel.add(topBar, BorderLayout.NORTH);
        
        // Content
        JPanel contentPanel = createContentPanel();
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(ColorPalette.AZUL_PRIMARIO);
        topBar.setPreferredSize(new Dimension(0, 70));
        topBar.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
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
            new CuentasPorClienteFrame(cedula).setVisible(true);
            dispose();
        });
        
        JLabel titleLabel = new JLabel("Crear Nueva Cuenta");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        topBar.add(backBtn, BorderLayout.WEST);
        topBar.add(titleLabel, BorderLayout.CENTER);
        
        return topBar;
    }
    
    private JPanel createContentPanel() {
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
        JLabel sectionTitle = new JLabel("Datos de la Nueva Cuenta");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sectionTitle.setForeground(ColorPalette.TEXTO_PRINCIPAL_NEGRO);
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(sectionTitle);
        formPanel.add(Box.createVerticalStrut(20));
        
        // Cédula Cliente (pre-filled, disabled)
        formPanel.add(createFieldLabel("Cédula del Cliente"));
        cedulaField = createTextField();
        cedulaField.setText(cedula);
        cedulaField.setEnabled(false);
        cedulaField.setBackground(ColorPalette.FONDO_DISABLED);
        formPanel.add(cedulaField);
        formPanel.add(Box.createVerticalStrut(15));
        
        // Tipo de Cuenta
        formPanel.add(createFieldLabel("Tipo de Cuenta *"));
        String[] tiposCuenta = {"AHORROS", "CORRIENTE"};
        tipoCuentaCombo = new JComboBox<>(tiposCuenta);
        tipoCuentaCombo.putClientProperty("FlatLaf.style", "arc: 8");
        tipoCuentaCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tipoCuentaCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        tipoCuentaCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(tipoCuentaCombo);
        formPanel.add(Box.createVerticalStrut(15));
        
        // Saldo Inicial
        formPanel.add(createFieldLabel("Saldo Inicial (opcional)"));
        saldoField = createTextField();
        saldoField.setText("0.00");
        formPanel.add(saldoField);
        formPanel.add(Box.createVerticalStrut(10));
        
        JLabel infoLabel = new JLabel("💡 El saldo inicial por defecto es $0.00");
        infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        infoLabel.setForeground(ColorPalette.TEXTO_SECUNDARIO_GRIS);
        infoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(infoLabel);
        formPanel.add(Box.createVerticalStrut(25));
        
        // Botón Guardar
        guardarBtn = new JButton("✓ Crear Cuenta");
        guardarBtn.putClientProperty("FlatLaf.style", "arc: 10; borderWidth: 0; font: bold +1");
        guardarBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        guardarBtn.setForeground(Color.WHITE);
        guardarBtn.setBackground(ColorPalette.VERDE_EXITO);
        guardarBtn.setFocusPainted(false);
        guardarBtn.setBorderPainted(false);
        guardarBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        guardarBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        guardarBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        guardarBtn.addActionListener(e -> crearCuenta());
        formPanel.add(guardarBtn);
        
        contentPanel.add(formPanel);
        
        return contentPanel;
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
        field.putClientProperty("FlatLaf.style", "arc: 8");
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.GRIS_BORDES, 1),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        return field;
    }
    
    private void crearCuenta() {
        String tipoCuenta = (String) tipoCuentaCombo.getSelectedItem();
        String saldoStr = saldoField.getText().trim();
        
        double saldo = 0.0;
        try {
            saldo = Double.parseDouble(saldoStr);
            if (saldo < 0) {
                JOptionPane.showMessageDialog(this,
                    "El saldo no puede ser negativo",
                    "Error de Validación",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "El saldo debe ser un número válido",
                "Error de Validación",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        guardarBtn.setEnabled(false);
        guardarBtn.setText("Creando...");
        
        double saldoFinal = saldo;
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                CuentaRequest request = new CuentaRequest();
                request.cedulaCliente = cedula;
                request.tipoCuenta = tipoCuenta;
                request.saldo = java.math.BigDecimal.valueOf(saldoFinal);
                
                cuentaController.crearCuenta(request);
                return null;
            }
            
            @Override
            protected void done() {
                try {
                    get();
                    JOptionPane.showMessageDialog(CrearCuentaFrame.this,
                        "Cuenta creada exitosamente",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                    new CuentasPorClienteFrame(cedula).setVisible(true);
                    dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(CrearCuentaFrame.this,
                        "Error al crear cuenta: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    guardarBtn.setEnabled(true);
                    guardarBtn.setText("💾 Crear Cuenta");
                }
            }
        };
        worker.execute();
    }
}
