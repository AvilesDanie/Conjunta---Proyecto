using System.Collections.Generic;
using System.Linq;
using WS_DOTNET_SOAP_Comercializadora.ec.edu.monster.model;

namespace WS_DOTNET_SOAP_Comercializadora.ec.edu.monster.service
{
    public class UsuarioService : IUsuarioService
    {
        public List<Usuario> Listar()
        {
            using (var ctx = new ComercializadoraContext())
            {
                return ctx.Usuarios.ToList();
            }
        }

        public Usuario Buscar(long id)
        {
            using (var ctx = new ComercializadoraContext())
            {
                return ctx.Usuarios.Find(id);
            }
        }

        public Usuario Crear(Usuario u)
        {
            using (var ctx = new ComercializadoraContext())
            {
                ctx.Usuarios.Add(u);
                ctx.SaveChanges();
                return u;
            }
        }

        public Usuario Actualizar(Usuario u)
        {
            using (var ctx = new ComercializadoraContext())
            {
                ctx.Entry(u).State = System.Data.Entity.EntityState.Modified;
                ctx.SaveChanges();
                return u;
            }
        }

        public bool Eliminar(long id)
        {
            using (var ctx = new ComercializadoraContext())
            {
                var u = ctx.Usuarios.Find(id);
                if (u == null) return false;

                ctx.Usuarios.Remove(u);
                ctx.SaveChanges();
                return true;
            }
        }

        public Usuario Login(string username)
        {
            using (var ctx = new ComercializadoraContext())
            {
                return ctx.Usuarios
                    .FirstOrDefault(x => x.Username == username && x.Activo);
            }
        }
    }
}
