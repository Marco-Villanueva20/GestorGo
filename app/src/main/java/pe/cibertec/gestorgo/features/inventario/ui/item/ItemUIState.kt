package pe.cibertec.gestorgo.features.inventario.ui.item

import android.net.Uri
import pe.cibertec.gestorgo.features.inventario.data.model.ItemApiModel
import pe.cibertec.gestorgo.features.inventario.domain.model.Item

data class ItemUIState(
    var listItem: List<ItemApiModel>? = emptyList()
)