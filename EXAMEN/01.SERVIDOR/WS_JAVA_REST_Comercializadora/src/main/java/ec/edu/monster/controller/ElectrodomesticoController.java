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
import ec.edu.monster.model.Electrodomestico;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Path("electrodomesticos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ElectrodomesticoController {

    public static class ElectroRequestDTO {
        public String codigo;
        public String nombre;
        public BigDecimal precioVenta;
    }

    public static class ElectroResponseDTO {
        public Long id;
        public String codigo;
        public String nombre;
        public BigDecimal precioVenta;
    }

    private ElectroResponseDTO toDTO(Electrodomestico e) {
        ElectroResponseDTO dto = new ElectroResponseDTO();
        dto.id = e.getId();
        dto.codigo = e.getCodigo();
        dto.nombre = e.getNombre();
        dto.precioVenta = e.getPrecioVenta();
        return dto;
    }

    @GET
    public List<ElectroResponseDTO> listar() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            List<Electrodomestico> lista = em
                    .createQuery("SELECT e FROM Electrodomestico e", Electrodomestico.class)
                    .getResultList();
            return lista.stream().map(this::toDTO).collect(Collectors.toList());
        } finally {
            em.close();
        }
    }

    @POST
    public Response crear(ElectroRequestDTO req) {
        if (req == null || req.codigo == null || req.nombre == null || req.precioVenta == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("codigo, nombre y precioVenta son obligatorios").build();
        }

        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            Long count = em.createQuery(
                            "SELECT COUNT(e) FROM Electrodomestico e WHERE e.codigo = :cod",
                            Long.class)
                    .setParameter("cod", req.codigo)
                    .getSingleResult();
            if (count > 0) {
                em.getTransaction().rollback();
                return Response.status(Response.Status.CONFLICT)
                        .entity("Ya existe un electrodoméstico con ese código").build();
            }

            Electrodomestico e = new Electrodomestico();
            e.setCodigo(req.codigo);
            e.setNombre(req.nombre);
            e.setPrecioVenta(req.precioVenta);

            em.persist(e);
            em.getTransaction().commit();

            return Response.status(Response.Status.CREATED).entity(toDTO(e)).build();
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw ex;
        } finally {
            em.close();
        }
    }
}
