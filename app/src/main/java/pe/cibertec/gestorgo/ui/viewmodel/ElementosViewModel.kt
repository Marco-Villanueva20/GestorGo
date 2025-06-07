package pe.cibertec.gestorgo.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.cibertec.gestorgo.data.model.Item
import pe.cibertec.gestorgo.data.repository.ItemsRepository
import javax.inject.Inject

@HiltViewModel
class ElementosViewModel @Inject constructor(
    private val itemsRepository: ItemsRepository
) : ViewModel() {

    var isLoading by mutableStateOf(false)

    private val _items = MutableStateFlow<List<Item>>(emptyList())
    val items = _items.asStateFlow()

    fun cargarItems() {
        viewModelScope.launch {
            _items.value = itemsRepository.getItems()
        }
    }
}