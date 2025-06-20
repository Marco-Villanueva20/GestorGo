package pe.cibertec.gestorgo.features.inventario.data.remote

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.result.PostgrestResult
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.postgresListDataFlow
import io.github.jan.supabase.realtime.selectAsFlow
import io.github.jan.supabase.storage.storage
import io.ktor.http.ContentType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import pe.cibertec.gestorgo.features.inventario.data.model.ItemApiModel
import pe.cibertec.gestorgo.features.inventario.domain.service.ItemService
import javax.inject.Inject

class ItemRemoteDataSource @Inject constructor(
    private val client: SupabaseClient,
) : ItemService {
    private val bucket = client.storage.from("items")

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val _itemsFlow = MutableStateFlow<List<ItemApiModel>>(emptyList())
    private var isInitialized = false
    private val channel = client.channel("public:items:id")

    private val columns = Columns.raw(
        """
        id, 
        nombre, 
        descripcion, 
        cantidad,
        imagen_url,
        detalle_items (
            id, 
            cantidad, 
            fecha, 
            item_id
        )
        """.trimIndent()
    )

    private suspend fun initializeListener() {
        if (!isInitialized) {
            isInitialized = true

            // Suscripción al flujo de cambios en la tabla "items"
            channel.postgresListDataFlow(
                schema = "public",
                table = "items",
                primaryKey = ItemApiModel::id
            ).onEach {
                val lista = client.from("items")
                    .select(columns = columns)
                    .decodeList<ItemApiModel>()
                _itemsFlow.value = lista
            }.launchIn(scope)

            // Esto es suspend y necesita estar dentro del launch
            channel.subscribe()
        }
    }


    override suspend fun obtenerItemConDetallesPorId(id: Int): ItemApiModel {
        return client.from("items").select(columns = columns) {
            filter {
                eq("id", id)
            }
        }.decodeSingle<ItemApiModel>()
    }

    override suspend fun obtenerItemsConDetalles(): Flow<List<ItemApiModel>> {
        if (!isInitialized) {
            initializeListener() // Esto es suspend, así que está OK
            isInitialized = true
        }
        return _itemsFlow

    }

    override suspend fun obtenerItemPorId(id: Int): ItemApiModel {
        return client.from("items").select {
            filter {
                eq("id", id)
            }
        }.decodeSingle<ItemApiModel>()
    }

    @OptIn(SupabaseExperimental::class)
    override suspend fun obtenerItems(): Flow<List<ItemApiModel>> {
        return client
            .from("items")
            .selectAsFlow(primaryKey = ItemApiModel::id)
    }

    override suspend fun crearItem(itemApiModel: ItemApiModel): ItemApiModel {
        val idUsuario = client.auth.currentUserOrNull()
        itemApiModel.usuarioId = idUsuario?.id
        return client.from("items").insert(itemApiModel) {
            select()
        }.decodeSingle<ItemApiModel>()
    }

    override suspend fun actualizarItem(itemApiModel: ItemApiModel): ItemApiModel {
        val idUsuario = client.auth.currentUserOrNull()
        itemApiModel.usuarioId = idUsuario?.id
        return client.from("items").update(itemApiModel) {
            select()
            filter {
                itemApiModel.id?.let {
                    eq("id", it)
                }
            }
        }.decodeSingle<ItemApiModel>()
    }

    override suspend fun eliminarItem(id: Int): PostgrestResult {
        return client.from("items").delete {
            filter {
                eq("id", id)
            }
        }
    }

    override suspend fun crearEnBucketItem(fileName: String, data: ByteArray): String {
        val safeName = if (fileName.endsWith(".png")) fileName else "$fileName.png"
        try {
            bucket.upload(path = safeName, data = data)
            {
                upsert = true
                contentType = ContentType.Image.PNG
            }
            return bucket.publicUrl(safeName)
        } catch (e: IllegalArgumentException) {
            throw Exception("No se seleccionó ningún archivo: ${e.message}")
        } catch (e: Exception) {
            throw Exception("No se pudo subir el archivo: ${e.message}")
        }
    }

}