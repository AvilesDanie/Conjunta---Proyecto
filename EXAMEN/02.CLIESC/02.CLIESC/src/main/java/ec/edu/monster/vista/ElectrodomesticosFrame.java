package ec.edu.monster.vista;

import ec.edu.monster.controlador.ElectrodomesticoController;
import ec.edu.monster.modelo.ComercializadoraModels.*;
import ec.edu.monster.util.ColorPalette;
import ec.edu.monster.util.ModernTableRenderer;
import ec.edu.monster.util.ToastNotification;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.imageio.ImageIO;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

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
        
        JButton crearBtn = new JButton("📦 Nuevo Electrodoméstico");
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
        String[] columnNames = {"ID", "Imagen", "Código", "Nombre", "Precio Venta"};
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
                return c;
            }
        };
        
        // Estilo moderno web-like
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(80); // Aumentado para las imágenes
        
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        table.getTableHeader().setBackground(ColorPalette.NARANJA_ELECTRO);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setPreferredSize(new Dimension(0, 52));
        table.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(ColorPalette.NARANJA_ELECTRO);
                c.setForeground(Color.WHITE);
                c.setFont(new Font("Segoe UI", Font.BOLD, 15));
                ((JLabel)c).setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(241, 90, 34)),
                    BorderFactory.createEmptyBorder(8, 15, 8, 15)
                ));
                ((JLabel)c).setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        });
        
        // Ajustar anchos de columna
        table.getColumnModel().getColumn(0).setPreferredWidth(60);   // ID
        table.getColumnModel().getColumn(1).setPreferredWidth(100);  // Imagen
        table.getColumnModel().getColumn(2).setPreferredWidth(150);  // Código
        table.getColumnModel().getColumn(3).setPreferredWidth(350);  // Nombre
        table.getColumnModel().getColumn(4).setPreferredWidth(180);  // Precio
        
        // Aplicar ModernTableRenderer a columnas 0, 2 con centramiento
        ModernTableRenderer modernRendererCenter = new ModernTableRenderer();
        modernRendererCenter.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(modernRendererCenter);
        table.getColumnModel().getColumn(2).setCellRenderer(modernRendererCenter);
        
        // Renderizador especial para nombre
        table.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                setFont(new Font("Segoe UI", Font.BOLD, 14));
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 235, 242)),
                    BorderFactory.createEmptyBorder(12, 20, 12, 20)
                ));
                
                if (isSelected) {
                    setBackground(new Color(187, 224, 251));
                } else {
                    setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252));
                }
                setForeground(new Color(33, 33, 33));
                setHorizontalAlignment(SwingConstants.CENTER);
                setText(value != null ? value.toString() : "");
                return this;
            }
        });
        
        // Renderizador para imagen (centrado con fondo alterno)
        table.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = new JLabel();
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 235, 242)),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)
                ));
                
                if (isSelected) {
                    label.setBackground(new Color(187, 224, 251));
                } else {
                    label.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252));
                }
                label.setOpaque(true);
                
                if (value instanceof javax.swing.ImageIcon) {
                    label.setIcon((javax.swing.ImageIcon) value);
                    label.setText("");
                } else {
                    label.setIcon(null);
                    label.setText(value != null ? value.toString() : "");
                }
                return label;
            }
        });
        
        // Renderizador para precio con estilo moderno
        table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                setFont(new Font("Segoe UI", Font.BOLD, 16));
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 235, 242)),
                    BorderFactory.createEmptyBorder(12, 20, 12, 20)
                ));
                
                if (isSelected) {
                    setBackground(new Color(187, 224, 251));
                } else {
                    setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252));
                }
                
                setForeground(ColorPalette.NARANJA_ELECTRO);
                setHorizontalAlignment(SwingConstants.CENTER);
                setText(value != null ? value.toString() : "");
                return this;
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
            ImageIcon iconoImagen = cargarImagenProducto(electro.imagenUrl);
            tableModel.addRow(new Object[]{
                electro.id,
                iconoImagen,
                electro.codigo,
                electro.nombre,
                String.format("$%.2f", electro.precioVenta)
            });
        }
    }
    
    private ImageIcon cargarImagenProducto(String imagenUrl) {
        if (imagenUrl == null || imagenUrl.isEmpty()) {
            return crearImagenPorDefecto();
        }
        
        try {
            // La URL ya viene completa desde el servidor: "/api/electrodomesticos/imagen/archivo.jpg"
            String baseUrl = "http://localhost:8080/WS_JAVA_REST_Comercializadora";
            String urlCompleta = baseUrl + imagenUrl;
            
            System.out.println("Intentando cargar imagen desde: " + urlCompleta);
            
            URL url = new URL(urlCompleta);
            BufferedImage img = ImageIO.read(url);
            
            if (img != null) {
                Image scaledImg = img.getScaledInstance(70, 70, Image.SCALE_SMOOTH);
                return new ImageIcon(scaledImg);
            } else {
                System.err.println("La imagen es null desde: " + urlCompleta);
            }
        } catch (Exception e) {
            System.err.println("Error cargando imagen desde " + imagenUrl + ": " + e.getMessage());
        }
        
        return crearImagenPorDefecto();
    }
    
    private ImageIcon crearImagenPorDefecto() {
        BufferedImage img = new BufferedImage(70, 70, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Fondo gris claro
        g2d.setColor(new Color(240, 240, 240));
        g2d.fillRect(0, 0, 70, 70);
        
        // Icono de imagen
        g2d.setColor(new Color(180, 180, 180));
        g2d.fillRect(20, 25, 30, 20);
        g2d.fillOval(25, 30, 8, 8);
        int[] xPoints = {35, 40, 45, 50};
        int[] yPoints = {45, 38, 42, 45};
        g2d.fillPolygon(xPoints, yPoints, 4);
        
        // Borde
        g2d.setColor(new Color(200, 200, 200));
        g2d.drawRect(0, 0, 69, 69);
        
        g2d.dispose();
        return new ImageIcon(img);
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

