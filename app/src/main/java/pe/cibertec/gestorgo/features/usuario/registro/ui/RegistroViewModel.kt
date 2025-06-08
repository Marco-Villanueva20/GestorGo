package pe.cibertec.gestorgo.features.usuario.registro.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.cibertec.gestorgo.core.ahoraEnPeruComoString
import pe.cibertec.gestorgo.data.model.Usuario
import pe.cibertec.gestorgo.features.usuario.data.UsuariosRepository
import javax.inject.Inject

@HiltViewModel
class RegistroViewModel @Inject constructor(private val repository: UsuariosRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterUIState())
    val uiState = _uiState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMsg = MutableStateFlow<String?>(null)
    val errorMsg = _errorMsg.asStateFlow()

    fun togglePasswordVisibility():Boolean{
        _uiState.value = _uiState.value.copy(passwordVisible = !_uiState.value.passwordVisible)
        return _uiState.value.passwordVisible
    }

    fun register() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMsg.value = null
            try {
                val usuario = Usuario(
                    nombres = _uiState.value.nombre,
                    apellidos = _uiState.value.apellido,
                    email = _uiState.value.email,
                    dni = _uiState.value.dni
                )
                repository.register(_uiState.value.password.text.toString(), usuario)
            } catch (e: Exception) {
                _errorMsg.value = "Error: ${e.localizedMessage}"
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
    }

    fun onNombreChange(nombre: String) {
        _uiState.value = _uiState.value.copy(nombre = nombre)
    }

    fun onApellidoChange(apellido: String) {
        _uiState.value = _uiState.value.copy(apellido = apellido)
    }


    fun onDniChange(dni: String) {
        _uiState.value = _uiState.value.copy(dni = dni)
    }

}