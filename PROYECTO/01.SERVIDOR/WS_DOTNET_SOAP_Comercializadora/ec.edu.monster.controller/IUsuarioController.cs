using System.Collections.Generic;
using System.ServiceModel;
using WS_DOTNET_SOAP_Comercializadora.ec.edu.monster.dto;

namespace WS_DOTNET_SOAP_Comercializadora.ec.edu.monster.controller
{
    [ServiceContract]
    public interface IUsuarioController
    {
        [OperationContract] List<UsuarioResponseDTO> Listar();
        [OperationContract] UsuarioResponseDTO Buscar(long id);
        [OperationContract] UsuarioResponseDTO Crear(CrearUsuarioDTO dto);
        [OperationContract] UsuarioResponseDTO Actualizar(long id, ActualizarUsuarioDTO dto);
        [OperationContract] bool Eliminar(long id);
        [OperationContract] UsuarioResponseDTO Login(LoginDTO dto);
    }
}
