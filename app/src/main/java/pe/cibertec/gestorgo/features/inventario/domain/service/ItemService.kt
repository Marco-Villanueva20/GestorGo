package pe.cibertec.gestorgo.features.inventario.domain.service

import io.github.jan.supabase.postgrest.result.PostgrestResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import pe.cibertec.gestorgo.features.inventario.data.model.ItemApiModel

interface ItemService {

    suspend fun obtenerItemPorId(id: Int): ItemApiModel
    suspend fun obtenerItemConDetallesPorId(id: Int): ItemApiModel
    suspend fun obtenerItemsConDetalles(): Flow<List<ItemApiModel>>
    suspend fun obtenerItems(): Flow<List<ItemApiModel>>


    suspend fun crearItem(itemApiModel: ItemApiModel): ItemApiModel
    suspend fun actualizarItem(itemApiModel: ItemApiModel): ItemApiModel
    suspend fun eliminarItem(id: Int): PostgrestResult


    suspend fun crearEnBucketItem(fileName: String, data: ByteArray): String
}