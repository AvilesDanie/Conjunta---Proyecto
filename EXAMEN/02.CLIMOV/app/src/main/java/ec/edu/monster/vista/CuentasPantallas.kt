package ec.edu.monster.vista

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ec.edu.monster.controlador.CuentaState
import ec.edu.monster.controlador.CuentaViewModel
import ec.edu.monster.modelo.CuentaRequest
import ec.edu.monster.modelo.CuentaResponse
import ec.edu.monster.util.rememberToastHelper
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*

// Pantalla para ver cuentas de un cliente específico
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CuentasPorClienteScreen(
    navController: NavController,
    cedula: String,
    cuentaViewModel: CuentaViewModel = viewModel()
) {
    val toastHelper = rememberToastHelper()
    val cuentaState by cuentaViewModel.cuentaState.collectAsState()
    
    LaunchedEffect(cedula) {
        cuentaViewModel.cargarCuentasPorCliente(cedula)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cuentas del Cliente") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1565C0),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.CrearCuenta.createRoute(cedula)) },
                containerColor = Color(0xFF388E3C)
            ) {
                Icon(Icons.Default.Add, "Nueva Cuenta", tint = Color.White)
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF1A237E),
                            Color(0xFF283593),
                            Color(0xFF303F9F)
                        )
                    )
                )
                .padding(paddingValues)
        ) {
        when (val state = cuentaState) {
            is CuentaState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            }
            
            is CuentaState.Success -> {
                if (state.cuentas.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.AccountBalance,
                                null,
                                modifier = Modifier.size(80.dp),
                                tint = Color.White.copy(alpha = 0.7f)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "No hay cuentas registradas",
                                color = Color.White,
                                fontSize = 18.sp
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.cuentas) { cuenta ->
                            CuentaCard(cuenta, navController)
                        }
                    }
                }
            }
            
            is CuentaState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.Error,
                            null,
                            modifier = Modifier.size(64.dp),
                            tint = Color(0xFFFF6B6B)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            state.message,
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }
                }
            }
            
            CuentaState.Idle -> {}
        }
        }
    }
}

@Composable
fun CuentaCard(cuenta: CuentaResponse, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate(Screen.Movimientos.createRoute(cuenta.numCuenta))
            },
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF1565C0),
                            Color(0xFF1976D2)
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF1565C0).copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.AccountBalance,
                            null,
                            tint = Color(0xFF1565C0),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            cuenta.numCuenta,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            cuenta.nombreCliente,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Tipo: ${cuenta.tipoCuenta}",
                            color = Color.White,
                            fontSize = 13.sp
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Saldo",
                    fontSize = 14.sp,
                    color = Color.White
                )
                Text(
                    "$${String.format("%.2f", cuenta.saldo)}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (cuenta.saldo >= BigDecimal.ZERO) Color(0xFF66BB6A) else Color(0xFFEF5350)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = { navController.navigate(Screen.Movimientos.createRoute(cuenta.numCuenta)) }
                ) {
                    Icon(Icons.Default.History, null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Ver Movimientos")
                }
            }
        }
    }
}

// Pantalla para crear una nueva cuenta
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearCuentaScreen(
    navController: NavController,
    cedula: String,
    cuentaViewModel: CuentaViewModel = viewModel()
) {
    val toastHelper = rememberToastHelper()
    val scope = rememberCoroutineScope()
    
    var tipoCuenta by remember { mutableStateOf("AHORROS") }
    var saldoInicial by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Nueva Cuenta") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1565C0),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF1A237E),
                            Color(0xFF283593),
                            Color(0xFF303F9F)
                        )
                    )
                )
        ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(Color.White, Color(0xFFF5F5FF))
                            )
                        )
                        .padding(20.dp)
                ) {
                    Text("Información de la Cuenta", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF212121))
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Text("Tipo de Cuenta", fontWeight = FontWeight.Bold, color = Color(0xFF212121))
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = tipoCuenta == "AHORROS",
                            onClick = { tipoCuenta = "AHORROS" },
                            label = { Text("Ahorros") },
                            leadingIcon = if (tipoCuenta == "AHORROS") {
                                { Icon(Icons.Default.CheckCircle, null, Modifier.size(18.dp)) }
                            } else null
                        )
                        
                        FilterChip(
                            selected = tipoCuenta == "CORRIENTE",
                            onClick = { tipoCuenta = "CORRIENTE" },
                            label = { Text("Corriente") },
                            leadingIcon = if (tipoCuenta == "CORRIENTE") {
                                { Icon(Icons.Default.CheckCircle, null, Modifier.size(18.dp)) }
                            } else null
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    OutlinedTextField(
                        value = saldoInicial,
                        onValueChange = { saldoInicial = it },
                        label = { Text("Saldo Inicial (Opcional)") },
                        leadingIcon = { Icon(Icons.Default.AttachMoney, null) },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color(0xFF212121),
                            unfocusedTextColor = Color(0xFF212121),
                            focusedLabelColor = Color(0xFF1565C0),
                            unfocusedLabelColor = Color(0xFF757575)
                        )
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        "El número de cuenta se generará automáticamente.",
                        fontSize = 14.sp,
                        color = Color(0xFF757575)
                    )
                }
            }
            
            Button(
                onClick = {
                    isLoading = true
                    scope.launch {
                        val saldoInicialValue = saldoInicial.toBigDecimalOrNull()
                        
                        val request = CuentaRequest(
                            cedulaCliente = cedula,
                            tipoCuenta = tipoCuenta,
                            saldoInicial = saldoInicialValue
                        )
                        
                        val result = cuentaViewModel.crearCuenta(request)
                        isLoading = false
                        
                        result.fold(
                            onSuccess = {
                                toastHelper.showSuccess("Cuenta creada exitosamente")
                                navController.navigateUp()
                            },
                            onFailure = { error ->
                                toastHelper.showError(error.message ?: "Error al crear cuenta")
                            }
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(Color(0xFF388E3C))
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Icon(Icons.Default.Save, null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("CREAR CUENTA", fontWeight = FontWeight.Bold)
                }
            }
        }
        }
    }
}
