package pe.cibertec.gestorgo.features.inventario.data.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("items")
data class ItemApiModel(
    val id: Int? = null,
    var nombre: String,
    var descripcion: String,
    @SerialName("imagen_url" ) var imagenUrl: String? = null,
    var cantidad: Int? = null,
    @SerialName("usuario_id") var usuarioId: String? = null,
    @Contextual @SerialName("detalle_items") val detalleItemApiModels: List<DetalleItemApiModel>? = null
)
