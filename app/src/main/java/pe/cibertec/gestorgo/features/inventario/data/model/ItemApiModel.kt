package pe.cibertec.gestorgo.features.inventario.data.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class ItemApiModel(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val imagenUrl: String?=null,
    val cantidad: Int? = null,
    @Contextual val detalleItemApiModels: List<DetalleItemApiModel>? = null
)
