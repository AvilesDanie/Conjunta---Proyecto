using System;
using System.Collections.Generic;
using System.Linq;
using System.ServiceModel;
using WS_DOTNET_SOAP_BanQuito.ec.edu.monster.dto;
using WS_DOTNET_SOAP_BanQuito.ec.edu.monster.model;
using WS_DOTNET_SOAP_BanQuito.ec.edu.monster.service;

namespace WS_DOTNET_SOAP_BanQuito.ec.edu.monster.controller
{
    public class MovimientoController : IMovimientoController
    {
        private readonly IMovimientoService _service;

        public MovimientoController()
        {
            _service = new MovimientoService();
        }

        private static MovimientoResponseDTO ToDto(Movimiento m)
        {
            if (m == null) return null;

            return new MovimientoResponseDTO
            {
                Id = m.Id,
                NumCuenta = m.Cuenta?.NumCuenta,
                Tipo = m.Tipo,
                Naturaleza = m.Naturaleza,
                InternoTransferencia = m.InternoTransferencia,
                Valor = m.Valor,
                Fecha = m.Fecha.ToString("yyyy-MM-dd"),
                SaldoCuenta = m.Cuenta != null ? m.Cuenta.Saldo : 0m
            };
        }


        public List<MovimientoResponseDTO> ListarMovimientosPorCuenta(string numCuenta)
        {
            var lista = _service.ListarPorCuenta(numCuenta);
            return lista.Select(ToDto).ToList();
        }

        public MovimientoResponseDTO CrearMovimiento(MovimientoRequestDTO req)
        {
            if (req == null)
                throw new FaultException("El request no puede ser nulo");

            if (string.IsNullOrWhiteSpace(req.Tipo))
                throw new FaultException("tipo es obligatorio");

            if (req.Valor <= 0)
                throw new FaultException("valor debe ser mayor a cero");

            DateTime? fecha = null;
            if (!string.IsNullOrWhiteSpace(req.Fecha))
            {
                fecha = DateTime.Parse(req.Fecha);
            }

            try
            {
                var mov = _service.RegistrarMovimiento(
                    req.Tipo,
                    req.Valor,
                    fecha,
                    req.NumCuenta,
                    req.NumCuentaOrigen,
                    req.NumCuentaDestino
                );

                return ToDto(mov);
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
