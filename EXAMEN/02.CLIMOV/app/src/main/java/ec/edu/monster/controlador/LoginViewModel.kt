package ec.edu.monster.controlador

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ec.edu.monster.modelo.*
import ec.edu.monster.util.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val user: UsuarioResponse, val appType: String) : LoginState()
    data class Error(val message: String) : LoginState()
}

class LoginViewModel : ViewModel() {
    
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()
    
    fun loginBanquito(username: String, password: String) {
        viewModelScope.launch {
            try {
                _loginState.value = LoginState.Loading
                
                val response = RetrofitClient.banquitoApi.login(
                    UsuarioRequest(username, password)
                )
                
                if (response.isSuccessful && response.body() != null) {
                    val user = response.body()!!
                    _loginState.value = LoginState.Success(user, "BANQUITO")
                } else {
                    _loginState.value = LoginState.Error("Usuario o contraseña incorrectos")
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error("Error de conexión: ${e.message}")
            }
        }
    }
    
    fun loginComercializadora(username: String, password: String) {
        viewModelScope.launch {
            try {
                _loginState.value = LoginState.Loading
                
                val response = RetrofitClient.comercializadoraApi.login(
                    UsuarioComercializadoraRequest(username, password)
                )
                
                if (response.isSuccessful && response.body() != null) {
                    val userComercializadora = response.body()!!
                    // Convertir a UsuarioResponse para unificar
                    val user = UsuarioResponse(
                        id = userComercializadora.id,
                        username = userComercializadora.username,
                        rol = userComercializadora.rol,
                        activo = userComercializadora.activo
                    )
                    _loginState.value = LoginState.Success(user, "COMERCIALIZADORA")
                } else {
                    _loginState.value = LoginState.Error("Usuario o contraseña incorrectos")
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error("Error de conexión: ${e.message}")
            }
        }
    }
    
    fun resetState() {
        _loginState.value = LoginState.Idle
    }
}
