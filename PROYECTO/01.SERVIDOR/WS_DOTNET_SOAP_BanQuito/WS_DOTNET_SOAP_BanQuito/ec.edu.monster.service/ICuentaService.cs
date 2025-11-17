using System.Collections.Generic;
using WS_DOTNET_SOAP_BanQuito.ec.edu.monster.model;

namespace WS_DOTNET_SOAP_BanQuito.ec.edu.monster.service
{
    public interface ICuentaService
    {
        List<Cuenta> Listar();
        Cuenta BuscarPorNumCuenta(string numCuenta);
        List<Cuenta> ListarPorCliente(string cedula);
        Cuenta CrearCuenta(string cedulaCliente, string tipoCuenta, decimal? saldoInicial);
    }
}
