using System;
using System.Data.Entity;
using System.Web;
using WS_DOTNET_SOAP_BanQuito.ec.edu.monster.model;
using WS_DOTNET_SOAP_BanQuito.ec.edu.monster.utils;

namespace WS_DOTNET_SOAP_BanQuito
{
    public class Global : HttpApplication
    {
        protected void Application_Start(object sender, EventArgs e)
        {
            // 1. Inicializador de EF: crea la BD si no existe
            Database.SetInitializer(
                new CreateDatabaseIfNotExists<BanquitoContext>());

            // 2. Forzar creación de la BD/tablas
            using (var ctx = new BanquitoContext())
            {
                ctx.Database.Initialize(false);
            }

            // 3. Crear usuario MONSTER si no existe
            DataInitializer.EnsureAdminUser();
        }
    }
}
