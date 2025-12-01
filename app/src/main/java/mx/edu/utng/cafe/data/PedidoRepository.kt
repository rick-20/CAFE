package mx.edu.utng.cafe.data

import mx.edu.utng.cafe.model.Pedido

class PedidoRepository {

    private val db = FirebaseConfig.firestore

    fun obtenerPedidosUsuario(
        userId: String,
        onSuccess: (List<Pedido>) -> Unit
    ) {
        db.collection("pedidos")
            .whereEqualTo("usuarioId", userId)
            .get()
            .addOnSuccessListener {
                onSuccess(it.toObjects(Pedido::class.java))
            }
    }
}
