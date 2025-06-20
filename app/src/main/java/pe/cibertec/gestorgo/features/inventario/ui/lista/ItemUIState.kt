package pe.cibertec.gestorgo.features.inventario.ui.lista

import pe.cibertec.gestorgo.features.inventario.domain.model.Item

data class ItemUIState(
    var search:String = "",
    val isLoading: Boolean = false,
    var listaItems: List<Item> = emptyList()
)
