package ec.edu.monster.vista

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import ec.edu.monster.controlador.FacturaState
import ec.edu.monster.controlador.FacturaViewModel
import ec.edu.monster.modelo.FacturaResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FacturasScreen(
    navController: NavController,
    viewModel: FacturaViewModel = viewModel()
) {
    val facturaState by viewModel.facturaState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var showSearchDialog by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        viewModel.cargarFacturas()
    }
    
    if (showSearchDialog) {
        AlertDialog(
            onDismissRequest = { showSearchDialog = false },
            title = { Text("Buscar Factura por ID") },
            text = {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("ID de Factura") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color(0xFF212121),
                        unfocusedTextColor = Color(0xFF212121),
                        focusedLabelColor = Color(0xFF212121),
                        unfocusedLabelColor = Color(0xFF757575)
                    )
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val id = searchQuery.toLongOrNull()
                        if (id != null) {
                            navController.navigate(Screen.FacturaDetalle.createRoute(id))
                            showSearchDialog = false
                            searchQuery = ""
                        }
                    }
                ) {
                    Text("Buscar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSearchDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Historial de Facturas") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFF6F00),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showSearchDialog = true },
                containerColor = Color(0xFFFF6F00)
            ) {
                Icon(Icons.Default.Search, "Buscar por ID", tint = Color.White)
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFE65100),
                            Color(0xFFF57C00),
                            Color(0xFFFF9800)
                        )
                    )
                )
                .padding(paddingValues)
        ) {
            when (val state = facturaState) {
                is FacturaState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.White
                    )
                }
                is FacturaState.Success -> {
                    if (state.facturas.isEmpty()) {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.Receipt,
                                null,
                                Modifier.size(80.dp),
                                tint = Color.White.copy(alpha = 0.7f)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "No hay facturas registradas",
                                color = Color.White,
                                fontSize = 18.sp
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(state.facturas) { factura ->
                                FacturaCard(
                                    factura = factura,
                                    onClick = {
                                        navController.navigate(
                                            Screen.FacturaDetalle.createRoute(factura.id)
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
                is FacturaState.Error -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.Error,
                            null,
                            Modifier.size(64.dp),
                            tint = Color(0xFFFF6B6B)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            state.message,
                            color = Color.White,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.cargarFacturas() }) {
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
fun FacturaCard(factura: FacturaResponse, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Column(
            modifier = Modifier
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color.White,
                            Color(0xFFFFF3E0)
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
                Text("Factura #${factura.id}", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF212121))
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(if (factura.formaPago == "EFECTIVO") Color(0xFF388E3C) else Color(0xFF1976D2)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        if (factura.formaPago == "EFECTIVO") Icons.Default.Money else Icons.Default.CreditCard,
                        contentDescription = factura.formaPago,
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text("Cliente: ${factura.nombreCliente}", fontSize = 14.sp, color = Color(0xFF212121))
            Text("Fecha: ${factura.fecha}", fontSize = 12.sp, color = Color(0xFF757575))
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    if (factura.descuento > java.math.BigDecimal.ZERO) {
                        Text("Descuento: $${factura.descuento}", fontSize = 12.sp, color = Color(0xFFD32F2F))
                    }
                }
                Text(
                    "Total: $${String.format("%.2f", factura.totalNeto)}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF388E3C)
                )
            }
        }
    }
}

@Composable
fun Chip(label: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = color.copy(alpha = 0.2f)
    ) {
        Text(
            text = label,
            color = color,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FacturaDetalleScreen(
    navController: NavController,
    facturaId: Long,
    viewModel: FacturaViewModel = viewModel()
) {
    val facturaDetailState by viewModel.facturaDetailState.collectAsState()
    
    LaunchedEffect(facturaId) {
        viewModel.obtenerFactura(facturaId)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Factura") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFF6F00),
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
                            Color(0xFFE65100),
                            Color(0xFFF57C00),
                            Color(0xFFFF9800)
                        )
                    )
                )
        ) {
            when (val state = facturaDetailState) {
                is ec.edu.monster.controlador.FacturaDetailState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.White
                    )
                }
                is ec.edu.monster.controlador.FacturaDetailState.Success -> {
                    val factura = state.factura
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
                                            colors = listOf(Color.White, Color(0xFFFFF3E0))
                                        )
                                    )
                                    .padding(20.dp)
                            ) {
                                Text("Factura #${factura.id}", fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Color(0xFF212121))
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Fecha: ${factura.fecha}", color = Color(0xFF212121))
                                Text("Cliente: ${factura.nombreCliente}", color = Color(0xFF212121))
                                Text("CI: ${factura.cedulaCliente}", color = Color(0xFF757575))
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
                                            colors = listOf(Color.White, Color(0xFFFFF3E0))
                                        )
                                    )
                                    .padding(20.dp)
                            ) {
                                Text("Detalles", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF212121))
                                Spacer(modifier = Modifier.height(12.dp))
                                factura.detalles.forEach { detalle ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(detalle.nombreElectro, fontWeight = FontWeight.Medium, color = Color(0xFF212121))
                                            Text("Cantidad: ${detalle.cantidad}", fontSize = 14.sp, color = Color(0xFF757575))
                                        }
                                        Text("$${detalle.subtotal}", fontWeight = FontWeight.Bold, color = Color(0xFF388E3C))
                                    }
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
                                            colors = listOf(Color.White, Color(0xFFFFF3E0))
                                        )
                                    )
                                    .padding(20.dp)
                            ) {
                                Text("Resumen", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF212121))
                                Spacer(modifier = Modifier.height(12.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Subtotal:", color = Color(0xFF212121), fontSize = 16.sp)
                                    Text("$${factura.totalBruto}", color = Color(0xFF212121), fontSize = 16.sp)
                                }
                                if (factura.descuento > java.math.BigDecimal.ZERO) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Descuento:", color = Color(0xFF212121), fontSize = 16.sp)
                                        Text("-$${factura.descuento}", color = Color(0xFFD32F2F), fontSize = 16.sp)
                                    }
                                }
                                Divider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFE0E0E0))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("TOTAL:", fontWeight = FontWeight.Bold, fontSize = 22.sp, color = Color(0xFF212121))
                                    Text(
                                        "$${factura.totalNeto}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 22.sp,
                                        color = Color(0xFF388E3C)
                                    )
                                }
                            }
                        }
                        
                        // Si tiene crédito asociado, mostrar botón para ver tabla de amortización
                        factura.idCreditoBanquito?.let { idCredito ->
                            Button(
                                onClick = {
                                    navController.navigate(Screen.TablaAmortizacion.createRoute(idCredito))
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.TableChart, null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Ver Tabla de Amortización")
                            }
                        }
                    }
                }
                is ec.edu.monster.controlador.FacturaDetailState.Error -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Error, null, Modifier.size(64.dp), tint = Color.Red)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(state.message, color = Color.Red)
                    }
                }
                else -> {}
            }
        }
    }
}
