using System;
using System.Collections.Generic;
using System.Linq;
using WS_DOTNET_SOAP_BanQuito.ec.edu.monster.model;
using WS_DOTNET_SOAP_BanQuito.ec.edu.monster.utils;

namespace WS_DOTNET_SOAP_BanQuito.ec.edu.monster.service
{
    public class UsuarioService : IUsuarioService
    {
        public List<Usuario> Listar()
        {
            using (var ctx = new BanquitoContext())
            {
                return ctx.Usuarios.ToList();
            }
        }

        public Usuario BuscarPorId(long id)
        {
            using (var ctx = new BanquitoContext())
            {
                return ctx.Usuarios.Find(id);
            }
        }

        public Usuario Crear(string username, string passwordPlano, string rol)
        {
            if (string.IsNullOrWhiteSpace(username) ||
                string.IsNullOrWhiteSpace(passwordPlano) ||
                string.IsNullOrWhiteSpace(rol))
            {
                throw new ArgumentException("username, password y rol son obligatorios");
            }

            if (passwordPlano.Length < 6)
            {
                throw new ArgumentException("La contraseña debe tener al menos 6 caracteres");
            }

            using (var ctx = new BanquitoContext())
            {
                bool exists = ctx.Usuarios.Any(x => x.Username == username);
                if (exists)
                {
                    throw new InvalidOperationException("Ya existe un usuario con ese username");
                }

                var u = new Usuario
                {
                    Username = username,
                    Password = PasswordUtil.HashPassword(passwordPlano),
                    Rol = rol,
                    Activo = true
                };

                ctx.Usuarios.Add(u);
                ctx.SaveChanges();
                return u;
            }
        }

        public Usuario Actualizar(long id, string passwordPlano, string rol, bool? activo)
        {
            using (var ctx = new BanquitoContext())
            {
                var u = ctx.Usuarios.Find(id);
                if (u == null)
                {
                    return null;
                }

                if (!string.IsNullOrWhiteSpace(passwordPlano))
                {
                    u.Password = PasswordUtil.HashPassword(passwordPlano);
                }

                if (!string.IsNullOrWhiteSpace(rol))
                {
                    u.Rol = rol;
                }

                if (activo.HasValue)
                {
                    u.Activo = activo.Value;
                }

                ctx.SaveChanges();
                return u;
            }
        }

        public bool Eliminar(long id)
        {
            using (var ctx = new BanquitoContext())
            {
                var u = ctx.Usuarios.Find(id);
                if (u == null) return false;

                ctx.Usuarios.Remove(u);
                ctx.SaveChanges();
                return true;
            }
        }

        public Usuario Login(string username, string passwordPlano)
        {
            using (var ctx = new BanquitoContext())
            {
                var u = ctx.Usuarios
                    .FirstOrDefault(x => x.Username == username && x.Activo);

                if (u == null) return null;

                if (!PasswordUtil.Matches(passwordPlano, u.Password))
                {
                    return null;
                }

                return u;
            }
        }
    }
}
