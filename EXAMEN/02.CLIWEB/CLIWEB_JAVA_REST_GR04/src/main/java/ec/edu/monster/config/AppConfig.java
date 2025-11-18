package ec.edu.monster.config;

/**
 * Configuración centralizada de endpoints REST.
 * Cambia las constantes (o define las system properties indicadas) para apuntar
 * el cliente web a otro host sin editar cada controlador.
 */
public final class AppConfig {

    private AppConfig() {
    }

    // ================== BANQUITO ==================
    public static final String BANQUITO_API_BASE =
            System.getProperty("banquito.api.base",
                    "http://localhost:8080/WS_JAVA_REST_BanQuito/api");

    // ================== COMERCIALIZADORA / ELECTROQUITO ==================
    public static final String COMERCIALIZADORA_API_BASE =
            System.getProperty("comercializadora.api.base",
                    "http://localhost:8080/WS_JAVA_REST_Comercializadora/api");

    public static final String COMERCIALIZADORA_HOST_BASE =
            System.getProperty("comercializadora.host.base",
                    "http://localhost:8080/WS_JAVA_REST_Comercializadora");
}
