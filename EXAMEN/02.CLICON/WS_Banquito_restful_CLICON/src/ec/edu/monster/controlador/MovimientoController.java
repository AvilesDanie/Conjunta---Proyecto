package ec.edu.monster.controlador;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import ec.edu.monster.modelo.Movimiento;
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
 * Controlador para gestión de movimientos bancarios
 * @author CLICON
 */
public class MovimientoController {
    private static final String API_URL = "http://10.40.18.255:8080/WS_JAVA_REST_BanQuito/api/movimientos";
    private static final Gson gson = new Gson();

    public List<Movimiento> listarMovimientosPorCuenta(String numCuenta) throws IOException {
        List<Movimiento> movimientos = new ArrayList<>();
        URL url = new URL(API_URL + "/cuenta/" + numCuenta);
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
                Type listType = new TypeToken<List<Movimiento>>() {}.getType();
                movimientos = gson.fromJson(jsonResponse.toString(), listType);
            }
        }
        conn.disconnect();
        return movimientos;
    }

    public boolean crearMovimiento(String numCuenta, String tipo, double monto, String descripcion) throws IOException {
        URL url = new URL(API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);

        JsonObject movimiento = new JsonObject();
        movimiento.addProperty("numCuenta", numCuenta);
        movimiento.addProperty("tipo", tipo);
        movimiento.addProperty("monto", monto);
        movimiento.addProperty("descripcion", descripcion);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(movimiento.toString().getBytes("UTF-8"));
            os.flush();
        }

        int responseCode = conn.getResponseCode();
        conn.disconnect();
        return responseCode == 200 || responseCode == 201;
    }
}
