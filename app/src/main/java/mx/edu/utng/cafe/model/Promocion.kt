package mx.edu.utng.cafe.model

import com.google.gson.annotations.SerializedName


data class Promocion(
    @SerializedName("id")
    val id: String = "",

    @SerializedName("nombre")
    val nombre: String = "",

    @SerializedName("descripcion")
    val descripcion: String = "",

    @SerializedName("puntosRequeridos")
    val puntosRequeridos: String = "",

    @SerializedName("fechaInicio")
    val fechaInicio: String = "",

    @SerializedName("fechaFin")
    val fechaFin: String = ""
)

data class PromocionRqst(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val puntosRequeridos: Int,
    val fechaInicio: String,
    val fechaFin: String
)
