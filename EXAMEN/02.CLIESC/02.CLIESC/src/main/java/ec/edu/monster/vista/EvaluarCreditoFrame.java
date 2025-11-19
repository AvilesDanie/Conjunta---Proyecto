package ec.edu.monster.vista;

import ec.edu.monster.controlador.CreditoController;
import ec.edu.monster.modelo.BanquitoModels.*;
import ec.edu.monster.util.ColorPalette;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

/**
 * Pantalla para evaluar una solicitud de crédito
 */
public class EvaluarCreditoFrame extends JFrame {
    
    private final CreditoController creditoController;
    private JTextField cedulaField, montoField, plazoField, cuentaCreditoField;
    private JButton evaluarBtn;
    private SolicitudCredito solicitudActual; // Guardar la solicitud para usarla al crear el crédito
    
    public EvaluarCreditoFrame() {
        this.creditoController = new CreditoController();
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Evaluar Crédito - BanQuito");
        setSize(700, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Configurar la ventana para pantalla completa
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        
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
            new HomeBanquitoFrame().setVisible(true);
            dispose();
        });
        
        JLabel titleLabel = new JLabel("Evaluar Crédito");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        topBar.add(backBtn, BorderLayout.WEST);
        topBar.add(titleLabel, BorderLayout.CENTER);
        
        return topBar;
    }
    
    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(new Color(240, 244, 248));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.putClientProperty("FlatLaf.style", "arc: 16");
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            new javax.swing.border.EmptyBorder(0, 0, 0, 0),
            BorderFactory.createEmptyBorder(35, 40, 35, 40)
        ));
        formPanel.setMaximumSize(new Dimension(650, Integer.MAX_VALUE));
        
        // Panel de información superior con icono
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.X_AXIS));
        infoPanel.setBackground(new Color(232, 245, 255));
        infoPanel.putClientProperty("FlatLaf.style", "arc: 12");
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        infoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        
        JLabel iconLabel = new JLabel("💳");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 42));
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        
        JLabel titleInfo = new JLabel("Evaluación de Crédito");
        titleInfo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleInfo.setForeground(new Color(33, 33, 33));
        
        JLabel subtitleInfo = new JLabel("Complete los datos para evaluar si el cliente es sujeto de crédito");
        subtitleInfo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitleInfo.setForeground(new Color(100, 100, 100));
        
        textPanel.add(titleInfo);
        textPanel.add(Box.createVerticalStrut(5));
        textPanel.add(subtitleInfo);
        
        infoPanel.add(iconLabel);
        infoPanel.add(Box.createHorizontalStrut(15));
        infoPanel.add(textPanel);
        
        formPanel.add(infoPanel);
        formPanel.add(Box.createVerticalStrut(30));
        
        // Cédula del Cliente
        formPanel.add(createModernFieldWithIcon("👤", "Cédula del Cliente *", cedulaField = createModernTextField()));
        formPanel.add(Box.createVerticalStrut(20));
        
        // Precio del Producto
        formPanel.add(createModernFieldWithIcon("💵", "Precio del Producto *", montoField = createModernTextField()));
        formPanel.add(Box.createVerticalStrut(20));
        
        // Plazo en Meses
        formPanel.add(createModernFieldWithIcon("📅", "Plazo en Meses *", plazoField = createModernTextField()));
        formPanel.add(Box.createVerticalStrut(20));
        
        // Número de Cuenta para Crédito
        formPanel.add(createModernFieldWithIcon("🏦", "Número de Cuenta para Crédito *", cuentaCreditoField = createModernTextField()));
        formPanel.add(Box.createVerticalStrut(35));
        
        // Botón Evaluar con estilo moderno
        evaluarBtn = new JButton("EVALUAR CRÉDITO");
        evaluarBtn.putClientProperty("FlatLaf.style", "arc: 12; borderWidth: 0; font: bold +2");
        evaluarBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        evaluarBtn.setForeground(Color.WHITE);
        evaluarBtn.setBackground(new Color(66, 133, 244));
        evaluarBtn.setFocusPainted(false);
        evaluarBtn.setBorderPainted(false);
        evaluarBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));
        evaluarBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        evaluarBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        evaluarBtn.addActionListener(e -> evaluarCredito());
        formPanel.add(evaluarBtn);
        
        contentPanel.add(formPanel);
        
        return contentPanel;
    }
    
    private JPanel createModernFieldWithIcon(String icon, String label, JTextField field) {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setOpaque(false);
        container.setAlignmentX(Component.LEFT_ALIGNMENT);
        container.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        
        JLabel labelText = new JLabel(label);
        labelText.setFont(new Font("Segoe UI", Font.BOLD, 13));
        labelText.setForeground(new Color(60, 60, 60));
        labelText.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel fieldPanel = new JPanel(new BorderLayout(12, 0));
        fieldPanel.setBackground(Color.WHITE);
        fieldPanel.putClientProperty("FlatLaf.style", "arc: 8");
        fieldPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        fieldPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        iconLabel.setForeground(new Color(120, 120, 120));
        
        field.setBorder(null);
        field.setOpaque(false);
        
        fieldPanel.add(iconLabel, BorderLayout.WEST);
        fieldPanel.add(field, BorderLayout.CENTER);
        
        container.add(labelText);
        container.add(Box.createVerticalStrut(8));
        container.add(fieldPanel);
        
        return container;
    }
    
    private JTextField createModernTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        field.setForeground(new Color(33, 33, 33));
        field.setBackground(Color.WHITE);
        return field;
    }
    
    private void evaluarCredito() {
        String cedula = cedulaField.getText().trim();
        String numCuenta = cuentaCreditoField.getText().trim();
        String montoStr = montoField.getText().trim();
        String plazoStr = plazoField.getText().trim();
        
        if (cedula.isEmpty() || numCuenta.isEmpty() || montoStr.isEmpty() || plazoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Por favor complete todos los campos",
                "Error de Validación",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        double monto;
        int plazo;
        try {
            monto = Double.parseDouble(montoStr);
            plazo = Integer.parseInt(plazoStr);
            
            if (monto <= 0 || plazo <= 0) {
                JOptionPane.showMessageDialog(this,
                    "El monto y plazo deben ser mayores a cero",
                    "Error de Validación",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Monto y plazo deben ser números válidos",
                "Error de Validación",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        evaluarBtn.setEnabled(false);
        evaluarBtn.setText("Evaluando...");
        
        double montoFinal = monto;
        int plazoFinal = plazo;
        String cedulaFinal = cedula;
        String numCuentaFinal = numCuenta;
        
        SwingWorker<ResultadoEvaluacion, Void> worker = new SwingWorker<>() {
            @Override
            protected ResultadoEvaluacion doInBackground() throws Exception {
                SolicitudCredito solicitud = new SolicitudCredito();
                solicitud.cedula = cedulaFinal;
                solicitud.precioProducto = java.math.BigDecimal.valueOf(montoFinal);
                solicitud.plazoMeses = plazoFinal;
                solicitud.numCuentaCredito = numCuentaFinal;
                
                // Guardar la solicitud para usarla después
                solicitudActual = solicitud;
                
                return creditoController.evaluarCredito(solicitud);
            }
            
            @Override
            protected void done() {
                try {
                    ResultadoEvaluacion resultado = get();
                    mostrarResultado(resultado);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(EvaluarCreditoFrame.this,
                        "Error al evaluar crédito: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    evaluarBtn.setEnabled(true);
                    evaluarBtn.setText("EVALUAR CRÉDITO");
                }
            }
        };
        worker.execute();
    }
    
    private void mostrarResultado(ResultadoEvaluacion resultado) {
        if (resultado.aprobado) {
            // Usar los campos que realmente vienen del servidor
            BigDecimal monto = resultado.montoMaximo != null ? resultado.montoMaximo : 
                              (resultado.montoAprobado != null ? resultado.montoAprobado : BigDecimal.ZERO);
            
            String mensaje = String.format(
                "✓ CRÉDITO APROBADO\n\n" +
                "El cliente es sujeto de crédito.\n\n" +
                "Monto Máximo Aprobado: $%.2f\n\n" +
                "¿Desea crear el crédito ahora?",
                monto
            );
            
            int opcion = JOptionPane.showConfirmDialog(
                this,
                mensaje,
                "Crédito Aprobado",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE
            );
            
            if (opcion == JOptionPane.YES_OPTION) {
                crearCredito();
            } else {
                new HomeBanquitoFrame().setVisible(true);
                dispose();
            }
            
        } else {
            String mensaje = "✗ CRÉDITO NO APROBADO\n\n" +
                     "El cliente no califica para crédito en este momento.";
            
            JOptionPane.showMessageDialog(this, mensaje, "Crédito No Aprobado", JOptionPane.WARNING_MESSAGE);
            
            evaluarBtn.setEnabled(true);
            evaluarBtn.setText("EVALUAR CRÉDITO");
        }
    }
    
    private void crearCredito() {
        evaluarBtn.setEnabled(false);
        evaluarBtn.setText("Creando crédito...");
        
        SwingWorker<CreditoResponse, Void> worker = new SwingWorker<>() {
            @Override
            protected CreditoResponse doInBackground() throws Exception {
                return creditoController.crearCredito(solicitudActual);
            }
            
            @Override
            protected void done() {
                try {
                    CreditoResponse credito = get();
                    
                    String mensaje = String.format(
                        "✓ CRÉDITO CREADO EXITOSAMENTE\n\n" +
                        "ID Crédito: %d\n" +
                        "Cliente: %s\n" +
                        "Monto: $%.2f\n" +
                        "Plazo: %d meses\n" +
                        "Tasa Anual: %.2f%%\n" +
                        "Estado: %s\n\n" +
                        "Tabla de amortización generada.",
                        credito.id,
                        credito.cedulaCliente,
                        credito.monto,
                        credito.plazoMeses,
                        credito.tasaAnual,
                        credito.estado != null ? credito.estado : 
                        (credito.estadoCredito != null ? credito.estadoCredito : "ACTIVO")
                    );
                    
                    JOptionPane.showMessageDialog(
                        EvaluarCreditoFrame.this,
                        mensaje,
                        "Crédito Creado",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                    
                    new HomeBanquitoFrame().setVisible(true);
                    dispose();
                    
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                        EvaluarCreditoFrame.this,
                        "Error al crear crédito: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                    evaluarBtn.setEnabled(true);
                    evaluarBtn.setText("EVALUAR CRÉDITO");
                }
            }
        };
        worker.execute();
    }
}
