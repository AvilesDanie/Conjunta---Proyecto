package ec.edu.monster.vista

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object AppSelection : Screen("app_selection")
    object LoginBanquito : Screen("login_banquito")
    object LoginComercializadora : Screen("login_comercializadora")
    
    // BanQuito Screens
    object HomeBanquito : Screen("home_banquito")
    object Clientes : Screen("clientes")
    object ClienteDetalle : Screen("cliente_detalle/{cedula}") {
        fun createRoute(cedula: String) = "cliente_detalle/$cedula"
    }
    object CrearCliente : Screen("crear_cliente")
    object EditarCliente : Screen("editar_cliente/{cedula}") {
        fun createRoute(cedula: String) = "editar_cliente/$cedula"
    }
    object CuentasPorCliente : Screen("cuentas_cliente/{cedula}") {
        fun createRoute(cedula: String) = "cuentas_cliente/$cedula"
    }
    object CrearCuenta : Screen("crear_cuenta/{cedula}") {
        fun createRoute(cedula: String) = "crear_cuenta/$cedula"
    }
    object Movimientos : Screen("movimientos/{numCuenta}") {
        fun createRoute(numCuenta: String) = "movimientos/$numCuenta"
    }
    object CrearMovimiento : Screen("crear_movimiento/{numCuenta}") {
        fun createRoute(numCuenta: String) = "crear_movimiento/$numCuenta"
    }
    
    // Nuevas rutas para listar todas las cuentas y gestionar créditos
    object TodasCuentas : Screen("todas_cuentas")
    object Creditos : Screen("creditos")
    object EvaluarCredito : Screen("evaluar_credito")
    object Cuotas : Screen("cuotas/{idCredito}") {
        fun createRoute(idCredito: String) = "cuotas/$idCredito"
    }
    object Usuarios : Screen("usuarios")
    object EditarUsuario : Screen("editar_usuario/{id}") {
        fun createRoute(id: Long) = "editar_usuario/$id"
    }
    
    // Comercializadora Screens
    object HomeComercializadora : Screen("home_comercializadora")
    object Electrodomesticos : Screen("electrodomesticos")
    object CrearElectrodomestico : Screen("crear_electrodomestico")
    object EditarElectrodomestico : Screen("editar_electrodomestico/{id}") {
        fun createRoute(id: Long) = "editar_electrodomestico/$id"
    }
    object Facturar : Screen("facturar")
    object Facturas : Screen("facturas")
    object FacturaDetalle : Screen("factura_detalle/{id}") {
        fun createRoute(id: Long) = "factura_detalle/$id"
    }
    object TablaAmortizacion : Screen("tabla_amortizacion/{idCredito}") {
        fun createRoute(idCredito: Long) = "tabla_amortizacion/$idCredito"
    }
}
