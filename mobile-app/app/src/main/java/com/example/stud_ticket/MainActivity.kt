package com.example.stud_ticket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.platform.LocalContext
import com.example.stud_ticket.network.RetrofitClient
import com.example.stud_ticket.ui.theme.StudTicketTheme
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            val dataStoreManager = remember { DataStoreManager(context) }
            val savedIp by dataStoreManager.serverIp.collectAsState(initial = "")
            val savedPort by dataStoreManager.serverPort.collectAsState(initial = "")

            LaunchedEffect(savedIp, savedPort) {
                if (!savedIp.isNullOrEmpty()) RetrofitClient.currentIp = savedIp!!
                if (!savedPort.isNullOrEmpty()) RetrofitClient.currentPort = savedPort!!
            }

            var isDarkMode by remember { mutableStateOf(false) }
            var currentUser by remember { mutableStateOf<UserData?>(null) }
            val scope = rememberCoroutineScope()

            StudTicketTheme(darkTheme = isDarkMode) {
                AppNavigation(
                    isDarkMode = isDarkMode,
                    onThemeToggle = { isDarkMode = !isDarkMode },
                    currentUser = currentUser,
                    onLoginSuccess = { user -> 
                        scope.launch {
                            dataStoreManager.getUserPhoto(user.id.toString()).collect { localPhoto ->
                                currentUser = if (localPhoto != null) user.copy(photoUrl = localPhoto) else user
                            }
                        }
                    },
                    onUserUpdate = { user -> currentUser = user },
                    dataStoreManager = dataStoreManager,
                    scope = scope
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
    onLoginSuccess: (UserData) -> Unit,
    onUserUpdate: (UserData) -> Unit,
    dataStoreManager: DataStoreManager,
    scope: kotlinx.coroutines.CoroutineScope
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
                    userData = currentUser ?: SampleUser,
                    onUserUpdate = onUserUpdate,
                    onSaveLocalPhoto = { id, path ->
                        scope.launch { dataStoreManager.saveUserPhoto(id, path) }
                    }
                )
        }
    }
}