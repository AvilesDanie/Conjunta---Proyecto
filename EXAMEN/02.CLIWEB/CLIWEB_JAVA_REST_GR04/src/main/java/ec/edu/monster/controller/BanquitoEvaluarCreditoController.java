package ec.edu.monster.controller;

import ec.edu.monster.dto.CreditoEvaluacionRequestDTO;
import ec.edu.monster.dto.CreditoEvaluacionResponseDTO;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@WebServlet("/banquito/creditos/evaluar")
public class BanquitoEvaluarCreditoController extends HttpServlet {

    private static final String BASE_URL =
            "http://localhost:8080/WS_JAVA_REST_BanQuito/api";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Validación de sesión
        Object usuarioSesion = request.getSession().getAttribute("usuarioSesion");
        if (usuarioSesion == null) {
            response.sendRedirect(request.getContextPath() + "/banquito/login");
            return;
        }

        RequestDispatcher rd = request.getRequestDispatcher(
                "/WEB-INF/views/banquito/banquitoEvaluarCredito.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Validación de sesión
        Object usuarioSesion = request.getSession().getAttribute("usuarioSesion");
        if (usuarioSesion == null) {
            response.sendRedirect(request.getContextPath() + "/banquito/login");
            return;
        }

        String cedula       = request.getParameter("cedula");
        String precioStr    = request.getParameter("precio");
        String plazoStr     = request.getParameter("plazoMeses");
        String numeroCuenta = request.getParameter("numeroCuenta");

        // Volver a pintar los valores en el form si hay error
        request.setAttribute("cedula", cedula);
        request.setAttribute("precio", precioStr);
        request.setAttribute("plazoMeses", plazoStr);
        request.setAttribute("numeroCuenta", numeroCuenta);

        // Validaciones básicas
        if (cedula == null || cedula.isBlank() ||
                precioStr == null || precioStr.isBlank() ||
                plazoStr == null || plazoStr.isBlank() ||
                numeroCuenta == null || numeroCuenta.isBlank()) {

            request.setAttribute("error", "Todos los campos son obligatorios.");
            RequestDispatcher rd = request.getRequestDispatcher(
                    "/WEB-INF/views/banquito/banquitoEvaluarCredito.jsp");
            rd.forward(request, response);
            return;
        }

        double precio;
        int plazoMeses;
        try {
            // Permite que el usuario ponga coma o punto
            precio = Double.parseDouble(precioStr.replace(",", "."));
            plazoMeses = Integer.parseInt(plazoStr);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Precio y plazo deben ser numéricos válidos.");
            RequestDispatcher rd = request.getRequestDispatcher(
                    "/WEB-INF/views/banquito/banquitoEvaluarCredito.jsp");
            rd.forward(request, response);
            return;
        }

        // Armar DTO para la API
        CreditoEvaluacionRequestDTO dto = new CreditoEvaluacionRequestDTO();
        dto.cedulaCliente = cedula;
        dto.precioProducto = precio;
        dto.plazoMeses = plazoMeses;
        dto.numeroCuentaCredito = numeroCuenta;

        Jsonb jsonb = JsonbBuilder.create();
        String jsonBody = jsonb.toJson(dto);

        String urlStr = BASE_URL + "/creditos/evaluar"; // AJUSTA si tu path real es otro
        HttpURLConnection conn = null;

        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");

            // Enviar JSON
            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonBody.getBytes(StandardCharsets.UTF_8));
                os.flush();
            }

            int status = conn.getResponseCode();
            if (status < 200 || status >= 300) {
                // Leer posible mensaje de error del backend
                String mensaje = "No se pudo evaluar el crédito. Código HTTP: " + status;
                InputStream err = conn.getErrorStream();
                if (err != null) {
                    try (Reader r = new InputStreamReader(err, StandardCharsets.UTF_8)) {
                        StringBuilder sb = new StringBuilder();
                        char[] buf = new char[1024];
                        int n;
                        while ((n = r.read(buf)) > 0) {
                            sb.append(buf, 0, n);
                        }
                        if (!sb.toString().isBlank()) {
                            mensaje += " - " + sb;
                        }
                    }
                }
                request.setAttribute("error", mensaje);
            } else {
                // OK -> parsear respuesta
                try (InputStream is = conn.getInputStream();
                     Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {

                    CreditoEvaluacionResponseDTO resumen =
                            jsonb.fromJson(reader, CreditoEvaluacionResponseDTO.class);

                    request.setAttribute("creditoResumen", resumen);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            request.setAttribute("error",
                    "Error de comunicación con el servidor de créditos.");
        } finally {
            if (conn != null) conn.disconnect();
        }

        RequestDispatcher rd = request.getRequestDispatcher(
                "/WEB-INF/views/banquito/banquitoEvaluarCredito.jsp");
        rd.forward(request, response);
    }
}
