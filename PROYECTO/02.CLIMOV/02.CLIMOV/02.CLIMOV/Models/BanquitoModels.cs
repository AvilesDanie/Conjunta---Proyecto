using System;
using System.Collections.Generic;
using System.Runtime.Serialization;

namespace _02.CLIMOV.Models
{
    // ===== USUARIO =====
    [DataContract(Namespace = "http://schemas.datacontract.org/2004/07/WS_DOTNET_SOAP_BanQuito.ec.edu.monster.dto")]
    public class LoginRequest
    {
        [DataMember]
        public string Username { get; set; }
        [DataMember]
        public string Password { get; set; }
    }

    [DataContract(Namespace = "http://schemas.datacontract.org/2004/07/WS_DOTNET_SOAP_BanQuito.ec.edu.monster.dto")]
    public class CrearUsuarioRequest
    {
        [DataMember]
        public string Username { get; set; }
        [DataMember]
        public string Password { get; set; }
        [DataMember]
        public string Rol { get; set; }
    }

    [DataContract(Namespace = "http://schemas.datacontract.org/2004/07/WS_DOTNET_SOAP_BanQuito.ec.edu.monster.dto")]
    public class ActualizarUsuarioRequest
    {
        [DataMember]
        public string Password { get; set; }
        [DataMember]
        public string Rol { get; set; }
        [DataMember]
        public bool? Activo { get; set; }
    }

    [DataContract(Namespace = "http://schemas.datacontract.org/2004/07/WS_DOTNET_SOAP_BanQuito.ec.edu.monster.dto")]
    public class UsuarioResponse
    {
        [DataMember]
        public long Id { get; set; }
        [DataMember]
        public string Username { get; set; }
        [DataMember]
        public string Rol { get; set; }
        [DataMember]
        public bool Activo { get; set; }
    }

    [DataContract(Namespace = "http://schemas.datacontract.org/2004/07/WS_DOTNET_SOAP_BanQuito.ec.edu.monster.dto")]
    public class UsuarioListResponse
    {
        [DataMember]
        public long Id { get; set; }
        [DataMember]
        public string Username { get; set; }
        [DataMember]
        public string Rol { get; set; }
        [DataMember]
        public bool Activo { get; set; }
    }

    // ===== CLIENTE =====
    [DataContract(Namespace = "http://schemas.datacontract.org/2004/07/WS_DOTNET_SOAP_BanQuito.ec.edu.monster.dto")]
    public class ClienteRequest
    {
        [DataMember]
        public string Cedula { get; set; }
        [DataMember]
        public string Nombre { get; set; }
        [DataMember]
        public string FechaNacimiento { get; set; } // yyyy-MM-dd
        [DataMember]
        public string EstadoCivil { get; set; }
        [DataMember]
        public string TipoCuentaInicial { get; set; }
    }

    [DataContract(Namespace = "http://schemas.datacontract.org/2004/07/WS_DOTNET_SOAP_BanQuito.ec.edu.monster.dto")]
    public class ClienteResponse
    {
        [DataMember]
        public string Cedula { get; set; }
        [DataMember]
        public string Nombre { get; set; }
        [DataMember]
        public string FechaNacimiento { get; set; }
        [DataMember]
        public string EstadoCivil { get; set; }
        [DataMember]
        public string NumCuentaInicial { get; set; }
        [DataMember]
        public string TipoCuentaInicial { get; set; }
    }

    [DataContract(Namespace = "http://schemas.datacontract.org/2004/07/WS_DOTNET_SOAP_BanQuito.ec.edu.monster.dto")]
    public class ClienteOnlyResponse
    {
        [DataMember]
        public string Cedula { get; set; }
        [DataMember]
        public string Nombre { get; set; }
        [DataMember]
        public string FechaNacimiento { get; set; }
        [DataMember]
        public string EstadoCivil { get; set; }
    }

    [DataContract(Namespace = "http://schemas.datacontract.org/2004/07/WS_DOTNET_SOAP_BanQuito.ec.edu.monster.dto")]
    public class ClienteUpdateRequest
    {
        [DataMember]
        public string Nombre { get; set; }
        [DataMember]
        public string EstadoCivil { get; set; }
    }

    // ===== CUENTA =====
    [DataContract(Namespace = "http://schemas.datacontract.org/2004/07/WS_DOTNET_SOAP_BanQuito.ec.edu.monster.dto")]
    public class CuentaRequest
    {
        [DataMember]
        public string CedulaCliente { get; set; }
        [DataMember]
        public string TipoCuenta { get; set; } // AHORROS o CORRIENTE
        [DataMember]
        public decimal SaldoInicial { get; set; }
    }

    [DataContract(Namespace = "http://schemas.datacontract.org/2004/07/WS_DOTNET_SOAP_BanQuito.ec.edu.monster.dto")]
    public class CuentaResponse
    {
        [DataMember]
        public string NumCuenta { get; set; }
        [DataMember]
        public string NumeroCuenta { get; set; }
        [DataMember]
        public string CedulaCliente { get; set; }
        [DataMember]
        public string NombreCliente { get; set; }
        [DataMember]
        public string TipoCuenta { get; set; }
        [DataMember]
        public decimal Saldo { get; set; }
    }

    // ===== MOVIMIENTO =====
    [DataContract(Namespace = "http://schemas.datacontract.org/2004/07/WS_DOTNET_SOAP_BanQuito.ec.edu.monster.dto")]
    public class MovimientoRequest
    {
        [DataMember]
        public string NumCuenta { get; set; }
        [DataMember]
        public string NumeroCuenta { get; set; }
        [DataMember]
        public string Tipo { get; set; } // DEP, RET, TRA
        [DataMember]
        public string TipoMovimiento { get; set; }
        [DataMember]
        public decimal Valor { get; set; }
        [DataMember]
        public decimal Monto { get; set; }
        [DataMember]
        public string NumCuentaDestino { get; set; }
    }

    [DataContract(Namespace = "http://schemas.datacontract.org/2004/07/WS_DOTNET_SOAP_BanQuito.ec.edu.monster.dto")]
    public class MovimientoResponse
    {
        [DataMember]
        public long Id { get; set; }
        [DataMember]
        public string NumCuenta { get; set; }
        [DataMember]
        public string Tipo { get; set; }
        [DataMember]
        public string Naturaleza { get; set; } // INGRESO o EGRESO
        [DataMember]
        public decimal Valor { get; set; }
        [DataMember]
        public string Fecha { get; set; }
        [DataMember]
        public bool InternoTransferencia { get; set; }
    }

    // ===== CRÉDITO =====
    [DataContract(Namespace = "http://schemas.datacontract.org/2004/07/WS_DOTNET_SOAP_BanQuito.ec.edu.monster.dto")]
    public class SolicitudCredito
    {
        [DataMember]
        public string Cedula { get; set; }
        [DataMember]
        public string NumeroCuenta { get; set; }
        [DataMember]
        public decimal PrecioProducto { get; set; }
        [DataMember]
        public decimal Monto { get; set; }
        [DataMember]
        public int PlazoMeses { get; set; }
        [DataMember]
        public string NumCuentaCredito { get; set; }
    }

    [DataContract(Namespace = "http://schemas.datacontract.org/2004/07/WS_DOTNET_SOAP_BanQuito.ec.edu.monster.dto")]
    public class ResultadoEvaluacion
    {
        [DataMember]
        public bool SujetoCredito { get; set; }
        [DataMember]
        public decimal MontoMaximo { get; set; }
        [DataMember]
        public bool Aprobado { get; set; }
        [DataMember]
        public string Motivo { get; set; }
        [DataMember]
        public string Mensaje { get; set; }
        [DataMember]
        public decimal TasaInteres { get; set; }
        [DataMember]
        public decimal CuotaMensual { get; set; }
        [DataMember]
        public decimal TotalPagar { get; set; }
    }

    [DataContract(Namespace = "http://schemas.datacontract.org/2004/07/WS_DOTNET_SOAP_BanQuito.ec.edu.monster.dto")]
    public class CreditoResponse
    {
        [DataMember]
        public long Id { get; set; }
        [DataMember]
        public string CedulaCliente { get; set; }
        [DataMember]
        public string NombreCliente { get; set; }
        [DataMember]
        public decimal Monto { get; set; }
        [DataMember]
        public int PlazoMeses { get; set; }
        [DataMember]
        public decimal TasaAnual { get; set; }
        [DataMember]
        public string FechaAprobacion { get; set; }
        [DataMember]
        public string Estado { get; set; }
        [DataMember]
        public string NumCuentaAsociada { get; set; }
    }

    // ===== CUOTA AMORTIZACIÓN =====
    [DataContract(Namespace = "http://schemas.datacontract.org/2004/07/WS_DOTNET_SOAP_BanQuito.ec.edu.monster.dto")]
    public class CuotaResponse
    {
        [DataMember]
        public long Id { get; set; }
        [DataMember]
        public long IdCredito { get; set; }
        [DataMember]
        public int NumeroCuota { get; set; }
        [DataMember]
        public decimal ValorCuota { get; set; }
        [DataMember]
        public decimal InteresPagado { get; set; }
        [DataMember]
        public decimal CapitalPagado { get; set; }
        [DataMember]
        public decimal Saldo { get; set; }
        [DataMember]
        public string FechaVencimiento { get; set; }
        [DataMember]
        public string Estado { get; set; } // PENDIENTE, PAGADA, VENCIDA, ANULADA
    }

    [DataContract(Namespace = "http://schemas.datacontract.org/2004/07/WS_DOTNET_SOAP_BanQuito.ec.edu.monster.dto")]
    public class ActualizarCuotaRequest
    {
        [DataMember]
        public long IdCuota { get; set; }
        [DataMember]
        public string Estado { get; set; }
    }
}
