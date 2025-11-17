package ec.edu.monster.controlador;

import ec.edu.monster.modelo.BanquitoModels.*;
import ec.edu.monster.util.ApiClient;

import java.io.IOException;

/**
 * Controlador para gestión de Usuarios de BanQuito
 */
public class UsuarioController {
    
    private final ApiClient apiClient;
    
    public UsuarioController() {
        this.apiClient = new ApiClient(ApiClient.BASE_URL_BANQUITO);
    }
    
    /**
     * Login de usuario
     */
    public UsuarioResponse login(String username, String password) throws IOException {
        UsuarioRequest request = new UsuarioRequest(username, password);
        return apiClient.post("usuarios/login", request, UsuarioResponse.class);
    }
    
    /**
     * Listar todos los usuarios
     */
    public UsuarioResponse[] listarUsuarios() throws IOException {
        return apiClient.getArray("usuarios", UsuarioResponse[].class);
    }
    
    /**
     * Obtener usuario por ID
     */
    public UsuarioResponse obtenerUsuario(Long id) throws IOException {
        return apiClient.get("usuarios/" + id, UsuarioResponse.class);
    }
    
    /**
     * Crear nuevo usuario
     */
    public UsuarioResponse crearUsuario(UsuarioRequest request) throws IOException {
        return apiClient.post("usuarios", request, UsuarioResponse.class);
    }
    
    /**
     * Actualizar usuario
     */
    public UsuarioResponse actualizarUsuario(Long id, UsuarioRequest request) throws IOException {
        return apiClient.put("usuarios/" + id, request, UsuarioResponse.class);
    }
    
    /**
     * Eliminar usuario
     */
    public void eliminarUsuario(Long id) throws IOException {
        apiClient.delete("usuarios/" + id);
    }
}
