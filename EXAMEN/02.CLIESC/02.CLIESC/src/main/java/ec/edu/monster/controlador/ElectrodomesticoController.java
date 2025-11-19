package ec.edu.monster.controlador;

import ec.edu.monster.modelo.ComercializadoraDTOs.*;
import ec.edu.monster.util.ApiClient;
import okhttp3.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;

/**
 * Controlador para gestión de Electrodomésticos
 */
public class ElectrodomesticoController {
    
    private final ApiClient apiClient;
    private final OkHttpClient httpClient;
    
    public ElectrodomesticoController() {
        this.apiClient = new ApiClient(ApiClient.BASE_URL_COMERCIALIZADORA);
        this.httpClient = new OkHttpClient();
    }
    
    /**
     * Listar todos los electrodomésticos
     */
    public ElectrodomesticoResponse[] listarElectrodomesticos() throws IOException {
        return apiClient.getArray("electrodomesticos", ElectrodomesticoResponse[].class);
    }
    
    /**
     * Alias de listarElectrodomesticos
     */
    public ElectrodomesticoResponse[] listar() throws IOException {
        return listarElectrodomesticos();
    }
    
    /**
     * Obtener electrodoméstico por código
     */
    public ElectrodomesticoResponse obtenerElectrodomestico(String codigo) throws IOException {
        ElectrodomesticoResponse[] lista = listarElectrodomesticos();
        for (ElectrodomesticoResponse electro : lista) {
            if (electro.codigo.equals(codigo)) {
                return electro;
            }
        }
        throw new IOException("Electrodoméstico no encontrado");
    }
    
    /**
     * Crear nuevo electrodoméstico
     */
    public ElectrodomesticoResponse crearElectrodomestico(ElectrodomesticoRequest request) throws IOException {
        return apiClient.post("electrodomesticos", request, ElectrodomesticoResponse.class);
    }
    
    /**
     * Actualizar electrodoméstico por ID
     */
    public ElectrodomesticoResponse actualizarElectrodomestico(Long id, ElectrodomesticoRequest request) throws IOException {
        return apiClient.put("electrodomesticos/" + id, request, ElectrodomesticoResponse.class);
    }
    
    /**
     * Actualizar electrodoméstico por código (compatibilidad)
     */
    public ElectrodomesticoResponse actualizarElectrodomestico(String codigo, ElectrodomesticoRequest request) throws IOException {
        return apiClient.put("electrodomesticos/" + codigo, request, ElectrodomesticoResponse.class);
    }
    
    /**
     * Eliminar electrodoméstico por ID
     */
    public void eliminarElectrodomestico(Long id) throws IOException {
        apiClient.delete("electrodomesticos/" + id);
    }
    
    /**
     * Eliminar electrodoméstico por código (compatibilidad)
     */
    public void eliminarElectrodomestico(String codigo) throws IOException {
        apiClient.delete("electrodomesticos/" + codigo);
    }
    
    /**
     * Crear electrodoméstico con imagen usando multipart/form-data
     */
    public ElectrodomesticoResponse crearElectrodomesticoConImagen(
            String codigo, String nombre, double precioVenta, File imagenFile) throws IOException {
        
        // Crear el cuerpo multipart con OkHttp
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("codigo", codigo)
                .addFormDataPart("nombre", nombre)
                .addFormDataPart("precioVenta", String.valueOf(precioVenta))
                .addFormDataPart("imagen", imagenFile.getName(),
                        RequestBody.create(imagenFile, MediaType.parse("image/*")))
                .build();
        
        // Construir la request
        Request request = new Request.Builder()
                .url(ApiClient.BASE_URL_COMERCIALIZADORA + "electrodomesticos")
                .post(requestBody)
                .build();
        
        // Ejecutar la petición
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "Sin respuesta";
                throw new IOException("Error al crear electrodoméstico. Código: " + response.code() + " - " + errorBody);
            }
            
            String responseBody = response.body().string();
            return apiClient.parseJson(responseBody, ElectrodomesticoResponse.class);
        }
    }
    
    /**
     * Actualizar electrodoméstico con imagen usando multipart/form-data (por ID)
     */
    public ElectrodomesticoResponse actualizarElectrodomesticoConImagen(
            Long id, String codigo, String nombre, double precioVenta, File imagenFile) throws IOException {
        
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("codigo", codigo)
                .addFormDataPart("nombre", nombre)
                .addFormDataPart("precioVenta", String.valueOf(precioVenta));
        
        // Solo añadir imagen si se proporciona
        if (imagenFile != null) {
            builder.addFormDataPart("imagen", imagenFile.getName(),
                    RequestBody.create(imagenFile, MediaType.parse("image/*")));
        }
        
        RequestBody requestBody = builder.build();
        
        Request request = new Request.Builder()
                .url(ApiClient.BASE_URL_COMERCIALIZADORA + "electrodomesticos/" + id)
                .put(requestBody)
                .build();
        
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "Sin respuesta";
                throw new IOException("Error al actualizar electrodoméstico. Código: " + response.code() + " - " + errorBody);
            }
            
            String responseBody = response.body().string();
            return apiClient.parseJson(responseBody, ElectrodomesticoResponse.class);
        }
    }
    
    /**
     * Actualizar electrodoméstico con imagen usando multipart/form-data (por código, compatibilidad)
     */
    public ElectrodomesticoResponse actualizarElectrodomesticoConImagen(
            String codigo, String nombre, double precioVenta, File imagenFile) throws IOException {
        
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("nombre", nombre)
                .addFormDataPart("precioVenta", String.valueOf(precioVenta));
        
        // Solo añadir imagen si se proporciona
        if (imagenFile != null) {
            builder.addFormDataPart("imagen", imagenFile.getName(),
                    RequestBody.create(imagenFile, MediaType.parse("image/*")));
        }
        
        RequestBody requestBody = builder.build();
        
        Request request = new Request.Builder()
                .url(ApiClient.BASE_URL_COMERCIALIZADORA + "electrodomesticos/" + codigo)
                .put(requestBody)
                .build();
        
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "Sin respuesta";
                throw new IOException("Error al actualizar electrodoméstico. Código: " + response.code() + " - " + errorBody);
            }
            
            String responseBody = response.body().string();
            return apiClient.parseJson(responseBody, ElectrodomesticoResponse.class);
        }
    }
}

