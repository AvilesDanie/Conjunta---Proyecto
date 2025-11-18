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
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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
import java.security.SecureRandom;

@Path("clientes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Clientes", description = "Gestión de clientes del Banco BanQuito")
public class ClienteController {

    // ====== DTOs ======

    @Schema(name = "ClienteRequest")
    public static class ClienteRequestDTO {
        public String cedula;
        public String nombre;
        public String fechaNacimiento;   // yyyy-MM-dd
        public String estadoCivil;
        public String tipoCuentaInicial; // nueva
        public java.math.BigDecimal saldoInicial; // saldo inicial de la cuenta
    }
    
    @Schema(name = "ClienteRequest")
    public static class ClienteUpdateRequestDTO {
        public String nombre;
        public String estadoCivil;
    }

    @Schema(name = "ClienteResponse")
    public static class ClienteResponseDTO {
        public String cedula;
        public String nombre;
        public String fechaNacimiento;
        public String estadoCivil;
        public String numCuentaInicial;
        public String tipoCuentaInicial;
    }
    
    @Schema(name = "ClienteOnlyResponse")
    public static class ClienteOnlyResponseDTO {
        public String cedula;
        public String nombre;
        public String fechaNacimiento;
        public String estadoCivil;
    }

    private ClienteResponseDTO toDTO(Cliente c, Cuenta cuentaInicial) {
        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.cedula = c.getCedula();
        dto.nombre = c.getNombre();
        dto.estadoCivil = c.getEstadoCivil();
        dto.fechaNacimiento = c.getFechaNacimiento() != null
                ? c.getFechaNacimiento().toString()
                : null;
        if (cuentaInicial != null) {
            dto.numCuentaInicial = cuentaInicial.getNumCuenta();
            dto.tipoCuentaInicial = cuentaInicial.getTipoCuenta();
        }
        return dto;
    }
    
    private ClienteOnlyResponseDTO toDTOCienteOnly(Cliente c) {
        ClienteOnlyResponseDTO dto = new ClienteOnlyResponseDTO();
        dto.cedula = c.getCedula();
        dto.nombre = c.getNombre();
        dto.estadoCivil = c.getEstadoCivil();
        dto.fechaNacimiento = c.getFechaNacimiento() != null
                ? c.getFechaNacimiento().toString()
                : null;
        
        return dto;
    }

    // ====== Listar todos ======

    @GET
    @Operation(
            summary = "Lista todos los clientes",
            description = "Devuelve todos los clientes registrados."
    )
    public List<ClienteResponseDTO> listar() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            List<Cliente> clientes = em.createQuery("SELECT c FROM Cliente c", Cliente.class)
                    .getResultList();
            return clientes.stream().map(c -> {
                // buscar una cuenta cualquiera como inicial (si quieres la primera)
                Cuenta cuenta = em.createQuery(
                                "SELECT cu FROM Cuenta cu WHERE cu.cliente.cedula = :cedula",
                                Cuenta.class)
                        .setParameter("cedula", c.getCedula())
                        .setMaxResults(1)
                        .getResultList()
                        .stream()
                        .findFirst()
                        .orElse(null);
                return toDTO(c, cuenta);
            }).collect(Collectors.toList());
        } finally {
            em.close();
        }
    }

    // ====== Obtener por cédula ======

    @GET
    @Path("{cedula}")
    @Operation(
            summary = "Obtiene un cliente por cédula",
            description = "Devuelve los datos del cliente."
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Cliente encontrado",
                content = @Content(schema = @Schema(implementation = ClienteResponseDTO.class))
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Cliente no encontrado"
        )
    })
    public Response obtener(@PathParam("cedula") String cedula) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Cliente c = em.find(Cliente.class, cedula);
            if (c == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"Cliente no encontrado\"}")
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }
            return Response.ok(toDTOCienteOnly(c)).build();
        } finally {
            em.close();
        }
    }

    // ====== Crear cliente ======

    @POST
    @Operation(
            summary = "Crea un cliente",
            description = "Registra un nuevo cliente con cuenta inical."
    )
    public Response crear(ClienteRequestDTO req) {
        if (req == null || req.cedula == null || req.nombre == null
                || req.tipoCuentaInicial == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"cedula, nombre y tipoCuentaInicial son obligatorios\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
        
        if (req.cedula.length() != 10) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"La cédula debe tener 10 dígitos\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            if (em.find(Cliente.class, req.cedula) != null) {
                em.getTransaction().rollback();
                return Response.status(Response.Status.CONFLICT)
                        .entity("{\"error\":\"Ya existe un cliente con esa cédula\"}")
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            Cliente c = new Cliente();
            c.setCedula(req.cedula);
            c.setNombre(req.nombre);
            if (req.fechaNacimiento != null && !req.fechaNacimiento.isBlank()) {
                c.setFechaNacimiento(LocalDate.parse(req.fechaNacimiento));
            }
            c.setEstadoCivil(req.estadoCivil);

            em.persist(c);

            String numCuentaGenerado = generarNumeroCuenta(em);
            
            Cuenta cuenta = new Cuenta();
            cuenta.setNumCuenta(numCuentaGenerado);
            cuenta.setCliente(c);
            cuenta.setTipoCuenta(req.tipoCuentaInicial.toUpperCase());
            // Usar saldoInicial del request, o 0 si no viene
            cuenta.setSaldo(req.saldoInicial != null ? req.saldoInicial : java.math.BigDecimal.ZERO);

            em.persist(cuenta);

            em.getTransaction().commit();

            return Response.status(Response.Status.CREATED)
                    .entity(toDTO(c, cuenta))
                    .build();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }


    // ====== Actualizar cliente ======

    @PUT
    @Path("{cedula}")
    @Operation(
            summary = "Actualiza un cliente",
            description = "Permite modificar nombre, fechaNacimiento y estadoCivil."
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Cliente actualizado",
                content = @Content(schema = @Schema(implementation = ClienteResponseDTO.class))
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Cliente no encontrado"
        )
    })
    public Response actualizar(@PathParam("cedula") String cedula, ClienteUpdateRequestDTO req) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Cliente c = em.find(Cliente.class, cedula);
            if (c == null) {
                em.getTransaction().rollback();
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"Cliente no encontrado\"}")
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            if (req.nombre != null) c.setNombre(req.nombre);
            
            if (req.estadoCivil != null) c.setEstadoCivil(req.estadoCivil);

            em.merge(c);
            em.getTransaction().commit();
            return Response.ok(toDTOCienteOnly(c)).build();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // ====== Eliminar cliente ======

    @DELETE
    @Path("{cedula}")
    @Operation(
            summary = "Elimina un cliente",
            description = "Elimina físicamente un cliente. (Cuidado con FK en cuentas/creditos)"
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "204",
                description = "Cliente eliminado"
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Cliente no encontrado"
        )
    })
    public Response eliminar(@PathParam("cedula") String cedula) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Cliente c = em.find(Cliente.class, cedula);
            if (c == null) {
                em.getTransaction().rollback();
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"Cliente no encontrado\"}")
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }
            em.remove(c);
            em.getTransaction().commit();
            return Response.noContent().build();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
    
// ====== Generador de número de cuenta ======
    
    private String generarNumeroCuenta(EntityManager em) {
        SecureRandom random = new SecureRandom();
        String numCuenta;
        int intentos = 0;

        do {
            intentos++;
            StringBuilder sb = new StringBuilder();
            sb.append(random.nextInt(9) + 1); // 1-9
            for (int i = 0; i < 9; i++) {
                sb.append(random.nextInt(10)); // 0-9
            }
            numCuenta = sb.toString();
        } while (em.find(Cuenta.class, numCuenta) != null && intentos < 10);

        return numCuenta;
    }
    
}
