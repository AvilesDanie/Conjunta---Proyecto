package ec.edu.monster.vista

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ec.edu.monster.controlador.ClienteState
import ec.edu.monster.controlador.ClienteViewModel
import ec.edu.monster.modelo.ClienteResponse
import ec.edu.monster.util.rememberToastHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientesScreen(
    navController: NavController,
    viewModel: ClienteViewModel = viewModel()
) {
    val clienteState by viewModel.clienteState.collectAsState()
    val toastHelper = rememberToastHelper()
    var searchQuery by remember { mutableStateOf("") }
    
    LaunchedEffect(Unit) {
        viewModel.cargarClientes()
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista de Clientes") },
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
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.CrearCliente.route) },
                containerColor = Color(0xFF1976D2)
            ) {
                Icon(Icons.Default.Add, "Agregar cliente", tint = Color.White)
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
            when (val state = clienteState) {
                is ClienteState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.White
                    )
                }
                is ClienteState.Success -> {
                    Column(modifier = Modifier.fillMaxSize()) {
                        // Buscador
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            placeholder = { Text("Buscar por nombre o cédula") },
                            leadingIcon = {
                                Icon(Icons.Default.Search, "Buscar")
                            },
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { searchQuery = "" }) {
                                        Icon(Icons.Default.Clear, "Limpiar")
                                    }
                                }
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedTextColor = Color(0xFF212121),
                                unfocusedTextColor = Color(0xFF212121),
                                focusedBorderColor = Color(0xFF1976D2),
                                unfocusedBorderColor = Color.White,
                                focusedLeadingIconColor = Color(0xFF1976D2),
                                unfocusedLeadingIconColor = Color(0xFF757575)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                        
                        // Filtrar clientes
                        val clientesFiltrados = if (searchQuery.isEmpty()) {
                            state.clientes
                        } else {
                            state.clientes.filter {
                                it.nombre.contains(searchQuery, ignoreCase = true) ||
                                it.cedula.contains(searchQuery, ignoreCase = true)
                            }
                        }
                        
                        if (clientesFiltrados.isEmpty()) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    Icons.Default.People,
                                    contentDescription = null,
                                    modifier = Modifier.size(80.dp),
                                    tint = Color.White.copy(alpha = 0.7f)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = if (searchQuery.isEmpty()) 
                                        "No hay clientes registrados" 
                                    else 
                                        "No se encontraron clientes",
                                    color = Color.White,
                                    fontSize = 18.sp
                                )
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(clientesFiltrados) { cliente ->
                                    ClienteCard(
                                        cliente = cliente,
                                        onClick = {
                                            navController.navigate(
                                                Screen.ClienteDetalle.createRoute(cliente.cedula)
                                            )
                                        },
                                        onDelete = {
                                            toastHelper.showError("No se puede eliminar: el cliente tiene cuentas asociadas. Elimine primero sus cuentas.")
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
                is ClienteState.Error -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.Error,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = Color(0xFFFF6B6B)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = state.message,
                            color = Color.White,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.cargarClientes() }) {
                            Text("Reintentar")
                        }
                    }
                }
                else -> {}
            }
        }
    }
}

@Composable
fun ClienteCard(
    cliente: ClienteResponse,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color.White,
                            Color(0xFFF5F5FF)
                        )
                    )
                )
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1976D2)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = cliente.nombre.firstOrNull()?.toString() ?: "?",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Información del cliente
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = cliente.nombre,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = Color(0xFF212121)
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "CI: ${cliente.cedula}",
                    fontSize = 14.sp,
                    color = Color(0xFF757575)
                )
                
                cliente.numCuentaInicial?.let {
                    Text(
                        text = "Cuenta: $it",
                        fontSize = 13.sp,
                        color = Color(0xFF9E9E9E)
                    )
                }
                
                cliente.tipoCuentaInicial?.let {
                    Text(
                        text = "Tipo: $it",
                        fontSize = 13.sp,
                        color = Color(0xFF9E9E9E)
                    )
                }
            }
            
            // Icono
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = "Ver detalles",
                tint = Color(0xFF1976D2),
                modifier = Modifier.size(28.dp)
            )
        }
    }
}
