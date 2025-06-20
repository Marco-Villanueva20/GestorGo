package pe.cibertec.gestorgo.features.inventario.ui.historial

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest // Importante: USAR collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.cibertec.gestorgo.features.inventario.domain.model.HistorialItem
import pe.cibertec.gestorgo.features.inventario.domain.repository.DetalleItemsRepository
import pe.cibertec.gestorgo.features.inventario.domain.repository.ItemsRepository
import javax.inject.Inject

@OptIn(FlowPreview::class) // Anotación para usar debounce
@HiltViewModel
class HistorialItemViewModel @Inject constructor(
    private val detalleItemsRepository: DetalleItemsRepository,
    private val itemsRepository: ItemsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistorialItemUIState())
    val uiState = _uiState.asStateFlow()

    private val _allHistorialItems = MutableStateFlow<List<HistorialItem>>(emptyList())
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    init {
        // Al iniciar el ViewModel, comenzamos a recolectar los detalles del repositorio
        // y también combinamos el texto de búsqueda con la lista completa.
        listarDetalleItems() // Este método ahora recolecta del Flow caliente compartido
        setupSearchFiltering()
    }

    private fun listarDetalleItems() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                // *** CAMBIO CLAVE AQUÍ: No pasas el scope, y el Flow ya está "caliente" ***
                itemsRepository.obtenerItemsConDetalles().collectLatest { itemsFromRepo ->
                    Log.d("HistorialItemViewModel", "Items recibidos del Flow: $itemsFromRepo")

                    val historialItems = mutableListOf<HistorialItem>()
                    itemsFromRepo.forEach { itemApiModel ->
                        itemApiModel.detalleItemApiModels?.forEach { detalle ->
                            historialItems.add(
                                HistorialItem(
                                    id = detalle.id,
                                    imagenUrl = itemApiModel.imagenUrl ?: "",
                                    nombre = itemApiModel.nombre,
                                    fecha = detalle.fecha ?: "",
                                    cantidad = detalle.cantidad,
                                    parentItemId = detalle.itemId
                                )
                            )
                        }
                    }
                    _allHistorialItems.value = historialItems
                    _uiState.update { it.copy(listaItems = historialItems, isLoading = false) }
                }
            } catch (e: Exception) {
                Log.e("HistorialItemViewModel", "Error al cargar ítems: ${e.message}", e)
                _uiState.update { it.copy(isLoading = false, errorMessage = "Error al cargar ítems: ${e.message}") }
            }
        }
    }

    private fun setupSearchFiltering() {
        viewModelScope.launch {
            combine(_allHistorialItems, _searchText.debounce(300L)) { allItems, query ->
                if (query.isBlank()) {
                    allItems
                } else {
                    allItems.filter { historialItem ->
                        historialItem.nombre.contains(query, ignoreCase = true) ||
                                historialItem.fecha.contains(query, ignoreCase = true) ||
                                historialItem.cantidad.toString().contains(query)
                    }
                }
            }.collectLatest { filteredList ->
                _uiState.update { it.copy(listaItemsFiltrada = filteredList) }
            }
        }
    }

    fun actualizarTextoBusqueda(newText: String) {
        _searchText.value = newText
        _uiState.update { it.copy(textoBusqueda = newText) }
    }

    fun showDeleteConfirmationDialog(historialItemId: Int, parentItemId: Int, quantity: Int) {
        _uiState.update {
            it.copy(
                showConfirmDialog = true,
                itemToDeleteId = historialItemId,
                itemToDeleteParentItemId = parentItemId,
                itemToDeleteQuantity = quantity
            )
        }
    }

    fun dismissDeleteConfirmationDialog() {
        _uiState.update {
            it.copy(
                showConfirmDialog = false,
                itemToDeleteId = null,
                itemToDeleteParentItemId = null,
                itemToDeleteQuantity = null
            )
        }
    }

    fun confirmDeleteItem() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val historialItemId = _uiState.value.itemToDeleteId
            val parentItemId = _uiState.value.itemToDeleteParentItemId
            val detalleQuantity = _uiState.value.itemToDeleteQuantity

            if (historialItemId == null || parentItemId == null || detalleQuantity == null) {
                _uiState.update {
                    it.copy(
                        errorMessage = "Datos incompletos para eliminar.",
                        isLoading = false
                    )
                }
                return@launch
            }

            try {
                val currentItem = itemsRepository.obtenerItemPorId(parentItemId)
                Log.d("HistorialItemViewModel", "Item principal actual: $currentItem")

                val nuevaCantidadItem = (currentItem.cantidad ?: 0) - detalleQuantity
                Log.d(
                    "HistorialItemViewModel",
                    "Cantidad actual del item ${currentItem.nombre}: ${currentItem.cantidad}"
                )
                Log.d("HistorialItemViewModel", "Cantidad del detalle a eliminar: $detalleQuantity")
                Log.d("HistorialItemViewModel", "Nueva cantidad calculada: $nuevaCantidadItem")

                val itemToUpdate = currentItem.copy(cantidad = nuevaCantidadItem)

                itemsRepository.actualizarItem(itemToUpdate)
                Log.d("HistorialItemViewModel", "Item principal actualizado: $itemToUpdate")

                detalleItemsRepository.eliminarDetalleItem(historialItemId)
                Log.d(
                    "HistorialItemViewModel",
                    "Detalle de historial eliminado: ID $historialItemId"
                )

                // *** CAMBIO CLAVE AQUÍ: ELIMINA esta llamada a listarDetalleItems() ***
                // La lista se actualizará AUTOMÁTICAMENTE a través del Flow que estás recolectando.
                // listarDetalleItems() // <--- ¡ELIMINAR ESTA LÍNEA!

                dismissDeleteConfirmationDialog()

            } catch (e: Exception) {
                Log.e("HistorialItemViewModel", "Error al eliminar historial: ${e.message}", e)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Error al eliminar historial: ${e.message}"
                    )
                }
            }
        }
    }
}