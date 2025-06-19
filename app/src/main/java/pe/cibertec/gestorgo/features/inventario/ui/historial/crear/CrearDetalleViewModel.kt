package pe.cibertec.gestorgo.features.inventario.ui.historial.crear

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.cibertec.gestorgo.features.inventario.data.model.DetalleItemApiModel
import pe.cibertec.gestorgo.features.inventario.data.model.ItemApiModel
import pe.cibertec.gestorgo.features.inventario.domain.model.Item
import pe.cibertec.gestorgo.features.inventario.domain.repository.DetalleItemsRepository
import pe.cibertec.gestorgo.features.inventario.domain.repository.ItemsRepository // Importa el repositorio de Items
import javax.inject.Inject

@HiltViewModel
class CrearDetalleViewModel @Inject constructor(
    private val detalleItemsRepository: DetalleItemsRepository,
    private val itemsRepository: ItemsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetalleMovimientoUIState())
    val uiState: StateFlow<DetalleMovimientoUIState> = _uiState.asStateFlow()

    init {
        cargarItemsDisponibles()
    }

    // En CrearDetalleViewModel.kt
    private fun cargarItemsDisponibles() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val itemApiList = itemsRepository.obtenerItemsConDetalles() // O la función que uses
                val items = itemApiList.map { it.toItemDomain() }
                _uiState.update { it.copy(listaItems = items, isLoading = false) }

            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Error al cargar ítems: ${e.message}") }
            }
        }
    }
    fun onTipoMovimientoChange(tipo: String) {
        _uiState.update { it.copy(tipoMovimiento = tipo) }
    }

    fun onCantidadChange(cantidad: String) {
        // Solo permite números
        _uiState.update { it.copy(cantidad = cantidad.filter { char -> char.isDigit() }) }
    }

    fun onItemSeleccionado(item: Item) {
        _uiState.update { it.copy(itemSeleccionado = item, showItemSelectionDialog = false) }
    }

    fun showItemSelectionDialog() {
        _uiState.update { it.copy(showItemSelectionDialog = true) }
    }

    fun dismissItemSelectionDialog() {
        _uiState.update { it.copy(showItemSelectionDialog = false) }
    }

    fun registrarMovimiento(onSuccess: () -> Unit) {
        viewModelScope.launch {

            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val selectedItem = _uiState.value.itemSeleccionado
            val cantidad = _uiState.value.cantidad.toIntOrNull()

            if (selectedItem == null || cantidad == null || cantidad <= 0) {
                _uiState.update { it.copy(errorMessage = "Selecciona un ítem y una cantidad válida.", isLoading = false) }
                return@launch
            }

            try {
                // Primero, actualizar la cantidad del ítem en la tabla 'items'
                val nuevaCantidadItem = if (_uiState.value.tipoMovimiento == "Ingreso") {
                    (selectedItem.cantidad ?: 0) + cantidad
                } else {
                    val stockActual = selectedItem.cantidad ?: 0
                    if (cantidad > stockActual) {
                        _uiState.update { it.copy(errorMessage = "La cantidad de salida excede el stock disponible.", isLoading = false) }
                        return@launch
                    }
                    stockActual - cantidad
                }

                // Crear un ItemApiModel para actualizar el ítem
                val itemToUpdate = ItemApiModel(
                    id = selectedItem.id,
                    nombre = selectedItem.nombre,
                    descripcion = selectedItem.descripcion,
                    imagenUrl = selectedItem.imagenUrl,
                    cantidad = nuevaCantidadItem,
                    usuarioId = selectedItem.usuarioId
                )
                itemsRepository.actualizarItem(itemToUpdate)
                Log.d("CrearDetalleViewModel", "Item actualizado: $itemToUpdate")

                // Luego, registrar el detalle del movimiento en la tabla 'detalle_items'
                val detalleItemApiModel = DetalleItemApiModel(
                    cantidad = if (_uiState.value.tipoMovimiento == "Ingreso") cantidad else -cantidad,
                    itemId = selectedItem.id!!
                )
                detalleItemsRepository.crearDetalleItem(detalleItemApiModel) // <-- Aquí es donde probablemente falla
                Log.d("CrearDetalleViewModel", "Detalle de movimiento creado: $detalleItemApiModel") // <-- Esta línea no se ejecuta

                _uiState.update { it.copy(isLoading = false) }
                Log.d("CrearDetalleViewModel", "Movimiento registrado exitosamente.")
                onSuccess()
                Log.d("CrearDetalleViewModel", "Callback de éxito llamado.")
            } catch (e: Exception) {
                // ¡MUY IMPORTANTE! Loguea la excepción completa para diagnosticar.
                Log.e("CrearDetalleViewModel", "Error al registrar movimiento: ${e.message}", e)
                _uiState.update { it.copy(isLoading = false, errorMessage = "Error al registrar movimiento: ${e.message}") }
            }
        }
    }

    // Función de mapeo de ItemApiModel a tu modelo de dominio Item
    private fun ItemApiModel.toItemDomain(): Item {
        return Item(
            id = this.id,
            nombre = this.nombre,
            descripcion = this.descripcion,
            imagenUrl = this.imagenUrl,
            cantidad = this.cantidad,
            usuarioId = this.usuarioId
        )
    }

    // Asume que tienes un modelo Item en pe.cibertec.gestorgo.features.inventario.domain.model
    // data class Item(...)
}