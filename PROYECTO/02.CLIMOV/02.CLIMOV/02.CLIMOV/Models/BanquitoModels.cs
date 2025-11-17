using System;
using System.Collections.Generic;

namespace _02.CLIMOV.Models
{
    // ===== USUARIO =====
    public class UsuarioRequest
    {
        public string Username { get; set; }
        public string Password { get; set; }
        public string Rol { get; set; }
        public bool? Activo { get; set; }
    }

    public class UsuarioResponse
    {
        public long Id { get; set; }
        public string Username { get; set; }
        public string Rol { get; set; }
        public bool Activo { get; set; }
    }

    // ===== CLIENTE =====
    public class ClienteRequest
    {
        public string Cedula { get; set; }
        public string Nombre { get; set; }
        public string FechaNacimiento { get; set; } // yyyy-MM-dd
        public string EstadoCivil { get; set; }
        public string TipoCuentaInicial { get; set; }
    }

    public class ClienteResponse
    {
        public string Cedula { get; set; }
        public string Nombre { get; set; }
        public string FechaNacimiento { get; set; }
        public string EstadoCivil { get; set; }
        public string NumCuentaInicial { get; set; }
        public string TipoCuentaInicial { get; set; }
    }

    public class ClienteOnlyResponse
    {
        public string Cedula { get; set; }
        public string Nombre { get; set; }
        public string FechaNacimiento { get; set; }
        public string EstadoCivil { get; set; }
    }

    public class ClienteUpdateRequest
    {
        public string Nombre { get; set; }
        public string EstadoCivil { get; set; }
    }

    // ===== CUENTA =====
    public class CuentaRequest
    {
        public string CedulaCliente { get; set; }
        public string TipoCuenta { get; set; } // AHORROS o CORRIENTE
        public decimal SaldoInicial { get; set; }
    }

    public class CuentaResponse
    {
        public string NumCuenta { get; set; }
        public string NumeroCuenta { get; set; }
        public string CedulaCliente { get; set; }
        public string NombreCliente { get; set; }
        public string TipoCuenta { get; set; }
        public decimal Saldo { get; set; }
    }

    // ===== MOVIMIENTO =====
    public class MovimientoRequest
    {
        public string NumCuenta { get; set; }
        public string NumeroCuenta { get; set; }
        public string Tipo { get; set; } // DEP, RET, TRA
        public string TipoMovimiento { get; set; }
        public decimal Valor { get; set; }
        public decimal Monto { get; set; }
        public string NumCuentaDestino { get; set; }
    }

    public class MovimientoResponse
    {
        public long Id { get; set; }
        public string NumCuenta { get; set; }
        public string Tipo { get; set; }
        public string Naturaleza { get; set; } // INGRESO o EGRESO
        public decimal Valor { get; set; }
        public string Fecha { get; set; }
        public bool InternoTransferencia { get; set; }
    }

    // ===== CRÉDITO =====
    public class SolicitudCredito
    {
        public string Cedula { get; set; }
        public string NumeroCuenta { get; set; }
        public decimal PrecioProducto { get; set; }
        public decimal Monto { get; set; }
        public int PlazoMeses { get; set; }
        public string NumCuentaCredito { get; set; }
    }

    public class ResultadoEvaluacion
    {
        public bool SujetoCredito { get; set; }
        public decimal MontoMaximo { get; set; }
        public bool Aprobado { get; set; }
        public string Motivo { get; set; }
        public string Mensaje { get; set; }
        public decimal TasaInteres { get; set; }
        public decimal CuotaMensual { get; set; }
        public decimal TotalPagar { get; set; }
    }

    public class CreditoResponse
    {
        public long Id { get; set; }
        public string CedulaCliente { get; set; }
        public string NombreCliente { get; set; }
        public decimal Monto { get; set; }
        public int PlazoMeses { get; set; }
        public decimal TasaAnual { get; set; }
        public string FechaAprobacion { get; set; }
        public string Estado { get; set; }
        public string NumCuentaAsociada { get; set; }
    }

    // ===== CUOTA AMORTIZACIÓN =====
    public class CuotaResponse
    {
        public long Id { get; set; }
        public long IdCredito { get; set; }
        public int NumeroCuota { get; set; }
        public decimal ValorCuota { get; set; }
        public decimal InteresPagado { get; set; }
        public decimal CapitalPagado { get; set; }
        public decimal Saldo { get; set; }
        public string FechaVencimiento { get; set; }
        public string Estado { get; set; } // PENDIENTE, PAGADA, VENCIDA, ANULADA
    }

    public class ActualizarCuotaRequest
    {
        public long IdCuota { get; set; }
        public string Estado { get; set; }
    }
}
