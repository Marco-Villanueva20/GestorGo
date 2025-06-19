package pe.cibertec.gestorgo.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import pe.cibertec.gestorgo.R
import pe.cibertec.gestorgo.features.inventario.ui.historial.HistorialScreen
import pe.cibertec.gestorgo.features.inventario.ui.historial.crear.CrearDetalleScreen
import pe.cibertec.gestorgo.features.inventario.ui.item.crearitem.CreateScreen
import pe.cibertec.gestorgo.features.inventario.ui.item.editaritem.EditarScreen
import pe.cibertec.gestorgo.features.inventario.ui.lista.ListScreen
import pe.cibertec.gestorgo.features.inventario.ui.reporte.ReporteExportScreen
import pe.cibertec.gestorgo.features.usuario.ui.iniciosesion.InicioSesionScreen
import pe.cibertec.gestorgo.features.usuario.ui.list.UsuarioScreen
import pe.cibertec.gestorgo.features.usuario.ui.registro.RegistroScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationScreen(
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Mostrar u ocultar barras
    val showBottomBar = bottomLevelRoutes.any { route ->
        currentDestination?.hierarchy?.any { it.hasRoute(route.route::class) } == true
    }
    println(currentDestination)


    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            if (showBottomBar) {
                TopAppBar(
                    title = {
                        Row(
                            Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            IconButton(onClick = { /* menú lateral */ }) {
                                Icon(Icons.Filled.Menu, contentDescription = "Menú")
                            }
                            Surface(
                                Modifier
                                    .weight(1f)
                                    .height(40.dp),
                                shape = MaterialTheme.shapes.extraLarge,
                                color = Color.White
                            ) {
                                Row(
                                    Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Filled.Search,
                                        contentDescription = "Buscar",
                                        tint = Color.Gray
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text("Buscar", color = Color.Gray)
                                }
                            }
                            Spacer(Modifier.width(8.dp))
                            Image(
                                painter = painterResource(R.drawable.login),
                                contentDescription = "Perfil",
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .clickable { /* perfil */ }
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            }
        },
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomLevelRoutes.forEach { item ->
                        val isSelected =
                            currentDestination?.hierarchy?.any { it.hasRoute(item.route::class) } == true
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.name) },
                            label = { Text(item.name) },
                            selected = isSelected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                unselectedIconColor = Color.Gray,
                                unselectedTextColor = Color.Gray
                            )
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            if (currentDestination?.hierarchy?.any{it.hasRoute( Home::class)} == true) {
                FloatingActionButton (onClick = {
                    navController.navigate(CreateItem)
                }) {
                    Icon(Icons.Filled.Add, contentDescription = "Agregar")
                }
            }
            if (currentDestination?.hierarchy?.any{it.hasRoute( Historial::class)} == true) {
                FloatingActionButton (onClick = {
                    navController.navigate(CrearDetalle)
                }) {
                    Icon(Icons.Filled.Add, contentDescription = "Agregar")
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController,
            startDestination = Login,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable<Login> {
                InicioSesionScreen(
                    onButtonRegister = { navController.navigate(Register) },
                    onButtonAccess = {
                        navController.navigate(Home) {
                            popUpTo(Login) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable<Register> {
                RegistroScreen(
                    paddingValues = paddingValues,
                    onRegisterSuccess = { navController.navigate(Login) }
                )
            }
            composable<Home> {
                ListScreen(
                    onEditClick = { item ->
                        navController.navigate(
                            EditItem(
                                id = item.id,
                                nombre = item.nombre,
                                descripcion = item.descripcion,
                                imagenUrl = item.imagenUrl,
                                cantidad = item.cantidad,
                                usuarioId = item.usuarioId
                            )
                        )
                    }
                )
            }
            composable<EditItem> { backStackEntry ->
                val route = backStackEntry.toRoute<EditItem>()
                EditarScreen(
                    itemId = route.id,
                    nombre = route.nombre,
                    descripcion = route.descripcion,
                    imagenUrl = route.imagenUrl,
                    cantidad = route.cantidad,
                    usuarioId = route.usuarioId,
                    onBackPressed = {
                        navController.popBackStack()
                    },
                    onUpdate = {
                        navController.popBackStack()
                    },
                    onCancel = { navController.popBackStack()}
                )
            }
            composable<CreateItem> {
                CreateScreen(
                    onBackPressed = {
                        navController.popBackStack()
                    },
                    onSaved = {
                        navController.popBackStack()
                    },
                    onCancelClick = { navController.popBackStack()}
                )
            }
            composable<Historial> { HistorialScreen()}
            composable<CrearDetalle> {
                CrearDetalleScreen(
                    onBackPressed = {
                        navController.popBackStack()
                    },
                    onSuccess = {
                        navController.popBackStack()
                    }
                )
            }
            composable<Users>{
                UsuarioScreen()
            }
            composable<Report> {
                ReporteExportScreen()
            }
        }
    }
}

    @Preview(showBackground = true)
    @Composable
    fun PreviewNavigationScreen() {
        MaterialTheme {
            NavigationScreen()
        }
    }