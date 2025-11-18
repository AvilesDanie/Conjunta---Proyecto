package ec.edu.monster.controller;

import ec.edu.monster.JPAUtil;
import ec.edu.monster.model.Electrodomestico;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

@Path("electrodomesticos")
@Produces(MediaType.APPLICATION_JSON)
public class ElectrodomesticoController {

    // Carpeta relativa donde se guardan físicamente las imágenes
    private static final String RELATIVE_IMG_DIR = "imagenes/electrodomesticos";

    // Carpeta física en el servidor donde realmente se guardan los archivos
    private static final java.nio.file.Path UPLOAD_ROOT
            = Paths.get(System.getProperty("user.dir"), "uploads", RELATIVE_IMG_DIR);

    // Ajusta esto si tu @ApplicationPath es distinto
    // Ej: @ApplicationPath("/api")
    private static final String API_BASE_PATH = "/api";

    public static class ElectroResponseDTO {

        public Long id;
        public String codigo;
        public String nombre;
        public BigDecimal precioVenta;
        public String imagenUrl;   // URL lista para usar en el cliente
    }

    // DTO solo para describir el multipart en Swagger
    public static class ElectroMultipartRequest {

        @Schema(type = "string", description = "Código del electrodoméstico")
        public String codigo;

        @Schema(type = "string", description = "Nombre del electrodoméstico")
        public String nombre;

        @Schema(type = "number", description = "Precio de venta")
        public BigDecimal precioVenta;

        @Schema(type = "string", format = "binary", description = "Imagen del electrodoméstico")
        public java.io.File imagen;
    }

    private ElectroResponseDTO toDTO(Electrodomestico e) {
        ElectroResponseDTO dto = new ElectroResponseDTO();
        dto.id = e.getId();
        dto.codigo = e.getCodigo();
        dto.nombre = e.getNombre();
        dto.precioVenta = e.getPrecioVenta();

        // En la BD guardamos solo el nombre del archivo, aquí armamos la URL de acceso
        if (e.getImagenUrl() != null) {
            dto.imagenUrl = API_BASE_PATH + "/electrodomesticos/imagen/" + e.getImagenUrl();
        } else {
            dto.imagenUrl = null;
        }

        return dto;
    }

    @GET
    public List<ElectroResponseDTO> listar() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            List<Electrodomestico> lista = em
                    .createQuery("SELECT e FROM Electrodomestico e WHERE e.activo = true", Electrodomestico.class)
                    .getResultList();
            return lista.stream().map(this::toDTO).collect(Collectors.toList());
        } finally {
            em.close();
        }
    }

    // ===== NUEVO: endpoint para devolver la imagen =====
    @GET
    @Path("imagen/{nombreArchivo}")
    @Produces({"image/png", "image/jpeg", "image/jpg", "image/gif"})
    public Response obtenerImagen(@PathParam("nombreArchivo") String nombreArchivo) {
        try {
            // (Opcional) pequeña protección para evitar rutas raras con "../"
            if (nombreArchivo.contains("..")) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            java.nio.file.Path ruta = UPLOAD_ROOT.resolve(nombreArchivo);

            if (!Files.exists(ruta)) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            String mimeType = Files.probeContentType(ruta);
            if (mimeType == null) {
                mimeType = MediaType.APPLICATION_OCTET_STREAM;
            }

            return Response.ok(Files.newInputStream(ruta), mimeType).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ===== POST: crear (multipart/form-data) =====
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Crear electrodoméstico",
            description = "Crea un electrodoméstico y sube la imagen"
    )
    @RequestBody(
            required = true,
            content = @Content(
                    mediaType = MediaType.MULTIPART_FORM_DATA,
                    schema = @Schema(implementation = ElectroMultipartRequest.class)
            )
    )
    public Response crear(
            @FormDataParam("codigo") String codigo,
            @FormDataParam("nombre") String nombre,
            @FormDataParam("precioVenta") BigDecimal precioVenta,
            @FormDataParam("imagen") java.io.InputStream imagenStream,
            @FormDataParam("imagen") FormDataContentDisposition imagenDetail
    ) {
        // Validación básica
        if (codigo == null || nombre == null || precioVenta == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"codigo, nombre y precioVenta son obligatorios\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        if (precioVenta.compareTo(BigDecimal.ZERO) <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"precioVenta debe ser mayor a 0\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        if (imagenStream == null || imagenDetail == null || imagenDetail.getFileName() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"La imagen es obligatoria\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            Long count = em.createQuery(
                    "SELECT COUNT(e) FROM Electrodomestico e WHERE e.codigo = :cod",
                    Long.class)
                    .setParameter("cod", codigo)
                    .getSingleResult();
            if (count > 0) {
                em.getTransaction().rollback();
                return Response.status(Response.Status.CONFLICT)
                        .entity("{\"error\":\"Ya existe un electrodoméstico con ese código\"}")
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            // === Guardar imagen en carpeta física del servidor ===
            if (!Files.exists(UPLOAD_ROOT)) {
                Files.createDirectories(UPLOAD_ROOT);
            }

            String nombreOriginal = imagenDetail.getFileName();
            String extension = "";
            int idx = nombreOriginal.lastIndexOf('.');
            if (idx >= 0) {
                extension = nombreOriginal.substring(idx);
            }

            String nombreUnico = UUID.randomUUID().toString() + extension;
            java.nio.file.Path destino = UPLOAD_ROOT.resolve(nombreUnico);
            System.out.println("UPLOAD_ROOT = " + UPLOAD_ROOT.toAbsolutePath());
            Logger.getLogger(ElectrodomesticoController.class.getName())
                    .info("UPLOAD_ROOT = " + UPLOAD_ROOT.toAbsolutePath());

            Files.copy(imagenStream, destino);

            // IMPORTANTE:
            // Ahora en la BD guardamos SOLO el nombre del archivo (no la ruta ni la URL)
            Electrodomestico e = new Electrodomestico();
            e.setCodigo(codigo);
            e.setNombre(nombre);
            e.setPrecioVenta(precioVenta);
            e.setImagenUrl(nombreUnico); // <- solo nombreUnico

            em.persist(e);
            em.getTransaction().commit();

            return Response.status(Response.Status.CREATED).entity(toDTO(e)).build();
        } catch (Exception ex) {
            ex.printStackTrace();  // mira el log del servidor para ver exactamente qué pasa
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error interno al crear el electrodoméstico\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } finally {
            em.close();
        }
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarLogico(@PathParam("id") Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            Electrodomestico e = em.find(Electrodomestico.class, id);
            if (e == null || Boolean.FALSE.equals(e.getActivo())) {
                em.getTransaction().rollback();
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"Electrodoméstico no encontrado\"}")
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            e.setActivo(false);
            em.merge(e);
            em.getTransaction().commit();

            return Response.noContent().build(); // 204 sin body
        } catch (Exception ex) {
            ex.printStackTrace();
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error interno al eliminar el electrodoméstico\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } finally {
            em.close();
        }
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Actualizar electrodoméstico",
            description = "Actualiza datos de un electrodoméstico y opcionalmente su imagen"
    )
    @RequestBody(
            required = true,
            content = @Content(
                    mediaType = MediaType.MULTIPART_FORM_DATA,
                    schema = @Schema(implementation = ElectroMultipartRequest.class)
            )
    )
    public Response actualizar(
            @PathParam("id") Long id,
            @FormDataParam("codigo") String codigo,
            @FormDataParam("nombre") String nombre,
            @FormDataParam("precioVenta") BigDecimal precioVenta,
            @FormDataParam("imagen") java.io.InputStream imagenStream,
            @FormDataParam("imagen") FormDataContentDisposition imagenDetail
    ) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            Electrodomestico e = em.find(Electrodomestico.class, id);
            if (e == null || Boolean.FALSE.equals(e.getActivo())) {
                em.getTransaction().rollback();
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"Electrodoméstico no encontrado\"}")
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            // Validaciones básicas (puedes ajustarlas a tu gusto)
            if (codigo == null || nombre == null || precioVenta == null) {
                em.getTransaction().rollback();
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"codigo, nombre y precioVenta son obligatorios\"}")
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            if (precioVenta.compareTo(BigDecimal.ZERO) <= 0) {
                em.getTransaction().rollback();
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"precioVenta debe ser mayor a 0\"}")
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            // Validar que el código sea único (excepto para el mismo registro)
            Long count = em.createQuery(
                    "SELECT COUNT(e) FROM Electrodomestico e WHERE e.codigo = :cod AND e.id <> :id",
                    Long.class)
                    .setParameter("cod", codigo)
                    .setParameter("id", id)
                    .getSingleResult();
            if (count > 0) {
                em.getTransaction().rollback();
                return Response.status(Response.Status.CONFLICT)
                        .entity("{\"error\":\"Ya existe un electrodoméstico con ese código\"}")
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            // Actualizar campos simples
            e.setCodigo(codigo);
            e.setNombre(nombre);
            e.setPrecioVenta(precioVenta);

            // Manejar imagen nueva (opcional)
            if (imagenStream != null && imagenDetail != null && imagenDetail.getFileName() != null) {

                if (!Files.exists(UPLOAD_ROOT)) {
                    Files.createDirectories(UPLOAD_ROOT);
                }

                String nombreOriginal = imagenDetail.getFileName();
                String extension = "";
                int idx = nombreOriginal.lastIndexOf('.');
                if (idx >= 0) {
                    extension = nombreOriginal.substring(idx);
                }

                String nombreUnico = UUID.randomUUID().toString() + extension;
                java.nio.file.Path destino = UPLOAD_ROOT.resolve(nombreUnico);

                Files.copy(imagenStream, destino);

                // Opcional: eliminar la imagen anterior del disco
                if (e.getImagenUrl() != null) {
                    java.nio.file.Path anterior = UPLOAD_ROOT.resolve(e.getImagenUrl());
                    try {
                        Files.deleteIfExists(anterior);
                    } catch (Exception exDel) {
                        exDel.printStackTrace();
                        // no es crítico, solo logueamos
                    }
                }

                // Guardar solo el nombre del archivo en BD
                e.setImagenUrl(nombreUnico);
            }

            em.merge(e);
            em.getTransaction().commit();

            return Response.ok(toDTO(e)).build();
        } catch (Exception ex) {
            ex.printStackTrace();
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error interno al actualizar el electrodoméstico\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } finally {
            em.close();
        }
    }

}
