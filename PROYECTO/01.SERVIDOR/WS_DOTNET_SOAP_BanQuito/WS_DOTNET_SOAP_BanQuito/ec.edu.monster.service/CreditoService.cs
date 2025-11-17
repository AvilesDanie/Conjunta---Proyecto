using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Numerics;
using WS_DOTNET_SOAP_BanQuito.ec.edu.monster.dto;
using WS_DOTNET_SOAP_BanQuito.ec.edu.monster.model;

namespace WS_DOTNET_SOAP_BanQuito.ec.edu.monster.service
{
    public class CreditoService : ICreditoService
    {
        public ResultadoEvaluacionDTO EvaluarCredito(SolicitudCreditoDTO solicitud)
        {
            using (var ctx = new BanquitoContext())
            {
                var res = new ResultadoEvaluacionDTO
                {
                    Aprobado = false,
                    SujetoCredito = false,
                    MontoMaximo = 0,
                    Motivo = ""
                };

                var hoy = DateTime.Today;

                var cliente = ctx.Clientes.Find(solicitud.Cedula);
                if (cliente == null)
                {
                    res.Motivo = "El solicitante no es cliente del banco (no tiene cuenta).";
                    return res;
                }

                // 1) Movimiento INGRESO no interno en último mes
                var haceUnMes = hoy.AddMonths(-1);

                var cantIngresos = ctx.Movimientos
                    .Count(m => m.Cuenta.CedulaCliente == solicitud.Cedula
                             && m.Naturaleza == "INGRESO"
                             && !m.InternoTransferencia
                             && m.Fecha >= haceUnMes);

                if (cantIngresos == 0)
                {
                    res.Motivo = "El cliente no tiene ingresos en el último mes.";
                    return res;
                }

                // 2) Edad mínima si casado
                if (cliente.EstadoCivil != null &&
                    cliente.EstadoCivil.ToUpper() == "CASADO")
                {
                    if (!cliente.FechaNacimiento.HasValue)
                    {
                        res.Motivo = "No se puede calcular la edad del cliente.";
                        return res;
                    }

                    var edad = hoy.Year - cliente.FechaNacimiento.Value.Year;
                    if (edad < 25)
                    {
                        res.Motivo = "Cliente casado menor de 25 años.";
                        return res;
                    }
                }

                // 3) No tener crédito activo
                var cantCreditos = ctx.Creditos
                    .Count(c => c.CedulaCliente == solicitud.Cedula &&
                                (c.Estado == "APROBADO" || c.Estado == "ACTIVO"));

                if (cantCreditos > 0)
                {
                    res.Motivo = "El cliente ya tiene un crédito activo.";
                    return res;
                }

                // 4) Capacidad de ahorro
                var haceTresMeses = hoy.AddMonths(-3);

                decimal promIngresos = ObtenerPromedio(ctx, solicitud.Cedula, "INGRESO", haceTresMeses);
                decimal promEgresos = ObtenerPromedio(ctx, solicitud.Cedula, "EGRESO", haceTresMeses);

                var diferencia = promIngresos - promEgresos;
                if (diferencia <= 0)
                {
                    res.Motivo = "La capacidad de ahorro es insuficiente.";
                    return res;
                }

                var montoMax = diferencia * 0.60m * 9;

                res.SujetoCredito = true;
                res.MontoMaximo = decimal.Round(montoMax, 2);

                if (solicitud.PrecioProducto <= 0)
                {
                    res.Motivo = "Precio del producto inválido.";
                    return res;
                }

                if (solicitud.PrecioProducto <= montoMax)
                {
                    res.Aprobado = true;
                    res.Motivo = "Crédito aprobado.";
                }
                else
                {
                    res.Motivo = "El precio excede el monto máximo aprobado.";
                }

                return res;
            }
        }

        private decimal ObtenerPromedio(BanquitoContext ctx, string cedula, string naturaleza, DateTime desde)
        {
            var result = ctx.Movimientos
                .Where(m => m.Cuenta.CedulaCliente == cedula &&
                            m.Naturaleza == naturaleza &&
                            !m.InternoTransferencia &&
                            m.Fecha >= desde)
                .Select(m => (decimal?)m.Valor)
                .Average();

            return result ?? 0;
        }

        public Credito CrearCredito(SolicitudCreditoDTO solicitud)
        {
            using (var ctx = new BanquitoContext())
            {
                var eval = EvaluarCredito(solicitud);
                if (!eval.Aprobado)
                    throw new InvalidOperationException(eval.Motivo);

                var cliente = ctx.Clientes.Find(solicitud.Cedula);
                var cuenta = ctx.Cuentas.Find(solicitud.NumCuentaCredito);

                if (cuenta == null || cuenta.CedulaCliente != cliente.Cedula)
                    throw new InvalidOperationException("La cuenta no pertenece al cliente");

                var credito = new Credito
                {
                    CedulaCliente = cliente.Cedula,
                    NumCuentaAsociada = cuenta.NumCuenta,
                    Monto = solicitud.PrecioProducto,
                    PlazoMeses = solicitud.PlazoMeses,
                    TasaAnual = 16.00m,
                    FechaAprobacion = DateTime.Today,
                    Estado = "APROBADO"
                };

                ctx.Creditos.Add(credito);
                ctx.SaveChanges();

                GenerarAmortizacion(ctx, credito);

                return credito;
            }
        }

        public Credito ObtenerCredito(long id)
        {
            using (var ctx = new BanquitoContext())
            {
                return ctx.Creditos.Include("Cliente").Include("CuentaAsociada").FirstOrDefault(c => c.Id == id);
            }
        }

        private void GenerarAmortizacion(BanquitoContext ctx, Credito credito)
        {
            decimal monto = credito.Monto;
            int n = credito.PlazoMeses;
            decimal tasa = (credito.TasaAnual / 100) / 12;

            var cuota = monto * (decimal)((double)tasa / (1 - Math.Pow(1 + (double)tasa, -n)));
            cuota = decimal.Round(cuota, 2);

            var saldo = monto;
            var fechaBase = credito.FechaAprobacion ?? DateTime.Today;

            for (int k = 1; k <= n; k++)
            {
                var interes = decimal.Round(saldo * tasa, 2);
                var capital = cuota - interes;
                var nuevoSaldo = saldo - capital;

                if (k == n && nuevoSaldo != 0)
                {
                    capital = saldo;
                    cuota = interes + capital;
                    nuevoSaldo = 0;
                }

                var cu = new CuotaAmortizacion
                {
                    Id = credito.Id,
                    NumeroCuota = k,
                    ValorCuota = cuota,
                    InteresPagado = interes,
                    CapitalPagado = capital,
                    Saldo = nuevoSaldo,
                    FechaVencimiento = fechaBase.AddMonths(k),
                    Estado = "PENDIENTE"
                };

                ctx.CuotasAmortizacion.Add(cu);
                saldo = nuevoSaldo;
            }

            ctx.SaveChanges();
        }
    }
}
