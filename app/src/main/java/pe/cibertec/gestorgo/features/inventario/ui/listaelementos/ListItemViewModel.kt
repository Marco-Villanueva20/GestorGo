package pe.cibertec.gestorgo.features.inventario.ui.listaelementos

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.cibertec.gestorgo.features.inventario.data.repository.ItemsRepositoryImpl
import javax.inject.Inject

@HiltViewModel
class ListItemViewModel @Inject constructor(private val itemsRepositoryImpl: ItemsRepositoryImpl ): ViewModel() {
    private var isLoading by mutableStateOf(false)

    private val _uiState = MutableStateFlow(ItemUIState())
    val uiState = _uiState.asStateFlow()
    init {
        cargarItems()
    }

    private fun cargarItems() {
        viewModelScope.launch {
            isLoading = true
            itemsRepositoryImpl.getItems().collect { itemList ->
                _uiState.value = _uiState.value.copy(listaItems = itemList)
                println("Lista actualizada con ${itemList.size} items")
            }
            isLoading = false // aunque este no se alcanza si el flow nunca termina
        }
    }
}