using System.Collections.Generic;

namespace _02.CLIMOV.Models
{
    // ===== USUARIO COMERCIALIZADORA =====
    public class UsuarioComercializadoraRequest
    {
        public string Username { get; set; }
        public string NombreUsuario { get; set; }
        public string Password { get; set; }
        public string Contrasena { get; set; }
    }

    public class UsuarioComercializadoraResponse
    {
        public long Id { get; set; }
        public string Username { get; set; }
        public string NombreUsuario { get => Username; set => Username = value; }
        public string Rol { get; set; }
        public bool Activo { get; set; }
    }

    // ===== ELECTRODOMÉSTICO =====
    public class ElectrodomesticoRequest
    {
        public string Codigo { get; set; }
        public string Nombre { get; set; }
        public decimal PrecioVenta { get; set; }
    }

    public class ElectrodomesticoResponse
    {
        public long Id { get; set; }
        public long IdElectrodomestico { get => Id; set => Id = value; }
        public string Codigo { get; set; }
        public string Nombre { get; set; }
        public string Descripcion { get; set; }
        public decimal PrecioVenta { get; set; }
        public decimal Precio { get => PrecioVenta; set => PrecioVenta = value; }
    }

    // ===== FACTURA =====
    public class FacturaRequest
    {
        public string CedulaCliente { get; set; }
        public string NombreCliente { get; set; }
        public long IdElectrodomestico { get; set; }
        public int Cantidad { get; set; }
        public string FormaPago { get; set; } // EFECTIVO o CREDITO
        public int PlazoMeses { get; set; } // solo si formaPago = CREDITO
        public string NumCuentaCredito { get; set; } // solo si formaPago = CREDITO
    }

    public class DetalleFacturaResponse
    {
        public string CodigoElectro { get; set; }
        public string NombreElectro { get; set; }
        public int Cantidad { get; set; }
        public decimal PrecioUnitario { get; set; }
        public decimal Subtotal { get; set; }
    }

    public class FacturaResponse
    {
        public long Id { get; set; }
        public string Fecha { get; set; }
        public string NumeroFactura { get; set; }
        public string CedulaCliente { get; set; }
        public string NombreCliente { get; set; }
        public string FormaPago { get; set; }
        public decimal TotalBruto { get; set; }
        public decimal Total { get; set; }
        public decimal Descuento { get; set; }
        public decimal TotalNeto { get; set; }
        public long? IdCreditoBanquito { get; set; }
        public List<DetalleFacturaResponse> Detalles { get; set; }
    }

    // ===== MODELOS ADICIONALES PARA LA APP =====
    public class Electrodomestico
    {
        public int IdElectrodomestico { get; set; }
        public string Nombre { get; set; }
        public string Descripcion { get; set; }
        public decimal Precio { get; set; }
        public int Stock { get; set; }
    }

    public class Factura
    {
        public int IdFactura { get; set; }
        public string NumeroFactura { get; set; }
        public string CedulaCliente { get; set; }
        public string NombreCliente { get; set; }
        public string Fecha { get; set; }
        public decimal Total { get; set; }
        public List<DetalleFactura> Detalles { get; set; }
    }

    public class DetalleFactura
    {
        public int IdElectrodomestico { get; set; }
        public string NombreElectrodomestico { get; set; }
        public int Cantidad { get; set; }
        public decimal PrecioUnitario { get; set; }
        public decimal Subtotal => Cantidad * PrecioUnitario;
    }
}
