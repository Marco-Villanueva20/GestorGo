package pe.cibertec.gestorgo.features.inventario.domain.model


data class HistorialItem(
    val id: Int?, // Este DEBE ser el ID del DetalleItemApiModel
    val imagenUrl: String,
    val nombre: String,
    val fecha: String,
    val cantidad: Int,
    val parentItemId: Int? // Este será el ID del ItemApiModel principal al que pertenece este detalle
)
