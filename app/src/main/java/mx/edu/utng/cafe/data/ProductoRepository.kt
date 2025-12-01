package mx.edu.utng.cafe.data

import mx.edu.utng.cafe.model.Producto

class ProductoRepository {

    private val db = FirebaseConfig.firestore

    fun obtenerProductos(
        onSuccess: (List<Producto>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        db.collection("productos")
            .get()
            .addOnSuccessListener { result ->
                val productos = result.documents.mapNotNull {
                    it.toObject(Producto::class.java)
                }
                onSuccess(productos)
            }
            .addOnFailureListener {
                onError(it)
            }
    }
}

