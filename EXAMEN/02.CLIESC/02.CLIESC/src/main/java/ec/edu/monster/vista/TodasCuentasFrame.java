package ec.edu.monster.vista;

import ec.edu.monster.controlador.CuentaController;
import ec.edu.monster.modelo.BanquitoModels.*;
import ec.edu.monster.util.ColorPalette;

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
                
                // Filas alternadas para mejor legibilidad
                if (!isRowSelected(row) && column != 4) {
                    if (row % 2 == 0) {
                        c.setBackground(Color.WHITE);
                    } else {
                        c.setBackground(new Color(245, 248, 250));
                    }
                } else if (isRowSelected(row) && column != 4) {
                    c.setBackground(new Color(227, 242, 253));
                }
                
                // Texto más oscuro para mejor contraste
                if (!isRowSelected(row) && column != 3) {
                    c.setForeground(new Color(33, 33, 33));
                }
                
                return c;
            }
        };
        
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(50);
        table.setShowGrid(true);
        table.setGridColor(new Color(230, 230, 230));
        table.setIntercellSpacing(new Dimension(1, 1));
        
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(ColorPalette.AZUL_PRIMARIO);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setPreferredSize(new Dimension(0, 45));
        table.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
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
        table.getTableHeader().setBorder(BorderFactory.createEmptyBorder());
        
        // Configurar ancho de columnas
        table.getColumnModel().getColumn(0).setPreferredWidth(140);
        table.getColumnModel().getColumn(1).setPreferredWidth(120);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(120);
        table.getColumnModel().getColumn(4).setPreferredWidth(150);
        
        // Renderizador personalizado para saldos
        table.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (value != null) {
                    try {
                        double saldo = Double.parseDouble(value.toString().replace("$", "").replace(",", ""));
                        c.setForeground(ColorPalette.getColorSaldo(saldo));
                        setFont(new Font("Segoe UI", Font.BOLD, 14));
                    } catch (Exception e) {
                        c.setForeground(new Color(33, 33, 33));
                    }
                }
                if (!isSelected && row % 2 == 0) {
                    c.setBackground(Color.WHITE);
                } else if (!isSelected) {
                    c.setBackground(new Color(245, 248, 250));
                }
                setHorizontalAlignment(SwingConstants.RIGHT);
                return c;
            }
        });
        
        // Renderizador para botón "Ver Movimientos"
        table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JButton button = new JButton("Ver Movimientos");
                button.setFont(new Font("Segoe UI", Font.BOLD, 13));
                button.setForeground(Color.WHITE);
                button.setBackground(ColorPalette.AZUL_PRIMARIO);
                button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                button.setFocusPainted(false);
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
                return button;
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
        scrollPane.setBorder(BorderFactory.createLineBorder(ColorPalette.GRIS_BORDES, 1));
        
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
