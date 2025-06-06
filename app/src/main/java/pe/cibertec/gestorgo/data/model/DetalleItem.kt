package pe.cibertec.gestorgo.data.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DetalleItem(
    val id: Int,
    val cantidad : Int,
    val fecha: String,
    @SerialName("item_id") val itemId: Int,
    @Contextual val item: Item? = null
)