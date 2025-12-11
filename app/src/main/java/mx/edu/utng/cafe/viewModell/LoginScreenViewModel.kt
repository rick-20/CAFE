package mx.edu.utng.cafe.viewModell

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import kotlinx.coroutines.launch
import mx.edu.utng.cafe.model.Usuario
import mx.edu.utng.cafe.ui.screen.MainScreen

class LoginScreenViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth
    private val _loading = MutableLiveData(false)

    fun signInWithEmailAndPassword(
        email: String,
        password: String,
        mainScreen: () -> Unit
    )
    = viewModelScope.launch {
        try{
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {task->
                if (task.isSuccessful){
                    Log.d("CafeUni", "signInWithEmailAndPassword logueado!!")
                    mainScreen()
                }
                else{
                    Log.d("CafeUni", "signInWithEmailAndPassword: ${task.result.toString()}")

                }
            }
    }
    catch (ex:Exception){
        Log.d("CafeUni", "signInWithEmailAndPassword ${ex.message}")

    }
    }

    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        mainscreen: () -> Unit
    ){
        if (_loading.value == false){
            _loading.value = true
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener  { task->
                    if(task.isSuccessful){
                        val displayName =
                            task.result.user?.email?.split("@")?.get(0)
                        createUser(displayName)
                        mainscreen()
                    }
                    else{
                        Log.d("CafeUni", "createUserWithEmailAndPassword: ${task.result.toString()}")
                        _loading.value = false
                    }
                }
        }
    }
    private fun createUser(displayName: String?) {
        val userId = auth.currentUser?.uid
        //val correo = auth.currentUser?.uid

        val user = Usuario(
            idU = userId.toString(),
            nombre = displayName.toString(),
            correo = "",//correo.toString(),
            puntosAcumulados = "0",
            universidad = "UTNG",
            id = null
        ).toMap()
        FirebaseFirestore.getInstance().collection("users")
            .add(user)
            .addOnSuccessListener {
                Log.d("CafeUni", "Creado ${it.id}")
            }.addOnFailureListener {
                Log.d("CafeUni", "Ocurrio un error ${it}")
            }
    }
}

