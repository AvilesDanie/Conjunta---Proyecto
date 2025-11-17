using System;
using System.Collections.Generic;
using WS_DOTNET_SOAP_BanQuito.ec.edu.monster.model;

namespace WS_DOTNET_SOAP_BanQuito.ec.edu.monster.service
{
    public interface IClienteService
    {
        List<Cliente> Listar();
        Cliente BuscarPorCedula(string cedula);
        Cliente CrearCliente(string cedula, string nombre, string estadoCivil,
                             DateTime? fechaNacimiento, string tipoCuentaInicial,
                             out Cuenta cuentaInicial);
        Cliente ActualizarCliente(string cedula, string nombre, string estadoCivil);
        bool EliminarCliente(string cedula);
    }
}
