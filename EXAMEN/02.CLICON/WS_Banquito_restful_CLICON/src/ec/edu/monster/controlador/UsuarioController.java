package ec.edu.monster.controlador;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para gestión de usuarios
 * @author CLICON
 */
public class UsuarioController {
    private static final Gson gson = new Gson();
    
    public List<String> listarUsuarios(String servidor) throws IOException {
        String apiUrl = servidor.equals("banquito") 
            ? "http://10.40.18.255:8080/WS_JAVA_REST_BanQuito/api/usuarios"
            : "http://10.40.18.255:8080/WS_JAVA_REST_Comercializadora/api/usuarios";
            
        List<String> usuarios = new ArrayList<>();
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() == 200) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                StringBuilder jsonResponse = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    jsonResponse.append(line);
                }
                Type listType = new TypeToken<List<JsonObject>>() {}.getType();
                List<JsonObject> lista = gson.fromJson(jsonResponse.toString(), listType);
                for (JsonObject obj : lista) {
                    usuarios.add(obj.toString());
                }
            }
        }
        conn.disconnect();
        return usuarios;
    }

    public String obtenerUsuario(String servidor, Long id) throws IOException {
        String apiUrl = servidor.equals("banquito") 
            ? "http://10.40.18.255:8080/WS_JAVA_REST_BanQuito/api/usuarios/" + id
            : "http://10.40.18.255:8080/WS_JAVA_REST_Comercializadora/api/usuarios/" + id;
            
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() == 200) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                conn.disconnect();
                return response.toString();
            }
        }
        conn.disconnect();
        return null;
    }

    public boolean crearUsuario(String servidor, String username, String password, String rol) throws IOException {
        String apiUrl = servidor.equals("banquito") 
            ? "http://10.40.18.255:8080/WS_JAVA_REST_BanQuito/api/usuarios"
            : "http://10.40.18.255:8080/WS_JAVA_REST_Comercializadora/api/usuarios";
            
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);

        JsonObject usuario = new JsonObject();
        usuario.addProperty("username", username);
        usuario.addProperty("password", password);
        usuario.addProperty("rol", rol);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(usuario.toString().getBytes("UTF-8"));
            os.flush();
        }

        int responseCode = conn.getResponseCode();
        conn.disconnect();
        return responseCode == 200 || responseCode == 201;
    }

    public boolean eliminarUsuario(String servidor, Long id) throws IOException {
        String apiUrl = servidor.equals("banquito") 
            ? "http://10.40.18.255:8080/WS_JAVA_REST_BanQuito/api/usuarios/" + id
            : "http://10.40.18.255:8080/WS_JAVA_REST_Comercializadora/api/usuarios/" + id;
            
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("DELETE");

        int responseCode = conn.getResponseCode();
        conn.disconnect();
        return responseCode == 204 || responseCode == 200;
    }
}
