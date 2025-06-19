package pe.cibertec.gestorgo.features.inventario.ui.historial

import pe.cibertec.gestorgo.features.inventario.domain.model.HistorialItem

data class HistorialItemUIState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val showConfirmDialog: Boolean = false,
    val itemToDeleteId: Int? = null, // ID del historial a eliminar
    val itemToDeleteParentItemId: Int? = null, // ID del item principal asociado al historial
    val itemToDeleteQuantity: Int? = null, // Cantidad del detalle a eliminar
    val listaItems: List<HistorialItem> = emptyList(),
)
