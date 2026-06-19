package com.example.multiscreenapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            AppNavHost(navController)
        }
    }
}

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("settings") { SettingsScreen(navController) }
        composable(
            "profile/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType }),
            deepLinks = listOf(navDeepLink { uriPattern = "myapp://profile/{userId}" })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            ProfileScreen(userId)
        }
    }
}

@Composable
fun HomeScreen(navController: NavController) {
    Column {
        Text("🏠 Home Screen")
        Button(onClick = { navController.navigate("profile/42") }) { Text("Go to Profile (userId=42)") }
        Button(onClick = { navController.navigate("settings") }) { Text("Go to Settings") }
    }
}

@Composable
fun ProfileScreen(userId: String?) {
    Column {
        Text("👤 Profile Screen")
        Text("User ID: $userId")
    }
}

class SettingsViewModel : ViewModel() {
    private val _username = MutableStateFlow("Guest")
    val username: StateFlow<String> = _username
    fun updateUsername(newName: String) { _username.value = newName }
}

@Composable
fun SettingsScreen(navController: NavController, viewModel: SettingsViewModel = viewModel()) {
    val username by viewModel.username.collectAsState()
    Column {
        Text("⚙️ Settings Screen")
        TextField(value = username, onValueChange = { viewModel.updateUsername(it) }, label = { Text("Enter your name") })
        Text("Current username: $username")
    }
}
