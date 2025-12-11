package mx.edu.utng.cafe.model
import com.google.gson.annotations.SerializedName


data class Pedido(
    @SerializedName("id")
    val id: String = "",

    @SerializedName("fecha")
    val fecha: String = "",

    @SerializedName("total")
    val total: Double = 0.0,

    @SerializedName("estado")
    val estado: String = "",

    @SerializedName("idU")
    val idU: String = "",
)
data class PedidoRequest(
    val id: Int,
    val fecha: String,
    val total: Double,
    val estado: String
)
