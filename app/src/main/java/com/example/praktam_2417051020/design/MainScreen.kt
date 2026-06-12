package com.example.praktam_2417051020.design

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.praktam_2417051020.data.SessionManager
import com.example.praktam_2417051020.data.repository.KamusRepository

@Composable
fun MainScreen(rootNavController: NavController, repository: KamusRepository, sessionManager: SessionManager) {
    val navController = rememberNavController()
    val items = listOf(
        Screen.Home,
        Screen.Favorite,
        Screen.Profile
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController, startDestination = Screen.Home.route, modifier = Modifier.padding(innerPadding)) {
            composable(Screen.Home.route) {
                DashboardScreen(navController = rootNavController, repository = repository, sessionManager = sessionManager)
            }
            composable(Screen.Favorite.route) {
                FavoriteScreen(navController = rootNavController, repository = repository, sessionManager = sessionManager)
            }
            composable(Screen.Profile.route) {
                ProfileScreen(rootNavController = rootNavController, repository = repository, sessionManager = sessionManager)
            }
        }
    }
}

sealed class Screen(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Favorite : Screen("favorite", "Favorite", Icons.Default.Favorite)
    object Profile : Screen("profile", "Profile", Icons.Default.Person)
}
