package ec.edu.monster.controlador;

import ec.edu.monster.modelo.BanquitoModels.*;
import ec.edu.monster.util.ApiClient;

import java.io.IOException;

/**
 * Controlador para gestión de Créditos
 */
public class CreditoController {
    
    private final ApiClient apiClient;
    
    public CreditoController() {
        this.apiClient = new ApiClient(ApiClient.BASE_URL_BANQUITO);
    }
    
    /**
     * Evaluar crédito (sin crearlo)
     */
    public ResultadoEvaluacion evaluarCredito(SolicitudCredito solicitud) throws IOException {
        return apiClient.post("creditos/evaluar", solicitud, ResultadoEvaluacion.class);
    }
    
    /**
     * Crear crédito y generar tabla de amortización
     */
    public CreditoResponse crearCredito(SolicitudCredito solicitud) throws IOException {
        return apiClient.post("creditos", solicitud, CreditoResponse.class);
    }
    
    /**
     * Obtener crédito por ID
     */
    public CreditoResponse obtenerCredito(Long id) throws IOException {
        return apiClient.get("creditos/" + id, CreditoResponse.class);
    }
    
    /**
     * Listar cuotas de un crédito (tabla de amortización)
     */
    public CuotaAmortizacion[] listarCuotas(Long idCredito) throws IOException {
        return apiClient.getArray("cuotas/credito/" + idCredito, CuotaAmortizacion[].class);
    }
    
    /**
     * Obtener cuota por ID
     */
    public CuotaAmortizacion obtenerCuota(Long id) throws IOException {
        return apiClient.get("cuotas/" + id, CuotaAmortizacion.class);
    }
    
    /**
     * Actualizar cuota (marcar como PAGADA)
     */
    public CuotaAmortizacion actualizarCuota(Long id, ActualizarCuotaRequest request) throws IOException {
        return apiClient.put("cuotas/" + id, request, CuotaAmortizacion.class);
    }
    
    /**
     * Actualizar estado de cuota (alias de actualizarCuota)
     */
    public CuotaAmortizacion actualizarEstadoCuota(int id, ActualizarCuotaRequest request) throws IOException {
        return actualizarCuota((long) id, request);
    }
    
    /**
     * Listar todos los créditos
     */
    public CreditoResponse[] listarCreditos() throws IOException {
        return apiClient.getArray("creditos", CreditoResponse[].class);
    }
    
    /**
     * Anular cuota
     */
    public void anularCuota(Long id) throws IOException {
        apiClient.delete("cuotas/" + id);
    }
}
