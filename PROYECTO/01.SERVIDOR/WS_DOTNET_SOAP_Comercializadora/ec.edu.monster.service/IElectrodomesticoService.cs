using System.Collections.Generic;
using WS_DOTNET_SOAP_Comercializadora.ec.edu.monster.model;

namespace WS_DOTNET_SOAP_Comercializadora.ec.edu.monster.service
{
    public interface IElectrodomesticoService
    {
        List<Electrodomestico> ListarElectrodomesticos();
        Electrodomestico CrearElectrodomestico(Electrodomestico e);
        bool ExisteCodigo(string codigo);
    }
}
