package mx.edu.utng.cafe.model

data class Promocion(
    var id: Int = 0,
    var nombre: String = "",
    var descripcion: String = "",
    var puntosRequeridos: Int = 0,
    var fechaInicio: String = "",
    var fechaFin: String = ""
)
