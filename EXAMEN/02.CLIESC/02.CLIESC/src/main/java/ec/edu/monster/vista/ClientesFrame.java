package ec.edu.monster.vista;

import ec.edu.monster.controlador.ClienteController;
import ec.edu.monster.modelo.BanquitoModels.ClienteResponse;
import ec.edu.monster.util.ColorPalette;
import ec.edu.monster.util.ModernTableRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Pantalla de lista de clientes con buscador
 */
public class ClientesFrame extends JFrame {
    
    private final ClienteController clienteController;
    private JTable tablaClientes;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private List<ClienteResponse> todosClientes;
    
    public ClientesFrame() {
        this.clienteController = new ClienteController();
        this.todosClientes = new ArrayList<>();
        initComponents();
        cargarClientes();
    }
    
    private void initComponents() {
        setTitle("Lista de Clientes - BanQuito");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(ColorPalette.FONDO_CLARO);
        
        // TopBar
        JPanel topBar = createTopBar();
        mainPanel.add(topBar, BorderLayout.NORTH);
        
        // Panel de contenido
        JPanel contentPanel = new JPanel(new BorderLayout(0, 15));
        contentPanel.setBackground(ColorPalette.FONDO_CLARO);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Buscador
        JPanel searchPanel = createSearchPanel();
        contentPanel.add(searchPanel, BorderLayout.NORTH);
        
        // Tabla de clientes
        JPanel tablePanel = createTablePanel();
        contentPanel.add(tablePanel, BorderLayout.CENTER);
        
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
        backBtn.setPreferredSize(new Dimension(110, 40));
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> {
            new HomeBanquitoFrame().setVisible(true);
            dispose();
        });
        
        JLabel titleLabel = new JLabel("Lista de Clientes");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JButton newBtn = new JButton("+ Nuevo Cliente");
        newBtn.putClientProperty("FlatLaf.style", "arc: 10; borderWidth: 0; font: bold +0");
        newBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        newBtn.setForeground(Color.WHITE);
        newBtn.setBackground(ColorPalette.VERDE_EXITO);
        newBtn.setFocusPainted(false);
        newBtn.setBorderPainted(false);
        newBtn.setPreferredSize(new Dimension(160, 42));
        newBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        newBtn.addActionListener(e -> {
            new CrearClienteFrame().setVisible(true);
            dispose();
        });
        
        topBar.add(backBtn, BorderLayout.WEST);
        topBar.add(titleLabel, BorderLayout.CENTER);
        topBar.add(newBtn, BorderLayout.EAST);
        
        return topBar;
    }
    
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 0));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(66, 133, 244), 2),
            BorderFactory.createEmptyBorder(12, 18, 12, 18)
        ));
        
        // Icono de búsqueda más visible
        JLabel searchIcon = new JLabel("🔍");
        searchIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        
        searchField = new JTextField();
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        searchField.putClientProperty("JTextField.placeholderText", "Buscar por nombre o cédula...");
        searchField.putClientProperty("JTextField.showClearButton", true);
        searchField.putClientProperty("JTextField.leadingIcon", new javax.swing.ImageIcon(new java.awt.image.BufferedImage(20, 20, java.awt.image.BufferedImage.TYPE_INT_ARGB)));
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filtrarClientes();
            }
        });
        
        JButton clearBtn = new JButton("✕");
        clearBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        clearBtn.setForeground(new Color(150, 150, 150));
        clearBtn.setFocusPainted(false);
        clearBtn.setBorderPainted(false);
        clearBtn.setContentAreaFilled(false);
        clearBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        clearBtn.setVisible(false);
        clearBtn.addActionListener(e -> {
            searchField.setText("");
            filtrarClientes();
        });
        
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                clearBtn.setVisible(!searchField.getText().isEmpty());
                filtrarClientes();
            }
        });
        
        panel.add(searchIcon, BorderLayout.WEST);
        panel.add(searchField, BorderLayout.CENTER);
        panel.add(clearBtn, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(66, 133, 244), 1),
            BorderFactory.createEmptyBorder(1, 1, 1, 1)
        ));
        
        String[] columnNames = {"Cédula", "Nombre", "Fecha Nac.", "Estado Civil", "Tipo Cuenta", "Acciones"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Solo la columna de acciones
            }
        };
        
        tablaClientes = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                
                // Filas alternadas con colores más vibrantes
                if (!isRowSelected(row)) {
                    if (row % 2 == 0) {
                        c.setBackground(new Color(248, 250, 252));
                    } else {
                        c.setBackground(new Color(237, 242, 250));
                    }
                } else {
                    c.setBackground(new Color(187, 222, 251));
                }
                
                // Texto más oscuro para mejor contraste
                if (!isRowSelected(row)) {
                    c.setForeground(new Color(33, 33, 33));
                } else {
                    c.setForeground(new Color(13, 60, 108));
                }
                
                return c;
            }
        };
        
        tablaClientes.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tablaClientes.setRowHeight(55);
        tablaClientes.setShowGrid(true);
        tablaClientes.setGridColor(new Color(200, 215, 230));
        tablaClientes.setIntercellSpacing(new Dimension(1, 1));
        
        // Configurar header de la tabla con degradado visual
        tablaClientes.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tablaClientes.getTableHeader().setBackground(new Color(66, 133, 244));
        tablaClientes.getTableHeader().setForeground(Color.WHITE);
        tablaClientes.getTableHeader().setPreferredSize(new Dimension(0, 52));
        tablaClientes.getTableHeader().setBorder(BorderFactory.createEmptyBorder());
        tablaClientes.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = new JLabel(value.toString());
                label.setOpaque(true);
                label.setBackground(new Color(66, 133, 244));
                label.setForeground(Color.WHITE);
                label.setFont(new Font("Segoe UI", Font.BOLD, 14));
                label.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(25, 103, 210)),
                    BorderFactory.createEmptyBorder(8, 15, 8, 15)
                ));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                return label;
            }
        });
        
        tablaClientes.setSelectionBackground(new Color(187, 222, 251));
        tablaClientes.setSelectionForeground(new Color(13, 60, 108));
        
        // Configurar ancho de columnas
        tablaClientes.getColumnModel().getColumn(0).setPreferredWidth(110);
        tablaClientes.getColumnModel().getColumn(1).setPreferredWidth(220);
        tablaClientes.getColumnModel().getColumn(2).setPreferredWidth(110);
        tablaClientes.getColumnModel().getColumn(3).setPreferredWidth(120);
        tablaClientes.getColumnModel().getColumn(4).setPreferredWidth(120);
        tablaClientes.getColumnModel().getColumn(5).setPreferredWidth(140);
        
        // Botón Ver en la columna Acciones con estilo mejorado
        tablaClientes.getColumnModel().getColumn(5).setCellRenderer((table, value, isSelected, hasFocus, row, column) -> {
            JButton btn = new JButton("Ver Detalles");
            btn.putClientProperty("FlatLaf.style", "arc: 8; borderWidth: 0");
            btn.setBackground(new Color(66, 133, 244));
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            return btn;
        });
        
        tablaClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = tablaClientes.rowAtPoint(evt.getPoint());
                int col = tablaClientes.columnAtPoint(evt.getPoint());
                if (col == 5 && row >= 0) {
                    String cedula = (String) tableModel.getValueAt(row, 0);
                    new ClienteDetalleFrame(cedula).setVisible(true);
                    dispose();
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tablaClientes);
        scrollPane.putClientProperty("FlatLaf.style", "arc: 12");
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 230), 1),
            BorderFactory.createEmptyBorder(2, 2, 2, 2)
        ));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void cargarClientes() {
        SwingWorker<ClienteResponse[], Void> worker = new SwingWorker<>() {
            @Override
            protected ClienteResponse[] doInBackground() throws Exception {
                return clienteController.listarClientes();
            }
            
            @Override
            protected void done() {
                try {
                    ClienteResponse[] clientes = get();
                    todosClientes.clear();
                    for (ClienteResponse cliente : clientes) {
                        todosClientes.add(cliente);
                    }
                    actualizarTabla(todosClientes);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(ClientesFrame.this,
                        "Error al cargar clientes: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }
    
    private void filtrarClientes() {
        String searchText = searchField.getText().toLowerCase().trim();
        
        if (searchText.isEmpty()) {
            actualizarTabla(todosClientes);
            return;
        }
        
        List<ClienteResponse> filtrados = new ArrayList<>();
        for (ClienteResponse cliente : todosClientes) {
            if ((cliente.nombre != null && cliente.nombre.toLowerCase().contains(searchText)) ||
                (cliente.cedula != null && cliente.cedula.contains(searchText))) {
                filtrados.add(cliente);
            }
        }
        
        actualizarTabla(filtrados);
    }
    
    private void actualizarTabla(List<ClienteResponse> clientes) {
        tableModel.setRowCount(0);
        
        for (ClienteResponse cliente : clientes) {
            Object[] row = {
                cliente.cedula,
                cliente.nombre,
                cliente.fechaNacimiento,
                cliente.estadoCivil,
                cliente.tipoCuentaInicial != null ? cliente.tipoCuentaInicial : "N/A",
                "Ver"
            };
            tableModel.addRow(row);
        }
    }
}
