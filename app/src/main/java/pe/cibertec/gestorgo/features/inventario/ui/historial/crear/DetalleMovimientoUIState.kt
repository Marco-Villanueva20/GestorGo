package pe.cibertec.gestorgo.features.inventario.ui.historial.crear

import pe.cibertec.gestorgo.features.inventario.domain.model.Item

data class DetalleMovimientoUIState(
    val tipoMovimiento: String = "Ingreso", // "Ingreso" o "Salida"
    val cantidad: String = "",
    val listaItems: List<Item> = emptyList(),
    val itemSeleccionado: Item? = null,
    val showItemSelectionDialog: Boolean = false, // Para controlar la visibilidad del diálogo
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)