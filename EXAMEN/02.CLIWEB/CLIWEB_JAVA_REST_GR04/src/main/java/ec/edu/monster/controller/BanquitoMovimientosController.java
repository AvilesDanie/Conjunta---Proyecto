package ec.edu.monster.controller;

import ec.edu.monster.config.AppConfig;
import ec.edu.monster.dto.MovimientoCrearRequestDTO;
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
import java.io.OutputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@WebServlet("/banquito/movimientos")
public class BanquitoMovimientosController extends HttpServlet {

    private static final String BASE_URL = AppConfig.BANQUITO_API_BASE;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Object usuarioSesion = request.getSession().getAttribute("usuarioSesion");
        if (usuarioSesion == null) {
            response.sendRedirect(request.getContextPath() + "/banquito/login");
            return;
        }

        String numeroCuenta = request.getParameter("cuenta");
        mostrarPagina(request, response, numeroCuenta);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Object usuarioSesion = request.getSession().getAttribute("usuarioSesion");
        if (usuarioSesion == null) {
            response.sendRedirect(request.getContextPath() + "/banquito/login");
            return;
        }

        String tipoMovimiento = request.getParameter("movimientoTipo");
        String cuentaParaConsulta = null;

        if (tipoMovimiento == null) {
            request.setAttribute("error", "Seleccione el tipo de movimiento.");
            mostrarPagina(request, response, request.getParameter("cuenta"));
            return;
        }

        try {
            MovimientoCrearRequestDTO dto = construirDTODesdeFormulario(request, tipoMovimiento);
            registrarMovimiento(dto);
            request.setAttribute("exito", "Movimiento registrado correctamente.");

            if ("TRA".equalsIgnoreCase(tipoMovimiento)) {
                cuentaParaConsulta = dto.numCuentaOrigen;
            } else {
                cuentaParaConsulta = dto.numCuenta;
            }
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            cuentaParaConsulta = request.getParameter("cuenta");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error",
                    "No se pudo registrar el movimiento. Detalle: " + e.getMessage());
            cuentaParaConsulta = request.getParameter("cuenta");
        }

        mostrarPagina(request, response, cuentaParaConsulta);
    }

    private void mostrarPagina(HttpServletRequest request, HttpServletResponse response,
                               String numeroCuenta)
            throws ServletException, IOException {

        String cuentaNormalizada = numeroCuenta != null ? numeroCuenta.trim() : "";
        request.setAttribute("numeroCuenta", cuentaNormalizada);

        if (cuentaNormalizada.isBlank()) {
            if (request.getAttribute("error") == null
                    && request.getAttribute("exito") == null) {
                request.setAttribute("error",
                        "Falta el número de cuenta para consultar movimientos.");
            }
            request.setAttribute("movimientos", List.of());
        } else {
            try {
                List<MovimientoDTO> movimientos = obtenerMovimientosPorCuenta(cuentaNormalizada);
                request.setAttribute("movimientos", movimientos);
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

    private MovimientoCrearRequestDTO construirDTODesdeFormulario(HttpServletRequest request,
                                                                  String tipoMovimiento) {
        MovimientoCrearRequestDTO dto = new MovimientoCrearRequestDTO();
        dto.tipo = tipoMovimiento.toUpperCase();
        String fecha = request.getParameter("movimientoFecha");
        if (fecha != null && !fecha.isBlank()) {
            dto.fecha = fecha;
        }

        if ("TRA".equalsIgnoreCase(dto.tipo)) {
            String origen = request.getParameter("transferOrigen");
            String destino = request.getParameter("transferDestino");
            if (origen == null || origen.isBlank() || destino == null || destino.isBlank()) {
                throw new IllegalArgumentException("La cuenta origen y destino son obligatorias.");
            }
            if (origen.equals(destino)) {
                throw new IllegalArgumentException("Las cuentas deben ser diferentes para transferencias.");
            }
            dto.numCuentaOrigen = origen.trim();
            dto.numCuentaDestino = destino.trim();
            dto.valor = parseMonto(request.getParameter("transferMonto"));
            String fechaTransfer = request.getParameter("transferFecha");
            if (fechaTransfer != null && !fechaTransfer.isBlank()) {
                dto.fecha = fechaTransfer;
            }
        } else {
            String cuenta = request.getParameter("cuentaSimple");
            if (cuenta == null || cuenta.isBlank()) {
                throw new IllegalArgumentException("Debe indicar el número de cuenta.");
            }
            dto.numCuenta = cuenta.trim();
            dto.valor = parseMonto(request.getParameter("montoSimple"));
            String fechaSimple = request.getParameter("fechaSimple");
            if (fechaSimple != null && !fechaSimple.isBlank()) {
                dto.fecha = fechaSimple;
            }
        }
        return dto;
    }

    private BigDecimal parseMonto(String monto) {
        if (monto == null || monto.isBlank()) {
            throw new IllegalArgumentException("El monto es obligatorio.");
        }
        try {
            BigDecimal valor = new BigDecimal(monto.replace(",", "."));
            if (valor.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("El monto debe ser mayor a cero.");
            }
            return valor;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El monto debe ser numérico.");
        }
    }

    private void registrarMovimiento(MovimientoCrearRequestDTO dto) throws IOException {
        URL url = new URL(BASE_URL + "/movimientos");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setDoOutput(true);

        Jsonb jsonb = JsonbBuilder.create();
        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonb.toJson(dto).getBytes(StandardCharsets.UTF_8));
        }

        int status = conn.getResponseCode();
        if (status != HttpURLConnection.HTTP_CREATED
                && status != HttpURLConnection.HTTP_OK) {
            String detalle = leerDetalleError(conn);
            throw new IOException("HTTP " + status +
                    (detalle.isBlank() ? "" : " - " + detalle));
        }

        conn.disconnect();
    }

    // ================== Consumo del REST ==================

    private List<MovimientoDTO> obtenerMovimientosPorCuenta(String numeroCuenta)
            throws IOException {

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
