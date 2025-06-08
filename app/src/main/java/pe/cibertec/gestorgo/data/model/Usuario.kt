package pe.cibertec.gestorgo.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("usuarios")
data class Usuario(
    var id: String? = null,
    val nombres: String,
    val apellidos: String,
    val email: String,
    val dni: String,
    val rol: String = "CLIENTE", // Valor por defecto
    val creado:String? = null
)