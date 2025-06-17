package pe.cibertec.gestorgo.features.inventario.ui.historial

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import pe.cibertec.gestorgo.features.inventario.domain.model.HistorialItem

@Composable
fun HistorialScreen(
    viewModel: HistorialItemViewModel = hiltViewModel(),
    alEliminar: (Int) -> Unit,
    onCrearNuevo: () -> Unit // Nuevo callback para el botón flotante
) {
    val uiState = viewModel.uiState.collectAsState()

    // Envuelve el contenido en un Scaffold
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onCrearNuevo) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Añadir nuevo registro"
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End // Posiciona el FAB en la esquina inferior derecha
    ) { paddingValues -> // paddingValues es crucial para que el contenido no quede debajo del TopBar/FAB
        LazyColumn(modifier = Modifier.padding(paddingValues)) { // Aplica el padding aquí
            items(uiState.value.listaItems) { item ->
                TarjetaHistorial(
                    historialItem = item,
                    alEliminar = { item.id?.let { alEliminar(it) } } // Si alEliminar también es para eliminar en tu caso. Parece que usas el icono de editar, quizás sea `alEditar`? Ajusta según tu lógica.
                )
            }
        }
    }
}

@Composable
fun TarjetaHistorial(
    historialItem: HistorialItem,
    alEliminar: () -> Unit // Considera cambiar el nombre a 'alEditar' si el icono es de edición.
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {

            // Fila principal con icono + título y puntaje a la derecha
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Visibility, // Puedes cambiar este ícono
                        contentDescription = "Icono",
                        tint = Color.Blue,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = historialItem.nombre,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                Text(
                    text = historialItem.cantidad.toString(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Fecha y hora
            Text(
                text = historialItem.fecha,
                fontSize = 13.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Botones de editar (o eliminar, según la lógica que le hayas dado)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = alEliminar) { // Si realmente es eliminar, el icono Icons.Default.Delete sería más apropiado.
                    Icon(
                        imageVector = Icons.Default.Delete, // Este icono es de edición. Si es para eliminar, usa Icons.Default.Delete
                        contentDescription = "Editar/Eliminar", // Cambia esto según el icono
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TarjetaHistorialPreview() {
    TarjetaHistorial(
        historialItem = HistorialItem(
            imagenUrl = "",
            nombre = "Nombre del Item",
            fecha = "Fecha",
            cantidad = 10
        ),
        alEliminar = {}
    )
}