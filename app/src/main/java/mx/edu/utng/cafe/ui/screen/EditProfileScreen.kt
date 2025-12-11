package mx.edu.utng.cafe.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import mx.edu.utng.cafe.viewModell.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavController,
    viewModel: UsuarioViewModel = viewModel()
) {
    // Intentamos obtener el usuario actual. Si es nulo (no debería ocurrir si la navegación es correcta),
    // mostramos un indicador de carga o regresamos.
    val usuario = viewModel.usuario

    // 1. Estados mutables para los campos de edición.
    // Inicializados con los datos del ViewModel, o vacíos si es nulo (aunque deberíamos tener usuario).
    var nombreText by remember(usuario) { mutableStateOf(usuario?.nombre ?: "") }
    var universidadText by remember(usuario) { mutableStateOf(usuario?.universidad ?: "") }

    // 2. Estado para controlar si estamos guardando (para deshabilitar el botón y mostrar progreso)
    var isSaving by remember { mutableStateOf(false) }

    // 3. Chequeo para habilitar el botón: solo si hay cambios Y el usuario no es nulo.
    val hasChanges = usuario != null && (nombreText != usuario.nombre || universidadText != usuario.universidad)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Perfil") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->

        if (usuario == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Campo Nombre
            OutlinedTextField(
                value = nombreText,
                onValueChange = { nombreText = it },
                label = { Text("Nombre Completo") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Campo Universidad
            OutlinedTextField(
                value = universidadText,
                onValueChange = { universidadText = it },
                label = { Text("Universidad") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Correo (No editable)
            Text(
                text = "Correo: ${usuario.correo}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botón de Guardar Cambios
            Button(
                onClick = {
                    if (usuario != null) {
                        isSaving = true

                        // Llamar a la función del ViewModel para guardar en Firebase
                        viewModel.updateUsuario(
                            nuevoNombre = nombreText,
                            nuevaUniversidad = universidadText
                        )

                        // Después de iniciar la operación, regresar inmediatamente
                        // (El estado del ViewModel se actualizará en background)
                        navController.popBackStack()
                        isSaving = false
                    }
                },
                // Deshabilitar si está guardando o si no hay cambios
                enabled = !isSaving && hasChanges,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 3.dp
                    )
                } else {
                    Text("Guardar Cambios")
                }
            }
        }
    }
}