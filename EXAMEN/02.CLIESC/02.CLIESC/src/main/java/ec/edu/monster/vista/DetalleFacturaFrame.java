package ec.edu.monster.vista;

import ec.edu.monster.controlador.FacturaController;
import ec.edu.monster.modelo.ComercializadoraDTOs.*;
import ec.edu.monster.util.ColorPalette;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Pantalla para mostrar el detalle completo de una factura
 */
public class DetalleFacturaFrame extends JFrame {
    
    private final FacturaController facturaController;
    private final long idFactura;
    
    private JLabel facturaNumeroLabel, fechaLabel, formaPagoLabel;
    private JLabel totalBrutoLabel, descuentoLabel, totalFinalLabel;
    private JTable tablaDetalles;
    private DefaultTableModel tableModel;
    
    public DetalleFacturaFrame(long idFactura) {
        this.facturaController = new FacturaController();
        this.idFactura = idFactura;
        initComponents();
        cargarDatos();
    }
    
    private void initComponents() {
        setTitle("Detalle de Factura - ElectroQuito");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(ColorPalette.FONDO_CLARO);
        
        // TopBar
        JPanel topBar = createTopBar();
        mainPanel.add(topBar, BorderLayout.NORTH);
        
        // Content
        JScrollPane scrollPane = new JScrollPane(createContentPanel());
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(ColorPalette.NARANJA_ELECTRO);
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
        backBtn.addActionListener(e -> dispose());
        
        JLabel titleLabel = new JLabel("Detalle de Factura");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        topBar.add(backBtn, BorderLayout.WEST);
        topBar.add(titleLabel, BorderLayout.CENTER);
        
        return topBar;
    }
    
    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(ColorPalette.FONDO_CLARO);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel de Información General
        JPanel infoPanel = createInfoPanel();
        contentPanel.add(infoPanel);
        contentPanel.add(Box.createVerticalStrut(20));
        
        // Panel de Detalles (Tabla de Productos)
        JPanel detallesPanel = createDetallesPanel();
        contentPanel.add(detallesPanel);
        contentPanel.add(Box.createVerticalStrut(20));
        
        // Panel de Totales
        JPanel totalesPanel = createTotalesPanel();
        contentPanel.add(totalesPanel);
        
        return contentPanel;
    }
    
    private JPanel createInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.GRIS_BORDES, 1),
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        
        JLabel tituloInfo = new JLabel("Información de la Factura");
        tituloInfo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tituloInfo.setForeground(ColorPalette.AZUL_PRIMARIO);
        tituloInfo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(tituloInfo);
        panel.add(Box.createVerticalStrut(15));
        
        facturaNumeroLabel = createInfoLabel("Factura #: Cargando...");
        fechaLabel = createInfoLabel("Fecha: Cargando...");
        formaPagoLabel = createInfoLabel("Forma de Pago: Cargando...");
        
        panel.add(facturaNumeroLabel);
        panel.add(Box.createVerticalStrut(8));
        panel.add(fechaLabel);
        panel.add(Box.createVerticalStrut(8));
        panel.add(formaPagoLabel);
        
        return panel;
    }
    
    private JPanel createDetallesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.GRIS_BORDES, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));
        
        JLabel tableTitle = new JLabel("Productos");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tableTitle.setForeground(ColorPalette.AZUL_PRIMARIO);
        panel.add(tableTitle, BorderLayout.NORTH);
        
        String[] columnNames = {"Producto", "Cantidad", "Precio Unit.", "Subtotal"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaDetalles = new JTable(tableModel);
        tablaDetalles.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaDetalles.setRowHeight(45);
        tablaDetalles.setShowGrid(true);
        tablaDetalles.setGridColor(new Color(230, 230, 230));
        tablaDetalles.setSelectionBackground(new Color(227, 242, 253));
        
        // Header
        tablaDetalles.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaDetalles.getTableHeader().setBackground(ColorPalette.NARANJA_ELECTRO);
        tablaDetalles.getTableHeader().setForeground(Color.WHITE);
        tablaDetalles.getTableHeader().setPreferredSize(new Dimension(0, 40));
        tablaDetalles.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(ColorPalette.NARANJA_ELECTRO);
                c.setForeground(Color.WHITE);
                c.setFont(new Font("Segoe UI", Font.BOLD, 13));
                ((JLabel)c).setHorizontalAlignment(SwingConstants.CENTER);
                ((JLabel)c).setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                return c;
            }
        });
        
        // Alineación de columnas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        tablaDetalles.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        tablaDetalles.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
        tablaDetalles.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
        
        JScrollPane scrollPane = new JScrollPane(tablaDetalles);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createTotalesPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.GRIS_BORDES, 1),
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));
        
        JLabel tituloTotales = new JLabel("Resumen de Totales");
        tituloTotales.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tituloTotales.setForeground(ColorPalette.AZUL_PRIMARIO);
        tituloTotales.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(tituloTotales);
        panel.add(Box.createVerticalStrut(15));
        
        totalBrutoLabel = createTotalLabel("Total Bruto: $0.00");
        descuentoLabel = createTotalLabel("Descuento: $0.00");
        descuentoLabel.setForeground(ColorPalette.VERDE_EXITO);
        totalFinalLabel = createTotalLabel("TOTAL FINAL: $0.00");
        totalFinalLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        totalFinalLabel.setForeground(ColorPalette.NARANJA_ELECTRO);
        
        panel.add(totalBrutoLabel);
        panel.add(Box.createVerticalStrut(8));
        panel.add(descuentoLabel);
        panel.add(Box.createVerticalStrut(12));
        panel.add(totalFinalLabel);
        
        return panel;
    }
    
    private JLabel createInfoLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(ColorPalette.TEXTO_PRINCIPAL_NEGRO);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }
    
    private JLabel createTotalLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 15));
        label.setForeground(ColorPalette.TEXTO_PRINCIPAL_NEGRO);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }
    
    private void cargarDatos() {
        SwingWorker<FacturaResponse, Void> worker = new SwingWorker<>() {
            @Override
            protected FacturaResponse doInBackground() throws Exception {
                return facturaController.obtenerFactura(idFactura);
            }
            
            @Override
            protected void done() {
                try {
                    FacturaResponse factura = get();
                    
                    // Actualizar información general
                    facturaNumeroLabel.setText("Factura #: " + factura.id);
                    fechaLabel.setText("Fecha: " + factura.fecha);
                    formaPagoLabel.setText("Forma de Pago: " + factura.formaPago);
                    
                    // Agregar productos a la tabla
                    tableModel.setRowCount(0);
                    for (DetalleFacturaResponse item : factura.detalles) {
                        tableModel.addRow(new Object[]{
                            item.nombreElectro != null ? item.nombreElectro : "Producto",
                            item.cantidad,
                            String.format("$%.2f", item.precioUnitario != null ? item.precioUnitario : 0.0),
                            String.format("$%.2f", item.subtotal != null ? item.subtotal : 0.0)
                        });
                    }
                    
                    // Actualizar totales
                    totalBrutoLabel.setText(String.format("Total Bruto: $%.2f", 
                        factura.totalBruto != null ? factura.totalBruto : 0.0));
                    descuentoLabel.setText(String.format("Descuento: $%.2f", 
                        factura.descuento != null ? factura.descuento : 0.0));
                    totalFinalLabel.setText(String.format("TOTAL FINAL: $%.2f", 
                        factura.totalNeto != null ? factura.totalNeto : 0.0));
                    
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(DetalleFacturaFrame.this,
                        "Error al cargar la factura: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    dispose();
                }
            }
        };
        worker.execute();
    }
}
