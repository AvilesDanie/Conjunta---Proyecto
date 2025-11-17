package ec.edu.monster.vista;

import ec.edu.monster.controlador.FacturaController;
import ec.edu.monster.controlador.MovimientoController;
import ec.edu.monster.controlador.ElectrodomesticoController;
import ec.edu.monster.modelo.ComercializadoraModels.*;
import ec.edu.monster.modelo.BanquitoModels.*;
import ec.edu.monster.util.ColorPalette;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

/**
 * Pantalla de facturación con dos formas de pago:
 * - EFECTIVO: 33% descuento automático
 * - CREDITO: Integración con BanQuito para débito automático
 */
public class FacturarFrame extends JFrame {
    
    private final FacturaController facturaController;
    private final MovimientoController movimientoController;
    private final ElectrodomesticoController electroController;
    
    private JTextField cedulaField, nombreField;
    private JTextField plazoField, cuentaBanquitoField;
    private JComboBox<String> formaPagoCombo, electroCombo;
    private JSpinner cantidadSpinner;
    private JPanel creditoPanel;
    private JLabel totalBrutoLabel, descuentoLabel, totalNetoLabel;
    private JButton guardarBtn, agregarBtn;
    private JTable tablaProductos;
    private DefaultTableModel tableModel;
    
    private List<ElectrodomesticoResponse> listaElectros;
    private List<ProductoFactura> productosSeleccionados;
    
    // Clase interna para productos seleccionados
    private static class ProductoFactura {
        ElectrodomesticoResponse electro;
        int cantidad;
        double subtotal;
        
        ProductoFactura(ElectrodomesticoResponse electro, int cantidad) {
            this.electro = electro;
            this.cantidad = cantidad;
            this.subtotal = electro.precioVenta.doubleValue() * cantidad;
        }
    }
    
    public FacturarFrame() {
        this.facturaController = new FacturaController();
        this.movimientoController = new MovimientoController();
        this.electroController = new ElectrodomesticoController();
        this.productosSeleccionados = new ArrayList<>();
        initComponents();
        cargarElectrodomesticos();
    }
    
    private void initComponents() {
        setTitle("Facturar - ElectroQuito");
        setSize(1000, 750);
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
        topBar.setBackground(new Color(255, 87, 34));
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
            new HomeComercializadoraFrame().setVisible(true);
            dispose();
        });
        
        JLabel titleLabel = new JLabel("Nueva Factura");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        topBar.add(backBtn, BorderLayout.WEST);
        topBar.add(titleLabel, BorderLayout.CENTER);
        
        return topBar;
    }
    
    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout(15, 0));
        contentPanel.setBackground(ColorPalette.FONDO_CLARO);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel Izquierdo - Formulario
        JPanel leftPanel = createFormPanel();
        
        // Panel Derecho - Productos y Totales
        JPanel rightPanel = createProductosPanel();
        
        contentPanel.add(leftPanel, BorderLayout.WEST);
        contentPanel.add(rightPanel, BorderLayout.CENTER);
        
        return contentPanel;
    }
    
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.GRIS_BORDES, 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        formPanel.setPreferredSize(new Dimension(380, 0));
        
        // Datos del Cliente
        JLabel clienteTitulo = createSectionTitle("Datos del Cliente");
        formPanel.add(clienteTitulo);
        formPanel.add(Box.createVerticalStrut(12));
        
        formPanel.add(createFieldLabel("Cédula:"));
        cedulaField = createTextField();
        formPanel.add(cedulaField);
        formPanel.add(Box.createVerticalStrut(10));
        
        formPanel.add(createFieldLabel("Nombre Completo:"));
        nombreField = createTextField();
        formPanel.add(nombreField);
        formPanel.add(Box.createVerticalStrut(20));
        
        // Selección de Producto
        JLabel productoTitulo = createSectionTitle("Agregar Producto");
        formPanel.add(productoTitulo);
        formPanel.add(Box.createVerticalStrut(12));
        
        formPanel.add(createFieldLabel("Electrodoméstico:"));
        electroCombo = new JComboBox<>();
        electroCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        electroCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        formPanel.add(electroCombo);
        formPanel.add(Box.createVerticalStrut(10));
        
        formPanel.add(createFieldLabel("Cantidad:"));
        cantidadSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
        cantidadSpinner.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cantidadSpinner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        ((JSpinner.DefaultEditor) cantidadSpinner.getEditor()).getTextField().setHorizontalAlignment(JTextField.CENTER);
        formPanel.add(cantidadSpinner);
        formPanel.add(Box.createVerticalStrut(12));
        
        agregarBtn = new JButton("+ Agregar a la Factura");
        agregarBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        agregarBtn.setForeground(Color.WHITE);
        agregarBtn.setBackground(ColorPalette.AZUL_PRIMARIO);
        agregarBtn.setFocusPainted(false);
        agregarBtn.setBorderPainted(false);
        agregarBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        agregarBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        agregarBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        agregarBtn.addActionListener(e -> agregarProducto());
        formPanel.add(agregarBtn);
        formPanel.add(Box.createVerticalStrut(20));
        
        // Forma de Pago
        JLabel pagoTitulo = createSectionTitle("Forma de Pago");
        formPanel.add(pagoTitulo);
        formPanel.add(Box.createVerticalStrut(12));
        
        formPanel.add(createFieldLabel("Forma de Pago:"));
        String[] formasPago = {"EFECTIVO", "CREDITO"};
        formaPagoCombo = new JComboBox<>(formasPago);
        formaPagoCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formaPagoCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        formaPagoCombo.addActionListener(e -> {
            toggleCreditoPanel();
            actualizarTotales();
        });
        formPanel.add(formaPagoCombo);
        formPanel.add(Box.createVerticalStrut(12));
        
        // Panel Crédito
        creditoPanel = new JPanel();
        creditoPanel.setLayout(new BoxLayout(creditoPanel, BoxLayout.Y_AXIS));
        creditoPanel.setBackground(new Color(227, 242, 253));
        creditoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.AZUL_PRIMARIO, 1),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        creditoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        creditoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel creditoInfo = new JLabel("Pago a crédito con BanQuito");
        creditoInfo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        creditoInfo.setForeground(ColorPalette.AZUL_PRIMARIO);
        creditoInfo.setAlignmentX(Component.LEFT_ALIGNMENT);
        creditoPanel.add(creditoInfo);
        creditoPanel.add(Box.createVerticalStrut(8));
        
        creditoPanel.add(createFieldLabel("Plazo (meses):"));
        plazoField = createTextField();
        plazoField.setText("12");
        creditoPanel.add(plazoField);
        creditoPanel.add(Box.createVerticalStrut(8));
        
        creditoPanel.add(createFieldLabel("Cuenta BanQuito:"));
        cuentaBanquitoField = createTextField();
        creditoPanel.add(cuentaBanquitoField);
        
        creditoPanel.setVisible(false);
        formPanel.add(creditoPanel);
        
        return formPanel;
    }
    
    private JPanel createProductosPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(ColorPalette.FONDO_CLARO);
        
        // Tabla de productos
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.GRIS_BORDES, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel tableTitle = new JLabel("Productos en la Factura");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tableTitle.setForeground(ColorPalette.TEXTO_PRINCIPAL_NEGRO);
        tablePanel.add(tableTitle, BorderLayout.NORTH);
        
        String[] columnNames = {"Producto", "Precio Unit.", "Cantidad", "Subtotal", ""};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Solo el botón eliminar
            }
        };
        
        tablaProductos = new JTable(tableModel);
        tablaProductos.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaProductos.setRowHeight(45);
        tablaProductos.setShowGrid(true);
        tablaProductos.setGridColor(new Color(230, 230, 230));
        tablaProductos.setSelectionBackground(new Color(227, 242, 253));
        
        // Configurar anchos de columnas
        tablaProductos.getColumnModel().getColumn(0).setPreferredWidth(200);
        tablaProductos.getColumnModel().getColumn(1).setPreferredWidth(100);
        tablaProductos.getColumnModel().getColumn(2).setPreferredWidth(80);
        tablaProductos.getColumnModel().getColumn(3).setPreferredWidth(100);
        tablaProductos.getColumnModel().getColumn(4).setPreferredWidth(80);
        
        // Header
        tablaProductos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaProductos.getTableHeader().setBackground(ColorPalette.NARANJA_ELECTRO);
        tablaProductos.getTableHeader().setForeground(Color.WHITE);
        tablaProductos.getTableHeader().setPreferredSize(new Dimension(0, 40));
        tablaProductos.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
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
        
        // Renderizador para alineación
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        tablaProductos.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        tablaProductos.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
        tablaProductos.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
        
        // Botón eliminar en cada fila
        tablaProductos.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JButton btn = new JButton("Eliminar");
                btn.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                btn.setForeground(ColorPalette.ROJO_ERROR);
                btn.setBackground(Color.WHITE);
                btn.setBorder(BorderFactory.createLineBorder(ColorPalette.ROJO_ERROR, 1));
                btn.setFocusPainted(false);
                btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                return btn;
            }
        });
        
        tablaProductos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int column = tablaProductos.columnAtPoint(evt.getPoint());
                int row = tablaProductos.rowAtPoint(evt.getPoint());
                if (column == 4 && row >= 0) {
                    eliminarProducto(row);
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tablaProductos);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Panel de totales
        JPanel totalesPanel = new JPanel();
        totalesPanel.setLayout(new BoxLayout(totalesPanel, BoxLayout.Y_AXIS));
        totalesPanel.setBackground(Color.WHITE);
        totalesPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.GRIS_BORDES, 1),
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));
        
        totalBrutoLabel = createTotalLabel("Total Bruto: $0.00");
        descuentoLabel = createTotalLabel("Descuento (33%): $0.00");
        descuentoLabel.setForeground(ColorPalette.VERDE_EXITO);
        totalNetoLabel = createTotalLabel("TOTAL A PAGAR: $0.00");
        totalNetoLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        totalNetoLabel.setForeground(ColorPalette.VERDE_EXITO);
        
        totalesPanel.add(totalBrutoLabel);
        totalesPanel.add(Box.createVerticalStrut(8));
        totalesPanel.add(descuentoLabel);
        totalesPanel.add(Box.createVerticalStrut(12));
        totalesPanel.add(totalNetoLabel);
        totalesPanel.add(Box.createVerticalStrut(20));
        
        guardarBtn = new JButton("Generar Factura");
        guardarBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        guardarBtn.setForeground(Color.WHITE);
        guardarBtn.setBackground(ColorPalette.VERDE_EXITO);
        guardarBtn.setFocusPainted(false);
        guardarBtn.setBorderPainted(false);
        guardarBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        guardarBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        guardarBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        guardarBtn.addActionListener(e -> generarFactura());
        totalesPanel.add(guardarBtn);
        
        panel.add(tablePanel, BorderLayout.CENTER);
        panel.add(totalesPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JLabel createSectionTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setForeground(ColorPalette.AZUL_PRIMARIO);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }
    
    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(ColorPalette.TEXTO_PRINCIPAL_NEGRO);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }
    
    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.GRIS_BORDES, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        return field;
    }
    
    private JLabel createTotalLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 15));
        label.setForeground(ColorPalette.TEXTO_PRINCIPAL_NEGRO);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }
    
    private void cargarElectrodomesticos() {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                listaElectros = java.util.Arrays.asList(electroController.listar());
                SwingUtilities.invokeLater(() -> {
                    electroCombo.removeAllItems();
                    for (ElectrodomesticoResponse e : listaElectros) {
                        electroCombo.addItem(e.nombre + " - $" + e.precioVenta);
                    }
                });
                return null;
            }
            
            @Override
            protected void done() {
                try {
                    get();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(FacturarFrame.this,
                        "Error al cargar electrodomésticos: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }
    
    private void toggleCreditoPanel() {
        String formaPago = (String) formaPagoCombo.getSelectedItem();
        creditoPanel.setVisible("CREDITO".equals(formaPago));
        revalidate();
        repaint();
    }
    
    private void agregarProducto() {
        int selectedIndex = electroCombo.getSelectedIndex();
        if (selectedIndex < 0 || listaElectros == null || listaElectros.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Seleccione un electrodoméstico",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        ElectrodomesticoResponse electro = listaElectros.get(selectedIndex);
        int cantidad = (Integer) cantidadSpinner.getValue();
        
        // Verificar si el producto ya está en la lista
        for (ProductoFactura pf : productosSeleccionados) {
            if (pf.electro.id.equals(electro.id)) {
                JOptionPane.showMessageDialog(this,
                    "Este producto ya está en la factura.\nElimínelo primero si desea cambiar la cantidad.",
                    "Producto Duplicado",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        
        ProductoFactura producto = new ProductoFactura(electro, cantidad);
        productosSeleccionados.add(producto);
        
        // Agregar a la tabla
        tableModel.addRow(new Object[]{
            electro.nombre,
            String.format("$%.2f", electro.precioVenta),
            cantidad,
            String.format("$%.2f", producto.subtotal),
            "Eliminar"
        });
        
        actualizarTotales();
        cantidadSpinner.setValue(1);
    }
    
    private void eliminarProducto(int row) {
        if (row >= 0 && row < productosSeleccionados.size()) {
            productosSeleccionados.remove(row);
            tableModel.removeRow(row);
            actualizarTotales();
        }
    }
    
    private void actualizarTotales() {
        if (productosSeleccionados.isEmpty()) {
            totalBrutoLabel.setText("Total Bruto: $0.00");
            descuentoLabel.setText("Descuento (33%): $0.00");
            totalNetoLabel.setText("TOTAL A PAGAR: $0.00");
            return;
        }
        
        double totalBruto = 0.0;
        for (ProductoFactura pf : productosSeleccionados) {
            totalBruto += pf.subtotal;
        }
        
        String formaPago = (String) formaPagoCombo.getSelectedItem();
        double descuento = "EFECTIVO".equals(formaPago) ? totalBruto * 0.33 : 0.0;
        double totalNeto = totalBruto - descuento;
        
        totalBrutoLabel.setText(String.format("Total Bruto: $%.2f", totalBruto));
        descuentoLabel.setText(String.format("Descuento (33%%): $%.2f", descuento));
        totalNetoLabel.setText(String.format("TOTAL A PAGAR: $%.2f", totalNeto));
    }
    
    private void generarFactura() {
        String cedula = cedulaField.getText().trim();
        String nombre = nombreField.getText().trim();
        
        if (cedula.isEmpty() || nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Complete los datos del cliente",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (productosSeleccionados.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Agregue al menos un producto a la factura",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String formaPago = (String) formaPagoCombo.getSelectedItem();
        
        if ("CREDITO".equals(formaPago)) {
            String cuenta = cuentaBanquitoField.getText().trim();
            String plazoStr = plazoField.getText().trim();
            
            if (cuenta.isEmpty() || plazoStr.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Complete los datos de crédito (cuenta y plazo)",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        guardarBtn.setEnabled(false);
        guardarBtn.setText("Generando...");
        
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                // Nota: La API actual solo acepta UN producto por factura
                // Si hay múltiples productos, se crea una factura por cada uno
                for (ProductoFactura pf : productosSeleccionados) {
                    FacturaRequest request = new FacturaRequest();
                    request.cedulaCliente = cedula;
                    request.nombreCliente = nombre;
                    request.idElectrodomestico = pf.electro.id;
                    request.cantidad = pf.cantidad;
                    request.formaPago = formaPago;
                    
                    if ("CREDITO".equals(formaPago)) {
                        request.plazoMeses = Integer.parseInt(plazoField.getText().trim());
                        request.numCuentaCredito = cuentaBanquitoField.getText().trim();
                    }
                    
                    facturaController.crearFactura(request);
                }
                
                // Si es CREDITO, registrar retiro total en BanQuito
                if ("CREDITO".equals(formaPago)) {
                    double totalBruto = 0.0;
                    for (ProductoFactura pf : productosSeleccionados) {
                        totalBruto += pf.subtotal;
                    }
                    
                    MovimientoRequest movReq = new MovimientoRequest();
                    movReq.numCuenta = cuentaBanquitoField.getText().trim();
                    movReq.tipo = "RETIRO";
                    movReq.valor = new BigDecimal(totalBruto);
                    
                    movimientoController.registrarMovimiento(movReq);
                }
                
                return null;
            }
            
            @Override
            protected void done() {
                try {
                    get();
                    int numProductos = productosSeleccionados.size();
                    JOptionPane.showMessageDialog(FacturarFrame.this,
                        "Factura(s) generada(s) exitosamente\n" + numProductos + " producto(s) facturado(s)",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                    new FacturasFrame().setVisible(true);
                    dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(FacturarFrame.this,
                        "Error al generar factura: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    guardarBtn.setEnabled(true);
                    guardarBtn.setText("Generar Factura");
                }
            }
        };
        worker.execute();
    }
}
