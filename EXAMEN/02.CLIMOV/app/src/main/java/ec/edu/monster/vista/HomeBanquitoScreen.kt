package ec.edu.monster.vista

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ec.edu.monster.R
import ec.edu.monster.util.SessionManager
import ec.edu.monster.util.rememberToastHelper
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeBanquitoScreen(navController: NavController) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val toastHelper = rememberToastHelper()
    val scope = rememberCoroutineScope()
    
    val username by sessionManager.username.collectAsState(initial = "")
    val userRol by sessionManager.userRol.collectAsState(initial = "")
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logo_usuario),
                            contentDescription = "Usuario",
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Column {
                            Text(
                                text = "BanQuito",
                                fontWeight = FontWeight.Bold
                            )
                            username?.let {
                                Text(
                                    text = "Bienvenido, $it",
                                    fontSize = 12.sp,
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1565C0),
                    titleContentColor = Color.White
                ),
                actions = {
                    IconButton(
                        onClick = {
                            scope.launch {
                                sessionManager.clearSession()
                                toastHelper.showInfo("Sesión cerrada")
                                navController.navigate(Screen.AppSelection.route) {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        }
                    ) {
                        Icon(
                            Icons.Default.ExitToApp,
                            contentDescription = "Cerrar sesión",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFF5F5F5),
                            Color(0xFFFFFFFF)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    MenuCard(
                        title = "Clientes",
                        icon = Icons.Default.People,
                        color = Color(0xFF1565C0),
                        onClick = { navController.navigate(Screen.Clientes.route) }
                    )
                }
                
                item {
                    MenuCard(
                        title = "Nuevo Cliente",
                        icon = Icons.Default.PersonAdd,
                        color = Color(0xFF2E7D32),
                        onClick = { navController.navigate(Screen.CrearCliente.route) }
                    )
                }
                
                item {
                    MenuCard(
                        title = "Cuentas",
                        icon = Icons.Default.AccountBalance,
                        color = Color(0xFF6A1B9A),
                        onClick = { navController.navigate(Screen.TodasCuentas.route) }
                    )
                }
                
                item {
                    MenuCard(
                        title = "Movimientos",
                        icon = Icons.Default.SwapHoriz,
                        color = Color(0xFFE65100),
                        onClick = { navController.navigate(Screen.TodasCuentas.route) }
                    )
                }
                
                item {
                    MenuCard(
                        title = "Créditos",
                        icon = Icons.Default.CreditCard,
                        color = Color(0xFFC62828),
                        onClick = { navController.navigate(Screen.Creditos.route) }
                    )
                }
                
                item {
                    MenuCard(
                        title = "Cuotas",
                        icon = Icons.Default.Receipt,
                        color = Color(0xFF00838F),
                        onClick = { navController.navigate(Screen.Creditos.route) }
                    )
                }
                
                item {
                    MenuCard(
                        title = "Usuarios",
                        icon = Icons.Default.ManageAccounts,
                        color = Color(0xFF455A64),
                        onClick = { navController.navigate(Screen.Usuarios.route) }
                    )
                }
            }
            
            // Footer con logo
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_banquito),
                    contentDescription = "Logo BanQuito",
                    modifier = Modifier.size(150.dp)
                )
            }
            }
        }
    }
}

@Composable
fun MenuCard(
    title: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = Color.White,
                    modifier = Modifier.size(56.dp)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
