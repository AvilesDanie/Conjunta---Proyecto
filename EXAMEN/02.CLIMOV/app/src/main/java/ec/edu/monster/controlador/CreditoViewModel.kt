package ec.edu.monster.controlador

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ec.edu.monster.modelo.*
import ec.edu.monster.util.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class CreditoState {
    object Idle : CreditoState()
    object Loading : CreditoState()
    data class EvaluationSuccess(val resultado: ResultadoEvaluacion) : CreditoState()
    data class CreditCreated(val credito: CreditoResponse) : CreditoState()
    data class Error(val message: String) : CreditoState()
}

sealed class EvaluationState {
    object Idle : EvaluationState()
    object Loading : EvaluationState()
    data class EvaluationSuccess(val resultado: ResultadoEvaluacion) : EvaluationState()
    data class Error(val message: String) : EvaluationState()
}

sealed class CuotasState {
    object Idle : CuotasState()
    object Loading : CuotasState()
    data class Success(val cuotas: List<CuotaAmortizacion>) : CuotasState()
    data class Error(val message: String) : CuotasState()
}

class CreditoViewModel : ViewModel() {
    
    private val _creditoState = MutableStateFlow<CreditoState>(CreditoState.Idle)
    val creditoState: StateFlow<CreditoState> = _creditoState.asStateFlow()
    
    private val _evaluationState = MutableStateFlow<EvaluationState>(EvaluationState.Idle)
    val evaluationState: StateFlow<EvaluationState> = _evaluationState.asStateFlow()
    
    private val _cuotasState = MutableStateFlow<CuotasState>(CuotasState.Idle)
    val cuotasState: StateFlow<CuotasState> = _cuotasState.asStateFlow()
    
    fun evaluarCredito(cedula: String, precioProducto: java.math.BigDecimal, plazoMeses: Int, numCuentaCredito: String) {
        viewModelScope.launch {
            try {
                _evaluationState.value = EvaluationState.Loading
                
                val solicitud = SolicitudCredito(
                    cedula = cedula,
                    precioProducto = precioProducto,
                    plazoMeses = plazoMeses,
                    numCuentaCredito = numCuentaCredito
                )
                
                val response = RetrofitClient.banquitoApi.evaluarCredito(solicitud)
                
                if (response.isSuccessful && response.body() != null) {
                    val resultado = response.body()!!
                    _evaluationState.value = EvaluationState.EvaluationSuccess(resultado)
                } else {
                    _evaluationState.value = EvaluationState.Error("Error al evaluar crédito")
                }
            } catch (e: Exception) {
                _evaluationState.value = EvaluationState.Error("Error: ${e.message}")
            }
        }
    }
    
    fun resetEvaluationState() {
        _evaluationState.value = EvaluationState.Idle
    }
    
    suspend fun evaluarCredito(request: SolicitudCredito): Result<ResultadoEvaluacion> {
        return try {
            _creditoState.value = CreditoState.Loading
            
            val response = RetrofitClient.banquitoApi.evaluarCredito(request)
            
            if (response.isSuccessful && response.body() != null) {
                val resultado = response.body()!!
                _creditoState.value = CreditoState.EvaluationSuccess(resultado)
                Result.success(resultado)
            } else {
                _creditoState.value = CreditoState.Error("Error al evaluar crédito")
                Result.failure(Exception("Error al evaluar crédito"))
            }
        } catch (e: Exception) {
            _creditoState.value = CreditoState.Error("Error: ${e.message}")
            Result.failure(e)
        }
    }
    
    suspend fun crearCredito(request: SolicitudCredito): Result<CreditoResponse> {
        return try {
            _creditoState.value = CreditoState.Loading
            
            val response = RetrofitClient.banquitoApi.crearCredito(request)
            
            if (response.isSuccessful && response.body() != null) {
                val credito = response.body()!!
                _creditoState.value = CreditoState.CreditCreated(credito)
                Result.success(credito)
            } else {
                val errorBody = response.errorBody()?.string()
                _creditoState.value = CreditoState.Error(errorBody ?: "Error al crear crédito")
                Result.failure(Exception(errorBody ?: "Error al crear crédito"))
            }
        } catch (e: Exception) {
            _creditoState.value = CreditoState.Error("Error: ${e.message}")
            Result.failure(e)
        }
    }
    
    fun cargarCuotasAmortizacion(idCredito: Long) {
        viewModelScope.launch {
            try {
                _cuotasState.value = CuotasState.Loading
                
                val response = RetrofitClient.banquitoApi.listarCuotasPorCredito(idCredito)
                
                if (response.isSuccessful && response.body() != null) {
                    _cuotasState.value = CuotasState.Success(response.body()!!)
                } else {
                    _cuotasState.value = CuotasState.Error("Error al cargar cuotas")
                }
            } catch (e: Exception) {
                _cuotasState.value = CuotasState.Error("Error: ${e.message}")
            }
        }
    }
    
    suspend fun actualizarEstadoCuota(id: Long, cuota: CuotaAmortizacion): Result<CuotaAmortizacion> {
        return try {
            val response = RetrofitClient.banquitoApi.actualizarCuota(id, cuota)
            
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                Result.failure(Exception(errorBody ?: "Error al actualizar cuota"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun anularCuota(id: Long): Result<Unit> {
        return try {
            val response = RetrofitClient.banquitoApi.eliminarCuota(id)
            
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                val errorBody = response.errorBody()?.string()
                Result.failure(Exception(errorBody ?: "Error al anular cuota"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    fun resetState() {
        _creditoState.value = CreditoState.Idle
        _cuotasState.value = CuotasState.Idle
    }
}
