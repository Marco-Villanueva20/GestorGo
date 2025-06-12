package pe.cibertec.gestorgo.features.inventario.data.remote

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.result.PostgrestResult
import pe.cibertec.gestorgo.features.inventario.data.model.ItemApiModel
import pe.cibertec.gestorgo.features.inventario.domain.service.ItemService
import javax.inject.Inject

class ItemRemoteDataSource @Inject constructor(private val client: SupabaseClient): ItemService {

    private val columns = Columns.raw(
        """ id, nombre, descripcion, cantidad,
    detalles_items (
    id, cantidad, fecha, item_id
    )
   """.trimIndent()
    )

    override suspend fun getItemWithDetails(id: Int): ItemApiModel {
        return client.from("items").select(columns = columns){
            filter{
                eq("id", id)
            }
        }.decodeSingle<ItemApiModel>()
    }
    override suspend fun getItemsWithDetails(): List<ItemApiModel> {
        return client.from("items").select(columns = columns).decodeList<ItemApiModel>()
    }

    override suspend fun getItems(): List<ItemApiModel> {
        return client.from("items").select().decodeList<ItemApiModel>()
    }

    override suspend fun getItem(id: Int): ItemApiModel {
        return client.from("items").select {
            filter {
                eq("id", id)
            }
        }.decodeSingle<ItemApiModel>()
    }

    override suspend fun crearItem(itemApiModel: ItemApiModel): PostgrestResult {
        return client.from("items").insert(itemApiModel)
    }

    override suspend fun actualizarItem(itemApiModel: ItemApiModel): PostgrestResult {
        return client.from("items").update(itemApiModel) {
            filter {
                eq("id", itemApiModel.id)
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



}