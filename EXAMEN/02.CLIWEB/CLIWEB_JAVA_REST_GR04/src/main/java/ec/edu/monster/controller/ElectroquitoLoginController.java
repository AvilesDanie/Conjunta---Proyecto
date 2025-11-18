package ec.edu.monster.controller;

import ec.edu.monster.config.AppConfig;
import ec.edu.monster.dto.UsuarioDTO;
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
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@WebServlet("/electroquito/login")
public class ElectroquitoLoginController extends HttpServlet {

    private static final String BASE_URL = AppConfig.COMERCIALIZADORA_API_BASE;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Si ya hay sesión, lo mando directo al home
        if (request.getSession().getAttribute("usuarioSesion") != null) {
            response.sendRedirect(request.getContextPath() + "/electroquito/home");
            return;
        }

        RequestDispatcher rd = request.getRequestDispatcher(
                "/WEB-INF/views/electroquito/electroquitoLogin.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Validación rápida
        if (username == null || username.isBlank()
                || password == null || password.isBlank()) {

            request.setAttribute("error", "Ingrese usuario y contraseña.");
            request.setAttribute("username", username);
            RequestDispatcher rd = request.getRequestDispatcher(
                    "/WEB-INF/views/electroquito/electroquitoLogin.jsp");
            rd.forward(request, response);
            return;
        }

        try {
            // Llamada al endpoint /usuarios/login
            URL url = new URL(BASE_URL + "/usuarios/login");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            String jsonBody = String.format(
                    "{\"username\":\"%s\",\"password\":\"%s\"}",
                    escapeJson(username),
                    escapeJson(password)
            );

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                os.write(input);
            }

            int status = conn.getResponseCode();

            if (status == HttpURLConnection.HTTP_OK) {
                // Parseo de la respuesta JSON a UsuarioDTO
                try (InputStream is = conn.getInputStream();
                     Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {

                    Jsonb jsonb = JsonbBuilder.create();
                    UsuarioDTO usuario = jsonb.fromJson(reader, UsuarioDTO.class);

                    // Guardo en sesión
                    request.getSession().setAttribute("usuarioSesion", usuario);
                }

                response.sendRedirect(request.getContextPath() + "/electroquito/home");
                return;

            } else if (status == HttpURLConnection.HTTP_UNAUTHORIZED) {

                request.setAttribute("error", "Usuario o contraseña incorrectos.");
                request.setAttribute("username", username);

            } else {
                request.setAttribute("error",
                        "Error al comunicarse con el servidor (" + status + ").");
                request.setAttribute("username", username);
            }

            RequestDispatcher rd = request.getRequestDispatcher(
                    "/WEB-INF/views/electroquito/electroquitoLogin.jsp");
            rd.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error",
                    "No se pudo conectar con el servidor REST de ElectroQuito.");
            request.setAttribute("username", username);

            RequestDispatcher rd = request.getRequestDispatcher(
                    "/WEB-INF/views/electroquito/electroquitoLogin.jsp");
            rd.forward(request, response);
        }
    }

    private String escapeJson(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }
}
