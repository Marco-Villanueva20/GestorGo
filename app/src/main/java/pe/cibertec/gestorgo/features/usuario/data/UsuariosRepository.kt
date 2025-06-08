package pe.cibertec.gestorgo.features.usuario.data

import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.coroutines.flow.Flow
import pe.cibertec.gestorgo.data.model.Usuario
import javax.inject.Inject

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


class UsuariosRepositorySupabase @Inject constructor(private val usuariosService: UsuariosService):
    UsuariosRepository {
    override suspend fun login(email: String, password: String) {
        return usuariosService.login(email, password)
    }

    override suspend fun logout() {
        return usuariosService.logout()
    }

    override suspend fun register(password: String, usuario: Usuario) : UserInfo?{
        return usuariosService.register(password, usuario)
    }

    override suspend fun obytenerUsuarios(): List<Usuario> {
        return usuariosService.obtenerUsuarios()
    }

    override suspend fun obtenerUsuariosRealTime(): Flow<List<Usuario>> {
        return usuariosService.obtenerUsuariosRealTime()
    }

    override suspend fun obtenerUsuario(id: String): Usuario {
        return usuariosService.obtenerUsuario(id)
    }

    override suspend fun crearUsuario(usuario: Usuario): Usuario {
        return usuariosService.crearUsuario(usuario)
    }

    override suspend fun actualizarUsuario(usuario: Usuario): Usuario {
        return usuariosService.actualizarUsuario(usuario)
    }

    override suspend fun eliminarUsuario(id: String): Usuario {
        return usuariosService.eliminarUsuario(id)
    }

}





