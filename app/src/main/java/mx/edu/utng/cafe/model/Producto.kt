package mx.edu.utng.cafe.model

data class Producto(
    var id: Int = 0,
    var nombre: String = "",
    var descripcion: String = "",
    var precio: Double = 0.0,
    var disponible: Boolean = false
)
