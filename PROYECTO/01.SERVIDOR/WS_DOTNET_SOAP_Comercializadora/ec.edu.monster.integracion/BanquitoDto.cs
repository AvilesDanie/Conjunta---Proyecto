using System.Runtime.Serialization;

namespace WS_DOTNET_SOAP_Comercializadora.ec.edu.monster.integracion
{
    // OJO: Namespace DEBE coincidir con el del servidor
    // http://schemas.datacontract.org/2004/07/WS_DOTNET_SOAP_BanQuito.ec.edu.monster.dto
    [DataContract(
        Namespace = "http://schemas.datacontract.org/2004/07/WS_DOTNET_SOAP_BanQuito.ec.edu.monster.dto")]
    public class SolicitudCreditoDTO
    {
        [DataMember] public string Cedula { get; set; }
        [DataMember] public decimal PrecioProducto { get; set; }
        [DataMember] public int PlazoMeses { get; set; }
        [DataMember] public string NumCuentaCredito { get; set; }
    }

    [DataContract(
        Namespace = "http://schemas.datacontract.org/2004/07/WS_DOTNET_SOAP_BanQuito.ec.edu.monster.dto")]
    public class ResultadoEvaluacionDTO
    {
        [DataMember] public bool SujetoCredito { get; set; }
        [DataMember] public decimal MontoMaximo { get; set; }
        [DataMember] public bool Aprobado { get; set; }
        [DataMember] public string Motivo { get; set; }
    }

    [DataContract(
        Namespace = "http://schemas.datacontract.org/2004/07/WS_DOTNET_SOAP_BanQuito.ec.edu.monster.dto")]
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
