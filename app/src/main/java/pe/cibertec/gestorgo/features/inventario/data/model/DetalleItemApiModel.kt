package pe.cibertec.gestorgo.features.inventario.data.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("detalle_items")
data class DetalleItemApiModel(
    val id: Int,
    val cantidad : Int,
    val fecha: String,
    @SerialName("item_id") val itemId: Int,
    @Contextual val itemApiModel: ItemApiModel? = null
)