package com.example.mad.ui.screens

sealed class Screen(
    val route: String
) {
    object HomeScreen: Screen("home_screen")
    object QuizScreen: Screen("quiz_screen")
}
