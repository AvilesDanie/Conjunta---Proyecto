package ec.edu.monster.vista

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ec.edu.monster.controlador.UsuarioViewModel
import ec.edu.monster.controlador.UsuarioState
import ec.edu.monster.modelo.UsuarioRequest
import ec.edu.monster.modelo.UsuarioResponse
import ec.edu.monster.util.rememberToastHelper
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsuariosScreen(
    navController: NavController,
    viewModel: UsuarioViewModel = viewModel()
) {
    val toastHelper = rememberToastHelper()
    val scope = rememberCoroutineScope()
    val usuarioState by viewModel.usuarioState.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.listarUsuarios()
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestión de Usuarios") },
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
                onClick = {
                    toastHelper.showInfo("Función de crear usuario próximamente")
                },
                containerColor = Color(0xFF2E7D32)
            ) {
                Icon(Icons.Default.Add, "Crear Usuario", tint = Color.White)
            }
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
                .padding(paddingValues)
        ) {
            when (val state = usuarioState) {
                is UsuarioState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.White)
                    }
                }
                
                is UsuarioState.Success -> {
                    if (state.usuarios.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    Icons.Default.People,
                                    null,
                                    modifier = Modifier.size(80.dp),
                                    tint = Color.White.copy(alpha = 0.7f)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    "No hay usuarios registrados",
                                    color = Color.White,
                                    fontSize = 18.sp
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(state.usuarios) { usuario ->
                                UsuarioCard(
                                    usuario = usuario,
                                    navController = navController,
                                    viewModel = viewModel,
                                    onUsuarioEliminado = { viewModel.listarUsuarios() }
                                )
                            }
                        }
                    }
                }
                
                is UsuarioState.Error -> {
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
                            Text(
                                state.message,
                                color = Color.White,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
                
                else -> {}
            }
        }
    }
}

@Composable
fun UsuarioCard(
    usuario: UsuarioResponse,
    navController: NavController,
    viewModel: UsuarioViewModel,
    onUsuarioEliminado: () -> Unit
) {
    val toastHelper = rememberToastHelper()
    val scope = rememberCoroutineScope()
    var showDeleteDialog by remember { mutableStateOf(false) }
    
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirmar Eliminación") },
            text = { Text("¿Está seguro de eliminar el usuario '${usuario.username}'? Esta acción no se puede deshacer.") },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            val result = viewModel.eliminarUsuario(usuario.id)
                            if (result.isSuccess) {
                                toastHelper.showSuccess("Usuario eliminado exitosamente")
                                onUsuarioEliminado()
                            } else {
                                toastHelper.showError("Error al eliminar usuario")
                            }
                        }
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
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
                        colors = listOf(
                            Color.White,
                            Color(0xFFF5F5FF)
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1565C0).copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Person,
                        null,
                        tint = Color(0xFF1565C0),
                        modifier = Modifier.size(32.dp)
                    )
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        usuario.username,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color(0xFF1A1A1A)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = when (usuario.rol) {
                                "SUPERADMIN" -> Color(0xFFE53935).copy(alpha = 0.1f)
                                "ADMIN" -> Color(0xFFFB8C00).copy(alpha = 0.1f)
                                else -> Color(0xFF43A047).copy(alpha = 0.1f)
                            }
                        ) {
                            Text(
                                usuario.rol,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                color = when (usuario.rol) {
                                    "SUPERADMIN" -> Color(0xFFE53935)
                                    "ADMIN" -> Color(0xFFFB8C00)
                                    else -> Color(0xFF43A047)
                                },
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (usuario.activo) Color(0xFF43A047).copy(alpha = 0.1f) else Color(0xFF757575).copy(alpha = 0.1f)
                        ) {
                            Text(
                                if (usuario.activo) "Activo" else "Inactivo",
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                color = if (usuario.activo) Color(0xFF43A047) else Color(0xFF757575),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { 
                        navController.navigate(Screen.EditarUsuario.createRoute(usuario.id))
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF1976D2)
                    )
                ) {
                    Icon(Icons.Default.Edit, null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Editar", fontSize = 13.sp)
                }
                
                OutlinedButton(
                    onClick = { 
                        showDeleteDialog = true
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFD32F2F)
                    )
                ) {
                    Icon(Icons.Default.Delete, null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Eliminar", fontSize = 13.sp)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarUsuarioScreen(
    navController: NavController,
    id: Long,
    viewModel: UsuarioViewModel = viewModel()
) {
    val toastHelper = rememberToastHelper()
    val scope = rememberCoroutineScope()
    val usuarioDetailState by viewModel.usuarioDetailState.collectAsState()
    
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rol by remember { mutableStateOf("USER") }
    var activo by remember { mutableStateOf(true) }
    var isLoading by remember { mutableStateOf(false) }
    var isDataLoaded by remember { mutableStateOf(false) }
    
    LaunchedEffect(id) {
        viewModel.obtenerUsuario(id)
    }
    
    LaunchedEffect(usuarioDetailState) {
        if (usuarioDetailState is ec.edu.monster.controlador.UsuarioDetailState.Success && !isDataLoaded) {
            val usuario = (usuarioDetailState as ec.edu.monster.controlador.UsuarioDetailState.Success).usuario
            username = usuario.username
            rol = usuario.rol
            activo = usuario.activo
            isDataLoaded = true
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Usuario") },
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
        when (usuarioDetailState) {
            is ec.edu.monster.controlador.UsuarioDetailState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            }
            is ec.edu.monster.controlador.UsuarioDetailState.Success -> {
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
                                colors = listOf(Color.White, Color(0xFFF5F5FF))
                            )
                        )
                        .padding(20.dp)
                ) {
                    Text(
                        "Editar Datos del Usuario",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color(0xFF212121)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Nombre de Usuario *") },
                leadingIcon = { Icon(Icons.Default.Person, null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color(0xFF212121),
                    unfocusedTextColor = Color(0xFF212121),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )
            
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Nueva Contraseña (dejar vacío para no cambiar)") },
                leadingIcon = { Icon(Icons.Default.Lock, null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color(0xFF212121),
                    unfocusedTextColor = Color(0xFF212121),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )
            
            var expandedRol by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedRol,
                onExpandedChange = { expandedRol = it }
            ) {
                OutlinedTextField(
                    value = rol,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Rol *") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedRol) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color(0xFF212121),
                        unfocusedTextColor = Color(0xFF212121),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )
                ExposedDropdownMenu(
                    expanded = expandedRol,
                    onDismissRequest = { expandedRol = false }
                ) {
                    listOf("SUPERADMIN", "ADMIN", "USER").forEach { r ->
                        DropdownMenuItem(
                            text = { Text(r) },
                            onClick = {
                                rol = r
                                expandedRol = false
                            }
                        )
                    }
                }
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = activo,
                    onCheckedChange = { activo = it }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Usuario Activo", color = Color(0xFF212121))
            }
                }
            }
            
            Button(
                onClick = {
                    if (username.isBlank()) {
                        toastHelper.showError("El nombre de usuario es requerido")
                        return@Button
                    }
                    
                    isLoading = true
                    scope.launch {
                        val request = UsuarioRequest(
                            username = username,
                            password = password.ifBlank { "" },
                            rol = rol,
                            activo = activo
                        )
                        
                        val result = viewModel.actualizarUsuario(id, request)
                        isLoading = false
                        
                        result.fold(
                            onSuccess = {
                                toastHelper.showSuccess("Usuario actualizado exitosamente")
                                navController.navigateUp()
                            },
                            onFailure = { error ->
                                toastHelper.showError(error.message ?: "Error al actualizar usuario")
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
                    Icon(Icons.Default.Save, null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("GUARDAR CAMBIOS", fontWeight = FontWeight.Bold)
                }
            }
        }
            }
            is ec.edu.monster.controlador.UsuarioDetailState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Error, null, tint = Color(0xFFFF6B6B), modifier = Modifier.size(64.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text((usuarioDetailState as ec.edu.monster.controlador.UsuarioDetailState.Error).message, color = Color.White)
                    }
                }
            }
            else -> {}
        }
        }
    }
}
