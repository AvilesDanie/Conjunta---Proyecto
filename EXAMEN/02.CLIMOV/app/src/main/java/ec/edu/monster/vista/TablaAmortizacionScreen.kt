package ec.edu.monster.vista

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ec.edu.monster.controlador.CreditoViewModel
import ec.edu.monster.controlador.CuotasState
import ec.edu.monster.modelo.CuotaAmortizacion

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TablaAmortizacionScreen(
    navController: NavController,
    idCredito: Long,
    viewModel: CreditoViewModel = viewModel()
) {
    val cuotasState by viewModel.cuotasState.collectAsState()
    
    LaunchedEffect(idCredito) {
        viewModel.cargarCuotasAmortizacion(idCredito)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tabla de Amortización") },
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
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
        ) {
            when (val state = cuotasState) {
                is CuotasState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Color(0xFF1976D2)
                    )
                }
                is CuotasState.Success -> {
                    if (state.cuotas.isEmpty()) {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.TableChart,
                                null,
                                Modifier.size(64.dp),
                                tint = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("No hay cuotas disponibles", color = Color.Gray)
                        }
                    } else {
                        Column(modifier = Modifier.fillMaxSize()) {
                            // Resumen del crédito
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                colors = CardDefaults.cardColors(Color.White),
                                elevation = CardDefaults.cardElevation(4.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        "Crédito #$idCredito",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp,
                                        color = Color(0xFF212121)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        "Total de cuotas: ${state.cuotas.size}",
                                        color = Color(0xFF212121)
                                    )
                                    Text(
                                        "Cuota mensual: $${String.format("%.2f", state.cuotas.firstOrNull()?.valorCuota ?: 0)}",
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 16.sp,
                                        color = Color(0xFF212121)
                                    )
                                }
                            }
                            
                            // Lista de cuotas
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(state.cuotas) { cuota ->
                                    CuotaCard(cuota)
                                }
                            }
                        }
                    }
                }
                is CuotasState.Error -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Error, null, Modifier.size(64.dp), tint = Color.Red)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(state.message, color = Color.Red)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.cargarCuotasAmortizacion(idCredito) }) {
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
fun CuotaCard(cuota: CuotaAmortizacion) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(
            containerColor = when (cuota.estado) {
                "PAGADA" -> Color(0xFFE8F5E9)
                "PENDIENTE" -> Color.White
                "VENCIDA" -> Color(0xFFFFEBEE)
                else -> Color.White
            }
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Cuota #${cuota.numeroCuota}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF212121)
                )
                
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = when (cuota.estado) {
                        "PAGADA" -> Color(0xFF388E3C)
                        "PENDIENTE" -> Color(0xFFFFA000)
                        "VENCIDA" -> Color(0xFFD32F2F)
                        else -> Color.Gray
                    }
                ) {
                    Text(
                        text = cuota.estado,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    InfoRow("Valor Cuota:", "$${String.format("%.2f", cuota.valorCuota)}")
                    InfoRow("Interés:", "$${String.format("%.2f", cuota.interesPagado)}")
                    InfoRow("Capital:", "$${String.format("%.2f", cuota.capitalPagado)}")
                }
                
                Column(horizontalAlignment = Alignment.End) {
                    Text("Saldo Pendiente", fontSize = 12.sp, color = Color(0xFF212121))
                    Text(
                        "$${String.format("%.2f", cuota.saldo)}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF1976D2)
                    )
                }
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 14.sp, color = Color(0xFF757575))
        Spacer(modifier = Modifier.width(8.dp))
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF212121))
    }
}
