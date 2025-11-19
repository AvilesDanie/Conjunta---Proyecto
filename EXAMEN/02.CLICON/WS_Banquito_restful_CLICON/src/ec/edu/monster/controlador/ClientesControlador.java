/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.monster.controlador;

/**
 *
 * @author danie
 */

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ec.edu.monster.modelo.ClienteModelo;
import ec.edu.monster.modelo.ClienteCrearRequest;
import ec.edu.monster.modelo.ClienteActualizarRequest;

import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Controlador que consume el servicio REST de clientes
 */
public class ClientesControlador {

    // AJUSTA ESTA URL a tu contexto real
    private static final String BASE_URL = "http://localhost:8080/WS_JAVA_REST_BanQuito/api/clientes";

    private final Gson gson;

    public ClientesControlador() {
        this.gson = new Gson();
    }

    // ==================== Métodos públicos ====================

    public List<ClienteModelo> listarClientes() throws IOException {
        String json = realizarPeticion("GET", "", null, 200);
        Type listType = new TypeToken<List<ClienteModelo>>() {}.getType();
        return gson.fromJson(json, listType);
    }

    public ClienteModelo obtenerCliente(String cedula) throws IOException {
        String json = realizarPeticion("GET", "/" + cedula, null, 200);
        return gson.fromJson(json, ClienteModelo.class);
    }

    public ClienteModelo crearCliente(ClienteCrearRequest request) throws IOException {
        String body = gson.toJson(request);
        String json = realizarPeticion("POST", "", body, 201);
        return gson.fromJson(json, ClienteModelo.class);
    }

    public ClienteModelo actualizarCliente(String cedula, ClienteActualizarRequest request) throws IOException {
        String body = gson.toJson(request);
        String json = realizarPeticion("PUT", "/" + cedula, body, 200);
        return gson.fromJson(json, ClienteModelo.class);
    }

    public boolean eliminarCliente(String cedula) throws IOException {
        realizarPeticion("DELETE", "/" + cedula, null, 204);
        return true;
    }

    // ==================== Helper HTTP ====================

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
            try (OutputStream os = conn.getOutputStream()) {
                os.write(body.getBytes(StandardCharsets.UTF_8));
            }
        }

        int status = conn.getResponseCode();
        InputStream is;
        if (status >= 200 && status < 400) {
            is = conn.getInputStream();
        } else {
            is = conn.getErrorStream();
        }

        String respuesta;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String linea;
            while ((linea = br.readLine()) != null) {
                sb.append(linea);
            }
            respuesta = sb.toString();
        }

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
}
