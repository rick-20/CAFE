package mx.edu.utng.cafe.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.tasks.await
import mx.edu.utng.cafe.model.Pedido
import mx.edu.utng.cafe.model.Usuario

class PedidoRepository {

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("pedidos")

    suspend fun addPedido(pedidos: Pedido): Result<String> {
        return try {
            val docRef = collection.add(
                mapOf(
                    "fecha" to pedidos.fecha,
                    "total" to pedidos.total,
                    "estado" to pedidos.estado
                )
            ).await()

            Result.success(docRef.id)  // Retornamos el ID generado
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updatePedido(pedidos: Pedido): Result<Unit> {
        return try {
            if (pedidos.id.isBlank())
                return Result.failure(IllegalArgumentException("ID vacío"))

            collection.document(pedidos.id.toString())
                .set(mapOf(
                    "estado" to pedidos.estado
                ))
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deletePedido(pedidosId: String): Result<Unit> {
        return try {
            collection.document(pedidosId)
                .delete()
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllPedidosOnce(): Result<List<Pedido>> {
        return try {
            val snapshot = collection.get().await()

            val notes = snapshot.documents.map { doc ->
                Pedido(
                    id = doc.id,
                    fecha = doc.getString("fecha") ?: "",
                    total = doc.getDouble("total") ?: 0.0,
                    estado = doc.getString("estado") ?: ""
                )
            }

            Result.success(notes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getPedidosByUserId(usuariosId: String): Result<List<Pedido>> {
        return try {
            val snapshot = collection
                .whereEqualTo("usuariosId", usuariosId)
                .get()
                .await()

            val pedidos = snapshot.documents.map { doc ->
                Pedido(
                    id = doc.id,
                    fecha = doc.getString("fecha") ?: "",
                    total = doc.getDouble("total") ?: 0.0,
                    estado = doc.getString("estado") ?: "",
                    idU = doc.getString("idU") ?: ""
                )
            }

            Result.success(pedidos)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    fun addPedidosListener(onChange: (List<Pedido>) -> Unit): ListenerRegistration {
        return collection.addSnapshotListener { snap, err ->
            if (err != null) return@addSnapshotListener

            val pedidos = snap?.documents?.map { doc ->
                Pedido(
                    id = doc.id,
                    fecha = doc.getString("fecha") ?: "",
                    total = doc.getDouble("total") ?: 0.0,
                    estado = doc.getString("estado") ?:"",
                )
            } ?: emptyList()

            onChange(pedidos)  // Notificamos los cambios
        }
    }

    /*fun obtenerPedidosUsuario(
        userId: String,
        onSuccess: (List<Pedido>) -> Unit
    ) {
        db.collection("pedidos")
            .whereEqualTo("usuarioId", userId)
            .get()
            .addOnSuccessListener {
                onSuccess(it.toObjects(Pedido::class.java))
            }
    }*/
}
