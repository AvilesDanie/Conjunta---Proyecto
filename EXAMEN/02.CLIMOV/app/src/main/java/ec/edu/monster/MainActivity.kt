package ec.edu.monster

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ec.edu.monster.ui.theme._02CLIMOVTheme
import ec.edu.monster.vista.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _02CLIMOVTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        // Splash y selección
        composable(Screen.Splash.route) {
            SplashScreen(navController)
        }
        
        composable(Screen.AppSelection.route) {
            AppSelectionScreen(navController)
        }
        
        // Login screens
        composable(Screen.LoginBanquito.route) {
            LoginBanquitoScreen(navController)
        }
        
        composable(Screen.LoginComercializadora.route) {
            LoginComercializadoraScreen(navController)
        }
        
        // BanQuito screens
        composable(Screen.HomeBanquito.route) {
            HomeBanquitoScreen(navController)
        }
        
        composable(Screen.Clientes.route) {
            ClientesScreen(navController)
        }
        
        composable(Screen.CrearCliente.route) {
            CrearClienteScreen(navController)
        }
        
        composable(
            route = Screen.EditarCliente.route,
            arguments = listOf(navArgument("cedula") { type = NavType.StringType })
        ) { backStackEntry ->
            val cedula = backStackEntry.arguments?.getString("cedula") ?: ""
            EditarClienteScreen(navController, cedula)
        }
        
        composable(
            route = Screen.ClienteDetalle.route,
            arguments = listOf(navArgument("cedula") { type = NavType.StringType })
        ) { backStackEntry ->
            val cedula = backStackEntry.arguments?.getString("cedula") ?: ""
            ClienteDetalleScreen(navController, cedula)
        }
        
        composable(
            route = Screen.CuentasPorCliente.route,
            arguments = listOf(navArgument("cedula") { type = NavType.StringType })
        ) { backStackEntry ->
            val cedula = backStackEntry.arguments?.getString("cedula") ?: ""
            CuentasPorClienteScreen(navController, cedula)
        }
        
        composable(
            route = Screen.CrearCuenta.route,
            arguments = listOf(navArgument("cedula") { type = NavType.StringType })
        ) { backStackEntry ->
            val cedula = backStackEntry.arguments?.getString("cedula") ?: ""
            CrearCuentaScreen(navController, cedula)
        }
        
        composable(
            route = Screen.Movimientos.route,
            arguments = listOf(navArgument("numCuenta") { type = NavType.StringType })
        ) { backStackEntry ->
            val numCuenta = backStackEntry.arguments?.getString("numCuenta") ?: ""
            MovimientosScreen(navController, numCuenta)
        }
        
        composable(
            route = Screen.CrearMovimiento.route,
            arguments = listOf(navArgument("numCuenta") { type = NavType.StringType })
        ) { backStackEntry ->
            val numCuenta = backStackEntry.arguments?.getString("numCuenta") ?: ""
            CrearMovimientoScreen(navController, numCuenta)
        }
        
        // Nuevas pantallas de BanQuito
        composable(Screen.TodasCuentas.route) {
            TodasCuentasScreen(navController)
        }
        
        composable(Screen.Creditos.route) {
            CreditosScreen(navController)
        }
        
        composable(Screen.EvaluarCredito.route) {
            EvaluarCreditoScreen(navController)
        }
        
        composable(Screen.Cuotas.route) { backStackEntry ->
            val idCredito = backStackEntry.arguments?.getString("idCredito") ?: return@composable
            CuotasScreen(navController, idCredito)
        }
        
        composable(Screen.Usuarios.route) {
            UsuariosScreen(navController)
        }
        
        // Comercializadora screens
        composable(Screen.HomeComercializadora.route) {
            HomeComercializadoraScreen(navController)
        }
        
        composable(Screen.Electrodomesticos.route) {
            ElectrodomesticosScreen(navController)
        }
        
        composable(Screen.CrearElectrodomestico.route) {
            CrearElectrodomesticoScreen(navController)
        }
        
        composable(Screen.Facturar.route) {
            FacturarScreen(navController)
        }
        
        composable(Screen.Facturas.route) {
            FacturasScreen(navController)
        }
        
        composable(
            route = Screen.FacturaDetalle.route,
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id") ?: 0L
            FacturaDetalleScreen(navController, id)
        }
        
        composable(
            route = Screen.TablaAmortizacion.route,
            arguments = listOf(navArgument("idCredito") { type = NavType.LongType })
        ) { backStackEntry ->
            val idCredito = backStackEntry.arguments?.getLong("idCredito") ?: 0L
            TablaAmortizacionScreen(navController, idCredito)
        }
        
        composable(
            route = Screen.EditarUsuario.route,
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id") ?: 0L
            EditarUsuarioScreen(navController, id)
        }
    }
}
