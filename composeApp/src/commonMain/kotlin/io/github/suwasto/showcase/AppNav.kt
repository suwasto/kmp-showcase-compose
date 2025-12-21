package io.github.suwasto.showcase

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.savedstate.SavedState
import androidx.savedstate.read
import androidx.savedstate.write
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.Json.Default.encodeToString
import kotlin.coroutines.EmptyCoroutineContext.get

@Serializable
data object Landing

@Serializable
data class Home(val showcaseStyle: String)

enum class Showcase {
    STANDAR,
    ANIMATED,
    ANIMATED_HIGHLIGHT_WATERDROP,
    ANIMATED_HIGHLIGHT_RIPPLE
}

@Composable
fun AppNav() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Landing,
    ) {
        composable<Landing> {
            LandingScreen(
                onGetStartedClick = { navController.navigate(Home(it)) }
            )
        }
        composable<Home> { backStackEntry ->
            val homeArgs = backStackEntry.toRoute<Home>()
            val showcaseStyle = Showcase.valueOf(homeArgs.showcaseStyle)
            HomeScreen(
                showcaseStyle = showcaseStyle
            )
        }
    }
}