package ec.edu.monster.controlador;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ec.edu.monster.modelo.FacturaModelo;
import ec.edu.monster.modelo.FacturaRequestModelo;

import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class FacturasControlador {

    // 🔴 AJUSTA ESTA URL A TU CONTEXTO REAL
    private static final String BASE_URL = "http://localhost:8080/WS_JAVA_REST_Comercializadora/api/facturas";

    private final Gson gson;

    public FacturasControlador() {
        this.gson = new Gson();
    }

    // ===== Listar facturas =====
    public List<FacturaModelo> listar() throws IOException {
        String json = realizarPeticion("GET", "", null, 200);
        Type listType = new TypeToken<List<FacturaModelo>>() {}.getType();
        return gson.fromJson(json, listType);
    }

    // ===== Obtener factura por id =====
    public FacturaModelo obtenerPorId(Long id) throws IOException {
        String json = realizarPeticion("GET", "/" + id, null, 200);
        return gson.fromJson(json, FacturaModelo.class);
    }

    // ===== Crear factura =====
    public FacturaModelo crearFactura(FacturaRequestModelo req) throws IOException {
        String body = gson.toJson(req);
        String json = realizarPeticion("POST", "", body, 201);
        return gson.fromJson(json, FacturaModelo.class);
    }

    // ===== Helper HTTP =====
    private String realizarPeticion(
            String metodo,
            String path,
            String body,
            int... codigosEsperados
    ) throws IOException {

        URL url = new URL(BASE_URL + path);
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
        InputStream is = (status >= 200 && status < 400) ? conn.getInputStream() : conn.getErrorStream();
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
