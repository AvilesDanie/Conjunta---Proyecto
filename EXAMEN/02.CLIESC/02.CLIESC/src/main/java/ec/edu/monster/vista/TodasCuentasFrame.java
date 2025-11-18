package ec.edu.monster.vista;

import ec.edu.monster.controlador.CuentaController;
import ec.edu.monster.modelo.BanquitoModels.*;
import ec.edu.monster.util.ColorPalette;
import ec.edu.monster.util.ModernTableRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Pantalla que muestra todas las cuentas del sistema
 */
public class TodasCuentasFrame extends JFrame {
    
    private final CuentaController cuentaController;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private List<CuentaResponse> todasLasCuentas;
    
    public TodasCuentasFrame() {
        this.cuentaController = new CuentaController();
        initComponents();
        cargarCuentas();
    }
    
    private void initComponents() {
        setTitle("Todas las Cuentas - BanQuito");
        setSize(950, 650);
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
        
        JButton backBtn = new JButton("← Volver");
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backBtn.setForeground(Color.WHITE);
        backBtn.setBackground(new Color(255, 255, 255, 40));
        backBtn.setFocusPainted(false);
        backBtn.setBorderPainted(false);
        backBtn.setPreferredSize(new Dimension(110, 40));
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> {
            new HomeBanquitoFrame().setVisible(true);
            dispose();
        });
        
        JLabel titleLabel = new JLabel("Todas las Cuentas");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        topBar.add(backBtn, BorderLayout.WEST);
        topBar.add(titleLabel, BorderLayout.CENTER);
        
        return topBar;
    }
    
    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout(0, 15));
        contentPanel.setBackground(ColorPalette.FONDO_CLARO);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Barra de búsqueda
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchPanel.setBackground(ColorPalette.FONDO_CLARO);
        
        JLabel searchLabel = new JLabel("Buscar:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchLabel.setForeground(ColorPalette.TEXTO_PRINCIPAL_NEGRO);
        
        searchField = new JTextField(25);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.GRIS_BORDES, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        searchField.putClientProperty("JTextField.placeholderText", "Buscar por número de cuenta, cédula o tipo...");
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filtrarTabla();
            }
        });
        
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        
        contentPanel.add(searchPanel, BorderLayout.NORTH);
        
        // Tabla
        String[] columnNames = {"Número Cuenta", "Cédula Cliente", "Tipo", "Saldo", "Acciones"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Solo botón "Ver" es interactivo
            }
        };
        
        table = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                return c;
            }
        };
        
        // Estilo moderno web-like
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(60);
        
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        table.getTableHeader().setBackground(ColorPalette.AZUL_PRIMARIO);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setPreferredSize(new Dimension(0, 52));
        table.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(ColorPalette.AZUL_PRIMARIO);
                c.setForeground(Color.WHITE);
                c.setFont(new Font("Segoe UI", Font.BOLD, 15));
                ((JLabel)c).setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(25, 103, 210)),
                    BorderFactory.createEmptyBorder(8, 15, 8, 15)
                ));
                ((JLabel)c).setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        });
        table.getTableHeader().setBorder(BorderFactory.createEmptyBorder());
        
        // Configurar ancho de columnas
        table.getColumnModel().getColumn(0).setPreferredWidth(160);
        table.getColumnModel().getColumn(1).setPreferredWidth(140);
        table.getColumnModel().getColumn(2).setPreferredWidth(120);
        table.getColumnModel().getColumn(3).setPreferredWidth(130);
        table.getColumnModel().getColumn(4).setPreferredWidth(180);
        
        // Aplicar ModernTableRenderer a las primeras columnas
        ModernTableRenderer modernRenderer = new ModernTableRenderer();
        table.getColumnModel().getColumn(0).setCellRenderer(modernRenderer);
        table.getColumnModel().getColumn(1).setCellRenderer(modernRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(modernRenderer);
        
        // Renderizador personalizado para saldos
        table.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                setFont(new Font("Segoe UI", Font.BOLD, 15));
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 235, 242)),
                    BorderFactory.createEmptyBorder(12, 15, 12, 15)
                ));
                
                if (isSelected) {
                    setBackground(new Color(187, 224, 251));
                } else {
                    setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252));
                }
                
                if (value != null) {
                    try {
                        double saldo = Double.parseDouble(value.toString().replace("$", "").replace(",", ""));
                        setForeground(ColorPalette.getColorSaldo(saldo));
                    } catch (Exception e) {
                        setForeground(new Color(46, 125, 50));
                    }
                }
                setHorizontalAlignment(SwingConstants.RIGHT);
                setText(value != null ? value.toString() : "");
                return this;
            }
        });
        
        // Renderizador para botón "Ver Movimientos"
        table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 12));
                panel.setOpaque(true);
                
                if (isSelected) {
                    panel.setBackground(new Color(187, 224, 251));
                } else {
                    panel.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252));
                }
                
                JButton button = new JButton("Ver Movimientos") {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(getBackground());
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                        g2.dispose();
                        super.paintComponent(g);
                    }
                };
                button.setFont(new Font("Segoe UI", Font.BOLD, 13));
                button.setForeground(Color.WHITE);
                button.setBackground(ColorPalette.AZUL_PRIMARIO);
                button.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
                button.setFocusPainted(false);
                button.setContentAreaFilled(false);
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
                
                panel.add(button);
                return panel;
            }
        });
        
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.rowAtPoint(evt.getPoint());
                int col = table.columnAtPoint(evt.getPoint());
                if (row >= 0 && col == 4) { // Click en "Ver Movimientos"
                    String numCuenta = table.getValueAt(row, 0).toString();
                    new MovimientosPorCuentaFrame(numCuenta).setVisible(true);
                    dispose();
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.putClientProperty("FlatLaf.style", "arc: 12");
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 230), 1),
            BorderFactory.createEmptyBorder(2, 2, 2, 2)
        ));
        
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        return contentPanel;
    }
    
    private void cargarCuentas() {
        SwingWorker<List<CuentaResponse>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<CuentaResponse> doInBackground() throws Exception {
                return java.util.Arrays.asList(cuentaController.listarTodasLasCuentas());
            }
            
            @Override
            protected void done() {
                try {
                    todasLasCuentas = get();
                    actualizarTabla(todasLasCuentas);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(TodasCuentasFrame.this,
                        "Error al cargar cuentas: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }
    
    private void actualizarTabla(List<CuentaResponse> cuentas) {
        tableModel.setRowCount(0);
        for (CuentaResponse cuenta : cuentas) {
            tableModel.addRow(new Object[]{
                cuenta.numCuenta,
                cuenta.cedulaCliente,
                cuenta.tipoCuenta,
                String.format("$%.2f", cuenta.saldo),
                "Ver"
            });
        }
    }
    
    private void filtrarTabla() {
        String searchText = searchField.getText().toLowerCase().trim();
        if (searchText.isEmpty()) {
            actualizarTabla(todasLasCuentas);
        } else {
            List<CuentaResponse> filtradas = todasLasCuentas.stream()
                .filter(c -> (c.numCuenta != null && c.numCuenta.toLowerCase().contains(searchText)) ||
                           (c.cedulaCliente != null && c.cedulaCliente.toLowerCase().contains(searchText)) ||
                           (c.tipoCuenta != null && c.tipoCuenta.toLowerCase().contains(searchText)))
                .toList();
            actualizarTabla(filtradas);
        }
    }
}
