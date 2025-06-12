package pe.cibertec.gestorgo.features.inventario.domain.model

import pe.cibertec.gestorgo.features.inventario.data.model.ItemApiModel

data class DetalleItemApiModel(
    val cantidad : Int,
    val fecha: String,
    val itemId: Int,
    val itemApiModel: ItemApiModel? = null
)
