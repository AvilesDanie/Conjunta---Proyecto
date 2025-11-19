package ec.edu.monster.vista;

import ec.edu.monster.controlador.FacturaController;
import ec.edu.monster.modelo.ComercializadoraDTOs.*;
import ec.edu.monster.util.ColorPalette;
import ec.edu.monster.util.ToastNotification;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Pantalla de visualización de facturas
 */
public class FacturasFrame extends JFrame {
    
    private final FacturaController facturaController;
    private JTable table;
    private DefaultTableModel tableModel;
    private List<FacturaResponse> todasLasFacturas;
    private JTextField searchField;
    
    public FacturasFrame() {
        this.facturaController = new FacturaController();
        initComponents();
        cargarFacturas();
    }
    
    private void initComponents() {
        setTitle("Facturas - ElectroQuito");
        // Configurar pantalla completa y deshabilitar cambio de tamaño
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setResizable(false);
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
        topBar.setBackground(ColorPalette.NARANJA_ELECTRO);
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
            new HomeComercializadoraFrame().setVisible(true);
            dispose();
        });
        
        JLabel titleLabel = new JLabel("Facturas");
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
        
        // Búsqueda
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
        searchField.putClientProperty("JTextField.placeholderText", "Buscar por ID o forma de pago...");
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filtrarTabla();
            }
        });
        
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        
        contentPanel.add(searchPanel, BorderLayout.NORTH);
        
        // Tabla
        String[] columnNames = {"ID", "Fecha", "Forma Pago", "Total", "Descuento", "Total Final", "Acciones"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }
        };
        
        table = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isCellSelected(row, column)) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 248, 250));
                    if (column != 2 && column != 3 && column != 4 && column != 5 && column != 6) {
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
        table.getTableHeader().setBackground(ColorPalette.NARANJA_ELECTRO);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setPreferredSize(new Dimension(0, 45));
        table.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(ColorPalette.NARANJA_ELECTRO);
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
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(120);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(100);
        table.getColumnModel().getColumn(5).setPreferredWidth(120);
        table.getColumnModel().getColumn(6).setPreferredWidth(150);
        
        // Renderizador para forma de pago
        table.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 248, 250));
                }
                
                if (value != null) {
                    String forma = value.toString();
                    if ("EFECTIVO".equals(forma)) {
                        c.setForeground(ColorPalette.VERDE_EXITO);
                        setFont(new Font("Segoe UI", Font.BOLD, 13));
                    } else if ("CREDITO".equals(forma)) {
                        c.setForeground(ColorPalette.NARANJA_ELECTRO);
                        setFont(new Font("Segoe UI", Font.BOLD, 13));
                    }
                }
                setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        });
        
        // Renderizador para totales
        DefaultTableCellRenderer moneyRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 248, 250));
                }
                
                c.setForeground(ColorPalette.NARANJA_ELECTRO);
                setFont(new Font("Segoe UI", Font.BOLD, 14));
                setHorizontalAlignment(SwingConstants.RIGHT);
                return c;
            }
        };
        table.getColumnModel().getColumn(3).setCellRenderer(moneyRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(moneyRenderer);
        table.getColumnModel().getColumn(5).setCellRenderer(moneyRenderer);
        
        // Renderizador para acciones
        table.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JButton button = new JButton("Ver Detalle");
                button.setFont(new Font("Segoe UI", Font.BOLD, 12));
                button.setForeground(ColorPalette.NARANJA_ELECTRO);
                button.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 248, 250));
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ColorPalette.NARANJA_ELECTRO, 1),
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
                if (row >= 0 && col == 6) {
                    String id = table.getValueAt(row, 0).toString();
                    mostrarDetalle(Integer.parseInt(id));
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(ColorPalette.GRIS_BORDES, 1));
        
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        return contentPanel;
    }
    
    private void cargarFacturas() {
        SwingWorker<List<FacturaResponse>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<FacturaResponse> doInBackground() throws Exception {
                List<FacturaResponse> facturas = java.util.Arrays.asList(facturaController.listarFacturas());
                // Ordenar de forma descendente por ID (más recientes primero)
                facturas.sort((f1, f2) -> Long.compare(f2.id, f1.id));
                return facturas;
            }
            
            @Override
            protected void done() {
                try {
                    todasLasFacturas = get();
                    actualizarTabla(todasLasFacturas);
                } catch (Exception ex) {
                    ToastNotification.showToast(
                        FacturasFrame.this,
                        "Error al cargar facturas: " + ex.getMessage(),
                        ToastNotification.ERROR
                    );
                }
            }
        };
        worker.execute();
    }
    
    private void actualizarTabla(List<FacturaResponse> facturas) {
        tableModel.setRowCount(0);
        for (FacturaResponse factura : facturas) {
            tableModel.addRow(new Object[]{
                factura.id,
                factura.fecha,
                factura.formaPago,
                factura.totalBruto != null ? String.format("$%.2f", factura.totalBruto) : "$0,00",
                factura.descuento != null ? String.format("$%.2f", factura.descuento) : "$0,00",
                factura.totalNeto != null ? String.format("$%.2f", factura.totalNeto) : "$0,00",
                "Ver"
            });
        }
    }
    
    private void filtrarTabla() {
        String searchText = searchField.getText().toLowerCase().trim();
        if (searchText.isEmpty()) {
            actualizarTabla(todasLasFacturas);
        } else {
            List<FacturaResponse> filtradas = todasLasFacturas.stream()
                .filter(f -> (f.formaPago != null && f.formaPago.toLowerCase().contains(searchText)) ||
                           String.valueOf(f.id).contains(searchText))
                .toList();
            actualizarTabla(filtradas);
        }
    }
    
    private void mostrarDetalle(int idFactura) {
        new DetalleFacturaFrame(idFactura).setVisible(true);
    }
}

