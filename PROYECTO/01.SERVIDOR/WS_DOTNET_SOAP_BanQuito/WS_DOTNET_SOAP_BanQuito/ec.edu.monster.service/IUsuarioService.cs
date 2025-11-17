using global::WS_DOTNET_SOAP_BanQuito.ec.edu.monster.model;
using System.Collections.Generic;

namespace WS_DOTNET_SOAP_BanQuito.ec.edu.monster.service

{
    public interface IUsuarioService
    {
        List<Usuario> Listar();
        Usuario BuscarPorId(long id);
        Usuario Crear(string username, string passwordPlano, string rol);
        Usuario Actualizar(long id, string passwordPlano, string rol, bool? activo);
        bool Eliminar(long id);
        Usuario Login(string username, string passwordPlano);
    }
}
