package ec.edu.monster.controller;

import ec.edu.monster.config.AppConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@WebServlet("/banquito/creditos/cuotas/anular")
public class BanquitoAnularCuotaController extends HttpServlet {

    private static final String BASE_URL = AppConfig.BANQUITO_API_BASE;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Object usuarioSesion = session != null ? session.getAttribute("usuarioSesion") : null;
        if (usuarioSesion == null) {
            response.sendRedirect(request.getContextPath() + "/banquito/login");
            return;
        }

        String idCredito = request.getParameter("idCredito");
        String idCuota = request.getParameter("idCuota");
        String numeroCuota = request.getParameter("numeroCuota");

        if (idCredito == null || idCredito.isBlank()
                || idCuota == null || idCuota.isBlank()) {
            registrarError(session, "No se pudo identificar la cuota a anular.");
            redirigir(response, request.getContextPath(), idCredito);
            return;
        }

        HttpURLConnection conn = null;
        try {
            URL url = new URL(BASE_URL + "/cuotas/" + idCuota);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            String body = "{\"estado\":\"PENDIENTE\"}";
            try (OutputStream os = conn.getOutputStream()) {
                os.write(body.getBytes(StandardCharsets.UTF_8));
            }

            int status = conn.getResponseCode();
            if (status >= 200 && status < 300) {
                registrarExito(session, "Se anuló el pago de la cuota "
                        + (numeroCuota != null ? "#" + numeroCuota + " " : "") + "correctamente.");
            } else {
                registrarError(session, "No se pudo anular la cuota (HTTP " + status + ").");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            registrarError(session, "Error de comunicación al anular la cuota.");
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        redirigir(response, request.getContextPath(), idCredito);
    }

    private void redirigir(HttpServletResponse response, String contextPath, String idCredito)
            throws IOException {
        String destino = contextPath + "/banquito/creditos/cuotas";
        if (idCredito != null && !idCredito.isBlank()) {
            destino += "?idCredito=" + idCredito;
        }
        response.sendRedirect(destino);
    }

    private void registrarExito(HttpSession session, String mensaje) {
        session.setAttribute(BanquitoCuotasFlash.EXITO, mensaje);
        session.removeAttribute(BanquitoCuotasFlash.ERROR);
    }

    private void registrarError(HttpSession session, String mensaje) {
        session.setAttribute(BanquitoCuotasFlash.ERROR, mensaje);
        session.removeAttribute(BanquitoCuotasFlash.EXITO);
    }
}
