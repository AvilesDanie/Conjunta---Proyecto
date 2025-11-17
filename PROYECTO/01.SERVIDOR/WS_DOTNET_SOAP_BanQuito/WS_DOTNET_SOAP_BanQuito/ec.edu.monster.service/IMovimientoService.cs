using System;
using System.Collections.Generic;
using WS_DOTNET_SOAP_BanQuito.ec.edu.monster.model;

namespace WS_DOTNET_SOAP_BanQuito.ec.edu.monster.service
{
    public interface IMovimientoService
    {
        List<Movimiento> ListarPorCuenta(string numCuenta);

        /// <summary>
        /// Registra DEP / RET / TRA y devuelve el movimiento principal (en TRA el de la cuenta origen).
        /// </summary>
        Movimiento RegistrarMovimiento(
            string tipo,
            decimal valor,
            DateTime? fecha,
            string numCuenta,
            string numCuentaOrigen,
            string numCuentaDestino);
    }
}
