package ec.edu.monster.modelo

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

// ============ USUARIO ============
data class UsuarioRequest(
    val username: String,
    val password: String,
    val rol: String? = null,
    val activo: Boolean? = null
)

data class UsuarioResponse(
    val id: Long,
    val username: String,
    val rol: String,
    val activo: Boolean
)

// ============ CLIENTE ============
data class ClienteRequest(
    val cedula: String,
    val nombre: String,
    val fechaNacimiento: String, // YYYY-MM-DD
    val estadoCivil: String,
    val numeroCuenta: String?,
    val tipoCuentaInicial: String?,
    val saldoInicial: BigDecimal?
)

data class ClienteResponse(
    val cedula: String,
    val nombre: String,
    val fechaNacimiento: String,
    val estadoCivil: String,
    val numCuentaInicial: String?,
    val tipoCuentaInicial: String?
)

data class ClienteOnlyResponse(
    val cedula: String,
    val nombre: String,
    val fechaNacimiento: String,
    val estadoCivil: String
)

// ============ CUENTA ============
data class CuentaRequest(
    val cedulaCliente: String,
    val tipoCuenta: String,
    val saldoInicial: BigDecimal? = null
)

data class CuentaResponse(
    val numCuenta: String,
    val cedulaCliente: String,
    val nombreCliente: String,
    val tipoCuenta: String,
    val saldo: BigDecimal
)

// ============ MOVIMIENTO ============
data class MovimientoRequest(
    val numCuenta: String,
    val tipo: String, // DEP, RET, TRA
    val valor: BigDecimal,
    val numCuentaOrigen: String? = null,
    val numCuentaDestino: String? = null
)

data class MovimientoResponse(
    val id: Long,
    val numCuenta: String,
    val tipo: String,
    val naturaleza: String,
    val valor: BigDecimal,
    val fecha: String,
    val internoTransferencia: Boolean
)

// ============ CRÉDITO ============
data class SolicitudCredito(
    val cedula: String,
    val precioProducto: BigDecimal,
    val plazoMeses: Int,
    val numCuentaCredito: String
)

data class ResultadoEvaluacion(
    val sujetoCredito: Boolean,
    val montoMaximo: BigDecimal,
    val aprobado: Boolean,
    val motivo: String
)

data class CreditoResponse(
    val id: Long,
    val cedulaCliente: String,
    val monto: BigDecimal,
    val plazoMeses: Int,
    val tasaAnual: BigDecimal,
    val fechaAprobacion: String,
    val estado: String,
    val numCuentaAsociada: String
)

// ============ CUOTA AMORTIZACIÓN ============
data class CuotaAmortizacion(
    val id: Long,
    val idCredito: Long,
    val numeroCuota: Int,
    val valorCuota: BigDecimal,
    val interesPagado: BigDecimal,
    val capitalPagado: BigDecimal,
    val saldo: BigDecimal,
    val fechaVencimiento: String,
    val estado: String
)
