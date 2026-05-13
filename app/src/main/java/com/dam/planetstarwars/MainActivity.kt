package com.dam.planetstarwars

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dam.planetstarwars.ui.components.topAppBar.BaseTopAppBarState
import com.dam.planetstarwars.ui.home.NavHostScreen
import com.dam.planetstarwars.ui.home.Routes
import com.dam.planetstarwars.ui.theme.PlanetStarWarsTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            PlanetStarWarsTheme {

                val navController = rememberNavController()
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                val snackbarHostState = remember { SnackbarHostState() }

                val menuIconPainter = rememberVectorPainter(image = Icons.Default.Menu)
                var topBarState by remember {
                    mutableStateOf(
                        BaseTopAppBarState(
                            title = "Star Wars Wiki",

                            iconUpAction = menuIconPainter,
                            upAction = {

                                scope.launch {
                                    if (drawerState.isClosed) drawerState.open() else drawerState.close()
                                }
                            },
                            actions = emptyList()
                        )
                    )
                }


                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val titulo = when (currentRoute) {
                    Routes.DASHBOARD -> "Inicio"
                    Routes.LIST -> "Listado de Planetas"
                    Routes.ADD -> "Nuevo Planeta"
                    Routes.EDIT -> "Editar Planeta"
                    Routes.SETTINGS -> "Ajustes"
                    Routes.ABOUT -> "Sobre Nosotros"
                    else -> "Star Wars Wiki"
                }


                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        ModalDrawerSheet{
                            Text("Menú Principal", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge)
                            HorizontalDivider()

                            NavigationDrawerItem(
                                label = { Text("Inicio") },
                                icon = { Icon(Icons.Default.Menu, "Ir a inicio") },
                                selected = currentRoute == Routes.DASHBOARD,
                                onClick = {
                                    scope.launch {
                                        drawerState.close()
                                        navController.navigate(Routes.DASHBOARD) {
                                            popUpTo(Routes.DASHBOARD) { inclusive = true }
                                        }
                                    }
                                }
                            )

                            NavigationDrawerItem(
                                label = { Text("Planetas") },
                                icon = { Icon(Icons.Default.MoreVert, "Item para desplegar listado") },
                                selected = currentRoute == Routes.LIST,
                                onClick = {
                                    scope.launch {
                                        drawerState.close()
                                        navController.navigate(Routes.LIST)
                                    }
                                }
                            )

                            NavigationDrawerItem(
                                label = { Text("Ajustes") },
                                icon = { Icon(Icons.Default.Menu, "Item para ajustes") },
                                selected = currentRoute == Routes.SETTINGS,
                                onClick = {
                                    scope.launch {
                                        drawerState.close()
                                        navController.navigate(Routes.SETTINGS)
                                    }
                                }
                            )

                            NavigationDrawerItem(
                                label = { Text("Acerca de") },
                                icon = { Icon(Icons.Default.Info, "Item para desplegar el aboutUs") },
                                selected = currentRoute == Routes.ABOUT,
                                onClick = {
                                    scope.launch {
                                        drawerState.close()
                                        navController.navigate(Routes.ABOUT)
                                    }
                                }
                            )
                        }
                    }
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = { Text(titulo) },
                                navigationIcon = {
                                    IconButton(onClick = {
                                        scope.launch { if (drawerState.isClosed) drawerState.open() else drawerState.close() }
                                    }) {
                                        Icon(Icons.Default.Menu, contentDescription = "Menú")
                                    }
                                },

                            )
                        },
                        floatingActionButton = {
                            if (currentRoute == Routes.LIST) {
                                FloatingActionButton(
                                    onClick = { navController.navigate(Routes.ADD) },
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                ) {
                                    Icon(imageVector = Icons.Default.Add, contentDescription = "Añadir Planeta")
                                }
                            }
                        },
                        snackbarHost = { SnackbarHost(snackbarHostState) }
                    ) { innerPadding ->


                        NavHostScreen(
                            navController = navController,
                            modifier = Modifier.padding(innerPadding),
                            snackbarHostState = snackbarHostState,
                            onConfigureTopBar = { newState -> topBarState = newState },
                            onOpenDrawer = { scope.launch { drawerState.open() } }
                        )
                    }
                }
            }
        }
    }
}