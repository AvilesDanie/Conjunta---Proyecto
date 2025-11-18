package ec.edu.monster.controlador;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ec.edu.monster.modelo.Cliente;
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
 * Controlador para gestión de clientes de BanQuito
 * @author CLICON
 */
public class ClienteController {
    private static final String API_URL = "http://10.40.18.255:8080/WS_JAVA_REST_BanQuito/api/clientes";
    private static final Gson gson = new Gson();

    /**
     * Obtiene la lista de todos los clientes
     */
    public List<Cliente> listarClientes() throws IOException {
        List<Cliente> clientes = new ArrayList<>();
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

                Type listType = new TypeToken<List<Cliente>>() {}.getType();
                clientes = gson.fromJson(jsonResponse.toString(), listType);
            }
        }

        conn.disconnect();
        return clientes;
    }

    /**
     * Obtiene un cliente por cédula
     */
    public Cliente obtenerCliente(String cedula) throws IOException {
        URL url = new URL(API_URL + "/" + cedula);
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

                Cliente cliente = gson.fromJson(jsonResponse.toString(), Cliente.class);
                conn.disconnect();
                return cliente;
            }
        }

        conn.disconnect();
        return null;
    }

    /**
     * Crea un nuevo cliente
     */
    public boolean crearCliente(Cliente cliente) throws IOException {
        URL url = new URL(API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);

        // Construir JSON manualmente solo con los campos que el API espera
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{");
        jsonBuilder.append("\"cedula\":\"").append(cliente.getCedula()).append("\",");
        jsonBuilder.append("\"nombre\":\"").append(cliente.getNombre()).append("\",");
        jsonBuilder.append("\"fechaNacimiento\":\"").append(cliente.getFechaNacimiento()).append("\",");
        jsonBuilder.append("\"estadoCivil\":\"").append(cliente.getEstadoCivil()).append("\",");
        jsonBuilder.append("\"tipoCuentaInicial\":\"").append(cliente.getTipoCuentaInicial()).append("\",");
        jsonBuilder.append("\"saldoInicial\":").append(cliente.getSaldoInicial());
        jsonBuilder.append("}");
        
        String json = jsonBuilder.toString();

        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes("UTF-8"));
            os.flush();
        }

        int responseCode = conn.getResponseCode();
        
        // Leer respuesta para debug si hay error
        if (responseCode != 200 && responseCode != 201) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getErrorStream()))) {
                StringBuilder errorResponse = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    errorResponse.append(line);
                }
                System.out.println("Error del servidor: " + errorResponse.toString());
            } catch (Exception e) {
                // Ignorar si no hay error stream
            }
        }
        
        conn.disconnect();
        return responseCode == 200 || responseCode == 201;
    }

    /**
     * Actualiza un cliente existente
     */
    public boolean actualizarCliente(String cedula, Cliente cliente) throws IOException {
        URL url = new URL(API_URL + "/" + cedula);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);

        String json = gson.toJson(cliente);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes("UTF-8"));
            os.flush();
        }

        int responseCode = conn.getResponseCode();
        conn.disconnect();
        return responseCode == 200;
    }

    /**
     * Elimina un cliente
     */
    public boolean eliminarCliente(String cedula) throws IOException {
        URL url = new URL(API_URL + "/" + cedula);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("DELETE");

        int responseCode = conn.getResponseCode();
        conn.disconnect();
        return responseCode == 204 || responseCode == 200;
    }
}
