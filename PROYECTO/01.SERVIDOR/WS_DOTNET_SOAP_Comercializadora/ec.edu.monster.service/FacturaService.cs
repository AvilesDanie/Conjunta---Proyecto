using System.Collections.Generic;
using System.Linq;
using WS_DOTNET_SOAP_Comercializadora.ec.edu.monster.model;

namespace WS_DOTNET_SOAP_Comercializadora.ec.edu.monster.service
{
    public class FacturaService : IFacturaService
    {
        public List<Factura> ListarFacturas()
        {
            using (var ctx = new ComercializadoraContext())
            {
                return ctx.Facturas
                          .OrderByDescending(f => f.Fecha)
                          .ToList();
            }
        }

        public Factura ObtenerFactura(long id)
        {
            using (var ctx = new ComercializadoraContext())
            {
                return ctx.Facturas.Find(id);
            }
        }

        public Factura CrearFactura(Factura f)
        {
            using (var ctx = new ComercializadoraContext()){
                ctx.Facturas.Add(f);
                ctx.SaveChanges();
                return f;
            }
            
        }
    }
}
