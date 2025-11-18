package ec.edu.monster.vista

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ec.edu.monster.controlador.CreditoViewModel
import ec.edu.monster.controlador.EvaluationState
import ec.edu.monster.modelo.SolicitudCredito
import ec.edu.monster.util.rememberToastHelper
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EvaluarCreditoScreen(
    navController: NavController,
    viewModel: CreditoViewModel = viewModel()
) {
    val toastHelper = rememberToastHelper()
    val evaluationState by viewModel.evaluationState.collectAsState()
    val scope = rememberCoroutineScope()
    
    var cedula by remember { mutableStateOf("") }
    var precioProducto by remember { mutableStateOf("") }
    var plazoMeses by remember { mutableStateOf("") }
    var numCuentaCredito by remember { mutableStateOf("") }
    
    var resultadoEvaluacion by remember { mutableStateOf<ec.edu.monster.modelo.ResultadoEvaluacion?>(null) }
    var mostrarBotonCrear by remember { mutableStateOf(false) }
    
    LaunchedEffect(evaluationState) {
        when (val state = evaluationState) {
            is EvaluationState.EvaluationSuccess -> {
                resultadoEvaluacion = state.resultado
                if (state.resultado.aprobado) {
                    toastHelper.showSuccess("Crédito APROBADO - Monto máximo: $${state.resultado.montoMaximo}")
                    mostrarBotonCrear = true
                } else {
                    val motivo = state.resultado.motivo ?: "No especificado"
                    
                    // Detectar si es por cuotas pendientes
                    if (motivo.contains("cuotas pendientes", ignoreCase = true) ||
                        motivo.contains("crédito activo", ignoreCase = true)) {
                        toastHelper.showError("⚠️ RECHAZADO - PROBLEMA DEL SERVIDOR: Se detecta un crédito anterior con estado ACTIVO pero todas las cuotas están pagadas. El servidor debe actualizar el estado del crédito a FINALIZADO.")
                    } else {
                        toastHelper.showError("Crédito RECHAZADO - $motivo")
                    }
                    mostrarBotonCrear = false
                }
                viewModel.resetEvaluationState()
            }
            is EvaluationState.Error -> {
                toastHelper.showError(state.message)
                viewModel.resetEvaluationState()
            }
            else -> {}
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Evaluar Crédito") },
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
                .background(Color(0xFFF5F5F5))
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
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = null,
                        tint = Color(0xFF1565C0),
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Evaluación de Crédito",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color(0xFF212121)
                        )
                        Text(
                            "Complete los datos para evaluar si el cliente es sujeto de crédito",
                            fontSize = 14.sp,
                            color = Color(0xFF212121)
                        )
                    }
                }
            }
            
            OutlinedTextField(
                value = cedula,
                onValueChange = { cedula = it },
                label = { Text("Cédula del Cliente *") },
                leadingIcon = { Icon(Icons.Default.Badge, null) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color(0xFF212121),
                    unfocusedTextColor = Color(0xFF212121),
                    focusedLabelColor = Color(0xFF212121),
                    unfocusedLabelColor = Color(0xFF212121),
                    focusedLeadingIconColor = Color(0xFF212121),
                    unfocusedLeadingIconColor = Color(0xFF212121),
                    focusedPlaceholderColor = Color(0xFF757575),
                    unfocusedPlaceholderColor = Color(0xFF757575),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )
            
            OutlinedTextField(
                value = precioProducto,
                onValueChange = { precioProducto = it },
                label = { Text("Precio del Producto *") },
                leadingIcon = { Icon(Icons.Default.AttachMoney, null) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                placeholder = { Text("Ejemplo: 1500.00") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color(0xFF212121),
                    unfocusedTextColor = Color(0xFF212121),
                    focusedLabelColor = Color(0xFF212121),
                    unfocusedLabelColor = Color(0xFF212121),
                    focusedLeadingIconColor = Color(0xFF212121),
                    unfocusedLeadingIconColor = Color(0xFF212121),
                    focusedPlaceholderColor = Color(0xFF757575),
                    unfocusedPlaceholderColor = Color(0xFF757575),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )
            
            OutlinedTextField(
                value = plazoMeses,
                onValueChange = { plazoMeses = it },
                label = { Text("Plazo en Meses *") },
                leadingIcon = { Icon(Icons.Default.CalendarToday, null) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                placeholder = { Text("Ejemplo: 12") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color(0xFF212121),
                    unfocusedTextColor = Color(0xFF212121),
                    focusedLabelColor = Color(0xFF212121),
                    unfocusedLabelColor = Color(0xFF212121),
                    focusedLeadingIconColor = Color(0xFF212121),
                    unfocusedLeadingIconColor = Color(0xFF212121),
                    focusedPlaceholderColor = Color(0xFF757575),
                    unfocusedPlaceholderColor = Color(0xFF757575),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )
            
            OutlinedTextField(
                value = numCuentaCredito,
                onValueChange = { numCuentaCredito = it },
                label = { Text("Número de Cuenta para Crédito *") },
                leadingIcon = { Icon(Icons.Default.AccountBalance, null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = { Text("Cuenta donde se depositará el crédito") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color(0xFF212121),
                    unfocusedTextColor = Color(0xFF212121),
                    focusedLabelColor = Color(0xFF212121),
                    unfocusedLabelColor = Color(0xFF212121),
                    focusedLeadingIconColor = Color(0xFF212121),
                    unfocusedLeadingIconColor = Color(0xFF212121),
                    focusedPlaceholderColor = Color(0xFF757575),
                    unfocusedPlaceholderColor = Color(0xFF757575),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = {
                    if (cedula.isBlank() || precioProducto.isBlank() || 
                        plazoMeses.isBlank() || numCuentaCredito.isBlank()) {
                        toastHelper.showError("Complete todos los campos")
                    } else {
                        val precio = precioProducto.toDoubleOrNull()
                        val plazo = plazoMeses.toIntOrNull()
                        
                        if (precio == null || precio <= 0) {
                            toastHelper.showError("Precio inválido")
                        } else if (plazo == null || plazo <= 0) {
                            toastHelper.showError("Plazo inválido")
                        } else {
                            viewModel.evaluarCredito(cedula, precio.toBigDecimal(), plazo, numCuentaCredito)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0)),
                enabled = evaluationState !is EvaluationState.Loading
            ) {
                if (evaluationState is EvaluationState.Loading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Icon(Icons.Default.Assessment, null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("EVALUAR CRÉDITO", fontSize = 16.sp)
                }
            }
            
            // Botón para CREAR CRÉDITO (aparece solo si fue aprobado)
            if (mostrarBotonCrear && resultadoEvaluacion != null) {
                Spacer(modifier = Modifier.height(8.dp))
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color(0xFF2E7D32),
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    "Crédito APROBADO",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = Color(0xFF2E7D32)
                                )
                                Text(
                                    "Monto máximo: $${resultadoEvaluacion!!.montoMaximo}",
                                    fontSize = 14.sp,
                                    color = Color(0xFF1B5E20)
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Button(
                            onClick = {
                                val precio = precioProducto.toDoubleOrNull()
                                val plazo = plazoMeses.toIntOrNull()
                                
                                if (precio != null && plazo != null) {
                                    scope.launch {
                                        val solicitud = SolicitudCredito(
                                            cedula = cedula.trim(),
                                            precioProducto = precio.toBigDecimal(),
                                            plazoMeses = plazo,
                                            numCuentaCredito = numCuentaCredito.trim()
                                        )
                                        
                                        val result = viewModel.crearCredito(solicitud)
                                        result.fold(
                                            onSuccess = { credito ->
                                                toastHelper.showSuccess("¡Crédito creado! ID: ${credito.id}")
                                                mostrarBotonCrear = false
                                                resultadoEvaluacion = null
                                                cedula = ""
                                                precioProducto = ""
                                                plazoMeses = ""
                                                numCuentaCredito = ""
                                            },
                                            onFailure = { error ->
                                                val errorMsg = error.message ?: "Error desconocido"
                                                
                                                if (errorMsg.contains("cuotas pendientes", ignoreCase = true) ||
                                                    errorMsg.contains("crédito activo", ignoreCase = true)) {
                                                    toastHelper.showError("⚠️ PROBLEMA DEL SERVIDOR: Tienes un crédito anterior con todas las cuotas pagadas pero el estado no se actualizó a FINALIZADO. Contacta al administrador del servidor.")
                                                } else {
                                                    toastHelper.showError("Error al crear crédito: $errorMsg")
                                                }
                                            }
                                        )
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                        ) {
                            Icon(Icons.Default.Done, null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("CREAR CRÉDITO Y GENERAR CUOTAS", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
        }
    }
}
