package ec.edu.monster.modelo

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

// ============ ELECTRODOMÉSTICO ============
data class ElectrodomesticoRequest(
    val codigo: String,
    val nombre: String,
    val precioVenta: BigDecimal
)

data class ElectrodomesticoResponse(
    val id: Long,
    val codigo: String,
    val nombre: String,
    val precioVenta: BigDecimal
)

// ============ FACTURA ============
data class FacturaRequest(
    val cedulaCliente: String,
    val nombreCliente: String,
    val idElectrodomestico: Long,
    val cantidad: Int,
    val formaPago: String, // EFECTIVO o CREDITO
    val plazoMeses: Int? = null,
    val numCuentaCredito: String? = null
)

data class DetalleFacturaResponse(
    val codigoElectro: String,
    val nombreElectro: String,
    val cantidad: Int,
    val precioUnitario: BigDecimal,
    val subtotal: BigDecimal
)

data class FacturaResponse(
    val id: Long,
    val fecha: String,
    val cedulaCliente: String,
    val nombreCliente: String,
    val formaPago: String,
    val totalBruto: BigDecimal,
    val descuento: BigDecimal,
    val totalNeto: BigDecimal,
    val idCreditoBanquito: Long?,
    val detalles: List<DetalleFacturaResponse>
)

// ============ USUARIO COMERCIALIZADORA ============
data class UsuarioComercializadoraRequest(
    val username: String,
    val password: String
)

data class UsuarioComercializadoraResponse(
    val id: Long,
    val username: String,
    val rol: String,
    val activo: Boolean
)
