/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.monster.controller;

/**
 *
 * @author danie
 */

import ec.edu.monster.JPAUtil;
import ec.edu.monster.model.CuotaAmortizacion;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

// ==== OpenAPI / Swagger ====
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


@Path("cuotas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Cuotas", description = "Gestión de la tabla de amortización")
public class CuotaAmortizacionController {

    // ====== DTOs ======

    @Schema(name = "CuotaResponse")
    public static class CuotaResponseDTO {
        public Long id;
        public Long idCredito;
        public int numeroCuota;
        public java.math.BigDecimal valorCuota;
        public java.math.BigDecimal interesPagado;
        public java.math.BigDecimal capitalPagado;
        public java.math.BigDecimal saldo;
        public String fechaVencimiento;
        public String estado;
    }

    private CuotaResponseDTO toDTO(CuotaAmortizacion c) {
        CuotaResponseDTO dto = new CuotaResponseDTO();
        dto.id = c.getId();
        dto.idCredito = c.getCredito().getId();
        dto.numeroCuota = c.getNumeroCuota();
        dto.valorCuota = c.getValorCuota();
        dto.interesPagado = c.getInteresPagado();
        dto.capitalPagado = c.getCapitalPagado();
        dto.saldo = c.getSaldo();
        dto.fechaVencimiento = c.getFechaVencimiento() != null
                ? c.getFechaVencimiento().toString()
                : null;
        dto.estado = c.getEstado();
        return dto;
    }

    @Schema(name = "ActualizarCuota")
    public static class ActualizarCuotaDTO {
        public String estado;      // PAGADA, VENCIDA, ANULADA
        public String fechaPago;   // opcional, formato yyyy-MM-dd (si luego agregas este campo en la entidad)
    }

    // ====== Listar cuotas por crédito ======

    @GET
    @Path("credito/{idCredito}")
    @Operation(
            summary = "Lista las cuotas de un crédito",
            description = "Devuelve la tabla de amortización completa para un crédito."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de cuotas",
            content = @Content(schema = @Schema(implementation = CuotaResponseDTO.class))
    )
    public List<CuotaResponseDTO> listarPorCredito(@PathParam("idCredito") Long idCredito) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<CuotaAmortizacion> q = em.createQuery(
                    "SELECT c FROM CuotaAmortizacion c " +
                            "WHERE c.credito.id = :id ORDER BY c.numeroCuota",
                    CuotaAmortizacion.class
            );
            q.setParameter("id", idCredito);
            List<CuotaAmortizacion> cuotas = q.getResultList();

            return cuotas.stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList());
        } finally {
            em.close();
        }
    }

    // ====== Obtener detalle de una cuota ======

    @GET
    @Path("{id}")
    @Operation(
            summary = "Obtiene una cuota por ID",
            description = "Devuelve el detalle de una cuota específica."
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Cuota encontrada",
                content = @Content(schema = @Schema(implementation = CuotaResponseDTO.class))
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Cuota no encontrada"
        )
    })
    public Response obtener(@PathParam("id") Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            CuotaAmortizacion cuota = em.find(CuotaAmortizacion.class, id);
            if (cuota == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(toDTO(cuota)).build();
        } finally {
            em.close();
        }
    }

    // ====== Actualizar estado de una cuota (ej. marcar PAGADA) ======

    @PUT
    @Path("{id}")
    @Operation(
            summary = "Actualiza el estado de una cuota",
            description = "Permite marcar una cuota como PAGADA, VENCIDA o ANULADA."
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Cuota actualizada",
                content = @Content(schema = @Schema(implementation = CuotaResponseDTO.class))
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Cuota no encontrada"
        )
    })
    public Response actualizar(@PathParam("id") Long id, ActualizarCuotaDTO dto) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            CuotaAmortizacion cuota = em.find(CuotaAmortizacion.class, id);
            if (cuota == null) {
                em.getTransaction().rollback();
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            if (dto.estado != null && !dto.estado.isBlank()) {
                cuota.setEstado(dto.estado.toUpperCase());
            }

            // Si luego agregas campo fechaPago en la entidad:
            // if (dto.fechaPago != null && !dto.fechaPago.isBlank()) {
            //     cuota.setFechaPago(LocalDate.parse(dto.fechaPago));
            // }

            em.merge(cuota);
            em.getTransaction().commit();

            return Response.ok(toDTO(cuota)).build();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // ====== "Eliminar" = anular lógicamente la cuota ======

    @DELETE
    @Path("{id}")
    @Operation(
            summary = "Anula una cuota",
            description = "Elimina lógicamente la cuota cambiando su estado a ANULADA."
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "204",
                description = "Cuota anulada"
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Cuota no encontrada"
        )
    })
    public Response anular(@PathParam("id") Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            CuotaAmortizacion cuota = em.find(CuotaAmortizacion.class, id);
            if (cuota == null) {
                em.getTransaction().rollback();
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            cuota.setEstado("ANULADA");
            em.merge(cuota);

            em.getTransaction().commit();
            return Response.noContent().build();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
