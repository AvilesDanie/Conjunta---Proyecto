package ec.edu.monster.vista

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.navigation.NavController
import ec.edu.monster.util.rememberToastHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreditosScreen(navController: NavController) {
    var showIdInput by remember { mutableStateOf(false) }
    var idCredito by remember { mutableStateOf("") }
    val toastHelper = rememberToastHelper()
    
    if (showIdInput) {
        AlertDialog(
            onDismissRequest = { showIdInput = false },
            title = { Text("Consultar Cuotas") },
            text = {
                Column {
                    Text("Ingrese el ID del crédito:")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = idCredito,
                        onValueChange = { idCredito = it },
                        label = { Text("ID Crédito") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color(0xFFBBBBBB),
                            cursorColor = Color.White,
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color(0xFFBBBBBB)
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (idCredito.isNotEmpty()) {
                            navController.navigate(Screen.Cuotas.createRoute(idCredito))
                            showIdInput = false
                            idCredito = ""
                        } else {
                            toastHelper.showError("Ingrese un ID válido")
                        }
                    }
                ) {
                    Text("Consultar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showIdInput = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestión de Créditos") },
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
                onClick = { navController.navigate(Screen.EvaluarCredito.route) },
                containerColor = Color(0xFF1565C0)
            ) {
                Icon(Icons.Default.Add, "Evaluar Crédito", tint = Color.White)
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF0D47A1),
                            Color(0xFF1565C0),
                            Color(0xFF1976D2)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(Color.White)
            ) {
                Column(
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
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF1565C0).copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.CreditCard,
                            contentDescription = null,
                            modifier = Modifier.size(40.dp),
                            tint = Color(0xFF1565C0)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Evaluación de Créditos",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF212121)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Evalúe si un cliente es sujeto de crédito según su historial de movimientos",
                        fontSize = 14.sp,
                        color = Color(0xFF212121)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { navController.navigate(Screen.EvaluarCredito.route) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Assessment, null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Evaluar Nuevo Crédito")
                    }
                }
            }
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color.White,
                                    Color(0xFFE8F5E9)
                                )
                            )
                        )
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF2E7D32).copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Receipt,
                            contentDescription = null,
                            modifier = Modifier.size(40.dp),
                            tint = Color(0xFF2E7D32)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Consultar Cuotas",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF212121)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Consulte las cuotas de amortización de un crédito específico",
                        fontSize = 14.sp,
                        color = Color(0xFF212121)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { showIdInput = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2E7D32)
                        )
                    ) {
                        Icon(Icons.Default.List, null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Ver Cuotas de Crédito")
                    }
                }
            }
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color.White,
                                    Color(0xFFF3E5F5)
                                )
                            )
                        )
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF6A1B9A).copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = null,
                            modifier = Modifier.size(40.dp),
                            tint = Color(0xFF6A1B9A)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Información",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF212121)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Los créditos aprobados generan automáticamente una tabla de amortización que puede consultar desde la facturación en ElectroQuito o desde esta sección",
                        fontSize = 14.sp,
                        color = Color(0xFF212121)
                    )
                }
            }
        }
        }
    }
}
