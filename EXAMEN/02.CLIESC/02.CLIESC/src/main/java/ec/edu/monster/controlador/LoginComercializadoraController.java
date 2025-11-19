package ec.edu.monster.controlador;

import ec.edu.monster.modelo.ComercializadoraDTOs.*;
import ec.edu.monster.util.ApiClient;

import java.io.IOException;

/**
 * Controlador para login en Comercializadora
 */
public class LoginComercializadoraController {
    
    private final ApiClient apiClient;
    
    public LoginComercializadoraController() {
        this.apiClient = new ApiClient(ApiClient.BASE_URL_COMERCIALIZADORA);
    }
    
    /**
     * Login de usuario en comercializadora
     */
    public UsuarioResponse login(String username, String password) throws IOException {
        UsuarioRequest request = new UsuarioRequest(username, password);
        return apiClient.post("usuarios/login", request, UsuarioResponse.class);
    }
}
