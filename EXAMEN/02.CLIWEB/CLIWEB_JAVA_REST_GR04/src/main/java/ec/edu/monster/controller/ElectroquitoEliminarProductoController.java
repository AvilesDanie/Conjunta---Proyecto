package ec.edu.monster.controller;

import ec.edu.monster.config.AppConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@WebServlet("/electroquito/productos/eliminar")
public class ElectroquitoEliminarProductoController extends HttpServlet {

    private static final String BASE_URL = AppConfig.COMERCIALIZADORA_API_BASE;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Object usuarioSesion = session != null ? session.getAttribute("usuarioSesion") : null;
        if (usuarioSesion == null) {
            response.sendRedirect(request.getContextPath() + "/electroquito/login");
            return;
        }

        Long id = parseId(request.getParameter("id"));
        if (id == null) {
            registrarFlashError(request, "No se pudo identificar el producto a eliminar.");
            response.sendRedirect(request.getContextPath() + "/electroquito/productos");
            return;
        }

        try {
            URL url = new URL(BASE_URL + "/electrodomesticos/" + id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Accept", "application/json");

            int status = conn.getResponseCode();
            if (status == HttpURLConnection.HTTP_NO_CONTENT
                    || status == HttpURLConnection.HTTP_OK) {
                conn.disconnect();
                registrarFlashExito(request, "Producto eliminado correctamente.");
                response.sendRedirect(request.getContextPath() + "/electroquito/productos");
                return;
            }

            if (status == HttpURLConnection.HTTP_NOT_FOUND) {
                conn.disconnect();
                registrarFlashError(request, "El producto seleccionado ya no existe.");
                response.sendRedirect(request.getContextPath() + "/electroquito/productos");
                return;
            }

            String detalle = leerDetalle(conn);
            conn.disconnect();
            registrarFlashError(request,
                    "No se pudo eliminar el producto (HTTP " + status + "). "
                            + (detalle.isBlank() ? "" : detalle));

        } catch (Exception ex) {
            ex.printStackTrace();
            registrarFlashError(request,
                    "Ocurrió un error inesperado al intentar eliminar el producto.");
        }

        response.sendRedirect(request.getContextPath() + "/electroquito/productos");
    }

    private Long parseId(String idParam) {
        try {
            if (idParam == null || idParam.isBlank()) {
                return null;
            }
            return Long.parseLong(idParam);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private void registrarFlashExito(HttpServletRequest request, String mensaje) {
        HttpSession session = request.getSession();
        session.setAttribute(ElectroquitoProductosFlash.EXITO, mensaje);
    }

    private void registrarFlashError(HttpServletRequest request, String mensaje) {
        HttpSession session = request.getSession();
        session.setAttribute(ElectroquitoProductosFlash.ERROR, mensaje);
    }

    private String leerDetalle(HttpURLConnection conn) throws IOException {
        InputStream stream = conn.getErrorStream() != null
                ? conn.getErrorStream()
                : conn.getInputStream();
        if (stream == null) {
            return "";
        }
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString().replaceAll("<[^>]+>", " ").trim();
        }
    }
}
