package ec.edu.monster.controlador

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ec.edu.monster.modelo.*
import ec.edu.monster.util.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class FacturaState {
    object Idle : FacturaState()
    object Loading : FacturaState()
    data class Success(val facturas: List<FacturaResponse>) : FacturaState()
    data class Error(val message: String) : FacturaState()
}

sealed class FacturaDetailState {
    object Idle : FacturaDetailState()
    object Loading : FacturaDetailState()
    data class Success(val factura: FacturaResponse) : FacturaDetailState()
    data class Error(val message: String) : FacturaDetailState()
}

class FacturaViewModel : ViewModel() {
    
    private val _facturaState = MutableStateFlow<FacturaState>(FacturaState.Idle)
    val facturaState: StateFlow<FacturaState> = _facturaState.asStateFlow()
    
    private val _facturaDetailState = MutableStateFlow<FacturaDetailState>(FacturaDetailState.Idle)
    val facturaDetailState: StateFlow<FacturaDetailState> = _facturaDetailState.asStateFlow()
    
    fun cargarFacturas() {
        viewModelScope.launch {
            try {
                _facturaState.value = FacturaState.Loading
                
                val response = RetrofitClient.comercializadoraApi.listarFacturas()
                
                if (response.isSuccessful && response.body() != null) {
                    _facturaState.value = FacturaState.Success(response.body()!!)
                } else {
                    _facturaState.value = FacturaState.Error("Error al cargar facturas")
                }
            } catch (e: Exception) {
                _facturaState.value = FacturaState.Error("Error de conexión: ${e.message}")
            }
        }
    }
    
    fun obtenerFactura(id: Long) {
        viewModelScope.launch {
            try {
                _facturaDetailState.value = FacturaDetailState.Loading
                
                val response = RetrofitClient.comercializadoraApi.obtenerFactura(id)
                
                if (response.isSuccessful && response.body() != null) {
                    _facturaDetailState.value = FacturaDetailState.Success(response.body()!!)
                } else {
                    _facturaDetailState.value = FacturaDetailState.Error("Factura no encontrada")
                }
            } catch (e: Exception) {
                _facturaDetailState.value = FacturaDetailState.Error("Error: ${e.message}")
            }
        }
    }
    
    suspend fun crearFactura(request: FacturaRequest): Result<FacturaResponse> {
        return try {
            val response = RetrofitClient.comercializadoraApi.crearFactura(request)
            
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                Result.failure(Exception(errorBody ?: "Error al crear factura"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
