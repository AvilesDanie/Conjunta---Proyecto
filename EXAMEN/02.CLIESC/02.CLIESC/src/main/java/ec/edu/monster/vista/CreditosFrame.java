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
 * Pantalla de gestión de créditos
 */
public class CreditosFrame extends JFrame {
    
    private final CreditoController creditoController;
    private JTable table;
    private DefaultTableModel tableModel;
    private List<CreditoResponse> todosLosCreditos;
    
    public CreditosFrame() {
        this.creditoController = new CreditoController();
        initComponents();
        cargarCreditos();
    }
    
    private void initComponents() {
        setTitle("Gestión de Créditos - BanQuito");
        setSize(1050, 650);
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
        
        JLabel titleLabel = new JLabel("Gestión de Créditos");
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
        
        // Botón evaluar crédito
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setBackground(ColorPalette.FONDO_CLARO);
        
        JButton evaluarBtn = new JButton("Evaluar Crédito");
        evaluarBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        evaluarBtn.setForeground(Color.WHITE);
        evaluarBtn.setBackground(ColorPalette.VERDE_EXITO);
        evaluarBtn.setFocusPainted(false);
        evaluarBtn.setBorderPainted(false);
        evaluarBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        evaluarBtn.addActionListener(e -> {
            new EvaluarCreditoFrame().setVisible(true);
            dispose();
        });
        
        actionPanel.add(evaluarBtn);
        contentPanel.add(actionPanel, BorderLayout.NORTH);
        
        // Tabla
        String[] columnNames = {"ID", "Num. Cuenta", "Monto Solicitado", "Plazo (meses)", "Estado", "Acciones"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };
        
        table = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                
                // Filas alternadas para mejor legibilidad
                if (!isRowSelected(row) && column != 5) {
                    if (row % 2 == 0) {
                        c.setBackground(Color.WHITE);
                    } else {
                        c.setBackground(new Color(245, 248, 250));
                    }
                } else if (isRowSelected(row) && column != 5) {
                    c.setBackground(new Color(227, 242, 253));
                }
                
                // Texto más oscuro para mejor contraste (excepto columnas con colores personalizados)
                if (!isRowSelected(row) && column != 2 && column != 4) {
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
        table.getColumnModel().getColumn(0).setPreferredWidth(60);
        table.getColumnModel().getColumn(1).setPreferredWidth(130);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(3).setPreferredWidth(120);
        table.getColumnModel().getColumn(4).setPreferredWidth(120);
        table.getColumnModel().getColumn(5).setPreferredWidth(140);
        
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
        table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (value != null) {
                    String estado = value.toString();
                    if ("APROBADO".equals(estado)) {
                        c.setForeground(ColorPalette.VERDE_EXITO);
                        setFont(new Font("Segoe UI", Font.BOLD, 13));
                    } else if ("RECHAZADO".equals(estado)) {
                        c.setForeground(ColorPalette.ROJO_ERROR);
                        setFont(new Font("Segoe UI", Font.BOLD, 13));
                    } else {
                        c.setForeground(ColorPalette.AMARILLO_ADVERTENCIA);
                        setFont(new Font("Segoe UI", Font.BOLD, 13));
                    }
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
        
        // Renderizador para botón
        table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JButton button = new JButton("Ver Tabla");
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
                if (row >= 0 && col == 5) {
                    String id = table.getValueAt(row, 0).toString();
                    new TablaAmortizacionFrame(Integer.parseInt(id)).setVisible(true);
                    dispose();
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(ColorPalette.GRIS_BORDES, 1));
        
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        return contentPanel;
    }
    
    private void cargarCreditos() {
        SwingWorker<List<CreditoResponse>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<CreditoResponse> doInBackground() throws Exception {
                return java.util.Arrays.asList(creditoController.listarCreditos());
            }
            
            @Override
            protected void done() {
                try {
                    todosLosCreditos = get();
                    actualizarTabla(todosLosCreditos);
                } catch (Exception ex) {
                    ToastNotification.showToast(
                        CreditosFrame.this,
                        "Error al cargar créditos. Verifica la conexión con el servidor.",
                        ToastNotification.ERROR
                    );
                    // Si no hay créditos, mostrar tabla vacía
                    tableModel.setRowCount(0);
                }
            }
        };
        worker.execute();
    }
    
    private void actualizarTabla(List<CreditoResponse> creditos) {
        tableModel.setRowCount(0);
        for (CreditoResponse credito : creditos) {
            tableModel.addRow(new Object[]{
                credito.id,
                credito.numCuenta,
                String.format("$%.2f", credito.montoSolicitado),
                credito.plazoMeses,
                credito.estadoCredito != null ? credito.estadoCredito : "PENDIENTE",
                "Ver"
            });
        }
    }
}

