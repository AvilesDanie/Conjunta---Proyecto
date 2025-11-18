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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@WebServlet("/electroquito/facturacion/nueva")
public class ElectroquitoFacturarController extends HttpServlet {

    private static final String BASE_URL = AppConfig.COMERCIALIZADORA_API_BASE;
    private static final String BASE_HOST = AppConfig.COMERCIALIZADORA_HOST_BASE;

    // DTOs para el POST de factura (solo del lado cliente)
    public static class CrearFacturaRequest {
        public String cedulaCliente;
        public String nombreCliente;
        public Long idElectrodomestico;
        public int cantidad;
        public String formaPago;
        public Integer plazoMeses;
        public String numCuentaCredito;
    }

    public static class FacturaResponse {
        public Long id;
        public String fechaEmision;
        public String clienteNombre;
        public String clienteCedula;
        public BigDecimal total;
    }

    // =================== GET: mostrar formulario ===================

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // IMPORTANTE: usar el MISMO atributo de sesión que el home.jsp
        Object usuarioSesion = request.getSession().getAttribute("usuarioSesion");
        if (usuarioSesion == null) {
            // Si no hay sesión, a login
            response.sendRedirect(request.getContextPath() + "/electroquito/login");
            return;
        }

        try {
            List<ElectrodomesticoDTO> productos = obtenerProductosDesdeAPI();
            request.setAttribute("productos", productos);
            request.setAttribute("imagenBaseUrl", BASE_HOST);

            RequestDispatcher rd = request.getRequestDispatcher(
                    "/WEB-INF/views/electroquito/electroquitoFacturar.jsp");
            rd.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error",
                    "No se pudo cargar el catálogo de productos. Intente nuevamente.");
            request.setAttribute("productos", List.of());
            request.setAttribute("imagenBaseUrl", BASE_HOST);

            RequestDispatcher rd = request.getRequestDispatcher(
                    "/WEB-INF/views/electroquito/electroquitoFacturar.jsp");
            rd.forward(request, response);
        }
    }

    // =================== POST: generar factura ===================

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Object usuarioSesion = request.getSession().getAttribute("usuarioSesion");
        if (usuarioSesion == null) {
            response.sendRedirect(request.getContextPath() + "/electroquito/login");
            return;
        }

        String cedula = request.getParameter("cedula");
        String nombreCompleto = request.getParameter("nombreCompleto");
        String productoIdStr = request.getParameter("productoId");
        String cantidadStr = request.getParameter("cantidad");
        String formaPago = request.getParameter("formaPago");

        if (cedula == null || cedula.isBlank()
                || nombreCompleto == null || nombreCompleto.isBlank()
                || productoIdStr == null || productoIdStr.isBlank()
                || cantidadStr == null || cantidadStr.isBlank()) {

            request.setAttribute("error",
                    "Complete todos los campos y seleccione un producto para generar la factura.");
            recargarProductosYForward(request, response);
            return;
        }

        int cantidad;
        Long productoId;
        try {
            cantidad = Integer.parseInt(cantidadStr);
            productoId = Long.parseLong(productoIdStr);
        } catch (NumberFormatException ex) {
            request.setAttribute("error", "Cantidad o producto inválidos.");
            recargarProductosYForward(request, response);
            return;
        }

        if (cantidad <= 0) {
            request.setAttribute("error", "La cantidad debe ser mayor a cero.");
            recargarProductosYForward(request, response);
            return;
        }

        CrearFacturaRequest facturaReq = new CrearFacturaRequest();
        facturaReq.cedulaCliente = cedula.trim();
        facturaReq.nombreCliente = nombreCompleto.trim();
        facturaReq.idElectrodomestico = productoId;
        facturaReq.cantidad = cantidad;
        facturaReq.formaPago = (formaPago == null || formaPago.isBlank())
                ? "EFECTIVO"
                : formaPago.trim().toUpperCase();

        Jsonb jsonb = JsonbBuilder.create();

        try {
            URL url = new URL(BASE_URL + "/facturas");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Enviamos el JSON
            try (OutputStream os = conn.getOutputStream()) {
                String json = jsonb.toJson(facturaReq);
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }

            int status = conn.getResponseCode();

            if (status == HttpURLConnection.HTTP_CREATED
                    || status == HttpURLConnection.HTTP_OK) {

                try (InputStream is = conn.getInputStream();
                     Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {

                    FacturaResponse factura =
                            jsonb.fromJson(reader, FacturaResponse.class);

                    if (factura != null && factura.id != null) {
                        request.setAttribute("exito",
                                "Factura generada correctamente. N.º " + factura.id);
                    } else {
                        request.setAttribute("exito",
                                "Factura generada correctamente.");
                    }
                }

            } else {
                String detalle = "";
                try (InputStream es = conn.getErrorStream()) {
                    if (es != null) {
                        detalle = new String(es.readAllBytes(), StandardCharsets.UTF_8);
                    }
                }
                request.setAttribute("error",
                        "No se pudo generar la factura. HTTP " + status +
                                (detalle.isBlank() ? "" : " - " + detalle));
            }

            conn.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error",
                    "Ocurrió un error al comunicarse con el servidor de facturas.");
        }

        recargarProductosYForward(request, response);
    }

    // =================== Helpers ===================

    private void recargarProductosYForward(HttpServletRequest request,
                                           HttpServletResponse response)
            throws ServletException, IOException {

        try {
            List<ElectrodomesticoDTO> productos = obtenerProductosDesdeAPI();
            request.setAttribute("productos", productos);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error",
                    "No se pudo cargar el catálogo de productos. Intente nuevamente.");
        }

        RequestDispatcher rd = request.getRequestDispatcher(
                "/WEB-INF/views/electroquito/electroquitoFacturar.jsp");
        rd.forward(request, response);
    }

    private List<ElectrodomesticoDTO> obtenerProductosDesdeAPI() throws IOException {
        String urlStr = BASE_URL + "/electrodomesticos";

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
