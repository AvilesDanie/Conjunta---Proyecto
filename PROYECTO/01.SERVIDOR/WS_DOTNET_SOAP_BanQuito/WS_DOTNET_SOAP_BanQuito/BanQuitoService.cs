using System.Collections.Generic;
using System.ServiceModel;
using WS_DOTNET_SOAP_BanQuito.ec.edu.monster.controller;
using WS_DOTNET_SOAP_BanQuito.ec.edu.monster.dto;

namespace WS_DOTNET_SOAP_BanQuito
{
    // IMPORTANTE: esta clase NO tiene [ServiceContract],
    // los [ServiceContract] están en las interfaces que implementa.
    public class BanQuitoService :
        IUsuarioController,
        IClienteController,
        ICuentaController,
        IMovimientoController,
        ICreditoController,
        ICuotaController
    {
        // Reutilizamos tus controllers actuales internamente:
        private readonly UsuarioController _usuarios = new UsuarioController();
        private readonly ClienteController _clientes = new ClienteController();
        private readonly CuentaController _cuentas = new CuentaController();
        private readonly MovimientoController _movs = new MovimientoController();
        private readonly CreditoController _creditos = new CreditoController();
        private readonly CuotaController _cuotas = new CuotaController();

        // ========== USUARIOS ==========

        public List<UsuarioResponseDTO> ListarUsuarios()
            => _usuarios.ListarUsuarios();

        public UsuarioResponseDTO BuscarUsuario(long id)
            => _usuarios.BuscarUsuario(id);

        public UsuarioResponseDTO CrearUsuario(CrearUsuarioRequest req)
            => _usuarios.CrearUsuario(req);

        public UsuarioResponseDTO ActualizarUsuario(long id, ActualizarUsuarioRequest req)
            => _usuarios.ActualizarUsuario(id, req);

        public bool EliminarUsuario(long id)
            => _usuarios.EliminarUsuario(id);

        public UsuarioResponseDTO Login(LoginRequest req)
            => _usuarios.Login(req);

        // ========== CLIENTES ==========

        public List<ClienteResponseDTO> ListarClientes()
            => _clientes.ListarClientes();

        public ClienteOnlyResponseDTO ObtenerCliente(string cedula)
            => _clientes.ObtenerCliente(cedula);

        public ClienteResponseDTO CrearCliente(ClienteRequestDTO req)
            => _clientes.CrearCliente(req);

        public ClienteOnlyResponseDTO ActualizarCliente(string cedula, ClienteUpdateRequestDTO req)
            => _clientes.ActualizarCliente(cedula, req);

        public bool EliminarCliente(string cedula)
            => _clientes.EliminarCliente(cedula);

        // ========== CUENTAS ==========

        public List<CuentaResponseDTO> ListarCuentas()
            => _cuentas.ListarCuentas();

        public CuentaResponseDTO ObtenerCuenta(string numCuenta)
            => _cuentas.ObtenerCuenta(numCuenta);

        public List<CuentaResponseDTO> ListarCuentasPorCliente(string cedula)
            => _cuentas.ListarCuentasPorCliente(cedula);

        public CuentaResponseDTO CrearCuenta(CuentaRequestDTO req)
            => _cuentas.CrearCuenta(req);

        // ========== MOVIMIENTOS ==========

        public List<MovimientoResponseDTO> ListarMovimientosPorCuenta(string numCuenta)
            => _movs.ListarMovimientosPorCuenta(numCuenta);

        public MovimientoResponseDTO CrearMovimiento(MovimientoRequestDTO req)
            => _movs.CrearMovimiento(req);

        // ========== CRÉDITOS ==========

        public ResultadoEvaluacionDTO EvaluarCredito(SolicitudCreditoDTO solicitud)
            => _creditos.EvaluarCredito(solicitud);

        public CreditoResponseDTO CrearCredito(SolicitudCreditoDTO solicitud)
            => _creditos.CrearCredito(solicitud);

        public CreditoResponseDTO ObtenerCredito(long id)
            => _creditos.ObtenerCredito(id);

        // ========== CUOTAS ==========

        public List<CuotaResponseDTO> ListarPorCredito(long idCredito)
            => _cuotas.ListarPorCredito(idCredito);

        public CuotaResponseDTO ObtenerCuota(long id)
            => _cuotas.ObtenerCuota(id);

        public CuotaResponseDTO ActualizarCuota(long id, ActualizarCuotaDTO dto)
            => _cuotas.ActualizarCuota(id, dto);

        public string AnularCuota(long id)
            => _cuotas.AnularCuota(id);
    }
}
