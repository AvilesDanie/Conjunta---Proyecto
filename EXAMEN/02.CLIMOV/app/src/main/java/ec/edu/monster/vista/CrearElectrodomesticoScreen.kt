package ec.edu.monster.vista

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ec.edu.monster.controlador.ElectrodomesticoViewModel
import ec.edu.monster.modelo.ElectrodomesticoRequest
import ec.edu.monster.util.rememberToastHelper
import kotlinx.coroutines.launch
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearElectrodomesticoScreen(
    navController: NavController,
    viewModel: ElectrodomesticoViewModel = viewModel()
) {
    val toastHelper = rememberToastHelper()
    val scope = rememberCoroutineScope()
    
    var codigo by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuevo Producto") },
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
                                    colors = listOf(
                                        Color.White,
                                        Color(0xFFFFF3E0)
                                    )
                                )
                            )
                            .padding(20.dp)
                    ) {
                        Text(
                            "Datos del Producto",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color(0xFF212121)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedTextField(
                value = codigo,
                onValueChange = { codigo = it },
                label = { Text("Código *") },
                leadingIcon = { Icon(Icons.Default.QrCode, null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color(0xFF212121),
                    unfocusedTextColor = Color(0xFF212121),
                    focusedLabelColor = Color(0xFF212121),
                    unfocusedLabelColor = Color(0xFF757575)
                )
            )
            
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre del Producto *") },
                leadingIcon = { Icon(Icons.Default.Devices, null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color(0xFF212121),
                    unfocusedTextColor = Color(0xFF212121),
                    focusedLabelColor = Color(0xFF212121),
                    unfocusedLabelColor = Color(0xFF757575)
                )
            )
            
            OutlinedTextField(
                value = precio,
                onValueChange = { precio = it },
                label = { Text("Precio de Venta *") },
                leadingIcon = { Icon(Icons.Default.AttachMoney, null) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color(0xFF212121),
                    unfocusedTextColor = Color(0xFF212121),
                    focusedLabelColor = Color(0xFF212121),
                    unfocusedLabelColor = Color(0xFF757575)
                )
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = {
                    if (codigo.isBlank() || nombre.isBlank() || precio.isBlank()) {
                        toastHelper.showError("Complete todos los campos")
                        return@Button
                    }
                    
                    isLoading = true
                    val precioDecimal = precio.toDoubleOrNull()
                    if (precioDecimal == null) {
                        toastHelper.showError("Precio inválido")
                        isLoading = false
                        return@Button
                    }
                    
                    scope.launch {
                        val request = ElectrodomesticoRequest(
                            codigo = codigo,
                            nombre = nombre,
                            precioVenta = BigDecimal(precioDecimal)
                        )
                        
                        val result = viewModel.crearElectrodomestico(request)
                        isLoading = false
                        
                        result.fold(
                            onSuccess = {
                                toastHelper.showSuccess("Producto creado exitosamente")
                                navController.navigateUp()
                            },
                            onFailure = { error ->
                                toastHelper.showError(error.message ?: "Error al crear producto")
                            }
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(Color(0xFFFF6F00))
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Icon(Icons.Default.Save, null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("GUARDAR PRODUCTO", fontWeight = FontWeight.Bold)
                }
            }
                    }
                }
            }
        }
    }
}
