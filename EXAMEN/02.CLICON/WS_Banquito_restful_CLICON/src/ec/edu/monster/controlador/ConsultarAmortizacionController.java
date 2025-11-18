package ec.edu.monster.controlador;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ec.edu.monster.modelo.Amortizacion;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para consultar la tabla de amortización
 * Consume el servicio REST de BanQuito (Java)
 * @author CLICON
 */
public class ConsultarAmortizacionController {
    private static final String API_CUOTAS_URL = "http://10.40.18.255:8080/WS_JAVA_REST_BanQuito/api/cuotas";
    private static final Gson gson = new Gson();

    /**
     * Consulta la tabla de amortización de un crédito
     * @param idCredito El ID del crédito
     * @return Lista de objetos Amortizacion
     */
    public List<Amortizacion> consultarAmortizacion(Long idCredito) {
        List<Amortizacion> tablaAmortizacion = new ArrayList<>();

        try {
            // Llamada REST para obtener cuotas por crédito
            URL url = new URL(API_CUOTAS_URL + "/credito/" + idCredito);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);

            int responseCode = conn.getResponseCode();
            
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();
                conn.disconnect();

                // Parsear respuesta JSON
                String jsonResponse = response.toString();
                Type listType = new TypeToken<List<Amortizacion>>() {}.getType();
                tablaAmortizacion = gson.fromJson(jsonResponse, listType);
                
            } else {
                System.err.println("Error HTTP " + responseCode + " al obtener cuotas");
            }

            conn.disconnect();
            
        } catch (Exception e) {
            System.err.println("Error al consultar la tabla de amortización: " + e.getMessage());
            e.printStackTrace();
        }

        return tablaAmortizacion;
    }
}
