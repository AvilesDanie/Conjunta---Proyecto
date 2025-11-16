package ec.edu.monster.controlador

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ec.edu.monster.modelo.*
import ec.edu.monster.util.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class ElectrodomesticoState {
    object Idle : ElectrodomesticoState()
    object Loading : ElectrodomesticoState()
    data class Success(val electrodomesticos: List<ElectrodomesticoResponse>) : ElectrodomesticoState()
    data class Error(val message: String) : ElectrodomesticoState()
}

class ElectrodomesticoViewModel : ViewModel() {
    
    private val _electroState = MutableStateFlow<ElectrodomesticoState>(ElectrodomesticoState.Idle)
    val electroState: StateFlow<ElectrodomesticoState> = _electroState.asStateFlow()
    
    fun cargarElectrodomesticos() {
        viewModelScope.launch {
            try {
                _electroState.value = ElectrodomesticoState.Loading
                
                val response = RetrofitClient.comercializadoraApi.listarElectrodomesticos()
                
                if (response.isSuccessful && response.body() != null) {
                    _electroState.value = ElectrodomesticoState.Success(response.body()!!)
                } else {
                    _electroState.value = ElectrodomesticoState.Error("Error al cargar productos")
                }
            } catch (e: Exception) {
                _electroState.value = ElectrodomesticoState.Error("Error de conexión: ${e.message}")
            }
        }
    }
    
    suspend fun crearElectrodomestico(request: ElectrodomesticoRequest): Result<ElectrodomesticoResponse> {
        return try {
            val response = RetrofitClient.comercializadoraApi.crearElectrodomestico(request)
            
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al crear electrodoméstico"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
