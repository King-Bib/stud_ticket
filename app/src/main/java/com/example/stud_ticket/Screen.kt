package com.example.stud_ticket

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Login : Screen("login")
    object Profile : Screen("profile")
}
