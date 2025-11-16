package ec.edu.monster.vista

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import ec.edu.monster.controlador.CuentaViewModel
import ec.edu.monster.controlador.MovimientoState
import ec.edu.monster.modelo.MovimientoRequest
import ec.edu.monster.modelo.MovimientoResponse
import ec.edu.monster.util.rememberToastHelper
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*

// Pantalla para ver movimientos de una cuenta
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovimientosScreen(
    navController: NavController,
    numCuenta: String,
    cuentaViewModel: CuentaViewModel = viewModel()
) {
    val toastHelper = rememberToastHelper()
    val movimientoState by cuentaViewModel.movimientoState.collectAsState()
    
    LaunchedEffect(numCuenta) {
        cuentaViewModel.cargarMovimientos(numCuenta)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text("Movimientos")
                        Text(numCuenta, fontSize = 14.sp, fontWeight = FontWeight.Normal)
                    }
                },
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
                onClick = { navController.navigate(Screen.CrearMovimiento.createRoute(numCuenta)) },
                containerColor = Color(0xFF388E3C)
            ) {
                Icon(Icons.Default.Add, "Nuevo Movimiento", tint = Color.White)
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
        when (val state = movimientoState) {
            is MovimientoState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            }
            
            is MovimientoState.Success -> {
                if (state.movimientos.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.History,
                                null,
                                modifier = Modifier.size(80.dp),
                                tint = Color.White.copy(alpha = 0.7f)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "No hay movimientos",
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
                        items(state.movimientos) { movimiento ->
                            MovimientoCard(movimiento)
                        }
                    }
                }
            }
            
            is MovimientoState.Error -> {
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
            
            MovimientoState.Idle -> {}
        }
        }
    }
}

@Composable
fun MovimientoCard(movimiento: MovimientoResponse) {
    val (icon, color, bgColor) = when (movimiento.tipo) {
        "DEP" -> Triple(Icons.Default.CallReceived, Color(0xFF2E7D32), Color(0xFFE8F5E9))
        "RET" -> Triple(Icons.Default.CallMade, Color(0xFFD32F2F), Color(0xFFFFEBEE))
        "TRA" -> Triple(Icons.Default.SwapHoriz, Color(0xFF1976D2), Color(0xFFE3F2FD))
        else -> Triple(Icons.Default.AttachMoney, Color(0xFF666666), Color(0xFFF5F5F5))
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color.White,
                            bgColor.copy(alpha = 0.3f)
                        )
                    )
                )
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(bgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    null,
                    tint = color,
                    modifier = Modifier.size(28.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    when (movimiento.tipo) {
                        "DEP" -> "DEPÓSITO"
                        "RET" -> "RETIRO"
                        "TRA" -> "TRANSFERENCIA"
                        else -> movimiento.tipo
                    },
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF1A1A1A)
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    movimiento.fecha,
                    fontSize = 13.sp,
                    color = Color(0xFF666666)
                )
            }
            
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "${if (movimiento.naturaleza == "INGRESO") "+" else "-"}$${String.format("%.2f", movimiento.valor)}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = if (movimiento.naturaleza == "INGRESO") Color(0xFF2E7D32) else Color(0xFFD32F2F)
                )
            }
        }
    }
}

// Pantalla para crear un nuevo movimiento
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearMovimientoScreen(
    navController: NavController,
    numCuenta: String,
    cuentaViewModel: CuentaViewModel = viewModel()
) {
    val toastHelper = rememberToastHelper()
    val scope = rememberCoroutineScope()
    
    var tipoMovimiento by remember { mutableStateOf("DEP") } // DEP, RET, TRA
    var valor by remember { mutableStateOf("") }
    var numCuentaDestino by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuevo Movimiento") },
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
                    Text("Cuenta: $numCuenta", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF212121))
                }
            }
            
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
                    Text("Tipo de Movimiento", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF212121))
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = tipoMovimiento == "DEP",
                            onClick = { tipoMovimiento = "DEP" },
                            label = { Text("Depósito") },
                            leadingIcon = if (tipoMovimiento == "DEP") {
                                { Icon(Icons.Default.CallReceived, null, Modifier.size(18.dp)) }
                            } else null
                        )
                        
                        FilterChip(
                            selected = tipoMovimiento == "RET",
                            onClick = { tipoMovimiento = "RET" },
                            label = { Text("Retiro") },
                            leadingIcon = if (tipoMovimiento == "RET") {
                                { Icon(Icons.Default.CallMade, null, Modifier.size(18.dp)) }
                            } else null
                        )
                        
                        FilterChip(
                            selected = tipoMovimiento == "TRA",
                            onClick = { tipoMovimiento = "TRA" },
                            label = { Text("Transferencia") },
                            leadingIcon = if (tipoMovimiento == "TRA") {
                                { Icon(Icons.Default.SwapHoriz, null, Modifier.size(18.dp)) }
                            } else null
                        )
                    }
                }
            }
            
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
                    Text("Detalles del Movimiento", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF212121))
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    OutlinedTextField(
                        value = valor,
                        onValueChange = { valor = it },
                        label = { Text("Monto") },
                        leadingIcon = { Icon(Icons.Default.AttachMoney, null) },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        placeholder = { Text("0.00") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color(0xFF212121),
                            unfocusedTextColor = Color(0xFF212121),
                            focusedLabelColor = Color(0xFF212121),
                            unfocusedLabelColor = Color(0xFF757575)
                        )
                    )
                    
                    if (tipoMovimiento == "TRA") {
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        OutlinedTextField(
                            value = numCuentaDestino,
                            onValueChange = { numCuentaDestino = it },
                            label = { Text("Cuenta Destino") },
                            leadingIcon = { Icon(Icons.Default.AccountBalance, null) },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Número de cuenta destino") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color(0xFF212121),
                                unfocusedTextColor = Color(0xFF212121),
                                focusedLabelColor = Color(0xFF212121),
                                unfocusedLabelColor = Color(0xFF757575)
                            )
                        )
                    }
                }
            }
            
            Button(
                onClick = {
                    if (valor.isBlank()) {
                        toastHelper.showError("Ingrese el monto")
                        return@Button
                    }
                    
                    val valorDecimal = valor.toBigDecimalOrNull()
                    if (valorDecimal == null || valorDecimal <= BigDecimal.ZERO) {
                        toastHelper.showError("Monto inválido")
                        return@Button
                    }
                    
                    if (tipoMovimiento == "TRA" && numCuentaDestino.isBlank()) {
                        toastHelper.showError("Ingrese la cuenta destino")
                        return@Button
                    }
                    
                    isLoading = true
                    scope.launch {
                        val request = MovimientoRequest(
                            numCuenta = numCuenta,
                            tipo = tipoMovimiento,
                            valor = valorDecimal,
                            numCuentaDestino = if (tipoMovimiento == "TRA") numCuentaDestino else null
                        )
                        
                        val result = cuentaViewModel.crearMovimiento(request)
                        isLoading = false
                        
                        result.fold(
                            onSuccess = { movimiento ->
                                toastHelper.showSuccess("Movimiento registrado exitosamente")
                                navController.navigateUp()
                            },
                            onFailure = { error ->
                                toastHelper.showError(error.message ?: "Error al crear movimiento")
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
                    Text("REGISTRAR MOVIMIENTO", fontWeight = FontWeight.Bold)
                }
            }
        }
        }
    }
}
