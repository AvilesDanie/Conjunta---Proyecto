using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using WS_DOTNET_SOAP_BanQuito.ec.edu.monster.model;

namespace WS_DOTNET_SOAP_BanQuito.ec.edu.monster.service
{
    public class CuentaService : ICuentaService
    {
        public List<Cuenta> Listar()
        {
            using (var ctx = new BanquitoContext())
            {
                // Incluimos Cliente para que esté disponible al mapear DTO
                return ctx.Cuentas.Include("Cliente").ToList();
            }
        }

        public Cuenta BuscarPorNumCuenta(string numCuenta)
        {
            using (var ctx = new BanquitoContext())
            {
                return ctx.Cuentas
                          .Include("Cliente")
                          .FirstOrDefault(c => c.NumCuenta == numCuenta);
            }
        }

        public List<Cuenta> ListarPorCliente(string cedula)
        {
            using (var ctx = new BanquitoContext())
            {
                return ctx.Cuentas
                          .Include("Cliente")
                          .Where(c => c.CedulaCliente == cedula)
                          .ToList();
            }
        }

        public Cuenta CrearCuenta(string cedulaCliente, string tipoCuenta, decimal? saldoInicial)
        {
            if (string.IsNullOrWhiteSpace(cedulaCliente) ||
                string.IsNullOrWhiteSpace(tipoCuenta))
            {
                throw new ArgumentException("cedulaCliente y tipoCuenta son obligatorios");
            }

            using (var ctx = new BanquitoContext())
            {
                var cliente = ctx.Clientes.Find(cedulaCliente);
                if (cliente == null)
                {
                    throw new InvalidOperationException("Cliente no existe");
                }

                string numCuenta = GenerarNumeroCuenta(ctx);

                var cuenta = new Cuenta
                {
                    NumCuenta = numCuenta,
                    CedulaCliente = cliente.Cedula,
                    Cliente = cliente,
                    TipoCuenta = tipoCuenta.ToUpper(),
                    Saldo = saldoInicial ?? 0m
                };

                ctx.Cuentas.Add(cuenta);
                ctx.SaveChanges();

                return cuenta;
            }
        }

        // ==== Generador de número de cuenta (10 dígitos, primero no 0) ====
        private string GenerarNumeroCuenta(BanquitoContext ctx)
        {
            var random = new Random();
            string numCuenta;
            int intentos = 0;

            do
            {
                intentos++;
                var chars = new char[10];
                chars[0] = (char)('1' + random.Next(9)); // 1-9
                for (int i = 1; i < 10; i++)
                {
                    chars[i] = (char)('0' + random.Next(10)); // 0-9
                }
                numCuenta = new string(chars);
            }
            while (ctx.Cuentas.Find(numCuenta) != null && intentos < 10);

            return numCuenta;
        }
    }
}
