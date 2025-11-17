using System;
using System.ServiceModel;

namespace WS_DOTNET_SOAP_Comercializadora.ec.edu.monster.integracion
{
    // Esta interface es la del CLIENTE,
    // pero la “Publicidad WCF” (Name/Namespace) debe coincidir con el servidor.
    [ServiceContract(
        Name = "ICreditoController",
        Namespace = "http://tempuri.org/")] // mismo namespace que el server
    public interface IBanquitoCreditoService
    {
        [OperationContract]
        ResultadoEvaluacionDTO EvaluarCredito(SolicitudCreditoDTO solicitud);

        [OperationContract]
        CreditoResponseDTO CrearCredito(SolicitudCreditoDTO solicitud);

        [OperationContract]
        CreditoResponseDTO ObtenerCredito(long id);
    }

    public class BanquitoSoapClient : IDisposable
    {
        private readonly ChannelFactory<IBanquitoCreditoService> _factory;
        private readonly IBanquitoCreditoService _proxy;

        public BanquitoSoapClient(string serviceUrl)
        {
            var isHttps = serviceUrl.StartsWith("https", StringComparison.OrdinalIgnoreCase);

            var binding = new BasicHttpBinding(
                isHttps ? BasicHttpSecurityMode.Transport : BasicHttpSecurityMode.None);

            binding.MaxReceivedMessageSize = 65536;

            var address = new EndpointAddress(serviceUrl);

            _factory = new ChannelFactory<IBanquitoCreditoService>(binding, address);
            _proxy = _factory.CreateChannel();
        }

        public ResultadoEvaluacionDTO EvaluarCredito(SolicitudCreditoDTO solicitud)
            => _proxy.EvaluarCredito(solicitud);

        public CreditoResponseDTO CrearCredito(SolicitudCreditoDTO solicitud)
            => _proxy.CrearCredito(solicitud);

        public CreditoResponseDTO ObtenerCredito(long id)
            => _proxy.ObtenerCredito(id);

        public void Dispose()
        {
            try
            {
                if (_factory.State == CommunicationState.Faulted)
                    _factory.Abort();
                else
                    _factory.Close();
            }
            catch
            {
                _factory.Abort();
            }
        }
    }
}
