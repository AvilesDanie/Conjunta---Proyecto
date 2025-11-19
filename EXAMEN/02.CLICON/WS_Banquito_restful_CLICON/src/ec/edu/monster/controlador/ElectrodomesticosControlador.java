package ec.edu.monster.controlador;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ec.edu.monster.modelo.ElectrodomesticoModelo;

import java.io.*;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ElectrodomesticosControlador {

    // 🔴 AJUSTA AL CONTEXTO REAL
    private static final String BASE_URL = "http://localhost:8080/WS_JAVA_REST_Comercializadora/api/electrodomesticos";

    private final Gson gson;

    public ElectrodomesticosControlador() {
        this.gson = new Gson();
    }

    // ===== LISTAR =====

    public List<ElectrodomesticoModelo> listar() throws IOException {
        String json = realizarPeticionJson("GET", "", null, 200);
        Type listType = new TypeToken<List<ElectrodomesticoModelo>>() {}.getType();
        return gson.fromJson(json, listType);
    }

    // ===== CREAR (multipart/form-data) =====

    public ElectrodomesticoModelo crear(
            String codigo,
            String nombre,
            BigDecimal precioVenta,
            File imagen
    ) throws IOException {

        if (imagen == null || !imagen.exists()) {
            throw new IOException("La imagen no existe: " + imagen);
        }

        String boundary = "----Boundary" + System.currentTimeMillis();
        HttpURLConnection conn = (HttpURLConnection) new URL(BASE_URL).openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        DataOutputStream out = null;
        try {
            out = new DataOutputStream(conn.getOutputStream());

            // campo texto: codigo
            escribirCampoTexto(out, boundary, "codigo", codigo);
            // campo texto: nombre
            escribirCampoTexto(out, boundary, "nombre", nombre);
            // campo texto: precioVenta (como string)
            escribirCampoTexto(out, boundary, "precioVenta", precioVenta.toPlainString());
            // campo archivo: imagen
            escribirCampoArchivo(out, boundary, "imagen", imagen);

            // cierre
            out.writeBytes("--" + boundary + "--\r\n");
            out.flush();
        } finally {
            if (out != null) {
                try { out.close(); } catch (IOException e) { /* ignore */ }
            }
        }

        int status = conn.getResponseCode();
        InputStream is = (status >= 200 && status < 400) ? conn.getInputStream() : conn.getErrorStream();
        String respuesta = leerStream(is);
        conn.disconnect();

        if (status != 201) {
            throw new IOException("Error HTTP " + status + ": " + respuesta);
        }

        return gson.fromJson(respuesta, ElectrodomesticoModelo.class);
    }

    // ===== ACTUALIZAR (multipart/form-data) =====

    public ElectrodomesticoModelo actualizar(
            Long id,
            String codigo,
            String nombre,
            BigDecimal precioVenta,
            File imagenOpcional // puede ser null para no cambiar imagen
    ) throws IOException {

        String boundary = "----Boundary" + System.currentTimeMillis();
        HttpURLConnection conn = (HttpURLConnection) new URL(BASE_URL + "/" + id).openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        DataOutputStream out = null;
        try {
            out = new DataOutputStream(conn.getOutputStream());

            escribirCampoTexto(out, boundary, "codigo", codigo);
            escribirCampoTexto(out, boundary, "nombre", nombre);
            escribirCampoTexto(out, boundary, "precioVenta", precioVenta.toPlainString());

            if (imagenOpcional != null && imagenOpcional.exists()) {
                escribirCampoArchivo(out, boundary, "imagen", imagenOpcional);
            }

            out.writeBytes("--" + boundary + "--\r\n");
            out.flush();
        } finally {
            if (out != null) {
                try { out.close(); } catch (IOException e) { /* ignore */ }
            }
        }

        int status = conn.getResponseCode();
        InputStream is = (status >= 200 && status < 400) ? conn.getInputStream() : conn.getErrorStream();
        String respuesta = leerStream(is);
        conn.disconnect();

        if (status != 200) {
            throw new IOException("Error HTTP " + status + ": " + respuesta);
        }

        return gson.fromJson(respuesta, ElectrodomesticoModelo.class);
    }

    // ===== ELIMINAR (DELETE lógico) =====

    public void eliminar(Long id) throws IOException {
        realizarPeticionJson("DELETE", "/" + id, null, 204);
    }

    // ===== Helpers multipart =====

    private void escribirCampoTexto(DataOutputStream out, String boundary, String nombreCampo, String valor)
            throws IOException {
        out.writeBytes("--" + boundary + "\r\n");
        out.writeBytes("Content-Disposition: form-data; name=\"" + nombreCampo + "\"\r\n\r\n");
        out.writeBytes(valor + "\r\n");
    }

    private void escribirCampoArchivo(DataOutputStream out, String boundary, String nombreCampo, File archivo)
            throws IOException {
        String nombreArchivo = archivo.getName();
        String contentType = adivinarContentType(nombreArchivo);

        out.writeBytes("--" + boundary + "\r\n");
        out.writeBytes("Content-Disposition: form-data; name=\"" + nombreCampo +
                "\"; filename=\"" + nombreArchivo + "\"\r\n");
        out.writeBytes("Content-Type: " + contentType + "\r\n\r\n");

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(archivo);
            byte[] buffer = new byte[4096];
            int leido;
            while ((leido = fis.read(buffer)) != -1) {
                out.write(buffer, 0, leido);
            }
        } finally {
            if (fis != null) {
                try { fis.close(); } catch (IOException e) { /* ignore */ }
            }
        }
        out.writeBytes("\r\n");
    }

    private String adivinarContentType(String nombreArchivo) {
        String lower = nombreArchivo.toLowerCase();
        if (lower.endsWith(".png")) return "image/png";
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) return "image/jpeg";
        if (lower.endsWith(".gif")) return "image/gif";
        return "application/octet-stream";
    }

    // ===== Helper JSON simple (GET/DELETE) =====

    private String realizarPeticionJson(
            String metodo,
            String path,
            String body,
            int... codigosEsperados
    ) throws IOException {

        URL url = new URL(BASE_URL + path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(metodo);
        conn.setRequestProperty("Accept", "application/json");

        if (body != null) {
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            OutputStream os = null;
            try {
                os = conn.getOutputStream();
                os.write(body.getBytes(StandardCharsets.UTF_8));
            } finally {
                if (os != null) {
                    try { os.close(); } catch (IOException e) { /* ignore */ }
                }
            }
        }

        int status = conn.getResponseCode();
        InputStream is = (status >= 200 && status < 400) ? conn.getInputStream() : conn.getErrorStream();
        String respuesta = leerStream(is);
        conn.disconnect();

        boolean ok = false;
        if (codigosEsperados != null && codigosEsperados.length > 0) {
            for (int c : codigosEsperados) {
                if (status == c) {
                    ok = true;
                    break;
                }
            }
        } else {
            ok = (status >= 200 && status < 300);
        }

        if (!ok) {
            throw new IOException("Error HTTP " + status + ": " + respuesta);
        }

        return respuesta;
    }

    private String leerStream(InputStream is) throws IOException {
        if (is == null) {
            return "";
        }
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            String linea;
            while ((linea = br.readLine()) != null) {
                sb.append(linea);
            }
            return sb.toString();
        } finally {
            if (br != null) {
                try { br.close(); } catch (IOException e) { /* ignore */ }
            }
        }
    }
}
