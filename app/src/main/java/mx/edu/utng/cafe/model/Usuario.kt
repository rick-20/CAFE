package mx.edu.utng.cafe.model

import com.google.gson.annotations.SerializedName


data class Usuario(
    @SerializedName("id")
    val id: String? = "",

    @SerializedName("idU")
    val idU: String = "",

    @SerializedName("nombre")
    val nombre: String = "",

    @SerializedName("correo")
    val correo: String = "",

    @SerializedName("puntosAcumulados")
    val puntosAcumulados: String = "",

    @SerializedName("universidad")
    val universidad: String = "",
){
    fun toMap(): MutableMap<String, Any>{
        return mutableMapOf(
            "user_id" to this.idU,
            "display_name" to this.nombre,
            "correo" to this.correo,
            "puntos_acumulados" to this.puntosAcumulados,
            "universidad" to this.universidad
        )
    }
}
data class Usuarioe(
    val id: String?,
    val idU: String,
    val nombre: String,
    val correo: String,
    val puntosAcumulados: String,
    val universidad: String
) {
    fun toMap(): MutableMap<String, Any> {
        return mutableMapOf(
            "user_id" to this.idU,
            "display_name" to this.nombre,
            "correo" to this.correo,
            "puntos_acumulados" to this.puntosAcumulados,
            "universidad" to this.universidad
        )
    }
}

