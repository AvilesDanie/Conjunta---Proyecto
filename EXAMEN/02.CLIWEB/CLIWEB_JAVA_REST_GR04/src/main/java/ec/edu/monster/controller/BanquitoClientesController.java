package ec.edu.monster.controller;

import ec.edu.monster.config.AppConfig;
import ec.edu.monster.dto.ClienteDTO;
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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@WebServlet("/banquito/clientes")
public class BanquitoClientesController extends HttpServlet {

    private static final String BASE_URL = AppConfig.BANQUITO_API_BASE;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // --- Validación de sesión, igual que en BanquitoHomeController ---
        Object usuarioSesion = request.getSession().getAttribute("usuarioSesion");
        if (usuarioSesion == null) {
            response.sendRedirect(request.getContextPath() + "/banquito/login");
            return;
        }

        String filtro = request.getParameter("q"); // texto de búsqueda (nombre/cedula), opcional

        try {
            List<ClienteDTO> clientes = obtenerClientesDesdeAPI(filtro);
            request.setAttribute("clientes", clientes);
            request.setAttribute("filtro", filtro != null ? filtro : "");

            RequestDispatcher rd = request.getRequestDispatcher(
                    "/WEB-INF/views/banquito/banquitoClientes.jsp");
            rd.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error",
                    "No se pudo cargar la lista de clientes. Intente nuevamente.");
            request.setAttribute("clientes", List.of());

            RequestDispatcher rd = request.getRequestDispatcher(
                    "/WEB-INF/views/banquito/banquitoClientes.jsp");
            rd.forward(request, response);
        }
    }

    // ================== Consumo del REST ==================

    private List<ClienteDTO> obtenerClientesDesdeAPI(String filtro) throws IOException {
        // GET /clientes  ó  /clientes?q=filtro  (ajusta el parámetro si tu API usa otro)
        String urlStr = BASE_URL + "/clientes";
        if (filtro != null && !filtro.isBlank()) {
            urlStr += "?q=" + URLEncoder.encode(filtro, StandardCharsets.UTF_8);
        }

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        int status = conn.getResponseCode();
        if (status != HttpURLConnection.HTTP_OK) {
            throw new IOException("Error HTTP al obtener clientes: " + status);
        }

        try (InputStream is = conn.getInputStream();
             Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {

            Jsonb jsonb = JsonbBuilder.create();
            ClienteDTO[] array = jsonb.fromJson(reader, ClienteDTO[].class);
            return Arrays.asList(array);
        } finally {
            conn.disconnect();
        }
    }
}
