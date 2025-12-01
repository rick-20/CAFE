package mx.edu.utng.cafe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import mx.edu.utng.cafe.ui.screen.MainScreen
import mx.edu.utng.cafe.ui.theme.UniversityPointsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UniversityPointsTheme {
                MainScreen()
            }
        }
    }
}