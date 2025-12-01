package mx.edu.utng.cafe.model

data class Pedido(
    var id: Int = 0,
    var fecha: String = "",
    var total: Double = 0.0,
    var estado: String = ""
)
