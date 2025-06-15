package pe.cibertec.gestorgo.features.inventario.data.remote

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.result.PostgrestResult
import io.github.jan.supabase.realtime.selectAsFlow
import io.github.jan.supabase.storage.storage
import io.ktor.http.ContentType
import kotlinx.coroutines.flow.Flow
import pe.cibertec.gestorgo.features.inventario.data.model.ItemApiModel
import pe.cibertec.gestorgo.features.inventario.domain.service.ItemService
import javax.inject.Inject

class ItemRemoteDataSource @Inject constructor(private val client: SupabaseClient): ItemService {
    private val bucket = client.storage.from("items")


    private val columns = Columns.raw(
        """
    id, 
    nombre, 
    descripcion, 
    cantidad,
    detalle_items (
        id, 
        cantidad, 
        fecha, 
        item_id
    )
    """.trimIndent()
    )


    override suspend fun getItemsWithDetails(): List<ItemApiModel> {
        val listaCompleta = client.from("items")
            .select(columns = columns)
            .decodeList<ItemApiModel>()

        println("Lista completa: $listaCompleta")
        return listaCompleta
    }

    override suspend fun getItemWithDetails(id: Int): ItemApiModel {
        return client.from("items").select(columns = columns){
            filter{
                eq("id", id)
            }
        }.decodeSingle<ItemApiModel>()
    }


    @OptIn(SupabaseExperimental::class)
    override suspend fun getItems(): Flow<List<ItemApiModel>> {
        return client
            .from("items")
            .selectAsFlow(primaryKey = ItemApiModel::id)
    }

    override suspend fun getItem(id: Int): ItemApiModel {
        return client.from("items").select {
            filter {
                eq("id", id)
            }
        }.decodeSingle<ItemApiModel>()
    }

    override suspend fun crearItem(itemApiModel: ItemApiModel): PostgrestResult {
        val idUsuario = client.auth.currentUserOrNull()
        println( idUsuario)
        itemApiModel.usuarioId = idUsuario?.id
        println(itemApiModel.usuarioId)
        return client.from("items").insert(itemApiModel)
    }

    override suspend fun actualizarItem(itemApiModel: ItemApiModel): PostgrestResult {
        return client.from("items").update(itemApiModel) {
            filter {
                itemApiModel.id?.let { eq("id", it) }
            }

        }
    }

    override suspend fun eliminarItem(id: Int): PostgrestResult {
        return client.from("items").delete {
            filter {
                eq("id", id)
            }
        }
    }

    override suspend fun createInBucketItem(fileName: String, data: ByteArray): String {
        val safeName = if (fileName.endsWith(".png")) fileName else "$fileName.png"
        try {
            bucket.upload(path = safeName, data = data)
            {
                upsert = true
                contentType = ContentType.Image.PNG
            }
            return bucket.publicUrl(safeName)
        } catch (e: IllegalArgumentException){
            throw Exception("No se seleccionó ningún archivo: ${e.message}")
        }
        catch (e: Exception) {
            throw Exception("No se pudo subir el archivo: ${e.message}")
        }
    }


}