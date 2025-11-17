using System.Collections.Generic;
using System.ServiceModel;
using WS_DOTNET_SOAP_Comercializadora.ec.edu.monster.dto;

namespace WS_DOTNET_SOAP_Comercializadora.ec.edu.monster.controller
{
    [ServiceContract]
    public interface IFacturaController
    {
        [OperationContract]
        List<FacturaResponseDTO> ListarFacturas();

        [OperationContract]
        FacturaResponseDTO ObtenerFactura(long id);

        [OperationContract]
        FacturaResponseDTO CrearFactura(FacturaRequestDTO req);
    }
}
