package ec.edu.monster.modelo;

import java.math.BigDecimal;
import java.util.List;

/**
 * Modelos de datos para BanQuito
 * DTOs que mapean las respuestas y peticiones de la API REST
 */
public class BanquitoModels {
    
    // ========== USUARIO ==========
    
    public static class UsuarioRequest {
        public String username;
        public String nombreUsuario;  // Alias para username
        public String password;
        public String rol;
        public Boolean activo;
        
        public UsuarioRequest() {}
        
        public UsuarioRequest(String username, String password) {
            this.username = username;
            this.nombreUsuario = username;
            this.password = password;
        }
    }
    
    public static class UsuarioResponse {
        public Long id;
        public String username;
        public String nombreUsuario;  // Alias para username
        public String rol;
        public boolean activo;
    }
    
    // ========== CLIENTE ==========
    
    public static class ClienteRequest {
        public String cedula;
        public String nombre;
        public String fechaNacimiento;  // formato YYYY-MM-DD
        public String estadoCivil;       // SOLTERO, CASADO, DIVORCIADO, VIUDO
        public String numeroCuenta;      // opcional
        public String tipoCuentaInicial; // AHORROS o CORRIENTE
        public BigDecimal saldoInicial;  // opcional
        
        public ClienteRequest() {}
    }
    
    public static class ClienteResponse {
        public String cedula;
        public String nombre;
        public String fechaNacimiento;
        public String estadoCivil;
        public String numCuentaInicial;  // ⚠️ Backend usa "numCuentaInicial"
        public String tipoCuentaInicial;
    }
    
    public static class ClienteOnlyResponse {
        public String cedula;
        public String nombre;
        public String fechaNacimiento;
        public String estadoCivil;
    }
    
    // ========== CUENTA ==========
    
    public static class CuentaRequest {
        public String cedulaCliente;
        public String tipoCuenta;  // AHORROS o CORRIENTE
        public String numCuenta;   // Opcional - para crear con número específico
        public java.math.BigDecimal saldo;  // Opcional - saldo inicial
        
        public CuentaRequest() {}
        
        public CuentaRequest(String cedulaCliente, String tipoCuenta) {
            this.cedulaCliente = cedulaCliente;
            this.tipoCuenta = tipoCuenta;
        }
    }
    
    public static class CuentaResponse {
        public String numCuenta;
        public String cedulaCliente;
        public String nombreCliente;  // ⚠️ Backend incluye nombreCliente
        public String tipoCuenta;
        public BigDecimal saldo;
    }
    
    // ========== MOVIMIENTO ==========
    
    public static class MovimientoRequest {
        public String numCuenta;
        public String tipo;  // DEPOSITO, RETIRO, TRANSFERENCIA
        public String tipoMovimiento;  // Alias para tipo
        public java.math.BigDecimal valor;
        public String numCuentaDestino;  // solo para TRANSFERENCIA
        public String descripcion;  // Descripción opcional
        
        public MovimientoRequest() {}
    }
    
    public static class MovimientoResponse {
        public Long id;
        public String numCuenta;
        public String tipoMovimiento;  // "DEPOSITO", "RETIRO", "TRANSFERENCIA"
        public String naturaleza;  // "INGRESO" o "EGRESO"
        public java.math.BigDecimal valor;
        public String fecha;
        
        // Campos que NO están en la API pero mantenemos para compatibilidad
        public String tipo;  // Alias para tipoMovimiento
        public java.math.BigDecimal saldo;  // NO viene del servidor
        public String descripcion;  // NO viene del servidor
    }
    
    // ========== CRÉDITO ==========
    
    public static class SolicitudCredito {
        public String cedula;
        public String numCuenta;  // Número de cuenta
        public java.math.BigDecimal precioProducto;
        public java.math.BigDecimal montoSolicitado;  // Alias para precioProducto
        public int plazoMeses;
        public String numCuentaCredito;
        
        public SolicitudCredito() {}
        
        public SolicitudCredito(String cedula, java.math.BigDecimal precioProducto, 
                               int plazoMeses, String numCuentaCredito) {
            this.cedula = cedula;
            this.precioProducto = precioProducto;
            this.plazoMeses = plazoMeses;
            this.numCuentaCredito = numCuentaCredito;
        }
    }
    
    public static class ResultadoEvaluacion {
        public boolean sujetoCredito;
        public boolean aprobado;
        public java.math.BigDecimal montoMaximo;
        public java.math.BigDecimal montoAprobado;  // Alias para montoMaximo
        public java.math.BigDecimal tasaInteres;  // Tasa de interés
        public int plazoMeses;  // Plazo en meses
        public java.math.BigDecimal cuotaMensual;  // Cuota mensual
        public String motivo;
        public String mensaje;  // Alias para motivo
    }
    
    public static class CreditoResponse {
        public Long id;
        public String cedulaCliente;
        public String numCuenta;  // Número de cuenta asociada
        public java.math.BigDecimal monto;
        public java.math.BigDecimal montoSolicitado;  // Alias para monto
        public int plazoMeses;
        public java.math.BigDecimal tasaAnual;
        public String fechaAprobacion;
        public String estado;
        public String estadoCredito;  // Alias para estado
        public String numCuentaAsociada;
    }
    
    // ========== CUOTA AMORTIZACIÓN ==========
    // Campos según respuesta real de la API
    public static class CuotaAmortizacion {
        public Long id;                              // ID de la cuota (viene del servidor)
        public Long idCredito;                       // ID del crédito (viene del servidor)
        public int numeroCuota;                      // Número de cuota (viene del servidor)
        public java.math.BigDecimal valorCuota;      // Valor de la cuota (viene del servidor)
        public java.math.BigDecimal capitalPagado;   // Capital pagado (viene del servidor)
        public java.math.BigDecimal interesPagado;   // Interés pagado (viene del servidor)
        public java.math.BigDecimal saldo;           // Saldo después de la cuota (viene del servidor)
        public String fechaVencimiento;              // Fecha de vencimiento (viene del servidor)
        public String fechaPago;                     // Fecha de pago real (viene del servidor, puede ser null)
        public String estado;                        // PENDIENTE, PAGADA, VENCIDA, ANULADA (viene del servidor)
    }
    
    public static class ActualizarCuotaRequest {
        public String estado;
        public String estadoCuota;  // Alias para estado
        public String fechaVencimiento;
        
        public ActualizarCuotaRequest() {}
        
        public ActualizarCuotaRequest(String estado) {
            this.estado = estado;
            this.estadoCuota = estado;
        }
    }
}
