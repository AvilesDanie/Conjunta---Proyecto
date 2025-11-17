using System.Collections.Generic;
using System.ServiceModel;
using WS_DOTNET_SOAP_BanQuito.ec.edu.monster.dto;

namespace WS_DOTNET_SOAP_BanQuito.ec.edu.monster.controller
{
    [ServiceContract]
    public interface ICuotaController
    {
        [OperationContract]
        List<CuotaResponseDTO> ListarPorCredito(long idCredito);

        [OperationContract]
        CuotaResponseDTO ObtenerCuota(long id);

        [OperationContract]
        CuotaResponseDTO ActualizarCuota(long id, ActualizarCuotaDTO dto);

        [OperationContract]
        string AnularCuota(long id);
    }
}
