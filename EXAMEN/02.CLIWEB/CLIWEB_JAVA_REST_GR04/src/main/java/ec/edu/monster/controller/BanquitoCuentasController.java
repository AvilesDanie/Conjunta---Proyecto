package ec.edu.monster.controller;

import ec.edu.monster.dto.CuentaDTO;
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

@WebServlet("/banquito/cuentas")
public class BanquitoCuentasController extends HttpServlet {

    private static final String BASE_URL =
            "http://localhost:8080/WS_JAVA_REST_BanQuito/api";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // --- Validación de sesión, igual que en Home y Clientes ---
        Object usuarioSesion = request.getSession().getAttribute("usuarioSesion");
        if (usuarioSesion == null) {
            response.sendRedirect(request.getContextPath() + "/banquito/login");
            return;
        }

        String cedula = request.getParameter("cedula"); // cédula del cliente
        request.setAttribute("cedulaBuscada", cedula != null ? cedula : "");

        try {
            List<CuentaDTO> cuentas = List.of();

            if (cedula != null && !cedula.isBlank()) {
                cuentas = obtenerCuentasPorCedula(cedula);
            }

            request.setAttribute("cuentas", cuentas);

            RequestDispatcher rd = request.getRequestDispatcher(
                    "/WEB-INF/views/banquito/banquitoCuentas.jsp");
            rd.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error",
                    "No se pudo cargar las cuentas. Detalle: " + e.getMessage());
            request.setAttribute("cuentas", List.of());

            RequestDispatcher rd = request.getRequestDispatcher(
                    "/WEB-INF/views/banquito/banquitoCuentas.jsp");
            rd.forward(request, response);
        }
    }

    // ================== Consumo del REST ==================

    private List<CuentaDTO> obtenerCuentasPorCedula(String cedula) throws IOException {

        // Ajusta esta ruta a lo que tenga tu CuentaController:
        //   GET /cuentas/cliente/{cedula}
        String urlStr = BASE_URL + "/cuentas/cliente/" +
                URLEncoder.encode(cedula, StandardCharsets.UTF_8);

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        int status = conn.getResponseCode();
        if (status != HttpURLConnection.HTTP_OK) {
            throw new IOException("Error HTTP al obtener cuentas: " + status);
        }

        try (InputStream is = conn.getInputStream();
             Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {

            Jsonb jsonb = JsonbBuilder.create();
            CuentaDTO[] array = jsonb.fromJson(reader, CuentaDTO[].class);
            return Arrays.asList(array);
        } finally {
            conn.disconnect();
        }
    }
}
