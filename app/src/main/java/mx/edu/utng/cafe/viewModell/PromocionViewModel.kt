package mx.edu.utng.cafe.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import mx.edu.utng.cafe.data.PromocionRepository
import mx.edu.utng.cafe.model.Promocion

class PromocionViewModel(
    private val repository: PromocionRepository = PromocionRepository()
) : ViewModel() {

    /* ===================== STATE ===================== */
    private val _promos = MutableStateFlow<List<Promocion>>(emptyList())
    val promos: StateFlow<List<Promocion>> = _promos.asStateFlow()

    private var listener: ListenerRegistration? = null

    init {
        listenPromociones()
    }

    /* ===================== READ (Tiempo real) ===================== */
    private fun listenPromociones() {
        listener = repository.addPromocionesListener { list ->
            _promos.value = list
        }
    }

    /* ===================== CREATE ===================== */
    fun addPromocion(
        nombre: String,
        descripcion: String,
        puntosRequeridos: String,
        fechaInicio: String,
        fechaFin: String
    ) {
        viewModelScope.launch {
            repository.addPromocion(
                Promocion(
                    nombre = nombre,
                    descripcion = descripcion,
                    puntosRequeridos = puntosRequeridos,
                    fechaInicio = fechaInicio,
                    fechaFin = fechaFin
                )
            )
        }
    }

    /* ===================== UPDATE ===================== */
    fun updatePromocion(promocion: Promocion) {
        viewModelScope.launch {
            repository.updatePromocion(promocion)
        }
    }

    /* ===================== DELETE ===================== */
    fun deletePromocion(id: String) {
        viewModelScope.launch {
            repository.deletePromocion(id)
        }
    }

    fun getAllPromocionesOnce(promocion: Promocion){
        viewModelScope.launch {
            repository.getAllPromocionesOnce(promocion)
        }
    }

    /* ===================== CLEANUP ===================== */
    override fun onCleared() {
        super.onCleared()
        listener?.remove()
    }
}
