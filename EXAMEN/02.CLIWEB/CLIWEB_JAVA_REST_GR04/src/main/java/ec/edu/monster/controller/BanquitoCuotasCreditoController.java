package ec.edu.monster.controller;

import ec.edu.monster.config.AppConfig;
import ec.edu.monster.dto.CuotaCreditoDTO;
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
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@WebServlet("/banquito/creditos/cuotas")
public class BanquitoCuotasCreditoController extends HttpServlet {

    private static final String BASE_URL = AppConfig.BANQUITO_API_BASE;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Validar sesión
        Object usuarioSesion = request.getSession().getAttribute("usuarioSesion");
        if (usuarioSesion == null) {
            response.sendRedirect(request.getContextPath() + "/banquito/login");
            return;
        }

        String idCredito = request.getParameter("idCredito");
        request.setAttribute("idCredito", idCredito != null ? idCredito : "");

        if (idCredito != null && !idCredito.isBlank()) {
            cargarCuotas(idCredito.trim(), request);
        }

        RequestDispatcher rd = request.getRequestDispatcher(
                "/WEB-INF/views/banquito/banquitoCuotasCredito.jsp");
        rd.forward(request, response);
    }

    private void cargarCuotas(String idCredito, HttpServletRequest request) {
        String urlStr = BASE_URL + "/cuotas/credito/" + idCredito; // AJUSTA si tu path es distinto

        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            int status = conn.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK) {
                request.setAttribute("error",
                        "No se pudo obtener la tabla de amortización (HTTP " + status + ").");
                return;
            }

            try (InputStream is = conn.getInputStream();
                 Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {

                Jsonb jsonb = JsonbBuilder.create();
                CuotaCreditoDTO[] array =
                        jsonb.fromJson(reader, CuotaCreditoDTO[].class);

                List<CuotaCreditoDTO> cuotas = Arrays.asList(array);
                request.setAttribute("cuotas", cuotas);
                request.setAttribute("idCreditoResumen", idCredito);

                if (!cuotas.isEmpty()) {
                    int totalCuotas = cuotas.size();
                    double cuotaMensual = cuotas.get(0).valorCuota;

                    request.setAttribute("totalCuotas", totalCuotas);
                    request.setAttribute("cuotaMensual", cuotaMensual);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            request.setAttribute("error",
                    "Error de comunicación con el servidor de cuotas.");
        } finally {
            if (conn != null) conn.disconnect();
        }
    }
}
