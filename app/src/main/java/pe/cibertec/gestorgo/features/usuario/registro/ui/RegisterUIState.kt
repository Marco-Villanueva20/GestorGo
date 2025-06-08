package pe.cibertec.gestorgo.features.usuario.registro.ui

import androidx.compose.foundation.text.input.TextFieldState

data class RegisterUIState(
    val nombre: String = "",
    val apellido: String = "",
    val email: String = "",
    val password: TextFieldState = TextFieldState(),
    val dni: String = "",
    val passwordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val loginSuccess: Boolean = false,
    val errorMessage: String? = null,
    val registerSuccess: Boolean = false
)