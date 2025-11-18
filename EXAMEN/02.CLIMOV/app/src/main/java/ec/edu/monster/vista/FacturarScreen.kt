package ec.edu.monster.vista

import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import ec.edu.monster.controlador.CreditoViewModel
import ec.edu.monster.controlador.ElectrodomesticoState
import ec.edu.monster.controlador.ElectrodomesticoViewModel
import ec.edu.monster.controlador.FacturaViewModel
import ec.edu.monster.modelo.ElectrodomesticoResponse
import ec.edu.monster.modelo.FacturaRequest
import ec.edu.monster.modelo.SolicitudCredito
import ec.edu.monster.util.RetrofitClient
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
    
    // Lista de productos seleccionados con cantidades
    data class ProductoConCantidad(
        val producto: ElectrodomesticoResponse,
        var cantidad: Int
    )
    var productosSeleccionados by remember { mutableStateOf<List<ProductoConCantidad>>(emptyList()) }
    
    var formaPago by remember { mutableStateOf("EFECTIVO") }
    var plazoMeses by remember { mutableStateOf("12") }
    var numCuenta by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var showProductDialog by remember { mutableStateOf(false) }
    var cantidadTemporal by remember { mutableStateOf("1") }
    
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
                    Text("Productos", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF212121))
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Button(
                        onClick = { showProductDialog = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF6F00)
                        )
                    ) {
                        Icon(Icons.Default.Add, null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Agregar Producto")
                    }
                    
                    // Lista de productos seleccionados
                    if (productosSeleccionados.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            "Productos Agregados:",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF212121)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        productosSeleccionados.forEach { item ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFFFF3E0)
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Imagen del producto
                                    Card(
                                        modifier = Modifier.size(60.dp),
                                        shape = RoundedCornerShape(6.dp),
                                        elevation = CardDefaults.cardElevation(2.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color.White)
                                    ) {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            if (!item.producto.imagenUrl.isNullOrBlank()) {
                                                val imageUrl = "${RetrofitClient.BASE_URL_COMERCIALIZADORA}${item.producto.imagenUrl}"
                                                Image(
                                                    painter = rememberAsyncImagePainter(imageUrl),
                                                    contentDescription = item.producto.nombre,
                                                    modifier = Modifier.fillMaxSize(),
                                                    contentScale = ContentScale.Crop
                                                )
                                            } else {
                                                Icon(
                                                    Icons.Default.Image,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(30.dp),
                                                    tint = Color(0xFF9E9E9E)
                                                )
                                            }
                                        }
                                    }
                                    
                                    Spacer(modifier = Modifier.width(10.dp))
                                    
                                    // Información del producto
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            item.producto.nombre,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF212121)
                                        )
                                        Text(
                                            "Cant: ${item.cantidad} × $${String.format("%.2f", item.producto.precioVenta)}",
                                            fontSize = 12.sp,
                                            color = Color(0xFF757575)
                                        )
                                    }
                                    
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            "$${String.format("%.2f", item.producto.precioVenta.toDouble() * item.cantidad)}",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF388E3C)
                                        )
                                        IconButton(
                                            onClick = {
                                                productosSeleccionados = productosSeleccionados.filter { it.producto.id != item.producto.id }
                                                creditoEvaluado = false
                                                sujetoCredito = false
                                            },
                                            modifier = Modifier.size(24.dp)
                                        ) {
                                            Icon(
                                                Icons.Default.Delete,
                                                contentDescription = "Eliminar",
                                                tint = Color(0xFFD32F2F),
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        
                        // Total
                        Spacer(modifier = Modifier.height(8.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF388E3C))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    "TOTAL:",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    "$${String.format("%.2f", productosSeleccionados.sumOf { it.producto.precioVenta.toDouble() * it.cantidad })}",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
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
            
            // Botón evaluar crédito (solo si formaPago = CREDITO)
            if (formaPago == "CREDITO" && !creditoEvaluado) {
                Button(
                    onClick = {
                        if (cedula.isBlank() || numCuenta.isBlank() || productosSeleccionados.isEmpty()) {
                            toastHelper.showError("Complete todos los campos para evaluar crédito")
                            return@Button
                        }
                        
                        val montoSolicitado = productosSeleccionados.sumOf { 
                            (it.producto.precioVenta * BigDecimal(it.cantidad)).toDouble()
                        }.toBigDecimal()
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
                    if (cedula.isBlank() || nombreCliente.isBlank() || productosSeleccionados.isEmpty()) {
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
                        // Crear la factura con múltiples productos
                        // Comercializadora se encarga de crear el crédito en BanQuito si es necesario
                        val productosRequest = productosSeleccionados.map { 
                            ec.edu.monster.modelo.DetalleProductoRequest(
                                idElectrodomestico = it.producto.id,
                                cantidad = it.cantidad
                            )
                        }
                        
                        val request = FacturaRequest(
                            cedulaCliente = cedula,
                            nombreCliente = nombreCliente,
                            productos = productosRequest,
                            formaPago = formaPago,
                            plazoMeses = if (formaPago == "CREDITO") plazoMeses.toIntOrNull() else null,
                            numCuentaCredito = if (formaPago == "CREDITO") numCuenta else null
                        )
                        
                        val result = facturaViewModel.crearFactura(request)
                        isLoading = false
                        
                        result.fold(
                            onSuccess = { factura ->
                                toastHelper.showSuccess("✅ Factura y crédito creados exitosamente #${factura.id}")
                                navController.navigate(Screen.FacturaDetalle.createRoute(factura.id))
                            },
                            onFailure = { error ->
                                val errorMsg = error.message ?: "Error desconocido"
                                toastHelper.showError("Error: $errorMsg")
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
        var productoTemporal by remember { mutableStateOf<ElectrodomesticoResponse?>(null) }
        
        AlertDialog(
            onDismissRequest = { 
                showProductDialog = false
                productoTemporal = null
                cantidadTemporal = "1"
            },
            title = {
                Text(
                    "Seleccionar Producto",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            },
            text = {
                Column {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(350.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items((electroState as ElectrodomesticoState.Success).electrodomesticos) { electro ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        productoTemporal = electro
                                    },
                                shape = RoundedCornerShape(12.dp),
                                elevation = CardDefaults.cardElevation(4.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (productoTemporal?.id == electro.id) {
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
                                                colors = if (productoTemporal?.id == electro.id) {
                                                    listOf(Color(0xFFFFE0B2), Color(0xFFFFCC80))
                                                } else {
                                                    listOf(Color.White, Color(0xFFFFF3E0))
                                                }
                                            )
                                        )
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Imagen del producto
                                    Box(
                                        modifier = Modifier
                                            .size(64.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(Color(0xFFF5F5F5)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (!electro.imagenUrl.isNullOrBlank()) {
                                            val imageUrl = "${RetrofitClient.BASE_URL_COMERCIALIZADORA}${electro.imagenUrl}"
                                            Image(
                                                painter = rememberAsyncImagePainter(imageUrl),
                                                contentDescription = "Imagen de ${electro.nombre}",
                                                modifier = Modifier.fillMaxSize(),
                                                contentScale = ContentScale.Crop
                                            )
                                        } else {
                                            Icon(
                                                Icons.Default.Devices,
                                                contentDescription = null,
                                                tint = Color(0xFFFF6F00),
                                                modifier = Modifier.size(32.dp)
                                            )
                                        }
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
                    
                    // Campo de cantidad
                    if (productoTemporal != null) {
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = cantidadTemporal,
                            onValueChange = { cantidadTemporal = it },
                            label = { Text("Cantidad") },
                            leadingIcon = { Icon(Icons.Default.ShoppingCart, null) },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (productoTemporal != null) {
                            val cant = cantidadTemporal.toIntOrNull() ?: 1
                            if (cant > 0) {
                                // Verificar si el producto ya está en la lista
                                val productoExistente = productosSeleccionados.find { it.producto.id == productoTemporal!!.id }
                                if (productoExistente != null) {
                                    // Actualizar cantidad
                                    productosSeleccionados = productosSeleccionados.map {
                                        if (it.producto.id == productoTemporal!!.id) {
                                            it.copy(cantidad = it.cantidad + cant)
                                        } else it
                                    }
                                } else {
                                    // Agregar nuevo producto
                                    productosSeleccionados = productosSeleccionados + ProductoConCantidad(productoTemporal!!, cant)
                                }
                                showProductDialog = false
                                productoTemporal = null
                                cantidadTemporal = "1"
                                // Resetear evaluación cuando cambian productos
                                creditoEvaluado = false
                                sujetoCredito = false
                            } else {
                                toastHelper.showError("Cantidad inválida")
                            }
                        }
                    },
                    enabled = productoTemporal != null
                ) {
                    Text("AGREGAR")
                }
            },
            dismissButton = {
                TextButton(onClick = { 
                    showProductDialog = false
                    productoTemporal = null
                    cantidadTemporal = "1"
                }) {
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
                        
                        val montoSolicitado = productosSeleccionados.sumOf { 
                            (it.producto.precioVenta.toDouble() * it.cantidad)
                        }.toBigDecimal()
                        
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
