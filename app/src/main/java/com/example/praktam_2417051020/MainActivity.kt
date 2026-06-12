package com.example.praktam_2417051020

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.praktam_2417051020.data.SessionManager
import com.example.praktam_2417051020.data.repository.KamusRepository
import com.example.praktam_2417051020.design.*
import com.example.praktam_2417051020.ui.theme.PrakTAM_2417051020Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val sessionManager = SessionManager(this)
        val repository = KamusRepository()
        
        enableEdgeToEdge()
        setContent {
            PrakTAM_2417051020Theme {
                val navController = rememberNavController()
                
                val startDestination = if (sessionManager.isLoggedIn()) "main" else "login"

                NavHost(navController = navController, startDestination = startDestination) {
                    composable("login") {
                        LoginScreen(navController, repository, sessionManager)
                    }
                    composable("register") {
                        RegisterScreen(navController, repository)
                    }
                    composable("main") {
                        MainScreen(navController, repository, sessionManager)
                    }
                    composable(
                        route = "detail/{istilahId}",
                        arguments = listOf(navArgument("istilahId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val istilahId = backStackEntry.arguments?.getInt("istilahId")
                        DetailIstilahScreen(istilahId, navController, repository, sessionManager)
                    }
                }
            }
        }
    }
}
