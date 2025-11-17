using System;
using System.Collections.Generic;
using System.Runtime.Serialization;

namespace WS_DOTNET_SOAP_Comercializadora.ec.edu.monster.dto
{
    [DataContract]
    public class FacturaRequestDTO
    {
        [DataMember] public string CedulaCliente { get; set; }
        [DataMember] public string NombreCliente { get; set; }
        [DataMember] public long IdElectrodomestico { get; set; }
        [DataMember] public int Cantidad { get; set; }
        [DataMember] public string FormaPago { get; set; }

        // Solo si formaPago = CREDITO
        [DataMember] public int PlazoMeses { get; set; }
        [DataMember] public string NumCuentaCredito { get; set; }
    }

    [DataContract]
    public class DetalleFacturaDTO
    {
        [DataMember] public string CodigoElectro { get; set; }
        [DataMember] public string NombreElectro { get; set; }
        [DataMember] public int Cantidad { get; set; }
        [DataMember] public decimal PrecioUnitario { get; set; }
        [DataMember] public decimal Subtotal { get; set; }
    }

    [DataContract]
    public class FacturaResponseDTO
    {
        [DataMember] public long Id { get; set; }
        [DataMember] public string Fecha { get; set; }
        [DataMember] public string CedulaCliente { get; set; }
        [DataMember] public string NombreCliente { get; set; }
        [DataMember] public string FormaPago { get; set; }
        [DataMember] public decimal TotalBruto { get; set; }
        [DataMember] public decimal Descuento { get; set; }
        [DataMember] public decimal TotalNeto { get; set; }
        [DataMember] public long? IdCreditoBanquito { get; set; }
        [DataMember] public List<DetalleFacturaDTO> Detalles { get; set; }
    }
}
