package pe.cibertec.gestorgo.features.inventario.ui.lista

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import pe.cibertec.gestorgo.features.inventario.data.model.ItemApiModel
import pe.cibertec.gestorgo.features.inventario.data.repository.ItemsRepositoryImpl // Asumo que este es tu repositorio
import pe.cibertec.gestorgo.features.inventario.domain.model.Item // Tu modelo de dominio Item
// Importar estas si son necesarias para la lógica de historial
// import pe.cibertec.gestorgo.features.inventario.domain.model.HistorialItem
// import pe.cibertec.gestorgo.features.inventario.data.model.DetalleItemApiModel

import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds
import android.util.Log // Importa Log para el Log.d

@OptIn(FlowPreview::class)
@HiltViewModel
class ListItemViewModel @Inject constructor(private val itemsRepositoryImpl: ItemsRepositoryImpl) :
    ViewModel() {
    private var isLoading by mutableStateOf(false) // Puedes exponer esto en el UIState si quieres mostrar un ProgressBar

    private val _uiState = MutableStateFlow(ItemUIState()) // Asegúrate de que ItemUIState contenga List<Item>
    val uiState = _uiState.asStateFlow()

    // --- VARIABLES DE ESTADO PARA EL DIÁLOGO ---
    private val _showDeleteConfirmationDialog = MutableStateFlow(false)
    val showDeleteConfirmationDialog: StateFlow<Boolean> = _showDeleteConfirmationDialog.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _itemToDelete = MutableStateFlow<Item?>(null)
    val itemToDelete: StateFlow<Item?> = _itemToDelete.asStateFlow()

    init {
        // Inicializa la carga de items y el debounce de búsqueda
        setupSearchDebounce() // Un funcion para encapsular la lógica del debounce
        cargarItems() // Carga inicial
    }

    private fun setupSearchDebounce() {
        viewModelScope.launch {
            _searchQuery
                .debounce(300.milliseconds)
                .collectLatest { query ->
                    performSearch(query)
                }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    private fun performSearch(query: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                // Obtener todos los ítems de la base de datos (o usar un funcion de búsqueda si el repositorio lo tiene)
                itemsRepositoryImpl.obtenerItems().collect { itemApiList ->
                    val items = itemApiList.map { it.toItem() } // Mapear a tu modelo de dominio Item

                    val filteredItems = if (query.isBlank()) {
                        items // Si la query está vacía, mostrar todos los items
                    } else {
                        items.filter { item -> // Filtrar sobre tu lista de objetos Item
                            item.nombre.contains(query, ignoreCase = true) ||
                                    item.descripcion.contains(query, ignoreCase = true)
                        }
                    }
                    _uiState.value = _uiState.value.copy(listaItems = filteredItems)
                    Log.d("ListItemViewModel", "Performing search for: '$query'. Found ${filteredItems.size} items.")
                }
            } catch (e: Exception) {
                Log.e("ListItemViewModel", "Error during search: ${e.message}", e)
                // Considera actualizar el UIState con un mensaje de error
            } finally {
                isLoading = false
            }
        }
    }

    private fun cargarItems() {
        viewModelScope.launch {
            isLoading = true
            try {
                itemsRepositoryImpl.obtenerItems().collect { itemApiList ->
                    val items = itemApiList.map { it.toItem() }
                    _uiState.value = _uiState.value.copy(listaItems = items)
                    println("Lista actualizada con ${items.size} items")
                }
            } catch (e: Exception) {
                Log.e("ListItemViewModel", "Error loading items: ${e.message}", e)
                // Considera actualizar el UIState con un mensaje de error
            } finally {
                isLoading = false
            }
        }
    }

    // --- FUNCIONES PARA EL DIÁLOGO ---
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
                    cargarItems() // Recargar la lista después de eliminar
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

    // Mapeo de ItemApiModel a Item de dominio
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