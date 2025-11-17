package ec.edu.monster.util;

/**
 * Gestor de sesión de usuario
 * Almacena información del usuario autenticado
 */
public class SessionManager {
    
    private static SessionManager instance;
    
    private Long userId;
    private String username;
    private String rol;
    private boolean isLoggedIn;
    private String tipoApp; // "BANQUITO" o "COMERCIALIZADORA"
    
    private SessionManager() {
        this.isLoggedIn = false;
    }
    
    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }
    
    /**
     * Inicia sesión de usuario
     */
    public void login(Long userId, String username, String rol, String tipoApp) {
        this.userId = userId;
        this.username = username;
        this.rol = rol;
        this.tipoApp = tipoApp;
        this.isLoggedIn = true;
    }
    
    /**
     * Cierra sesión de usuario
     */
    public void logout() {
        this.userId = null;
        this.username = null;
        this.rol = null;
        this.tipoApp = null;
        this.isLoggedIn = false;
    }
    
    // Getters
    public Long getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getRol() { return rol; }
    public boolean isLoggedIn() { return isLoggedIn; }
    public String getTipoApp() { return tipoApp; }
}
