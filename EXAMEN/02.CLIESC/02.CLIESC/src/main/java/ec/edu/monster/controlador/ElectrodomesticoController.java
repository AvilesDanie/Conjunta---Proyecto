package ec.edu.monster.controlador;

import ec.edu.monster.modelo.ComercializadoraModels.*;
import ec.edu.monster.util.ApiClient;

import java.io.IOException;

/**
 * Controlador para gestión de Electrodomésticos
 */
public class ElectrodomesticoController {
    
    private final ApiClient apiClient;
    
    public ElectrodomesticoController() {
        this.apiClient = new ApiClient(ApiClient.BASE_URL_COMERCIALIZADORA);
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
     * Actualizar electrodoméstico
     */
    public ElectrodomesticoResponse actualizarElectrodomestico(String codigo, ElectrodomesticoRequest request) throws IOException {
        return apiClient.put("electrodomesticos/" + codigo, request, ElectrodomesticoResponse.class);
    }
    
    /**
     * Eliminar electrodoméstico
     */
    public void eliminarElectrodomestico(String codigo) throws IOException {
        apiClient.delete("electrodomesticos/" + codigo);
    }
}
