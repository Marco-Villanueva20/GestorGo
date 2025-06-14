package pe.cibertec.gestorgo.features.inventario.ui.item.crearitem

import android.net.Uri

data class CreateItemUIState (
    var uri: Uri? = null,
    var nombre:String = "",
    var descripcion:String = "",
    var cantidad:Int = 0,
    var imagenUrl:String? = null
)