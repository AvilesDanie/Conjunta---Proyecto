package ec.edu.monster.controlador

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ec.edu.monster.modelo.*
import ec.edu.monster.util.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class ClienteState {
    object Idle : ClienteState()
    object Loading : ClienteState()
    data class Success(val clientes: List<ClienteResponse>) : ClienteState()
    data class Error(val message: String) : ClienteState()
}

sealed class ClienteDetailState {
    object Idle : ClienteDetailState()
    object Loading : ClienteDetailState()
    data class Success(val cliente: ClienteOnlyResponse) : ClienteDetailState()
    data class Error(val message: String) : ClienteDetailState()
}

class ClienteViewModel : ViewModel() {
    
    private val _clienteState = MutableStateFlow<ClienteState>(ClienteState.Idle)
    val clienteState: StateFlow<ClienteState> = _clienteState.asStateFlow()
    
    private val _clienteDetailState = MutableStateFlow<ClienteDetailState>(ClienteDetailState.Idle)
    val clienteDetailState: StateFlow<ClienteDetailState> = _clienteDetailState.asStateFlow()
    
    fun cargarClientes() {
        viewModelScope.launch {
            try {
                _clienteState.value = ClienteState.Loading
                
                val response = RetrofitClient.banquitoApi.listarClientes()
                
                if (response.isSuccessful && response.body() != null) {
                    _clienteState.value = ClienteState.Success(response.body()!!)
                } else {
                    _clienteState.value = ClienteState.Error("Error al cargar clientes")
                }
            } catch (e: Exception) {
                _clienteState.value = ClienteState.Error("Error de conexión: ${e.message}")
            }
        }
    }
    
    fun obtenerCliente(cedula: String) {
        viewModelScope.launch {
            try {
                _clienteDetailState.value = ClienteDetailState.Loading
                
                val response = RetrofitClient.banquitoApi.obtenerCliente(cedula)
                
                if (response.isSuccessful && response.body() != null) {
                    _clienteDetailState.value = ClienteDetailState.Success(response.body()!!)
                } else {
                    _clienteDetailState.value = ClienteDetailState.Error("Cliente no encontrado")
                }
            } catch (e: Exception) {
                _clienteDetailState.value = ClienteDetailState.Error("Error: ${e.message}")
            }
        }
    }
    
    suspend fun crearCliente(request: ClienteRequest): Result<ClienteResponse> {
        return try {
            val response = RetrofitClient.banquitoApi.crearCliente(request)
            
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al crear cliente"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun eliminarCliente(cedula: String): Result<Unit> {
        return try {
            val response = RetrofitClient.banquitoApi.eliminarCliente(cedula)
            
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al eliminar cliente"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun actualizarCliente(cedula: String, request: ClienteRequest): Result<ClienteOnlyResponse> {
        return try {
            val response = RetrofitClient.banquitoApi.actualizarCliente(cedula, request)
            
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al actualizar cliente"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
