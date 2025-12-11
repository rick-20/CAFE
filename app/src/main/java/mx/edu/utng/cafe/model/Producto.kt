package mx.edu.utng.cafe.model

import com.google.gson.annotations.SerializedName


data class Producto(
    @SerializedName("id")
    val id: String = "",

    @SerializedName("nombre")
    val nombre: String = "",

    @SerializedName("descripcion")
    val descripcion: String = "",

    @SerializedName("precio")
    val precio: Double = 0.0,

    @SerializedName("disponible")
    val disponible: Boolean = false,
)
data class ProductoReqst(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val disponible: Boolean
)
