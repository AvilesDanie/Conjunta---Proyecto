using System;
using System.Runtime.Serialization;

namespace WS_DOTNET_SOAP_BanQuito.ec.edu.monster.dto
{
    [DataContract]
    public class CuotaResponseDTO
    {
        [DataMember] public long Id { get; set; }
        [DataMember] public long IdCredito { get; set; }
        [DataMember] public int NumeroCuota { get; set; }
        [DataMember] public decimal ValorCuota { get; set; }
        [DataMember] public decimal InteresPagado { get; set; }
        [DataMember] public decimal CapitalPagado { get; set; }
        [DataMember] public decimal Saldo { get; set; }
        [DataMember] public string FechaVencimiento { get; set; }
        [DataMember] public string Estado { get; set; }
    }

    [DataContract]
    public class ActualizarCuotaDTO
    {
        [DataMember] public string Estado { get; set; }
        [DataMember] public string FechaPago { get; set; } // opcional futuro
    }
}
