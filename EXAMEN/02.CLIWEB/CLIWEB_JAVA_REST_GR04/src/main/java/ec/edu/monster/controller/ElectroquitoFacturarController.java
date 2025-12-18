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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebServlet("/electroquito/facturacion/nueva")
public class ElectroquitoFacturarController extends HttpServlet {

    private static final String BASE_URL = AppConfig.COMERCIALIZADORA_API_BASE;
    private static final String BASE_HOST = AppConfig.COMERCIALIZADORA_HOST_BASE;

    // DTOs para el POST de factura (solo del lado cliente)
    public static class DetalleFacturaRequest {
        public Long idElectrodomestico;
        public int cantidad;
    }

    public static class CrearFacturaRequest {
        public String cedulaCliente;
        public String nombreCliente;
        public Long idElectrodomestico;
        public int cantidad;
        public List<DetalleFacturaRequest> productos;
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
            request.setAttribute("itemsStateValue", "[]");

            RequestDispatcher rd = request.getRequestDispatcher(
                    "/WEB-INF/views/electroquito/electroquitoFacturar.jsp");
            rd.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error",
                    "No se pudo cargar el catálogo de productos. Intente nuevamente.");
            request.setAttribute("productos", List.of());
            request.setAttribute("imagenBaseUrl", BASE_HOST);
            request.setAttribute("itemsStateValue", "[]");

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
        String formaPago = request.getParameter("formaPago");
        String plazoMesesStr = request.getParameter("plazoMeses");
        String numCuentaCredito = request.getParameter("numCuentaCredito");
        String itemsStateJson = request.getParameter("itemsState");

        Jsonb jsonb = JsonbBuilder.create();

        if (cedula == null || cedula.isBlank()
                || nombreCompleto == null || nombreCompleto.isBlank()) {

            request.setAttribute("error",
                    "Complete los datos del cliente antes de generar la factura.");
            recargarProductosYForward(request, response);
            return;
        }

        if (itemsStateJson == null || itemsStateJson.isBlank()) {
            request.setAttribute("error",
                    "Agregue al menos un producto a la factura.");
            request.setAttribute("itemsStateValue", "[]");
            recargarProductosYForward(request, response);
            return;
        }

        DetalleFacturaRequest[] itemsArray;
        try {
            itemsArray = jsonb.fromJson(itemsStateJson, DetalleFacturaRequest[].class);
        } catch (Exception ex) {
            request.setAttribute("error",
                    "No se pudo leer la lista de productos seleccionados. Intente nuevamente.");
            request.setAttribute("itemsStateValue", "[]");
            recargarProductosYForward(request, response);
            return;
        }

        List<DetalleFacturaRequest> detalles = new ArrayList<>();
        if (itemsArray != null) {
            for (DetalleFacturaRequest item : itemsArray) {
                if (item == null || item.idElectrodomestico == null) {
                    continue;
                }
                if (item.cantidad <= 0) {
                    request.setAttribute("error",
                            "Cada producto debe tener una cantidad mayor a cero.");
                    request.setAttribute("itemsStateValue", itemsStateJson);
                    recargarProductosYForward(request, response);
                    return;
                }
                DetalleFacturaRequest detalle = new DetalleFacturaRequest();
                detalle.idElectrodomestico = item.idElectrodomestico;
                detalle.cantidad = item.cantidad;
                detalles.add(detalle);
            }
        }

        if (detalles.isEmpty()) {
            request.setAttribute("error",
                    "Agregue al menos un producto a la factura.");
            request.setAttribute("itemsStateValue", "[]");
            recargarProductosYForward(request, response);
            return;
        }

        String sanitizedItemsJson = jsonb.toJson(detalles);
        request.setAttribute("itemsStateValue", sanitizedItemsJson);

        CrearFacturaRequest facturaReq = new CrearFacturaRequest();
        facturaReq.cedulaCliente = cedula.trim();
        facturaReq.nombreCliente = nombreCompleto.trim();
        facturaReq.idElectrodomestico = detalles.get(0).idElectrodomestico;
        facturaReq.cantidad = detalles.get(0).cantidad;
        facturaReq.productos = detalles;
        facturaReq.formaPago = (formaPago == null || formaPago.isBlank())
                ? "EFECTIVO"
                : formaPago.trim().toUpperCase();
        
        // Agregar plazoMeses y numCuentaCredito si es pago a crédito
        if ("CREDITO".equals(facturaReq.formaPago)) {
            if (plazoMesesStr != null && !plazoMesesStr.isBlank()) {
                try {
                    facturaReq.plazoMeses = Integer.parseInt(plazoMesesStr.trim());
                } catch (NumberFormatException e) {
                    // Ignorar si no es un número válido
                }
            }
            if (numCuentaCredito != null && !numCuentaCredito.isBlank()) {
                facturaReq.numCuentaCredito = numCuentaCredito.trim();
            }
        }

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
                request.setAttribute("itemsStateValue", "[]");

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

        if (request.getAttribute("itemsStateValue") == null) {
            String paramState = request.getParameter("itemsState");
            if (paramState != null && !paramState.isBlank()) {
                request.setAttribute("itemsStateValue", paramState);
            } else {
                request.setAttribute("itemsStateValue", "[]");
            }
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
