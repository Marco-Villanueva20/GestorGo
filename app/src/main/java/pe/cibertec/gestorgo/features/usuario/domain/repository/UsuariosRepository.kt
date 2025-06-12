package pe.cibertec.gestorgo.features.usuario.domain.repository

import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.coroutines.flow.Flow
import pe.cibertec.gestorgo.features.usuario.data.model.Usuario

interface UsuariosRepository {
    //Sesion
    suspend fun login(email: String, password: String)
    suspend fun logout()
    suspend fun register( password: String, usuario: Usuario): UserInfo?

    suspend fun obytenerUsuarios(): List<Usuario>
    suspend fun obtenerUsuariosRealTime(): Flow<List<Usuario>>
    suspend fun obtenerUsuario(id: String): Usuario
    suspend fun crearUsuario(usuario: Usuario): Usuario
    suspend fun actualizarUsuario(usuario: Usuario): Usuario
    suspend fun eliminarUsuario(id: String): Usuario
}