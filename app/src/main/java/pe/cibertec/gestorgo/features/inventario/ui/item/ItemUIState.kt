package pe.cibertec.gestorgo.features.inventario.ui.item

import pe.cibertec.gestorgo.features.inventario.data.model.ItemApiModel

data class ItemUIState(
    val name: String = "",
    val description: String="",
    val iconResId: Int? = null,
    val listaItems: List<ItemApiModel> = emptyList()
)
