package ec.edu.monster.controlador;

import com.google.gson.Gson;
import ec.edu.monster.modelo.LoginRequestModelo;
import ec.edu.monster.modelo.UsuarioModelo;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class AuthControlador {

    private final String loginUrl;
    private final Gson gson;

    /**
     * @param loginUrl URL completa del endpoint de login, ej:
     *        "http://localhost:8080/WS_JAVA_REST_BanQuito/api/usuarios/login"
     */
    public AuthControlador(String loginUrl) {
        this.loginUrl = loginUrl;
        this.gson = new Gson();
    }

    /**
     * Intenta loguear al usuario.
     * @return UsuarioModelo si login OK, null si credenciales inválidas (401)
     * @throws IOException si hay otros errores HTTP o de red
     */
    public UsuarioModelo login(String username, String password) throws IOException {
        LoginRequestModelo req = new LoginRequestModelo();
        req.username = username;
        req.password = password;

        String body = gson.toJson(req);

        URL url = new URL(loginUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        // Enviar body
        OutputStream os = null;
        try {
            os = conn.getOutputStream();
            os.write(body.getBytes(StandardCharsets.UTF_8));
        } finally {
            if (os != null) {
                try { os.close(); } catch (IOException e) { /* ignore */ }
            }
        }

        int status = conn.getResponseCode();

        if (status == 200) {
            String json = leerStream(conn.getInputStream());
            conn.disconnect();
            return gson.fromJson(json, UsuarioModelo.class);
        } else if (status == 401) {
            // Credenciales incorrectas
            cerrarSilencioso(conn.getErrorStream());
            conn.disconnect();
            return null;
        } else {
            String error = leerStream(conn.getErrorStream());
            conn.disconnect();
            throw new IOException("Error HTTP " + status + ": " + error);
        }
    }

    private String leerStream(InputStream is) throws IOException {
        if (is == null) {
            return "";
        }
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            String linea;
            while ((linea = br.readLine()) != null) {
                sb.append(linea);
            }
            return sb.toString();
        } finally {
            if (br != null) {
                try { br.close(); } catch (IOException e) { /* ignore */ }
            }
        }
    }

    private void cerrarSilencioso(InputStream is) {
        if (is != null) {
            try { is.close(); } catch (IOException e) { /* ignore */ }
        }
    }
}
