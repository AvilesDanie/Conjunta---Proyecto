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
import ec.edu.monster.model.Cliente;
import ec.edu.monster.model.Credito;
import ec.edu.monster.model.Cuenta;
import ec.edu.monster.model.CuotaAmortizacion;
//import ec.edu.monster.model.Movimiento;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

// ==== OpenAPI / Swagger ====
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


@Path("creditos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Créditos", description = "Operaciones de evaluación y registro de créditos")
public class CreditoController {

    // ====== DTOs ======

    @Schema(name = "SolicitudCredito")
    public static class SolicitudCreditoDTO {
        public String cedula;
        public BigDecimal precioProducto;
        public int plazoMeses;
        public String numCuentaCredito; // cuenta donde se asociará el crédito
    }

    @Schema(name = "ResultadoEvaluacion")
    public static class ResultadoEvaluacionDTO {
        public boolean sujetoCredito;
        public BigDecimal montoMaximo;
        public boolean aprobado;
        public String motivo;
    }
     
    @Schema(name = "CreditoResponse")
    public static class CreditoResponseDTO {
        public Long id;
        public String cedulaCliente;
        public String nombreCliente;
        public BigDecimal monto;
        public int plazoMeses;
        public BigDecimal tasaAnual;
        public String fechaAprobacion;
        public String estado;
        public String numCuentaAsociada;
    }
    
    
    private CreditoResponseDTO toDTO(Credito c) {
        CreditoResponseDTO dto = new CreditoResponseDTO();
        dto.id = c.getId();
        dto.cedulaCliente = c.getCliente().getCedula();
        dto.nombreCliente = c.getCliente().getNombre();
        dto.monto = c.getMonto();
        dto.plazoMeses = c.getPlazoMeses();
        dto.tasaAnual = c.getTasaAnual();
        dto.fechaAprobacion = c.getFechaAprobacion() != null
                ? c.getFechaAprobacion().toString()
                : null;
        dto.estado = c.getEstado();
        dto.numCuentaAsociada = c.getCuentaAsociada() != null
                ? c.getCuentaAsociada().getNumCuenta()
                : null;
        return dto;
    }

    // ====== ENDPOINT: Evaluar crédito ======

    @POST
    @Path("evaluar")
    @Operation(
            summary = "Evalúa si un cliente es sujeto de crédito",
            description = "Aplica reglas y calcula monto máximo."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Resultado de la evaluación",
            content = @Content(schema = @Schema(implementation = ResultadoEvaluacionDTO.class))
    )
    public Response evaluar(SolicitudCreditoDTO solicitud) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            ResultadoEvaluacionDTO res = evaluarInterno(em, solicitud);
            return Response.ok(res).build();
        } finally {
            em.close();
        }
    }

    // ====== ENDPOINT: Crear crédito (si se aprueba) ======

    @POST
    @Operation(
            summary = "Crea un crédito y genera la tabla de amortización",
            description = "Primero evalúa; si se aprueba, crea el crédito y sus cuotas."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Crédito creado",
                    content = @Content(schema = @Schema(implementation = CreditoResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Crédito no aprobado o datos inválidos",
                    content = @Content(schema = @Schema(implementation = ResultadoEvaluacionDTO.class))
            )
    })
    public Response crearCredito(SolicitudCreditoDTO solicitud) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            ResultadoEvaluacionDTO evaluacion = evaluarInterno(em, solicitud);
            if (!evaluacion.aprobado) {
                em.getTransaction().rollback();
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(evaluacion)
                        .build();
            }

            Cliente cliente = em.find(Cliente.class, solicitud.cedula);
            if (cliente == null) {
                em.getTransaction().rollback();
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Cliente no existe").build();
            }

            if (solicitud.numCuentaCredito == null) {
                em.getTransaction().rollback();
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Debe indicar numCuentaCredito").build();
            }

            Cuenta cuenta = em.find(Cuenta.class, solicitud.numCuentaCredito);
            if (cuenta == null || !cuenta.getCliente().getCedula().equals(cliente.getCedula())) {
                em.getTransaction().rollback();
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("La cuenta asociada no pertenece al cliente").build();
            }

            Credito credito = new Credito();
            credito.setCliente(cliente);
            credito.setCuentaAsociada(cuenta);
            credito.setMonto(solicitud.precioProducto);
            credito.setPlazoMeses(solicitud.plazoMeses);
            credito.setTasaAnual(new BigDecimal("16.00"));
            credito.setFechaAprobacion(LocalDate.now());
            credito.setEstado("APROBADO");

            em.persist(credito);

            generarAmortizacion(em, credito);

            em.getTransaction().commit();
            return Response.status(Response.Status.CREATED)
                    .entity(toDTO(credito))
                    .build();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @GET
    @Path("{id}")
    @Operation(
            summary = "Obtiene un crédito por ID",
            description = "Devuelve datos generales del crédito."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Crédito encontrado",
                    content = @Content(schema = @Schema(implementation = CreditoResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Crédito no encontrado"
            )
    })
    public Response obtener(@PathParam("id") Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Credito c = em.find(Credito.class, id);
            if (c == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"Crédito no encontrado\"}")
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }
            return Response.ok(toDTO(c)).build();
        } finally {
            em.close();
        }
    }

    // ========================================================================
    // ====================     MÉTODOS PRIVADOS      =========================
    // ========================================================================

    private ResultadoEvaluacionDTO evaluarInterno(EntityManager em, SolicitudCreditoDTO solicitud) {
        ResultadoEvaluacionDTO res = new ResultadoEvaluacionDTO();
        res.sujetoCredito = false;
        res.aprobado = false;
        res.montoMaximo = BigDecimal.ZERO;

        LocalDate hoy = LocalDate.now();

        Cliente cliente = em.find(Cliente.class, solicitud.cedula);
        if (cliente == null) {
            res.motivo = "El solicitante no es cliente del banco (no tiene cuenta).";
            return res;
        }

        // 1) Verificar que tenga al menos un INGRESO (no interno) en el último mes
        LocalDate haceUnMes = hoy.minusMonths(1);
        TypedQuery<Long> qIng = em.createQuery(
                "SELECT COUNT(m) FROM Movimiento m " +
                        "WHERE m.cuenta.cliente.cedula = :cedula " +
                        "AND m.naturaleza = 'INGRESO' " +
                        "AND m.internoTransferencia = false " +
                        "AND m.fecha >= :desde",
                Long.class
        );
        qIng.setParameter("cedula", solicitud.cedula);
        qIng.setParameter("desde", haceUnMes);
        Long cantIngresos = qIng.getSingleResult();

        if (cantIngresos == 0) {
            res.motivo = "El cliente no tiene ingresos en el último mes.";
            return res;
        }

        // 2) Reglas de edad si CASADO
        if (cliente.getEstadoCivil() != null &&
                cliente.getEstadoCivil().equalsIgnoreCase("CASADO")) {
            if (cliente.getFechaNacimiento() == null) {
                res.motivo = "No se puede calcular la edad del cliente.";
                return res;
            }
            int edad = Period.between(cliente.getFechaNacimiento(), hoy).getYears();
            if (edad < 25) {
                res.motivo = "Cliente casado menor de 25 años.";
                return res;
            }
        }

        // 3) Sin crédito activo
        TypedQuery<Long> qCreditos = em.createQuery(
                "SELECT COUNT(c) FROM Credito c " +
                        "WHERE c.cliente.cedula = :cedula " +
                        "AND c.estado IN ('APROBADO','ACTIVO')",
                Long.class
        );
        qCreditos.setParameter("cedula", solicitud.cedula);
        Long cantCreditos = qCreditos.getSingleResult();
        if (cantCreditos > 0) {
            res.motivo = "El cliente ya tiene un crédito activo.";
            return res;
        }

        // 4) Promedios últimos 3 meses (INGRESO / EGRESO), excluyendo transferencias internas
        LocalDate haceTresMeses = hoy.minusMonths(3);

        BigDecimal promIngresos = obtenerPromedioMovimientos(
                em, solicitud.cedula, "INGRESO", haceTresMeses);
        BigDecimal promEgresos = obtenerPromedioMovimientos(
                em, solicitud.cedula, "EGRESO", haceTresMeses);

        BigDecimal diferencia = promIngresos.subtract(promEgresos);
        if (diferencia.compareTo(BigDecimal.ZERO) <= 0) {
            res.motivo = "La capacidad de ahorro es insuficiente.";
            return res;
        }

        BigDecimal sesenta = new BigDecimal("0.60");
        BigDecimal nueve = new BigDecimal("9");
        BigDecimal montoMax = diferencia
                .multiply(sesenta)
                .multiply(nueve)
                .setScale(2, RoundingMode.HALF_UP);

        res.montoMaximo = montoMax;
        res.sujetoCredito = true;

        if (solicitud.precioProducto == null ||
                solicitud.precioProducto.compareTo(BigDecimal.ZERO) <= 0) {
            res.aprobado = false;
            res.motivo = "Precio del producto inválido.";
            return res;
        }

        if (solicitud.precioProducto.compareTo(montoMax) <= 0) {
            res.aprobado = true;
            res.motivo = "Crédito aprobado.";
        } else {
            res.aprobado = false;
            res.motivo = "El precio del electrodoméstico excede el monto máximo aprobado.";
        }

        return res;
    }

    private BigDecimal obtenerPromedioMovimientos(EntityManager em,
                                                  String cedula,
                                                  String naturaleza,
                                                  LocalDate desde) {
        TypedQuery<Double> q = em.createQuery(
                "SELECT AVG(m.valor) FROM Movimiento m " +
                        "WHERE m.cuenta.cliente.cedula = :cedula " +
                        "AND m.naturaleza = :naturaleza " +
                        "AND m.internoTransferencia = false " +
                        "AND m.fecha >= :desde",
                Double.class
        );
        q.setParameter("cedula", cedula);
        q.setParameter("naturaleza", naturaleza);
        q.setParameter("desde", desde);

        Double promedio = q.getSingleResult();
        if (promedio == null) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(promedio).setScale(2, RoundingMode.HALF_UP);
    }

    // ====== Amortización ======

    private void generarAmortizacion(EntityManager em, Credito credito) {
        BigDecimal monto = credito.getMonto();
        int n = credito.getPlazoMeses();

        BigDecimal tasaAnual = credito.getTasaAnual(); // 16.00
        BigDecimal cien = new BigDecimal("100");
        BigDecimal doce = new BigDecimal("12");

        BigDecimal tasaPeriodo = tasaAnual
                .divide(cien, 10, RoundingMode.HALF_UP)
                .divide(doce, 10, RoundingMode.HALF_UP);

        double i = tasaPeriodo.doubleValue();
        double factor = i / (1 - Math.pow(1 + i, -n));
        BigDecimal cuota = monto
                .multiply(BigDecimal.valueOf(factor))
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal saldo = monto;
        LocalDate fechaBase = credito.getFechaAprobacion() != null
                ? credito.getFechaAprobacion()
                : LocalDate.now();

        for (int k = 1; k <= n; k++) {
            BigDecimal interes = saldo.multiply(tasaPeriodo)
                    .setScale(2, RoundingMode.HALF_UP);
            BigDecimal capital = cuota.subtract(interes)
                    .setScale(2, RoundingMode.HALF_UP);
            BigDecimal nuevoSaldo = saldo.subtract(capital)
                    .setScale(2, RoundingMode.HALF_UP);

            if (k == n && nuevoSaldo.compareTo(BigDecimal.ZERO) != 0) {
                capital = saldo;
                cuota = capital.add(interes).setScale(2, RoundingMode.HALF_UP);
                nuevoSaldo = BigDecimal.ZERO;
            }

            CuotaAmortizacion c = new CuotaAmortizacion();
            c.setCredito(credito);
            c.setNumeroCuota(k);
            c.setValorCuota(cuota);
            c.setInteresPagado(interes);
            c.setCapitalPagado(capital);
            c.setSaldo(nuevoSaldo);
            c.setFechaVencimiento(fechaBase.plusMonths(k));
            c.setEstado("PENDIENTE");

            em.persist(c);
            saldo = nuevoSaldo;
        }
    }
}
