using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using WS_DOTNET_SOAP_BanQuito.ec.edu.monster.model;

namespace WS_DOTNET_SOAP_BanQuito.ec.edu.monster.service
{
    public class MovimientoService : IMovimientoService
    {
        public List<Movimiento> ListarPorCuenta(string numCuenta)
        {
            using (var ctx = new BanquitoContext())
            {
                return ctx.Movimientos
                          .Include("Cuenta")
                          .Where(m => m.Cuenta.NumCuenta == numCuenta)
                          .OrderBy(m => m.Fecha)
                          .ToList();
            }
        }

        public Movimiento RegistrarMovimiento(
            string tipo,
            decimal valor,
            DateTime? fecha,
            string numCuenta,
            string numCuentaOrigen,
            string numCuentaDestino)
        {
            if (string.IsNullOrWhiteSpace(tipo))
                throw new ArgumentException("tipo es obligatorio");

            if (valor <= 0)
                throw new ArgumentException("valor debe ser mayor a cero");

            var tipoUpper = tipo.ToUpperInvariant();
            if (tipoUpper != "DEP" && tipoUpper != "RET" && tipoUpper != "TRA")
                throw new ArgumentException("tipo debe ser DEP, RET o TRA");

            using (var ctx = new BanquitoContext())
            {
                var f = fecha ?? DateTime.Today;

                if (tipoUpper == "DEP" || tipoUpper == "RET")
                {
                    if (string.IsNullOrWhiteSpace(numCuenta))
                        throw new ArgumentException("numCuenta es obligatorio para DEP y RET");

                    var cuenta = ctx.Cuentas.Find(numCuenta);
                    if (cuenta == null)
                        throw new InvalidOperationException("Cuenta no existe");

                    var saldoActual = cuenta.Saldo;
                    decimal nuevoSaldo;
                    string naturaleza;

                    if (tipoUpper == "DEP")
                    {
                        naturaleza = "INGRESO";
                        nuevoSaldo = saldoActual + valor;
                    }
                    else
                    {
                        naturaleza = "EGRESO";
                        if (saldoActual < valor)
                            throw new InvalidOperationException("Saldo insuficiente para el retiro");

                        nuevoSaldo = saldoActual - valor;
                    }

                    var mov = new Movimiento
                    {
                        Cuenta = cuenta,
                        Tipo = tipoUpper,
                        Naturaleza = naturaleza,
                        InternoTransferencia = false,
                        Valor = valor,
                        Fecha = f
                    };

                    cuenta.Saldo = nuevoSaldo;

                    ctx.Movimientos.Add(mov);
                    ctx.SaveChanges();

                    return mov;
                }
                else // TRA
                {
                    if (string.IsNullOrWhiteSpace(numCuentaOrigen) ||
                        string.IsNullOrWhiteSpace(numCuentaDestino))
                    {
                        throw new ArgumentException("numCuentaOrigen y numCuentaDestino son obligatorios para TRA");
                    }

                    var origen = ctx.Cuentas.Find(numCuentaOrigen);
                    var destino = ctx.Cuentas.Find(numCuentaDestino);

                    if (origen == null || destino == null)
                        throw new InvalidOperationException("Cuentas de origen o destino no existen");

                    var saldoOrigen = origen.Saldo;
                    if (saldoOrigen < valor)
                        throw new InvalidOperationException("Saldo insuficiente en cuenta origen");

                    bool esInternaMismoCliente =
                        origen.CedulaCliente == destino.CedulaCliente;

                    var movOrigen = new Movimiento
                    {
                        Cuenta = origen,
                        Tipo = "TRA",
                        Naturaleza = "EGRESO",
                        InternoTransferencia = esInternaMismoCliente,
                        Valor = valor,
                        Fecha = f
                    };

                    var movDestino = new Movimiento
                    {
                        Cuenta = destino,
                        Tipo = "TRA",
                        Naturaleza = "INGRESO",
                        InternoTransferencia = esInternaMismoCliente,
                        Valor = valor,
                        Fecha = f
                    };

                    origen.Saldo = origen.Saldo - valor;
                    destino.Saldo = destino.Saldo + valor;

                    ctx.Movimientos.Add(movOrigen);
                    ctx.Movimientos.Add(movDestino);
                    ctx.SaveChanges();

                    // Igual que en Java: devolvemos el de cuenta origen
                    return movOrigen;
                }
            }
        }
    }
}
