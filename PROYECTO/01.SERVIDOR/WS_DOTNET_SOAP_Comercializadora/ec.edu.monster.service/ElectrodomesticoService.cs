using System.Collections.Generic;
using System.Linq;
using WS_DOTNET_SOAP_Comercializadora.ec.edu.monster.model;

namespace WS_DOTNET_SOAP_Comercializadora.ec.edu.monster.service
{
    public class ElectrodomesticoService : IElectrodomesticoService
    {
        public List<Electrodomestico> ListarElectrodomesticos()
        {
            using (var ctx = new ComercializadoraContext())
            {
                return ctx.Electrodomesticos.ToList();
            }
        }

        public Electrodomestico CrearElectrodomestico(Electrodomestico e)
        {
            using (var ctx = new ComercializadoraContext())
            {
                ctx.Electrodomesticos.Add(e);
                ctx.SaveChanges();
                return e;
            }
        }

        public bool ExisteCodigo(string codigo)
        {
            using (var ctx = new ComercializadoraContext())
            {
                return ctx.Electrodomesticos.Any(x => x.Codigo == codigo);
            }
        }
    }
}
