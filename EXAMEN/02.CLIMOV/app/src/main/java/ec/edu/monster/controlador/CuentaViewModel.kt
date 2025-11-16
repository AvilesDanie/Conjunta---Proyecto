package ec.edu.monster.controlador

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ec.edu.monster.modelo.*
import ec.edu.monster.util.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class CuentaState {
    object Idle : CuentaState()
    object Loading : CuentaState()
    data class Success(val cuentas: List<CuentaResponse>) : CuentaState()
    data class Error(val message: String) : CuentaState()
}

sealed class MovimientoState {
    object Idle : MovimientoState()
    object Loading : MovimientoState()
    data class Success(val movimientos: List<MovimientoResponse>) : MovimientoState()
    data class Error(val message: String) : MovimientoState()
}

class CuentaViewModel : ViewModel() {
    
    private val _cuentaState = MutableStateFlow<CuentaState>(CuentaState.Idle)
    val cuentaState: StateFlow<CuentaState> = _cuentaState.asStateFlow()
    
    private val _movimientoState = MutableStateFlow<MovimientoState>(MovimientoState.Idle)
    val movimientoState: StateFlow<MovimientoState> = _movimientoState.asStateFlow()
    
    fun cargarCuentasPorCliente(cedula: String) {
        viewModelScope.launch {
            try {
                _cuentaState.value = CuentaState.Loading
                
                val response = RetrofitClient.banquitoApi.listarCuentasPorCliente(cedula)
                
                if (response.isSuccessful && response.body() != null) {
                    _cuentaState.value = CuentaState.Success(response.body()!!)
                } else {
                    _cuentaState.value = CuentaState.Error("Error al cargar cuentas")
                }
            } catch (e: Exception) {
                _cuentaState.value = CuentaState.Error("Error: ${e.message}")
            }
        }
    }
    
    fun listarTodasCuentas() {
        viewModelScope.launch {
            try {
                _cuentaState.value = CuentaState.Loading
                
                val response = RetrofitClient.banquitoApi.listarCuentas()
                
                if (response.isSuccessful && response.body() != null) {
                    _cuentaState.value = CuentaState.Success(response.body()!!)
                } else {
                    _cuentaState.value = CuentaState.Error("Error al cargar cuentas")
                }
            } catch (e: Exception) {
                _cuentaState.value = CuentaState.Error("Error: ${e.message}")
            }
        }
    }
    
    fun cargarMovimientos(numCuenta: String) {
        viewModelScope.launch {
            try {
                _movimientoState.value = MovimientoState.Loading
                
                val response = RetrofitClient.banquitoApi.listarMovimientosPorCuenta(numCuenta)
                
                if (response.isSuccessful && response.body() != null) {
                    _movimientoState.value = MovimientoState.Success(response.body()!!)
                } else {
                    _movimientoState.value = MovimientoState.Error("Error al cargar movimientos")
                }
            } catch (e: Exception) {
                _movimientoState.value = MovimientoState.Error("Error: ${e.message}")
            }
        }
    }
    
    suspend fun crearMovimiento(request: MovimientoRequest): Result<MovimientoResponse> {
        return try {
            val response = RetrofitClient.banquitoApi.crearMovimiento(request)
            
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al crear movimiento"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun crearCuenta(request: CuentaRequest): Result<CuentaResponse> {
        return try {
            val response = RetrofitClient.banquitoApi.crearCuenta(request)
            
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al crear cuenta"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
