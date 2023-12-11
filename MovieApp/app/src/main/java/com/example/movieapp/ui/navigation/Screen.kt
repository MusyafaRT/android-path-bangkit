package com.example.movieapp.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Favorite : Screen("fav")
    object Profile : Screen("profile")
    object DetailMovie : Screen("home/{movieId}"){
        fun createRoute(movieId: Long) = "home/$movieId"
    }
}