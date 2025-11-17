using System.ServiceModel;
using WS_DOTNET_SOAP_BanQuito.ec.edu.monster.dto;

namespace WS_DOTNET_SOAP_BanQuito.ec.edu.monster.controller
{
    [ServiceContract]
    public interface ICreditoController
    {
        [OperationContract]
        ResultadoEvaluacionDTO EvaluarCredito(SolicitudCreditoDTO solicitud);

        [OperationContract]
        CreditoResponseDTO CrearCredito(SolicitudCreditoDTO solicitud);

        [OperationContract]
        CreditoResponseDTO ObtenerCredito(long id);
    }
}
