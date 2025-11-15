package ec.edu.monster.controller;

import ec.edu.monster.JPAUtil;
import ec.edu.monster.model.Usuario;
import ec.edu.monster.util.PasswordUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

// Swagger
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Path("usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Usuarios", description = "Gestión de usuarios del sistema BanQuito")
public class UsuarioController {

    // ====== DTOs ======

    @Schema(name = "UsuarioResponse")
    public static class UsuarioResponseDTO {
        public Long id;
        public String username;
        public String rol;
        public boolean activo;
    }

    @Schema(name = "CrearUsuarioRequest")
    public static class CrearUsuarioRequest {
        public String username;
        public String password;
        public String rol;
    }

    @Schema(name = "ActualizarUsuarioRequest")
    public static class ActualizarUsuarioRequest {
        public String password;  // opcional
        public String rol;       // opcional
        public Boolean activo;   // opcional
    }

    @Schema(name = "LoginRequest")
    public static class LoginRequest {
        public String username;
        public String password;
    }

    private UsuarioResponseDTO toDTO(Usuario u) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.id = u.getId();
        dto.username = u.getUsername();
        dto.rol = u.getRol();
        dto.activo = u.isActivo();
        return dto;
    }

    // ====== ENDPOINTS ======

    @GET
    @Operation(
            summary = "Lista todos los usuarios",
            description = "Devuelve la lista de usuarios sin exponer las contraseñas."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de usuarios",
            content = @Content(schema = @Schema(implementation = UsuarioResponseDTO.class))
    )
    public List<UsuarioResponseDTO> listar() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            List<Usuario> usuarios = em.createQuery("SELECT u FROM Usuario u", Usuario.class)
                    .getResultList();
            return usuarios.stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList());
        } finally {
            em.close();
        }
    }

    @GET
    @Path("{id}")
    @Operation(
            summary = "Busca un usuario por ID",
            description = "Devuelve los datos de un usuario sin la contraseña."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario encontrado",
                    content = @Content(schema = @Schema(implementation = UsuarioResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado"
            )
    })
    public Response buscar(@PathParam("id") Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Usuario u = em.find(Usuario.class, id);
            if (u == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(toDTO(u)).build();
        } finally {
            em.close();
        }
    }

    @POST
    @Operation(
            summary = "Crea un nuevo usuario",
            description = "Registra un usuario con contraseña hasheada."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Usuario creado",
            content = @Content(schema = @Schema(implementation = UsuarioResponseDTO.class))
    )
    public Response crear(CrearUsuarioRequest req) {
        if (req == null || req.username == null || req.password == null || req.rol == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("username, password y rol son obligatorios")
                    .build();
        }

        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            // ¿username ya existe?
            TypedQuery<Long> q = em.createQuery(
                    "SELECT COUNT(u) FROM Usuario u WHERE u.username = :user",
                    Long.class
            );
            q.setParameter("user", req.username);
            Long count = q.getSingleResult();
            if (count > 0) {
                em.getTransaction().rollback();
                return Response.status(Response.Status.CONFLICT)
                        .entity("Ya existe un usuario con ese username")
                        .build();
            }

            Usuario u = new Usuario();
            u.setUsername(req.username);
            u.setPassword(PasswordUtil.hashPassword(req.password)); // 🔐
            u.setRol(req.rol);
            u.setActivo(true);

            em.persist(u);
            em.getTransaction().commit();

            return Response.status(Response.Status.CREATED).entity(toDTO(u)).build();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @PUT
    @Path("{id}")
    @Operation(
            summary = "Actualiza un usuario",
            description = "Permite cambiar rol, estado y opcionalmente la contraseña."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario actualizado",
                    content = @Content(schema = @Schema(implementation = UsuarioResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado"
            )
    })
    public Response actualizar(@PathParam("id") Long id, ActualizarUsuarioRequest req) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Usuario u = em.find(Usuario.class, id);
            if (u == null) {
                em.getTransaction().rollback();
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            if (req.password != null && !req.password.isBlank()) {
                u.setPassword(PasswordUtil.hashPassword(req.password)); // 🔐
            }
            if (req.rol != null && !req.rol.isBlank()) {
                u.setRol(req.rol);
            }
            if (req.activo != null) {
                u.setActivo(req.activo);
            }

            em.merge(u);
            em.getTransaction().commit();
            return Response.ok(toDTO(u)).build();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @DELETE
    @Path("{id}")
    @Operation(
            summary = "Elimina un usuario",
            description = "Elimina definitivamente un usuario del sistema."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Usuario eliminado"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado"
            )
    })
    public Response eliminar(@PathParam("id") Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Usuario u = em.find(Usuario.class, id);
            if (u == null) {
                em.getTransaction().rollback();
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            em.remove(u);
            em.getTransaction().commit();
            return Response.noContent().build();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // ==== LOGIN ====

    @POST
    @Path("login")
    @Operation(
            summary = "Login de usuario",
            description = "Valida credenciales (password hasheado) y devuelve datos básicos."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Login exitoso",
                    content = @Content(schema = @Schema(implementation = UsuarioResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Credenciales inválidas"
            )
    })
    public Response login(LoginRequest req) {
        if (req == null || req.username == null || req.password == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("username y password son obligatorios").build();
        }

        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Usuario> q = em.createQuery(
                    "SELECT u FROM Usuario u WHERE u.username = :user AND u.activo = true",
                    Usuario.class
            );
            q.setParameter("user", req.username);

            List<Usuario> lista = q.getResultList();
            if (lista.isEmpty()) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
            Usuario u = lista.get(0);

            if (!PasswordUtil.matches(req.password, u.getPassword())) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }

            return Response.ok(toDTO(u)).build();
        } finally {
            em.close();
        }
    }
}
