/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.monster.controlador;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ec.edu.monster.modelo.Electrodomestico;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para gestionar electrodomésticos
 * Consume el servicio REST del servidor
 * @author CLICON
 */
public class ElectrodomesticoController {
    private static final String API_URL = "http://10.40.18.255:8080/WS_JAVA_REST_Comercializadora/api/electrodomesticos";
    private static final Gson gson = new Gson();

    /**
     * Obtiene la lista de todos los electrodomésticos del servidor
     * @return Lista de objetos Electrodomestico
     */
    public List<Electrodomestico> obtenerElectrodomesticos() {
        List<Electrodomestico> electrodomesticos = new ArrayList<>();
        
        try {
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            int responseCode = conn.getResponseCode();
            
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder jsonResponse = new StringBuilder();
                String line;
                
                while ((line = in.readLine()) != null) {
                    jsonResponse.append(line);
                }
                in.close();

                Type listType = new TypeToken<List<Electrodomestico>>() {}.getType();
                electrodomesticos = gson.fromJson(jsonResponse.toString(), listType);
            } else {
                System.err.println("Error HTTP " + responseCode + " al obtener electrodomésticos");
            }

            conn.disconnect();
            
        } catch (Exception e) {
            System.err.println("Error al conectar con el servidor: " + e.getMessage());
            e.printStackTrace();
        }
        
        return electrodomesticos;
    }

    /**
     * Crea un nuevo electrodoméstico en el servidor
     * @param nuevoElectrodomestico El electrodoméstico a crear
     * @return true si se creó exitosamente, false en caso contrario
     */
    public boolean crearElectrodomestico(Electrodomestico nuevoElectrodomestico) {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            // Enviar el JSON del electrodoméstico
            String jsonInputString = gson.toJson(nuevoElectrodomestico);
            
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            conn.disconnect();
            
            return responseCode == HttpURLConnection.HTTP_OK || 
                   responseCode == HttpURLConnection.HTTP_CREATED;
                   
        } catch (Exception e) {
            System.err.println("Error al crear electrodoméstico: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Obtiene un electrodoméstico por su ID
     * @param id El ID del electrodoméstico
     * @return El electrodoméstico encontrado o null
     */
    public Electrodomestico obtenerElectrodomesticoPorId(Long id) {
        try {
            URL url = new URL(API_URL + "/" + id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            int responseCode = conn.getResponseCode();
            
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder jsonResponse = new StringBuilder();
                String line;
                
                while ((line = in.readLine()) != null) {
                    jsonResponse.append(line);
                }
                in.close();

                Electrodomestico electrodomestico = gson.fromJson(jsonResponse.toString(), Electrodomestico.class);
                conn.disconnect();
                return electrodomestico;
            }

            conn.disconnect();
            
        } catch (Exception e) {
            System.err.println("Error al obtener electrodoméstico: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
}
