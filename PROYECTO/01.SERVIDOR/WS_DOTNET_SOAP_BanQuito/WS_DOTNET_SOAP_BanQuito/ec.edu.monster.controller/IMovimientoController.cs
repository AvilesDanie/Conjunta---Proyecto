using System.Collections.Generic;
using System.ServiceModel;
using WS_DOTNET_SOAP_BanQuito.ec.edu.monster.dto;

namespace WS_DOTNET_SOAP_BanQuito.ec.edu.monster.controller
{
    [ServiceContract]
    public interface IMovimientoController
    {
        [OperationContract]
        List<MovimientoResponseDTO> ListarMovimientosPorCuenta(string numCuenta);

        [OperationContract]
        MovimientoResponseDTO CrearMovimiento(MovimientoRequestDTO req);
    }
}
