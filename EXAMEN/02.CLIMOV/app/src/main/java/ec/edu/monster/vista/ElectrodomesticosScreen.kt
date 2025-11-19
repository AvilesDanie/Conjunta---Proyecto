package ec.edu.monster.vista

import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import ec.edu.monster.controlador.ElectrodomesticoState
import ec.edu.monster.controlador.ElectrodomesticoViewModel
import ec.edu.monster.modelo.ElectrodomesticoResponse
import ec.edu.monster.util.RetrofitClient
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ElectrodomesticosScreen(
    navController: NavController,
    viewModel: ElectrodomesticoViewModel = viewModel()
) {
    val electroState by viewModel.electroState.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.cargarElectrodomesticos()
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Productos") },
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
                onClick = { navController.navigate(Screen.CrearElectrodomestico.route) },
                containerColor = Color(0xFFFF6F00)
            ) {
                Icon(Icons.Default.Add, "Agregar producto", tint = Color.White)
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
            when (val state = electroState) {
                is ElectrodomesticoState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Color(0xFFFF6F00)
                    )
                }
                is ElectrodomesticoState.Success -> {
                    if (state.electrodomesticos.isEmpty()) {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.Devices,
                                contentDescription = null,
                                modifier = Modifier.size(80.dp),
                                tint = Color.White.copy(alpha = 0.7f)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "No hay productos registrados",
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
                            items(state.electrodomesticos) { electro ->
                                ElectrodomesticoCard(
                                    electrodomestico = electro,
                                    onEdit = { 
                                        navController.navigate(Screen.EditarElectrodomestico.createRoute(electro.id))
                                    },
                                    onDelete = {
                                        // Implementar diálogo de confirmación
                                    },
                                    viewModel = viewModel
                                )
                            }
                        }
                    }
                }
                is ElectrodomesticoState.Error -> {
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
                        Button(onClick = { viewModel.cargarElectrodomesticos() }) {
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
fun ElectrodomesticoCard(
    electrodomestico: ElectrodomesticoResponse,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    viewModel: ElectrodomesticoViewModel
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var isDeleting by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color.White,
                            Color(0xFFFFF3E0)
                        )
                    )
                )
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen del producto
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF5F5F5)),
                contentAlignment = Alignment.Center
            ) {
                if (!electrodomestico.imagenUrl.isNullOrBlank()) {
                    val imageUrl = "${RetrofitClient.BASE_URL_COMERCIALIZADORA}${electrodomestico.imagenUrl}"
                    Image(
                        painter = rememberAsyncImagePainter(imageUrl),
                        contentDescription = "Imagen de ${electrodomestico.nombre}",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        Icons.Default.Devices,
                        contentDescription = null,
                        tint = Color(0xFFFF6F00),
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = electrodomestico.nombre,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = Color(0xFF212121)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Código: ${electrodomestico.codigo}",
                    fontSize = 14.sp,
                    color = Color(0xFF757575)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$${String.format("%.2f", electrodomestico.precioVenta)}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF388E3C)
                )
            }
            
            // Botones de acción
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Botón Editar
                IconButton(
                    onClick = onEdit,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Editar",
                        tint = Color(0xFF1976D2)
                    )
                }
                
                // Botón Eliminar
                IconButton(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier.size(40.dp),
                    enabled = !isDeleting
                ) {
                    if (isDeleting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color(0xFFD32F2F),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Eliminar",
                            tint = Color(0xFFD32F2F)
                        )
                    }
                }
            }
        }
    }
    
    // Diálogo de confirmación para eliminar
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirmar eliminación") },
            text = { 
                Text("¿Está seguro que desea eliminar el producto '${electrodomestico.nombre}'? Esta acción no se puede deshacer.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        isDeleting = true
                        showDeleteDialog = false
                        scope.launch {
                            val result = viewModel.eliminarElectrodomestico(electrodomestico.id)
                            result.fold(
                                onSuccess = {
                                    viewModel.cargarElectrodomesticos()
                                },
                                onFailure = { error ->
                                    // Mostrar error
                                }
                            )
                            isDeleting = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(Color(0xFFD32F2F))
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
