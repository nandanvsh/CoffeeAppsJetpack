package com.example.coffeeapps.screen.components.navigation

sealed class Screen(val route: String){
    object Home : Screen("home")
    object Keranjang : Screen("keranjang")
    object Profile : Screen("profile")
    object DetailMenu : Screen("home/{idMenu}") {
        fun createRoute(idMenu: Long) = "home/$idMenu"
    }
}
