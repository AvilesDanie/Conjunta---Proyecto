package ec.edu.monster.vista

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import ec.edu.monster.controlador.ElectrodomesticoState
import ec.edu.monster.controlador.ElectrodomesticoViewModel
import ec.edu.monster.modelo.ElectrodomesticoResponse
import ec.edu.monster.util.RetrofitClient
import ec.edu.monster.util.rememberToastHelper
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarElectrodomesticoScreen(
    navController: NavController,
    idElectrodomestico: Long,
    viewModel: ElectrodomesticoViewModel = viewModel()
) {
    val toastHelper = rememberToastHelper()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val electroState by viewModel.electroState.collectAsState()
    
    var electrodomestico by remember { mutableStateOf<ElectrodomesticoResponse?>(null) }
    var codigo by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var imagenUri by remember { mutableStateOf<Uri?>(null) }
    var nombreImagen by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var cambiarImagen by remember { mutableStateOf(false) }
    
    // Cargar datos del electrodoméstico
    LaunchedEffect(Unit) {
        viewModel.cargarElectrodomesticos()
    }
    
    // Actualizar campos cuando se carguen los datos
    LaunchedEffect(electroState) {
        if (electroState is ElectrodomesticoState.Success) {
            val electro = (electroState as ElectrodomesticoState.Success)
                .electrodomesticos
                .find { it.id == idElectrodomestico }
            
            if (electro != null) {
                electrodomestico = electro
                codigo = electro.codigo
                nombre = electro.nombre
                precio = electro.precioVenta.toString()
            }
        }
    }
    
    // Launcher para seleccionar imagen de la galería
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imagenUri = it
            nombreImagen = obtenerNombreArchivoEditar(context, it)
            cambiarImagen = true
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Producto") },
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
                            Color(0xFF1565C0),
                            Color(0xFF1976D2),
                            Color(0xFF1E88E5)
                        )
                    )
                )
        ) {
            if (electrodomestico == null) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.White
                )
            } else {
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
                                            Color(0xFFE3F2FD)
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
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            // Sección de imagen
                            Text(
                                "Imagen del Producto",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                color = Color(0xFF757575)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            // Preview de la imagen
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFF5F5F5))
                                    .border(
                                        width = 2.dp,
                                        color = if (cambiarImagen) Color(0xFF4CAF50) else Color(0xFF1976D2),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .clickable { imagePickerLauncher.launch("image/*") },
                                contentAlignment = Alignment.Center
                            ) {
                                when {
                                    // Nueva imagen seleccionada
                                    imagenUri != null -> {
                                        Image(
                                            painter = rememberAsyncImagePainter(imagenUri),
                                            contentDescription = "Nueva imagen",
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                    // Imagen existente
                                    !electrodomestico!!.imagenUrl.isNullOrBlank() -> {
                                        val imageUrl = "${RetrofitClient.BASE_URL_COMERCIALIZADORA}${electrodomestico!!.imagenUrl}"
                                        Image(
                                            painter = rememberAsyncImagePainter(imageUrl),
                                            contentDescription = "Imagen actual",
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                    // Sin imagen
                                    else -> {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            Icon(
                                                Icons.Default.AddPhotoAlternate,
                                                contentDescription = null,
                                                modifier = Modifier.size(64.dp),
                                                tint = Color(0xFF9E9E9E)
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                "Toca para cambiar imagen",
                                                fontSize = 14.sp,
                                                color = Color(0xFF757575),
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                }
                            }
                            
                            // Mostrar nombre del archivo seleccionado o actual
                            if (nombreImagen != null || !electrodomestico!!.imagenUrl.isNullOrBlank()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        if (cambiarImagen) Icons.Default.CheckCircle else Icons.Default.Image,
                                        contentDescription = null,
                                        tint = if (cambiarImagen) Color(0xFF4CAF50) else Color(0xFF1976D2),
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        nombreImagen ?: "Imagen actual",
                                        fontSize = 12.sp,
                                        color = Color(0xFF424242),
                                        modifier = Modifier.weight(1f)
                                    )
                                    if (cambiarImagen) {
                                        IconButton(
                                            onClick = {
                                                imagenUri = null
                                                nombreImagen = null
                                                cambiarImagen = false
                                            }
                                        ) {
                                            Icon(
                                                Icons.Default.Close,
                                                contentDescription = "Cancelar cambio",
                                                tint = Color(0xFFE53935)
                                            )
                                        }
                                    }
                                }
                            }
                            
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
                                        try {
                                            // Convertir Uri a File solo si se cambió la imagen
                                            val file = if (cambiarImagen && imagenUri != null) {
                                                uriToFileEditar(context, imagenUri!!)
                                            } else {
                                                null
                                            }
                                            
                                            val result = viewModel.actualizarElectrodomestico(
                                                id = idElectrodomestico,
                                                codigo = codigo,
                                                nombre = nombre,
                                                precioVenta = BigDecimal(precioDecimal),
                                                imagenFile = file
                                            )
                                            
                                            isLoading = false
                                            
                                            result.fold(
                                                onSuccess = {
                                                    toastHelper.showSuccess("Producto actualizado exitosamente")
                                                    navController.navigateUp()
                                                },
                                                onFailure = { error ->
                                                    toastHelper.showError(error.message ?: "Error al actualizar producto")
                                                }
                                            )
                                        } catch (e: Exception) {
                                            isLoading = false
                                            toastHelper.showError("Error: ${e.message}")
                                        }
                                    }
                                },
                                modifier = Modifier.fillMaxWidth().height(56.dp),
                                enabled = !isLoading,
                                colors = ButtonDefaults.buttonColors(Color(0xFF1976D2))
                            ) {
                                if (isLoading) {
                                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                                } else {
                                    Icon(Icons.Default.Save, null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("ACTUALIZAR PRODUCTO", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// Funciones auxiliares
private fun obtenerNombreArchivoEditar(context: Context, uri: Uri): String? {
    var nombre: String? = null
    context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
        val nombreIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        if (cursor.moveToFirst() && nombreIndex >= 0) {
            nombre = cursor.getString(nombreIndex)
        }
    }
    return nombre
}

private fun uriToFileEditar(context: Context, uri: Uri): File {
    val inputStream = context.contentResolver.openInputStream(uri)
        ?: throw IllegalArgumentException("No se pudo abrir el archivo")
    
    val fileName = obtenerNombreArchivoEditar(context, uri) ?: "imagen_${System.currentTimeMillis()}.jpg"
    val file = File(context.cacheDir, fileName)
    
    FileOutputStream(file).use { outputStream ->
        inputStream.copyTo(outputStream)
    }
    inputStream.close()
    
    return file
}
