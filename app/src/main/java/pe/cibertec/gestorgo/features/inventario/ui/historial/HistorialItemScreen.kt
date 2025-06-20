package pe.cibertec.gestorgo.features.inventario.ui.historial


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import pe.cibertec.gestorgo.features.inventario.domain.model.HistorialItem

// Anotación para Scaffold si estás usando Material3
@Composable
fun HistorialScreen(
    viewModel: HistorialItemViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsState().value

    Column {
        BarraBusquedaHistorial(
            texto = uiState.textoBusqueda, // <-- Conecta con el texto de búsqueda del UIState
            onTextoCambio = { viewModel.actualizarTextoBusqueda(it) }, // <-- Llama a la función del ViewModel
            modifier = Modifier.fillMaxWidth()
        )
        LazyColumn {
            items(uiState.listaItemsFiltrada) { item -> // <-- Usa la lista filtrada
                TarjetaHistorial(
                    historialItem = item,
                    alEliminarConfirmacion = {
                        item.id?.let { detalleId ->
                            item.parentItemId?.let { parentItemId ->
                                viewModel.showDeleteConfirmationDialog(
                                    detalleId,
                                    parentItemId,
                                    item.cantidad
                                )
                            } ?: Log.e(
                                "HistorialScreen",
                                "Error: parentItemId del historial es nulo para eliminar."
                            )
                        } ?: Log.e("HistorialScreen", "Error: HistorialId es nulo para eliminar.")
                    }
                )
            }
        }
    }

    // --- Diálogo de Confirmación ---
    if (uiState.showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissDeleteConfirmationDialog() },
            title = { Text(text = "Confirmar Eliminación") },
            text = { Text(text = "¿Estás seguro de que quieres eliminar este registro de historial? Se ajustará la cantidad del ítem principal.") },
            confirmButton = {
                Button(onClick = { viewModel.confirmDeleteItem() }) {
                    Text("Sí, Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.dismissDeleteConfirmationDialog() }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Mostrar un mensaje de error si existe
    if (uiState.errorMessage != null) {
        // Puedes mostrar un SnackBar o un Toast aquí
        Text(
            text = "Error: ${uiState.errorMessage}",
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(16.dp)
        )
    }
}


@Composable
fun TarjetaHistorial(
    historialItem: HistorialItem,
    alEliminarConfirmacion: () -> Unit // Cambiado para indicar que muestra la confirmación
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {

                    AsyncImage(
                        model = historialItem.imagenUrl,
                        contentDescription = "Imagen de ${historialItem.nombre}",
                        modifier = Modifier
                            .size(44.dp) // Tamaño ligeramente más grande para visibilidad
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.LightGray)
                    )

                    Spacer(modifier = Modifier.width(8.dp)) // Espacio ajustado
                    Text(
                        text = historialItem.nombre,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
                // Mostrar la cantidad con un color para diferenciar ingreso/salida
                val cantidadColor =
                    if (historialItem.cantidad >= 0) Color(0xFF4CAF50) else Color(0xFFF44336) // Verde para ingreso, Rojo para salida
                Text(
                    text = "${if (historialItem.cantidad > 0) "+" else ""}${historialItem.cantidad}", // Añade '+' si es positivo
                    fontSize = 16.sp, // Tamaño de fuente ajustado
                    fontWeight = FontWeight.Bold,
                    color = cantidadColor
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Fecha y hora
            Text(
                text = "Fecha: ${Fecha.formatearFecha(historialItem.fecha)}", // Etiqueta "Fecha:"
                fontSize = 13.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = alEliminarConfirmacion) {
                    Icon(
                        imageVector = Icons.Default.Delete, // Ahora sí, el icono de borrar
                        contentDescription = "Eliminar registro",
                        tint = MaterialTheme.colorScheme.error // Color rojo para eliminar
                    )
                }
            }
        }
    }
}

@Composable
fun BarraBusquedaHistorial(
    texto: String,
    onTextoCambio: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = texto,
        onValueChange = onTextoCambio,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Buscar"
            )
        },
        placeholder = { Text("Buscar en el historial de movimientos") },
        singleLine = true,
        shape = MaterialTheme.shapes.extraLarge,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}





@Preview(showBackground = true)
@Composable
fun TarjetaHistorialPreview() {
    Column {
        TarjetaHistorial(
            historialItem = HistorialItem(
                id = 1,
                imagenUrl = "https://example.com/item1.png", // URL de ejemplo
                nombre = "Producto A",
                fecha = "2025-06-18 10:00",
                cantidad = 50, // Ejemplo de ingreso
                parentItemId = 101
            ),
            alEliminarConfirmacion = {}
        )
        TarjetaHistorial(
            historialItem = HistorialItem(
                id = 2,
                imagenUrl = "https://example.com/item2.png", // URL de ejemplo
                nombre = "Producto B",
                fecha = "2025-06-18 11:30",
                cantidad = -15, // Ejemplo de salida
                parentItemId = 102
            ),
            alEliminarConfirmacion = {}
        )
    }
}
