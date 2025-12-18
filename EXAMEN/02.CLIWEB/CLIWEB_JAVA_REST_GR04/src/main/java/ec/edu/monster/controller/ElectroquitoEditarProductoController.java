package ec.edu.monster.controller;

import ec.edu.monster.config.AppConfig;
import ec.edu.monster.dto.ElectrodomesticoDTO;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Arrays;

@WebServlet("/electroquito/productos/editar")
@MultipartConfig(maxFileSize = 5 * 1024 * 1024)
public class ElectroquitoEditarProductoController extends HttpServlet {

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

        String idParam = request.getParameter("id");
        Long id = parseId(idParam);
        if (id == null) {
            registrarFlashError(request, "Selecciona un producto válido para editar.");
            response.sendRedirect(request.getContextPath() + "/electroquito/productos");
            return;
        }

        try {
            ElectrodomesticoDTO producto = obtenerProductoPorId(id);
            if (producto == null) {
                registrarFlashError(request, "El producto solicitado ya no existe.");
                response.sendRedirect(request.getContextPath() + "/electroquito/productos");
                return;
            }

            request.setAttribute("producto", producto);
            request.setAttribute("imagenActual", resolverImagenActual(producto));

            RequestDispatcher rd = request.getRequestDispatcher(
                    "/WEB-INF/views/electroquito/electroquitoEditarProducto.jsp");
            rd.forward(request, response);

        } catch (Exception ex) {
            ex.printStackTrace();
            registrarFlashError(request, "No se pudo cargar la información del producto.");
            response.sendRedirect(request.getContextPath() + "/electroquito/productos");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Object usuarioSesion = session != null ? session.getAttribute("usuarioSesion") : null;
        if (usuarioSesion == null) {
            response.sendRedirect(request.getContextPath() + "/electroquito/login");
            return;
        }

        request.setCharacterEncoding("UTF-8");

        String idParam = request.getParameter("id");
        Long id = parseId(idParam);
        if (id == null) {
            registrarFlashError(request, "El identificador del producto es inválido.");
            response.sendRedirect(request.getContextPath() + "/electroquito/productos");
            return;
        }

        String codigo = request.getParameter("codigo");
        String nombre = request.getParameter("nombre");
        String precioStr = request.getParameter("precio");
        String imagenActual = request.getParameter("imagenActual");
        if (imagenActual == null) {
            imagenActual = "";
        }

        ElectrodomesticoDTO producto = new ElectrodomesticoDTO();
        producto.setId(id);
        String codigoTrim = codigo != null ? codigo.trim() : "";
        String nombreTrim = nombre != null ? nombre.trim() : "";
        producto.setCodigo(codigoTrim);
        producto.setNombre(nombreTrim);

        BigDecimal precio;
        try {
            if (precioStr == null || precioStr.isBlank()) {
                throw new NumberFormatException("precio vacio");
            }
            precio = new BigDecimal(precioStr.trim());
            if (precio.compareTo(BigDecimal.ZERO) <= 0) {
                throw new NumberFormatException("precio <= 0");
            }
        } catch (NumberFormatException ex) {
            request.setAttribute("error", "Ingresa un precio de venta válido.");
            reenviarFormulario(request, response, producto, imagenActual);
            return;
        }
        producto.setPrecioVenta(precio);

        if (codigoTrim.isBlank() || nombreTrim.isBlank()) {
            request.setAttribute("error",
                    "Los campos Código y Nombre son obligatorios.");
            reenviarFormulario(request, response, producto, imagenActual);
            return;
        }

        Part imagenPart = request.getPart("imagen");
        boolean incluirImagen = imagenPart != null && imagenPart.getSize() > 0;

        try {
            String urlStr = BASE_URL + "/electrodomesticos/" + id;
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setDoOutput(true);

            String boundary = "----EQEdit" + System.currentTimeMillis();
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            conn.setRequestProperty("Accept", "application/json");

            try (OutputStream os = conn.getOutputStream()) {
                escribirCampo(os, boundary, "codigo", codigoTrim);
                escribirCampo(os, boundary, "nombre", nombreTrim);
                escribirCampo(os, boundary, "precioVenta", precio.toPlainString());

                if (incluirImagen) {
                    escribirArchivo(os, boundary, "imagen", imagenPart);
                }

                os.write(("--" + boundary + "--\r\n").getBytes(StandardCharsets.UTF_8));
            }

            int status = conn.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK
                    || status == HttpURLConnection.HTTP_NO_CONTENT) {

                conn.disconnect();
                registrarFlashExito(request, "Producto actualizado correctamente.");
                response.sendRedirect(request.getContextPath() + "/electroquito/productos");
                return;
            }

            String detalle = leerRespuesta(conn);
            conn.disconnect();
            request.setAttribute("error",
                    "No se pudo actualizar el producto (HTTP " + status + "). "
                            + (detalle.isBlank() ? "" : detalle));
            reenviarFormulario(request, response, producto, imagenActual);

        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("error",
                    "Ocurrió un error inesperado al actualizar el producto.");
            reenviarFormulario(request, response, producto, imagenActual);
        }
    }

    private ElectrodomesticoDTO obtenerProductoPorId(Long id) throws IOException {
        String urlStr = BASE_URL + "/electrodomesticos";
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        int status = conn.getResponseCode();
        if (status != HttpURLConnection.HTTP_OK) {
            conn.disconnect();
            throw new IOException("Error HTTP al obtener productos: " + status);
        }

        try (InputStream is = conn.getInputStream();
             Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {

            Jsonb jsonb = JsonbBuilder.create();
            ElectrodomesticoDTO[] array =
                    jsonb.fromJson(reader, ElectrodomesticoDTO[].class);
            return Arrays.stream(array)
                    .filter(p -> p.getId() != null && p.getId().equals(id))
                    .findFirst()
                    .orElse(null);
        } finally {
            conn.disconnect();
        }
    }

    private void reenviarFormulario(HttpServletRequest request, HttpServletResponse response,
                                    ElectrodomesticoDTO producto, String imagenActual)
            throws ServletException, IOException {
        request.setAttribute("producto", producto);
        request.setAttribute("imagenActual", imagenActual != null ? imagenActual : "");
        RequestDispatcher rd = request.getRequestDispatcher(
                "/WEB-INF/views/electroquito/electroquitoEditarProducto.jsp");
        rd.forward(request, response);
    }

    private String resolverImagenActual(ElectrodomesticoDTO producto) {
        if (producto == null || producto.getImagenUrl() == null
                || producto.getImagenUrl().isBlank()) {
            return "";
        }
        String url = producto.getImagenUrl();
        if (url.startsWith("http")) {
            return url;
        }
        return BASE_HOST.concat(url);
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

    private String leerRespuesta(HttpURLConnection conn) throws IOException {
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
            return limpiarHtml(sb.toString());
        }
    }

    private String limpiarHtml(String origen) {
        if (origen == null) {
            return "";
        }
        return origen.replaceAll("<[^>]+>", "")
                .replace("&nbsp;", " ")
                .trim();
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
}
