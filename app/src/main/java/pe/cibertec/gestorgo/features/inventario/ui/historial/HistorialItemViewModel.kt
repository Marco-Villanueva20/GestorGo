package pe.cibertec.gestorgo.features.inventario.ui.historial

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.cibertec.gestorgo.features.inventario.domain.model.HistorialItem
import pe.cibertec.gestorgo.features.inventario.domain.repository.DetalleItemsRepository
import pe.cibertec.gestorgo.features.inventario.domain.repository.ItemsRepository
import javax.inject.Inject

@HiltViewModel
class HistorialItemViewModel @Inject constructor(private val detalleItemsRepository: DetalleItemsRepository,
                                                 private val itemsRepository: ItemsRepository): ViewModel() {

    private val _uiState = MutableStateFlow(HistorialItemUIState())
    val uiState = _uiState.asStateFlow()

    private fun listarDetalleItems(){
        viewModelScope.launch {
            val items = itemsRepository.obtenerItemsConDetalles()
            Log.d("HistorialItemViewModel", "Items: $items")

            // Aquí cambiamos la lógica de mapeo
            val historialItems = mutableListOf<HistorialItem>()
            items.forEach { itemApiModel ->
                itemApiModel.detalleItemApiModels?.forEach { detalle ->
                    println( "Detalle ID: ${detalle.id}, Item ID (parent): ${detalle.itemId}") // Para depuración

                    historialItems.add(
                        HistorialItem(
                            id = detalle.id, // <-- ¡CORREGIDO! Asigna el ID ÚNICO del detalle_item
                            imagenUrl = itemApiModel.imagenUrl ?: "",
                            nombre = itemApiModel.nombre,
                            fecha = detalle.fecha ?: "",
                            cantidad = detalle.cantidad,
                            parentItemId = detalle.itemId // <-- Este es el ID del Item principal asociado al detalle
                        )
                    )
                }
            }

            // Si necesitas ordenar por fecha, puedes hacerlo aquí
            // historialItems.sortByDescending { it.fecha } // Esto requerirá que 'fecha' sea un tipo comparable (ej. LocalDateTime)

            _uiState.value = _uiState.value.copy(listaItems = historialItems)
        }
    }
    init {
        listarDetalleItems()
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
                // 1. Obtener el ítem principal actual para calcular la nueva cantidad
                val currentItem =
                    itemsRepository.obtenerItem(parentItemId)
                println( "Item principal actual: $currentItem")

                // 2. Calcular la nueva cantidad del ítem principal
                // Si el detalle fue un ingreso (+cantidad), al eliminarlo restamos.
                // Si el detalle fue una salida (-cantidad), al eliminarlo sumamos (restar un negativo es sumar).
                val nuevaCantidadItem = (currentItem.cantidad ?: 0) - detalleQuantity
                Log.d(
                    "HistorialItemViewModel",
                    "Cantidad actual del item ${currentItem.nombre}: ${currentItem.cantidad}"
                )
                Log.d("HistorialItemViewModel", "Cantidad del detalle a eliminar: $detalleQuantity")
                Log.d("HistorialItemViewModel", "Nueva cantidad calculada: $nuevaCantidadItem")


                // 3. Actualizar la cantidad del ítem principal
                val itemToUpdate = currentItem.copy(cantidad = nuevaCantidadItem)

                itemsRepository.actualizarItem(itemToUpdate)
                Log.d("HistorialItemViewModel", "Item principal actualizado: $itemToUpdate")

                // 4. Eliminar el registro de detalle
                detalleItemsRepository.eliminarDetalleItem(historialItemId)
                Log.d(
                    "HistorialItemViewModel",
                    "Detalle de historial eliminado: ID $historialItemId"
                )

                // 5. Refrescar la lista de historial
                listarDetalleItems()
                dismissDeleteConfirmationDialog() // Cerrar el diálogo al finalizar

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