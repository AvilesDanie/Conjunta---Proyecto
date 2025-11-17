using System;
using System.Runtime.Serialization;

namespace WS_DOTNET_SOAP_BanQuito.ec.edu.monster.dto
{
    [DataContract]
    public class SolicitudCreditoDTO
    {
        [DataMember] public string Cedula { get; set; }
        [DataMember] public decimal PrecioProducto { get; set; }
        [DataMember] public int PlazoMeses { get; set; }
        [DataMember] public string NumCuentaCredito { get; set; }
    }

    [DataContract]
    public class ResultadoEvaluacionDTO
    {
        [DataMember] public bool SujetoCredito { get; set; }
        [DataMember] public decimal MontoMaximo { get; set; }
        [DataMember] public bool Aprobado { get; set; }
        [DataMember] public string Motivo { get; set; }
    }

    [DataContract]
    public class CreditoResponseDTO
    {
        [DataMember] public long Id { get; set; }
        [DataMember] public string CedulaCliente { get; set; }
        [DataMember] public string NombreCliente { get; set; }
        [DataMember] public decimal Monto { get; set; }
        [DataMember] public int PlazoMeses { get; set; }
        [DataMember] public decimal TasaAnual { get; set; }
        [DataMember] public string FechaAprobacion { get; set; }
        [DataMember] public string Estado { get; set; }
        [DataMember] public string NumCuentaAsociada { get; set; }
    }
}
