package mx.edu.utng.cafe.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.tasks.await
import mx.edu.utng.cafe.model.Producto

class ProductoRepository {

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("productos")

    suspend fun addProducto(productos: Producto): Result<String> {
        return try {
            val docRef = collection.add(
                mapOf(
                    "nombre" to productos.nombre,
                    "descripcion" to productos.descripcion,
                    "precio" to productos.precio,
                    "disponible" to productos.disponible
                )
            ).await()

            Result.success(docRef.id)  // Retornamos el ID generado
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateProducto(productos: Producto): Result<Unit> {
        return try {
            if (productos.id.isBlank())
                return Result.failure(IllegalArgumentException("ID vacío"))

            collection.document(productos.id.toString())
                .set(mapOf(
                    "nombre" to productos.nombre,
                    "descripcion" to productos.descripcion,
                    "precio" to productos.precio,
                    "disponible" to productos.disponible
                ))
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteProducto(productosId: String): Result<Unit> {
        return try {
            collection.document(productosId)
                .delete()
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllProductosOnce(): Result<List<Producto>> {
        return try {
            val snapshot = collection.get().await()

            val productos = snapshot.documents.map { doc ->
                Producto(
                    id = doc.id,
                    nombre = doc.getString("nombre") ?: "",
                    descripcion = doc.getString("descripcion") ?: "",
                    precio = doc.getDouble("precio") ?: 0.0,
                    disponible = doc.getBoolean("disponible") ?: false
                )
            }

            Result.success(productos)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun addProductoListener(onChange: (List<Producto>) -> Unit): ListenerRegistration {
        return collection.addSnapshotListener { snap, err ->
            if (err != null) return@addSnapshotListener

            val productos = snap?.documents?.map { doc ->
                Producto(
                    id = doc.id,
                    nombre = doc.getString("nombre") ?: "",
                    descripcion = doc.getString("descripcion") ?: "",
                    precio = doc.getDouble("precio") ?: 0.0,
                    disponible = doc.getBoolean("disponible") ?: false
                )
            } ?: emptyList()

            onChange(productos)  // Notificamos los cambios
        }
    }

    /*fun obtenerProductos(
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
    }*/
}

