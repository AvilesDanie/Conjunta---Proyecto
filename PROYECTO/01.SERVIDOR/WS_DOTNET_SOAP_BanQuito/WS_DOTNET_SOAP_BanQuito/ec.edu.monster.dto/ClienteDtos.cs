using System.Runtime.Serialization;

namespace WS_DOTNET_SOAP_BanQuito.ec.edu.monster.dto
{
    [DataContract]
    public class ClienteRequestDTO
    {
        [DataMember] public string Cedula { get; set; }
        [DataMember] public string Nombre { get; set; }
        [DataMember] public string FechaNacimiento { get; set; }   // yyyy-MM-dd
        [DataMember] public string EstadoCivil { get; set; }
        [DataMember] public string TipoCuentaInicial { get; set; }
    }

    [DataContract]
    public class ClienteUpdateRequestDTO
    {
        [DataMember] public string Nombre { get; set; }
        [DataMember] public string EstadoCivil { get; set; }
    }

    [DataContract]
    public class ClienteResponseDTO
    {
        [DataMember] public string Cedula { get; set; }
        [DataMember] public string Nombre { get; set; }
        [DataMember] public string FechaNacimiento { get; set; }
        [DataMember] public string EstadoCivil { get; set; }
        [DataMember] public string NumCuentaInicial { get; set; }
        [DataMember] public string TipoCuentaInicial { get; set; }
    }

    [DataContract]
    public class ClienteOnlyResponseDTO
    {
        [DataMember] public string Cedula { get; set; }
        [DataMember] public string Nombre { get; set; }
        [DataMember] public string FechaNacimiento { get; set; }
        [DataMember] public string EstadoCivil { get; set; }
    }
}
