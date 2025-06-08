package pe.cibertec.gestorgo.features.usuario.data

import android.util.Log
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.user.UserInfo
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.realtime.selectAsFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import pe.cibertec.gestorgo.data.model.Usuario
import javax.inject.Inject

interface UsuariosService {
    suspend fun obtenerUsuarios(): List<Usuario>
    suspend fun login(email: String, password: String)
    suspend fun logout()
    suspend fun register(password: String, usuario: Usuario): UserInfo?
    suspend fun obtenerUsuariosRealTime(): Flow<List<Usuario>>

    suspend fun obtenerUsuario(id: String): Usuario
    suspend fun crearUsuario(usuario: Usuario): Usuario
    suspend fun actualizarUsuario(usuario: Usuario): Usuario
    suspend fun eliminarUsuario(id: String): Usuario
}

class UsuariosServiceSupabase @Inject constructor(private val client: SupabaseClient):
    UsuariosService {
    override suspend fun obtenerUsuarios(): List<Usuario> {
        val usuarios: List<Usuario> = client
            .from("usuarios")
            .select()
            .decodeList()
        return usuarios
    }

    override suspend fun login(email: String, password: String) {
        try {
            client.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
        } catch (e: Exception) {
            Log.e("LoginError", "Error al iniciar sesión", e)
            throw e // o manejarlo según tu lógica de UI
        }
    }

    override suspend fun logout() {
        TODO("Not yet implemented")
    }

    override suspend fun register(password: String, usuario: Usuario): UserInfo? {
        val usuarioCreado : UserInfo? = client.auth.signUpWith(Email) {
            email = usuario.email
            this.password = password
            data = buildJsonObject {
                put("nombres", usuario.nombres)
                put("apellidos", usuario.apellidos)
                put("email", usuario.email)
                put("dni", usuario.dni)
                put("rol", usuario.rol)
            }
            Log.d("SupabaseUsuariosApiService", "Usuario creado: $data")
        }
        return usuarioCreado
    }

    @OptIn(SupabaseExperimental::class)
    override suspend fun obtenerUsuariosRealTime(): Flow<List<Usuario>> {
        return client
            .from("usuarios")
            .selectAsFlow(primaryKey = Usuario::id)
    }

    override suspend fun obtenerUsuario(id: String): Usuario {
        return client.from("usuarios").select {
            filter {
                eq("id", id)
            }
        }.decodeSingle<Usuario>()
    }

    override suspend fun crearUsuario(usuario: Usuario): Usuario {
        return client.from("usuarios").insert(usuario).decodeSingle<Usuario>()
    }

    override suspend fun actualizarUsuario(usuario: Usuario): Usuario {
        return client.from("usuarios").update(usuario) {
            filter {
                eq("id", usuario.id!!)
            }
        }.decodeSingle<Usuario>()
    }

    override suspend fun eliminarUsuario(id: String): Usuario {
        return client.from("usuarios").delete {
            filter {
                eq("id", id)
            }
        }.decodeSingle<Usuario>()
    }

}