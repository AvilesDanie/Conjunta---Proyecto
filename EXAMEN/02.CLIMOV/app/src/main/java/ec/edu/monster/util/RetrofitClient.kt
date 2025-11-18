package ec.edu.monster.util

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    
    // URLs de los servidores (ajustar según tu configuración)
    private const val BANQUITO_BASE_URL = "http://10.40.18.255:8080/WS_JAVA_REST_BanQuito/api/"
    const val BASE_URL_COMERCIALIZADORA = "http://10.40.18.255:8080/WS_JAVA_REST_Comercializadora"
    private const val COMERCIALIZADORA_BASE_URL = "$BASE_URL_COMERCIALIZADORA/api/"
    
    // Logging interceptor para debug
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    
    // Cliente HTTP con timeout
    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
    
    // Gson con configuración especial para BigDecimal
    private val gson = GsonBuilder()
        .setLenient()
        .create()
    
    // Retrofit para BanQuito
    private val retrofitBanquito = Retrofit.Builder()
        .baseUrl(BANQUITO_BASE_URL)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
    
    // Retrofit para Comercializadora
    private val retrofitComercializadora = Retrofit.Builder()
        .baseUrl(COMERCIALIZADORA_BASE_URL)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
    
    // Instancias de los servicios
    val banquitoApi: BanquitoApiService by lazy {
        retrofitBanquito.create(BanquitoApiService::class.java)
    }
    
    val comercializadoraApi: ComercializadoraApiService by lazy {
        retrofitComercializadora.create(ComercializadoraApiService::class.java)
    }
}
