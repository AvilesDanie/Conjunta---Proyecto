package ec.edu.monster.controller;

import ec.edu.monster.config.AppConfig;
import ec.edu.monster.dto.FacturaDetalleDTO;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
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

@WebServlet("/electroquito/facturas/detalle")
public class ElectroquitoFacturaDetalleController extends HttpServlet {

    private static final String BASE_URL = AppConfig.COMERCIALIZADORA_API_BASE;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Object usuarioSesion = request.getSession().getAttribute("usuarioSesion");
        if (usuarioSesion == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"error\":\"No autorizado\"}");
            return;
        }

        String idStr = request.getParameter("id");
        if (idStr == null || idStr.isBlank()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"error\":\"ID de factura requerido\"}");
            return;
        }

        try {
            Long id = Long.parseLong(idStr);
            FacturaDetalleDTO detalle = obtenerDetalleDesdeAPI(id);

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            Jsonb jsonb = JsonbBuilder.create();
            String json = jsonb.toJson(detalle);
            response.getWriter().write(json);

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"error\":\"ID de factura inválido\"}");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"error\":\"Error al obtener el detalle de la factura\"}");
        }
    }

    private FacturaDetalleDTO obtenerDetalleDesdeAPI(Long id) throws IOException {
        String urlStr = BASE_URL + "/facturas/" + id;

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        int status = conn.getResponseCode();
        if (status != HttpURLConnection.HTTP_OK) {
            throw new IOException("Error HTTP al obtener detalle de factura: " + status);
        }

        try (InputStream is = conn.getInputStream();
             Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {

            Jsonb jsonb = JsonbBuilder.create();
            return jsonb.fromJson(reader, FacturaDetalleDTO.class);

        } finally {
            conn.disconnect();
        }
    }
}
