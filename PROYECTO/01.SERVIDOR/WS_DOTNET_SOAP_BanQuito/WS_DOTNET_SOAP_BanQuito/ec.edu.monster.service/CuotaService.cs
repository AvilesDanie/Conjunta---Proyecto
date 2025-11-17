using System;
using System.Collections.Generic;
using System.Linq;
using WS_DOTNET_SOAP_BanQuito.ec.edu.monster.model;

namespace WS_DOTNET_SOAP_BanQuito.ec.edu.monster.service
{
    public class CuotaService : ICuotaService
    {
        public List<CuotaAmortizacion> ListarPorCredito(long idCredito)
        {
            using (var ctx = new BanquitoContext())
            {
                return ctx.CuotasAmortizacion
                    .Where(c => c.IdCredito == idCredito)
                    .OrderBy(c => c.NumeroCuota)
                    .ToList();
            }
        }

        public CuotaAmortizacion Obtener(long id)
        {
            using (var ctx = new BanquitoContext())
            {
                return ctx.CuotasAmortizacion.Find(id);
            }
        }

        public CuotaAmortizacion Actualizar(long id, string estado)
        {
            using (var ctx = new BanquitoContext())
            {
                var cuota = ctx.CuotasAmortizacion.Find(id);
                if (cuota == null)
                    return null;

                if (!string.IsNullOrWhiteSpace(estado))
                    cuota.Estado = estado.ToUpper();

                ctx.SaveChanges();
                return cuota;
            }
        }

        public void Anular(long id)
        {
            using (var ctx = new BanquitoContext())
            {
                var cuota = ctx.CuotasAmortizacion.Find(id);
                if (cuota == null)
                    return;

                cuota.Estado = "ANULADA";
                ctx.SaveChanges();
            }
        }
    }
}
