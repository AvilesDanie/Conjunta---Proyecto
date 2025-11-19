package ec.edu.monster.vista;

import ec.edu.monster.controlador.ElectrodomesticoController;
import ec.edu.monster.modelo.ComercializadoraDTOs.*;
import ec.edu.monster.util.ApiClient;
import ec.edu.monster.util.ColorPalette;
import ec.edu.monster.util.ToastNotification;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import javax.imageio.ImageIO;

/**
 * Pantalla para editar electrodoméstico existente
 */
public class EditarElectrodomesticoFrame extends JFrame {
    
    private final ElectrodomesticoController electroController;
    private final Long id;
    private JTextField nombreField, precioField;
    private JLabel codigoLabel;
    private JButton actualizarBtn, seleccionarImagenBtn;
    private JLabel imagenPreviewLabel;
    private File archivoImagenSeleccionado;
    private ElectrodomesticoResponse electroActual;
    
    public EditarElectrodomesticoFrame(Long id) {
        this.electroController = new ElectrodomesticoController();
        this.id = id;
        initComponents();
        cargarElectrodomestico();
    }
    
    private void initComponents() {
        setTitle("Editar Electrodoméstico - ElectroQuito");
        setSize(750, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(ColorPalette.FONDO_CLARO);
        
        JPanel topBar = createTopBar();
        mainPanel.add(topBar, BorderLayout.NORTH);
        
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
            new ElectrodomesticosFrame().setVisible(true);
            dispose();
        });
        
        JLabel titleLabel = new JLabel("Editar Electrodoméstico");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        topBar.add(backBtn, BorderLayout.WEST);
        topBar.add(titleLabel, BorderLayout.CENTER);
        
        return topBar;
    }
    
    private void cargarElectrodomestico() {
        try {
            ElectrodomesticoResponse[] lista = electroController.listarElectrodomesticos();
            for (ElectrodomesticoResponse electro : lista) {
                if (electro.id.equals(id)) {
                    electroActual = electro;
                    mostrarFormulario();
                    return;
                }
            }
            throw new Exception("Electrodoméstico no encontrado");
        } catch (Exception ex) {
            ToastNotification.showToast(
                EditarElectrodomesticoFrame.this,
                "Error al cargar electrodoméstico: " + ex.getMessage(),
                ToastNotification.ERROR
            );
            new ElectrodomesticosFrame().setVisible(true);
            dispose();
        }
    }
    
    private void mostrarFormulario() {
        // Remover contenido anterior
        Container contentPane = getContentPane();
        contentPane.removeAll();
        
        // Crear mainPanel completo con fondo gris
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(ColorPalette.FONDO_CLARO);
        
        // TopBar
        JPanel topBar = createTopBar();
        mainPanel.add(topBar, BorderLayout.NORTH);
        
        // Content panel con fondo gris
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(ColorPalette.FONDO_CLARO);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Form panel con fondo blanco
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.GRIS_BORDES, 1),
            BorderFactory.createEmptyBorder(25, 30, 25, 30)
        ));
        formPanel.setMaximumSize(new Dimension(600, Integer.MAX_VALUE));
        
        JLabel sectionTitle = new JLabel("Datos del Electrodoméstico");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sectionTitle.setForeground(ColorPalette.TEXTO_PRINCIPAL_NEGRO);
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(sectionTitle);
        formPanel.add(Box.createVerticalStrut(20));
        
        // Código (solo lectura)
        formPanel.add(createFieldLabel("Código"));
        codigoLabel = new JLabel(electroActual.codigo);
        codigoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        codigoLabel.setForeground(ColorPalette.TEXTO_GRIS_MEDIO);
        codigoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(codigoLabel);
        formPanel.add(Box.createVerticalStrut(15));
        
        // Nombre
        formPanel.add(createFieldLabel("Nombre *"));
        nombreField = createTextField();
        nombreField.setText(electroActual.nombre);
        formPanel.add(nombreField);
        formPanel.add(Box.createVerticalStrut(15));
        
        // Precio
        formPanel.add(createFieldLabel("Precio de Venta *"));
        precioField = createTextField();
        precioField.setText(String.valueOf(electroActual.precioVenta));
        formPanel.add(precioField);
        formPanel.add(Box.createVerticalStrut(15));
        
        // Campo para seleccionar imagen
        formPanel.add(createFieldLabel("Imagen del Producto"));
        formPanel.add(Box.createVerticalStrut(8));
        
        JPanel imagenPanel = new JPanel(new BorderLayout(10, 0));
        imagenPanel.setOpaque(false);
        imagenPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        imagenPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Preview de la imagen actual
        imagenPreviewLabel = new JLabel("Cargando...");
        imagenPreviewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imagenPreviewLabel.setVerticalAlignment(SwingConstants.CENTER);
        imagenPreviewLabel.setBorder(BorderFactory.createLineBorder(ColorPalette.GRIS_BORDES, 2));
        imagenPreviewLabel.setPreferredSize(new Dimension(120, 120));
        imagenPreviewLabel.setBackground(new Color(245, 245, 245));
        imagenPreviewLabel.setOpaque(true);
        
        // Cargar imagen actual
        cargarImagenActual();
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);
        
        seleccionarImagenBtn = new JButton("Cambiar Imagen");
        seleccionarImagenBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        seleccionarImagenBtn.setForeground(Color.WHITE);
        seleccionarImagenBtn.setBackground(ColorPalette.NARANJA_ELECTRO);
        seleccionarImagenBtn.setFocusPainted(false);
        seleccionarImagenBtn.setBorderPainted(false);
        seleccionarImagenBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        seleccionarImagenBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        seleccionarImagenBtn.addActionListener(e -> seleccionarImagen());
        
        buttonPanel.add(seleccionarImagenBtn);
        buttonPanel.add(Box.createVerticalStrut(10));
        
        JLabel infoLabel = new JLabel("<html><small>Formatos: JPG, PNG<br>Tamaño máx: 5MB<br>(Opcional)</small></html>");
        infoLabel.setForeground(Color.GRAY);
        infoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonPanel.add(infoLabel);
        
        imagenPanel.add(imagenPreviewLabel, BorderLayout.WEST);
        imagenPanel.add(buttonPanel, BorderLayout.CENTER);
        
        formPanel.add(imagenPanel);
        formPanel.add(Box.createVerticalStrut(25));
        
        // Panel de botones
        JPanel botonesPanel = new JPanel();
        botonesPanel.setLayout(new BoxLayout(botonesPanel, BoxLayout.X_AXIS));
        botonesPanel.setOpaque(false);
        botonesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        botonesPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        
        JButton cancelarBtn = new JButton("Cancelar");
        cancelarBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        cancelarBtn.setForeground(ColorPalette.TEXTO_GRIS_MEDIO);
        cancelarBtn.setBackground(Color.WHITE);
        cancelarBtn.setFocusPainted(false);
        cancelarBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.GRIS_BORDES, 2),
            BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));
        cancelarBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelarBtn.addActionListener(e -> {
            new ElectrodomesticosFrame().setVisible(true);
            dispose();
        });
        
        actualizarBtn = new JButton("Actualizar Electrodoméstico");
        actualizarBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        actualizarBtn.setForeground(Color.WHITE);
        actualizarBtn.setBackground(ColorPalette.NARANJA_ELECTRO);
        actualizarBtn.setFocusPainted(false);
        actualizarBtn.setBorderPainted(false);
        actualizarBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        actualizarBtn.addActionListener(e -> actualizarElectrodomestico());
        
        botonesPanel.add(cancelarBtn);
        botonesPanel.add(Box.createHorizontalStrut(15));
        botonesPanel.add(actualizarBtn);
        
        formPanel.add(botonesPanel);
        
        contentPanel.add(formPanel);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Agregar mainPanel completo al frame
        contentPane.add(mainPanel);
        revalidate();
        repaint();
    }
    
    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(ColorPalette.TEXTO_PRINCIPAL_NEGRO);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }
    
    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.GRIS_BORDES, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        return field;
    }
    
    private void cargarImagenActual() {
        if (electroActual.imagenUrl == null || electroActual.imagenUrl.isEmpty()) {
            imagenPreviewLabel.setText("Sin imagen");
            return;
        }
        
        SwingWorker<ImageIcon, Void> worker = new SwingWorker<>() {
            @Override
            protected ImageIcon doInBackground() throws Exception {
                String urlCompleta = ApiClient.BASE_URL_COMERCIALIZADORA_ROOT + electroActual.imagenUrl;
                
                URL url = new URL(urlCompleta);
                BufferedImage img = ImageIO.read(url);
                
                if (img != null) {
                    Image scaledImg = img.getScaledInstance(110, 110, Image.SCALE_SMOOTH);
                    return new ImageIcon(scaledImg);
                }
                return null;
            }
            
            @Override
            protected void done() {
                try {
                    ImageIcon icon = get();
                    if (icon != null) {
                        imagenPreviewLabel.setIcon(icon);
                        imagenPreviewLabel.setText("");
                    } else {
                        imagenPreviewLabel.setText("Sin imagen");
                    }
                } catch (Exception e) {
                    imagenPreviewLabel.setText("Error al cargar");
                }
            }
        };
        worker.execute();
    }
    
    private void seleccionarImagen() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar Nueva Imagen");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            "Imágenes (JPG, PNG, GIF)", "jpg", "jpeg", "png", "gif"));
        
        int resultado = fileChooser.showOpenDialog(this);
        
        if (resultado == JFileChooser.APPROVE_OPTION) {
            archivoImagenSeleccionado = fileChooser.getSelectedFile();
            
            // Validar tamaño (5MB máx)
            if (archivoImagenSeleccionado.length() > 5 * 1024 * 1024) {
                ToastNotification.showToast(
                    this,
                    "La imagen es demasiado grande. Tamaño máximo: 5MB",
                    ToastNotification.ERROR
                );
                archivoImagenSeleccionado = null;
                return;
            }
            
            // Mostrar preview
            try {
                ImageIcon icon = new ImageIcon(archivoImagenSeleccionado.getAbsolutePath());
                Image img = icon.getImage().getScaledInstance(110, 110, Image.SCALE_SMOOTH);
                imagenPreviewLabel.setIcon(new ImageIcon(img));
                imagenPreviewLabel.setText("");
            } catch (Exception e) {
                imagenPreviewLabel.setText("Error al cargar");
            }
        }
    }
    
    private void actualizarElectrodomestico() {
        String nombre = nombreField.getText().trim();
        String precioStr = precioField.getText().trim();
        
        if (nombre.isEmpty() || precioStr.isEmpty()) {
            ToastNotification.showToast(
                this,
                "Por favor complete todos los campos",
                ToastNotification.ERROR
            );
            return;
        }
        
        double precio;
        try {
            precio = Double.parseDouble(precioStr);
            if (precio <= 0) {
                ToastNotification.showToast(
                    this,
                    "El precio debe ser mayor a cero",
                    ToastNotification.ERROR
                );
                return;
            }
        } catch (NumberFormatException e) {
            ToastNotification.showToast(
                this,
                "El precio debe ser un número válido",
                ToastNotification.ERROR
            );
            return;
        }
        
        actualizarBtn.setEnabled(false);
        actualizarBtn.setText("Actualizando...");
        
        String codigo = electroActual.codigo;
        double precioFinal = precio;
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                if (archivoImagenSeleccionado != null) {
                    // Actualizar con nueva imagen
                    electroController.actualizarElectrodomesticoConImagen(
                        id, codigo, nombre, precioFinal, archivoImagenSeleccionado);
                } else {
                    // Actualizar sin cambiar imagen
                    electroController.actualizarElectrodomesticoConImagen(
                        id, codigo, nombre, precioFinal, null);
                }
                return null;
            }
            
            @Override
            protected void done() {
                try {
                    get();
                    ToastNotification.showToast(
                        EditarElectrodomesticoFrame.this,
                        "Electrodoméstico actualizado exitosamente",
                        ToastNotification.SUCCESS
                    );
                    new ElectrodomesticosFrame().setVisible(true);
                    dispose();
                } catch (Exception ex) {
                    ToastNotification.showToast(
                        EditarElectrodomesticoFrame.this,
                        "Error al actualizar: " + ex.getMessage(),
                        ToastNotification.ERROR
                    );
                    actualizarBtn.setEnabled(true);
                    actualizarBtn.setText("💾 Actualizar Electrodoméstico");
                }
            }
        };
        worker.execute();
    }
}
