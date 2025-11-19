package ec.edu.monster.vista;

import ec.edu.monster.controlador.CuentaController;
import ec.edu.monster.modelo.BanquitoModels.*;
import ec.edu.monster.util.ColorPalette;
import ec.edu.monster.util.ModernTableRenderer;
import ec.edu.monster.util.ToastNotification;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Pantalla que muestra todas las cuentas de un cliente específico
 */
public class CuentasPorClienteFrame extends JFrame {
    
    private final CuentaController cuentaController;
    private final String cedula;
    private JTable table;
    private DefaultTableModel tableModel;
    
    public CuentasPorClienteFrame(String cedula) {
        this.cuentaController = new CuentaController();
        this.cedula = cedula;
        initComponents();
        cargarCuentas();
    }
    
    private void initComponents() {
        setTitle("Cuentas del Cliente - BanQuito");
        setSize(850, 600);
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
        
        JLabel titleLabel = new JLabel("Cuentas del Cliente: " + cedula);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
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
        
        // Botón crear cuenta
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setBackground(ColorPalette.FONDO_CLARO);
        
        JButton crearBtn = new JButton("+ Nueva Cuenta");
        crearBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        crearBtn.setForeground(Color.WHITE);
        crearBtn.setBackground(ColorPalette.VERDE_EXITO);
        crearBtn.setFocusPainted(false);
        crearBtn.setBorderPainted(false);
        crearBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        crearBtn.addActionListener(e -> {
            new CrearCuentaFrame(cedula).setVisible(true);
            dispose();
        });
        
        actionPanel.add(crearBtn);
        contentPanel.add(actionPanel, BorderLayout.NORTH);
        
        // Tabla
        String[] columnNames = {"Número Cuenta", "Tipo", "Saldo", "Acciones"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };
        
        table = new JTable(tableModel);
        
        // Aplicar estilo moderno web-like
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.putClientProperty("FlatLaf.style", "rowHeight: 60; selectionInactiveBackground: #BBE0FB; selectionInactiveForeground: #0D3C6C");
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(60);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFillsViewportHeight(true);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(66, 133, 244));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setPreferredSize(new Dimension(0, 52));
        table.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = new JLabel(value.toString());
                label.setOpaque(true);
                label.setBackground(new Color(66, 133, 244));
                label.setForeground(Color.WHITE);
                label.setFont(new Font("Segoe UI", Font.BOLD, 15));
                label.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(25, 103, 210)),
                    BorderFactory.createEmptyBorder(8, 15, 8, 15)
                ));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                return label;
            }
        });
        
        // Ajustar anchos de columna
        table.getColumnModel().getColumn(0).setPreferredWidth(180);
        table.getColumnModel().getColumn(1).setPreferredWidth(140);
        table.getColumnModel().getColumn(2).setPreferredWidth(140);
        table.getColumnModel().getColumn(3).setPreferredWidth(180);
        
        // Aplicar ModernTableRenderer a columnas 0 y 1
        ModernTableRenderer modernRenderer = new ModernTableRenderer();
        table.getColumnModel().getColumn(0).setCellRenderer(modernRenderer);
        table.getColumnModel().getColumn(1).setCellRenderer(modernRenderer);
        
        // Renderizador especial para saldo con colores
        table.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
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
        
        // Renderizador para botón con diseño distintivo
        table.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 12));
                panel.setOpaque(true);
                
                if (isSelected) {
                    panel.setBackground(new Color(187, 224, 251));
                } else {
                    panel.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252));
                }
                
                JButton button = new JButton("Movimientos") {
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
                button.setBackground(new Color(66, 133, 244));
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
                if (row >= 0 && col == 3) {
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
                return java.util.Arrays.asList(cuentaController.listarCuentasPorCliente(cedula));
            }
            
            @Override
            protected void done() {
                try {
                    List<CuentaResponse> cuentas = get();
                    actualizarTabla(cuentas);
                } catch (Exception ex) {
                    ToastNotification.showToast(
                        CuentasPorClienteFrame.this,
                        "Error al cargar cuentas: " + ex.getMessage(),
                        ToastNotification.ERROR
                    );
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
                cuenta.tipoCuenta,
                String.format("$%.2f", cuenta.saldo),
                "Ver"
            });
        }
    }
}
