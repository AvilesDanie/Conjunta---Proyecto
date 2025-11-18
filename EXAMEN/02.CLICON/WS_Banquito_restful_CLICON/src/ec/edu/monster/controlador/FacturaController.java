/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.monster.controlador;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ec.edu.monster.modelo.Factura;
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
 * Controlador para gestionar las facturas de la comercializadora
 * @author CLICON
 */
public class FacturaController {
    private static final String API_URL = "http://10.40.18.255:8080/WS_JAVA_REST_Comercializadora/api/facturas";
    private static final Gson gson = new Gson();

    /**
     * Obtiene la lista de todas las facturas del servidor.
     *
     * @return Lista de objetos Factura.
     * @throws IOException Si ocurre un error durante la conexión al servidor.
     */
    public List<Factura> listarFacturas() throws IOException {
        List<Factura> facturas = new ArrayList<>();
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

                Type listType = new TypeToken<List<Factura>>() {}.getType();
                facturas = gson.fromJson(jsonResponse.toString(), listType);
            }
        } else {
            throw new IOException("Error al obtener las facturas: Código HTTP " + conn.getResponseCode());
        }

        conn.disconnect();
        return facturas;
    }

    /**
     * Obtiene una factura específica por ID.
     *
     * @param id El ID de la factura a obtener.
     * @return La factura encontrada.
     * @throws IOException Si ocurre un error durante la conexión al servidor.
     */
    public Factura obtenerFactura(Long id) throws IOException {
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

                Factura factura = gson.fromJson(jsonResponse.toString(), Factura.class);
                conn.disconnect();
                return factura;
            }
        } else {
            conn.disconnect();
            throw new IOException("Error al obtener la factura: Código HTTP " + conn.getResponseCode());
        }
    }

    /**
     * Crea una nueva factura en el servidor.
     *
     * @param cedulaCliente Cédula del cliente
     * @param nombreCliente Nombre del cliente
     * @param idElectrodomestico ID del electrodoméstico
     * @param cantidad Cantidad a comprar
     * @param formaPago EFECTIVO o CREDITO
     * @param plazoMeses Plazo en meses (solo si es crédito)
     * @param numCuentaCredito Número de cuenta (solo si es crédito)
     * @return La factura creada.
     * @throws IOException Si ocurre un error durante la conexión al servidor.
     */
    public Factura crearFactura(String cedulaCliente, String nombreCliente, 
                                Long idElectrodomestico, int cantidad, 
                                String formaPago, Integer plazoMeses, 
                                String numCuentaCredito) throws IOException {
        URL url = new URL(API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);

        // Construir el JSON según el DTO del servidor
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{");
        jsonBuilder.append("\"cedulaCliente\":\"").append(cedulaCliente).append("\",");
        jsonBuilder.append("\"nombreCliente\":\"").append(nombreCliente).append("\",");
        jsonBuilder.append("\"idElectrodomestico\":").append(idElectrodomestico).append(",");
        jsonBuilder.append("\"cantidad\":").append(cantidad).append(",");
        jsonBuilder.append("\"formaPago\":\"").append(formaPago).append("\"");
        
        if ("CREDITO".equalsIgnoreCase(formaPago)) {
            if (plazoMeses != null) {
                jsonBuilder.append(",\"plazoMeses\":").append(plazoMeses);
            }
            if (numCuentaCredito != null && !numCuentaCredito.isEmpty()) {
                jsonBuilder.append(",\"numCuentaCredito\":\"").append(numCuentaCredito).append("\"");
            }
        }
        
        jsonBuilder.append("}");
        String json = jsonBuilder.toString();

        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes("UTF-8"));
            os.flush();
        }

        int responseCode = conn.getResponseCode();
        if (responseCode == 200 || responseCode == 201) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                StringBuilder jsonResponse = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    jsonResponse.append(line);
                }

                Factura factura = gson.fromJson(jsonResponse.toString(), Factura.class);
                conn.disconnect();
                return factura;
            }
        } else {
            // Leer el error del servidor
            StringBuilder errorResponse = new StringBuilder();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getErrorStream()))) {
                String line;
                while ((line = in.readLine()) != null) {
                    errorResponse.append(line);
                }
            } catch (Exception e) {
                // Si no hay error stream, usar el código de respuesta
            }
            conn.disconnect();
            throw new IOException("Error al crear la factura: Código HTTP " + responseCode + 
                                  (errorResponse.length() > 0 ? " - " + errorResponse.toString() : ""));
        }
    }

    /**
     * Lista facturas por cliente
     */
    public List<Factura> listarFacturasPorCliente(String cedula) throws IOException {
        List<Factura> facturas = new ArrayList<>();
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
                Type listType = new TypeToken<List<Factura>>() {}.getType();
                facturas = gson.fromJson(jsonResponse.toString(), listType);
            }
        }
        conn.disconnect();
        return facturas;
    }
}
