package mx.edu.utng.cafe.viewModell


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import mx.edu.utng.cafe.data.ProductoRepository
import mx.edu.utng.cafe.model.Producto

class ProductoViewModel : ViewModel() {

    private val repo = ProductoRepository()

    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos = _productos.asStateFlow()

    private var listener: ListenerRegistration? = null

    init {
        listener = repo.addProductoListener {
            _productos.value = it
        }
    }

    fun addProducto(producto: Producto) {
        viewModelScope.launch {
            repo.addProducto(producto)
        }
    }

    fun updateProducto(producto: Producto) {
        viewModelScope.launch {
            repo.updateProducto(producto)
        }
    }

    fun deleteProducto(id: String) {
        viewModelScope.launch {
            repo.deleteProducto(id)
        }
    }

    override fun onCleared() {
        listener?.remove()
        super.onCleared()
    }
}

