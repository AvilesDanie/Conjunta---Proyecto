package ec.edu.monster.controlador;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ec.edu.monster.modelo.CuentaCrearRequest;
import ec.edu.monster.modelo.CuentaModelo;

import java.io.*;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class CuentasControlador {

    // 🔴 AJUSTA ESTA URL A TU SERVIDOR REAL
    // Ejemplo con tu IP del splash: 10.40.18.255
    private static final String BASE_URL = "http://localhost:8080/WS_JAVA_REST_BanQuito/api/cuentas";

    private final Gson gson;

    public CuentasControlador() {
        this.gson = new Gson();
    }

    // =========== Métodos públicos ===========

    public List<CuentaModelo> listarTodas() throws IOException {
        String json = realizarPeticion("GET", "", null, 200);
        Type listType = new TypeToken<List<CuentaModelo>>() {}.getType();
        return gson.fromJson(json, listType);
    }

    public CuentaModelo obtenerPorNumero(String numCuenta) throws IOException {
        String json = realizarPeticion("GET", "/" + numCuenta, null, 200);
        return gson.fromJson(json, CuentaModelo.class);
    }

    public List<CuentaModelo> listarPorCedulaCliente(String cedula) throws IOException {
        String json = realizarPeticion("GET", "/cliente/" + cedula, null, 200);
        Type listType = new TypeToken<List<CuentaModelo>>() {}.getType();
        return gson.fromJson(json, listType);
    }

    public CuentaModelo crearCuenta(CuentaCrearRequest req) throws IOException {
        String body = gson.toJson(req);
        String json = realizarPeticion("POST", "", body, 201);
        return gson.fromJson(json, CuentaModelo.class);
    }

    // =========== Helper HTTP ===========

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
