package ec.edu.monster.vista;

import ec.edu.monster.controlador.CuentaController;
import ec.edu.monster.modelo.BanquitoModels.*;
import ec.edu.monster.util.ColorPalette;
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
        
        JButton crearBtn = new JButton("➕ Nueva Cuenta");
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
        
        table = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isCellSelected(row, column)) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 248, 250));
                    if (column != 2 && column != 3) {
                        c.setForeground(ColorPalette.TEXTO_PRINCIPAL_NEGRO);
                    }
                }
                return c;
            }
        };
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(50);
        table.setShowGrid(true);
        table.setGridColor(new Color(224, 224, 224));
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
        
        // Ajustar anchos de columna
        table.getColumnModel().getColumn(0).setPreferredWidth(150);
        table.getColumnModel().getColumn(1).setPreferredWidth(120);
        table.getColumnModel().getColumn(2).setPreferredWidth(120);
        table.getColumnModel().getColumn(3).setPreferredWidth(180);
        
        // Renderizador para saldo con colores
        table.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 248, 250));
                }
                
                if (value != null) {
                    try {
                        double saldo = Double.parseDouble(value.toString().replace("$", "").replace(",", ""));
                        c.setForeground(ColorPalette.getColorSaldo(saldo));
                        setFont(new Font("Segoe UI", Font.BOLD, 15));
                    } catch (Exception e) {
                        c.setForeground(ColorPalette.TEXTO_PRINCIPAL_NEGRO);
                    }
                }
                setHorizontalAlignment(SwingConstants.RIGHT);
                return c;
            }
        });
        
        // Renderizador para botón
        table.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JButton button = new JButton("Ver Movimientos");
                button.setFont(new Font("Segoe UI", Font.BOLD, 12));
                button.setForeground(ColorPalette.AZUL_PRIMARIO);
                button.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 248, 250));
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ColorPalette.AZUL_PRIMARIO, 1),
                    BorderFactory.createEmptyBorder(5, 10, 5, 10)
                ));
                button.setFocusPainted(false);
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
                return button;
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
        scrollPane.setBorder(BorderFactory.createLineBorder(ColorPalette.GRIS_BORDES, 1));
        
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
