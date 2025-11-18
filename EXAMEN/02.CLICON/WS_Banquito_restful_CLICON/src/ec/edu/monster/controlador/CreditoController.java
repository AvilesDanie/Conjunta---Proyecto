package ec.edu.monster.controlador;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Controlador para gestión de créditos
 * @author CLICON
 */
public class CreditoController {
    private static final String API_URL = "http://10.40.18.255:8080/WS_JAVA_REST_BanQuito/api/creditos";
    private static final Gson gson = new Gson();

    /**
     * Evalúa si un cliente es sujeto de crédito
     */
    public String evaluarCredito(String cedula, double monto, int plazoMeses) throws IOException {
        URL url = new URL(API_URL + "/evaluar");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);

        JsonObject solicitud = new JsonObject();
        solicitud.addProperty("cedula", cedula);
        solicitud.addProperty("precioProducto", monto);
        solicitud.addProperty("plazoMeses", plazoMeses);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(solicitud.toString().getBytes("UTF-8"));
            os.flush();
        }

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

    /**
     * Crea un crédito
     */
    public String crearCredito(String cedula, double monto, int plazoMeses, String numCuenta) throws IOException {
        URL url = new URL(API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);

        JsonObject solicitud = new JsonObject();
        solicitud.addProperty("cedula", cedula);
        solicitud.addProperty("precioProducto", monto);
        solicitud.addProperty("plazoMeses", plazoMeses);
        solicitud.addProperty("numCuentaCredito", numCuenta);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(solicitud.toString().getBytes("UTF-8"));
            os.flush();
        }

        int responseCode = conn.getResponseCode();
        if (responseCode == 200 || responseCode == 201) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                conn.disconnect();
                return response.toString();
            }
        } else if (responseCode == 400) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getErrorStream()))) {
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

    /**
     * Obtiene un crédito por ID
     */
    public String obtenerCredito(Long id) throws IOException {
        URL url = new URL(API_URL + "/" + id);
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
}
