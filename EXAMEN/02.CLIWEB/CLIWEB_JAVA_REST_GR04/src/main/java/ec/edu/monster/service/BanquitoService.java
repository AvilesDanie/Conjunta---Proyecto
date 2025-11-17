package ec.edu.monster.service;

import ec.edu.monster.config.AppConfig;
import ec.edu.monster.model.UsuarioLogin;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Servicio que se encarga de comunicarse con el servidor REST BanQuito.
 * Aquí NO hay lógica de presentación; solo llamada HTTP.
 */
public class BanquitoService {

    /**
     * Valida las credenciales contra el servidor REST.
     *
     * @param credenciales Objeto con usuario y clave.
     * @return true si el login es correcto (HTTP 200), false si es inválido.
     * @throws IOException si hay problemas de conexión.
     */
    public boolean validarLogin(UsuarioLogin credenciales) throws IOException {

        String endpoint = AppConfig.BANQUITO_BASE_URL + AppConfig.BANQUITO_LOGIN_PATH;

        URL url = new URL(endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        try {
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);

            // OJO: los nombres de campos deben coincidir con la entidad/DTO que reciba tu UsuarioController
            String jsonBody = "{\"usuario\":\"" + credenciales.getUsuario() + "\"," +
                              "\"clave\":\"" + credenciales.getClave() + "\"}";

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                os.write(input);
            }

            int status = conn.getResponseCode();
            // Suponemos que el servidor devuelve 200 si el login es correcto
            return status == HttpURLConnection.HTTP_OK;

        } finally {
            conn.disconnect();
        }
    }
}
