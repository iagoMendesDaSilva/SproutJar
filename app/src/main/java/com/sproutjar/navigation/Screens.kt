package com.sproutjar.navigation


enum class Screens {
    SplashScreen,
    BoxesScreen,
    BoxScreen,
    SimulationsScreen,
    ProjectionsScreen,
    SettingsScreen;

    companion object {
        fun fromRoute(route: String?): Screens = when (route?.substringBefore("/")) {
            SplashScreen.name -> SplashScreen
            BoxesScreen.name -> BoxesScreen
            BoxScreen.name -> BoxScreen
            SimulationsScreen.name -> SimulationsScreen
            ProjectionsScreen.name -> ProjectionsScreen
            SettingsScreen.name -> SettingsScreen
            null -> SplashScreen
            else -> throw  IllegalArgumentException("Route $route is not recognized")
        }
    }

}