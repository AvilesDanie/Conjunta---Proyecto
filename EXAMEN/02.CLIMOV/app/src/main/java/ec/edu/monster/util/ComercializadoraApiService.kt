package ec.edu.monster.util

import ec.edu.monster.modelo.*
import retrofit2.Response
import retrofit2.http.*

interface ComercializadoraApiService {
    
    // ========== USUARIOS ==========
    @POST("usuarios/login")
    suspend fun login(@Body request: UsuarioComercializadoraRequest): Response<UsuarioComercializadoraResponse>
    
    // ========== ELECTRODOMÉSTICOS ==========
    @GET("electrodomesticos")
    suspend fun listarElectrodomesticos(): Response<List<ElectrodomesticoResponse>>
    
    @POST("electrodomesticos")
    suspend fun crearElectrodomestico(@Body request: ElectrodomesticoRequest): Response<ElectrodomesticoResponse>
    
    // ========== FACTURAS ==========
    @GET("facturas")
    suspend fun listarFacturas(): Response<List<FacturaResponse>>
    
    @GET("facturas/{id}")
    suspend fun obtenerFactura(@Path("id") id: Long): Response<FacturaResponse>
    
    @POST("facturas")
    suspend fun crearFactura(@Body request: FacturaRequest): Response<FacturaResponse>
}
