package pe.cibertec.gestorgo.features.inventario.ui.listaelementos

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import pe.cibertec.gestorgo.features.inventario.data.model.ItemApiModel

// Supongamos que tienes las imágenes en tus drawables
// Por ejemplo, R.drawable.saw_icon, R.drawable.hammer_icon, R.drawable.tsquare_icon

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ListScreen(viewModel: ListItemViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val items = uiState.listaItems


    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { /* Acción para añadir nuevo elemento */ }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Añadir nuevo"
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            items(items) { item ->
                ItemCard(tool = item)
                // Puedes añadir un divisor si quieres una línea entre los elementos
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            }
        }
    }
}
@Composable
fun ItemCard(tool: ItemApiModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = tool.nombre,
                style = MaterialTheme.typography.titleMedium,
                fontSize = 18.sp
            )
            Text(
                text = tool.descripcion,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
        IconButton(onClick = { /* Acción para el menú de opciones */ }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Más opciones"
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
fun PreviewToolListScreen() {
    MaterialTheme { // Envuelve tu preview en un tema
        ListScreen()
    }
}