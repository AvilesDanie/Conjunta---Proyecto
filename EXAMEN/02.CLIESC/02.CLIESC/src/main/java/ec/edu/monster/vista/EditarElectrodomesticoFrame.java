package ec.edu.monster.vista;

import ec.edu.monster.controlador.ElectrodomesticoController;
import ec.edu.monster.modelo.ComercializadoraModels.*;
import ec.edu.monster.util.ColorPalette;

import javax.swing.*;
import java.awt.*;

public class EditarElectrodomesticoFrame extends JFrame {
    
    private final ElectrodomesticoController electroController;
    private final String codigo;
    private JTextField codigoField, nombreField, precioField;
    private JButton actualizarBtn;
    private ElectrodomesticoResponse electroActual;
    
    public EditarElectrodomesticoFrame(String codigo) {
        this.electroController = new ElectrodomesticoController();
        this.codigo = codigo;
        initComponents();
        cargarElectrodomestico();
    }
    
    private void initComponents() {
        setTitle("Editar Electrodoméstico - ElectroQuito");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(ColorPalette.FONDO_CLARO);
        
        JPanel topBar = createTopBar();
        mainPanel.add(topBar, BorderLayout.NORTH);
        
        add(mainPanel);
    }
    
    private JPanel createTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(ColorPalette.VERDE_EXITO);
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
            new ElectrodomesticosFrame().setVisible(true);
            dispose();
        });
        
        JLabel titleLabel = new JLabel("Editar Electrodoméstico");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        topBar.add(backBtn, BorderLayout.WEST);
        topBar.add(titleLabel, BorderLayout.CENTER);
        
        return topBar;
    }
    
    private void cargarElectrodomestico() {
        SwingWorker<ElectrodomesticoResponse, Void> worker = new SwingWorker<>() {
            @Override
            protected ElectrodomesticoResponse doInBackground() throws Exception {
                return electroController.obtenerElectrodomestico(codigo);
            }
            
            @Override
            protected void done() {
                try {
                    electroActual = get();
                    mostrarFormulario();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(EditarElectrodomesticoFrame.this,
                        "Error al cargar electrodoméstico: " + ex.getMessage(),
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
        
        JLabel sectionTitle = new JLabel("Editar Datos del Electrodoméstico");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sectionTitle.setForeground(ColorPalette.TEXTO_PRINCIPAL_NEGRO);
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(sectionTitle);
        formPanel.add(Box.createVerticalStrut(20));
        
        formPanel.add(createFieldLabel("Código"));
        codigoField = createTextField();
        codigoField.setText(electroActual.codigo);
        codigoField.setEnabled(false);
        codigoField.setBackground(ColorPalette.FONDO_DISABLED);
        formPanel.add(codigoField);
        formPanel.add(Box.createVerticalStrut(15));
        
        formPanel.add(createFieldLabel("Nombre *"));
        nombreField = createTextField();
        nombreField.setText(electroActual.nombre);
        formPanel.add(nombreField);
        formPanel.add(Box.createVerticalStrut(15));
        
        formPanel.add(createFieldLabel("Precio de Venta *"));
        precioField = createTextField();
        precioField.setText(String.format("%.2f", electroActual.precioVenta));
        formPanel.add(precioField);
        formPanel.add(Box.createVerticalStrut(25));
        
        actualizarBtn = new JButton("💾 Actualizar Electrodoméstico");
        actualizarBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        actualizarBtn.setForeground(Color.WHITE);
        actualizarBtn.setBackground(ColorPalette.VERDE_EXITO);
        actualizarBtn.setFocusPainted(false);
        actualizarBtn.setBorderPainted(false);
        actualizarBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        actualizarBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        actualizarBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        actualizarBtn.addActionListener(e -> actualizarElectrodomestico());
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
    
    private void actualizarElectrodomestico() {
        String nombre = nombreField.getText().trim();
        String precioStr = precioField.getText().trim();
        
        if (nombre.isEmpty() || precioStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Por favor complete todos los campos",
                "Error de Validación",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        double precio;
        try {
            precio = Double.parseDouble(precioStr);
            if (precio <= 0) {
                JOptionPane.showMessageDialog(this,
                    "El precio debe ser mayor a cero",
                    "Error de Validación",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "El precio debe ser un número válido",
                "Error de Validación",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        actualizarBtn.setEnabled(false);
        actualizarBtn.setText("Actualizando...");
        
        double precioFinal = precio;
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                ElectrodomesticoRequest request = new ElectrodomesticoRequest();
                request.codigo = codigo;
                request.nombre = nombre;
                request.precioVenta = java.math.BigDecimal.valueOf(precioFinal);
                
                electroController.actualizarElectrodomestico(codigo, request);
                return null;
            }
            
            @Override
            protected void done() {
                try {
                    get();
                    JOptionPane.showMessageDialog(EditarElectrodomesticoFrame.this,
                        "Electrodoméstico actualizado exitosamente",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                    new ElectrodomesticosFrame().setVisible(true);
                    dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(EditarElectrodomesticoFrame.this,
                        "Error al actualizar electrodoméstico: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    actualizarBtn.setEnabled(true);
                    actualizarBtn.setText("💾 Actualizar Electrodoméstico");
                }
            }
        };
        worker.execute();
    }
}
