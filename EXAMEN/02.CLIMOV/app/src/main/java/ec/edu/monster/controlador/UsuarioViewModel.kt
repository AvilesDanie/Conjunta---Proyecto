package ec.edu.monster.controlador

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ec.edu.monster.modelo.UsuarioRequest
import ec.edu.monster.modelo.UsuarioResponse
import ec.edu.monster.util.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class UsuarioState {
    object Idle : UsuarioState()
    object Loading : UsuarioState()
    data class Success(val usuarios: List<UsuarioResponse>) : UsuarioState()
    data class Error(val message: String) : UsuarioState()
}

sealed class UsuarioDetailState {
    object Idle : UsuarioDetailState()
    object Loading : UsuarioDetailState()
    data class Success(val usuario: UsuarioResponse) : UsuarioDetailState()
    data class Error(val message: String) : UsuarioDetailState()
}

class UsuarioViewModel : ViewModel() {
    
    private val _usuarioState = MutableStateFlow<UsuarioState>(UsuarioState.Idle)
    val usuarioState: StateFlow<UsuarioState> = _usuarioState.asStateFlow()
    
    private val _usuarioDetailState = MutableStateFlow<UsuarioDetailState>(UsuarioDetailState.Idle)
    val usuarioDetailState: StateFlow<UsuarioDetailState> = _usuarioDetailState.asStateFlow()
    
    fun listarUsuarios() {
        viewModelScope.launch {
            try {
                _usuarioState.value = UsuarioState.Loading
                
                val response = RetrofitClient.banquitoApi.listarUsuarios()
                
                if (response.isSuccessful && response.body() != null) {
                    _usuarioState.value = UsuarioState.Success(response.body()!!)
                } else {
                    _usuarioState.value = UsuarioState.Error("Error al cargar usuarios")
                }
            } catch (e: Exception) {
                _usuarioState.value = UsuarioState.Error("Error: ${e.message}")
            }
        }
    }
    
    fun obtenerUsuario(id: Long) {
        viewModelScope.launch {
            try {
                _usuarioDetailState.value = UsuarioDetailState.Loading
                
                val response = RetrofitClient.banquitoApi.obtenerUsuario(id)
                
                if (response.isSuccessful && response.body() != null) {
                    _usuarioDetailState.value = UsuarioDetailState.Success(response.body()!!)
                } else {
                    _usuarioDetailState.value = UsuarioDetailState.Error("Usuario no encontrado")
                }
            } catch (e: Exception) {
                _usuarioDetailState.value = UsuarioDetailState.Error("Error: ${e.message}")
            }
        }
    }
    
    suspend fun crearUsuario(request: UsuarioRequest): Result<UsuarioResponse> {
        return try {
            val response = RetrofitClient.banquitoApi.crearUsuario(request)
            
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al crear usuario"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun actualizarUsuario(id: Long, request: UsuarioRequest): Result<UsuarioResponse> {
        return try {
            val response = RetrofitClient.banquitoApi.actualizarUsuario(id, request)
            
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al actualizar usuario"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun eliminarUsuario(id: Long): Result<Unit> {
        return try {
            val response = RetrofitClient.banquitoApi.eliminarUsuario(id)
            
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al eliminar usuario"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
