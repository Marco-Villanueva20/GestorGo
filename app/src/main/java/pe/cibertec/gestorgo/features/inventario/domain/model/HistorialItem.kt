package pe.cibertec.gestorgo.features.inventario.domain.model

data class HistorialItem(
    val id : Int? = null,
    val imagenUrl: String,
    val nombre : String,
    val fecha: String,
    val cantidad: Int,
)
