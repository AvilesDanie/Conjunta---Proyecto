package ec.edu.monster.controlador;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Controlador para autenticación de usuarios
 * @author CLICON
 */
public class AuthController {
    private static final Gson gson = new Gson();
    
    /**
     * Realiza login en el servidor de BanQuito
     * @param username Usuario
     * @param password Contraseña
     * @return true si login exitoso, false en caso contrario
     * @throws IOException Si hay error de conexión
     */
    public boolean loginBanquito(String username, String password) throws IOException {
        String urlString = "http://10.40.18.255:8080/WS_JAVA_REST_BanQuito/api/usuarios/login";
        return realizarLogin(urlString, username, password);
    }
    
    /**
     * Realiza login en el servidor de Comercializadora
     * @param username Usuario
     * @param password Contraseña
     * @return true si login exitoso, false en caso contrario
     * @throws IOException Si hay error de conexión
     */
    public boolean loginComercializadora(String username, String password) throws IOException {
        String urlString = "http://10.40.18.255:8080/WS_JAVA_REST_Comercializadora/api/usuarios/login";
        return realizarLogin(urlString, username, password);
    }
    
    /**
     * Método privado que realiza el login HTTP
     */
    private boolean realizarLogin(String urlString, String username, String password) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        
        try {
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            
            // Crear JSON de login (contraseña en texto plano - el servidor la hashea)
            JsonObject loginJson = new JsonObject();
            loginJson.addProperty("username", username);
            loginJson.addProperty("password", password);
            
            // Enviar request
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = loginJson.toString().getBytes("UTF-8");
                os.write(input, 0, input.length);
            }
            
            int responseCode = conn.getResponseCode();
            
            if (responseCode == 200) {
                // Login exitoso
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), "UTF-8"))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        response.append(line.trim());
                    }
                    
                    // Parsear respuesta
                    JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
                    boolean activo = jsonResponse.get("activo").getAsBoolean();
                    
                    return activo; // Solo permite login si usuario está activo
                }
            } else if (responseCode == 401) {
                // Credenciales inválidas
                return false;
            } else {
                throw new IOException("Error en login: Código HTTP " + responseCode);
            }
            
        } finally {
            conn.disconnect();
        }
    }
}
