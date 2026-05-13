package com.dam.planetstarwars.ui.home

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dam.planetstarwars.ui.components.topAppBar.BaseTopAppBarState
import com.dam.planetstarwars.ui.planetScreens.AboutUsScreen
import com.dam.planetstarwars.ui.planetScreens.add.PlanetModificationViewModel
import com.dam.planetstarwars.ui.planetScreens.list.PlanetListScreen
import com.dam.planetstarwars.data.model.Planet
import com.dam.planetstarwars.ui.planetScreens.add.PlanetDetailScreen


import kotlinx.coroutines.launch

object Routes {
    const val DASHBOARD = "DASHBOARD"
    const val LIST = "LIST"
    const val ADD = "ADD"
    const val EDIT = "EDIT"
    const val ABOUT = "ABOUTUS"
    const val SETTINGS = "SETTINGS"
}

@Composable
fun NavHostScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    onConfigureTopBar: (BaseTopAppBarState) -> Unit,
    onOpenDrawer: () -> Unit
) {
    val scope = rememberCoroutineScope()

    NavHost(
        navController = navController,
        startDestination = Routes.DASHBOARD,
        modifier = modifier,

        enterTransition = {
            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(500))
        },

        exitTransition = {
            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(500))
        },

        popEnterTransition = {
            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(500))
        },

        popExitTransition = {
            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(500))
        }
    ) {
        composable(route = Routes.DASHBOARD) {
            DashboardScreen(
                onConfigureTopBar = onConfigureTopBar,
                onOpenDrawer = onOpenDrawer
            )
        }

        composable(route = Routes.LIST) {
            PlanetListScreen(
                viewModel = hiltViewModel(),
                onAddPlanet = { navController.navigate(Routes.ADD) },
                onEditPlanet = { planet: Planet ->
                    navController.currentBackStackEntry?.savedStateHandle?.set("planet", planet)
                    navController.navigate(Routes.EDIT)
                },
                onShowSnackbar = { msg -> scope.launch { snackbarHostState.showSnackbar(msg) } },
                onAboutUs = { navController.navigate(Routes.ABOUT) },
                onConfigureTopBar = onConfigureTopBar,
                onOpenDrawer = onOpenDrawer
            )
        }


        composable(route = Routes.ADD) {
            val viewModel = hiltViewModel<PlanetModificationViewModel>()

            LaunchedEffect(Unit) { viewModel.setPlanet(null) }

            PlanetDetailScreen(
                viewmodel = viewModel,
                onShowMessage = { message ->
                    scope.launch { snackbarHostState.showSnackbar(message) }
                },
                onBack = { navController.popBackStack() },
                onConfigureTopBar = onConfigureTopBar
            )
        }


        composable(route = Routes.EDIT) {
            val planet = navController.previousBackStackEntry?.savedStateHandle?.get<Planet>("planet")
            val viewModel = hiltViewModel<PlanetModificationViewModel>()

            LaunchedEffect(planet) {
                if (planet != null) viewModel.setPlanet(planet)
            }

            PlanetDetailScreen(
                viewmodel = viewModel,
                onShowMessage = { message ->
                    scope.launch { snackbarHostState.showSnackbar(message) }
                },
                onBack = { navController.popBackStack() },
                onConfigureTopBar = onConfigureTopBar
            )
        }

        // 4. Pantalla Sobre Nosotros
        composable(route = Routes.ABOUT) {
            AboutUsScreen(
                onBack = { navController.popBackStack() },
                onConfigureTopBar = onConfigureTopBar
            )
        }

        composable(route = Routes.SETTINGS) {
            SettingsScreen(
                onConfigureTopBar = onConfigureTopBar,
                onOpenDrawer = onOpenDrawer
            )
        }
    }
}