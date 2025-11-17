using System.Collections.Generic;
using WS_DOTNET_SOAP_Comercializadora.ec.edu.monster.controller;
using WS_DOTNET_SOAP_Comercializadora.ec.edu.monster.dto;

namespace WS_DOTNET_SOAP_Comercializadora
{
    // Esta clase NO lleva [ServiceContract].
    // Los [ServiceContract] están en las interfaces que implementa.
    public class ComercializadoraService :
        IUsuarioController,
        IElectrodomesticoController,
        IFacturaController
    {
        // Controladores internos
        private readonly UsuarioController _usuarios = new UsuarioController();
        private readonly ElectrodomesticoController _electros = new ElectrodomesticoController();
        private readonly FacturaController _facturas = new FacturaController();

        // ====================== USUARIOS ======================

        // Coincide con IUsuarioController.Listar()
        public List<UsuarioResponseDTO> Listar()
        {
            // delega al controlador
            return _usuarios.Listar();
        }

        // Coincide con IUsuarioController.Buscar(long id)
        public UsuarioResponseDTO Buscar(long id)
        {
            return _usuarios.Buscar(id);
        }

        // Coincide con IUsuarioController.Crear(CrearUsuarioDTO req)
        public UsuarioResponseDTO Crear(CrearUsuarioDTO req)
        {
            return _usuarios.Crear(req);
        }

        public UsuarioResponseDTO Actualizar(long id, ActualizarUsuarioDTO req)
        {
            return _usuarios.Actualizar(id, req);
        }

        public bool Eliminar(long id)
        {
            return _usuarios.Eliminar(id);
        }

        public UsuarioResponseDTO Login(LoginDTO req)
        {
            return _usuarios.Login(req);
        }

        // ================== ELECTRODOMÉSTICOS ==================

        // Ajusta estos nombres a lo que diga tu IElectrodomesticoController

        public List<ElectroResponseDTO> ListarElectrodomesticos()
        {
            return _electros.ListarElectrodomesticos();
        }

        public ElectroResponseDTO CrearElectrodomestico(ElectroRequestDTO req)
        {
            return _electros.CrearElectrodomestico(req);
        }

        // ======================= FACTURAS =======================

        public List<FacturaResponseDTO> ListarFacturas()
        {
            return _facturas.ListarFacturas();
        }

        public FacturaResponseDTO ObtenerFactura(long id)
        {
            return _facturas.ObtenerFactura(id);
        }

        public FacturaResponseDTO CrearFactura(FacturaRequestDTO req)
        {
            return _facturas.CrearFactura(req);
        }
    }
}
