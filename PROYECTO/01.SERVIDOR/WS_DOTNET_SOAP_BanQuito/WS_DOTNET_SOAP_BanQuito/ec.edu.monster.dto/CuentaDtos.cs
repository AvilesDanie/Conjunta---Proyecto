using System;
using System.Runtime.Serialization;

namespace WS_DOTNET_SOAP_BanQuito.ec.edu.monster.dto
{
    [DataContract]
    public class CuentaRequestDTO
    {
        [DataMember] public string CedulaCliente { get; set; }
        [DataMember] public string TipoCuenta { get; set; }
        [DataMember] public decimal? SaldoInicial { get; set; }
    }

    [DataContract]
    public class CuentaResponseDTO
    {
        [DataMember] public string NumCuenta { get; set; }
        [DataMember] public string CedulaCliente { get; set; }
        [DataMember] public string NombreCliente { get; set; }
        [DataMember] public string TipoCuenta { get; set; }
        [DataMember] public decimal Saldo { get; set; }
    }
}
