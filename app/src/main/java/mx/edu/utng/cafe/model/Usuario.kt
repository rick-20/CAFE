package mx.edu.utng.cafe.model

data class Usuario(
    var id: Int = 0,
    var nombre: String = "",
    var correo: String = "",
    var puntosAcumulados: Int = 0,
    var universidad: String = ""
)

