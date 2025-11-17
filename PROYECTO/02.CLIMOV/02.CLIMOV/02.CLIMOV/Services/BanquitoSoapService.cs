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
        Task<ResultadoEvaluacion> EvaluarCreditoAsync(SolicitudCredito solicitud);

        [OperationContract]
        Task<CreditoResponse> CrearCreditoAsync(SolicitudCredito solicitud);

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
        Task<CuotaResponse> ActualizarCuotaAsync(long id, ActualizarCuotaRequest dto);

        [OperationContract]
        Task<string> AnularCuotaAsync(long id);
    }

    [ServiceContract]
    public interface IUsuarioController
    {
        [OperationContract]
        Task<List<UsuarioResponse>> ListarUsuariosAsync();

        [OperationContract]
        Task<UsuarioResponse> BuscarUsuarioAsync(long id);

        [OperationContract]
        Task<UsuarioResponse> CrearUsuarioAsync(UsuarioRequest req);

        [OperationContract]
        Task<UsuarioResponse> ActualizarUsuarioAsync(long id, UsuarioRequest req);

        [OperationContract]
        Task<bool> EliminarUsuarioAsync(long id);

        [OperationContract]
        Task<UsuarioResponse> LoginAsync(UsuarioRequest req);
    }

    // ===== CLASE DE SERVICIO PRINCIPAL DE BANQUITO =====

    public class BanquitoSoapService
    {
        // URL base del servidor SOAP de BanQuito
        private const string BaseUrl = "http://192.168.100.53:52582";

        private static ChannelFactory<T> CreateChannel<T>(string serviceName)
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

            // Permitir HTTP sin seguridad
            binding.Security.Mode = BasicHttpSecurityMode.None;

            var endpoint = new EndpointAddress($"{BaseUrl}/BanQuitoService.svc");
            
            System.Diagnostics.Debug.WriteLine($"[SOAP] Creando canal para: {endpoint.Uri}");
            
            return new ChannelFactory<T>(binding, endpoint);
        }

        // ===== CLIENTES =====
        public async Task<List<ClienteResponse>> ListarClientesAsync()
        {
            var factory = CreateChannel<IClienteController>("ClienteController");
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
            var factory = CreateChannel<IClienteController>("ClienteController");
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
            var factory = CreateChannel<IClienteController>("ClienteController");
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
            var factory = CreateChannel<IClienteController>("ClienteController");
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
            var factory = CreateChannel<IClienteController>("ClienteController");
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
            var factory = CreateChannel<ICuentaController>("CuentaController");
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
            var factory = CreateChannel<ICuentaController>("CuentaController");
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
            var factory = CreateChannel<ICuentaController>("CuentaController");
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
            var factory = CreateChannel<ICuentaController>("CuentaController");
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

        // ===== MOVIMIENTOS =====
        public async Task<List<MovimientoResponse>> ListarMovimientosPorCuentaAsync(string numCuenta)
        {
            var factory = CreateChannel<IMovimientoController>("MovimientoController");
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

        public async Task<MovimientoResponse> CrearMovimientoAsync(MovimientoRequest req)
        {
            var factory = CreateChannel<IMovimientoController>("MovimientoController");
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
        public async Task<ResultadoEvaluacion> EvaluarCreditoAsync(SolicitudCredito solicitud)
        {
            var factory = CreateChannel<ICreditoController>("CreditoController");
            var client = factory.CreateChannel();
            try
            {
                return await client.EvaluarCreditoAsync(solicitud);
            }
            finally
            {
                ((IClientChannel)client).Close();
                factory.Close();
            }
        }

        public async Task<CreditoResponse> CrearCreditoAsync(SolicitudCredito solicitud)
        {
            var factory = CreateChannel<ICreditoController>("CreditoController");
            var client = factory.CreateChannel();
            try
            {
                return await client.CrearCreditoAsync(solicitud);
            }
            finally
            {
                ((IClientChannel)client).Close();
                factory.Close();
            }
        }

        public async Task<List<CreditoResponse>> ListarCreditosAsync()
        {
            var factory = CreateChannel<ICreditoController>("CreditoController");
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

        public async Task<CreditoResponse> ObtenerCreditoAsync(long id)
        {
            var factory = CreateChannel<ICreditoController>("CreditoController");
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

        // ===== CUOTAS =====
        public async Task<List<CuotaResponse>> ListarPorCreditoAsync(long idCredito)
        {
            var factory = CreateChannel<ICuotaController>("CuotaController");
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

        public async Task<CuotaResponse> ObtenerCuotaAsync(long id)
        {
            var factory = CreateChannel<ICuotaController>("CuotaController");
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

        public async Task<CuotaResponse> ActualizarCuotaAsync(long id, ActualizarCuotaRequest dto)
        {
            var factory = CreateChannel<ICuotaController>("CuotaController");
            var client = factory.CreateChannel();
            try
            {
                return await client.ActualizarCuotaAsync(id, dto);
            }
            finally
            {
                ((IClientChannel)client).Close();
                factory.Close();
            }
        }

        public async Task<string> AnularCuotaAsync(long id)
        {
            var factory = CreateChannel<ICuotaController>("CuotaController");
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
        public async Task<List<UsuarioResponse>> ListarUsuariosAsync()
        {
            var factory = CreateChannel<IUsuarioController>("UsuarioController");
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

        public async Task<UsuarioResponse> LoginAsync(UsuarioRequest req)
        {
            // PRIMERO: Verificar conectividad HTTP básica
            try
            {
                System.Diagnostics.Debug.WriteLine($"[SOAP TEST] Verificando conectividad a {BaseUrl}");
                using (var httpClient = new System.Net.Http.HttpClient())
                {
                    httpClient.Timeout = TimeSpan.FromSeconds(10);
                    var testUrl = $"{BaseUrl}/BanQuitoService.svc";
                    System.Diagnostics.Debug.WriteLine($"[SOAP TEST] Probando GET a {testUrl}");
                    
                    var response = await httpClient.GetAsync(testUrl);
                    System.Diagnostics.Debug.WriteLine($"[SOAP TEST] Respuesta HTTP: {response.StatusCode}");
                }
            }
            catch (Exception testEx)
            {
                System.Diagnostics.Debug.WriteLine($"[SOAP TEST ERROR] {testEx.GetType().Name}: {testEx.Message}");
                if (testEx.InnerException != null)
                {
                    System.Diagnostics.Debug.WriteLine($"[SOAP TEST ERROR INNER] {testEx.InnerException.GetType().Name}: {testEx.InnerException.Message}");
                    if (testEx.InnerException.InnerException != null)
                    {
                        System.Diagnostics.Debug.WriteLine($"[SOAP TEST ERROR INNER2] {testEx.InnerException.InnerException.Message}");
                    }
                }
                throw new Exception($"No se puede conectar al servidor {BaseUrl}. Verifique:\n1. Que el servidor esté corriendo\n2. Que esté en la misma red WiFi\n3. Que la IP sea correcta\n\nError técnico: {testEx.Message}", testEx);
            }

            var factory = CreateChannel<IUsuarioController>("UsuarioController");
            var client = factory.CreateChannel();
            try
            {
                System.Diagnostics.Debug.WriteLine($"[SOAP] Intentando login para usuario: {req.Username}");
                System.Diagnostics.Debug.WriteLine($"[SOAP] URL: {BaseUrl}/BanQuitoService.svc");
                return await client.LoginAsync(req);
            }
            catch (Exception ex)
            {
                System.Diagnostics.Debug.WriteLine($"[SOAP ERROR] {ex.GetType().Name}: {ex.Message}");
                if (ex.InnerException != null)
                {
                    System.Diagnostics.Debug.WriteLine($"[SOAP INNER ERROR] {ex.InnerException.Message}");
                }
                throw new Exception($"Error de conexión SOAP: {ex.Message}\n\nVerifique que el servidor esté corriendo en {BaseUrl}", ex);
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
                    // Ignore cleanup errors
                }
            }
        }

        public async Task<UsuarioResponse> BuscarUsuarioAsync(long id)
        {
            var factory = CreateChannel<IUsuarioController>("UsuarioController");
            var client = factory.CreateChannel();
            try
            {
                return await client.BuscarUsuarioAsync(id);
            }
            finally
            {
                ((IClientChannel)client).Close();
                factory.Close();
            }
        }

        public async Task<UsuarioResponse> CrearUsuarioAsync(UsuarioRequest req)
        {
            var factory = CreateChannel<IUsuarioController>("UsuarioController");
            var client = factory.CreateChannel();
            try
            {
                return await client.CrearUsuarioAsync(req);
            }
            finally
            {
                ((IClientChannel)client).Close();
                factory.Close();
            }
        }

        public async Task<List<UsuarioResponse>> ObtenerUsuariosAsync()
        {
            var factory = CreateChannel<IUsuarioController>("UsuarioController");
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

        public async Task<List<MovimientoResponse>> ObtenerMovimientosPorCuentaAsync(string numCuenta)
        {
            var factory = CreateChannel<IMovimientoController>("MovimientoController");
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

        public async Task<List<CuotaResponse>> ObtenerCuotasPorCreditoAsync(long idCredito)
        {
            var factory = CreateChannel<ICuotaController>("CuotaController");
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

        public async Task<bool> EliminarCuentaAsync(string numCuenta)
        {
            var factory = CreateChannel<ICuentaController>("CuentaController");
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

        public async Task<UsuarioResponse> ActualizarUsuarioAsync(long id, UsuarioRequest req)
        {
            var factory = CreateChannel<IUsuarioController>("UsuarioController");
            var client = factory.CreateChannel();
            try
            {
                return await client.ActualizarUsuarioAsync(id, req);
            }
            finally
            {
                ((IClientChannel)client).Close();
                factory.Close();
            }
        }

        public async Task<bool> EliminarUsuarioAsync(long id)
        {
            var factory = CreateChannel<IUsuarioController>("UsuarioController");
            var client = factory.CreateChannel();
            try
            {
                return await client.EliminarUsuarioAsync(id);
            }
            finally
            {
                ((IClientChannel)client).Close();
                factory.Close();
            }
        }
    }
}
