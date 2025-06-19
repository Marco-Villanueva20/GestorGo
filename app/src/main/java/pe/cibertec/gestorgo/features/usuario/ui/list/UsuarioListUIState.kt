package pe.cibertec.gestorgo.features.usuario.ui.list

import pe.cibertec.gestorgo.features.usuario.data.model.Usuario

data class UsuarioListUIState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val usuarios: List<Usuario> = emptyList()
)