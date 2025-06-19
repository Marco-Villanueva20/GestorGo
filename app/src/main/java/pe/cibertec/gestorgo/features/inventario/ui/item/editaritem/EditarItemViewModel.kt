package pe.cibertec.gestorgo.features.inventario.ui.item.editaritem

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
import pe.cibertec.gestorgo.features.inventario.ui.item.crearitem.CreateItemUIState // Asegúrate de que CreateItemUIState pueda manejar Uri
import javax.inject.Inject

@HiltViewModel
class EditarItemViewModel @Inject constructor(private val itemsRepositoryImpl: ItemsRepository) :
    ViewModel() {

    private var isLoading by mutableStateOf(false)

    private val _uiState = MutableStateFlow(CreateItemUIState())
    val uiState: StateFlow<CreateItemUIState> = _uiState.asStateFlow()

    // El ID del item que estamos editando
    private var currentItemId: Int? = null

    // Función para inicializar el ViewModel con los datos del Item a editar
    fun setItemToEdit(item: Item) {
        currentItemId = item.id // Guarda el ID del ítem
        _uiState.value = _uiState.value.copy(
            nombre = item.nombre ?: "", // Asegura que no sea null
            descripcion = item.descripcion ?: "", // Asegura que no sea null
            cantidad = item.cantidad ?: 0, // Asegura que no sea null
            // !!! Aquí está el cambio clave: Convertir la imagenUrl a Uri si existe
            uri = item.imagenUrl?.let { Uri.parse(it) } // Si es URL de internet, Coil la manejará
        )
    }

    fun onNombreChange(nuevoNombre: String) {
        _uiState.value = _uiState.value.copy(nombre = nuevoNombre)
    }

    fun onDescripcionChange(nuevaDescripcion: String) {
        _uiState.value = _uiState.value.copy(descripcion = nuevaDescripcion)
    }

    fun onCantidadChange(nuevaCantidad: String) {
        val cantidadInt = nuevaCantidad.toIntOrNull() ?: 0
        _uiState.value = _uiState.value.copy(cantidad = cantidadInt)
    }

    fun updateImage(uri: Uri?) {
        _uiState.value = _uiState.value.copy(uri = uri)
    }

    fun editarItem(context: Context, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true

            if (!validarCampos()) {
                isLoading = false
                // Aquí podrías mostrar un Toast o Snackbar al usuario
                return@launch
            }

            var finalImageUrl: String? = null

            // Si hay una URI seleccionada (ya sea local o una URL existente convertida a Uri)
            _uiState.value.uri?.let { uri ->
                // Verificar si la URI es una URI de contenido (local) o una URL (internet)
                if (uri.scheme == "content") { // Es una URI local, necesita ser subida
                    val byteArray = withContext(Dispatchers.IO) {
                        uri.toByteArray(context)
                    }
                    if (byteArray != null) {
                        // Aquí llamas a tu función para subir la imagen a Supabase Storage
                        // y obtienes la URL final de la imagen subida.
                        // Asumiendo que createInBucketItem devuelve la URL final
                        finalImageUrl = itemsRepositoryImpl.createInBucketItem(
                            uri.lastPathSegment ?: "item_image", // Nombre de archivo, puedes mejorarlo
                            byteArray
                        )
                    }
                } else if (uri.scheme == "http" || uri.scheme == "https") {
                    // Es una URL de internet, usarla directamente
                    finalImageUrl = uri.toString()
                }
            }

            // Crear el objeto Item con la imagen URL final y el ID del ítem
            val itemToUpdate = Item(
                id = currentItemId, // ¡Importante! Pasa el ID para actualizar el item correcto
                nombre = _uiState.value.nombre,
                descripcion = _uiState.value.descripcion,
                cantidad = _uiState.value.cantidad,
                imagenUrl = finalImageUrl
            )

            // Actualizar el ítem en la base de datos
            itemsRepositoryImpl.actualizarItem(itemToUpdate.toItemApiModel())
            isLoading = false
            onSuccess() // Llamar solo al final si todo fue exitoso
        }
    }

    private fun validarCampos(): Boolean {
        val item = _uiState.value
        return item.nombre.isNotBlank() &&
                item.descripcion.isNotBlank()
    }

    // Asegúrate de que tu CreateItemUIState tenga un campo `uri: Uri?`
    // Si no es el caso, crea una nueva data class o modifica la existente.
    // Ejemplo de CreateItemUIState para que funcione con Uri
    /*
    data class CreateItemUIState(
        val nombre: String = "",
        val descripcion: String = "",
        val cantidad: Int = 0,
        val uri: Uri? = null, // <- Añadir o asegurar este campo
        val imagenUrl: String? = null // Mantener esto si es necesario para el API Model
    )
    */

    private fun Item.toItemApiModel(): ItemApiModel {
        return ItemApiModel(
            id = id, // Asegúrate de que el ID también se pase al API Model para la actualización
            nombre = nombre,
            descripcion = descripcion,
            cantidad = cantidad,
            imagenUrl = imagenUrl
        )
    }

    private fun Uri.toByteArray(context: Context) =
        context.contentResolver.openInputStream(this)?.use { it.buffered().readBytes() }
}