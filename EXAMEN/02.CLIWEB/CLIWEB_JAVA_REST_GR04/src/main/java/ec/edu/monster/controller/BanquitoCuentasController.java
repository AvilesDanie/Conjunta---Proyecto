package ec.edu.monster.controller;

import ec.edu.monster.config.AppConfig;
import ec.edu.monster.dto.CuentaDTO;
import ec.edu.monster.dto.NuevaCuentaRequestDTO;
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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@WebServlet("/banquito/cuentas")
public class BanquitoCuentasController extends HttpServlet {

    private static final String BASE_URL = AppConfig.BANQUITO_API_BASE;

    private static class CuentaCreadaResponse {
        public String numCuenta;
        public String tipoCuenta;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Object usuarioSesion = request.getSession().getAttribute("usuarioSesion");
        if (usuarioSesion == null) {
            response.sendRedirect(request.getContextPath() + "/banquito/login");
            return;
        }

        String cedula = request.getParameter("cedula");
        mostrarPagina(request, response, cedula);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Object usuarioSesion = request.getSession().getAttribute("usuarioSesion");
        if (usuarioSesion == null) {
            response.sendRedirect(request.getContextPath() + "/banquito/login");
            return;
        }
        
        String cedulaNueva = request.getParameter("nuevaCuentaCedula");
        String tipoCuenta = request.getParameter("nuevaCuentaTipo");
        String saldoStr = request.getParameter("nuevaCuentaSaldo");

        request.setAttribute("nuevaCuentaCedula", cedulaNueva != null ? cedulaNueva.trim() : "");
        request.setAttribute("nuevaCuentaTipo", tipoCuenta != null ? tipoCuenta : "");
        request.setAttribute("nuevaCuentaSaldo", saldoStr != null ? saldoStr : "");

        boolean hayError = false;
        if (cedulaNueva == null || cedulaNueva.isBlank() ||
                tipoCuenta == null || tipoCuenta.isBlank()) {
            request.setAttribute("nuevaCuentaError",
                    "La cédula y el tipo de cuenta son obligatorios.");
            hayError = true;
        }

        BigDecimal saldoInicial = null;
        if (!hayError && saldoStr != null && !saldoStr.isBlank()) {
            try {
                saldoInicial = new BigDecimal(saldoStr.replace(",", "."));
            } catch (NumberFormatException e) {
                request.setAttribute("nuevaCuentaError",
                        "El saldo inicial debe ser un número válido.");
                hayError = true;
            }
        }

        if (!hayError) {
            try {
                CuentaCreadaResponse creada = crearCuentaEnAPI(
                        cedulaNueva.trim(),
                        tipoCuenta.trim(),
                        saldoInicial);
                String mensaje = "Cuenta " + creada.numCuenta +
                        " creada correctamente.";
                request.setAttribute("nuevaCuentaExito", mensaje);
                request.setAttribute("nuevaCuentaSaldo", "");
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("nuevaCuentaError",
                        "No se pudo crear la cuenta. Detalle: " + e.getMessage());
            }
        }

        mostrarPagina(request, response, cedulaNueva);
    }

    private void mostrarPagina(HttpServletRequest request, HttpServletResponse response,
                               String cedula)
            throws ServletException, IOException {

        String cedulaNormalizada = cedula != null ? cedula.trim() : "";
        request.setAttribute("cedulaBuscada", cedulaNormalizada);

        if (request.getAttribute("nuevaCuentaCedula") == null) {
            request.setAttribute("nuevaCuentaCedula", cedulaNormalizada);
        }
        if (request.getAttribute("nuevaCuentaTipo") == null) {
            request.setAttribute("nuevaCuentaTipo", "");
        }
        if (request.getAttribute("nuevaCuentaSaldo") == null) {
            request.setAttribute("nuevaCuentaSaldo", "");
        }

        try {
            List<CuentaDTO> cuentas = cedulaNormalizada.isBlank()
                    ? List.of()
                    : obtenerCuentasPorCedula(cedulaNormalizada);
            request.setAttribute("cuentas", cuentas);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error",
                    "No se pudo cargar las cuentas. Detalle: " + e.getMessage());
            request.setAttribute("cuentas", List.of());
        }

        RequestDispatcher rd = request.getRequestDispatcher(
                "/WEB-INF/views/banquito/banquitoCuentas.jsp");
        rd.forward(request, response);
    }

    // ================== Consumo del REST ==================

    private List<CuentaDTO> obtenerCuentasPorCedula(String cedula) throws IOException {
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

    private CuentaCreadaResponse crearCuentaEnAPI(String cedula,
                                                  String tipo,
                                                  BigDecimal saldoInicial) throws IOException {

        URL url = new URL(BASE_URL + "/cuentas");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setDoOutput(true);

        NuevaCuentaRequestDTO payload = new NuevaCuentaRequestDTO();
        payload.cedulaCliente = cedula;
        payload.tipoCuenta = tipo.toUpperCase();
        payload.saldoInicial = saldoInicial;

        Jsonb jsonb = JsonbBuilder.create();
        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonb.toJson(payload).getBytes(StandardCharsets.UTF_8));
        }

        int status = conn.getResponseCode();
        if (status != HttpURLConnection.HTTP_CREATED
                && status != HttpURLConnection.HTTP_OK) {
            String detalle = leerDetalleError(conn);
            throw new IOException("HTTP " + status +
                    (detalle.isBlank() ? "" : " - " + detalle));
        }

        try (InputStream is = conn.getInputStream();
             Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            return jsonb.fromJson(reader, CuentaCreadaResponse.class);
        } finally {
            conn.disconnect();
        }
    }

    private String leerDetalleError(HttpURLConnection conn) {
        try (InputStream es = conn.getErrorStream()) {
            if (es == null) {
                return "";
            }
            return new String(es.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            return "";
        }
    }
}
