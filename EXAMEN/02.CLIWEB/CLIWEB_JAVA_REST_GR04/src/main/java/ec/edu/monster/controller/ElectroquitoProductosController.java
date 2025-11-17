package ec.edu.monster.controller;

import ec.edu.monster.dto.ElectrodomesticoDTO;
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

@WebServlet("/electroquito/productos")
public class ElectroquitoProductosController extends HttpServlet {

    private static final String BASE_URL =
            "http://localhost:8080/WS_JAVA_REST_Comercializadora/api";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Validar sesión (mismo atributo que usaste en login)
        Object usuarioSesion = request.getSession().getAttribute("usuarioSesion");
        if (usuarioSesion == null) {
            response.sendRedirect(request.getContextPath() + "/electroquito/login");
            return;
        }

        String filtro = request.getParameter("q");

        try {
            List<ElectrodomesticoDTO> productos = obtenerProductosDesdeAPI(filtro);
            request.setAttribute("productos", productos);
            request.setAttribute("filtro", filtro != null ? filtro : "");

            RequestDispatcher rd = request.getRequestDispatcher(
                    "/WEB-INF/views/electroquito/electroquitoProductos.jsp");
            rd.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error",
                    "No se pudo cargar el catálogo de productos. Intente nuevamente.");
            request.setAttribute("productos", List.of());

            RequestDispatcher rd = request.getRequestDispatcher(
                    "/WEB-INF/views/electroquito/electroquitoProductos.jsp");
            rd.forward(request, response);
        }
    }

    // ================== Consumo REST ==================

    private List<ElectrodomesticoDTO> obtenerProductosDesdeAPI(String filtro) throws IOException {

        // Ajusta el path si tu ElectrodomesticoController usa otro (por ejemplo "productos")
        String urlStr = BASE_URL + "/electrodomesticos";

        // Si el servicio soporta filtro por query param, lo mandas:
        if (filtro != null && !filtro.isBlank()) {
            urlStr += "?q=" + java.net.URLEncoder.encode(filtro, StandardCharsets.UTF_8);
        }

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        int status = conn.getResponseCode();
        if (status != HttpURLConnection.HTTP_OK) {
            throw new IOException("Error HTTP al obtener productos: " + status);
        }

        try (InputStream is = conn.getInputStream();
             Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {

            Jsonb jsonb = JsonbBuilder.create();
            ElectrodomesticoDTO[] array =
                    jsonb.fromJson(reader, ElectrodomesticoDTO[].class);
            return Arrays.asList(array);

        } finally {
            conn.disconnect();
        }
    }
}
