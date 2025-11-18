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
 * Pantalla de gestión de cuotas de crédito
 */
public class CuotasFrame extends JFrame {
    
    private final CreditoController creditoController;
    private final Long idCredito;
    private JTable table;
    private DefaultTableModel tableModel;
    
    public CuotasFrame(Long idCredito) {
        this.creditoController = new CreditoController();
        this.idCredito = idCredito;
        initComponents();
        cargarCuotas();
    }
    
    private void initComponents() {
        setTitle("Gestión de Cuotas - BanQuito");
        setSize(1100, 650);
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
        backBtn.setPreferredSize(new Dimension(110, 40));
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> {
            new HomeBanquitoFrame().setVisible(true);
            dispose();
        });
        
        JLabel titleLabel = new JLabel("Cuotas - Crédito #" + idCredito);
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
        
        // Tabla
        String[] columnNames = {"ID", "Número", "Monto", "Estado", "Fecha Vencimiento", "Acciones"};
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
                
                // Filas alternadas para mejor legibilidad (excepto columna de acciones)
                if (!isRowSelected(row) && column != 6) {
                    if (row % 2 == 0) {
                        c.setBackground(Color.WHITE);
                    } else {
                        c.setBackground(new Color(245, 248, 250));
                    }
                } else if (isRowSelected(row) && column != 6) {
                    c.setBackground(new Color(227, 242, 253));
                }
                
                // Texto más oscuro para mejor contraste (excepto columnas con colores personalizados)
                if (!isRowSelected(row) && column != 2 && column != 3) {
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
        
        // Configurar ancho de columnas (ahora son 6 columnas: 0-5)
        table.getColumnModel().getColumn(0).setPreferredWidth(50);   // ID
        table.getColumnModel().getColumn(1).setPreferredWidth(80);   // Número
        table.getColumnModel().getColumn(2).setPreferredWidth(120);  // Monto
        table.getColumnModel().getColumn(3).setPreferredWidth(100);  // Estado
        table.getColumnModel().getColumn(4).setPreferredWidth(140);  // Fecha Vencimiento
        table.getColumnModel().getColumn(5).setPreferredWidth(150);  // Acciones
        
        // Renderizador para monto
        table.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setForeground(ColorPalette.AZUL_PRIMARIO);
                setFont(new Font("Segoe UI", Font.BOLD, 14));
                if (!isSelected) {
                    if (row % 2 == 0) {
                        c.setBackground(Color.WHITE);
                    } else {
                        c.setBackground(new Color(245, 248, 250));
                    }
                }
                setHorizontalAlignment(SwingConstants.RIGHT);
                return c;
            }
        });
        
        // Renderizador para estado
        table.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (value != null) {
                    c.setForeground(ColorPalette.getColorEstadoCuota(value.toString()));
                    setFont(new Font("Segoe UI", Font.BOLD, 13));
                }
                if (!isSelected) {
                    if (row % 2 == 0) {
                        c.setBackground(Color.WHITE);
                    } else {
                        c.setBackground(new Color(245, 248, 250));
                    }
                }
                setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        });
        
        // Renderizador para acciones (columna 5, no 6)
        table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
                panel.setBackground(Color.WHITE);
                
                Object estadoObj = table.getValueAt(row, 3);
                String estado = null;
                if (estadoObj != null) {
                    estado = estadoObj.toString();
                    
                    if ("PENDIENTE".equals(estado)) {
                        JButton pagarBtn = new JButton("Pagar");
                        pagarBtn.setFont(new Font("Segoe UI", Font.BOLD, 11));
                        pagarBtn.setForeground(Color.WHITE);
                        pagarBtn.setBackground(ColorPalette.VERDE_EXITO);
                        pagarBtn.setFocusPainted(false);
                        pagarBtn.setBorderPainted(false);
                        pagarBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                        panel.add(pagarBtn);
                    }
                    
                    if ("PAGADA".equals(estado)) {
                        JButton anularBtn = new JButton("Anular");
                        anularBtn.setFont(new Font("Segoe UI", Font.BOLD, 11));
                        anularBtn.setForeground(Color.WHITE);
                        anularBtn.setBackground(ColorPalette.ROJO_ERROR);
                        anularBtn.setFocusPainted(false);
                        anularBtn.setBorderPainted(false);
                        anularBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                        panel.add(anularBtn);
                    }
                }
                
                return panel;
            }
        });
        
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.rowAtPoint(evt.getPoint());
                int col = table.columnAtPoint(evt.getPoint());
                if (row >= 0 && col == 5) { // Columna 5 es Acciones
                    String id = table.getValueAt(row, 0).toString();
                    String estado = table.getValueAt(row, 3).toString();
                    
                    if ("PENDIENTE".equals(estado)) {
                        pagarCuota(Integer.parseInt(id));
                    } else if ("PAGADA".equals(estado)) {
                        anularCuota(Integer.parseInt(id));
                    }
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(ColorPalette.GRIS_BORDES, 1));
        
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        return contentPanel;
    }
    
    private void cargarCuotas() {
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
                        CuotasFrame.this,
                        "Error al cargar cuotas. Verifica la conexión con el servidor.",
                        ToastNotification.ERROR
                    );
                    tableModel.setRowCount(0);
                }
            }
        };
        worker.execute();
    }
    
    private void actualizarTabla(List<CuotaAmortizacion> cuotas) {
        tableModel.setRowCount(0);
        for (CuotaAmortizacion cuota : cuotas) {
            tableModel.addRow(new Object[]{
                cuota.id,
                cuota.numeroCuota,
                String.format("$%.2f", cuota.valorCuota != null ? cuota.valorCuota : 0.0),
                cuota.estado != null ? cuota.estado : "PENDIENTE",
                cuota.fechaVencimiento != null ? cuota.fechaVencimiento : "-",
                "Acciones"
            });
        }
    }
    
    private void pagarCuota(int idCuota) {
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de pagar esta cuota?",
            "Confirmar Pago",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            SwingWorker<Void, Void> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() throws Exception {
                    ActualizarCuotaRequest request = new ActualizarCuotaRequest("PAGADA");
                    creditoController.actualizarEstadoCuota(idCuota, request);
                    // Pequeño delay para asegurar que el servidor procese
                    Thread.sleep(500);
                    return null;
                }
                
                @Override
                protected void done() {
                    try {
                        get();
                        ToastNotification.showToast(
                            CuotasFrame.this,
                            "Cuota pagada exitosamente",
                            ToastNotification.SUCCESS
                        );
                        cargarCuotas();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        ToastNotification.showToast(
                            CuotasFrame.this,
                            "Error al pagar cuota: " + ex.getMessage(),
                            ToastNotification.ERROR
                        );
                    }
                }
            };
            worker.execute();
        }
    }
    
    private void anularCuota(int idCuota) {
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de anular el pago de esta cuota?",
            "Confirmar Anulación",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            SwingWorker<Void, Void> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() throws Exception {
                    ActualizarCuotaRequest request = new ActualizarCuotaRequest("PENDIENTE");
                    creditoController.actualizarEstadoCuota(idCuota, request);
                    // Pequeño delay para asegurar que el servidor procese
                    Thread.sleep(500);
                    return null;
                }
                
                @Override
                protected void done() {
                    try {
                        get();
                        ToastNotification.showToast(
                            CuotasFrame.this,
                            "Pago de cuota anulado exitosamente",
                            ToastNotification.SUCCESS
                        );
                        cargarCuotas();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        ToastNotification.showToast(
                            CuotasFrame.this,
                            "Error al anular pago: " + ex.getMessage(),
                            ToastNotification.ERROR
                        );
                    }
                }
            };
            worker.execute();
        }
    }
}

