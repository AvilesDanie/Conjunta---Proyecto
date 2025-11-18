package ec.edu.monster.modelo;

import java.math.BigDecimal;
import java.util.List;

/**
 * Modelos de datos para Comercializadora
 * DTOs que mapean las respuestas y peticiones de la API REST
 */
public class ComercializadoraModels {
    
    // ========== USUARIO ==========
    
    public static class UsuarioRequest {
        public String username;
        public String password;
        
        public UsuarioRequest() {}
        
        public UsuarioRequest(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }
    
    public static class UsuarioResponse {
        public Long id;
        public String username;
        public String rol;
        public boolean activo;
    }
    
    // ========== ELECTRODOMÉSTICO ==========
    
    public static class ElectrodomesticoRequest {
        public String codigo;
        public String nombre;
        public BigDecimal precioVenta;
        
        public ElectrodomesticoRequest() {}
        
        public ElectrodomesticoRequest(String codigo, String nombre, BigDecimal precioVenta) {
            this.codigo = codigo;
            this.nombre = nombre;
            this.precioVenta = precioVenta;
        }
    }
    
    public static class ElectrodomesticoResponse {
        public Long id;
        public String codigo;
        public String nombre;
        public BigDecimal precioVenta;
        public String imagenUrl;  // URL de la imagen desde el servidor
    }
    
    // ========== FACTURA ==========
    
    public static class FacturaRequest {
        public String cedulaCliente;
        public String nombreCliente;
        public Long idElectrodomestico;
        public int cantidad;
        public String formaPago;  // EFECTIVO o CREDITO
        
        // Solo si formaPago = CREDITO
        public Integer plazoMeses;
        public String numCuentaCredito;
        
        public FacturaRequest() {}
    }
    
    public static class DetalleFacturaResponse {
        public String codigoElectro;
        public String codigoElectrodomestico;  // Alias para codigoElectro
        public String nombreElectro;
        public int cantidad;
        public java.math.BigDecimal precioUnitario;
        public java.math.BigDecimal subtotal;
    }
    
    public static class FacturaResponse {
        public Long id;
        public String fecha;
        public String cedulaCliente;
        public String nombreCliente;
        public String formaPago;
        public java.math.BigDecimal totalBruto;
        public java.math.BigDecimal total;  // Alias para totalBruto
        public java.math.BigDecimal descuento;
        public java.math.BigDecimal totalNeto;
        public java.math.BigDecimal totalFinal;  // Alias para totalNeto
        public Long idCreditoBanquito;
        public List<DetalleFacturaResponse> detalles;
    }
}
