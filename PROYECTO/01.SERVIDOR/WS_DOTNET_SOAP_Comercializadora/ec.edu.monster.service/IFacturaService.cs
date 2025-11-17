using System.Collections.Generic;
using WS_DOTNET_SOAP_Comercializadora.ec.edu.monster.model;

namespace WS_DOTNET_SOAP_Comercializadora.ec.edu.monster.service
{
    public interface IFacturaService
    {
        List<Factura> ListarFacturas();
        Factura ObtenerFactura(long id);
        Factura CrearFactura(Factura f);
    }
}
