package ec.edu.monster.controlador;

import ec.edu.monster.modelo.ComercializadoraModels.*;
import ec.edu.monster.util.ApiClient;

import java.io.IOException;

/**
 * Controlador para gestión de Facturas
 */
public class FacturaController {
    
    private final ApiClient apiClient;
    
    public FacturaController() {
        this.apiClient = new ApiClient(ApiClient.BASE_URL_COMERCIALIZADORA);
    }
    
    /**
     * Listar todas las facturas
     */
    public FacturaResponse[] listarFacturas() throws IOException {
        return apiClient.getArray("facturas", FacturaResponse[].class);
    }
    
    /**
     * Obtener factura por ID
     */
    public FacturaResponse obtenerFactura(Long id) throws IOException {
        return apiClient.get("facturas/" + id, FacturaResponse.class);
    }
    
    /**
     * Crear nueva factura
     */
    public FacturaResponse crearFactura(FacturaRequest request) throws IOException {
        return apiClient.post("facturas", request, FacturaResponse.class);
    }
}
