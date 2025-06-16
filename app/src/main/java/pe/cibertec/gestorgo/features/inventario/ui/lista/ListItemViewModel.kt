package pe.cibertec.gestorgo.features.inventario.ui.lista

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.cibertec.gestorgo.features.inventario.data.model.ItemApiModel
import pe.cibertec.gestorgo.features.inventario.data.repository.ItemsRepositoryImpl
import pe.cibertec.gestorgo.features.inventario.domain.model.Item
import javax.inject.Inject

@HiltViewModel
class ListItemViewModel @Inject constructor(private val itemsRepositoryImpl: ItemsRepositoryImpl) :
    ViewModel() {
    private var isLoading by mutableStateOf(false)

    private val _uiState = MutableStateFlow(ItemUIState())
    val uiState = _uiState.asStateFlow()

    // --- NUEVAS VARIABLES DE ESTADO PARA EL DIÁLOGO ---
    private val _showDeleteConfirmationDialog = MutableStateFlow(false)
    val showDeleteConfirmationDialog: StateFlow<Boolean> = _showDeleteConfirmationDialog.asStateFlow()

    private val _itemToDelete = MutableStateFlow<Item?>(null)
    val itemToDelete: StateFlow<Item?> = _itemToDelete.asStateFlow()
    // ----------------------------------------------------

    init {
        cargarItems()
    }

    private fun cargarItems() {
        viewModelScope.launch {
            isLoading = true
            itemsRepositoryImpl.getItems().collect { itemApiList ->
                val items = itemApiList.map { it.toItem() }
                _uiState.value = _uiState.value.copy(listaItems = items)
                println("Lista actualizada con ${items.size} items")
            }
            isLoading = false // aunque este no se alcanza si el flow nunca termina
        }
    }

    // --- NUEVAS FUNCIONES PARA EL DIÁLOGO ---
    fun confirmDeleteItem(item: Item) {
        _itemToDelete.value = item
        _showDeleteConfirmationDialog.value = true
    }

    fun dismissDeleteConfirmationDialog() {
        _showDeleteConfirmationDialog.value = false
        _itemToDelete.value = null // Limpiar el ítem seleccionado
    }

    fun deleteConfirmed(onSuccess: () -> Unit) {
        val item = _itemToDelete.value
        if (item?.id != null) {
            viewModelScope.launch {
                try {
                    itemsRepositoryImpl.eliminarItem(item.id)
                    // Después de eliminar, recargar la lista o eliminar el ítem del estado local
                    cargarItems() // Recargar para asegurar la consistencia
                    onSuccess()
                } catch (e: Exception) {
                    // Manejar error de eliminación (ej. mostrar un Toast)
                    println("Error al eliminar item: ${e.message}")
                } finally {
                    dismissDeleteConfirmationDialog() // Siempre cierra el diálogo
                }
            }
        }
    }
    // ------------------------------------------

    private fun ItemApiModel.toItem(): Item {
        return Item(
            id = this.id,
            nombre = this.nombre,
            descripcion = this.descripcion,
            imagenUrl = this.imagenUrl,
            cantidad = this.cantidad,
            usuarioId = this.usuarioId
        )
    }
}

// Asegúrate de que tu ItemUIState (si lo tienes) o similar esté definido así:
/*
data class ItemUIState(
    val listaItems: List<Item> = emptyList(),
    // ... otras propiedades
)
*/