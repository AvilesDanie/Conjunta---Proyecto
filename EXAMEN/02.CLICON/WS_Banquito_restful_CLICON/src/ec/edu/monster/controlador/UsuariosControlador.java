package ec.edu.monster.controlador;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ec.edu.monster.modelo.ActualizarUsuarioModelo;
import ec.edu.monster.modelo.CrearUsuarioModelo;
import ec.edu.monster.modelo.UsuarioModelo;

import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class UsuariosControlador {

    private final String baseUrl;
    private final Gson gson;

    /**
     * @param sistema "banquito" o "comercializadora" para elegir el backend.
     */
    public UsuariosControlador(String sistema) {
        // 🔴 AJUSTA ESTOS CONTEXT PATH A TU SERVIDOR REAL
        if ("comercializadora".equalsIgnoreCase(sistema)) {
            this.baseUrl = "http://localhost:8080/WS_JAVA_REST_Comercializadora/api/usuarios";
        } else {
            // por defecto banquito
            this.baseUrl = "http://localhost:8080/WS_JAVA_REST_BanQuito/api/usuarios";
        }
        this.gson = new Gson();
    }

    // ====== CRUD ======

    public List<UsuarioModelo> listar() throws IOException {
        String json = realizarPeticion("GET", "", null, 200);
        Type listType = new TypeToken<List<UsuarioModelo>>() {}.getType();
        return gson.fromJson(json, listType);
    }

    public UsuarioModelo obtenerPorId(Long id) throws IOException {
        String json = realizarPeticion("GET", "/" + id, null, 200);
        return gson.fromJson(json, UsuarioModelo.class);
    }

    public UsuarioModelo crear(CrearUsuarioModelo req) throws IOException {
        String body = gson.toJson(req);
        String json = realizarPeticion("POST", "", body, 201);
        return gson.fromJson(json, UsuarioModelo.class);
    }

    public UsuarioModelo actualizar(Long id, ActualizarUsuarioModelo req) throws IOException {
        String body = gson.toJson(req);
        String json = realizarPeticion("PUT", "/" + id, body, 200);
        return gson.fromJson(json, UsuarioModelo.class);
    }

    public void eliminar(Long id) throws IOException {
        realizarPeticion("DELETE", "/" + id, null, 204);
    }

    // ====== Helper HTTP ======

    private String realizarPeticion(
            String metodo,
            String path,
            String body,
            int... codigosEsperados
    ) throws IOException {

        URL url = new URL(baseUrl + path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(metodo);
        conn.setRequestProperty("Accept", "application/json");

        if (body != null) {
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            OutputStream os = null;
            try {
                os = conn.getOutputStream();
                os.write(body.getBytes(StandardCharsets.UTF_8));
            } finally {
                if (os != null) {
                    try { os.close(); } catch (IOException e) { /* ignore */ }
                }
            }
        }

        int status = conn.getResponseCode();

        InputStream is;
        if (status >= 200 && status < 400) {
            is = conn.getInputStream();
        } else {
            is = conn.getErrorStream();
        }

        String respuesta = leerStream(is);
        conn.disconnect();

        boolean ok = false;
        if (codigosEsperados != null && codigosEsperados.length > 0) {
            for (int c : codigosEsperados) {
                if (status == c) {
                    ok = true;
                    break;
                }
            }
        } else {
            ok = (status >= 200 && status < 300);
        }

        if (!ok) {
            throw new IOException("Error HTTP " + status + ": " + respuesta);
        }

        return respuesta;
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
}
