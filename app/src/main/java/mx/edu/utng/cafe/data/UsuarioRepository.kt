package mx.edu.utng.cafe.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.tasks.await
import mx.edu.utng.cafe.model.Usuario

class UsuarioRepository {

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("users")


suspend fun addUsuario(usuarios: Usuario): Result<String> {
    return try {
        val docRef = collection.add(
            mapOf(
                "nombre" to usuarios.nombre,
                "correo" to usuarios.correo,
                "puntosAcumulados" to usuarios.puntosAcumulados,
                "universidad" to usuarios.universidad
            )
        ).await()

        Result.success(docRef.id)  // Retornamos el ID generado
    } catch (e: Exception) {
        Result.failure(e)
    }
}

    suspend fun updateUsuario(usuarios: Usuario): Result<Unit> {
        return try {
            if (usuarios.idU.isBlank())
                return Result.failure(IllegalArgumentException("ID vacío"))

            collection.document(usuarios.idU.toString())
                .set(mapOf(
                    "nombre" to usuarios.nombre,
                    "correo" to usuarios.correo,
                    "puntosAcumulados" to usuarios.puntosAcumulados,
                    "universidad" to usuarios.universidad
                ))
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteUsuario(usuariosId: String): Result<Unit> {
        return try {
            collection.document(usuariosId)
                .delete()
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllUsuariosOnce(): Result<List<Usuario>> {
        return try {
            val snapshot = collection.get().await()

            val usuarios = snapshot.documents.map { doc ->
                Usuario(
                    idU = doc.id,
                    nombre = doc.getString("nombre") ?: "",
                    correo = doc.getString("correo") ?: "",
                    puntosAcumulados = doc.getString("puntosAcumulados") ?: "",
                    universidad = doc.getString("universidad") ?: "",
                    id = null
                )
            }

            Result.success(usuarios)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun addUsuarioListener(onChange: (List<Usuario>) -> Unit): ListenerRegistration {
        return collection.addSnapshotListener { snap, err ->
            if (err != null) return@addSnapshotListener

            val usuarios = snap?.documents?.map { doc ->
                Usuario(
                    idU = doc.id,
                    nombre = doc.getString("nombre") ?: "",
                    correo = doc.getString("correo") ?: "",
                    puntosAcumulados = doc.getString("puntosAcumulados") ?:"",
                    universidad = doc.getString("universidad") ?:"",
                    id = null
                )
            } ?: emptyList()

            onChange(usuarios)  // Notificamos los cambios
        }
    }

suspend fun obtenerUsuario(idU: String): Usuario? {
        val snapshot = db.collection("users")
            .document(idU)
            .get()
            .await()

        return snapshot.toObject(Usuario::class.java)
    }

    suspend fun obtenerUsuarioLogueado(uid : String): Usuario? {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
           // ?: return null   // No hay usuario logueado

        val snapshot = db.collection("users")
            .document()
            .get()
            .await()

        return snapshot.toObject(Usuario::class.java)
    }
}