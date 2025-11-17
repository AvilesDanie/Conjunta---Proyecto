using System.Linq;
using WS_DOTNET_SOAP_Comercializadora.ec.edu.monster.model;

namespace WS_DOTNET_SOAP_Comercializadora.ec.edu.monster.utils
{
    public static class DataInitializer
    {
        public static void EnsureAdminUser()
        {
            const string adminUser = "MONSTER";
            const string adminPasswordPlain = "MONSTER9";

            using (var ctx = new ComercializadoraContext())
            {
                bool exists = ctx.Usuarios.Any(u => u.Username == adminUser);

                if (!exists)
                {
                    var admin = new Usuario
                    {
                        Username = adminUser,
                        Password = PasswordUtil.HashPassword(adminPasswordPlain),
                        Rol = "SUPERADMIN",
                        Activo = true
                    };

                    ctx.Usuarios.Add(admin);
                    ctx.SaveChanges();
                }
            }
        }
    }
}
