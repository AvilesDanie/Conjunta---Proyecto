package ec.edu.monster.controlador;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Controlador para gestionar evaluación de crédito
 * Consume el servicio REST de BanQuito (Java)
 * @author CLICON
 */
public class ComprarController {
    // API REST de BanQuito (Java)
    private static final String API_CREDITOS_URL = "http://10.40.18.255:8080/WS_JAVA_REST_BanQuito/api/creditos";
    private static final String API_CLIENTES_URL = "http://10.40.18.255:8080/WS_JAVA_REST_BanQuito/api/clientes";
    private static final Gson gson = new Gson();

    /**
     * DTO para solicitud de crédito
     */
    public static class SolicitudCreditoDTO {
        public String cedula;
        public double precioProducto;
        public int plazoMeses;
        public String numCuentaCredito;
    }

    /**
     * DTO para resultado de evaluación
     */
    public static class ResultadoEvaluacionDTO {
        public boolean aprobado;
        public String mensaje;
    }

    /**
     * Evalúa si un cliente es sujeto de crédito
     * @param cedula Cédula del cliente
     * @param monto Monto solicitado
     * @param plazoMeses Plazo en meses
     * @param numCuentaCredito Número de cuenta para el crédito
     * @return true si es sujeto de crédito, false en caso contrario
     */
    public boolean esSujetoCredito(String cedula, double monto, int plazoMeses, String numCuentaCredito) {
        try {
            // Verificar que el cliente exista
            if (!existeCliente(cedula)) {
                System.err.println("Cliente no encontrado en el sistema");
                return false;
            }

            // Crear solicitud JSON con los campos correctos
            SolicitudCreditoDTO solicitud = new SolicitudCreditoDTO();
            solicitud.cedula = cedula;
            solicitud.precioProducto = monto;
            solicitud.plazoMeses = plazoMeses;
            solicitud.numCuentaCredito = numCuentaCredito;

            String jsonRequest = gson.toJson(solicitud);
            System.out.println("DEBUG - JSON enviado: " + jsonRequest);

            URL url = new URL(API_CREDITOS_URL + "/evaluar");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);

            // Enviar la solicitud
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonRequest.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            System.out.println("DEBUG - Response code: " + responseCode);
            
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();
                
                System.out.println("DEBUG - Respuesta: " + response.toString());
                
                // Parsear respuesta JSON
                JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
                conn.disconnect();
                return jsonResponse.has("aprobado") && jsonResponse.get("aprobado").getAsBoolean();
            } else {
                // Leer error
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                StringBuilder errorResponse = new StringBuilder();
                String line;
                while ((line = errorReader.readLine()) != null) {
                    errorResponse.append(line);
                }
                errorReader.close();
                System.err.println("DEBUG - Error del servidor: " + errorResponse.toString());
            }

            conn.disconnect();
            return false;
            
        } catch (Exception e) {
            System.err.println("Error al evaluar crédito: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Verifica si un cliente existe en el sistema
     * @param cedula Cédula del cliente
     * @return true si existe, false en caso contrario
     */
    private boolean existeCliente(String cedula) {
        try {
            URL url = new URL(API_CLIENTES_URL + "/" + cedula);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            int responseCode = conn.getResponseCode();
            conn.disconnect();
            
            return responseCode == HttpURLConnection.HTTP_OK;
            
        } catch (Exception e) {
            System.err.println("Error al verificar cliente: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene la razón de rechazo del crédito
     * @param cedula Cédula del cliente
     * @param monto Monto solicitado
     * @param plazoMeses Plazo en meses
     * @param numCuentaCredito Número de cuenta
     * @return Razón del rechazo
     */
    public String obtenerRazonRechazo(String cedula, double monto, int plazoMeses, String numCuentaCredito) {
        try {
            if (!existeCliente(cedula)) {
                return "Cliente no encontrado en el sistema";
            }

            // Aquí podrías implementar lógica adicional para obtener razones específicas
            // Por ahora retornamos un mensaje genérico
            double montoMaximo = calcularMontoMaximo(cedula);
            
            if (monto > montoMaximo) {
                return "El monto solicitado ($" + String.format("%.2f", monto) + 
                       ") excede el monto máximo permitido ($" + String.format("%.2f", montoMaximo) + ")";
            }
            
            return "Cliente no cumple con los requisitos de crédito";
            
        } catch (Exception e) {
            return "Error al evaluar crédito: " + e.getMessage();
        }
    }

    /**
     * Calcula el monto máximo de crédito para un cliente
     * Basado en el promedio de movimientos del cliente
     * @param cedula Cédula del cliente
     * @return Monto máximo de crédito (aproximado como 10 veces el promedio)
     */
    public double calcularMontoMaximo(String cedula) {
        try {
            // Por ahora retornamos un valor fijo alto
            // El cálculo real se hace en el servidor al evaluar
            return 100000.0;
            
        } catch (Exception e) {
            System.err.println("Error al calcular monto máximo: " + e.getMessage());
            return 0.0;
        }
    }
}
