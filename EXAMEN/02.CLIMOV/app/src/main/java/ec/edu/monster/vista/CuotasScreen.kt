package ec.edu.monster.vista

import androidx.compose.foundation.background
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
import ec.edu.monster.modelo.CuotaAmortizacion
import ec.edu.monster.controlador.CreditoViewModel
import ec.edu.monster.controlador.CuotasState
import ec.edu.monster.util.ToastHelper
import ec.edu.monster.util.rememberToastHelper
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CuotasScreen(
    navController: NavController,
    idCredito: String,
    viewModel: CreditoViewModel = viewModel()
) {
    val toastHelper = rememberToastHelper()
    val cuotasState by viewModel.cuotasState.collectAsState()
    
    LaunchedEffect(idCredito) {
        val id = idCredito.toLongOrNull()
        if (id != null) {
            viewModel.cargarCuotasAmortizacion(id)
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cuotas de Amortización") },
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
            when (val state = cuotasState) {
                is CuotasState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.White)
                    }
                }
                
                is CuotasState.Success -> {
                    if (state.cuotas.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    Icons.Default.Receipt,
                                    null,
                                    modifier = Modifier.size(64.dp),
                                    tint = Color.White
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    "No hay cuotas registradas para este crédito",
                                    fontSize = 18.sp,
                                    color = Color.White
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize().padding(paddingValues),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            item {
                                // Resumen del crédito
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color(0xFF1565C0)
                                    ),
                                    elevation = CardDefaults.cardElevation(4.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(16.dp)
                                    ) {
                                        Text(
                                            "Crédito ID: $idCredito",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            "Total de cuotas: ${state.cuotas.size}",
                                            fontSize = 16.sp,
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                            
                            items(state.cuotas) { cuota ->
                                CuotaCard(cuota, toastHelper)
                            }
                        }
                    }
                }
                
                is CuotasState.Error -> {
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
                
                else -> {}
            }
        }
    }
}

@Composable
fun CuotaCard(cuota: CuotaAmortizacion, toastHelper: ToastHelper) {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val viewModel: CreditoViewModel = viewModel()
    val scope = rememberCoroutineScope()
    var showPagarDialog by remember { mutableStateOf(false) }
    var showAnularDialog by remember { mutableStateOf(false) }
    
    val estadoColor = when (cuota.estado) {
        "PAGADA" -> Color(0xFF2E7D32)
        "PENDIENTE" -> Color(0xFFE65100)
        "VENCIDA" -> Color(0xFFD32F2F)
        "ANULADA" -> Color(0xFF757575)
        else -> Color.Gray
    }
    
    // Diálogo de confirmación para pagar
    if (showPagarDialog) {
        AlertDialog(
            onDismissRequest = { showPagarDialog = false },
            title = { Text("Confirmar Pago") },
            text = { Text("¿Está seguro de marcar esta cuota como PAGADA?") },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            val cuotaActualizada = cuota.copy(estado = "PAGADA")
                            val result = viewModel.actualizarEstadoCuota(cuota.id, cuotaActualizada)
                            if (result.isSuccess) {
                                toastHelper.showSuccess("Cuota marcada como PAGADA")
                                viewModel.cargarCuotasAmortizacion(cuota.idCredito)
                            } else {
                                toastHelper.showError("Error: ${result.exceptionOrNull()?.message}")
                            }
                            showPagarDialog = false
                        }
                    }
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPagarDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
    
    // Diálogo de confirmación para anular
    if (showAnularDialog) {
        AlertDialog(
            onDismissRequest = { showAnularDialog = false },
            title = { Text("Confirmar Anulación") },
            text = { Text("¿Está seguro de anular esta cuota? Esta acción no se puede deshacer.") },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            val result = viewModel.anularCuota(cuota.id)
                            if (result.isSuccess) {
                                toastHelper.showSuccess("Cuota anulada exitosamente")
                                viewModel.cargarCuotasAmortizacion(cuota.idCredito)
                            } else {
                                toastHelper.showError("Error: ${result.exceptionOrNull()?.message}")
                            }
                            showAnularDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFD32F2F)
                    )
                ) {
                    Text("Anular")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAnularDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(Color.White, Color(0xFFF5F5FF))
                    )
                )
                .padding(20.dp)
        ) {
            // Encabezado con número de cuota y estado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Cuota #${cuota.numeroCuota}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF212121)
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(estadoColor.copy(alpha = 0.15f))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        cuota.estado,
                        color = estadoColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            Divider()
            Spacer(modifier = Modifier.height(12.dp))
            
            // Detalles de la cuota
            DetailRowCuota("Valor Cuota", "$${cuota.valorCuota}")
            DetailRowCuota("Interés Pagado", "$${cuota.interesPagado}")
            DetailRowCuota("Capital Pagado", "$${cuota.capitalPagado}")
            DetailRowCuota("Saldo", "$${cuota.saldo}")
            DetailRowCuota("Fecha Vencimiento", cuota.fechaVencimiento)
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Botones de acción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (cuota.estado == "PENDIENTE") {
                    OutlinedButton(
                        onClick = { showPagarDialog = true },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFF2E7D32)
                        )
                    ) {
                        Icon(Icons.Default.CheckCircle, null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("PAGAR", fontSize = 12.sp)
                    }
                }
                
                if (cuota.estado != "ANULADA") {
                    OutlinedButton(
                        onClick = { showAnularDialog = true },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFFD32F2F)
                        )
                    ) {
                        Icon(Icons.Default.Cancel, null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("ANULAR", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun DetailRowCuota(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color(0xFF757575), fontSize = 14.sp)
        Text(value, fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color(0xFF212121))
    }
}
