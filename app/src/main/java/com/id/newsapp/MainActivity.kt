package com.id.newsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.id.domain.model.NewsCategory
import com.id.domain.repository.UserRepository
import com.id.newsapp.core.extensions.orZero
import com.id.newsapp.navigation.Route
import com.id.newsapp.screen.alldatabycategory.NewsGridRoute
import com.id.newsapp.screen.homescreen.HomeRoute
import com.id.newsapp.screen.homescreen.detailitem.NewsDetailRoute
import com.id.newsapp.screen.login.LoginRoute
import com.id.newsapp.screen.register.RegisterRoute
import org.koin.compose.getKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppEntryPoint()
                }
            }
        }
    }
}

@Composable
fun AppEntryPoint() {
    val navController = rememberNavController()
    val repository: UserRepository = getKoin().get()

    val isLoggedIn by repository.isLoggedIn().collectAsState(initial = false)

    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            navController.navigate(Route.HOME) {
                popUpTo(Route.LOGIN) { inclusive = true }
            }
        }
    }

    NavHost(navController = navController, startDestination = Route.LOGIN) {
        composable(Route.LOGIN) {
            LoginRoute(
                onLoginSuccess = {
                navController.navigate(Route.HOME) {
                    popUpTo(Route.LOGIN) { inclusive = true }
                }
            }, onRegisterClick = {
                    navController.navigate(Route.REGISTER)
                },
            )
        }

        composable(Route.HOME) {
            HomeRoute(navController = navController)
        }

        composable(Route.REGISTER) {
            RegisterRoute(navController = navController)
        }

        composable("${Route.NEWS_DETAIL}/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            NewsDetailRoute(navController, newsId = id.orZero())
        }

        composable("${Route.CATEGORY_GRID}/{category}") { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category")
                ?.let { runCatching { NewsCategory.valueOf(it) }.getOrNull() }
                ?: NewsCategory.ARTICLES

            NewsGridRoute(navController = navController, category = category)
        }
    }
}