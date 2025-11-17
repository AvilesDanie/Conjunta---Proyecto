package ec.edu.monster.controlador;

import ec.edu.monster.modelo.BanquitoModels.*;
import ec.edu.monster.util.ApiClient;

import java.io.IOException;

/**
 * Controlador para gestión de Cuentas
 */
public class CuentaController {
    
    private final ApiClient apiClient;
    
    public CuentaController() {
        this.apiClient = new ApiClient(ApiClient.BASE_URL_BANQUITO);
    }
    
    /**
     * Listar todas las cuentas
     */
    public CuentaResponse[] listarTodasCuentas() throws IOException {
        return apiClient.getArray("cuentas", CuentaResponse[].class);
    }
    
    /**
     * Alias de listarTodasCuentas para compatibilidad
     */
    public CuentaResponse[] listarTodasLasCuentas() throws IOException {
        return listarTodasCuentas();
    }
    
    /**
     * Obtener cuenta por número
     */
    public CuentaResponse obtenerCuenta(String numCuenta) throws IOException {
        return apiClient.get("cuentas/" + numCuenta, CuentaResponse.class);
    }
    
    /**
     * Listar cuentas de un cliente
     */
    public CuentaResponse[] listarCuentasPorCliente(String cedula) throws IOException {
        return apiClient.getArray("cuentas/cliente/" + cedula, CuentaResponse[].class);
    }
    
    /**
     * Crear nueva cuenta
     */
    public CuentaResponse crearCuenta(CuentaRequest request) throws IOException {
        return apiClient.post("cuentas", request, CuentaResponse.class);
    }
}
