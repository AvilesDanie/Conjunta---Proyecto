using System.Collections.Generic;
using WS_DOTNET_SOAP_Comercializadora.ec.edu.monster.model;

namespace WS_DOTNET_SOAP_Comercializadora.ec.edu.monster.service
{
    public interface IUsuarioService
    {
        List<Usuario> Listar();
        Usuario Buscar(long id);
        Usuario Crear(Usuario u);
        Usuario Actualizar(Usuario u);
        bool Eliminar(long id);
        Usuario Login(string username);
    }
}
