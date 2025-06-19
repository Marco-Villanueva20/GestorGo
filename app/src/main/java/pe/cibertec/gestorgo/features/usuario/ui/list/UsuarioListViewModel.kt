package pe.cibertec.gestorgo.features.usuario.ui.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.cibertec.gestorgo.features.usuario.domain.repository.UsuariosRepository
import javax.inject.Inject

@HiltViewModel
class UsuarioViewModel @Inject constructor(
    private val usuarioRepository: UsuariosRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UsuarioListUIState())
    val uiState = _uiState.asStateFlow()

    init {
        listarUsuarios()
    }

    private fun listarUsuarios() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val listaUsuarios = usuarioRepository.obytenerUsuarios()
                Log.d("UsuarioViewModel", "Usuarios cargados: ${listaUsuarios.size}")
                _uiState.update { it.copy(usuarios = listaUsuarios, isLoading = false) }
            } catch (e: Exception) {
                Log.e("UsuarioViewModel", "Error al cargar usuarios: ${e.message}", e)
                _uiState.update { it.copy(isLoading = false, errorMessage = "Error al cargar usuarios: ${e.message}") }
            }
        }
    }
}