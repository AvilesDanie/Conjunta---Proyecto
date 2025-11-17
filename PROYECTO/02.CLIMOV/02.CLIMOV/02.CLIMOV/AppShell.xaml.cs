using _02.CLIMOV.Views;

namespace _02.CLIMOV
{
    public partial class AppShell : Shell
    {
        public AppShell()
        {
            InitializeComponent();

            // Registrar todas las rutas de navegación
            Routing.RegisterRoute("app_selection", typeof(AppSelectionScreen));
            Routing.RegisterRoute("login_banquito", typeof(LoginBanquitoScreen));
            Routing.RegisterRoute("login_comercializadora", typeof(LoginComercializadoraScreen));
            
            // Rutas BanQuito
            Routing.RegisterRoute("home_banquito", typeof(HomeBanquitoScreen));
            Routing.RegisterRoute("clientes", typeof(ClientesScreen));
            Routing.RegisterRoute("crear_cliente", typeof(CrearClienteScreen));
            Routing.RegisterRoute("cliente_detalle", typeof(ClienteDetalleScreen));
            Routing.RegisterRoute("editar_cliente", typeof(EditarClienteScreen));
            Routing.RegisterRoute("cuentas_cliente", typeof(CuentasPorClienteScreen));
            Routing.RegisterRoute("crear_cuenta", typeof(CrearCuentaScreen));
            Routing.RegisterRoute("todas_cuentas", typeof(TodasCuentasScreen));
            Routing.RegisterRoute("movimientos", typeof(MovimientosScreen));
            Routing.RegisterRoute("crear_movimiento", typeof(CrearMovimientoScreen));
            Routing.RegisterRoute("creditos", typeof(CreditosScreen));
            Routing.RegisterRoute("evaluar_credito", typeof(EvaluarCreditoScreen));
            Routing.RegisterRoute("cuotas", typeof(CuotasScreen));
            Routing.RegisterRoute("tabla_amortizacion", typeof(TablaAmortizacionScreen));
            Routing.RegisterRoute("usuarios", typeof(UsuariosScreen));
            
            // Rutas Comercializadora
            Routing.RegisterRoute("home_comercializadora", typeof(HomeComercializadoraScreen));
            Routing.RegisterRoute("electrodomesticos", typeof(ElectrodomesticosScreen));
            Routing.RegisterRoute("crear_electrodomestico", typeof(CrearElectrodomesticoScreen));
            Routing.RegisterRoute("editar_electrodomestico", typeof(CrearElectrodomesticoScreen));
            Routing.RegisterRoute("facturar", typeof(FacturarScreen));
            Routing.RegisterRoute("facturas", typeof(FacturasScreen));
            Routing.RegisterRoute("factura_detalle", typeof(FacturaDetalleScreen));
        }
    }
}
