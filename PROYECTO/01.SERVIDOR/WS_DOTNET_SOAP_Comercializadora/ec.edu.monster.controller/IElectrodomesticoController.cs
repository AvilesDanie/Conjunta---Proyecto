using System.Collections.Generic;
using System.ServiceModel;
using WS_DOTNET_SOAP_Comercializadora.ec.edu.monster.dto;

namespace WS_DOTNET_SOAP_Comercializadora.ec.edu.monster.controller
{
    [ServiceContract]
    public interface IElectrodomesticoController
    {
        [OperationContract]
        List<ElectroResponseDTO> ListarElectrodomesticos();

        [OperationContract]
        ElectroResponseDTO CrearElectrodomestico(ElectroRequestDTO dto);
    }
}
