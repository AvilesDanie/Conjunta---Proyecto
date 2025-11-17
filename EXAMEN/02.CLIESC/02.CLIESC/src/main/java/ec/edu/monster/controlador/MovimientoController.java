package ec.edu.monster.controlador;

import ec.edu.monster.modelo.BanquitoModels.*;
import ec.edu.monster.util.ApiClient;

import java.io.IOException;

/**
 * Controlador para gestión de Movimientos
 */
public class MovimientoController {
    
    private final ApiClient apiClient;
    
    public MovimientoController() {
        this.apiClient = new ApiClient(ApiClient.BASE_URL_BANQUITO);
    }
    
    /**
     * Listar movimientos de una cuenta
     */
    public MovimientoResponse[] listarMovimientosPorCuenta(String numCuenta) throws IOException {
        return apiClient.getArray("movimientos/cuenta/" + numCuenta, MovimientoResponse[].class);
    }
    
    /**
     * Crear nuevo movimiento
     */
    public MovimientoResponse crearMovimiento(MovimientoRequest request) throws IOException {
        return apiClient.post("movimientos", request, MovimientoResponse.class);
    }
    
    /**
     * Registrar movimiento (alias de crearMovimiento)
     */
    public MovimientoResponse registrarMovimiento(MovimientoRequest request) throws IOException {
        return crearMovimiento(request);
    }
}
