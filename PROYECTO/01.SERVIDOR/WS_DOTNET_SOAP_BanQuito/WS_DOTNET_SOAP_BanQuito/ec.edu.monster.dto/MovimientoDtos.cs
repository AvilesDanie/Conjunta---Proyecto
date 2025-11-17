using System;
using System.Runtime.Serialization;

namespace WS_DOTNET_SOAP_BanQuito.ec.edu.monster.dto
{
    [DataContract]
    public class MovimientoRequestDTO
    {
        [DataMember] public string Tipo { get; set; }              // DEP, RET, TRA
        [DataMember] public decimal Valor { get; set; }
        [DataMember] public string Fecha { get; set; }             // opcional yyyy-MM-dd

        // DEP / RET
        [DataMember] public string NumCuenta { get; set; }

        // TRA
        [DataMember] public string NumCuentaOrigen { get; set; }
        [DataMember] public string NumCuentaDestino { get; set; }
    }

    [DataContract]
    public class MovimientoResponseDTO
    {
        [DataMember] public long Id { get; set; }
        [DataMember] public string NumCuenta { get; set; }
        [DataMember] public string Tipo { get; set; }
        [DataMember] public string Naturaleza { get; set; }
        [DataMember] public bool InternoTransferencia { get; set; }
        [DataMember] public decimal Valor { get; set; }
        [DataMember] public string Fecha { get; set; }
        [DataMember] public decimal SaldoCuenta { get; set; }
    }
}
