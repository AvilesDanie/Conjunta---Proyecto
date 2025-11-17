package ec.edu.monster.vista;

import ec.edu.monster.controlador.ClienteController;
import ec.edu.monster.modelo.BanquitoModels.ClienteResponse;
import ec.edu.monster.util.ColorPalette;

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
        
        JButton newBtn = new JButton("➕ Nuevo Cliente");
        newBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        newBtn.setForeground(Color.WHITE);
        newBtn.setBackground(ColorPalette.VERDE_EXITO);
        newBtn.setFocusPainted(false);
        newBtn.setBorderPainted(false);
        newBtn.setPreferredSize(new Dimension(160, 40));
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
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.GRIS_BORDES, 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        JLabel searchIcon = new JLabel("");
        searchIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        
        searchField = new JTextField();
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        searchField.setOpaque(true);
        searchField.setBackground(Color.WHITE);
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filtrarClientes();
            }
        });
        
        JLabel placeholder = new JLabel("Buscar por nombre o cédula...");
        placeholder.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        placeholder.setForeground(ColorPalette.TEXTO_GRIS_MEDIO);
        
        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                placeholder.setVisible(false);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                placeholder.setVisible(searchField.getText().isEmpty());
            }
        });
        
        JPanel fieldPanel = new JPanel(new BorderLayout());
        fieldPanel.setOpaque(false);
        fieldPanel.add(searchField, BorderLayout.CENTER);
        
        JButton clearBtn = new JButton("✕");
        clearBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        clearBtn.setForeground(ColorPalette.TEXTO_GRIS_MEDIO);
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
        panel.add(fieldPanel, BorderLayout.CENTER);
        panel.add(clearBtn, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        
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
                
                // Filas alternadas para mejor legibilidad
                if (!isRowSelected(row)) {
                    if (row % 2 == 0) {
                        c.setBackground(Color.WHITE);
                    } else {
                        c.setBackground(new Color(245, 248, 250));
                    }
                } else {
                    c.setBackground(new Color(227, 242, 253));
                }
                
                // Texto más oscuro para mejor contraste
                if (!isRowSelected(row)) {
                    c.setForeground(new Color(33, 33, 33));
                }
                
                return c;
            }
        };
        
        tablaClientes.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tablaClientes.setRowHeight(50);
        tablaClientes.setShowGrid(true);
        tablaClientes.setGridColor(new Color(230, 230, 230));
        tablaClientes.setIntercellSpacing(new Dimension(1, 1));
        
        // Configurar header de la tabla
        tablaClientes.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tablaClientes.getTableHeader().setBackground(ColorPalette.AZUL_PRIMARIO);
        tablaClientes.getTableHeader().setForeground(Color.WHITE);
        tablaClientes.getTableHeader().setPreferredSize(new Dimension(0, 45));
        tablaClientes.getTableHeader().setBorder(BorderFactory.createEmptyBorder());
        tablaClientes.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(ColorPalette.AZUL_PRIMARIO);
                c.setForeground(Color.WHITE);
                c.setFont(new Font("Segoe UI", Font.BOLD, 14));
                ((JLabel)c).setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE),
                    BorderFactory.createEmptyBorder(5, 10, 5, 10)
                ));
                ((JLabel)c).setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        });
        
        tablaClientes.setSelectionBackground(new Color(227, 242, 253));
        tablaClientes.setSelectionForeground(new Color(33, 33, 33));
        
        // Configurar ancho de columnas
        tablaClientes.getColumnModel().getColumn(0).setPreferredWidth(110);
        tablaClientes.getColumnModel().getColumn(1).setPreferredWidth(220);
        tablaClientes.getColumnModel().getColumn(2).setPreferredWidth(110);
        tablaClientes.getColumnModel().getColumn(3).setPreferredWidth(120);
        tablaClientes.getColumnModel().getColumn(4).setPreferredWidth(120);
        tablaClientes.getColumnModel().getColumn(5).setPreferredWidth(140);
        
        // Botón Ver en la columna Acciones
        tablaClientes.getColumnModel().getColumn(5).setCellRenderer((table, value, isSelected, hasFocus, row, column) -> {
            JButton btn = new JButton("Ver Detalles");
            btn.setBackground(ColorPalette.AZUL_PRIMARIO);
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
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
        scrollPane.setBorder(null);
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
