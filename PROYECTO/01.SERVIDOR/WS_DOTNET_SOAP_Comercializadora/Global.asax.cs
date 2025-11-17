using System;
using System.Data.Entity;
using System.Web;
using WS_DOTNET_SOAP_Comercializadora.ec.edu.monster.model;
using WS_DOTNET_SOAP_Comercializadora.ec.edu.monster.utils;

namespace WS_DOTNET_SOAP_Comercializadora
{
    public class Global : HttpApplication
    {
        protected void Application_Start(object sender, EventArgs e)
        {
            // 1. Inicializador de EF: crea la DB si no existe
            Database.SetInitializer(
                new CreateDatabaseIfNotExists<ComercializadoraContext>());

            // 2. Forzar creación
            using (var ctx = new ComercializadoraContext())
            {
                ctx.Database.Initialize(false);
            }

            // 3. Crear usuario por defecto si no existe
            DataInitializer.EnsureAdminUser();
        }
    }
}
