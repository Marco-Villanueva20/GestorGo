package pe.cibertec.gestorgo.data.repository

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import pe.cibertec.gestorgo.data.model.Usuario
import javax.inject.Inject

class UsuariosRepository @Inject constructor(private val client: SupabaseClient) {
    suspend fun obtenerUsuarios(): List<Usuario> {
        return client.from("usuarios").select().decodeList<Usuario>()
    }

    suspend fun obtenerUsuario(id: String): Usuario {
        return client.from("usuarios").select {
            filter {
                eq("id", id)
            }
        }.decodeSingle<Usuario>()
    }

    suspend fun crearUsuario(usuario: Usuario): Usuario {
        return client.from("usuarios").insert(usuario).decodeSingle<Usuario>()
    }
    suspend fun actualizarUsuario(usuario: Usuario): Usuario {
        return client.from("usuarios").update(usuario) {
            filter {
                eq("id", usuario.id!!)
            }
        }.decodeSingle<Usuario>()
    }

    suspend fun eliminarUsuario(id: String): Usuario {
        return client.from("usuarios").delete {
            filter {
                eq("id", id)
            }
        }.decodeSingle<Usuario>()
    }
}