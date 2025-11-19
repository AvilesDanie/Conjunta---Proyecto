package ec.edu.monster.controlador

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ec.edu.monster.modelo.*
import ec.edu.monster.util.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.math.BigDecimal

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
    
    suspend fun crearElectrodomestico(
        codigo: String,
        nombre: String,
        precioVenta: BigDecimal,
        imagenFile: File?
    ): Result<ElectrodomesticoResponse> {
        return try {
            val codigoBody = codigo.toRequestBody("text/plain".toMediaTypeOrNull())
            val nombreBody = nombre.toRequestBody("text/plain".toMediaTypeOrNull())
            val precioBody = precioVenta.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            
            val imagenPart = imagenFile?.let {
                val requestFile = it.asRequestBody("image/*".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("imagen", it.name, requestFile)
            }
            
            val response = RetrofitClient.comercializadoraApi.crearElectrodomestico(
                codigoBody,
                nombreBody,
                precioBody,
                imagenPart
            )
            
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al crear electrodoméstico"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun actualizarElectrodomestico(
        id: Long,
        codigo: String,
        nombre: String,
        precioVenta: BigDecimal,
        imagenFile: File?
    ): Result<ElectrodomesticoResponse> {
        return try {
            val codigoBody = codigo.toRequestBody("text/plain".toMediaTypeOrNull())
            val nombreBody = nombre.toRequestBody("text/plain".toMediaTypeOrNull())
            val precioBody = precioVenta.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            
            val imagenPart = imagenFile?.let {
                val requestFile = it.asRequestBody("image/*".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("imagen", it.name, requestFile)
            }
            
            val response = RetrofitClient.comercializadoraApi.actualizarElectrodomestico(
                id,
                codigoBody,
                nombreBody,
                precioBody,
                imagenPart
            )
            
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                Result.failure(Exception(errorBody ?: "Error al actualizar electrodoméstico"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun eliminarElectrodomestico(id: Long): Result<Unit> {
        return try {
            val response = RetrofitClient.comercializadoraApi.eliminarElectrodomestico(id)
            
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                val errorBody = response.errorBody()?.string()
                Result.failure(Exception(errorBody ?: "Error al eliminar electrodoméstico"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
