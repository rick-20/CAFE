package mx.edu.utng.cafe.viewModell

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import mx.edu.utng.cafe.model.Usuario
import mx.edu.utng.cafe.data.UsuarioRepository

// Archivo: mx.edu.utng.cafe.viewModell.UsuarioViewModel.kt

class UsuarioViewModel(
    // 🔥 Inyectamos el Repository
    private val repository: UsuarioRepository = UsuarioRepository()
) : ViewModel() {

    var usuario by mutableStateOf<Usuario?>(null)
        private set
    var loading by mutableStateOf(false)
        private set
    var error by mutableStateOf<String?>(null)
        private set

    // ===============================================
    // 1. CARGA DE USUARIO LOGUEADO (USANDO REPOSITORY)
    // ===============================================
    fun loadCurrentUser() {
        // Ejecutar si el usuario actual es nulo o si se necesita recargar
        if (usuario != null) return

        viewModelScope.launch {
            loading = true
            error = null

            try {
                // Obtener el ID del usuario de FirebaseAuth
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                if (userId == null) {
                    usuario = null
                    loading = false
                    return@launch
                }

                // 🔥 Usar el método del repository para obtener el usuario
                // Nota: Usaremos el método 'obtenerUsuario' del repository asumiendo
                // que la colección del repository es "users" y no "usuarios" como antes.
                usuario = repository.obtenerUsuario(userId)

            } catch (e: Exception) {
                error = "Error al cargar usuario: ${e.message}"
                println(error)
            } finally {
                loading = false
            }
        }
    }

    // ===============================================
    // 2. LÓGICA DE CERRAR SESIÓN
    // ===============================================
    fun logout() {
        FirebaseAuth.getInstance().signOut()
        usuario = null
        error = null
        loading = false
    }

    // ===============================================
    // 3. ACTUALIZAR PERFIL (USANDO REPOSITORY)
    // ===============================================
    fun updateUser(nuevoNombre: String, nuevaUniversidad: String) {
        viewModelScope.launch {
            error = null
            val currentUsuario = usuario ?: return@launch

            // Creamos un objeto Usuario actualizado localmente para el repositorio
            val usuarioActualizado = currentUsuario.copy(
                nombre = nuevoNombre,
                universidad = nuevaUniversidad
                // Asegúrate de que idU tenga el UID de Firebase para la actualización
                // Asumiendo que el campo 'idU' en el modelo Usuario es el UID de Firestore.
            )

            // Si el idU está vacío (lo cual no debería pasar si fue cargado correctamente), salimos.
            if (usuarioActualizado.idU.isBlank()) {
                error = "No se pudo obtener el ID del usuario para actualizar."
                return@launch
            }

            // 🔥 Usar el método del repository para actualizar los datos
            val result = repository.updateUsuario(usuarioActualizado)

            result.fold(
                onSuccess = {
                    // Actualizamos el estado local del ViewModel tras el éxito
                    usuario = usuarioActualizado
                },
                onFailure = { e ->
                    error = "Fallo al guardar cambios: ${e.message}"
                }
            )
        }
    }
}