using System;
using System.Collections.Generic;
using System.Linq;
using System.ServiceModel;
using WS_DOTNET_SOAP_Comercializadora.ec.edu.monster.dto;
using WS_DOTNET_SOAP_Comercializadora.ec.edu.monster.model;
using WS_DOTNET_SOAP_Comercializadora.ec.edu.monster.service;
using WS_DOTNET_SOAP_Comercializadora.ec.edu.monster.integracion;
using System.Data.Entity;

namespace WS_DOTNET_SOAP_Comercializadora.ec.edu.monster.controller
{
    public class FacturaController : IFacturaController
    {
        private readonly FacturaService _facturaService =
            new FacturaService();

        // URL del servicio SOAP de BanQuito (ajusta el puerto / path si es distinto)
        private readonly BanquitoSoapClient _banquito =
            new BanquitoSoapClient(
                "http://localhost:52582/ec.edu.monster.controller/CreditoController.svc");


        private FacturaResponseDTO ToDTO(Factura f)
        {
            var dto = new FacturaResponseDTO();
            dto.Id = f.Id;
            dto.Fecha = f.Fecha.ToString("yyyy-MM-dd");
            dto.CedulaCliente = f.CedulaCliente;
            dto.NombreCliente = f.NombreCliente;
            dto.FormaPago = f.FormaPago;
            dto.TotalBruto = f.TotalBruto;
            dto.Descuento = f.Descuento;
            dto.TotalNeto = f.TotalNeto;
            dto.IdCreditoBanquito = f.IdCreditoBanquito;

            dto.Detalles = f.Detalles == null
                ? new List<DetalleFacturaDTO>()
                : f.Detalles.Select(d => new DetalleFacturaDTO
                {
                    CodigoElectro = d.Electrodomestico.Codigo,
                    NombreElectro = d.Electrodomestico.Nombre,
                    Cantidad = d.Cantidad,
                    PrecioUnitario = d.PrecioUnitario,
                    Subtotal = d.Subtotal
                }).ToList();

            return dto;
        }

        public List<FacturaResponseDTO> ListarFacturas()
        {
            var facturas = _facturaService.ListarFacturas();
            return facturas.Select(ToDTO).ToList();
        }

        public FacturaResponseDTO ObtenerFactura(long id)
        {
            var f = _facturaService.ObtenerFactura(id);
            if (f == null)
                throw new FaultException("Factura no encontrada");

            return ToDTO(f);
        }

        public FacturaResponseDTO CrearFactura(FacturaRequestDTO req)
        {
            if (req == null ||
                string.IsNullOrWhiteSpace(req.CedulaCliente) ||
                string.IsNullOrWhiteSpace(req.NombreCliente) ||
                req.IdElectrodomestico <= 0 ||
                req.Cantidad <= 0 ||
                string.IsNullOrWhiteSpace(req.FormaPago))
            {
                throw new FaultException("cedulaCliente, nombreCliente, idElectrodomestico, cantidad>0 y formaPago son obligatorios");
            }

            string forma = req.FormaPago.ToUpperInvariant();
            if (forma != "EFECTIVO" && forma != "CREDITO")
            {
                throw new FaultException("formaPago debe ser EFECTIVO o CREDITO");
            }

            using (var ctx = new ComercializadoraContext())
            {
                var electro = ctx.Electrodomesticos.Find(req.IdElectrodomestico);
                if (electro == null)
                {
                    throw new FaultException("Electrodoméstico no existe");
                }

                decimal totalBruto = Math.Round(
                    electro.PrecioVenta * req.Cantidad, 2);

                decimal descuento = 0m;
                decimal totalNeto = totalBruto;
                long? idCreditoBanquito = null;

                if (forma == "EFECTIVO")
                {
                    descuento = Math.Round(totalBruto * 0.33m, 2);
                    totalNeto = totalBruto - descuento;
                }
                else // CREDITO
                {
                    if (req.PlazoMeses <= 0 ||
                        string.IsNullOrWhiteSpace(req.NumCuentaCredito))
                    {
                        throw new FaultException("Para CREDITO se requiere plazoMeses y numCuentaCredito");
                    }

                    var sol = new SolicitudCreditoDTO
                    {
                        Cedula = req.CedulaCliente,
                        PrecioProducto = totalBruto,
                        PlazoMeses = req.PlazoMeses,
                        NumCuentaCredito = req.NumCuentaCredito
                    };

                    var eval = _banquito.EvaluarCredito(sol);
                    if (!eval.Aprobado)
                    {
                        throw new FaultException("Crédito rechazado por BanQuito: " + eval.Motivo);
                    }

                    var cred = _banquito.CrearCredito(sol);
                    idCreditoBanquito = cred.Id;
                    // En crédito no hay descuento: totalNeto = totalBruto
                }

                var f = new Factura();
                f.Fecha = DateTime.Now;
                f.CedulaCliente = req.CedulaCliente;
                f.NombreCliente = req.NombreCliente;
                f.FormaPago = forma;
                f.TotalBruto = totalBruto;
                f.Descuento = descuento;
                f.TotalNeto = totalNeto;
                f.IdCreditoBanquito = idCreditoBanquito;

                var detalle = new DetalleFactura();
                detalle.Factura = f;
                detalle.Electrodomestico = electro;
                detalle.Cantidad = req.Cantidad;
                detalle.PrecioUnitario = electro.PrecioVenta;
                detalle.Subtotal = totalBruto;

                f.Detalles = new List<DetalleFactura> { detalle };

                ctx.Facturas.Add(f);
                ctx.SaveChanges();

                return ToDTO(f);
            }
        }
    }
}
