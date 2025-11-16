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
import ec.edu.monster.controlador.CreditoViewModel
import ec.edu.monster.controlador.ElectrodomesticoState
import ec.edu.monster.controlador.ElectrodomesticoViewModel
import ec.edu.monster.controlador.FacturaViewModel
import ec.edu.monster.modelo.ElectrodomesticoResponse
import ec.edu.monster.modelo.FacturaRequest
import ec.edu.monster.modelo.SolicitudCredito
import ec.edu.monster.util.rememberToastHelper
import kotlinx.coroutines.launch
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FacturarScreen(
    navController: NavController,
    electroViewModel: ElectrodomesticoViewModel = viewModel(),
    facturaViewModel: FacturaViewModel = viewModel(),
    creditoViewModel: CreditoViewModel = viewModel()
) {
    val toastHelper = rememberToastHelper()
    val scope = rememberCoroutineScope()
    val electroState by electroViewModel.electroState.collectAsState()
    
    var cedula by remember { mutableStateOf("") }
    var nombreCliente by remember { mutableStateOf("") }
    var productoSeleccionado by remember { mutableStateOf<ElectrodomesticoResponse?>(null) }
    var cantidad by remember { mutableStateOf("1") }
    var formaPago by remember { mutableStateOf("EFECTIVO") }
    var plazoMeses by remember { mutableStateOf("12") }
    var numCuenta by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var showProductDialog by remember { mutableStateOf(false) }
    
    // Estados para evaluación de crédito
    var creditoEvaluado by remember { mutableStateOf(false) }
    var sujetoCredito by remember { mutableStateOf(false) }
    var montoMaximo by remember { mutableStateOf(BigDecimal.ZERO) }
    var motivoRechazo by remember { mutableStateOf("") }
    var creditoId by remember { mutableStateOf<Long?>(null) }
    var showEvaluacionDialog by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        electroViewModel.cargarElectrodomesticos()
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nueva Factura") },
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
            // Datos del cliente
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
                    Text("Datos del Cliente", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    OutlinedTextField(
                        value = cedula,
                        onValueChange = { cedula = it },
                        label = { Text("Cédula") },
                        leadingIcon = { Icon(Icons.Default.Badge, null) },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color(0xFF212121),
                            unfocusedTextColor = Color(0xFF212121),
                            focusedLabelColor = Color(0xFF212121),
                            unfocusedLabelColor = Color(0xFF757575)
                        )
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    OutlinedTextField(
                        value = nombreCliente,
                        onValueChange = { nombreCliente = it },
                        label = { Text("Nombre completo") },
                        leadingIcon = { Icon(Icons.Default.Person, null) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color(0xFF212121),
                            unfocusedTextColor = Color(0xFF212121),
                            focusedLabelColor = Color(0xFF212121),
                            unfocusedLabelColor = Color(0xFF757575)
                        )
                    )
                }
            }
            
            // Selección de producto
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
                    Text("Producto", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Button(
                        onClick = { showProductDialog = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF6F00)
                        )
                    ) {
                        Icon(Icons.Default.Devices, null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(productoSeleccionado?.nombre ?: "Seleccionar Producto")
                    }
                    
                    productoSeleccionado?.let { producto ->
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Precio: $${producto.precioVenta}", fontSize = 16.sp, color = Color(0xFF388E3C))
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    OutlinedTextField(
                        value = cantidad,
                        onValueChange = { cantidad = it },
                        label = { Text("Cantidad") },
                        leadingIcon = { Icon(Icons.Default.ShoppingCart, null) },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color(0xFF212121),
                            unfocusedTextColor = Color(0xFF212121),
                            focusedLabelColor = Color(0xFF212121),
                            unfocusedLabelColor = Color(0xFF757575)
                        )
                    )
                }
            }
            
            // Forma de pago
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
                    Text("Forma de Pago", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = formaPago == "EFECTIVO",
                            onClick = { formaPago = "EFECTIVO" },
                            label = { Text("Efectivo (33% desc.)") },
                            leadingIcon = if (formaPago == "EFECTIVO") {
                                { Icon(Icons.Default.CheckCircle, null, Modifier.size(18.dp)) }
                            } else null
                        )
                        
                        FilterChip(
                            selected = formaPago == "CREDITO",
                            onClick = { formaPago = "CREDITO" },
                            label = { Text("Crédito") },
                            leadingIcon = if (formaPago == "CREDITO") {
                                { Icon(Icons.Default.CheckCircle, null, Modifier.size(18.dp)) }
                            } else null
                        )
                    }
                    
                    if (formaPago == "CREDITO") {
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        OutlinedTextField(
                            value = numCuenta,
                            onValueChange = { numCuenta = it },
                            label = { Text("Número de Cuenta") },
                            leadingIcon = { Icon(Icons.Default.AccountBalance, null) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color(0xFF212121),
                                unfocusedTextColor = Color(0xFF212121),
                                focusedLabelColor = Color(0xFF212121),
                                unfocusedLabelColor = Color(0xFF757575)
                            )
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        OutlinedTextField(
                            value = plazoMeses,
                            onValueChange = { plazoMeses = it },
                            label = { Text("Plazo (meses)") },
                            leadingIcon = { Icon(Icons.Default.CalendarToday, null) },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
            
            // Resumen
            productoSeleccionado?.let { producto ->
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
                            "Resumen",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color(0xFF212121)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        val cant = cantidad.toIntOrNull() ?: 1
                        val totalBruto = producto.precioVenta.multiply(BigDecimal(cant))
                        val descuento = if (formaPago == "EFECTIVO") {
                            totalBruto.multiply(BigDecimal("0.33"))
                        } else BigDecimal.ZERO
                        val totalNeto = totalBruto.subtract(descuento)
                        
                        Text(
                            "Subtotal: $${String.format("%.2f", totalBruto)}",
                            color = Color(0xFF212121),
                            fontSize = 16.sp
                        )
                        if (descuento > BigDecimal.ZERO) {
                            Text(
                                "Descuento: -$${String.format("%.2f", descuento)}",
                                color = Color(0xFFD32F2F),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "TOTAL: $${String.format("%.2f", totalNeto)}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = Color(0xFF388E3C)
                        )
                    }
                }
            }
            
            // Botón evaluar crédito (solo si formaPago = CREDITO)
            if (formaPago == "CREDITO" && !creditoEvaluado) {
                Button(
                    onClick = {
                        if (cedula.isBlank() || numCuenta.isBlank() || productoSeleccionado == null) {
                            toastHelper.showError("Complete todos los campos para evaluar crédito")
                            return@Button
                        }
                        
                        val cant = cantidad.toIntOrNull() ?: 1
                        val montoSolicitado = productoSeleccionado!!.precioVenta.multiply(BigDecimal(cant))
                        val plazo = plazoMeses.toIntOrNull() ?: 12
                        
                        isLoading = true
                        scope.launch {
                            val solicitud = SolicitudCredito(
                                cedula = cedula,
                                precioProducto = montoSolicitado,
                                plazoMeses = plazo,
                                numCuentaCredito = numCuenta
                            )
                            
                            val resultado = creditoViewModel.evaluarCredito(solicitud)
                            isLoading = false
                            
                            resultado.fold(
                                onSuccess = { evaluacion ->
                                    creditoEvaluado = true
                                    sujetoCredito = evaluacion.sujetoCredito
                                    montoMaximo = evaluacion.montoMaximo
                                    motivoRechazo = evaluacion.motivo
                                    showEvaluacionDialog = true
                                    
                                    if (sujetoCredito) {
                                        toastHelper.showSuccess("Crédito APROBADO")
                                    } else {
                                        toastHelper.showError("Crédito RECHAZADO")
                                    }
                                },
                                onFailure = { error ->
                                    toastHelper.showError(error.message ?: "Error al evaluar crédito")
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
                        Icon(Icons.Default.Assessment, null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("EVALUAR CRÉDITO", fontWeight = FontWeight.Bold)
                    }
                }
            }
            
            // Mensaje de evaluación
            if (creditoEvaluado && formaPago == "CREDITO") {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(8.dp),
                    colors = CardDefaults.cardColors(
                        if (sujetoCredito) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .background(
                                Brush.horizontalGradient(
                                    colors = if (sujetoCredito) {
                                        listOf(Color(0xFFE8F5E9), Color(0xFFC8E6C9))
                                    } else {
                                        listOf(Color(0xFFFFEBEE), Color(0xFFFFCDD2))
                                    }
                                )
                            )
                            .padding(20.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                if (sujetoCredito) Icons.Default.CheckCircle else Icons.Default.Error,
                                null,
                                tint = if (sujetoCredito) Color(0xFF388E3C) else Color(0xFFD32F2F)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                if (sujetoCredito) "CRÉDITO APROBADO" else "CRÉDITO RECHAZADO",
                                fontWeight = FontWeight.Bold,
                                color = if (sujetoCredito) Color(0xFF388E3C) else Color(0xFFD32F2F)
                            )
                        }
                        if (sujetoCredito) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Monto máximo disponible: $${String.format("%.2f", montoMaximo)}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        } else {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Motivo: $motivoRechazo",
                                color = Color(0xFFD32F2F),
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            }
            
            // Botón facturar
            Button(
                onClick = {
                    if (cedula.isBlank() || nombreCliente.isBlank() || productoSeleccionado == null) {
                        toastHelper.showError("Complete todos los campos")
                        return@Button
                    }
                    
                    if (formaPago == "CREDITO") {
                        if (numCuenta.isBlank()) {
                            toastHelper.showError("Ingrese el número de cuenta")
                            return@Button
                        }
                        
                        if (!creditoEvaluado) {
                            toastHelper.showError("Debe evaluar el crédito primero")
                            return@Button
                        }
                        
                        if (!sujetoCredito) {
                            toastHelper.showError("Crédito no aprobado, no se puede facturar")
                            return@Button
                        }
                    }
                    
                    isLoading = true
                    scope.launch {
                        // Si es crédito, primero crear el crédito en BanQuito
                        var idCreditoCreado: Long? = null
                        
                        if (formaPago == "CREDITO" && sujetoCredito) {
                            val cant = cantidad.toIntOrNull() ?: 1
                            val montoSolicitado = productoSeleccionado!!.precioVenta.multiply(BigDecimal(cant))
                            val plazo = plazoMeses.toIntOrNull() ?: 12
                            
                            val solicitud = SolicitudCredito(
                                cedula = cedula,
                                precioProducto = montoSolicitado,
                                plazoMeses = plazo,
                                numCuentaCredito = numCuenta
                            )
                            
                            val resultCredito = creditoViewModel.crearCredito(solicitud)
                            
                            resultCredito.fold(
                                onSuccess = { credito ->
                                    idCreditoCreado = credito.id
                                    toastHelper.showSuccess("Crédito creado: ID ${credito.id}")
                                },
                                onFailure = { error ->
                                    isLoading = false
                                    toastHelper.showError("Error al crear crédito: ${error.message}")
                                    return@launch
                                }
                            )
                        }
                        
                        // Ahora crear la factura
                        val request = FacturaRequest(
                            cedulaCliente = cedula,
                            nombreCliente = nombreCliente,
                            idElectrodomestico = productoSeleccionado!!.id,
                            cantidad = cantidad.toIntOrNull() ?: 1,
                            formaPago = formaPago,
                            plazoMeses = if (formaPago == "CREDITO") plazoMeses.toIntOrNull() else null,
                            numCuentaCredito = if (formaPago == "CREDITO") numCuenta else null
                        )
                        
                        val result = facturaViewModel.crearFactura(request)
                        isLoading = false
                        
                        result.fold(
                            onSuccess = { factura ->
                                toastHelper.showSuccess("Factura creada exitosamente #${factura.id}")
                                navController.navigate(Screen.FacturaDetalle.createRoute(factura.id))
                            },
                            onFailure = { error ->
                                toastHelper.showError(error.message ?: "Error al crear factura")
                            }
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !isLoading && (formaPago == "EFECTIVO" || (formaPago == "CREDITO" && creditoEvaluado && sujetoCredito)),
                colors = ButtonDefaults.buttonColors(Color(0xFF388E3C))
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Icon(Icons.Default.Receipt, null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("GENERAR FACTURA", fontWeight = FontWeight.Bold)
                }
            }
        }
        }
    }
    
    // Diálogo de selección de producto mejorado
    if (showProductDialog && electroState is ElectrodomesticoState.Success) {
        AlertDialog(
            onDismissRequest = { showProductDialog = false },
            title = {
                Text(
                    "Seleccionar Producto",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            },
            text = {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items((electroState as ElectrodomesticoState.Success).electrodomesticos) { electro ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    productoSeleccionado = electro
                                    showProductDialog = false
                                    // Resetear evaluación cuando cambia el producto
                                    creditoEvaluado = false
                                    sujetoCredito = false
                                },
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(4.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (productoSeleccionado?.id == electro.id) {
                                    Color(0xFFFFE0B2)
                                } else {
                                    Color.White
                                }
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        Brush.horizontalGradient(
                                            colors = if (productoSeleccionado?.id == electro.id) {
                                                listOf(Color(0xFFFFE0B2), Color(0xFFFFCC80))
                                            } else {
                                                listOf(Color.White, Color(0xFFFFF3E0))
                                            }
                                        )
                                    )
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFFF6F00).copy(alpha = 0.1f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.Devices,
                                        contentDescription = null,
                                        tint = Color(0xFFFF6F00),
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                                
                                Spacer(modifier = Modifier.width(12.dp))
                                
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = electro.nombre,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = Color(0xFF212121)
                                    )
                                    Text(
                                        text = "Código: ${electro.codigo}",
                                        fontSize = 13.sp,
                                        color = Color(0xFF757575)
                                    )
                                }
                                
                                Text(
                                    text = "$${electro.precioVenta}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = Color(0xFF388E3C)
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showProductDialog = false }) {
                    Text("Cerrar", fontWeight = FontWeight.Bold)
                }
            }
        )
    }
    
    // Diálogo de resultado de evaluación
    if (showEvaluacionDialog) {
        AlertDialog(
            onDismissRequest = { showEvaluacionDialog = false },
            icon = {
                Icon(
                    if (sujetoCredito) Icons.Default.CheckCircle else Icons.Default.Error,
                    null,
                    tint = if (sujetoCredito) Color(0xFF388E3C) else Color(0xFFD32F2F),
                    modifier = Modifier.size(48.dp)
                )
            },
            title = { 
                Text(
                    if (sujetoCredito) "Crédito Aprobado" else "Crédito Rechazado",
                    fontWeight = FontWeight.Bold
                ) 
            },
            text = {
                Column {
                    if (sujetoCredito) {
                        Text("El cliente es sujeto de crédito.")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Monto máximo disponible:", fontWeight = FontWeight.Bold)
                        Text("$${String.format("%.2f", montoMaximo)}", fontSize = 24.sp, color = Color(0xFF388E3C))
                        
                        val cant = cantidad.toIntOrNull() ?: 1
                        val montoSolicitado = productoSeleccionado?.precioVenta?.multiply(BigDecimal(cant)) ?: BigDecimal.ZERO
                        
                        if (montoSolicitado > montoMaximo) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "⚠️ El monto solicitado ($${String.format("%.2f", montoSolicitado)}) excede el monto máximo disponible.",
                                color = Color(0xFFFF6F00)
                            )
                        }
                    } else {
                        Text("El cliente NO es sujeto de crédito.")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Motivo:", fontWeight = FontWeight.Bold)
                        Text(motivoRechazo, color = Color(0xFFD32F2F))
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showEvaluacionDialog = false }) {
                    Text("Entendido")
                }
            }
        )
    }
}
