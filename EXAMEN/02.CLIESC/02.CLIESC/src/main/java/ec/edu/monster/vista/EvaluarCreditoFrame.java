package ec.edu.monster.vista;

import ec.edu.monster.controlador.CreditoController;
import ec.edu.monster.modelo.BanquitoModels.*;
import ec.edu.monster.util.ColorPalette;

import javax.swing.*;
import java.awt.*;

/**
 * Pantalla para evaluar una solicitud de crédito
 */
public class EvaluarCreditoFrame extends JFrame {
    
    private final CreditoController creditoController;
    private JTextField cuentaField, montoField, plazoField;
    private JButton evaluarBtn;
    
    public EvaluarCreditoFrame() {
        this.creditoController = new CreditoController();
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Evaluar Crédito - BanQuito");
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
            new CreditosFrame().setVisible(true);
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
        JLabel sectionTitle = new JLabel("Solicitud de Crédito");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sectionTitle.setForeground(ColorPalette.TEXTO_PRINCIPAL_NEGRO);
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(sectionTitle);
        formPanel.add(Box.createVerticalStrut(20));
        
        // Número Cuenta
        formPanel.add(createFieldLabel("Número de Cuenta *"));
        cuentaField = createTextField();
        formPanel.add(cuentaField);
        formPanel.add(Box.createVerticalStrut(15));
        
        // Monto Solicitado
        formPanel.add(createFieldLabel("Monto Solicitado *"));
        montoField = createTextField();
        formPanel.add(montoField);
        formPanel.add(Box.createVerticalStrut(15));
        
        // Plazo en Meses
        formPanel.add(createFieldLabel("Plazo en Meses *"));
        plazoField = createTextField();
        formPanel.add(plazoField);
        formPanel.add(Box.createVerticalStrut(10));
        
        JLabel infoLabel = new JLabel("💡 El sistema evaluará automáticamente la elegibilidad del crédito");
        infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        infoLabel.setForeground(ColorPalette.TEXTO_SECUNDARIO_GRIS);
        infoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(infoLabel);
        formPanel.add(Box.createVerticalStrut(25));
        
        // Botón Evaluar
        evaluarBtn = new JButton("Evaluar Crédito");
        evaluarBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        evaluarBtn.setForeground(Color.WHITE);
        evaluarBtn.setBackground(ColorPalette.AZUL_PRIMARIO_MEDIO);
        evaluarBtn.setFocusPainted(false);
        evaluarBtn.setBorderPainted(false);
        evaluarBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        evaluarBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        evaluarBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        evaluarBtn.addActionListener(e -> evaluarCredito());
        formPanel.add(evaluarBtn);
        
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
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.GRIS_BORDES, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        return field;
    }
    
    private void evaluarCredito() {
        String numCuenta = cuentaField.getText().trim();
        String montoStr = montoField.getText().trim();
        String plazoStr = plazoField.getText().trim();
        
        if (numCuenta.isEmpty() || montoStr.isEmpty() || plazoStr.isEmpty()) {
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
        SwingWorker<ResultadoEvaluacion, Void> worker = new SwingWorker<>() {
            @Override
            protected ResultadoEvaluacion doInBackground() throws Exception {
                SolicitudCredito solicitud = new SolicitudCredito();
                solicitud.numCuenta = numCuenta;
                solicitud.montoSolicitado = java.math.BigDecimal.valueOf(montoFinal);
                solicitud.plazoMeses = plazoFinal;
                
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
                    evaluarBtn.setText("📊 Evaluar Crédito");
                }
            }
        };
        worker.execute();
    }
    
    private void mostrarResultado(ResultadoEvaluacion resultado) {
        String mensaje;
        int tipo;
        
        if (resultado.aprobado) {
            mensaje = String.format(
                "CRÉDITO APROBADO\n\n" +
                "Monto Aprobado: $%.2f\n" +
                "Tasa de Interés: %.2f%%\n" +
                "Plazo: %d meses\n" +
                "Cuota Mensual: $%.2f\n\n" +
                "Razón: %s",
                resultado.montoAprobado,
                resultado.tasaInteres,
                resultado.plazoMeses,
                resultado.cuotaMensual,
                resultado.mensaje
            );
            tipo = JOptionPane.INFORMATION_MESSAGE;
        } else {
            mensaje = String.format(
                "CRÉDITO RECHAZADO\n\n" +
                "Razón: %s",
                resultado.mensaje
            );
            tipo = JOptionPane.WARNING_MESSAGE;
        }
        
        JOptionPane.showMessageDialog(this, mensaje, "Resultado de Evaluación", tipo);
        
        if (resultado.aprobado) {
            new CreditosFrame().setVisible(true);
            dispose();
        } else {
            evaluarBtn.setEnabled(true);
            evaluarBtn.setText("📊 Evaluar Crédito");
        }
    }
}
