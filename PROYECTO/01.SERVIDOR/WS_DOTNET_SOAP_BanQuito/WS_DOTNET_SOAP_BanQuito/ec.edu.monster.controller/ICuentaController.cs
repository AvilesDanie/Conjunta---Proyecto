using System.Collections.Generic;
using System.ServiceModel;
using WS_DOTNET_SOAP_BanQuito.ec.edu.monster.dto;

namespace WS_DOTNET_SOAP_BanQuito.ec.edu.monster.controller
{
    [ServiceContract]
    public interface ICuentaController
    {
        [OperationContract]
        List<CuentaResponseDTO> ListarCuentas();

        [OperationContract]
        CuentaResponseDTO ObtenerCuenta(string numCuenta);

        [OperationContract]
        List<CuentaResponseDTO> ListarCuentasPorCliente(string cedula);

        [OperationContract]
        CuentaResponseDTO CrearCuenta(CuentaRequestDTO req);
    }
}
