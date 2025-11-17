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
    public interface IElectrodomesticoController
    {
        [OperationContract]
        Task<List<ElectrodomesticoResponse>> ListarElectrodomesticosAsync();

        [OperationContract]
        Task<ElectrodomesticoResponse> CrearElectrodomesticoAsync(ElectrodomesticoRequest dto);

        [OperationContract]
        Task<bool> EliminarElectrodomesticoAsync(string codigo);
    }

    [ServiceContract]
    public interface IFacturaController
    {
        [OperationContract]
        Task<List<FacturaResponse>> ListarFacturasAsync();

        [OperationContract]
        Task<FacturaResponse> ObtenerFacturaAsync(long id);

        [OperationContract]
        Task<FacturaResponse> CrearFacturaAsync(FacturaRequest req);
    }

    [ServiceContract]
    public interface IUsuarioComercializadoraController
    {
        [OperationContract]
        Task<List<UsuarioComercializadoraResponse>> ListarAsync();

        [OperationContract]
        Task<UsuarioComercializadoraResponse> LoginAsync(UsuarioComercializadoraRequest dto);
    }

    // ===== CLASE DE SERVICIO PRINCIPAL DE COMERCIALIZADORA =====

    public class ComercializadoraSoapService
    {
        // URL base del servidor SOAP de Comercializadora
        private const string BaseUrl = "http://192.168.100.53:62997";

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

            var endpoint = new EndpointAddress($"{BaseUrl}/ComercializadoraService.svc");
            
            System.Diagnostics.Debug.WriteLine($"[SOAP] Creando canal para: {endpoint.Uri}");
            
            return new ChannelFactory<T>(binding, endpoint);
        }

        // ===== ELECTRODOMÉSTICOS =====
        public async Task<List<ElectrodomesticoResponse>> ListarElectrodomesticosAsync()
        {
            var factory = CreateChannel<IElectrodomesticoController>("ElectrodomesticoController");
            var client = factory.CreateChannel();
            try
            {
                return await client.ListarElectrodomesticosAsync();
            }
            finally
            {
                ((IClientChannel)client).Close();
                factory.Close();
            }
        }

        public async Task<ElectrodomesticoResponse> CrearElectrodomesticoAsync(ElectrodomesticoRequest dto)
        {
            var factory = CreateChannel<IElectrodomesticoController>("ElectrodomesticoController");
            var client = factory.CreateChannel();
            try
            {
                return await client.CrearElectrodomesticoAsync(dto);
            }
            finally
            {
                ((IClientChannel)client).Close();
                factory.Close();
            }
        }

        // ===== FACTURAS =====
        public async Task<List<FacturaResponse>> ListarFacturasAsync()
        {
            var factory = CreateChannel<IFacturaController>("FacturaController");
            var client = factory.CreateChannel();
            try
            {
                return await client.ListarFacturasAsync();
            }
            finally
            {
                ((IClientChannel)client).Close();
                factory.Close();
            }
        }

        public async Task<FacturaResponse> ObtenerFacturaAsync(long id)
        {
            var factory = CreateChannel<IFacturaController>("FacturaController");
            var client = factory.CreateChannel();
            try
            {
                return await client.ObtenerFacturaAsync(id);
            }
            finally
            {
                ((IClientChannel)client).Close();
                factory.Close();
            }
        }

        public async Task<List<ElectrodomesticoResponse>> ObtenerElectrodomesticosAsync()
        {
            var factory = CreateChannel<IElectrodomesticoController>("ElectrodomesticoController");
            var client = factory.CreateChannel();
            try
            {
                return await client.ListarElectrodomesticosAsync();
            }
            finally
            {
                ((IClientChannel)client).Close();
                factory.Close();
            }
        }

        public async Task<bool> EliminarElectrodomesticoAsync(long id)
        {
            var factory = CreateChannel<IElectrodomesticoController>("ElectrodomesticoController");
            var client = factory.CreateChannel();
            try
            {
                return await client.EliminarElectrodomesticoAsync(id.ToString());
            }
            finally
            {
                ((IClientChannel)client).Close();
                factory.Close();
            }
        }

        public async Task<FacturaResponse> CrearFacturaAsync(FacturaRequest req)
        {
            var factory = CreateChannel<IFacturaController>("FacturaController");
            var client = factory.CreateChannel();
            try
            {
                return await client.CrearFacturaAsync(req);
            }
            finally
            {
                ((IClientChannel)client).Close();
                factory.Close();
            }
        }

        // ===== USUARIOS =====
        public async Task<List<UsuarioComercializadoraResponse>> ListarUsuariosAsync()
        {
            var factory = CreateChannel<IUsuarioComercializadoraController>("UsuarioController");
            var client = factory.CreateChannel();
            try
            {
                return await client.ListarAsync();
            }
            finally
            {
                ((IClientChannel)client).Close();
                factory.Close();
            }
        }

        public async Task<UsuarioComercializadoraResponse> LoginAsync(UsuarioComercializadoraRequest dto)
        {
            var factory = CreateChannel<IUsuarioComercializadoraController>("UsuarioController");
            var client = factory.CreateChannel();
            try
            {
                System.Diagnostics.Debug.WriteLine($"[SOAP COMERCIALIZADORA] Intentando login para usuario: {dto.Username}");
                System.Diagnostics.Debug.WriteLine($"[SOAP COMERCIALIZADORA] URL: {BaseUrl}/UsuarioController.svc");
                return await client.LoginAsync(dto);
            }
            catch (Exception ex)
            {
                System.Diagnostics.Debug.WriteLine($"[SOAP ERROR] {ex.GetType().Name}: {ex.Message}");
                if (ex.InnerException != null)
                {
                    System.Diagnostics.Debug.WriteLine($"[SOAP INNER ERROR] {ex.InnerException.Message}");
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
                    // Ignore cleanup errors
                }
            }
        }
    }
}
