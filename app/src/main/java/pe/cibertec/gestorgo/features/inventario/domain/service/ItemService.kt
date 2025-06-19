package pe.cibertec.gestorgo.features.inventario.domain.service

import io.github.jan.supabase.postgrest.result.PostgrestResult
import kotlinx.coroutines.flow.Flow
import pe.cibertec.gestorgo.features.inventario.data.model.ItemApiModel

interface ItemService {

    suspend fun getItemWithDetails(id: Int): ItemApiModel
    suspend fun getItemsWithDetails(): List<ItemApiModel>
    suspend fun getItems(): Flow<List<ItemApiModel>>

    suspend fun getItem(id: Int): ItemApiModel
    suspend fun crearItem(itemApiModel: ItemApiModel): ItemApiModel
    suspend fun actualizarItem(itemApiModel: ItemApiModel): ItemApiModel
    suspend fun eliminarItem(id: Int): PostgrestResult


    suspend fun createInBucketItem(fileName: String, data: ByteArray): String
}