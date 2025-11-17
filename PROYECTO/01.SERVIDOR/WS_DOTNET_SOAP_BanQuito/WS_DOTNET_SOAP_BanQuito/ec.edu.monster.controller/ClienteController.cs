using System;
using System.Collections.Generic;
using System.Linq;
using System.ServiceModel;
using WS_DOTNET_SOAP_BanQuito.ec.edu.monster.dto;
using WS_DOTNET_SOAP_BanQuito.ec.edu.monster.model;
using WS_DOTNET_SOAP_BanQuito.ec.edu.monster.service;

namespace WS_DOTNET_SOAP_BanQuito.ec.edu.monster.controller
{
    public class ClienteController : IClienteController
    {
        private readonly IClienteService _service;

        public ClienteController()
        {
            _service = new ClienteService();
        }

        // ===== mapeos =====

        private static ClienteResponseDTO ToDto(Cliente c, Cuenta cuentaInicial)
        {
            if (c == null) return null;

            return new ClienteResponseDTO
            {
                Cedula = c.Cedula,
                Nombre = c.Nombre,
                EstadoCivil = c.EstadoCivil,
                FechaNacimiento = c.FechaNacimiento != null
                    ? c.FechaNacimiento.Value.ToString("yyyy-MM-dd")
                    : null,
                NumCuentaInicial = cuentaInicial?.NumCuenta,
                TipoCuentaInicial = cuentaInicial?.TipoCuenta
            };
        }

        private static ClienteOnlyResponseDTO ToDtoOnly(Cliente c)
        {
            if (c == null) return null;

            return new ClienteOnlyResponseDTO
            {
                Cedula = c.Cedula,
                Nombre = c.Nombre,
                EstadoCivil = c.EstadoCivil,
                FechaNacimiento = c.FechaNacimiento != null
                    ? c.FechaNacimiento.Value.ToString("yyyy-MM-dd")
                    : null
            };
        }

        // ===== operaciones =====

        public List<ClienteResponseDTO> ListarClientes()
        {
            var clientes = _service.Listar();
            var result = new List<ClienteResponseDTO>();

            using (var ctx = new BanquitoContext())
            {
                foreach (var c in clientes)
                {
                    var cuenta = ctx.Cuentas
                        .Where(cu => cu.CedulaCliente == c.Cedula)
                        .FirstOrDefault();

                    result.Add(ToDto(c, cuenta));
                }
            }

            return result;
        }

        public ClienteOnlyResponseDTO ObtenerCliente(string cedula)
        {
            var c = _service.BuscarPorCedula(cedula);
            if (c == null)
            {
                throw new FaultException("Cliente no encontrado");
            }
            return ToDtoOnly(c);
        }

        public ClienteResponseDTO CrearCliente(ClienteRequestDTO req)
        {
            if (req == null)
            {
                throw new FaultException("El request no puede ser nulo");
            }

            DateTime? fechaNac = null;
            if (!string.IsNullOrWhiteSpace(req.FechaNacimiento))
            {
                fechaNac = DateTime.Parse(req.FechaNacimiento);
            }

            try
            {
                Cuenta cuentaInicial;
                var cliente = _service.CrearCliente(
                    req.Cedula,
                    req.Nombre,
                    req.EstadoCivil,
                    fechaNac,
                    req.TipoCuentaInicial,
                    out cuentaInicial);

                return ToDto(cliente, cuentaInicial);
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

        public ClienteOnlyResponseDTO ActualizarCliente(string cedula, ClienteUpdateRequestDTO req)
        {
            if (req == null)
            {
                throw new FaultException("El request no puede ser nulo");
            }

            var cliente = _service.ActualizarCliente(cedula, req.Nombre, req.EstadoCivil);
            if (cliente == null)
            {
                throw new FaultException("Cliente no encontrado");
            }

            return ToDtoOnly(cliente);
        }

        public bool EliminarCliente(string cedula)
        {
            return _service.EliminarCliente(cedula);
        }
    }
}
