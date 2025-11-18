package ec.edu.monster.vista;

import ec.edu.monster.controlador.ClienteController;
import ec.edu.monster.modelo.BanquitoModels.ClienteOnlyResponse;
import ec.edu.monster.util.ColorPalette;

import javax.swing.*;
import java.awt.*;

/**
 * Pantalla de detalle de cliente
 */
public class ClienteDetalleFrame extends JFrame {
    
    private final ClienteController clienteController;
    private final String cedula;
    private ClienteOnlyResponse cliente;
    
    public ClienteDetalleFrame(String cedula) {
        this.clienteController = new ClienteController();
        this.cedula = cedula;
        initComponents();
        cargarCliente();
    }
    
    private void initComponents() {
        setTitle("Detalle del Cliente - BanQuito");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(ColorPalette.FONDO_CLARO);
        
        // TopBar
        JPanel topBar = createTopBar();
        mainPanel.add(topBar, BorderLayout.NORTH);
        
        // Agregar panel de carga
        JPanel loadingPanel = new JPanel(new GridBagLayout());
        loadingPanel.setBackground(ColorPalette.FONDO_CLARO);
        JLabel loadingLabel = new JLabel("Cargando...");
        loadingLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        loadingLabel.setForeground(ColorPalette.TEXTO_GRIS_MEDIO);
        loadingPanel.add(loadingLabel);
        mainPanel.add(loadingPanel, BorderLayout.CENTER);
        
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
            new ClientesFrame().setVisible(true);
            dispose();
        });
        
        JLabel titleLabel = new JLabel("Detalle del Cliente");
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
                    cliente = get();
                    mostrarDetalles();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(ClienteDetalleFrame.this,
                        "Error al cargar cliente: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }
    
    private void mostrarDetalles() {
        // Remover el panel de carga
        Container contentPane = getContentPane();
        Component[] components = contentPane.getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                JPanel panel = (JPanel) comp;
                // Remover el mainPanel que contiene el loading
                contentPane.remove(panel);
                break;
            }
        }
        
        // Crear nuevo mainPanel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(ColorPalette.FONDO_CLARO);
        
        // TopBar
        JPanel topBar = createTopBar();
        mainPanel.add(topBar, BorderLayout.NORTH);
        
        // Content
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(ColorPalette.FONDO_CLARO);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.putClientProperty("FlatLaf.style", "arc: 12");
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(66, 133, 244), 2),
            BorderFactory.createEmptyBorder(30, 35, 30, 35)
        ));
        card.setMaximumSize(new Dimension(650, Integer.MAX_VALUE));
        
        // Icono header
        JLabel iconLabel = new JLabel("👤", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 72));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(iconLabel);
        card.add(Box.createVerticalStrut(20));
        
        // Datos del cliente
        card.add(createInfoRow("", "Cédula", cliente.cedula));
        card.add(createDivider());
        card.add(createInfoRow("", "Nombre", cliente.nombre));
        card.add(createDivider());
        card.add(createInfoRow("", "Fecha de Nacimiento", cliente.fechaNacimiento));
        card.add(createDivider());
        card.add(createInfoRow("", "Estado Civil", cliente.estadoCivil));
        
        card.add(Box.createVerticalStrut(30));
        
        // Botones de acción
        JPanel buttonsPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        buttonsPanel.setOpaque(false);
        buttonsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        buttonsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JButton editarBtn = new JButton("Editar");
        editarBtn.putClientProperty("FlatLaf.style", "arc: 8; borderWidth: 2");
        editarBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        editarBtn.setForeground(ColorPalette.AZUL_PRIMARIO);
        editarBtn.setBackground(Color.WHITE);
        editarBtn.setFocusPainted(false);
        editarBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.AZUL_PRIMARIO, 2),
            BorderFactory.createEmptyBorder(10, 18, 10, 18)
        ));
        editarBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editarBtn.addActionListener(e -> {
            new EditarClienteFrame(cedula).setVisible(true);
            dispose();
        });
        
        JButton eliminarBtn = new JButton("Eliminar");
        eliminarBtn.putClientProperty("FlatLaf.style", "arc: 8; borderWidth: 2");
        eliminarBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        eliminarBtn.setForeground(ColorPalette.ROJO_ERROR);
        eliminarBtn.setBackground(Color.WHITE);
        eliminarBtn.setFocusPainted(false);
        eliminarBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.ROJO_ERROR, 2),
            BorderFactory.createEmptyBorder(10, 18, 10, 18)
        ));
        eliminarBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        eliminarBtn.addActionListener(e -> eliminarCliente());
        
        JButton verCuentasBtn = new JButton("Ver Cuentas");
        verCuentasBtn.putClientProperty("FlatLaf.style", "arc: 8; borderWidth: 0");
        verCuentasBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        verCuentasBtn.setForeground(Color.WHITE);
        verCuentasBtn.setBackground(ColorPalette.VERDE_EXITO);
        verCuentasBtn.setFocusPainted(false);
        verCuentasBtn.setBorderPainted(false);
        verCuentasBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        verCuentasBtn.addActionListener(e -> {
            new CuentasPorClienteFrame(cedula).setVisible(true);
            dispose();
        });
        
        buttonsPanel.add(editarBtn);
        buttonsPanel.add(eliminarBtn);
        buttonsPanel.add(verCuentasBtn);
        
        card.add(buttonsPanel);
        
        contentPanel.add(card);
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel);
        revalidate();
        repaint();
    }
    
    private JPanel createInfoRow(String emoji, String label, String value) {
        JPanel row = new JPanel(new BorderLayout(15, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel iconLabel = new JLabel(emoji);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        iconLabel.setPreferredSize(new Dimension(40, 40));
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        
        JLabel labelText = new JLabel(label);
        labelText.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        labelText.setForeground(new Color(100, 100, 100));
        
        JLabel valueText = new JLabel(value != null ? value : "N/A");
        valueText.setFont(new Font("Segoe UI", Font.BOLD, 17));
        valueText.setForeground(ColorPalette.TEXTO_PRINCIPAL_NEGRO);
        
        textPanel.add(labelText);
        textPanel.add(Box.createVerticalStrut(3));
        textPanel.add(valueText);
        
        row.add(iconLabel, BorderLayout.WEST);
        row.add(textPanel, BorderLayout.CENTER);
        
        return row;
    }
    
    private JPanel createDivider() {
        JPanel divider = new JPanel();
        divider.setBackground(ColorPalette.GRIS_BORDES);
        divider.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        divider.setAlignmentX(Component.LEFT_ALIGNMENT);
        return divider;
    }
    
    private void eliminarCliente() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de eliminar este cliente?\nEsta acción no se puede deshacer.",
            "Confirmar Eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            SwingWorker<Void, Void> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() throws Exception {
                    clienteController.eliminarCliente(cedula);
                    return null;
                }
                
                @Override
                protected void done() {
                    try {
                        get();
                        JOptionPane.showMessageDialog(ClienteDetalleFrame.this,
                            "Cliente eliminado exitosamente",
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                        new ClientesFrame().setVisible(true);
                        dispose();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(ClienteDetalleFrame.this,
                            "Error al eliminar cliente: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            };
            worker.execute();
        }
    }
}
