package mx.edu.utng.cafe.viewModell

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import mx.edu.utng.cafe.model.Usuario
import mx.edu.utng.cafe.data.UsuarioRepository

// Archivo: mx.edu.utng.cafe.viewModell.UsuarioViewModel.kt

class UsuarioViewModel : ViewModel() {
    var usuario by mutableStateOf<Usuario?>(null)
        private set
    var loading by mutableStateOf(false)
        private set

    // 1. Nueva Función: Cargar el usuario actual
    fun loadCurrentUser() {
        viewModelScope.launch {
            loading = true
            // --- Lógica de consulta a Firebase ---
            try {
                // Obtener el ID del usuario de FirebaseAuth
                val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch

                // Consultar Firestore
                val userDoc = FirebaseFirestore.getInstance()
                    .collection("usuarios")
                    .document(userId)
                    .get()
                    .await() // Se usa .await() si estás en una corrutina

                usuario = userDoc.toObject(Usuario::class.java)

            } catch (e: Exception) {
                // Manejo de errores
                println("Error cargando usuario: ${e.message}")
            } finally {
                loading = false
            }
            // -------------------------------------
        }
    }

    // 2. Nueva Función: Lógica de Cerrar Sesión
    fun logout() {
        FirebaseAuth.getInstance().signOut()
        // Aquí podrías limpiar el estado del usuario o navegar a la pantalla de Login
        usuario = null
    }
}