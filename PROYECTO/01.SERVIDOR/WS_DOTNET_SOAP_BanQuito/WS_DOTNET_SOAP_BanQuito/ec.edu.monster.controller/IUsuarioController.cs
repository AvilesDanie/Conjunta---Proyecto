using System.Collections.Generic;
using System.ServiceModel;
using WS_DOTNET_SOAP_BanQuito.ec.edu.monster.dto;

namespace WS_DOTNET_SOAP_BanQuito.ec.edu.monster.controller
{
    [ServiceContract]
    public interface IUsuarioController
    {
        [OperationContract]
        List<UsuarioResponseDTO> ListarUsuarios();

        [OperationContract]
        UsuarioResponseDTO BuscarUsuario(long id);

        [OperationContract]
        UsuarioResponseDTO CrearUsuario(CrearUsuarioRequest req);

        [OperationContract]
        UsuarioResponseDTO ActualizarUsuario(long id, ActualizarUsuarioRequest req);

        [OperationContract]
        bool EliminarUsuario(long id);

        [OperationContract]
        UsuarioResponseDTO Login(LoginRequest req);
    }
}
