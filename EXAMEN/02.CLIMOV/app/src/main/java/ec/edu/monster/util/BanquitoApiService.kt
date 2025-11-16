package ec.edu.monster.util

import ec.edu.monster.modelo.*
import retrofit2.Response
import retrofit2.http.*

interface BanquitoApiService {
    
    // ========== USUARIOS ==========
    @POST("usuarios/login")
    suspend fun login(@Body request: UsuarioRequest): Response<UsuarioResponse>
    
    @GET("usuarios")
    suspend fun listarUsuarios(): Response<List<UsuarioResponse>>
    
    @GET("usuarios/{id}")
    suspend fun obtenerUsuario(@Path("id") id: Long): Response<UsuarioResponse>
    
    @POST("usuarios")
    suspend fun crearUsuario(@Body request: UsuarioRequest): Response<UsuarioResponse>
    
    @PUT("usuarios/{id}")
    suspend fun actualizarUsuario(
        @Path("id") id: Long,
        @Body request: UsuarioRequest
    ): Response<UsuarioResponse>
    
    @DELETE("usuarios/{id}")
    suspend fun eliminarUsuario(@Path("id") id: Long): Response<Unit>
    
    // ========== CLIENTES ==========
    @GET("clientes")
    suspend fun listarClientes(): Response<List<ClienteResponse>>
    
    @GET("clientes/{cedula}")
    suspend fun obtenerCliente(@Path("cedula") cedula: String): Response<ClienteOnlyResponse>
    
    @POST("clientes")
    suspend fun crearCliente(@Body request: ClienteRequest): Response<ClienteResponse>
    
    @PUT("clientes/{cedula}")
    suspend fun actualizarCliente(
        @Path("cedula") cedula: String,
        @Body request: ClienteRequest
    ): Response<ClienteOnlyResponse>
    
    @DELETE("clientes/{cedula}")
    suspend fun eliminarCliente(@Path("cedula") cedula: String): Response<Unit>
    
    // ========== CUENTAS ==========
    @GET("cuentas")
    suspend fun listarCuentas(): Response<List<CuentaResponse>>
    
    @GET("cuentas/{numCuenta}")
    suspend fun obtenerCuenta(@Path("numCuenta") numCuenta: String): Response<CuentaResponse>
    
    @GET("cuentas/cliente/{cedula}")
    suspend fun listarCuentasPorCliente(@Path("cedula") cedula: String): Response<List<CuentaResponse>>
    
    @POST("cuentas")
    suspend fun crearCuenta(@Body request: CuentaRequest): Response<CuentaResponse>
    
    // ========== MOVIMIENTOS ==========
    @GET("movimientos/cuenta/{numCuenta}")
    suspend fun listarMovimientosPorCuenta(@Path("numCuenta") numCuenta: String): Response<List<MovimientoResponse>>
    
    @POST("movimientos")
    suspend fun crearMovimiento(@Body request: MovimientoRequest): Response<MovimientoResponse>
    
    // ========== CRÉDITOS ==========
    @POST("creditos/evaluar")
    suspend fun evaluarCredito(@Body request: SolicitudCredito): Response<ResultadoEvaluacion>
    
    @POST("creditos")
    suspend fun crearCredito(@Body request: SolicitudCredito): Response<CreditoResponse>
    
    @GET("creditos/{id}")
    suspend fun obtenerCredito(@Path("id") id: Long): Response<CreditoResponse>
    
    // ========== CUOTAS AMORTIZACIÓN ==========
    @GET("cuotas/credito/{idCredito}")
    suspend fun listarCuotasPorCredito(@Path("idCredito") idCredito: Long): Response<List<CuotaAmortizacion>>
    
    @GET("cuotas/{id}")
    suspend fun obtenerCuota(@Path("id") id: Long): Response<CuotaAmortizacion>
    
    @PUT("cuotas/{id}")
    suspend fun actualizarCuota(
        @Path("id") id: Long,
        @Body cuota: CuotaAmortizacion
    ): Response<CuotaAmortizacion>
    
    @DELETE("cuotas/{id}")
    suspend fun eliminarCuota(@Path("id") id: Long): Response<Unit>
}
