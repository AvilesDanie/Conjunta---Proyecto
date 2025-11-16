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
import ec.edu.monster.integracion.BanquitoClient;
import ec.edu.monster.model.Electrodomestico;
import ec.edu.monster.model.Factura;
import ec.edu.monster.model.DetalleFactura;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Path("facturas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FacturaController {

    private final BanquitoClient banquitoClient = new BanquitoClient();

    // ====== DTOs ======

    public static class FacturaRequestDTO {
        public String cedulaCliente;
        public String nombreCliente;
        public Long idElectrodomestico;
        public int cantidad;
        public String formaPago; // EFECTIVO o CREDITO

        // Solo si formaPago = CREDITO
        public int plazoMeses;
        public String numCuentaCredito;
    }

    public static class DetalleFacturaDTO {
        public String codigoElectro;
        public String nombreElectro;
        public int cantidad;
        public BigDecimal precioUnitario;
        public BigDecimal subtotal;
    }

    public static class FacturaResponseDTO {
        public Long id;
        public String fecha;
        public String cedulaCliente;
        public String nombreCliente;
        public String formaPago;
        public BigDecimal totalBruto;
        public BigDecimal descuento;
        public BigDecimal totalNeto;
        public Long idCreditoBanquito;
        public List<DetalleFacturaDTO> detalles;
    }

    private FacturaResponseDTO toDTO(Factura f) {
        FacturaResponseDTO dto = new FacturaResponseDTO();
        dto.id = f.getId();
        dto.fecha = f.getFecha() != null ? f.getFecha().toString() : null;
        dto.cedulaCliente = f.getCedulaCliente();
        dto.nombreCliente = f.getNombreCliente();
        dto.formaPago = f.getFormaPago();
        dto.totalBruto = f.getTotalBruto();
        dto.descuento = f.getDescuento();
        dto.totalNeto = f.getTotalNeto();
        dto.idCreditoBanquito = f.getIdCreditoBanquito();

        dto.detalles = f.getDetalles() == null ? Collections.emptyList() :
                f.getDetalles().stream().map(d -> {
                    DetalleFacturaDTO dd = new DetalleFacturaDTO();
                    dd.codigoElectro = d.getElectrodomestico().getCodigo();
                    dd.nombreElectro = d.getElectrodomestico().getNombre();
                    dd.cantidad = d.getCantidad();
                    dd.precioUnitario = d.getPrecioUnitario();
                    dd.subtotal = d.getSubtotal();
                    return dd;
                }).collect(Collectors.toList());
        return dto;
    }

    // ====== Listar facturas (simple) ======

    @GET
    public List<FacturaResponseDTO> listar() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            List<Factura> facturas = em
                    .createQuery("SELECT f FROM Factura f ORDER BY f.fecha DESC", Factura.class)
                    .getResultList();
            return facturas.stream().map(this::toDTO).collect(Collectors.toList());
        } finally {
            em.close();
        }
    }

    // ====== Obtener factura por id ======

    @GET
    @Path("{id}")
    public Response obtener(@PathParam("id") Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Factura f = em.find(Factura.class, id);
            if (f == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"Factura no encontrada\"}")
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }
            return Response.ok(toDTO(f)).build();
        } finally {
            em.close();
        }
    }

    // ====== Crear factura ======

    @POST
    public Response crear(FacturaRequestDTO req) {
        if (req == null ||
            req.cedulaCliente == null ||
            req.nombreCliente == null ||
            req.idElectrodomestico == null ||
            req.cantidad <= 0 ||
            req.formaPago == null) {

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"cedulaCliente, nombreCliente, idElectrodomestico, cantidad>0 y formaPago son obligatorios\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        String forma = req.formaPago.toUpperCase();
        if (!forma.equals("EFECTIVO") && !forma.equals("CREDITO")) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"formaPago debe ser EFECTIVO o CREDITO\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            Electrodomestico electro = em.find(Electrodomestico.class, req.idElectrodomestico);
            if (electro == null) {
                em.getTransaction().rollback();
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"Electrodoméstico no existe\"}")
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            // Total bruto = precio * cantidad
            BigDecimal totalBruto = electro.getPrecioVenta()
                    .multiply(BigDecimal.valueOf(req.cantidad))
                    .setScale(2, RoundingMode.HALF_UP);

            BigDecimal descuento = BigDecimal.ZERO;
            BigDecimal totalNeto = totalBruto;
            Long idCreditoBanquito = null;

            if (forma.equals("EFECTIVO")) {
                // Descuento 33%
                descuento = totalBruto
                        .multiply(new BigDecimal("0.33"))
                        .setScale(2, RoundingMode.HALF_UP);
                totalNeto = totalBruto.subtract(descuento);

            } else { // CREDITO
                if (req.plazoMeses <= 0 || req.numCuentaCredito == null) {
                    em.getTransaction().rollback();
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity("Para CREDITO se requiere plazoMeses y numCuentaCredito").build();
                }

                // Llamar a BanQuito
                BanquitoClient.SolicitudCreditoDTO sol = new BanquitoClient.SolicitudCreditoDTO();
                sol.cedula = req.cedulaCliente;
                sol.precioProducto = totalBruto;  // valor del electrodoméstico
                sol.plazoMeses = req.plazoMeses;
                sol.numCuentaCredito = req.numCuentaCredito;

                // 1) Evaluar
                BanquitoClient.ResultadoEvaluacionDTO eval =
                        banquitoClient.evaluarCredito(sol);

                if (!eval.aprobado) {
                    em.getTransaction().rollback();
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity("Crédito rechazado por BanQuito: " + eval.motivo)
                            .build();
                }

                // 2) Crear crédito y recibir id
                BanquitoClient.CreditoResponseDTO cred =
                        banquitoClient.crearCredito(sol);

                idCreditoBanquito = cred.id;
                // En crédito no hay descuento, total_neto = total_bruto
            }

            // Crear Factura
            Factura f = new Factura();
            f.setFecha(LocalDate.now());
            f.setCedulaCliente(req.cedulaCliente);
            f.setNombreCliente(req.nombreCliente);
            f.setFormaPago(forma);
            f.setTotalBruto(totalBruto);
            f.setDescuento(descuento);
            f.setTotalNeto(totalNeto);
            f.setIdCreditoBanquito(idCreditoBanquito);

            // Detalle único (un electrodoméstico)
            DetalleFactura d = new DetalleFactura();
            d.setFactura(f);
            d.setElectrodomestico(electro);
            d.setCantidad(req.cantidad);
            d.setPrecioUnitario(electro.getPrecioVenta());
            d.setSubtotal(totalBruto);

            f.setDetalles(List.of(d));

            em.persist(f);  // cascada debería persistir el detalle si tienes Cascade.ALL
            em.getTransaction().commit();

            return Response.status(Response.Status.CREATED)
                    .entity(toDTO(f))
                    .build();

        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
