package mx.edu.utng.cafe.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.tasks.await
import mx.edu.utng.cafe.model.Promocion

class PromocionRepository {

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("promociones")

    suspend fun addPromocion(promociones: Promocion): Result<String> {
        return try {
            val docRef = collection.add(
                mapOf(
                    "nombre" to promociones.nombre,
                    "descripcion" to promociones.descripcion,
                    "puntosRequeridos" to promociones.puntosRequeridos,
                    "fechaInicio" to promociones.fechaInicio,
                    "fechaFin" to promociones.fechaFin
                )
            ).await()

            Result.success(docRef.id)  // Retornamos el ID generado
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updatePromocion(promociones: Promocion): Result<Unit> {
        return try {
            if (promociones.id.isBlank())
                return Result.failure(IllegalArgumentException("ID vacío"))

            collection.document(promociones.id.toString())
                .set(mapOf(
                    "nombre" to promociones.nombre,
                    "descripcion" to promociones.descripcion,
                    "puntosRequeridos" to promociones.puntosRequeridos,
                    "fechaInicio" to promociones.fechaInicio,
                    "fechaFin" to promociones.fechaFin
                ))
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deletePromocion(usuariosId: String): Result<Unit> {
        return try {
            collection.document(usuariosId)
                .delete()
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllPromocionesOnce(promocion: Promocion): Result<List<Promocion>> {
        return try {
            val snapshot = collection.get().await()

            val promociones = snapshot.documents.map { doc ->
                Promocion(
                    id = doc.id,
                    nombre = doc.getString("nombre") ?: "",
                    descripcion = doc.getString("descripcion") ?: "",
                    puntosRequeridos = doc.getString("puntosRequeridos") ?: "",
                    fechaInicio = doc.getString("fechaInicio") ?: "",
                    fechaFin = doc.getString("fechaFin") ?: ""
                )
            }

            Result.success(promociones)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun addPromocionesListener(onChange: (List<Promocion>) -> Unit): ListenerRegistration {
        return collection.addSnapshotListener { snap, err ->
            if (err != null) return@addSnapshotListener

            val promociones = snap?.documents?.map { doc ->
                Promocion(
                    id = doc.id,
                    nombre = doc.getString("nombre") ?: "",
                    descripcion = doc.getString("descripcion") ?: "",
                    puntosRequeridos = doc.getString("puntosRequeridos") ?:"",
                    fechaInicio = doc.getString("fechaInicio") ?:"",
                    fechaFin = doc.getString("fechaFin") ?: ""
                )
            } ?: emptyList()

            onChange(promociones)  // Notificamos los cambios
        }
    }

    /*fun obtenerPromociones(
        onSuccess: (List<Promocion>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        db.collection("promociones")
            .get()
            .addOnSuccessListener {
                onSuccess(it.toObjects(Promocion::class.java))
            }
            .addOnFailureListener(onError)
    }*/
}
