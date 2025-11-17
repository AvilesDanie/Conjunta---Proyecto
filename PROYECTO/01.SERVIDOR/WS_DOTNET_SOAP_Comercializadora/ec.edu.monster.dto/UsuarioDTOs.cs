using System.Runtime.Serialization;

namespace WS_DOTNET_SOAP_Comercializadora.ec.edu.monster.dto
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
    public class CrearUsuarioDTO
    {
        [DataMember] public string Username { get; set; }
        [DataMember] public string Password { get; set; }
        [DataMember] public string Rol { get; set; }
    }

    [DataContract]
    public class ActualizarUsuarioDTO
    {
        [DataMember] public string Password { get; set; }
        [DataMember] public string Rol { get; set; }
        [DataMember] public bool? Activo { get; set; }
    }

    [DataContract]
    public class LoginDTO
    {
        [DataMember] public string Username { get; set; }
        [DataMember] public string Password { get; set; }
    }
}
