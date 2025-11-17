package ec.edu.monster.config;

/**
 * Configuración general del cliente web.
 * Ajusta aquí las URLs de los servicios REST.
 */
public class AppConfig {

    // ================== BANQUITO ==================

    // Ajusta según ApplicationConfig de WS_JAVA_REST_BanQuito
    public static final String BANQUITO_BASE_URL =
            "http://localhost:8080/WS_JAVA_REST_BanQuito/api/clientes";

    // Ajusta este path al @Path real de UsuarioController en BanQuito
    public static final String BANQUITO_LOGIN_PATH =
            "/ec.edu.monster.usuario/login";


    // ================== ELECTROQUITO ==================

    // Ajusta según ApplicationConfig de WS_JAVA_REST_Comercializadora
    public static final String ELECTRO_BASE_URL =
            "http://localhost:8080/WS_JAVA_REST_Comercializadora/webresources";

    // Ajusta este path al @Path real de UsuarioController en Comercializadora
    public static final String ELECTRO_LOGIN_PATH =
            "/ec.edu.monster.usuario/login";
}
