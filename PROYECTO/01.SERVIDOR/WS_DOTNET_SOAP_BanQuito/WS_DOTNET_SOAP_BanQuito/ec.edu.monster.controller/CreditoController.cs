using System;
using System.ServiceModel;
using WS_DOTNET_SOAP_BanQuito.ec.edu.monster.dto;
using WS_DOTNET_SOAP_BanQuito.ec.edu.monster.service;

namespace WS_DOTNET_SOAP_BanQuito.ec.edu.monster.controller
{
    public class CreditoController : ICreditoController
    {
        private readonly ICreditoService _service;

        public CreditoController()
        {
            _service = new CreditoService();
        }

        public ResultadoEvaluacionDTO EvaluarCredito(SolicitudCreditoDTO solicitud)
        {
            return _service.EvaluarCredito(solicitud);
        }

        public CreditoResponseDTO CrearCredito(SolicitudCreditoDTO solicitud)
        {
            try
            {
                var c = _service.CrearCredito(solicitud);
                return new CreditoResponseDTO
                {
                    Id = c.Id,
                    CedulaCliente = c.CedulaCliente,
                    NombreCliente = c.Cliente?.Nombre,
                    Monto = c.Monto,
                    PlazoMeses = c.PlazoMeses,
                    TasaAnual = c.TasaAnual,
                    FechaAprobacion = c.FechaAprobacion?.ToString("yyyy-MM-dd"),
                    Estado = c.Estado,
                    NumCuentaAsociada = c.NumCuentaAsociada
                };
            }
            catch (Exception ex)
            {
                throw new FaultException(ex.Message);
            }
        }

        public CreditoResponseDTO ObtenerCredito(long id)
        {
            var c = _service.ObtenerCredito(id);
            if (c == null)
                throw new FaultException("Crédito no encontrado");

            return new CreditoResponseDTO
            {
                Id = c.Id,
                CedulaCliente = c.CedulaCliente,
                NombreCliente = c.Cliente?.Nombre,
                Monto = c.Monto,
                PlazoMeses = c.PlazoMeses,
                TasaAnual = c.TasaAnual,
                FechaAprobacion = c.FechaAprobacion?.ToString("yyyy-MM-dd"),
                Estado = c.Estado,
                NumCuentaAsociada = c.NumCuentaAsociada
            };
        }
    }
}
