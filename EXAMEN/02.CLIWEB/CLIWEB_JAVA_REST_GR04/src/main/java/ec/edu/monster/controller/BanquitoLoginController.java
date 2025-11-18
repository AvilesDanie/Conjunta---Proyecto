package ec.edu.monster.controller;

import ec.edu.monster.config.AppConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/banquito/login")
public class BanquitoLoginController extends HttpServlet {

    private static final String LOGIN_API_URL =
            AppConfig.BANQUITO_API_BASE + "/usuarios/login";

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        RequestDispatcher rd = request.getRequestDispatcher(
                "/WEB-INF/views/banquito/banquitoLogin.jsp"
        );
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || username.isBlank()
                || password == null || password.isBlank()) {

            request.setAttribute("error", "Debe ingresar usuario y contraseña.");
            doGet(request, response);
            return;
        }

        try {
            URL url = new URL(LOGIN_API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");

            String jsonBody = String.format(
                    "{\"username\":\"%s\",\"password\":\"%s\"}",
                    escapeJson(username),
                    escapeJson(password)
            );

            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonBody.getBytes(StandardCharsets.UTF_8));
            }

            int status = conn.getResponseCode();
            System.out.println("[BanquitoLogin] HTTP status = " + status);

            if (status == HttpURLConnection.HTTP_OK) {
                String body = readBody(conn.getInputStream());
                System.out.println("[BanquitoLogin] Respuesta OK = " + body);

                HttpSession session = request.getSession(true);
                session.setAttribute("usuarioSesion", username);
                session.setAttribute("usuarioJson", body);

                response.sendRedirect(request.getContextPath() + "/banquito/home");
                return;

            } else if (status == HttpURLConnection.HTTP_UNAUTHORIZED) {

                request.setAttribute("error", "Usuario o contraseña incorrectos.");
                doGet(request, response);
                return;

            } else {

                String errorBody = null;
                try {
                    InputStream es = conn.getErrorStream();
                    if (es != null) errorBody = readBody(es);
                } catch (Exception ignored) { }

                System.out.println("[BanquitoLogin] Error HTTP " + status +
                        " body = " + errorBody);

                request.setAttribute("error",
                        "Error al autenticar (HTTP " + status + ").");
                doGet(request, response);
                return;
            }

        } catch (IOException ex) {
            ex.printStackTrace();
            request.setAttribute("error",
                    "No se pudo conectar con el servidor BanQuito.");
            doGet(request, response);
        }
    }

    private String readBody(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();
    }

    private String escapeJson(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }
}
