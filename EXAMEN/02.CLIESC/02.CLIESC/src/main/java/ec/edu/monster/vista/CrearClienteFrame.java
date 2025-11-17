package ec.edu.monster.vista;

import ec.edu.monster.controlador.ClienteController;
import ec.edu.monster.modelo.BanquitoModels.ClienteRequest;
import ec.edu.monster.util.ColorPalette;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

/**
 * Pantalla para crear nuevo cliente
 */
public class CrearClienteFrame extends JFrame {
    
    private final ClienteController clienteController;
    private JTextField cedulaField, nombreField, fechaField, numCuentaField, saldoField;
    private JComboBox<String> estadoCivilCombo, tipoCuentaCombo;
    private JButton guardarBtn;
    
    public CrearClienteFrame() {
        this.clienteController = new ClienteController();
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Nuevo Cliente - BanQuito");
        setSize(700, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(ColorPalette.FONDO_CLARO);
        
        // TopBar
        JPanel topBar = createTopBar();
        mainPanel.add(topBar, BorderLayout.NORTH);
        
        // Contenido scrollable
        JPanel contentWrapper = new JPanel(new GridBagLayout());
        contentWrapper.setBackground(ColorPalette.FONDO_CLARO);
        
        JPanel formPanel = createFormPanel();
        contentWrapper.add(formPanel);
        
        JScrollPane scrollPane = new JScrollPane(contentWrapper);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
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
            new ClientesFrame().setVisible(true);
            dispose();
        });
        
        JLabel titleLabel = new JLabel("Nuevo Cliente");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        topBar.add(backBtn, BorderLayout.WEST);
        topBar.add(titleLabel, BorderLayout.CENTER);
        
        return topBar;
    }
    
    private JPanel createFormPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.GRIS_BORDES, 1),
            BorderFactory.createEmptyBorder(25, 30, 25, 30)
        ));
        panel.setMaximumSize(new Dimension(600, Integer.MAX_VALUE));
        
        // Título de sección
        JLabel sectionTitle = new JLabel("Datos del Cliente");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sectionTitle.setForeground(ColorPalette.TEXTO_PRINCIPAL_NEGRO);
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(sectionTitle);
        panel.add(Box.createVerticalStrut(20));
        
        // Campo Cédula
        panel.add(createFieldLabel("Cédula *"));
        cedulaField = createTextField();
        panel.add(cedulaField);
        panel.add(Box.createVerticalStrut(15));
        
        // Campo Nombre
        panel.add(createFieldLabel("Nombre Completo *"));
        nombreField = createTextField();
        panel.add(nombreField);
        panel.add(Box.createVerticalStrut(15));
        
        // Campo Fecha Nacimiento
        panel.add(createFieldLabel("Fecha de Nacimiento (YYYY-MM-DD) *"));
        fechaField = createTextField();
        fechaField.setText("1990-01-01");
        panel.add(fechaField);
        panel.add(Box.createVerticalStrut(15));
        
        // Estado Civil
        panel.add(createFieldLabel("Estado Civil *"));
        String[] estadosCiviles = {"SOLTERO", "CASADO", "DIVORCIADO", "VIUDO"};
        estadoCivilCombo = new JComboBox<>(estadosCiviles);
        estadoCivilCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        estadoCivilCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        estadoCivilCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(estadoCivilCombo);
        panel.add(Box.createVerticalStrut(15));
        
        // Tipo de Cuenta
        panel.add(createFieldLabel("Tipo de Cuenta Inicial *"));
        String[] tiposCuenta = {"AHORROS", "CORRIENTE"};
        tipoCuentaCombo = new JComboBox<>(tiposCuenta);
        tipoCuentaCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tipoCuentaCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        tipoCuentaCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(tipoCuentaCombo);
        panel.add(Box.createVerticalStrut(15));
        
        // Número de Cuenta (Opcional)
        panel.add(createFieldLabel("Número de Cuenta (Opcional - Se genera automáticamente)"));
        numCuentaField = createTextField();
        panel.add(numCuentaField);
        panel.add(Box.createVerticalStrut(15));
        
        // Saldo Inicial
        panel.add(createFieldLabel("Saldo Inicial (Opcional)"));
        saldoField = createTextField();
        saldoField.setText("0.00");
        panel.add(saldoField);
        panel.add(Box.createVerticalStrut(25));
        
        // Botón Guardar
        guardarBtn = new JButton("💾 Guardar Cliente");
        guardarBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        guardarBtn.setForeground(Color.WHITE);
        guardarBtn.setBackground(ColorPalette.VERDE_EXITO);
        guardarBtn.setFocusPainted(false);
        guardarBtn.setBorderPainted(false);
        guardarBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        guardarBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        guardarBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        guardarBtn.addActionListener(e -> guardarCliente());
        panel.add(guardarBtn);
        
        return panel;
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
    
    private void guardarCliente() {
        // Validaciones
        String cedula = cedulaField.getText().trim();
        String nombre = nombreField.getText().trim();
        String fecha = fechaField.getText().trim();
        String estadoCivil = (String) estadoCivilCombo.getSelectedItem();
        String tipoCuenta = (String) tipoCuentaCombo.getSelectedItem();
        
        if (cedula.isEmpty() || nombre.isEmpty() || fecha.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Por favor complete todos los campos obligatorios (*)",
                "Error de Validación",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Validar formato de cédula (solo números)
        if (!cedula.matches("\\d+")) {
            JOptionPane.showMessageDialog(this,
                "La cédula debe contener solo números",
                "Error de Validación",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Validar formato de fecha
        if (!fecha.matches("\\d{4}-\\d{2}-\\d{2}")) {
            JOptionPane.showMessageDialog(this,
                "La fecha debe estar en formato YYYY-MM-DD",
                "Error de Validación",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        guardarBtn.setEnabled(false);
        guardarBtn.setText("Guardando...");
        
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                ClienteRequest request = new ClienteRequest();
                request.cedula = cedula;
                request.nombre = nombre;
                request.fechaNacimiento = fecha;
                request.estadoCivil = estadoCivil;
                request.tipoCuentaInicial = tipoCuenta;
                
                String numCuenta = numCuentaField.getText().trim();
                if (!numCuenta.isEmpty()) {
                    request.numeroCuenta = numCuenta;
                }
                
                String saldoText = saldoField.getText().trim();
                if (!saldoText.isEmpty()) {
                    try {
                        request.saldoInicial = new BigDecimal(saldoText);
                    } catch (NumberFormatException e) {
                        throw new Exception("Saldo inválido");
                    }
                }
                
                clienteController.crearCliente(request);
                return null;
            }
            
            @Override
            protected void done() {
                try {
                    get();
                    JOptionPane.showMessageDialog(CrearClienteFrame.this,
                        "Cliente creado exitosamente",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                    new ClientesFrame().setVisible(true);
                    dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(CrearClienteFrame.this,
                        "Error al crear cliente: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    guardarBtn.setEnabled(true);
                    guardarBtn.setText("💾 Guardar Cliente");
                }
            }
        };
        worker.execute();
    }
}
