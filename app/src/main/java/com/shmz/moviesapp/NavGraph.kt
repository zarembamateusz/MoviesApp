package com.shmz.moviesapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.shmz.feature_now_playing_movies_list.NowPlayingListScreen

@Composable
fun NavGraph(modifier: Modifier = Modifier, navController: NavHostController) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Destination.NOW_PLAYING_LIST.route
    ) {
        composable(Destination.NOW_PLAYING_LIST.route) {
            NowPlayingListScreen()
        }
//        composable(Destination.MOVIE_DETAILS.route) {

//        }
    }
}

enum class Destination(val route: String) {
    NOW_PLAYING_LIST("now_playing_list"),
    MOVIE_DETAILS("movie_details/{${Argument.MOVIE_ID}}");

    override fun toString(): String = route

    fun routeWithArguments(argumentName: String, value: String): String =
        this.route.replace(argumentName, value)

    enum class Argument(internal val value: String) {
        MOVIE_ID("movie_id");

        val routeValue: String = value.asRouteValue()

        override fun toString(): String = value

        @Suppress("MemberVisibilityCanBePrivate")
        fun String.asRouteValue(): String = "{$this}"
    }
}