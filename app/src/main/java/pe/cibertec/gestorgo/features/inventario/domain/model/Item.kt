package pe.cibertec.gestorgo.features.inventario.domain.model

data class Item(
    var nombre: String,
    var descripcion: String,
    var imagenUrl: String? = null,
    var cantidad: Int? = null,
    var usuarioId: String? = null,
)
