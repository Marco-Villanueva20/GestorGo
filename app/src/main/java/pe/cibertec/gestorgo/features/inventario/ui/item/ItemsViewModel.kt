package pe.cibertec.gestorgo.features.inventario.ui.item

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.cibertec.gestorgo.features.inventario.domain.repository.ItemsRepository
import javax.inject.Inject

@HiltViewModel
class ItemsViewModel @Inject constructor(
    private val itemsRepositoryImpl: ItemsRepository
) : ViewModel() {

    private var isLoading by mutableStateOf(false)
    private val _uiState = MutableStateFlow(ItemUIState())
    val uiState = _uiState.asStateFlow()


    fun cargarItems() {
        viewModelScope.launch {
            isLoading = true
            itemsRepositoryImpl.getItems().collect{
                _uiState.value.listItem = it
            } // Suponiendo que esto devuelve List<Item>

            isLoading = false
        }
    }


}