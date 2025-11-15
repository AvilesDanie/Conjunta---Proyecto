/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.monster.integracion;

/**
 *
 * @author danie
 */

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.math.BigDecimal;

// DTOs que imitan los del módulo de crédito
public class BanquitoClient {

    private static final String BASE_URL =
            "http://localhost:8080/WS_JAVA_REST_BanQuito/api";

    private final Client client = ClientBuilder.newClient();

    public static class SolicitudCreditoDTO {
        public String cedula;
        public BigDecimal precioProducto;
        public int plazoMeses;
        public String numCuentaCredito;
    }

    public static class ResultadoEvaluacionDTO {
        public boolean sujetoCredito;
        public BigDecimal montoMaximo;
        public boolean aprobado;
        public String motivo;
    }

    public static class CreditoResponseDTO {
        public Long id;
        public String cedulaCliente;
        public BigDecimal monto;
        public int plazoMeses;
        public BigDecimal tasaAnual;
        public String fechaAprobacion;
        public String estado;
        public String numCuentaAsociada;
    }

    public ResultadoEvaluacionDTO evaluarCredito(SolicitudCreditoDTO solicitud) {
        Response r = client
                .target(BASE_URL)
                .path("creditos/evaluar")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(solicitud));

        if (r.getStatus() != 200) {
            throw new RuntimeException("Error al evaluar crédito en BanQuito: HTTP " + r.getStatus());
        }
        return r.readEntity(ResultadoEvaluacionDTO.class);
    }

    public CreditoResponseDTO crearCredito(SolicitudCreditoDTO solicitud) {
        Response r = client
                .target(BASE_URL)
                .path("creditos")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(solicitud));

        if (r.getStatus() != 201) {
            String body = r.readEntity(String.class);
            throw new RuntimeException("Crédito no aprobado en BanQuito: HTTP " + r.getStatus() + " - " + body);
        }
        return r.readEntity(CreditoResponseDTO.class);
    }
}

