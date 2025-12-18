package ec.edu.monster.controller;

import ec.edu.monster.config.AppConfig;
import ec.edu.monster.dto.ElectrodomesticoDTO;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

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
import java.util.stream.Collectors;

@WebServlet("/electroquito/productos")
public class ElectroquitoProductosController extends HttpServlet {

    private static final String BASE_URL = AppConfig.COMERCIALIZADORA_API_BASE;
    private static final String BASE_HOST = AppConfig.COMERCIALIZADORA_HOST_BASE;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Object usuarioSesion = session != null ? session.getAttribute("usuarioSesion") : null;
        if (usuarioSesion == null) {
            response.sendRedirect(request.getContextPath() + "/electroquito/login");
            return;
        }

        if (session != null) {
            Object flashExito = session.getAttribute(ElectroquitoProductosFlash.EXITO);
            if (flashExito != null) {
                request.setAttribute("mensajeExito", flashExito.toString());
                session.removeAttribute(ElectroquitoProductosFlash.EXITO);
            }
            Object flashError = session.getAttribute(ElectroquitoProductosFlash.ERROR);
            if (flashError != null) {
                request.setAttribute("error", flashError.toString());
                session.removeAttribute(ElectroquitoProductosFlash.ERROR);
            }
        }

        String filtro = request.getParameter("q");

        try {
            List<ElectrodomesticoDTO> productos = obtenerProductosDesdeAPI(filtro);

            if (filtro != null && !filtro.isBlank()) {
                productos = filtrarLocalmente(productos, filtro);
            }

            request.setAttribute("productos", productos);
            request.setAttribute("filtro", filtro != null ? filtro : "");
            request.setAttribute("imagenBaseUrl", BASE_HOST);

            RequestDispatcher rd = request.getRequestDispatcher(
                    "/WEB-INF/views/electroquito/electroquitoProductos.jsp");
            rd.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error",
                    "No se pudo cargar el catálogo de productos. Intente nuevamente.");
            request.setAttribute("productos", List.of());
            request.setAttribute("imagenBaseUrl", BASE_HOST);

            RequestDispatcher rd = request.getRequestDispatcher(
                    "/WEB-INF/views/electroquito/electroquitoProductos.jsp");
            rd.forward(request, response);
        }
    }

    // ================== Consumo REST ==================

    private List<ElectrodomesticoDTO> obtenerProductosDesdeAPI(String filtro) throws IOException {

        String urlStr = BASE_URL + "/electrodomesticos";

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

    private List<ElectrodomesticoDTO> filtrarLocalmente(List<ElectrodomesticoDTO> origen,
                                                        String filtro) {
        if (origen == null || origen.isEmpty()) {
            return origen;
        }
        String criterio = filtro.toLowerCase(Locale.ROOT).trim();
        return origen.stream()
                .filter(p -> {
                    String nombre = p.getNombre() != null
                            ? p.getNombre().toLowerCase(Locale.ROOT)
                            : "";
                    String codigo = p.getCodigo() != null
                            ? p.getCodigo().toLowerCase(Locale.ROOT)
                            : "";
                    return nombre.contains(criterio) || codigo.contains(criterio);
                })
                .collect(Collectors.toList());
    }
}
