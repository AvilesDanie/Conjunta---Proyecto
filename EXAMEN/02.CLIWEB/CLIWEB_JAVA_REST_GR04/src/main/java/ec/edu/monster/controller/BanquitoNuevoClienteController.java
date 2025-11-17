package ec.edu.monster.controller;

import ec.edu.monster.dto.NuevoClienteRequestDTO;
import ec.edu.monster.dto.ClienteDTO;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@WebServlet("/banquito/clientes/nuevo")
public class BanquitoNuevoClienteController extends HttpServlet {

    private static final String BASE_URL =
            "http://localhost:8080/WS_JAVA_REST_BanQuito/api";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Validar sesión
        Object usuarioSesion = request.getSession().getAttribute("usuarioSesion");
        if (usuarioSesion == null) {
            response.sendRedirect(request.getContextPath() + "/banquito/login");
            return;
        }

        // Valores por defecto para el formulario
        request.setAttribute("cedula", "");
        request.setAttribute("nombre", "");
        request.setAttribute("fechaNacimiento", "");
        request.setAttribute("estadoCivil", "");
        request.setAttribute("tipoCuentaInicial", "");

        RequestDispatcher rd = request.getRequestDispatcher(
                "/WEB-INF/views/banquito/banquitoNuevoCliente.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // Validar sesión
        Object usuarioSesion = request.getSession().getAttribute("usuarioSesion");
        if (usuarioSesion == null) {
            response.sendRedirect(request.getContextPath() + "/banquito/login");
            return;
        }

        // Leer campos del formulario
        String cedula = request.getParameter("cedula");
        String nombre = request.getParameter("nombre");
        String fechaNacimiento = request.getParameter("fechaNacimiento");
        String estadoCivil = request.getParameter("estadoCivil");
        String tipoCuentaInicial = request.getParameter("tipoCuentaInicial");

        // Volver a poner valores en request para no perderlos si hay error
        request.setAttribute("cedula", cedula);
        request.setAttribute("nombre", nombre);
        request.setAttribute("fechaNacimiento", fechaNacimiento);
        request.setAttribute("estadoCivil", estadoCivil);
        request.setAttribute("tipoCuentaInicial", tipoCuentaInicial);

        // Validaciones básicas
        if (cedula == null || cedula.isBlank()
                || nombre == null || nombre.isBlank()
                || fechaNacimiento == null || fechaNacimiento.isBlank()
                || estadoCivil == null || estadoCivil.isBlank()
                || tipoCuentaInicial == null || tipoCuentaInicial.isBlank()) {

            request.setAttribute("error",
                    "Todos los campos marcados con * son obligatorios.");
            RequestDispatcher rd = request.getRequestDispatcher(
                    "/WEB-INF/views/banquito/banquitoNuevoCliente.jsp");
            rd.forward(request, response);
            return;
        }

        try {
            // Construir DTO
            NuevoClienteRequestDTO dto = new NuevoClienteRequestDTO();
            dto.cedula = cedula.trim();
            dto.nombre = nombre.trim();
            dto.fechaNacimiento = fechaNacimiento.trim();
            dto.estadoCivil = estadoCivil.trim();
            dto.tipoCuentaInicial = tipoCuentaInicial.trim();

            ClienteDTO creado = crearClienteEnAPI(dto);

            // Éxito: mensaje y limpiar campos
            request.setAttribute("exito",
                    "Cliente registrado correctamente con número de cédula " + creado.cedula);

            request.setAttribute("cedula", "");
            request.setAttribute("nombre", "");
            request.setAttribute("fechaNacimiento", "");
            request.setAttribute("estadoCivil", "");
            request.setAttribute("tipoCuentaInicial", "");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error",
                    "No se pudo registrar el cliente. Detalle: " + e.getMessage());
        }

        RequestDispatcher rd = request.getRequestDispatcher(
                "/WEB-INF/views/banquito/banquitoNuevoCliente.jsp");
        rd.forward(request, response);
    }

    // ================== Consumo del REST (POST /clientes) ==================

    private ClienteDTO crearClienteEnAPI(NuevoClienteRequestDTO dto) throws IOException {

        String urlStr = BASE_URL + "/clientes";  // coincide con @Path("clientes") del servidor
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setDoOutput(true);

        Jsonb jsonb = JsonbBuilder.create();
        String json = jsonb.toJson(dto);

        try (OutputStream os = conn.getOutputStream();
             Writer w = new OutputStreamWriter(os, StandardCharsets.UTF_8)) {
            w.write(json);
        }

        int status = conn.getResponseCode();
        if (status != HttpURLConnection.HTTP_OK
                && status != HttpURLConnection.HTTP_CREATED) {

            // Intentar leer mensaje de error del backend
            String backendMsg = null;
            try (InputStream es = conn.getErrorStream()) {
                if (es != null) {
                    backendMsg = new String(es.readAllBytes(), StandardCharsets.UTF_8);
                }
            }
            if (backendMsg != null && !backendMsg.isBlank()) {
                throw new IOException("HTTP " + status + " - " + backendMsg);
            } else {
                throw new IOException("HTTP " + status + " al crear cliente.");
            }
        }

        // Parsear respuesta JSON a ClienteDTO (el que ya usas para listar)
        try (InputStream is = conn.getInputStream();
             Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {

            Jsonb jsonbResp = JsonbBuilder.create();
            return jsonbResp.fromJson(reader, ClienteDTO.class);
        } finally {
            conn.disconnect();
        }
    }
}
