package pe.cibertec.gestorgo.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.InsertChart
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import pe.cibertec.gestorgo.features.inventario.domain.model.Item


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
@Serializable data object CrearDetalle

@Serializable data class EditItem(val id: Int? = null,
                                  val nombre: String,
                                  val descripcion: String,
                                  val imagenUrl: String? = null,
                                  val cantidad: Int? = null,
                                  val usuarioId: String? = null)
@Serializable data object CreateItem



// Listas de rutas
val bottomLevelRoutes = listOf(
    BottomLevelRoute("Lista", Home, Icons.Filled.Home),
    BottomLevelRoute("Historial", Historial, Icons.Filled.Inventory),
    BottomLevelRoute("Reporte", Report, Icons.Filled.InsertChart),
    BottomLevelRoute("Usuarios", Users, Icons.Filled.Person)
)
val floatingButtonRoutes = listOf(
    BottomLevelRoute("Historial", Historial, Icons.Filled.Inventory),
    BottomLevelRoute("Lista", Home, Icons.Filled.Home),
)
