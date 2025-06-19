package pe.cibertec.gestorgo.features.inventario.domain.repository

import io.github.jan.supabase.postgrest.result.PostgrestResult
import kotlinx.coroutines.flow.Flow
import pe.cibertec.gestorgo.features.inventario.data.model.ItemApiModel

interface ItemsRepository {
    suspend fun getItems(): Flow<List<ItemApiModel>>
    suspend fun crearItem(itemApiModel: ItemApiModel): ItemApiModel
    suspend fun actualizarItem(itemApiModel: ItemApiModel): ItemApiModel
    suspend fun eliminarItem(id: Int): PostgrestResult
    suspend fun obtenerItemPorId(id: Int): ItemApiModel
    suspend fun obtenerItemsConDetalles(): List<ItemApiModel>
    suspend fun obtenerItem(id: Int): ItemApiModel


    suspend fun createInBucketItem(name: String, data: ByteArray): String
}