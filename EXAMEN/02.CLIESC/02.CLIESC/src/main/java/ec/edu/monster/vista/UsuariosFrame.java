package ec.edu.monster.vista;

import ec.edu.monster.controlador.UsuarioController;
import ec.edu.monster.modelo.BanquitoModels.*;
import ec.edu.monster.util.ColorPalette;
import ec.edu.monster.util.ToastNotification;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Pantalla de gestión de usuarios BanQuito
 */
public class UsuariosFrame extends JFrame {
    
    private final UsuarioController usuarioController;
    private JTable table;
    private DefaultTableModel tableModel;
    private List<UsuarioResponse> todosLosUsuarios;
    private JTextField searchField;
    
    public UsuariosFrame() {
        this.usuarioController = new UsuarioController();
        initComponents();
        cargarUsuarios();
    }
    
    private void initComponents() {
        setTitle("Gestión de Usuarios - BanQuito");
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
            new HomeBanquitoFrame().setVisible(true);
            dispose();
        });
        
        JLabel titleLabel = new JLabel("Gestión de Usuarios");
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
        searchField.putClientProperty("JTextField.placeholderText", "Buscar por username o rol...");
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
        
        JButton crearBtn = new JButton("+ Nuevo Usuario");
        crearBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        crearBtn.setForeground(Color.WHITE);
        crearBtn.setBackground(ColorPalette.VERDE_EXITO);
        crearBtn.setFocusPainted(false);
        crearBtn.setBorderPainted(false);
        crearBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        crearBtn.addActionListener(e -> {
            new CrearUsuarioFrame().setVisible(true);
            dispose();
        });
        
        actionPanel.add(crearBtn);
        
        topPanel.add(searchPanel, BorderLayout.WEST);
        topPanel.add(actionPanel, BorderLayout.EAST);
        
        contentPanel.add(topPanel, BorderLayout.NORTH);
        
        // Tabla
        String[] columnNames = {"ID", "Usuario", "Rol", "Activo", "Acciones"};
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
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(200);
        
        // Renderizador para estado activo
        table.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                // Color de fondo alternado
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 248, 250));
                }
                
                if (value != null) {
                    boolean activo = Boolean.parseBoolean(value.toString());
                    if (activo) {
                        c.setForeground(ColorPalette.VERDE_EXITO);
                        setFont(new Font("Segoe UI", Font.BOLD, 13));
                    } else {
                        c.setForeground(ColorPalette.ROJO_ERROR);
                        setFont(new Font("Segoe UI", Font.BOLD, 13));
                    }
                }
                setHorizontalAlignment(SwingConstants.CENTER);
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
                editarBtn.setForeground(ColorPalette.AZUL_PRIMARIO_MEDIO);
                editarBtn.setBackground(Color.WHITE);
                editarBtn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ColorPalette.AZUL_PRIMARIO_MEDIO, 1),
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
                    String id = table.getValueAt(row, 0).toString();
                    Point p = evt.getPoint();
                    
                    // Determinar si click fue en Editar o Eliminar
                    if (p.x < table.getCellRect(row, col, false).x + 100) {
                        new EditarUsuarioFrame(Integer.parseInt(id)).setVisible(true);
                        dispose();
                    } else {
                        eliminarUsuario(Integer.parseInt(id));
                    }
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(ColorPalette.GRIS_BORDES, 1));
        
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        return contentPanel;
    }
    
    private void cargarUsuarios() {
        SwingWorker<List<UsuarioResponse>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<UsuarioResponse> doInBackground() throws Exception {
                return java.util.Arrays.asList(usuarioController.listarUsuarios());
            }
            
            @Override
            protected void done() {
                try {
                    todosLosUsuarios = get();
                    actualizarTabla(todosLosUsuarios);
                } catch (Exception ex) {
                    ToastNotification.showToast(
                        UsuariosFrame.this,
                        "Error al cargar usuarios: " + ex.getMessage(),
                        ToastNotification.ERROR
                    );
                }
            }
        };
        worker.execute();
    }
    
    private void actualizarTabla(List<UsuarioResponse> usuarios) {
        tableModel.setRowCount(0);
        for (UsuarioResponse usuario : usuarios) {
            tableModel.addRow(new Object[]{
                usuario.id,
                usuario.username,
                usuario.rol != null ? usuario.rol : "USER",
                Boolean.toString(usuario.activo),
                "Acciones"
            });
        }
    }
    
    private void filtrarTabla() {
        String searchText = searchField.getText().toLowerCase().trim();
        if (searchText.isEmpty()) {
            actualizarTabla(todosLosUsuarios);
        } else {
            List<UsuarioResponse> filtrados = todosLosUsuarios.stream()
                .filter(u -> (u.username != null && u.username.toLowerCase().contains(searchText)) ||
                           (u.rol != null && u.rol.toLowerCase().contains(searchText)))
                .toList();
            actualizarTabla(filtrados);
        }
    }
    
    private void eliminarUsuario(int id) {
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de eliminar este usuario?",
            "Confirmar Eliminación",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            SwingWorker<Void, Void> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() throws Exception {
                    usuarioController.eliminarUsuario((long) id);
                    return null;
                }
                
                @Override
                protected void done() {
                    try {
                        get();
                        ToastNotification.showToast(
                            UsuariosFrame.this,
                            "Usuario eliminado exitosamente",
                            ToastNotification.SUCCESS
                        );
                        cargarUsuarios();
                    } catch (Exception ex) {
                        ToastNotification.showToast(
                            UsuariosFrame.this,
                            "Error al eliminar usuario: " + ex.getMessage(),
                            ToastNotification.ERROR
                        );
                    }
                }
            };
            worker.execute();
        }
    }
}

