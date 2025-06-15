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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
    alEditar: (HistorialItem) -> Unit,
    alIrADetalle: (HistorialItem) -> Unit
) {
    val uiState = viewModel.uiState.collectAsState()

    LazyColumn(modifier = Modifier.padding(vertical = 8.dp)) {
        items(uiState.value.listaItems) { item ->
            TarjetaHistorial(
                historialItem = item,
                alEditar = { alEditar(item) },
                alIrADetalle = { alIrADetalle(item) }
            )
        }
    }
}

@Composable
fun TarjetaHistorial(
    historialItem: HistorialItem,
    alEditar: () -> Unit,
    alIrADetalle: () -> Unit
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

            // Botones de editar y flecha
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = alEditar) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar",
                        modifier = Modifier.size(20.dp)
                    )
                }
                IconButton(onClick = alIrADetalle) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Ver Detalle",
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
        alEditar = {},
        alIrADetalle = {}
    )
}
