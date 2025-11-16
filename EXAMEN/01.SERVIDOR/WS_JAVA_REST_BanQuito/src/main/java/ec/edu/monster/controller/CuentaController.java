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
import ec.edu.monster.model.Cuenta;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

// Swagger
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.security.SecureRandom;

@Path("cuentas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Cuentas", description = "Gestión de cuentas bancarias")
public class CuentaController {

    // ====== DTOs ======

    @Schema(name = "CuentaRequest")
    public static class CuentaRequestDTO {
        public String cedulaCliente;
        public String tipoCuenta;
        public BigDecimal saldoInicial;
    }

    @Schema(name = "CuentaResponse")
    public static class CuentaResponseDTO {
        public String numCuenta;
        public String cedulaCliente;
        public String nombreCliente;
        public String tipoCuenta;
        public BigDecimal saldo;
    }

    private CuentaResponseDTO toDTO(Cuenta c) {
        CuentaResponseDTO dto = new CuentaResponseDTO();
        dto.numCuenta = c.getNumCuenta();
        dto.cedulaCliente = c.getCliente().getCedula();
        dto.nombreCliente = c.getCliente().getNombre();
        dto.tipoCuenta = c.getTipoCuenta();
        dto.saldo = c.getSaldo();
        return dto;
    }

    // ====== Listar todas ======

    @GET
    @Operation(
            summary = "Lista todas las cuentas",
            description = "Devuelve todas las cuentas bancarias."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de cuentas",
            content = @Content(schema = @Schema(implementation = CuentaResponseDTO.class))
    )
    public List<CuentaResponseDTO> listar() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            List<Cuenta> cuentas = em.createQuery("SELECT c FROM Cuenta c", Cuenta.class)
                    .getResultList();
            return cuentas.stream().map(this::toDTO).collect(Collectors.toList());
        } finally {
            em.close();
        }
    }

    // ====== Obtener por número de cuenta ======

    @GET
    @Path("{numCuenta}")
    @Operation(
            summary = "Obtiene una cuenta por número",
            description = "Devuelve los datos de la cuenta."
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Cuenta encontrada",
                content = @Content(schema = @Schema(implementation = CuentaResponseDTO.class))
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Cuenta no encontrada"
        )
    })
    public Response obtener(@PathParam("numCuenta") String numCuenta) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Cuenta c = em.find(Cuenta.class, numCuenta);
            if (c == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"Cuenta no encontrada\"}")
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }
            return Response.ok(toDTO(c)).build();
        } finally {
            em.close();
        }
    }

    // ====== Listar cuentas por cliente ======

    @GET
    @Path("cliente/{cedula}")
    @Operation(
            summary = "Lista cuentas de un cliente",
            description = "Devuelve todas las cuentas asociadas a una cédula de cliente."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de cuentas",
            content = @Content(schema = @Schema(implementation = CuentaResponseDTO.class))
    )
    public List<CuentaResponseDTO> listarPorCliente(@PathParam("cedula") String cedula) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Cuenta> q = em.createQuery(
                    "SELECT c FROM Cuenta c WHERE c.cliente.cedula = :cedula",
                    Cuenta.class
            );
            q.setParameter("cedula", cedula);
            List<Cuenta> cuentas = q.getResultList();
            return cuentas.stream().map(this::toDTO).collect(Collectors.toList());
        } finally {
            em.close();
        }
    }

    // ====== Crear cuenta ======

    @POST
    @Operation(
            summary = "Crea una cuenta",
            description = "Registra una nueva cuenta asociada a un cliente."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Cuenta creada",
            content = @Content(schema = @Schema(implementation = CuentaResponseDTO.class))
    )
    public Response crear(CuentaRequestDTO req) {
        if (req == null || req.cedulaCliente == null || req.tipoCuenta == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("cedulaCliente y tipoCuenta son obligatorios").build();
        }

        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            
            Cliente cliente = em.find(Cliente.class, req.cedulaCliente);
            if (cliente == null) {
                em.getTransaction().rollback();
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Cliente no existe").build();
            }
            
            String numCuentaGenerado = generarNumeroCuenta(em);

            Cuenta c = new Cuenta();
            c.setNumCuenta(numCuentaGenerado);
            c.setCliente(cliente);
            c.setTipoCuenta(req.tipoCuenta.toUpperCase());
            BigDecimal saldoInicial = req.saldoInicial != null ? req.saldoInicial : BigDecimal.ZERO;
            c.setSaldo(saldoInicial);

            em.persist(c);
            em.getTransaction().commit();

            return Response.status(Response.Status.CREATED).entity(toDTO(c)).build();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
    
    // ====== Generador de número de cuenta ======
    // Genera un número de 10 dígitos y verifica que no exista en BD.
    private String generarNumeroCuenta(EntityManager em) {
        SecureRandom random = new SecureRandom();
        String numCuenta;
        int intentos = 0;

        do {
            intentos++;
            // 10 dígitos, primera cifra no cero
            StringBuilder sb = new StringBuilder();
            sb.append(random.nextInt(9) + 1); // 1-9
            for (int i = 0; i < 9; i++) {
                sb.append(random.nextInt(10)); // 0-9
            }
            numCuenta = sb.toString();

            // Si existe, repetimos
        } while (em.find(Cuenta.class, numCuenta) != null && intentos < 10);

        // En la práctica, el choque es extremadamente raro.
        return numCuenta;
    }

}
