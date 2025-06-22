package pe.cibertec.gestorgo.features.inventario.ui.reporte

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.tehras.charts.bar.BarChart
import com.github.tehras.charts.bar.BarChartData
import com.github.tehras.charts.bar.BarChartData.Bar
import com.github.tehras.charts.bar.renderer.bar.SimpleBarDrawer
import com.github.tehras.charts.bar.renderer.label.SimpleValueDrawer
import com.github.tehras.charts.bar.renderer.xaxis.SimpleXAxisDrawer
import com.github.tehras.charts.bar.renderer.yaxis.SimpleYAxisDrawer
import com.github.tehras.charts.piechart.animation.simpleChartAnimation
import kotlinx.coroutines.launch
import pe.cibertec.gestorgo.features.inventario.domain.model.Reporte

@Composable
fun ReporteExportScreen(
    viewModel: ReporteViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.listarItems()
    }

    ReporteScreen(
        listaItems = uiState.listaItems,
        context = context,
        viewModel = viewModel
    )
}

@Composable
fun ReporteScreen(
    listaItems: List<Reporte>,
    context: Context,
    viewModel: ReporteViewModel
) {
    val scope = rememberCoroutineScope()

    val bars = listaItems.map {
        Bar(
            label = it.nombre,
            value = (it.cantidad ?: 0).toFloat().toInt().toFloat(),
            color = MaterialTheme.colorScheme.primaryContainer
        )
    }

    val barChartData = BarChartData(bars = bars)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
            Text(
                text = "📊 Total Stock Disponible",
                style = MaterialTheme.typography.headlineSmall
            )
        Spacer(modifier = Modifier.height(16.dp))
        // Gráfico
        InventoryBarChart(barChartData = barChartData)
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                scope.launch {
                    val file = viewModel.generateExcelFile(context)

                    val uri = FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.fileprovider",
                        file
                    )

                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        setDataAndType(uri, "text/csv")
                        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK
                    }

                    try {
                        context.startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        Toast.makeText(
                            context,
                            "No se encontró una app para abrir CSV",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        ) {
            Text("Exportar en Excel 📥", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun InventoryBarChart(barChartData: BarChartData) {
    val itemCount = barChartData.bars.size
    val totalWidth = (itemCount * 80).dp  // ancho proporcional

    Box(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .fillMaxWidth()
    ) {
        BarChart(
            barChartData = barChartData,
            modifier = Modifier
                .width(totalWidth + 40.dp) // añade espacio a izquierda/derecha
                .padding(start = 20.dp, end = 20.dp)
                .height(400.dp),
            animation = simpleChartAnimation(),
            barDrawer = SimpleBarDrawer(),
            xAxisDrawer = SimpleXAxisDrawer(),
            yAxisDrawer = SimpleYAxisDrawer(),
            labelDrawer = SimpleValueDrawer()
        )
    }
}
