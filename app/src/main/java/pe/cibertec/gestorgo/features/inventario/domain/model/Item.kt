package pe.cibertec.gestorgo.features.inventario.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Item(
    val id: Int? = null,
    var nombre: String,
    var descripcion: String,
    var imagenUrl: String? = null,
    var cantidad: Int? = null,
    var usuarioId: String? = null,
)
