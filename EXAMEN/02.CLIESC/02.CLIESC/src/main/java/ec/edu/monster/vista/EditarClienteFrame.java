package ec.edu.monster.vista;

import ec.edu.monster.controlador.ClienteController;
import ec.edu.monster.modelo.BanquitoModels.*;
import ec.edu.monster.util.ColorPalette;

import javax.swing.*;
import java.awt.*;

/**
 * Pantalla para editar cliente existente
 */
public class EditarClienteFrame extends JFrame {
    
    private final ClienteController clienteController;
    private final String cedula;
    private JTextField nombreField, fechaField;
    private JComboBox<String> estadoCivilCombo;
    private JButton actualizarBtn;
    private ClienteOnlyResponse clienteActual;
    
    public EditarClienteFrame(String cedula) {
        this.clienteController = new ClienteController();
        this.cedula = cedula;
        initComponents();
        cargarCliente();
    }
    
    private void initComponents() {
        setTitle("Editar Cliente - BanQuito");
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
            new ClienteDetalleFrame(cedula).setVisible(true);
            dispose();
        });
        
        JLabel titleLabel = new JLabel("Editar Cliente");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        topBar.add(backBtn, BorderLayout.WEST);
        topBar.add(titleLabel, BorderLayout.CENTER);
        
        return topBar;
    }
    
    private void cargarCliente() {
        SwingWorker<ClienteOnlyResponse, Void> worker = new SwingWorker<>() {
            @Override
            protected ClienteOnlyResponse doInBackground() throws Exception {
                return clienteController.obtenerCliente(cedula);
            }
            
            @Override
            protected void done() {
                try {
                    clienteActual = get();
                    mostrarFormulario();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(EditarClienteFrame.this,
                        "Error al cargar cliente: " + ex.getMessage(),
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
        JLabel sectionTitle = new JLabel("Editar Datos del Cliente");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sectionTitle.setForeground(ColorPalette.TEXTO_PRINCIPAL_NEGRO);
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(sectionTitle);
        formPanel.add(Box.createVerticalStrut(20));
        
        // Cédula (deshabilitado)
        formPanel.add(createFieldLabel("Cédula"));
        JTextField cedulaField = createTextField();
        cedulaField.setText(clienteActual.cedula);
        cedulaField.setEnabled(false);
        cedulaField.setBackground(ColorPalette.FONDO_DISABLED);
        formPanel.add(cedulaField);
        formPanel.add(Box.createVerticalStrut(15));
        
        // Nombre
        formPanel.add(createFieldLabel("Nombre Completo *"));
        nombreField = createTextField();
        nombreField.setText(clienteActual.nombre);
        formPanel.add(nombreField);
        formPanel.add(Box.createVerticalStrut(15));
        
        // Fecha Nacimiento
        formPanel.add(createFieldLabel("Fecha de Nacimiento (YYYY-MM-DD) *"));
        fechaField = createTextField();
        fechaField.setText(clienteActual.fechaNacimiento);
        formPanel.add(fechaField);
        formPanel.add(Box.createVerticalStrut(15));
        
        // Estado Civil
        formPanel.add(createFieldLabel("Estado Civil *"));
        String[] estadosCiviles = {"SOLTERO", "CASADO", "DIVORCIADO", "VIUDO"};
        estadoCivilCombo = new JComboBox<>(estadosCiviles);
        estadoCivilCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        estadoCivilCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        estadoCivilCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        estadoCivilCombo.setSelectedItem(clienteActual.estadoCivil);
        formPanel.add(estadoCivilCombo);
        formPanel.add(Box.createVerticalStrut(25));
        
        // Botón Actualizar
        actualizarBtn = new JButton("💾 Actualizar Cliente");
        actualizarBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        actualizarBtn.setForeground(Color.WHITE);
        actualizarBtn.setBackground(ColorPalette.AZUL_PRIMARIO_MEDIO);
        actualizarBtn.setFocusPainted(false);
        actualizarBtn.setBorderPainted(false);
        actualizarBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        actualizarBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        actualizarBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        actualizarBtn.addActionListener(e -> actualizarCliente());
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
    
    private void actualizarCliente() {
        String nombre = nombreField.getText().trim();
        String fecha = fechaField.getText().trim();
        String estadoCivil = (String) estadoCivilCombo.getSelectedItem();
        
        if (nombre.isEmpty() || fecha.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Por favor complete todos los campos obligatorios",
                "Error de Validación",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!fecha.matches("\\d{4}-\\d{2}-\\d{2}")) {
            JOptionPane.showMessageDialog(this,
                "La fecha debe estar en formato YYYY-MM-DD",
                "Error de Validación",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        actualizarBtn.setEnabled(false);
        actualizarBtn.setText("Actualizando...");
        
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                ClienteRequest request = new ClienteRequest();
                request.cedula = cedula;
                request.nombre = nombre;
                request.fechaNacimiento = fecha;
                request.estadoCivil = estadoCivil;
                
                clienteController.actualizarCliente(cedula, request);
                return null;
            }
            
            @Override
            protected void done() {
                try {
                    get();
                    JOptionPane.showMessageDialog(EditarClienteFrame.this,
                        "Cliente actualizado exitosamente",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                    new ClienteDetalleFrame(cedula).setVisible(true);
                    dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(EditarClienteFrame.this,
                        "Error al actualizar cliente: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    actualizarBtn.setEnabled(true);
                    actualizarBtn.setText("💾 Actualizar Cliente");
                }
            }
        };
        worker.execute();
    }
}
