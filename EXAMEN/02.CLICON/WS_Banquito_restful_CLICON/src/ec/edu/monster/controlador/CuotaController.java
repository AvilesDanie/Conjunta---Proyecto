package ec.edu.monster.controlador;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import ec.edu.monster.modelo.Cuota;
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
 * Controlador para gestión de cuotas
 * @author CLICON
 */
public class CuotaController {
    private static final String API_URL = "http://10.40.18.255:8080/WS_JAVA_REST_BanQuito/api/cuotas";
    private static final Gson gson = new Gson();

    public List<Cuota> listarCuotasPorCredito(Long idCredito) throws IOException {
        List<Cuota> cuotas = new ArrayList<>();
        URL url = new URL(API_URL + "/credito/" + idCredito);
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
                Type listType = new TypeToken<List<Cuota>>() {}.getType();
                cuotas = gson.fromJson(jsonResponse.toString(), listType);
            }
        }
        conn.disconnect();
        return cuotas;
    }

    public Cuota obtenerCuota(Long id) throws IOException {
        URL url = new URL(API_URL + "/" + id);
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
                Cuota cuota = gson.fromJson(jsonResponse.toString(), Cuota.class);
                conn.disconnect();
                return cuota;
            }
        }
        conn.disconnect();
        return null;
    }

    public boolean actualizarEstadoCuota(Long id, String estado) throws IOException {
        URL url = new URL(API_URL + "/" + id);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);

        JsonObject actualizacion = new JsonObject();
        actualizacion.addProperty("estado", estado);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(actualizacion.toString().getBytes("UTF-8"));
            os.flush();
        }

        int responseCode = conn.getResponseCode();
        conn.disconnect();
        return responseCode == 200;
    }

    public boolean anularCuota(Long id) throws IOException {
        URL url = new URL(API_URL + "/" + id);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("DELETE");

        int responseCode = conn.getResponseCode();
        conn.disconnect();
        return responseCode == 204 || responseCode == 200;
    }
}
