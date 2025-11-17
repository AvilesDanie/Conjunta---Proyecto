using System.Runtime.Serialization;

namespace WS_DOTNET_SOAP_BanQuito.ec.edu.monster.dto
{
    [DataContract]
    public class UsuarioResponseDTO
    {
        [DataMember] public long Id { get; set; }
        [DataMember] public string Username { get; set; }
        [DataMember] public string Rol { get; set; }
        [DataMember] public bool Activo { get; set; }
    }

    [DataContract]
    public class CrearUsuarioRequest
    {
        [DataMember] public string Username { get; set; }
        [DataMember] public string Password { get; set; }
        [DataMember] public string Rol { get; set; }
    }

    [DataContract]
    public class ActualizarUsuarioRequest
    {
        [DataMember] public string Password { get; set; }
        [DataMember] public string Rol { get; set; }
        [DataMember] public bool? Activo { get; set; }
    }

    [DataContract]
    public class LoginRequest
    {
        [DataMember] public string Username { get; set; }
        [DataMember] public string Password { get; set; }
    }
}
