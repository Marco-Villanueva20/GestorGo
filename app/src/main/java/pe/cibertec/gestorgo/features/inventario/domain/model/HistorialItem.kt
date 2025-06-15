package pe.cibertec.gestorgo.features.inventario.domain.model

data class HistorialItem(
    val imagenUrl: String,
    val nombre : String,
    val fecha: String,
    val cantidad: Int,
)
