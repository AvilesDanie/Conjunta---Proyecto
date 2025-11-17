using System;
using System.Collections.Generic;
using System.Linq;
using System.ServiceModel;
using WS_DOTNET_SOAP_Comercializadora.ec.edu.monster.dto;
using WS_DOTNET_SOAP_Comercializadora.ec.edu.monster.model;
using WS_DOTNET_SOAP_Comercializadora.ec.edu.monster.service;

namespace WS_DOTNET_SOAP_Comercializadora.ec.edu.monster.controller
{
    public class ElectrodomesticoController : IElectrodomesticoController
    {
        private readonly IElectrodomesticoService _service;

        public ElectrodomesticoController()
        {
            _service = new ElectrodomesticoService();
        }

        private ElectroResponseDTO ToDTO(Electrodomestico e)
        {
            return new ElectroResponseDTO
            {
                Id = e.Id,
                Codigo = e.Codigo,
                Nombre = e.Nombre,
                PrecioVenta = e.PrecioVenta
            };
        }

        public List<ElectroResponseDTO> ListarElectrodomesticos()
        {
            return _service.ListarElectrodomesticos()
                           .Select(ToDTO)
                           .ToList();
        }

        public ElectroResponseDTO CrearElectrodomestico(ElectroRequestDTO req)
        {
            if (req == null ||
                string.IsNullOrWhiteSpace(req.Codigo) ||
                string.IsNullOrWhiteSpace(req.Nombre))
            {
                throw new FaultException("codigo, nombre y precioVenta son obligatorios");
            }

            if (req.PrecioVenta <= 0)
            {
                throw new FaultException("precioVenta debe ser mayor a 0");
            }

            if (_service.ExisteCodigo(req.Codigo))
            {
                throw new FaultException("Ya existe un electrodoméstico con ese código");
            }

            var e = new Electrodomestico
            {
                Codigo = req.Codigo,
                Nombre = req.Nombre,
                PrecioVenta = req.PrecioVenta
            };

            var creado = _service.CrearElectrodomestico(e);
            return ToDTO(creado);
        }
    }
}
