package ec.edu.monster.vista;

import ec.edu.monster.controlador.ElectrodomesticoController;
import ec.edu.monster.modelo.ComercializadoraModels.*;
import ec.edu.monster.util.ColorPalette;
import ec.edu.monster.util.ToastNotification;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Pantalla de gestión de electrodomésticos
 */
public class ElectrodomesticosFrame extends JFrame {
    
    private final ElectrodomesticoController electroController;
    private JTable table;
    private DefaultTableModel tableModel;
    private List<ElectrodomesticoResponse> todosLosElectros;
    private JTextField searchField;
    
    public ElectrodomesticosFrame() {
        this.electroController = new ElectrodomesticoController();
        initComponents();
        cargarElectrodomesticos();
    }
    
    private void initComponents() {
        setTitle("Electrodomésticos - ElectroQuito");
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
        
        JLabel titleLabel = new JLabel("📱 Electr odomésticos");
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
        
        // Barra superior con búsqueda y botón crear
        JPanel topPanel = new JPanel(new BorderLayout(10, 0));
        topPanel.setBackground(ColorPalette.FONDO_CLARO);
        
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
        searchField.putClientProperty("JTextField.placeholderText", "Buscar por código o nombre...");
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filtrarTabla();
            }
        });
        
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        
        // Botón crear
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setBackground(ColorPalette.FONDO_CLARO);
        
        JButton crearBtn = new JButton("➕ Nuevo Electrodoméstico");
        crearBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        crearBtn.setForeground(Color.WHITE);
        crearBtn.setBackground(ColorPalette.NARANJA_ELECTRO);
        crearBtn.setFocusPainted(false);
        crearBtn.setBorderPainted(false);
        crearBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        crearBtn.addActionListener(e -> {
            new CrearElectrodomesticoFrame().setVisible(true);
            dispose();
        });
        
        actionPanel.add(crearBtn);
        
        topPanel.add(searchPanel, BorderLayout.WEST);
        topPanel.add(actionPanel, BorderLayout.EAST);
        
        contentPanel.add(topPanel, BorderLayout.NORTH);
        
        // Tabla
        String[] columnNames = {"ID", "Código", "Nombre", "Precio Venta", "Acciones"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };
        
        table = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isCellSelected(row, column)) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 248, 250));
                    if (column != 3 && column != 4) {
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
        table.getColumnModel().getColumn(2).setPreferredWidth(300);
        table.getColumnModel().getColumn(3).setPreferredWidth(120);
        table.getColumnModel().getColumn(4).setPreferredWidth(200);
        
        // Renderizador para precio
        table.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 248, 250));
                }
                
                c.setForeground(ColorPalette.NARANJA_ELECTRO);
                setFont(new Font("Segoe UI", Font.BOLD, 15));
                setHorizontalAlignment(SwingConstants.RIGHT);
                return c;
            }
        });
        
        // Renderizador para acciones
        table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
                panel.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 248, 250));
                
                JButton editarBtn = new JButton("✏️ Editar");
                editarBtn.setFont(new Font("Segoe UI", Font.BOLD, 11));
                editarBtn.setForeground(ColorPalette.NARANJA_ELECTRO);
                editarBtn.setBackground(Color.WHITE);
                editarBtn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ColorPalette.NARANJA_ELECTRO, 1),
                    BorderFactory.createEmptyBorder(3, 8, 3, 8)
                ));
                editarBtn.setFocusPainted(false);
                editarBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                
                JButton eliminarBtn = new JButton("🗑️ Eliminar");
                eliminarBtn.setFont(new Font("Segoe UI", Font.BOLD, 11));
                eliminarBtn.setForeground(ColorPalette.ROJO_ERROR);
                eliminarBtn.setBackground(Color.WHITE);
                eliminarBtn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ColorPalette.ROJO_ERROR, 1),
                    BorderFactory.createEmptyBorder(3, 8, 3, 8)
                ));
                eliminarBtn.setFocusPainted(false);
                eliminarBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                
                panel.add(editarBtn);
                panel.add(eliminarBtn);
                
                return panel;
            }
        });
        
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.rowAtPoint(evt.getPoint());
                int col = table.columnAtPoint(evt.getPoint());
                if (row >= 0 && col == 4) {
                    String codigo = table.getValueAt(row, 1).toString();
                    Point p = evt.getPoint();
                    
                    if (p.x < table.getCellRect(row, col, false).x + 100) {
                        new EditarElectrodomesticoFrame(codigo).setVisible(true);
                        dispose();
                    } else {
                        eliminarElectrodomestico(codigo);
                    }
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(ColorPalette.GRIS_BORDES, 1));
        
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        return contentPanel;
    }
    
    private void cargarElectrodomesticos() {
        SwingWorker<List<ElectrodomesticoResponse>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<ElectrodomesticoResponse> doInBackground() throws Exception {
                return java.util.Arrays.asList(electroController.listarElectrodomesticos());
            }
            
            @Override
            protected void done() {
                try {
                    todosLosElectros = get();
                    actualizarTabla(todosLosElectros);
                } catch (Exception ex) {
                    ToastNotification.showToast(
                        ElectrodomesticosFrame.this,
                        "Error al cargar electrodomésticos: " + ex.getMessage(),
                        ToastNotification.ERROR
                    );
                }
            }
        };
        worker.execute();
    }
    
    private void actualizarTabla(List<ElectrodomesticoResponse> electros) {
        tableModel.setRowCount(0);
        for (ElectrodomesticoResponse electro : electros) {
            tableModel.addRow(new Object[]{
                electro.id,
                electro.codigo,
                electro.nombre,
                String.format("$%.2f", electro.precioVenta),
                "Acciones"
            });
        }
    }
    
    private void filtrarTabla() {
        String searchText = searchField.getText().toLowerCase().trim();
        if (searchText.isEmpty()) {
            actualizarTabla(todosLosElectros);
        } else {
            List<ElectrodomesticoResponse> filtrados = todosLosElectros.stream()
                .filter(e -> (e.codigo != null && e.codigo.toLowerCase().contains(searchText)) ||
                           (e.nombre != null && e.nombre.toLowerCase().contains(searchText)))
                .toList();
            actualizarTabla(filtrados);
        }
    }
    
    private void eliminarElectrodomestico(String codigo) {
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de eliminar este electrodoméstico?",
            "Confirmar Eliminación",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            SwingWorker<Void, Void> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() throws Exception {
                    electroController.eliminarElectrodomestico(codigo);
                    return null;
                }
                
                @Override
                protected void done() {
                    try {
                        get();
                        ToastNotification.showToast(
                            ElectrodomesticosFrame.this,
                            "Electrodoméstico eliminado exitosamente",
                            ToastNotification.SUCCESS
                        );
                        cargarElectrodomesticos();
                    } catch (Exception ex) {
                        ToastNotification.showToast(
                            ElectrodomesticosFrame.this,
                            "Error al eliminar electrodoméstico: " + ex.getMessage(),
                            ToastNotification.ERROR
                        );
                    }
                }
            };
            worker.execute();
        }
    }
}

