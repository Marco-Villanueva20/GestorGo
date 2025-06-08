package pe.cibertec.gestorgo.features.usuario.iniciosesion.ui

import androidx.compose.foundation.text.input.TextFieldState

data class LoginUIState (
    val email: String = "",
    val contrasenha: TextFieldState = TextFieldState(),
    val passwordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val loginSuccess: Boolean = false,
    val errorMessage: String? = null
)