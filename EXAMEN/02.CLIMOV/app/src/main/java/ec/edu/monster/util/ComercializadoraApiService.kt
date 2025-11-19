package ec.edu.monster.util

import ec.edu.monster.modelo.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ComercializadoraApiService {
    
    // ========== USUARIOS ==========
    @POST("usuarios/login")
    suspend fun login(@Body request: UsuarioComercializadoraRequest): Response<UsuarioComercializadoraResponse>
    
    // ========== ELECTRODOMÉSTICOS ==========
    @GET("electrodomesticos")
    suspend fun listarElectrodomesticos(): Response<List<ElectrodomesticoResponse>>
    
    @Multipart
    @POST("electrodomesticos")
    suspend fun crearElectrodomestico(
        @Part("codigo") codigo: RequestBody,
        @Part("nombre") nombre: RequestBody,
        @Part("precioVenta") precioVenta: RequestBody,
        @Part imagen: MultipartBody.Part?
    ): Response<ElectrodomesticoResponse>
    
    @GET("electrodomesticos/imagen/{nombreArchivo}")
    suspend fun obtenerImagen(@Path("nombreArchivo") nombreArchivo: String): Response<okhttp3.ResponseBody>
    
    @Multipart
    @PUT("electrodomesticos/{id}")
    suspend fun actualizarElectrodomestico(
        @Path("id") id: Long,
        @Part("codigo") codigo: RequestBody,
        @Part("nombre") nombre: RequestBody,
        @Part("precioVenta") precioVenta: RequestBody,
        @Part imagen: MultipartBody.Part?
    ): Response<ElectrodomesticoResponse>
    
    @DELETE("electrodomesticos/{id}")
    suspend fun eliminarElectrodomestico(@Path("id") id: Long): Response<Unit>
    
    // ========== FACTURAS ==========
    @GET("facturas")
    suspend fun listarFacturas(): Response<List<FacturaResponse>>
    
    @GET("facturas/{id}")
    suspend fun obtenerFactura(@Path("id") id: Long): Response<FacturaResponse>
    
    @POST("facturas")
    suspend fun crearFactura(@Body request: FacturaRequest): Response<FacturaResponse>
}
