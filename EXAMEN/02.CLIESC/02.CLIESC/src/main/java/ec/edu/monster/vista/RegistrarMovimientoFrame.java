package ec.edu.monster.vista;

import ec.edu.monster.controlador.MovimientoController;
import ec.edu.monster.modelo.BanquitoModels.*;
import ec.edu.monster.util.ColorPalette;

import javax.swing.*;
import java.awt.*;

/**
 * Pantalla para registrar un nuevo movimiento bancario
 */
public class RegistrarMovimientoFrame extends JFrame {
    
    private final MovimientoController movimientoController;
    private final String numCuenta;
    private JComboBox<String> tipoMovimientoCombo;
    private JTextField valorField, descripcionField;
    private JButton registrarBtn;
    
    public RegistrarMovimientoFrame(String numCuenta) {
        this.movimientoController = new MovimientoController();
        this.numCuenta = numCuenta;
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Registrar Movimiento - BanQuito");
        setSize(700, 550);
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
            new MovimientosPorCuentaFrame(numCuenta).setVisible(true);
            dispose();
        });
        
        JLabel titleLabel = new JLabel("Registrar Movimiento");
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
        formPanel.putClientProperty("FlatLaf.style", "arc: 12");
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(66, 133, 244), 2),
            BorderFactory.createEmptyBorder(30, 35, 30, 35)
        ));
        formPanel.setMaximumSize(new Dimension(600, Integer.MAX_VALUE));
        
        // Título
        JLabel sectionTitle = new JLabel("Datos del Movimiento");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sectionTitle.setForeground(ColorPalette.TEXTO_PRINCIPAL_NEGRO);
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(sectionTitle);
        formPanel.add(Box.createVerticalStrut(20));
        
        // Número Cuenta (disabled)
        formPanel.add(createFieldLabel("Número de Cuenta"));
        JTextField numCuentaField = createTextField();
        numCuentaField.setText(numCuenta);
        numCuentaField.setEnabled(false);
        numCuentaField.setBackground(ColorPalette.FONDO_DISABLED);
        formPanel.add(numCuentaField);
        formPanel.add(Box.createVerticalStrut(15));
        
        // Tipo Movimiento
        formPanel.add(createFieldLabel("Tipo de Movimiento *"));
        String[] tiposMovimiento = {"DEPOSITO", "RETIRO", "TRANSFERENCIA"};
        tipoMovimientoCombo = new JComboBox<>(tiposMovimiento);
        tipoMovimientoCombo.putClientProperty("FlatLaf.style", "arc: 8");
        tipoMovimientoCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tipoMovimientoCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        tipoMovimientoCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(tipoMovimientoCombo);
        formPanel.add(Box.createVerticalStrut(15));
        
        // Valor
        formPanel.add(createFieldLabel("Valor *"));
        valorField = createTextField();
        formPanel.add(valorField);
        formPanel.add(Box.createVerticalStrut(25));
        
        // Botón Registrar
        registrarBtn = new JButton("Registrar Movimiento");
        registrarBtn.putClientProperty("FlatLaf.style", "arc: 10; borderWidth: 0; font: bold +1");
        registrarBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        registrarBtn.setForeground(Color.WHITE);
        registrarBtn.setBackground(ColorPalette.VERDE_EXITO);
        registrarBtn.setFocusPainted(false);
        registrarBtn.setBorderPainted(false);
        registrarBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        registrarBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        registrarBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registrarBtn.addActionListener(e -> registrarMovimiento());
        formPanel.add(registrarBtn);
        
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
    
    private void registrarMovimiento() {
        String tipoMovimiento = (String) tipoMovimientoCombo.getSelectedItem();
        String valorStr = valorField.getText().trim();
        
        if (valorStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Por favor ingrese el valor del movimiento",
                "Error de Validación",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        double valor;
        try {
            valor = Double.parseDouble(valorStr);
            if (valor <= 0) {
                JOptionPane.showMessageDialog(this,
                    "El valor debe ser mayor a cero",
                    "Error de Validación",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "El valor debe ser un número válido",
                "Error de Validación",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        registrarBtn.setEnabled(false);
        registrarBtn.setText("Registrando...");
        
        double valorFinal = valor;
        String tipoMovimientoFinal = tipoMovimiento;
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                MovimientoRequest request = new MovimientoRequest();
                request.numCuenta = numCuenta;
                
                // Convertir tipo a formato del servidor: DEP, RET, TRA
                String tipoServidor = "";
                switch (tipoMovimientoFinal) {
                    case "DEPOSITO":
                        tipoServidor = "DEP";
                        break;
                    case "RETIRO":
                        tipoServidor = "RET";
                        break;
                    case "TRANSFERENCIA":
                        tipoServidor = "TRA";
                        break;
                }
                
                request.tipo = tipoServidor;
                request.tipoMovimiento = tipoServidor;
                request.valor = java.math.BigDecimal.valueOf(valorFinal);
                
                movimientoController.registrarMovimiento(request);
                return null;
            }
            
            @Override
            protected void done() {
                try {
                    get();
                    JOptionPane.showMessageDialog(RegistrarMovimientoFrame.this,
                        "Movimiento registrado exitosamente",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                    new MovimientosPorCuentaFrame(numCuenta).setVisible(true);
                    dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(RegistrarMovimientoFrame.this,
                        "Error al registrar movimiento: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    registrarBtn.setEnabled(true);
                    registrarBtn.setText("✓ Registrar Movimiento");
                }
            }
        };
        worker.execute();
    }
}
