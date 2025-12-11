package mx.edu.utng.cafe.viewModell


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import mx.edu.utng.cafe.data.PedidoRepository
import mx.edu.utng.cafe.model.Pedido

class PedidoViewModel(
    private val userId: String
) : ViewModel() {

    private val repo = PedidoRepository()

    private val _pedidos = MutableStateFlow<List<Pedido>>(emptyList())
    val pedidos: StateFlow<List<Pedido>> = _pedidos

    private var listener: ListenerRegistration? = null

    init {
        listener = repo.addPedidosListener { list ->
            // Filtramos SOLO los pedidos del usuario logueado
            _pedidos.value = list.filter { it.idU == userId }
        }
    }

    override fun onCleared() {
        listener?.remove()
        super.onCleared()
    }

    class PedidoViewModelFactory(
        private val userId: String
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PedidoViewModel(userId) as T
        }
    }
}
