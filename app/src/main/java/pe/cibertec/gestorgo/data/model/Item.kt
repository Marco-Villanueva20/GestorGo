package pe.cibertec.gestorgo.data.model

data class Item(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val imagenUrl: String,
    val cantidad: Int? = null,
    val detalleItems: List<DetalleItem>? = null
)
