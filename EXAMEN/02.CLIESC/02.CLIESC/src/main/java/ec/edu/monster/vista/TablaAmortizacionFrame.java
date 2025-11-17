package ec.edu.monster.vista;

import ec.edu.monster.controlador.CreditoController;
import ec.edu.monster.modelo.BanquitoModels.*;
import ec.edu.monster.util.ColorPalette;
import ec.edu.monster.util.ToastNotification;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Pantalla de visualización de tabla de amortización
 */
public class TablaAmortizacionFrame extends JFrame {
    
    private final CreditoController creditoController;
    private final int idCredito;
    private JTable table;
    private DefaultTableModel tableModel;
    
    public TablaAmortizacionFrame(int idCredito) {
        this.creditoController = new CreditoController();
        this.idCredito = idCredito;
        initComponents();
        cargarTabla();
    }
    
    private void initComponents() {
        setTitle("Tabla de Amortización - BanQuito");
        setSize(1150, 700);
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
            new CreditosFrame().setVisible(true);
            dispose();
        });
        
        JLabel titleLabel = new JLabel("📊 Tabla de Amortización - Crédito #" + idCredito);
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
        
        // Info Panel
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.setBackground(ColorPalette.FONDO_CLARO);
        
        JLabel infoLabel = new JLabel("Visualización de solo lectura");
        infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        infoLabel.setForeground(ColorPalette.TEXTO_SECUNDARIO_GRIS);
        infoPanel.add(infoLabel);
        
        contentPanel.add(infoPanel, BorderLayout.NORTH);
        
        // Tabla
        String[] columnNames = {"#", "Cuota", "Capital", "Interés", "Saldo", "Fecha Venc.", "Fecha Pago", "Estado"};
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
                    if (column != 1 && column != 2 && column != 3 && column != 4 && column != 7) {
                        c.setForeground(ColorPalette.TEXTO_PRINCIPAL_NEGRO);
                    }
                }
                return c;
            }
        };
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
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
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(120);
        table.getColumnModel().getColumn(5).setPreferredWidth(120);
        table.getColumnModel().getColumn(6).setPreferredWidth(120);
        table.getColumnModel().getColumn(7).setPreferredWidth(100);
        
        // Renderizador para cuota
        table.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 248, 250));
                }
                
                c.setForeground(ColorPalette.AZUL_PRIMARIO);
                setFont(new Font("Segoe UI", Font.BOLD, 13));
                setHorizontalAlignment(SwingConstants.RIGHT);
                return c;
            }
        });
        
        // Renderizador para capital
        table.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 248, 250));
                }
                
                c.setForeground(ColorPalette.TEXTO_PRINCIPAL_NEGRO);
                setFont(new Font("Segoe UI", Font.PLAIN, 12));
                setHorizontalAlignment(SwingConstants.RIGHT);
                return c;
            }
        });
        
        // Renderizador para interés
        table.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 248, 250));
                }
                
                c.setForeground(ColorPalette.AMARILLO_ADVERTENCIA);
                setFont(new Font("Segoe UI", Font.PLAIN, 12));
                setHorizontalAlignment(SwingConstants.RIGHT);
                return c;
            }
        });
        
        // Renderizador para saldo
        table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
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
                        setFont(new Font("Segoe UI", Font.BOLD, 13));
                    } catch (Exception e) {
                        c.setForeground(ColorPalette.TEXTO_PRINCIPAL_NEGRO);
                    }
                }
                setHorizontalAlignment(SwingConstants.RIGHT);
                return c;
            }
        });
        
        // Renderizador para estado
        table.getColumnModel().getColumn(7).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 248, 250));
                }
                
                if (value != null) {
                    c.setForeground(ColorPalette.getColorEstadoCuota(value.toString()));
                    setFont(new Font("Segoe UI", Font.BOLD, 12));
                }
                setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(ColorPalette.GRIS_BORDES, 1));
        
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        return contentPanel;
    }
    
    private void cargarTabla() {
        SwingWorker<List<CuotaAmortizacion>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<CuotaAmortizacion> doInBackground() throws Exception {
                return java.util.Arrays.asList(creditoController.listarCuotas((long) idCredito));
            }
            
            @Override
            protected void done() {
                try {
                    List<CuotaAmortizacion> cuotas = get();
                    actualizarTabla(cuotas);
                } catch (Exception ex) {
                    ToastNotification.showToast(
                        TablaAmortizacionFrame.this,
                        "Error al cargar tabla de amortización: " + ex.getMessage(),
                        ToastNotification.ERROR
                    );
                }
            }
        };
        worker.execute();
    }
    
    private void actualizarTabla(List<CuotaAmortizacion> cuotas) {
        tableModel.setRowCount(0);
        for (CuotaAmortizacion cuota : cuotas) {
            tableModel.addRow(new Object[]{
                cuota.numeroCuota,
                String.format("$%.2f", cuota.valorCuota != null ? cuota.valorCuota : 0.0),
                String.format("$%.2f", cuota.capitalPagado != null ? cuota.capitalPagado : 0.0),
                String.format("$%.2f", cuota.interesPagado != null ? cuota.interesPagado : 0.0),
                String.format("$%.2f", cuota.saldo != null ? cuota.saldo : 0.0),
                cuota.fechaVencimiento != null ? cuota.fechaVencimiento : "-",
                cuota.fechaPago != null ? cuota.fechaPago : "-",
                cuota.estado != null ? cuota.estado : "PENDIENTE"
            });
        }
    }
}
