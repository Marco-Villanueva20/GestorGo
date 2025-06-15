package pe.cibertec.gestorgo.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.InsertChart
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable
import pe.cibertec.gestorgo.features.inventario.data.model.ItemApiModel


// Data class para tus rutas de barra inferior
data class BottomLevelRoute<T : Any>(
    val name: String,
    val route: T,
    val icon: ImageVector
)

// Rutas serializables
@Serializable data object Home
@Serializable data object Historial
@Serializable data object Report
@Serializable data object Users
@Serializable data object Login
@Serializable data object Register

@Serializable data class EditItem(val itemApiModel: ItemApiModel)
@Serializable data object CreateItem

// Listas de rutas
val bottomLevelRoutes = listOf(
    BottomLevelRoute("Lista", Home, Icons.Filled.Home),
    BottomLevelRoute("Historial", Historial, Icons.Filled.Inventory),
    BottomLevelRoute("Reporte", Report, Icons.Filled.InsertChart),
    BottomLevelRoute("Usuarios", Users, Icons.Filled.Person)
)
val authRoutes = listOf(Login, Register)
