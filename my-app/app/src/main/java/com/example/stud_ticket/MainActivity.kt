package com.example.stud_ticket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.stud_ticket.ui.theme.StudTicketTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isDarkMode by remember { mutableStateOf(false) }
            var currentUser by remember { mutableStateOf<UserData?>(null) }

            StudTicketTheme(darkTheme = isDarkMode) {
                AppNavigation(
                    isDarkMode = isDarkMode,
                    onThemeToggle = { isDarkMode = !isDarkMode },
                    currentUser = currentUser,
                    onLoginSuccess = { user -> currentUser = user }
                )
            }
        }
    }
}

@Composable
fun AppNavigation(
    isDarkMode: Boolean,
    onThemeToggle: () -> Unit,
    currentUser: UserData?,
    onLoginSuccess: (UserData) -> Unit
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(
                navController = navController,
                isDarkMode = isDarkMode,
                onThemeToggle = onThemeToggle
            )
        }
        composable(Screen.Login.route) {
            LoginScreen(
                navController = navController,
                isDarkMode = isDarkMode,
                onThemeToggle = onThemeToggle,
                onLoginSuccess = onLoginSuccess
            )
        }
        composable(Screen.Profile.route) {
            ProfileScreen(
                navController = navController,
                isDarkMode = isDarkMode,
                onThemeToggle = onThemeToggle,
                userData = currentUser ?: SampleUser // Fallback to sample if null
            )
        }
    }
}