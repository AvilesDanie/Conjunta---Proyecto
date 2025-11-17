package ec.edu.monster.controller;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@WebServlet("/electroquito/productos/nuevo")
public class ElectroquitoNuevoProductoController extends HttpServlet {

    private static final String BASE_URL =
            "http://localhost:8080/WS_JAVA_REST_Comercializadora/api";

    // DTO para el POST al servicio REST
    public static class CrearElectrodomesticoRequest {
        public String codigo;
        public String nombre;
        public BigDecimal precioVenta;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Validar sesión
        Object usuarioSesion = request.getSession().getAttribute("usuarioSesion");
        if (usuarioSesion == null) {
            response.sendRedirect(request.getContextPath() + "/electroquito/login");
            return;
        }

        RequestDispatcher rd = request.getRequestDispatcher(
                "/WEB-INF/views/electroquito/electroquitoNuevoProducto.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Validar sesión
        Object usuarioSesion = request.getSession().getAttribute("usuarioSesion");
        if (usuarioSesion == null) {
            response.sendRedirect(request.getContextPath() + "/electroquito/login");
            return;
        }

        request.setCharacterEncoding("UTF-8");

        String codigo = request.getParameter("codigo");
        String nombre = request.getParameter("nombre");
        String precioStr = request.getParameter("precio");

        // Devolvemos estos valores al JSP si hay error
        request.setAttribute("codigo", codigo);
        request.setAttribute("nombre", nombre);
        request.setAttribute("precio", precioStr);

        // Validaciones simples
        if (codigo == null || codigo.isBlank()
                || nombre == null || nombre.isBlank()
                || precioStr == null || precioStr.isBlank()) {

            request.setAttribute("error",
                    "Todos los campos marcados con * son obligatorios.");
            reenviarFormulario(request, response);
            return;
        }

        BigDecimal precio;
        try {
            precio = new BigDecimal(precioStr.trim());
            if (precio.compareTo(BigDecimal.ZERO) <= 0) {
                throw new NumberFormatException("precio <= 0");
            }
        } catch (NumberFormatException ex) {
            request.setAttribute("error",
                    "El precio de venta debe ser un número mayor que cero.");
            reenviarFormulario(request, response);
            return;
        }

        try {
            // Armar request para el servicio REST
            CrearElectrodomesticoRequest cuerpo = new CrearElectrodomesticoRequest();
            cuerpo.codigo = codigo.trim();
            cuerpo.nombre = nombre.trim();
            cuerpo.precioVenta = precio;

            Jsonb jsonb = JsonbBuilder.create();
            String json = jsonb.toJson(cuerpo);

            String urlStr = BASE_URL + "/electrodomesticos"; // ajusta si tu path es distinto
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            // Enviar JSON
            try (OutputStream os = conn.getOutputStream()) {
                byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
                os.write(bytes);
            }

            int status = conn.getResponseCode();

            if (status == HttpURLConnection.HTTP_CREATED
                    || status == HttpURLConnection.HTTP_OK) {

                // OK -> regresar al listado de productos
                conn.disconnect();
                response.sendRedirect(request.getContextPath() + "/electroquito/productos");
                return;

            } else {
                // Leer cuerpo de error (si lo hay) para mostrar algo útil
                StringBuilder body = new StringBuilder();
                try (InputStream is = conn.getErrorStream() != null
                        ? conn.getErrorStream()
                        : conn.getInputStream();
                     BufferedReader br = new BufferedReader(
                             new InputStreamReader(is, StandardCharsets.UTF_8))) {

                    String line;
                    while ((line = br.readLine()) != null) {
                        body.append(line);
                    }
                }
                conn.disconnect();

                String msg = "No se pudo guardar el producto (HTTP " + status + ").";
                if (!body.isEmpty()) {
                    msg += " Detalle: " + body;
                }
                request.setAttribute("error", msg);
                reenviarFormulario(request, response);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("error",
                    "Ocurrió un error inesperado al guardar el producto.");
            reenviarFormulario(request, response);
        }
    }

    private void reenviarFormulario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        RequestDispatcher rd = request.getRequestDispatcher(
                "/WEB-INF/views/electroquito/electroquitoNuevoProducto.jsp");
        rd.forward(request, response);
    }
}
