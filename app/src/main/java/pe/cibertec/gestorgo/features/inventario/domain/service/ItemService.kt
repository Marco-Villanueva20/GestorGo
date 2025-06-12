package pe.cibertec.gestorgo.features.inventario.domain.service

import io.github.jan.supabase.postgrest.result.PostgrestResult
import pe.cibertec.gestorgo.features.inventario.data.model.ItemApiModel

interface ItemService {

    suspend fun getItemWithDetails(id: Int): ItemApiModel
    suspend fun getItemsWithDetails(): List<ItemApiModel>
    suspend fun getItems(): List<ItemApiModel>

    suspend fun getItem(id: Int): ItemApiModel
    suspend fun crearItem(itemApiModel: ItemApiModel): PostgrestResult
    suspend fun actualizarItem(itemApiModel: ItemApiModel): PostgrestResult
    suspend fun eliminarItem(id: Int): PostgrestResult

}