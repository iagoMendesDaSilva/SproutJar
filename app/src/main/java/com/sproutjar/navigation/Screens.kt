package com.sproutjar.navigation


enum class Screens {
    SplashScreen,
    PotsScreen,
    PotScreen,
    SimulationsScreen,
    ProjectionsScreen,
    SettingsScreen;

    companion object {
        fun fromRoute(route: String?): Screens = when (route?.substringBefore("/")) {
            SplashScreen.name -> SplashScreen
            PotsScreen.name -> PotsScreen
            PotScreen.name -> PotScreen
            SimulationsScreen.name -> SimulationsScreen
            ProjectionsScreen.name -> ProjectionsScreen
            SettingsScreen.name -> SettingsScreen
            null -> SplashScreen
            else -> throw  IllegalArgumentException("Route $route is not recognized")
        }
    }

}