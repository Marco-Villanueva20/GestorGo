package pe.cibertec.gestorgo.features.inventario.ui.item.crearitem

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import pe.cibertec.gestorgo.features.inventario.domain.model.Item
import pe.cibertec.gestorgo.features.inventario.domain.repository.ItemsRepository
import javax.inject.Inject

@HiltViewModel
class CreateItemViewModel
@Inject constructor(private val itemsRepositoryImpl: ItemsRepository)
    :ViewModel() {
    private var isLoading by mutableStateOf(false)

    private val _uiState = MutableStateFlow(CreateItemUIState())
    val uiState: StateFlow<CreateItemUIState> = _uiState.asStateFlow()


    fun onNombreChange(nuevoNombre: String) {
        println(nuevoNombre)
        _uiState.value = _uiState.value.copy(nombre = nuevoNombre)
    }

    fun onDescripcionChange(nuevaDescripcion: String) {
        println(nuevaDescripcion)
        _uiState.value = _uiState.value.copy(descripcion = nuevaDescripcion)
    }

    fun onCantidadChange(nuevaCantidad: String) {
        val cantidadInt = nuevaCantidad.toIntOrNull() ?: 0
        _uiState.value = _uiState.value.copy(cantidad = cantidadInt)

    }



    fun guardarItem(context: Context, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true

            if (!validarCampos()) {
                isLoading = false
                return@launch
            }

            val byteArray = withContext(Dispatchers.IO) {
                _uiState.value.uri?.toByteArray(context)
            }

            if (byteArray != null) {
                _uiState.value = _uiState.value.copy(
                    imagenUrl = itemsRepositoryImpl.createInBucketItem(
                        _uiState.value.uri.toString(),
                        byteArray
                    )
                )
            }

            itemsRepositoryImpl.crearItem(_uiState.value.toItem().toItemApiModel())
            isLoading = false
            onSuccess() //  Llamar solo al final
        }
    }






    private fun validarCampos(): Boolean {
        val item = _uiState.value
        return item.nombre.isNotBlank() &&
                item.descripcion.isNotBlank() &&
                item.cantidad > 0
    }

    fun updateImage(uri: Uri?) {
        _uiState.value = _uiState.value.copy(uri = uri)
    }

    private fun CreateItemUIState.toItem(): Item {
        return Item(
            nombre = nombre,
            descripcion = descripcion,
            cantidad = cantidad,
            imagenUrl = imagenUrl
        )
    }
    private fun Item.toItemApiModel(): ItemApiModel {
        return ItemApiModel(
            nombre = nombre,
            descripcion = descripcion,
            cantidad = cantidad,
            imagenUrl = imagenUrl)
    }


    private fun Uri.toByteArray(context: Context) =
        context.contentResolver.openInputStream(this)?.use { it.buffered().readBytes() }
}