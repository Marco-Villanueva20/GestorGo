package pe.cibertec.gestorgo.features.inventario.ui.listaelementos

import pe.cibertec.gestorgo.features.inventario.data.model.ItemApiModel

data class ItemUIState(
    val name: String = "",
    val description: String="",
    val iconResId: Int? = null,
    var listaItems: List<ItemApiModel> = emptyList()
)
