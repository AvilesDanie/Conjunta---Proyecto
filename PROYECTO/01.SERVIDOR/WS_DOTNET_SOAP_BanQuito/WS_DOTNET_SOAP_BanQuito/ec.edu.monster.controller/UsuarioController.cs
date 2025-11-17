using System;
using System.Collections.Generic;
using System.Linq;
using System.ServiceModel;
using WS_DOTNET_SOAP_BanQuito.ec.edu.monster.dto;
using WS_DOTNET_SOAP_BanQuito.ec.edu.monster.model;
using WS_DOTNET_SOAP_BanQuito.ec.edu.monster.service;

namespace WS_DOTNET_SOAP_BanQuito.ec.edu.monster.controller
{
    public class UsuarioController : IUsuarioController
    {
        private readonly IUsuarioService _service;

        public UsuarioController()
        {
            // Versión simple sin IoC:
            _service = new UsuarioService();
        }

        private static UsuarioResponseDTO ToDto(Usuario u)
        {
            if (u == null) return null;
            return new UsuarioResponseDTO
            {
                Id = u.Id,
                Username = u.Username,
                Rol = u.Rol,
                Activo = u.Activo
            };
        }

        public List<UsuarioResponseDTO> ListarUsuarios()
        {
            var lista = _service.Listar();
            return lista.Select(ToDto).ToList();
        }

        public UsuarioResponseDTO BuscarUsuario(long id)
        {
            var u = _service.BuscarPorId(id);
            return ToDto(u); // null si no existe
        }

        public UsuarioResponseDTO CrearUsuario(CrearUsuarioRequest req)
        {
            try
            {
                var u = _service.Crear(req.Username, req.Password, req.Rol);
                return ToDto(u);
            }
            catch (ArgumentException ex)
            {
                throw new FaultException(ex.Message);
            }
            catch (InvalidOperationException ex)
            {
                throw new FaultException(ex.Message);
            }
        }

        public UsuarioResponseDTO ActualizarUsuario(long id, ActualizarUsuarioRequest req)
        {
            var u = _service.Actualizar(id, req.Password, req.Rol, req.Activo);
            if (u == null)
            {
                throw new FaultException("Usuario no encontrado");
            }
            return ToDto(u);
        }

        public bool EliminarUsuario(long id)
        {
            return _service.Eliminar(id);
        }

        public UsuarioResponseDTO Login(LoginRequest req)
        {
            if (req == null || string.IsNullOrWhiteSpace(req.Username) ||
                string.IsNullOrWhiteSpace(req.Password))
            {
                throw new FaultException("username y password son obligatorios");
            }

            var u = _service.Login(req.Username, req.Password);
            if (u == null)
            {
                throw new FaultException("Credenciales inválidas");
            }

            return ToDto(u);
        }
    }
}
