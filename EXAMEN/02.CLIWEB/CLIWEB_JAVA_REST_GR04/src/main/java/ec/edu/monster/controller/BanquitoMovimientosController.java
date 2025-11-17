package ec.edu.monster.controller;

import ec.edu.monster.dto.MovimientoDTO;
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

@WebServlet("/banquito/movimientos")
public class BanquitoMovimientosController extends HttpServlet {

    private static final String BASE_URL =
            "http://localhost:8080/WS_JAVA_REST_BanQuito/api";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Object usuarioSesion = request.getSession().getAttribute("usuarioSesion");
        if (usuarioSesion == null) {
            response.sendRedirect(request.getContextPath() + "/banquito/login");
            return;
        }

        String numeroCuenta = request.getParameter("cuenta");
        request.setAttribute("numeroCuenta", numeroCuenta);

        if (numeroCuenta == null || numeroCuenta.isBlank()) {
            request.setAttribute("error",
                    "Falta el número de cuenta para consultar movimientos.");
            request.setAttribute("movimientos", List.of());
        } else {
            try {
                List<MovimientoDTO> movs = obtenerMovimientosPorCuenta(numeroCuenta);
                request.setAttribute("movimientos", movs);
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("error",
                        "No se pudieron cargar los movimientos. Detalle: " + e.getMessage());
                request.setAttribute("movimientos", List.of());
            }
        }

        RequestDispatcher rd = request.getRequestDispatcher(
                "/WEB-INF/views/banquito/banquitoMovimientos.jsp");
        rd.forward(request, response);
    }

    // ================== Consumo del REST ==================

    private List<MovimientoDTO> obtenerMovimientosPorCuenta(String numeroCuenta)
            throws IOException {

        // Ajusta la ruta a la de tu MovimientoController
        //   GET /movimientos/cuenta/{numeroCuenta}
        String urlStr = BASE_URL + "/movimientos/cuenta/" +
                URLEncoder.encode(numeroCuenta, StandardCharsets.UTF_8);

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        int status = conn.getResponseCode();
        if (status != HttpURLConnection.HTTP_OK) {
            throw new IOException("Error HTTP al obtener movimientos: " + status);
        }

        try (InputStream is = conn.getInputStream();
             Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {

            Jsonb jsonb = JsonbBuilder.create();
            MovimientoDTO[] array = jsonb.fromJson(reader, MovimientoDTO[].class);
            return Arrays.asList(array);
        } finally {
            conn.disconnect();
        }
    }
}
