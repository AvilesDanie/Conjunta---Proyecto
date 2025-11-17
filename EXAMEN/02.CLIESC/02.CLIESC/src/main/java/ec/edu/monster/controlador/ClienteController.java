package ec.edu.monster.controlador;

import ec.edu.monster.modelo.BanquitoModels.*;
import ec.edu.monster.util.ApiClient;

import java.io.IOException;

/**
 * Controlador para gestión de Clientes
 * Maneja todas las operaciones CRUD de clientes
 */
public class ClienteController {
    
    private final ApiClient apiClient;
    
    public ClienteController() {
        this.apiClient = new ApiClient(ApiClient.BASE_URL_BANQUITO);
    }
    
    /**
     * Listar todos los clientes
     */
    public ClienteResponse[] listarClientes() throws IOException {
        return apiClient.getArray("clientes", ClienteResponse[].class);
    }
    
    /**
     * Obtener cliente por cédula
     */
    public ClienteOnlyResponse obtenerCliente(String cedula) throws IOException {
        return apiClient.get("clientes/" + cedula, ClienteOnlyResponse.class);
    }
    
    /**
     * Crear nuevo cliente
     */
    public ClienteResponse crearCliente(ClienteRequest request) throws IOException {
        return apiClient.post("clientes", request, ClienteResponse.class);
    }
    
    /**
     * Actualizar cliente
     */
    public ClienteOnlyResponse actualizarCliente(String cedula, ClienteRequest request) throws IOException {
        return apiClient.put("clientes/" + cedula, request, ClienteOnlyResponse.class);
    }
    
    /**
     * Eliminar cliente
     */
    public void eliminarCliente(String cedula) throws IOException {
        apiClient.delete("clientes/" + cedula);
    }
}
