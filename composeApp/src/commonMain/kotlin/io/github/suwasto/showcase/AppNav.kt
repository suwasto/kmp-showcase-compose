package io.github.suwasto.showcase

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

@Serializable
data object Landing

@Serializable
data class Home(val showcaseStyle: Showcase)

@Serializable
enum class Showcase {
    STANDAR,
    ANIMATED,
    ANIMATED_HIGHLIGHT_WATERDROP,
    ANIMATED_HIGHLIGHT_RIPPLE
}

@Composable
fun AppNav() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Landing) {
        composable<Landing> {
            LandingScreen(
                onGetStartedClick = { navController.navigate(Home(it)) }
            )
        }
        composable<Home> { backStackEntry ->
            val homeArgs = backStackEntry.toRoute<Home>()
            val showcaseStyle = homeArgs.showcaseStyle
            HomeScreen(
                showcaseStyle = showcaseStyle
            )
        }
    }
}