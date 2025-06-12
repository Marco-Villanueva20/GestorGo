package pe.cibertec.gestorgo.features.inventario.domain.repository

import io.github.jan.supabase.postgrest.result.PostgrestResult
import pe.cibertec.gestorgo.features.inventario.data.model.ItemApiModel

interface ItemsRepository {
    suspend fun listarItems(): List<ItemApiModel>
    suspend fun crearItem(itemApiModel: ItemApiModel): PostgrestResult
    suspend fun actualizarItem(itemApiModel: ItemApiModel): PostgrestResult
    suspend fun eliminarItem(id: Int): PostgrestResult
    suspend fun obtenerItemPorId(id: Int): ItemApiModel
    suspend fun obtenerItemsConDetalles(): List<ItemApiModel>
    suspend fun obtenerItem(id: Int): ItemApiModel
}