package mx.edu.utng.cafe.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mx.edu.utng.cafe.data.UsuarioRepository
import mx.edu.utng.cafe.data.PromocionRepository
import mx.edu.utng.cafe.model.Promocion
import mx.edu.utng.cafe.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class homeViewmodel(
    private val usuarioRepo: UsuarioRepository = UsuarioRepository(),
    private val promocionRepo: PromocionRepository = PromocionRepository(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : ViewModel() {

    /* ---------- UI STATES ---------- */

    var usuario by mutableStateOf<Usuario?>(null)
        private set

    var promos by mutableStateOf<List<Promocion>>(emptyList())
        private set

    var loading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set


    /* ---------- INIT (se carga automáticamente) ---------- */
    init {
        cargarDatos()
    }


    /* ---------- FUNCIÓN PRINCIPAL ---------- */
    private fun cargarDatos() {

        val uid = auth.currentUser?.uid

        if (uid == null) {
            error = "Usuario no autenticado"
            loading = false
            return
        }

        viewModelScope.launch {
            try {
                loading = true
                error = null

                // 🔥 Obtener usuario del UID autenticado
                usuario = usuarioRepo.obtenerUsuarioLogueado(
                    uid = uid
                )

                // 🔥 Obtener promociones
                val result = promocionRepo.getAllPromocionesOnce(Promocion())
                promos = result.getOrElse { emptyList() }

            } catch (e: Exception) {
                error = e.message ?: "Error inesperado"
            } finally {
                loading = false
            }
        }
    }
}
