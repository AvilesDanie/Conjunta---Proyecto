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
import ec.edu.monster.model.Cuenta;
import ec.edu.monster.model.Movimiento;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

// Swagger
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Path("movimientos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Movimientos", description = "Registro de depósitos y retiros")
public class MovimientoController {

    // ====== DTOs ======
    @Schema(name = "MovimientoRequest")
    public static class MovimientoRequestDTO {

        public String tipo;              // DEP, RET, TRA
        public BigDecimal valor;
        public String fecha;             // opcional yyyy-MM-dd

        // Para DEP / RET
        public String numCuenta;

        // Para TRA
        public String numCuentaOrigen;
        public String numCuentaDestino;
    }

    @Schema(name = "MovimientoResponse")
    public static class MovimientoResponseDTO {

        public Long id;
        public String numCuenta;
        public String tipo;
        public String naturaleza;
        public boolean internoTransferencia;
        public BigDecimal valor;
        public String fecha;
        public BigDecimal saldoCuenta;
    }

    private MovimientoResponseDTO toDTO(Movimiento m) {
        MovimientoResponseDTO dto = new MovimientoResponseDTO();
        dto.id = m.getId();
        dto.numCuenta = m.getCuenta().getNumCuenta();
        dto.tipo = m.getTipo();
        dto.naturaleza = m.getNaturaleza();
        dto.internoTransferencia = m.isInternoTransferencia();
        dto.valor = m.getValor();
        dto.fecha = m.getFecha() != null ? m.getFecha().toString() : null;
        dto.saldoCuenta = m.getCuenta().getSaldo();
        return dto;
    }

    // ====== Listar movimientos por cuenta ======
    @GET
    @Path("cuenta/{numCuenta}")
    @Operation(
            summary = "Lista movimientos de una cuenta",
            description = "Devuelve todos los movimientos asociados a una cuenta."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de movimientos",
            content = @Content(schema = @Schema(implementation = MovimientoResponseDTO.class))
    )
    public List<MovimientoResponseDTO> listarPorCuenta(@PathParam("numCuenta") String numCuenta) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Movimiento> q = em.createQuery(
                    "SELECT m FROM Movimiento m WHERE m.cuenta.numCuenta = :num ORDER BY m.fecha",
                    Movimiento.class
            );
            q.setParameter("num", numCuenta);
            List<Movimiento> movs = q.getResultList();
            return movs.stream().map(this::toDTO).collect(Collectors.toList());
        } finally {
            em.close();
        }
    }

    // ====== Registrar movimiento (DEP / RET / TRA) ======
    @POST
    @Operation(
            summary = "Registra un movimiento",
            description = "Crea un movimiento de depósito (DEP) o retiro (RET) y actualiza el saldo."
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "201",
                description = "Movimiento registrado",
                content = @Content(schema = @Schema(implementation = MovimientoResponseDTO.class))
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Datos inválidos"
        )
    })
    public Response crear(MovimientoRequestDTO req) {
        if (req == null || req.tipo == null || req.valor == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("tipo y valor son obligatorios").build();
        }

        String tipo = req.tipo.toUpperCase();
        if (!tipo.equals("DEP") && !tipo.equals("RET") && !tipo.equals("TRA")) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("tipo debe ser DEP, RET o TRA").build();
        }

        if (req.valor.compareTo(BigDecimal.ZERO) <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("valor debe ser mayor a cero").build();
        }

        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            LocalDate fecha = (req.fecha != null && !req.fecha.isBlank())
                    ? LocalDate.parse(req.fecha)
                    : LocalDate.now();

            if (tipo.equals("DEP") || tipo.equals("RET")) {
                if (req.numCuenta == null) {
                    em.getTransaction().rollback();
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity("numCuenta es obligatorio para DEP y RET").build();
                }

                Cuenta cuenta = em.find(Cuenta.class, req.numCuenta);
                if (cuenta == null) {
                    em.getTransaction().rollback();
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity("Cuenta no existe").build();
                }

                BigDecimal saldoActual = cuenta.getSaldo();
                BigDecimal nuevoSaldo;

                String naturaleza;
                if (tipo.equals("DEP")) {
                    naturaleza = "INGRESO";
                    nuevoSaldo = saldoActual.add(req.valor);
                } else { // RET
                    naturaleza = "EGRESO";
                    if (saldoActual.compareTo(req.valor) < 0) {
                        em.getTransaction().rollback();
                        return Response.status(Response.Status.BAD_REQUEST)
                                .entity("Saldo insuficiente para el retiro").build();
                    }
                    nuevoSaldo = saldoActual.subtract(req.valor);
                }

                Movimiento m = new Movimiento();
                m.setCuenta(cuenta);
                m.setTipo(tipo);
                m.setNaturaleza(naturaleza);
                m.setInternoTransferencia(false);
                m.setValor(req.valor);
                m.setFecha(fecha);

                cuenta.setSaldo(nuevoSaldo);

                em.persist(m);
                em.merge(cuenta);

                em.getTransaction().commit();
                return Response.status(Response.Status.CREATED).entity(toDTO(m)).build();

            } else { // TRA
                if (req.numCuentaOrigen == null || req.numCuentaDestino == null) {
                    em.getTransaction().rollback();
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity("numCuentaOrigen y numCuentaDestino son obligatorios para TRA").build();
                }

                Cuenta origen = em.find(Cuenta.class, req.numCuentaOrigen);
                Cuenta destino = em.find(Cuenta.class, req.numCuentaDestino);

                if (origen == null || destino == null) {
                    em.getTransaction().rollback();
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity("Cuentas de origen o destino no existen").build();
                }

                BigDecimal saldoOrigen = origen.getSaldo();
                if (saldoOrigen.compareTo(req.valor) < 0) {
                    em.getTransaction().rollback();
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity("Saldo insuficiente en cuenta origen").build();
                }

                boolean esInternaMismoCliente
                        = origen.getCliente().getCedula().equals(destino.getCliente().getCedula());

                // Origen: EGRESO
                Movimiento movOrigen = new Movimiento();
                movOrigen.setCuenta(origen);
                movOrigen.setTipo("TRA");
                movOrigen.setNaturaleza("EGRESO");
                movOrigen.setInternoTransferencia(esInternaMismoCliente);
                movOrigen.setValor(req.valor);
                movOrigen.setFecha(fecha);

                // Destino: INGRESO
                Movimiento movDestino = new Movimiento();
                movDestino.setCuenta(destino);
                movDestino.setTipo("TRA");
                movDestino.setNaturaleza("INGRESO");
                movDestino.setInternoTransferencia(esInternaMismoCliente);
                movDestino.setValor(req.valor);
                movDestino.setFecha(fecha);

                origen.setSaldo(saldoOrigen.subtract(req.valor));
                destino.setSaldo(destino.getSaldo().add(req.valor));

                em.persist(movOrigen);
                em.persist(movDestino);
                em.merge(origen);
                em.merge(destino);

                em.getTransaction().commit();

                // Devuelvo el movimiento de la cuenta origen como principal
                return Response.status(Response.Status.CREATED)
                        .entity(toDTO(movOrigen))
                        .build();
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

}
