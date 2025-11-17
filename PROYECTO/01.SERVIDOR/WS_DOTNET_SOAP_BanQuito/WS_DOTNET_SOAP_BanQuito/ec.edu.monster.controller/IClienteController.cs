using System.Collections.Generic;
using System.ServiceModel;
using WS_DOTNET_SOAP_BanQuito.ec.edu.monster.dto;

namespace WS_DOTNET_SOAP_BanQuito.ec.edu.monster.controller
{
    [ServiceContract]
    public interface IClienteController
    {
        [OperationContract]
        List<ClienteResponseDTO> ListarClientes();

        [OperationContract]
        ClienteOnlyResponseDTO ObtenerCliente(string cedula);

        [OperationContract]
        ClienteResponseDTO CrearCliente(ClienteRequestDTO req);

        [OperationContract]
        ClienteOnlyResponseDTO ActualizarCliente(string cedula, ClienteUpdateRequestDTO req);

        [OperationContract]
        bool EliminarCliente(string cedula);
    }
}
