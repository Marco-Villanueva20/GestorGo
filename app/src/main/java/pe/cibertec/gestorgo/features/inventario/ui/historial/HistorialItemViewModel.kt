package pe.cibertec.gestorgo.features.inventario.ui.historial

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.cibertec.gestorgo.features.inventario.data.model.ItemApiModel
import pe.cibertec.gestorgo.features.inventario.domain.model.HistorialItem
import pe.cibertec.gestorgo.features.inventario.domain.repository.ItemsRepository
import javax.inject.Inject

@HiltViewModel
class HistorialItemViewModel @Inject constructor(private val repository: ItemsRepository): ViewModel() {

    private val _uiState = MutableStateFlow(HistorialItemUIState())
    val uiState = _uiState.asStateFlow()

    private fun listarDetalleItems(){
        viewModelScope.launch {
            val items = repository.obtenerItemsConDetalles()

            // Filtramos los que sí tienen detalles
            val historialItems = items
                .filter { !it.detalleItemApiModels.isNullOrEmpty() }
                .map { it.toHistorialItem() }

            // Actualizamos el estado con la lista convertida
            _uiState.value = _uiState.value.copy(listaItems = historialItems)
        }
    }
    init {
        listarDetalleItems()
    }

    private fun ItemApiModel.toHistorialItem(): HistorialItem {
        return HistorialItem(
            imagenUrl = this.imagenUrl ?: "",
            nombre = this.nombre,
            fecha = this.detalleItemApiModels?.firstOrNull()?.fecha ?: "",
            cantidad = this.detalleItemApiModels?.firstOrNull()?.cantidad ?: 0)
    }

}