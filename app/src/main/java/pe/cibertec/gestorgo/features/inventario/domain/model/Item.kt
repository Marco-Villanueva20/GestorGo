package pe.cibertec.gestorgo.features.inventario.domain.model

import pe.cibertec.gestorgo.features.inventario.data.model.DetalleItemApiModel

data class Item(
    val nombre: String,
    val descripcion: String,
    val imagenUrl: String,
    val cantidad: Int? = null,
    val detalleItemApiModels: List<DetalleItemApiModel>? = null
)
