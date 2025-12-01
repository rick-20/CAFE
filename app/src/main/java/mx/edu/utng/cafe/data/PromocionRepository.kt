package mx.edu.utng.cafe.data

import mx.edu.utng.cafe.model.Promocion

class PromocionRepository {

    private val db = FirebaseConfig.firestore

    fun obtenerPromociones(
        onSuccess: (List<Promocion>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        db.collection("promociones")
            .get()
            .addOnSuccessListener {
                onSuccess(it.toObjects(Promocion::class.java))
            }
            .addOnFailureListener(onError)
    }
}
