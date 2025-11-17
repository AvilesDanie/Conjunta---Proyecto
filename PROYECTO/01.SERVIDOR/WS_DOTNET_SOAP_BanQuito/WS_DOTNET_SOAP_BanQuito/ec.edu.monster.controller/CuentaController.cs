using System;
using System.Collections.Generic;
using System.Linq;
using System.ServiceModel;
using WS_DOTNET_SOAP_BanQuito.ec.edu.monster.dto;
using WS_DOTNET_SOAP_BanQuito.ec.edu.monster.model;
using WS_DOTNET_SOAP_BanQuito.ec.edu.monster.service;

namespace WS_DOTNET_SOAP_BanQuito.ec.edu.monster.controller
{
    public class CuentaController : ICuentaController
    {
        private readonly ICuentaService _service;

        public CuentaController()
        {
            _service = new CuentaService();
        }

        private static CuentaResponseDTO ToDto(Cuenta c)
        {
            if (c == null) return null;

            return new CuentaResponseDTO
            {
                NumCuenta = c.NumCuenta,
                CedulaCliente = c.Cliente?.Cedula ?? c.CedulaCliente,
                NombreCliente = c.Cliente?.Nombre,
                TipoCuenta = c.TipoCuenta,
                Saldo = c.Saldo
            };
        }

        public List<CuentaResponseDTO> ListarCuentas()
        {
            var cuentas = _service.Listar();
            return cuentas.Select(ToDto).ToList();
        }

        public CuentaResponseDTO ObtenerCuenta(string numCuenta)
        {
            var cuenta = _service.BuscarPorNumCuenta(numCuenta);
            if (cuenta == null)
            {
                throw new FaultException("Cuenta no encontrada");
            }
            return ToDto(cuenta);
        }

        public List<CuentaResponseDTO> ListarCuentasPorCliente(string cedula)
        {
            var cuentas = _service.ListarPorCliente(cedula);
            return cuentas.Select(ToDto).ToList();
        }

        public CuentaResponseDTO CrearCuenta(CuentaRequestDTO req)
        {
            if (req == null)
            {
                throw new FaultException("El request no puede ser nulo");
            }

            try
            {
                var cuenta = _service.CrearCuenta(
                    req.CedulaCliente,
                    req.TipoCuenta,
                    req.SaldoInicial
                );

                return ToDto(cuenta);
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
    }
}
