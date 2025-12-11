package mx.edu.utng.cafe.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import mx.edu.utng.cafe.ui.screen.EditProfileScreen
import mx.edu.utng.cafe.ui.screen.HomeScreen
import mx.edu.utng.cafe.ui.screen.MainScreen
import mx.edu.utng.cafe.ui.screen.ProfileScreen
import mx.edu.utng.cafe.ui.screen.login.CafeUniSplashScreen
import mx.edu.utng.cafe.ui.screen.login.LoginScreen

@Composable
fun CafeNavigation(){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = CafeUniScreens.SplashScreen.name
    ){
        composable(CafeUniScreens.SplashScreen.name){
            CafeUniSplashScreen(navController = navController)
        }
        composable(CafeUniScreens.LoginScreen.name){
            LoginScreen(navController = navController)
        }
        composable(CafeUniScreens.MainScreen.name){
            MainScreen(navController = navController)
        }
        composable(CafeUniScreens.EditProfileScreen.name) {
            EditProfileScreen(navController = navController)
        }
        /*composable("edit_profile_route") {
            EditProfileScreen(navController = navController)
        }*/
    }
}