using System.Collections.Generic;
using WS_DOTNET_SOAP_BanQuito.ec.edu.monster.dto;
using WS_DOTNET_SOAP_BanQuito.ec.edu.monster.model;

namespace WS_DOTNET_SOAP_BanQuito.ec.edu.monster.service
{
    public interface ICuotaService
    {
        List<CuotaAmortizacion> ListarPorCredito(long idCredito);
        CuotaAmortizacion Obtener(long id);
        CuotaAmortizacion Actualizar(long id, string estado);
        void Anular(long id);
    }
}
