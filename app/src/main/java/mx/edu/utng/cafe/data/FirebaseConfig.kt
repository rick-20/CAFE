package mx.edu.utng.cafe.data

import com.google.firebase.firestore.FirebaseFirestore

object FirebaseConfig {
    val firestore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }
}
