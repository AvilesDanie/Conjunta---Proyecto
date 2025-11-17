package ec.edu.monster.vista;

import ec.edu.monster.controlador.MovimientoController;
import ec.edu.monster.modelo.BanquitoModels.*;
import ec.edu.monster.util.ColorPalette;
import ec.edu.monster.util.ToastNotification;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Pantalla que muestra todos los movimientos de una cuenta específica
 */
public class MovimientosPorCuentaFrame extends JFrame {
    
    private final MovimientoController movimientoController;
    private final String numCuenta;
    private JTable table;
    private DefaultTableModel tableModel;
    
    public MovimientosPorCuentaFrame(String numCuenta) {
        this.movimientoController = new MovimientoController();
        this.numCuenta = numCuenta;
        initComponents();
        cargarMovimientos();
    }
    
    private void initComponents() {
        setTitle("Movimientos de Cuenta - BanQuito");
        setSize(1000, 650);
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
            new TodasCuentasFrame().setVisible(true);
            dispose();
        });
        
        JLabel titleLabel = new JLabel("Movimientos - Cuenta: " + numCuenta);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
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
        
        // Botón registrar movimiento
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setBackground(ColorPalette.FONDO_CLARO);
        
        JButton registrarBtn = new JButton("➕ Registrar Movimiento");
        registrarBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        registrarBtn.setForeground(Color.WHITE);
        registrarBtn.setBackground(ColorPalette.VERDE_EXITO);
        registrarBtn.setFocusPainted(false);
        registrarBtn.setBorderPainted(false);
        registrarBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registrarBtn.addActionListener(e -> {
            new RegistrarMovimientoFrame(numCuenta).setVisible(true);
            dispose();
        });
        
        actionPanel.add(registrarBtn);
        contentPanel.add(actionPanel, BorderLayout.NORTH);
        
        // Tabla
        String[] columnNames = {"ID", "Fecha", "Tipo", "Naturaleza", "Valor"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isCellSelected(row, column)) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 248, 250));
                    if (column != 2 && column != 3 && column != 4) {
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
        
        // Ajustar anchos de columna - ahora son 5 columnas: ID, Fecha, Tipo, Naturaleza, Valor
        table.getColumnModel().getColumn(0).setPreferredWidth(50);   // ID
        table.getColumnModel().getColumn(1).setPreferredWidth(120);  // Fecha
        table.getColumnModel().getColumn(2).setPreferredWidth(120);  // Tipo
        table.getColumnModel().getColumn(3).setPreferredWidth(120);  // Naturaleza
        table.getColumnModel().getColumn(4).setPreferredWidth(120);  // Valor
        
        // Renderizador para tipo de movimiento
        table.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 248, 250));
                }
                
                if (value != null) {
                    String tipo = value.toString();
                    if ("DEPOSITO".equals(tipo)) {
                        c.setForeground(ColorPalette.VERDE_EXITO);
                        setFont(new Font("Segoe UI", Font.BOLD, 13));
                    } else if ("RETIRO".equals(tipo)) {
                        c.setForeground(ColorPalette.ROJO_ERROR);
                        setFont(new Font("Segoe UI", Font.BOLD, 13));
                    } else {
                        c.setForeground(ColorPalette.TEXTO_PRINCIPAL_NEGRO);
                    }
                }
                setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        });
        
        // Renderizador para valor
        table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 248, 250));
                }
                
                Object tipoObj = table.getValueAt(row, 2);
                if (tipoObj != null) {
                    String tipo = tipoObj.toString();
                    if ("DEPOSITO".equals(tipo)) {
                        c.setForeground(ColorPalette.VERDE_EXITO);
                        setFont(new Font("Segoe UI", Font.BOLD, 14));
                    } else if ("RETIRO".equals(tipo) || "TRANSFERENCIA".equals(tipo)) {
                        c.setForeground(ColorPalette.ROJO_ERROR);
                        setFont(new Font("Segoe UI", Font.BOLD, 14));
                    }
                }
                setHorizontalAlignment(SwingConstants.RIGHT);
                return c;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(ColorPalette.GRIS_BORDES, 1));
        
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        return contentPanel;
    }
    
    private void cargarMovimientos() {
        SwingWorker<List<MovimientoResponse>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<MovimientoResponse> doInBackground() throws Exception {
                return java.util.Arrays.asList(movimientoController.listarMovimientosPorCuenta(numCuenta));
            }
            
            @Override
            protected void done() {
                try {
                    List<MovimientoResponse> movimientos = get();
                    actualizarTabla(movimientos);
                } catch (Exception ex) {
                    ToastNotification.showToast(
                        MovimientosPorCuentaFrame.this,
                        "Error al cargar movimientos: " + ex.getMessage(),
                        ToastNotification.ERROR
                    );
                }
            }
        };
        worker.execute();
    }
    
    private void actualizarTabla(List<MovimientoResponse> movimientos) {
        tableModel.setRowCount(0);
        for (MovimientoResponse mov : movimientos) {
            String tipo = mov.tipoMovimiento != null ? mov.tipoMovimiento : mov.tipo;
            String naturaleza = mov.naturaleza != null ? mov.naturaleza : "";
            tableModel.addRow(new Object[]{
                mov.id,
                mov.fecha,
                tipo,
                naturaleza,
                String.format("$%.2f", mov.valor)
            });
        }
    }
}
