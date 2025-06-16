package pe.cibertec.gestorgo.features.inventario.ui.reporte

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pe.cibertec.gestorgo.features.inventario.data.model.ItemApiModel
import pe.cibertec.gestorgo.features.inventario.domain.model.Reporte
import pe.cibertec.gestorgo.features.inventario.domain.repository.ItemsRepository
import java.io.File
import org.apache.poi.ss.usermodel.*
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import javax.inject.Inject

@HiltViewModel
class ReporteViewModel @Inject constructor(private val reporteRepository: ItemsRepository) :
    ViewModel() {

    private val _uiState = MutableStateFlow(ReporteUIState())
    val uiState: StateFlow<ReporteUIState> = _uiState.asStateFlow()


    fun listarItems() {
        viewModelScope.launch {
            reporteRepository.getItems().collect { lista ->
                val itemsReporte: List<Reporte> = lista.map { it.toReporte() }
                _uiState.value = _uiState.value.copy(listaItems = itemsReporte)
            }
        }
    }
    suspend fun generateExcelFile(context: Context): File {
        val items = _uiState.value.listaItems
        val file = File(context.cacheDir, "reporte_stock.xlsx")
        exportReportToExcel(items, file)
        return file
    }

    private suspend fun exportReportToExcel(items: List<Reporte>, file: File) {
        withContext(Dispatchers.IO) {
            val workbook: Workbook = XSSFWorkbook()
            val sheet = workbook.createSheet("Stock")

            // === Estilos ===
            val borderStyle = BorderStyle.THIN
            val blackHeaderStyle = workbook.createCellStyle().apply {
                fillForegroundColor = IndexedColors.BLACK.index
                fillPattern = FillPatternType.SOLID_FOREGROUND
                borderTop = borderStyle
                borderBottom = borderStyle
                borderLeft = borderStyle
                borderRight = borderStyle
            }
            val whiteBoldFont = workbook.createFont().apply {
                color = IndexedColors.WHITE.index
                bold = true
            }
            blackHeaderStyle.setFont(whiteBoldFont)

            val yellowBodyStyle = workbook.createCellStyle().apply {
                fillForegroundColor = IndexedColors.LIGHT_YELLOW.index
                fillPattern = FillPatternType.SOLID_FOREGROUND
                borderTop = borderStyle
                borderBottom = borderStyle
                borderLeft = borderStyle
                borderRight = borderStyle
            }
            val blackFont = workbook.createFont().apply {
                color = IndexedColors.BLACK.index
            }
            yellowBodyStyle.setFont(blackFont)

            // === Cabecera ===
            val headerRow = sheet.createRow(0)
            listOf("Producto", "Cantidad").forEachIndexed { i, title ->
                val cell = headerRow.createCell(i)
                cell.setCellValue(title)
                cell.cellStyle = blackHeaderStyle
            }

            // === Filas ===
            items.forEachIndexed { index, item ->
                val row = sheet.createRow(index + 1)
                val cellProducto = row.createCell(0).apply {
                    setCellValue(item.nombre)
                    cellStyle = yellowBodyStyle
                }
                val cellCantidad = row.createCell(1).apply {
                    setCellValue(item.cantidad?.toDouble() ?: 0.0)
                    cellStyle = yellowBodyStyle
                }
            }


            // Escribir archivo
            file.outputStream().use { workbook.write(it) }
            workbook.close()
        }
    }
    private fun ItemApiModel.toReporte(): Reporte {
        return Reporte(
            nombre = this.nombre,
            cantidad = this.cantidad
        )
    }
}





