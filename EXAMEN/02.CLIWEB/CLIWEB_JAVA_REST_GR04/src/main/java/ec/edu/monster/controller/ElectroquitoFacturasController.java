package ec.edu.monster.controller;

import ec.edu.monster.config.AppConfig;
import ec.edu.monster.dto.FacturaDTO;
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
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

@WebServlet("/electroquito/facturas")
public class ElectroquitoFacturasController extends HttpServlet {

    private static final String BASE_URL = AppConfig.COMERCIALIZADORA_API_BASE;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Object usuarioSesion = request.getSession().getAttribute("usuarioSesion");
        if (usuarioSesion == null) {
            response.sendRedirect(request.getContextPath() + "/electroquito/login");
            return;
        }

        String filtro = request.getParameter("q");

        try {
            List<FacturaDTO> facturas = obtenerFacturasDesdeAPI();
            if (filtro != null && !filtro.isBlank()) {
                final String filtroNormalizado = filtro.toLowerCase(Locale.ROOT);
                facturas = facturas.stream()
                        .filter(f -> coincideFiltro(f, filtroNormalizado))
                        .collect(Collectors.toList());
            }

            request.setAttribute("facturas", facturas);
            request.setAttribute("filtro", filtro != null ? filtro : "");

            RequestDispatcher rd = request.getRequestDispatcher(
                    "/WEB-INF/views/electroquito/electroquitoFacturas.jsp");
            rd.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error",
                    "No se pudo obtener el historial de facturas. Intente nuevamente.");
            request.setAttribute("facturas", List.of());
            request.setAttribute("filtro", filtro != null ? filtro : "");

            RequestDispatcher rd = request.getRequestDispatcher(
                    "/WEB-INF/views/electroquito/electroquitoFacturas.jsp");
            rd.forward(request, response);
        }
    }

    private boolean coincideFiltro(FacturaDTO factura, String filtro) {
        if (factura == null) {
            return false;
        }

        String id = factura.getId() != null ? factura.getId().toString() : "";
        String cliente = factura.getClienteNombre() != null
                ? factura.getClienteNombre().toLowerCase(Locale.ROOT) : "";
        String cedula = factura.getClienteCedula() != null
                ? factura.getClienteCedula().toLowerCase(Locale.ROOT) : "";

        return id.contains(filtro)
                || cliente.contains(filtro)
                || cedula.contains(filtro);
    }

    private List<FacturaDTO> obtenerFacturasDesdeAPI() throws IOException {
        String urlStr = BASE_URL + "/facturas";

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        int status = conn.getResponseCode();
        if (status != HttpURLConnection.HTTP_OK) {
            throw new IOException("Error HTTP al obtener facturas: " + status);
        }

        try (InputStream is = conn.getInputStream();
             Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {

            Jsonb jsonb = JsonbBuilder.create();
            FacturaDTO[] array = jsonb.fromJson(reader, FacturaDTO[].class);
            return Arrays.stream(array != null ? array : new FacturaDTO[0])
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

        } finally {
            conn.disconnect();
        }
    }
}
