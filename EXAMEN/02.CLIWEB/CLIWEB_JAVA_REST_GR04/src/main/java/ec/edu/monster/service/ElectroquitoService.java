package ec.edu.monster.service;

import ec.edu.monster.config.AppConfig;
import ec.edu.monster.model.UsuarioLogin;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Servicio para comunicarse con el servidor REST de ElectroQuito
 * (Comercializadora).
 */
public class ElectroquitoService {

    /**
     * Valida las credenciales contra el servidor REST de ElectroQuito.
     *
     * @param credenciales Objeto con usuario y clave.
     * @return true si el login es correcto (HTTP 200), false en otro caso.
     * @throws IOException si hay problemas de conexión.
     */
    public boolean validarLogin(UsuarioLogin credenciales) throws IOException {

        String endpoint = AppConfig.COMERCIALIZADORA_API_BASE + "/usuarios/login";

        URL url = new URL(endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        try {
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);

            // Los nombres de campos deben coincidir con el DTO/entidad del servidor
            String jsonBody = "{\"usuario\":\"" + credenciales.getUsuario() + "\"," +
                              "\"clave\":\"" + credenciales.getClave() + "\"}";

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                os.write(input);
            }

            int status = conn.getResponseCode();
            return status == HttpURLConnection.HTTP_OK;

        } finally {
            conn.disconnect();
        }
    }
}
