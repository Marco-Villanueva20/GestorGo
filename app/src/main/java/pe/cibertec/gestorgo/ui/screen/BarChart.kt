package pe.cibertec.gestorgo.ui.screen


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun NivelesScreen(viewModel: NivelesViewModel) {
    val niveles by viewModel.niveles.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.cargarStock()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Niveles") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* Mostrar detalles o exportar */ }) {
                Icon(Icons.Default.Add, contentDescription = "Datos")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Text(
                text = "Total Stock Disponible",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp)
            )

            BarChart(data = niveles)
        }
    }
}


@Composable
fun BarChart(data: List<StockLevel>) {
    val max = data.maxOfOrNull { it.total } ?: 0

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        data.forEach { item ->
            Column (horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .height((item.total * 2).dp.coerceAtMost(200.dp))
                        .width(24.dp)
                        .background(Color.Blue)
                )
                Text(text = item.nombre.take(6), style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}
