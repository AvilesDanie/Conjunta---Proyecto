using System.Collections.Generic;
using System.Linq;
using System.ServiceModel;
using WS_DOTNET_SOAP_Comercializadora.ec.edu.monster.dto;
using WS_DOTNET_SOAP_Comercializadora.ec.edu.monster.model;
using WS_DOTNET_SOAP_Comercializadora.ec.edu.monster.service;
using WS_DOTNET_SOAP_Comercializadora.ec.edu.monster.utils;

namespace WS_DOTNET_SOAP_Comercializadora.ec.edu.monster.controller
{
    public class UsuarioController : IUsuarioController
    {
        private readonly IUsuarioService _service;

        public UsuarioController()
        {
            _service = new UsuarioService();
        }

        private UsuarioResponseDTO ToDTO(Usuario u)
        {
            return new UsuarioResponseDTO
            {
                Id = u.Id,
                Username = u.Username,
                Rol = u.Rol,
                Activo = u.Activo
            };
        }

        public List<UsuarioResponseDTO> Listar()
        {
            return _service.Listar()
                .Select(ToDTO)
                .ToList();
        }

        public UsuarioResponseDTO Buscar(long id)
        {
            var u = _service.Buscar(id);
            if (u == null)
                throw new FaultException("Usuario no encontrado");
            return ToDTO(u);
        }

        public UsuarioResponseDTO Crear(CrearUsuarioDTO dto)
        {
            if (dto == null || dto.Username == null || dto.Password == null || dto.Rol == null)
                throw new FaultException("username, password y rol son obligatorios");

            if (dto.Password.Length < 6)
                throw new FaultException("La contraseña debe tener al menos 6 caracteres");

            var nuevo = new Usuario
            {
                Username = dto.Username,
                Password = PasswordUtil.HashPassword(dto.Password),
                Rol = dto.Rol,
                Activo = true
            };

            var result = _service.Crear(nuevo);
            return ToDTO(result);
        }

        public UsuarioResponseDTO Actualizar(long id, ActualizarUsuarioDTO dto)
        {
            var u = _service.Buscar(id);
            if (u == null)
                throw new FaultException("Usuario no encontrado");

            if (!string.IsNullOrWhiteSpace(dto.Password))
                u.Password = PasswordUtil.HashPassword(dto.Password);

            if (!string.IsNullOrWhiteSpace(dto.Rol))
                u.Rol = dto.Rol;

            if (dto.Activo.HasValue)
                u.Activo = dto.Activo.Value;

            return ToDTO(_service.Actualizar(u));
        }

        

        public bool Eliminar(long id)
        {
            return _service.Eliminar(id);
        }

        public UsuarioResponseDTO Login(LoginDTO dto)
        {
            var u = _service.Login(dto.Username);

            if (u == null)
                throw new FaultException("Usuario no encontrado o inactivo");

            if (!PasswordUtil.Matches(dto.Password, u.Password))
                throw new FaultException("Credenciales inválidas");

            return ToDTO(u);
        }
    }
}
