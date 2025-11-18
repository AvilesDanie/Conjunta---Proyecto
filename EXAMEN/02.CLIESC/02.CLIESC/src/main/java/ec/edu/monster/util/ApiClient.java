package ec.edu.monster.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Cliente HTTP para consumir las APIs REST de BanQuito y Comercializadora
 */
public class ApiClient {
    
    // URLs Base
    public static final String BASE_URL_BANQUITO = "http://192.168.100.17:8080/WS_JAVA_REST_BanQuito/api/";
    public static final String BASE_URL_COMERCIALIZADORA = "http://192.168.100.17:8080/WS_JAVA_REST_Comercializadora/api/";
    
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    
    private final OkHttpClient client;
    private final Gson gson;
    private final String baseUrl;
    
    /**
     * Constructor
     * @param baseUrl URL base (BANQUITO o COMERCIALIZADORA)
     */
    public ApiClient(String baseUrl) {
        this.baseUrl = baseUrl;
        
        // Configurar OkHttpClient con timeouts de 30 segundos
        this.client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
        
        // Configurar Gson
        this.gson = new GsonBuilder()
                .setLenient()
                .create();
    }
    
    /**
     * GET Request
     */
    public <T> T get(String endpoint, Class<T> responseClass) throws IOException {
        Request request = new Request.Builder()
                .url(baseUrl + endpoint)
                .get()
                .build();
        
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error HTTP: " + response.code() + " - " + response.message());
            }
            
            String responseBody = response.body().string();
            return gson.fromJson(responseBody, responseClass);
        }
    }
    
    /**
     * POST Request
     */
    public <T> T post(String endpoint, Object requestBody, Class<T> responseClass) throws IOException {
        String json = gson.toJson(requestBody);
        RequestBody body = RequestBody.create(json, JSON);
        
        Request request = new Request.Builder()
                .url(baseUrl + endpoint)
                .post(body)
                .build();
        
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "";
                throw new IOException("Error HTTP: " + response.code() + " - " + errorBody);
            }
            
            String responseBody = response.body().string();
            return gson.fromJson(responseBody, responseClass);
        }
    }
    
    /**
     * PUT Request
     */
    public <T> T put(String endpoint, Object requestBody, Class<T> responseClass) throws IOException {
        String json = gson.toJson(requestBody);
        RequestBody body = RequestBody.create(json, JSON);
        
        Request request = new Request.Builder()
                .url(baseUrl + endpoint)
                .put(body)
                .build();
        
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "";
                throw new IOException("Error HTTP: " + response.code() + " - " + errorBody);
            }
            
            String responseBody = response.body().string();
            return gson.fromJson(responseBody, responseClass);
        }
    }
    
    /**
     * DELETE Request
     */
    public void delete(String endpoint) throws IOException {
        Request request = new Request.Builder()
                .url(baseUrl + endpoint)
                .delete()
                .build();
        
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "";
                throw new IOException("Error HTTP: " + response.code() + " - " + errorBody);
            }
        }
    }
    
    /**
     * GET Request que retorna array
     */
    public <T> T[] getArray(String endpoint, Class<T[]> arrayClass) throws IOException {
        Request request = new Request.Builder()
                .url(baseUrl + endpoint)
                .get()
                .build();
        
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error HTTP: " + response.code() + " - " + response.message());
            }
            
            String responseBody = response.body().string();
            return gson.fromJson(responseBody, arrayClass);
        }
    }
    
    /**
     * Parsear JSON string a objeto
     */
    public <T> T parseJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }
}
