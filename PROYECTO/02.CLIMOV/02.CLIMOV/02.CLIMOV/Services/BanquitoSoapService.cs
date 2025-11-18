using System;
using System.Collections.Generic;
using System.ServiceModel;
using System.ServiceModel.Channels;
using System.Threading.Tasks;
using _02.CLIMOV.Models;

namespace _02.CLIMOV.Services
{
    // ===== INTERFACES DE CONTRATOS SOAP =====

    [ServiceContract]
    public interface IClienteController
    {
        [OperationContract]
        Task<List<ClienteResponse>> ListarClientesAsync();

        [OperationContract]
        Task<ClienteOnlyResponse> ObtenerClienteAsync(string cedula);

        [OperationContract]
        Task<ClienteResponse> CrearClienteAsync(ClienteRequest req);

        [OperationContract]
        Task<ClienteOnlyResponse> ActualizarClienteAsync(string cedula, ClienteUpdateRequest req);

        [OperationContract]
        Task<bool> EliminarClienteAsync(string cedula);
    }

    [ServiceContract]
    public interface ICuentaController
    {
        [OperationContract]
        Task<List<CuentaResponse>> ListarCuentasAsync();

        [OperationContract]
        Task<CuentaResponse> ObtenerCuentaAsync(string numCuenta);

        [OperationContract]
        Task<List<CuentaResponse>> ListarCuentasPorClienteAsync(string cedula);

        [OperationContract]
        Task<CuentaResponse> CrearCuentaAsync(CuentaRequest req);

        [OperationContract]
        Task<bool> EliminarCuentaAsync(string numCuenta);
    }

    [ServiceContract]
    public interface IMovimientoController
    {
        [OperationContract]
        Task<List<MovimientoResponse>> ListarMovimientosPorCuentaAsync(string numCuenta);

        [OperationContract]
        Task<MovimientoResponse> CrearMovimientoAsync(MovimientoRequest req);
    }

    [ServiceContract]
    public interface ICreditoController
    {
        [OperationContract]
        Task<ResultadoEvaluacion> EvaluarCreditoAsync(SolicitudCredito req);

        [OperationContract]
        Task<CreditoResponse> CrearCreditoAsync(SolicitudCredito req);

        [OperationContract]
        Task<CreditoResponse> ObtenerCreditoAsync(long id);

        [OperationContract]
        Task<List<CreditoResponse>> ListarCreditosAsync();
    }

    [ServiceContract]
    public interface ICuotaController
    {
        [OperationContract]
        Task<List<CuotaResponse>> ListarPorCreditoAsync(long idCredito);

        [OperationContract]
        Task<CuotaResponse> ObtenerCuotaAsync(long id);

        [OperationContract]
        Task<CuotaResponse> ActualizarCuotaAsync(long id, ActualizarCuotaRequest req);

        [OperationContract]
        Task<bool> AnularCuotaAsync(long id);
    }

    [ServiceContract]
    public interface IUsuarioController
    {
        [OperationContract]
        Task<List<UsuarioListResponse>> ListarUsuariosAsync();

        [OperationContract]
        Task<UsuarioResponse> BuscarUsuarioAsync(long id);

        [OperationContract]
        Task<UsuarioResponse> CrearUsuarioAsync(CrearUsuarioRequest req);

        [OperationContract]
        Task<UsuarioResponse> ActualizarUsuarioAsync(long id, ActualizarUsuarioRequest req);

        [OperationContract]
        Task<bool> EliminarUsuarioAsync(long id);

        [OperationContract]
        Task<UsuarioResponse> LoginAsync(LoginRequest req);
    }

    // ===== SERVICIO PRINCIPAL =====

    public class BanquitoSoapService
    {
        // ========================================
        // CONFIGURACIÓN GLOBAL - SOLO CAMBIAR AQUÍ
        // ========================================
        private const string IP_SERVIDOR = "10.40.25.70";
        private const string PUERTO = "52582";
        
        // URLs COMPLETAS DE ENDPOINTS (SEGÚN WSDL)
        private static string URL_USUARIOS => $"http://{IP_SERVIDOR}:{PUERTO}/BanQuitoService.svc/usuarios";
        private static string URL_CLIENTES => $"http://{IP_SERVIDOR}:{PUERTO}/BanQuitoService.svc/clientes";
        private static string URL_CUENTAS => $"http://{IP_SERVIDOR}:{PUERTO}/BanQuitoService.svc/cuentas";
        private static string URL_MOVIMIENTOS => $"http://{IP_SERVIDOR}:{PUERTO}/BanQuitoService.svc/movimientos";
        private static string URL_CREDITOS => $"http://{IP_SERVIDOR}:{PUERTO}/BanQuitoService.svc/creditos";
        private static string URL_CUOTAS => $"http://{IP_SERVIDOR}:{PUERTO}/BanQuitoService.svc/cuotas";

        private static ChannelFactory<T> CreateChannel<T>(string endpointUrl)
        {
            var binding = new BasicHttpBinding
            {
                MaxReceivedMessageSize = 2147483647,
                MaxBufferSize = 2147483647,
                SendTimeout = TimeSpan.FromMinutes(2),
                ReceiveTimeout = TimeSpan.FromMinutes(2),
                OpenTimeout = TimeSpan.FromMinutes(1),
                CloseTimeout = TimeSpan.FromMinutes(1)
            };

            binding.Security.Mode = BasicHttpSecurityMode.None;

            var endpoint = new EndpointAddress(endpointUrl);
            
            System.Diagnostics.Debug.WriteLine($"[SOAP] Creando canal para: {endpoint.Uri}");
            
            return new ChannelFactory<T>(binding, endpoint);
        }

        // ===== CLIENTES =====
        public async Task<List<ClienteResponse>> ListarClientesAsync()
        {
            var factory = CreateChannel<IClienteController>(URL_CLIENTES);
            var client = factory.CreateChannel();
            try
            {
                return await client.ListarClientesAsync();
            }
            finally
            {
                ((IClientChannel)client).Close();
                factory.Close();
            }
        }

        public async Task<ClienteOnlyResponse> ObtenerClienteAsync(string cedula)
        {
            var factory = CreateChannel<IClienteController>(URL_CLIENTES);
            var client = factory.CreateChannel();
            try
            {
                return await client.ObtenerClienteAsync(cedula);
            }
            finally
            {
                ((IClientChannel)client).Close();
                factory.Close();
            }
        }

        public async Task<ClienteResponse> CrearClienteAsync(ClienteRequest req)
        {
            var factory = CreateChannel<IClienteController>(URL_CLIENTES);
            var client = factory.CreateChannel();
            try
            {
                return await client.CrearClienteAsync(req);
            }
            finally
            {
                ((IClientChannel)client).Close();
                factory.Close();
            }
        }

        public async Task<ClienteOnlyResponse> ActualizarClienteAsync(string cedula, ClienteUpdateRequest req)
        {
            var factory = CreateChannel<IClienteController>(URL_CLIENTES);
            var client = factory.CreateChannel();
            try
            {
                return await client.ActualizarClienteAsync(cedula, req);
            }
            finally
            {
                ((IClientChannel)client).Close();
                factory.Close();
            }
        }

        public async Task<bool> EliminarClienteAsync(string cedula)
        {
            var factory = CreateChannel<IClienteController>(URL_CLIENTES);
            var client = factory.CreateChannel();
            try
            {
                return await client.EliminarClienteAsync(cedula);
            }
            finally
            {
                ((IClientChannel)client).Close();
                factory.Close();
            }
        }

        // ===== CUENTAS =====
        public async Task<List<CuentaResponse>> ListarCuentasAsync()
        {
            var factory = CreateChannel<ICuentaController>(URL_CUENTAS);
            var client = factory.CreateChannel();
            try
            {
                return await client.ListarCuentasAsync();
            }
            finally
            {
                ((IClientChannel)client).Close();
                factory.Close();
            }
        }

        public async Task<CuentaResponse> ObtenerCuentaAsync(string numCuenta)
        {
            var factory = CreateChannel<ICuentaController>(URL_CUENTAS);
            var client = factory.CreateChannel();
            try
            {
                return await client.ObtenerCuentaAsync(numCuenta);
            }
            finally
            {
                ((IClientChannel)client).Close();
                factory.Close();
            }
        }

        public async Task<List<CuentaResponse>> ListarCuentasPorClienteAsync(string cedula)
        {
            var factory = CreateChannel<ICuentaController>(URL_CUENTAS);
            var client = factory.CreateChannel();
            try
            {
                return await client.ListarCuentasPorClienteAsync(cedula);
            }
            finally
            {
                ((IClientChannel)client).Close();
                factory.Close();
            }
        }

        public async Task<CuentaResponse> CrearCuentaAsync(CuentaRequest req)
        {
            var factory = CreateChannel<ICuentaController>(URL_CUENTAS);
            var client = factory.CreateChannel();
            try
            {
                return await client.CrearCuentaAsync(req);
            }
            finally
            {
                ((IClientChannel)client).Close();
                factory.Close();
            }
        }

        public async Task<bool> EliminarCuentaAsync(string numCuenta)
        {
            var factory = CreateChannel<ICuentaController>(URL_CUENTAS);
            var client = factory.CreateChannel();
            try
            {
                return await client.EliminarCuentaAsync(numCuenta);
            }
            finally
            {
                ((IClientChannel)client).Close();
                factory.Close();
            }
        }

        // ===== MOVIMIENTOS =====
        public async Task<List<MovimientoResponse>> ListarMovimientosPorCuentaAsync(string numCuenta)
        {
            var factory = CreateChannel<IMovimientoController>(URL_MOVIMIENTOS);
            var client = factory.CreateChannel();
            try
            {
                return await client.ListarMovimientosPorCuentaAsync(numCuenta);
            }
            finally
            {
                ((IClientChannel)client).Close();
                factory.Close();
            }
        }

        public async Task<List<MovimientoResponse>> ObtenerMovimientosPorCuentaAsync(string numCuenta)
        {
            return await ListarMovimientosPorCuentaAsync(numCuenta);
        }

        public async Task<MovimientoResponse> CrearMovimientoAsync(MovimientoRequest req)
        {
            var factory = CreateChannel<IMovimientoController>(URL_MOVIMIENTOS);
            var client = factory.CreateChannel();
            try
            {
                return await client.CrearMovimientoAsync(req);
            }
            finally
            {
                ((IClientChannel)client).Close();
                factory.Close();
            }
        }

        // ===== CRÉDITOS =====
        public async Task<ResultadoEvaluacion> EvaluarCreditoAsync(SolicitudCredito req)
        {
            var factory = CreateChannel<ICreditoController>(URL_CREDITOS);
            var client = factory.CreateChannel();
            try
            {
                return await client.EvaluarCreditoAsync(req);
            }
            finally
            {
                ((IClientChannel)client).Close();
                factory.Close();
            }
        }

        public async Task<CreditoResponse> CrearCreditoAsync(SolicitudCredito req)
        {
            var factory = CreateChannel<ICreditoController>(URL_CREDITOS);
            var client = factory.CreateChannel();
            try
            {
                return await client.CrearCreditoAsync(req);
            }
            finally
            {
                ((IClientChannel)client).Close();
                factory.Close();
            }
        }

        public async Task<CreditoResponse> ObtenerCreditoAsync(long id)
        {
            var factory = CreateChannel<ICreditoController>(URL_CREDITOS);
            var client = factory.CreateChannel();
            try
            {
                return await client.ObtenerCreditoAsync(id);
            }
            finally
            {
                ((IClientChannel)client).Close();
                factory.Close();
            }
        }

        public async Task<List<CreditoResponse>> ListarCreditosAsync()
        {
            var factory = CreateChannel<ICreditoController>(URL_CREDITOS);
            var client = factory.CreateChannel();
            try
            {
                return await client.ListarCreditosAsync();
            }
            finally
            {
                ((IClientChannel)client).Close();
                factory.Close();
            }
        }

        // ===== CUOTAS =====
        public async Task<List<CuotaResponse>> ListarPorCreditoAsync(long idCredito)
        {
            var factory = CreateChannel<ICuotaController>(URL_CUOTAS);
            var client = factory.CreateChannel();
            try
            {
                return await client.ListarPorCreditoAsync(idCredito);
            }
            finally
            {
                ((IClientChannel)client).Close();
                factory.Close();
            }
        }

        public async Task<List<CuotaResponse>> ObtenerCuotasPorCreditoAsync(long idCredito)
        {
            return await ListarPorCreditoAsync(idCredito);
        }

        public async Task<CuotaResponse> ObtenerCuotaAsync(long id)
        {
            var factory = CreateChannel<ICuotaController>(URL_CUOTAS);
            var client = factory.CreateChannel();
            try
            {
                return await client.ObtenerCuotaAsync(id);
            }
            finally
            {
                ((IClientChannel)client).Close();
                factory.Close();
            }
        }

        public async Task<CuotaResponse> ActualizarCuotaAsync(long id, ActualizarCuotaRequest req)
        {
            var factory = CreateChannel<ICuotaController>(URL_CUOTAS);
            var client = factory.CreateChannel();
            try
            {
                return await client.ActualizarCuotaAsync(id, req);
            }
            finally
            {
                ((IClientChannel)client).Close();
                factory.Close();
            }
        }

        public async Task<bool> AnularCuotaAsync(long id)
        {
            var factory = CreateChannel<ICuotaController>(URL_CUOTAS);
            var client = factory.CreateChannel();
            try
            {
                return await client.AnularCuotaAsync(id);
            }
            finally
            {
                ((IClientChannel)client).Close();
                factory.Close();
            }
        }

        // ===== USUARIOS =====
        public async Task<List<UsuarioListResponse>> ListarUsuariosAsync()
        {
            var factory = CreateChannel<IUsuarioController>(URL_USUARIOS);
            var client = factory.CreateChannel();
            try
            {
                return await client.ListarUsuariosAsync();
            }
            finally
            {
                ((IClientChannel)client).Close();
                factory.Close();
            }
        }

        public async Task<List<UsuarioListResponse>> ObtenerUsuariosAsync()
        {
            return await ListarUsuariosAsync();
        }

        public async Task<UsuarioResponse> LoginAsync(LoginRequest req)
        {
            // Validar que req no sea null y tenga los campos requeridos
            if (req == null)
            {
                throw new ArgumentNullException(nameof(req), "LoginRequest no puede ser null");
            }

            if (string.IsNullOrWhiteSpace(req.Username))
            {
                throw new ArgumentException("El campo Username es obligatorio", nameof(req.Username));
            }

            if (string.IsNullOrWhiteSpace(req.Password))
            {
                throw new ArgumentException("El campo Password es obligatorio", nameof(req.Password));
            }

            System.Diagnostics.Debug.WriteLine($"[SOAP LOGIN] Usuario: {req.Username}");
            System.Diagnostics.Debug.WriteLine($"[SOAP LOGIN] Password: {req.Password}");  // Mostrar contraseña para debug
            System.Diagnostics.Debug.WriteLine($"[SOAP LOGIN] Password length: {req.Password?.Length}");
            System.Diagnostics.Debug.WriteLine($"[SOAP LOGIN] URL: {URL_USUARIOS}");
            System.Diagnostics.Debug.WriteLine($"[SOAP LOGIN] LoginRequest Type: {req.GetType().FullName}");
            System.Diagnostics.Debug.WriteLine($"[SOAP LOGIN] Username IsNullOrEmpty: {string.IsNullOrEmpty(req.Username)}");
            System.Diagnostics.Debug.WriteLine($"[SOAP LOGIN] Password IsNullOrEmpty: {string.IsNullOrEmpty(req.Password)}");

            var factory = CreateChannel<IUsuarioController>(URL_USUARIOS);
            var client = factory.CreateChannel();
            try
            {
                System.Diagnostics.Debug.WriteLine($"[SOAP LOGIN] Llamando a client.LoginAsync...");
                var result = await client.LoginAsync(req);
                System.Diagnostics.Debug.WriteLine($"[SOAP LOGIN] Éxito - Usuario ID: {result?.Id}");
                System.Diagnostics.Debug.WriteLine($"[SOAP LOGIN] Username: {result?.Username}");
                System.Diagnostics.Debug.WriteLine($"[SOAP LOGIN] Rol: {result?.Rol}");
                System.Diagnostics.Debug.WriteLine($"[SOAP LOGIN] Activo: {result?.Activo}");
                System.Diagnostics.Debug.WriteLine($"[SOAP LOGIN] Result is null: {result == null}");
                return result;
            }
            catch (Exception ex)
            {
                System.Diagnostics.Debug.WriteLine($"[SOAP LOGIN ERROR] {ex.GetType().Name}: {ex.Message}");
                if (ex.InnerException != null)
                {
                    System.Diagnostics.Debug.WriteLine($"[SOAP LOGIN INNER ERROR] {ex.InnerException.Message}");
                }
                throw new Exception($"Error de conexión SOAP: {ex.Message}", ex);
            }
            finally
            {
                try
                {
                    ((IClientChannel)client).Close();
                    factory.Close();
                }
                catch
                {
                    // Ignorar errores al cerrar
                }
            }
        }
    }
}
