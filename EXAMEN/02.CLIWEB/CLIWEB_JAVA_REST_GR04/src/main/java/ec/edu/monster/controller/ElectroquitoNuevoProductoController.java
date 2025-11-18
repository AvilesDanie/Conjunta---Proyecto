package ec.edu.monster.controller;

import ec.edu.monster.config.AppConfig;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

@WebServlet("/electroquito/productos/nuevo")
@MultipartConfig(maxFileSize = 5 * 1024 * 1024)
public class ElectroquitoNuevoProductoController extends HttpServlet {

    private static final String BASE_URL = AppConfig.COMERCIALIZADORA_API_BASE;

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

        Part imagenPart = request.getPart("imagen");
        if (imagenPart == null || imagenPart.getSize() == 0) {
            request.setAttribute("error",
                    "Selecciona una imagen para el producto (PNG o JPG).");
            reenviarFormulario(request, response);
            return;
        }

        try {
            String urlStr = BASE_URL + "/electrodomesticos"; // ajusta si tu path es distinto
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");

            String boundary = "----EQForm" + System.currentTimeMillis();
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                escribirCampo(os, boundary, "codigo", codigo.trim());
                escribirCampo(os, boundary, "nombre", nombre.trim());
                escribirCampo(os, boundary, "precioVenta", precio.toPlainString());
                escribirArchivo(os, boundary, "imagen", imagenPart);
                os.write(("--" + boundary + "--\r\n").getBytes(StandardCharsets.UTF_8));
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
                    msg += " Detalle: " + limpiarHtml(body.toString());
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

    private String limpiarHtml(String origen) {
        return origen.replaceAll("<[^>]+>", "")
                .replace("&nbsp;", " ")
                .trim();
    }

    private void escribirCampo(OutputStream os, String boundary, String nombre, String valor)
            throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("--").append(boundary).append("\r\n");
        sb.append("Content-Disposition: form-data; name=\"").append(nombre).append("\"\r\n\r\n");
        sb.append(valor).append("\r\n");
        os.write(sb.toString().getBytes(StandardCharsets.UTF_8));
    }

    private void escribirArchivo(OutputStream os, String boundary, String nombre, Part part)
            throws IOException {
        String fileName = Paths.get(part.getSubmittedFileName()).getFileName().toString();
        String contentType = part.getContentType() != null
                ? part.getContentType()
                : "application/octet-stream";

        StringBuilder sb = new StringBuilder();
        sb.append("--").append(boundary).append("\r\n");
        sb.append("Content-Disposition: form-data; name=\"").append(nombre)
                .append("\"; filename=\"").append(fileName).append("\"\r\n");
        sb.append("Content-Type: ").append(contentType).append("\r\n\r\n");
        os.write(sb.toString().getBytes(StandardCharsets.UTF_8));

        try (InputStream is = part.getInputStream()) {
            is.transferTo(os);
        }
        os.write("\r\n".getBytes(StandardCharsets.UTF_8));
    }
}
