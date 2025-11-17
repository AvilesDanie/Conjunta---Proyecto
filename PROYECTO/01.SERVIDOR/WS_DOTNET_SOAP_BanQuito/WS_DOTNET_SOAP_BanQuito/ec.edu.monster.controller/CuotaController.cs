using System;
using System.Collections.Generic;
using System.Linq;
using System.ServiceModel;
using WS_DOTNET_SOAP_BanQuito.ec.edu.monster.dto;
using WS_DOTNET_SOAP_BanQuito.ec.edu.monster.service;

namespace WS_DOTNET_SOAP_BanQuito.ec.edu.monster.controller
{
    public class CuotaController : ICuotaController
    {
        private readonly ICuotaService _service;

        public CuotaController()
        {
            _service = new CuotaService();
        }

        private CuotaResponseDTO ToDTO(model.CuotaAmortizacion c)
        {
            return new CuotaResponseDTO
            {
                Id = c.Id,
                IdCredito = c.IdCredito,
                NumeroCuota = c.NumeroCuota,
                ValorCuota = c.ValorCuota,
                InteresPagado = c.InteresPagado,
                CapitalPagado = c.CapitalPagado,
                Saldo = c.Saldo,
                FechaVencimiento = c.FechaVencimiento.HasValue 
                ? c.FechaVencimiento.Value.ToString("yyyy-MM-dd")
                : null,
                Estado = c.Estado
            };
        }

        public List<CuotaResponseDTO> ListarPorCredito(long idCredito)
        {
            return _service.ListarPorCredito(idCredito)
                .Select(ToDTO)
                .ToList();
        }

        public CuotaResponseDTO ObtenerCuota(long id)
        {
            var c = _service.Obtener(id);
            if (c == null)
                throw new FaultException("Cuota no encontrada");

            return ToDTO(c);
        }

        public CuotaResponseDTO ActualizarCuota(long id, ActualizarCuotaDTO dto)
        {
            try
            {
                var c = _service.Actualizar(id, dto.Estado);
                if (c == null)
                    throw new FaultException("Cuota no encontrada");

                return ToDTO(c);
            }
            catch (Exception ex)
            {
                throw new FaultException(ex.Message);
            }
        }

        public string AnularCuota(long id)
        {
            _service.Anular(id);
            return "OK";
        }
    }
}
