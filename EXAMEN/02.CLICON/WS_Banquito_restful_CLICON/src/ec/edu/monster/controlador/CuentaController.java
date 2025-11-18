package ec.edu.monster.controlador;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ec.edu.monster.modelo.Cuenta;
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
 * Controlador para gestión de cuentas bancarias
 * @author CLICON
 */
public class CuentaController {
    private static final String API_URL = "http://10.40.18.255:8080/WS_JAVA_REST_BanQuito/api/cuentas";
    private static final Gson gson = new Gson();

    public List<Cuenta> listarCuentas() throws IOException {
        List<Cuenta> cuentas = new ArrayList<>();
        URL url = new URL(API_URL);
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
                Type listType = new TypeToken<List<Cuenta>>() {}.getType();
                cuentas = gson.fromJson(jsonResponse.toString(), listType);
            }
        }
        conn.disconnect();
        return cuentas;
    }

    public List<Cuenta> listarCuentasPorCliente(String cedula) throws IOException {
        List<Cuenta> cuentas = new ArrayList<>();
        URL url = new URL(API_URL + "/cliente/" + cedula);
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
                Type listType = new TypeToken<List<Cuenta>>() {}.getType();
                cuentas = gson.fromJson(jsonResponse.toString(), listType);
            }
        }
        conn.disconnect();
        return cuentas;
    }

    public Cuenta obtenerCuenta(String numCuenta) throws IOException {
        URL url = new URL(API_URL + "/" + numCuenta);
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
                Cuenta cuenta = gson.fromJson(jsonResponse.toString(), Cuenta.class);
                conn.disconnect();
                return cuenta;
            }
        }
        conn.disconnect();
        return null;
    }

    public boolean crearCuenta(Cuenta cuenta) throws IOException {
        URL url = new URL(API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);

        String json = gson.toJson(cuenta);
        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes("UTF-8"));
            os.flush();
        }

        int responseCode = conn.getResponseCode();
        conn.disconnect();
        return responseCode == 200 || responseCode == 201;
    }
}
