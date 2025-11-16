package ec.edu.monster.vista

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import ec.edu.monster.controlador.ClienteDetailState
import ec.edu.monster.controlador.ClienteViewModel
import ec.edu.monster.modelo.ClienteRequest
import ec.edu.monster.util.rememberToastHelper
import kotlinx.coroutines.launch
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearClienteScreen(
    navController: NavController,
    viewModel: ClienteViewModel = viewModel()
) {
    val toastHelper = rememberToastHelper()
    val scope = rememberCoroutineScope()
    
    var cedula by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf("") }
    var estadoCivil by remember { mutableStateOf("SOLTERO") }
    var tipoCuenta by remember { mutableStateOf("AHORROS") }
    var numeroCuenta by remember { mutableStateOf("") }
    var saldo by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuevo Cliente") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1976D2),
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
                    Text(
                        "Datos del Cliente",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color(0xFF212121)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = cedula,
                onValueChange = { cedula = it },
                label = { Text("Cédula *") },
                leadingIcon = { Icon(Icons.Default.Badge, null) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color(0xFF212121),
                    unfocusedTextColor = Color(0xFF212121),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )
            
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre Completo *") },
                leadingIcon = { Icon(Icons.Default.Person, null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color(0xFF212121),
                    unfocusedTextColor = Color(0xFF212121),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )
            
            OutlinedTextField(
                value = fechaNacimiento,
                onValueChange = { fechaNacimiento = it },
                label = { Text("Fecha Nacimiento (YYYY-MM-DD) *") },
                leadingIcon = { Icon(Icons.Default.CalendarToday, null) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("1990-01-01") },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color(0xFF212121),
                    unfocusedTextColor = Color(0xFF212121),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )
            
            // Estado Civil Selector
            var expandedEstadoCivil by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedEstadoCivil,
                onExpandedChange = { expandedEstadoCivil = it }
            ) {
                OutlinedTextField(
                    value = estadoCivil,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Estado Civil *") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedEstadoCivil) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expandedEstadoCivil,
                    onDismissRequest = { expandedEstadoCivil = false }
                ) {
                    listOf("SOLTERO", "CASADO", "DIVORCIADO", "VIUDO").forEach { estado ->
                        DropdownMenuItem(
                            text = { Text(estado) },
                            onClick = {
                                estadoCivil = estado
                                expandedEstadoCivil = false
                            }
                        )
                    }
                }
            }
            
            // Tipo de Cuenta Selector
            var expandedTipoCuenta by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedTipoCuenta,
                onExpandedChange = { expandedTipoCuenta = it }
            ) {
                OutlinedTextField(
                    value = tipoCuenta,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tipo de Cuenta *") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedTipoCuenta) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expandedTipoCuenta,
                    onDismissRequest = { expandedTipoCuenta = false }
                ) {
                    listOf("AHORROS", "CORRIENTE").forEach { tipo ->
                        DropdownMenuItem(
                            text = { Text(tipo) },
                            onClick = {
                                tipoCuenta = tipo
                                expandedTipoCuenta = false
                            }
                        )
                    }
                }
            }
            
            OutlinedTextField(
                value = numeroCuenta,
                onValueChange = { numeroCuenta = it },
                label = { Text("Número de Cuenta (Opcional)") },
                leadingIcon = { Icon(Icons.Default.AccountBalance, null) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Se generará automáticamente si se deja vacío") },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color(0xFF212121),
                    unfocusedTextColor = Color(0xFF212121),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )
            
            OutlinedTextField(
                value = saldo,
                onValueChange = { saldo = it },
                label = { Text("Saldo Inicial *") },
                leadingIcon = { Icon(Icons.Default.AttachMoney, null) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                placeholder = { Text("0.00") },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color(0xFF212121),
                    unfocusedTextColor = Color(0xFF212121),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )
                }
            }
            
            Button(
                onClick = {
                    if (cedula.isBlank() || nombre.isBlank() || fechaNacimiento.isBlank() || saldo.isBlank()) {
                        toastHelper.showError("Complete todos los campos requeridos")
                        return@Button
                    }
                    
                    isLoading = true
                    scope.launch {
                        val request = ClienteRequest(
                            cedula = cedula,
                            nombre = nombre,
                            fechaNacimiento = fechaNacimiento,
                            estadoCivil = estadoCivil,
                            numeroCuenta = numeroCuenta.ifBlank { null },
                            tipoCuentaInicial = tipoCuenta,
                            saldoInicial = saldo.toBigDecimalOrNull()
                        )
                        
                        val result = viewModel.crearCliente(request)
                        isLoading = false
                        
                        result.fold(
                            onSuccess = {
                                toastHelper.showSuccess("Cliente creado exitosamente")
                                navController.navigateUp()
                            },
                            onFailure = { error ->
                                toastHelper.showError(error.message ?: "Error al crear cliente")
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
                    Text("GUARDAR CLIENTE", fontWeight = FontWeight.Bold)
                }
            }
        }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarClienteScreen(
    navController: NavController,
    cedula: String,
    viewModel: ClienteViewModel = viewModel()
) {
    val toastHelper = rememberToastHelper()
    val scope = rememberCoroutineScope()
    val clienteDetailState by viewModel.clienteDetailState.collectAsState()
    
    var nombre by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf("") }
    var estadoCivil by remember { mutableStateOf("SOLTERO") }
    var isLoading by remember { mutableStateOf(false) }
    var isDataLoaded by remember { mutableStateOf(false) }
    
    LaunchedEffect(cedula) {
        viewModel.obtenerCliente(cedula)
    }
    
    LaunchedEffect(clienteDetailState) {
        if (clienteDetailState is ClienteDetailState.Success && !isDataLoaded) {
            val cliente = (clienteDetailState as ClienteDetailState.Success).cliente
            nombre = cliente.nombre
            fechaNacimiento = cliente.fechaNacimiento
            estadoCivil = cliente.estadoCivil
            isDataLoaded = true
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Cliente") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1976D2),
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
        when (clienteDetailState) {
            is ClienteDetailState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            }
            is ClienteDetailState.Success -> {
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
                    Text(
                        "Editar Datos del Cliente",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color(0xFF212121)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedTextField(
                value = cedula,
                onValueChange = {},
                label = { Text("Cédula") },
                leadingIcon = { Icon(Icons.Default.Badge, null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = Color(0xFF757575),
                    disabledContainerColor = Color(0xFFF5F5F5),
                    disabledBorderColor = Color(0xFFE0E0E0)
                )
            )
            
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre Completo *") },
                leadingIcon = { Icon(Icons.Default.Person, null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color(0xFF212121),
                    unfocusedTextColor = Color(0xFF212121),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )
            
            OutlinedTextField(
                value = fechaNacimiento,
                onValueChange = { fechaNacimiento = it },
                label = { Text("Fecha Nacimiento (YYYY-MM-DD) *") },
                leadingIcon = { Icon(Icons.Default.CalendarToday, null) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("1990-01-01") },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color(0xFF212121),
                    unfocusedTextColor = Color(0xFF212121),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )
            
            var expandedEstadoCivil by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedEstadoCivil,
                onExpandedChange = { expandedEstadoCivil = it }
            ) {
                OutlinedTextField(
                    value = estadoCivil,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Estado Civil *") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedEstadoCivil) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color(0xFF212121),
                        unfocusedTextColor = Color(0xFF212121),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )
                ExposedDropdownMenu(
                    expanded = expandedEstadoCivil,
                    onDismissRequest = { expandedEstadoCivil = false }
                ) {
                    listOf("SOLTERO", "CASADO", "DIVORCIADO", "VIUDO").forEach { estado ->
                        DropdownMenuItem(
                            text = { Text(estado) },
                            onClick = {
                                estadoCivil = estado
                                expandedEstadoCivil = false
                            }
                        )
                    }
                }
            }
                }
            }
            
            Button(
                onClick = {
                    if (nombre.isBlank() || fechaNacimiento.isBlank()) {
                        toastHelper.showError("Complete todos los campos requeridos")
                        return@Button
                    }
                    
                    isLoading = true
                    scope.launch {
                        val request = ClienteRequest(
                            cedula = cedula,
                            nombre = nombre,
                            fechaNacimiento = fechaNacimiento,
                            estadoCivil = estadoCivil,
                            numeroCuenta = null,
                            tipoCuentaInicial = "AHORROS",
                            saldoInicial = null
                        )
                        
                        val result = viewModel.actualizarCliente(cedula, request)
                        isLoading = false
                        
                        result.fold(
                            onSuccess = {
                                toastHelper.showSuccess("Cliente actualizado exitosamente")
                                navController.navigateUp()
                            },
                            onFailure = { error ->
                                toastHelper.showError(error.message ?: "Error al actualizar cliente")
                            }
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(Color(0xFF1976D2))
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Icon(Icons.Default.Save, null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("GUARDAR CAMBIOS", fontWeight = FontWeight.Bold)
                }
            }
        }
            }
            is ClienteDetailState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Error, null, tint = Color(0xFFFF6B6B), modifier = Modifier.size(64.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text((clienteDetailState as ClienteDetailState.Error).message, color = Color.White)
                    }
                }
            }
            else -> {}
        }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClienteDetalleScreen(
    navController: NavController,
    cedula: String,
    clienteViewModel: ClienteViewModel = viewModel()
) {
    val toastHelper = rememberToastHelper()
    val clienteDetailState by clienteViewModel.clienteDetailState.collectAsState()
    val scope = rememberCoroutineScope()
    var showDeleteDialog by remember { mutableStateOf(false) }
    
    LaunchedEffect(cedula) {
        clienteViewModel.obtenerCliente(cedula)
    }
    
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirmar Eliminación") },
            text = { Text("¿Está seguro de eliminar este cliente? Esta acción no se puede deshacer.") },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            val result = clienteViewModel.eliminarCliente(cedula)
                            if (result.isSuccess) {
                                toastHelper.showSuccess("Cliente eliminado exitosamente")
                                navController.navigateUp()
                            } else {
                                toastHelper.showError("Error al eliminar cliente")
                            }
                        }
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Cliente") },
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
        when (val state = clienteDetailState) {
            is ClienteDetailState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            }
            
            is ClienteDetailState.Success -> {
                val cliente = state.cliente
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
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(56.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF1565C0)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.Person,
                                        null,
                                        modifier = Modifier.size(32.dp),
                                        tint = Color.White
                                    )
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text(
                                        cliente.nombre,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp,
                                        color = Color(0xFF212121)
                                    )
                                    Text(cliente.cedula, color = Color(0xFF757575))
                                    Text("Estado Civil: ${cliente.estadoCivil}", fontSize = 12.sp, color = Color(0xFF757575))
                                }
                            }
                            
                            Divider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFE0E0E0))
                            
                            DetailRow("Fecha Nacimiento", state.cliente.fechaNacimiento)
                        }
                    }
                    
                    // Botones de acción
                    Button(
                        onClick = { navController.navigate(Screen.CuentasPorCliente.createRoute(cedula)) },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(Color(0xFF1976D2))
                    ) {
                        Icon(Icons.Default.AccountBalance, null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("VER CUENTAS", fontWeight = FontWeight.Bold)
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Botones de Editar y Eliminar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                navController.navigate(Screen.EditarCliente.createRoute(cedula))
                            },
                            modifier = Modifier.weight(1f).height(56.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color(0xFF1976D2)
                            )
                        ) {
                            Icon(Icons.Default.Edit, null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("EDITAR")
                        }
                        
                        OutlinedButton(
                            onClick = {
                                showDeleteDialog = true
                            },
                            modifier = Modifier.weight(1f).height(56.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color(0xFFD32F2F)
                            )
                        ) {
                            Icon(Icons.Default.Delete, null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("ELIMINAR")
                        }
                    }
                }
            }
            
            is ClienteDetailState.Error -> {
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
                        Text(state.message, color = Color.White, fontSize = 16.sp)
                    }
                }
            }
            
            ClienteDetailState.Idle -> {}
        }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color(0xFF757575))
        Text(value, fontWeight = FontWeight.Medium, color = Color(0xFF212121))
    }
}
