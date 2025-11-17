using System;
using WS_DOTNET_SOAP_BanQuito.ec.edu.monster.dto;
using WS_DOTNET_SOAP_BanQuito.ec.edu.monster.model;

namespace WS_DOTNET_SOAP_BanQuito.ec.edu.monster.service
{
    public interface ICreditoService
    {
        ResultadoEvaluacionDTO EvaluarCredito(SolicitudCreditoDTO solicitud);
        Credito CrearCredito(SolicitudCreditoDTO solicitud);
        Credito ObtenerCredito(long id);
    }
}
