package pe.cibertec.gestorgo.features.usuario.ui.iniciosesion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.cibertec.gestorgo.features.usuario.domain.repository.UsuariosRepository
import javax.inject.Inject

@HiltViewModel
class InicioSesionViewModel @Inject constructor(private val usuariosRepository: UsuariosRepository): ViewModel() {
    private val _uiState = MutableStateFlow(LoginUIState())
    val uiState: StateFlow<LoginUIState> = _uiState.asStateFlow()


    fun updateUserEmail(email: String){
        _uiState.value = _uiState.value.copy(email = email)
    }

    fun togglePasswordVisibility():Boolean{
        _uiState.value = _uiState.value.copy(passwordVisible = !_uiState.value.passwordVisible)
        return _uiState.value.passwordVisible
    }

    fun iniciarSesion() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                usuariosRepository.login(_uiState.value.email, _uiState.value.contrasenha.text.toString())
                _uiState.value = _uiState.value.copy(isLoading = false, loginSuccess = true)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    loginSuccess = false,
                    errorMessage = e.message ?: "Error desconocido"
                )
            }
        }
    }
}