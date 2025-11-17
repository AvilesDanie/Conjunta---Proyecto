using System;
using System.Collections.Generic;
using System.Linq;
using WS_DOTNET_SOAP_BanQuito.ec.edu.monster.model;

namespace WS_DOTNET_SOAP_BanQuito.ec.edu.monster.service
{
    public class ClienteService : IClienteService
    {
        public List<Cliente> Listar()
        {
            using (var ctx = new BanquitoContext())
            {
                return ctx.Clientes.ToList();
            }
        }

        public Cliente BuscarPorCedula(string cedula)
        {
            using (var ctx = new BanquitoContext())
            {
                return ctx.Clientes.Find(cedula);
            }
        }

        public Cliente CrearCliente(string cedula, string nombre, string estadoCivil,
                                    DateTime? fechaNacimiento, string tipoCuentaInicial,
                                    out Cuenta cuentaInicial)
        {
            if (string.IsNullOrWhiteSpace(cedula) ||
                string.IsNullOrWhiteSpace(nombre) ||
                string.IsNullOrWhiteSpace(tipoCuentaInicial))
            {
                throw new ArgumentException("cedula, nombre y tipoCuentaInicial son obligatorios");
            }

            if (cedula.Length != 10)
            {
                throw new ArgumentException("La cédula debe tener 10 dígitos");
            }

            using (var ctx = new BanquitoContext())
            {
                if (ctx.Clientes.Find(cedula) != null)
                {
                    throw new InvalidOperationException("Ya existe un cliente con esa cédula");
                }

                var cliente = new Cliente
                {
                    Cedula = cedula,
                    Nombre = nombre,
                    EstadoCivil = estadoCivil
                };

                if (fechaNacimiento.HasValue)
                {
                    cliente.FechaNacimiento = fechaNacimiento.Value;
                }

                string numCuenta = GenerarNumeroCuenta(ctx);

                var cuenta = new Cuenta
                {
                    NumCuenta = numCuenta,
                    Cliente = cliente,
                    TipoCuenta = tipoCuentaInicial.ToUpper(),
                    Saldo = 0m
                };

                ctx.Clientes.Add(cliente);
                ctx.Cuentas.Add(cuenta);
                ctx.SaveChanges();

                cuentaInicial = cuenta;
                return cliente;
            }
        }

        public Cliente ActualizarCliente(string cedula, string nombre, string estadoCivil)
        {
            using (var ctx = new BanquitoContext())
            {
                var cliente = ctx.Clientes.Find(cedula);
                if (cliente == null)
                    return null;

                if (!string.IsNullOrWhiteSpace(nombre))
                    cliente.Nombre = nombre;

                if (!string.IsNullOrWhiteSpace(estadoCivil))
                    cliente.EstadoCivil = estadoCivil;

                ctx.SaveChanges();
                return cliente;
            }
        }

        public bool EliminarCliente(string cedula)
        {
            using (var ctx = new BanquitoContext())
            {
                var cliente = ctx.Clientes.Find(cedula);
                if (cliente == null)
                    return false;

                ctx.Clientes.Remove(cliente);
                ctx.SaveChanges();
                return true;
            }
        }

        // ==== Generador de número de cuenta ====
        private string GenerarNumeroCuenta(BanquitoContext ctx)
        {
            var random = new Random();
            string numCuenta = null;
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

            } while (ctx.Cuentas.Find(numCuenta) != null && intentos < 10);

            return numCuenta;
        }
    }
}
