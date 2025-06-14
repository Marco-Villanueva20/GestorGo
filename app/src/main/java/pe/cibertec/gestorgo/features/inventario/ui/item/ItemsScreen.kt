package pe.cibertec.gestorgo.features.inventario.ui.item

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.rememberAsyncImagePainter
import pe.cibertec.gestorgo.features.inventario.data.model.ItemApiModel
import pe.cibertec.gestorgo.ui.theme.GestorGoTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemsScreen(viewModel: ItemsViewModel = hiltViewModel()) {
    val items = viewModel.uiState.value.listItem

    LaunchedEffect(Unit) {
        viewModel.cargarItems()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Elementos") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* agregar itemApiModel */ }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
            }
        }
    ) {
        LazyColumn(contentPadding = PaddingValues(16.dp)) {
            items(items!!.size) { index ->
                ItemCard(items[index])
            }
        }
    }
}

@Composable
fun ItemCard(itemApiModel: ItemApiModel) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            Image(
                painter = rememberAsyncImagePainter(itemApiModel.imagenUrl),
                contentDescription = itemApiModel.nombre,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = itemApiModel.nombre, style = MaterialTheme.typography.titleMedium)
                Text(text = itemApiModel.descripcion, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Preview
@Composable
fun ElementosScreenPreview() {
    GestorGoTheme {
        ItemsScreen()
    }
}