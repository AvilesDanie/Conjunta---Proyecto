package ec.edu.monster.controller;

import ec.edu.monster.config.AppConfig;
import ec.edu.monster.dto.CuentaDTO;
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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@WebServlet("/banquito/cuentas/cliente")
public class BanquitoCuentasClienteController extends HttpServlet {

    private static final String BASE_URL = AppConfig.BANQUITO_API_BASE;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Object usuarioSesion = request.getSession().getAttribute("usuarioSesion");
        if (usuarioSesion == null) {
            response.sendRedirect(request.getContextPath() + "/banquito/login");
            return;
        }

        String cedula = request.getParameter("cedula");
        String nombre = request.getParameter("nombre");

        if (cedula == null || cedula.isBlank()) {
            response.sendRedirect(request.getContextPath() + "/banquito/clientes");
            return;
        }

        try {
            List<CuentaDTO> cuentas = obtenerCuentasPorCedula(cedula);

            // Formateador de moneda para mostrar saldos
            Locale ec = new Locale("es", "EC");
            NumberFormat nf = NumberFormat.getCurrencyInstance(ec);
            request.setAttribute("monedaFormat", nf);

            request.setAttribute("cuentas", cuentas);
            request.setAttribute("cedula", cedula);
            request.setAttribute("nombre", nombre != null ? nombre : "");

            RequestDispatcher rd = request.getRequestDispatcher(
                    "/WEB-INF/views/banquito/banquitoCuentasCliente.jsp");
            rd.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("cuentas", List.of());
            request.setAttribute("cedula", cedula);
            request.setAttribute("nombre", nombre != null ? nombre : "");
            request.setAttribute("error",
                    "No se pudieron cargar las cuentas del cliente.");
            RequestDispatcher rd = request.getRequestDispatcher(
                    "/WEB-INF/views/banquito/banquitoCuentasCliente.jsp");
            rd.forward(request, response);
        }
    }

    private List<CuentaDTO> obtenerCuentasPorCedula(String cedula) throws IOException {
        // Ajusta esta ruta según tu API real:
        // opciones típicas: /cuentas/cliente/{cedula} o /cuentas?cedula=
        String urlStr = BASE_URL + "/cuentas/cliente/" +
                URLEncoder.encode(cedula, StandardCharsets.UTF_8);

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        int status = conn.getResponseCode();
        if (status != HttpURLConnection.HTTP_OK) {
            throw new IOException("HTTP " + status + " al obtener cuentas");
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
}
