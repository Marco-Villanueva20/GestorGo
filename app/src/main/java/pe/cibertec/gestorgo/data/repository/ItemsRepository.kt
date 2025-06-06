package pe.cibertec.gestorgo.data.repository

import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.result.PostgrestResult
import pe.cibertec.gestorgo.data.model.Item
import pe.cibertec.gestorgo.di.SupabaseClient

class ItemsRepository {
    private val client = SupabaseClient.client

    private val columns = Columns.raw(
        """ id, nombre, descripcion, cantidad,
    detalles_items (
    id, cantidad, fecha, item_id
    )
   """.trimIndent()
    )
    suspend fun getItemWithDetails(id: Int): Item {
        return client.from("items").select(columns = columns){
            filter{
                eq("id", id)
            }
        }.decodeSingle<Item>()
    }
    suspend fun getItemsWithDetails(): List<Item> {
        return client.from("items").select(columns = columns).decodeList<Item>()
    }

    suspend fun getItems(): List<Item> {
        return client.from("items").select().decodeList<Item>()
    }

    suspend fun getItem(id: Int): Item {
        return client.from("items").select {
            filter {
                eq("id", id)
            }
        }.decodeSingle<Item>()
    }

    suspend fun crearItem(item: Item): PostgrestResult {
        return client.from("items").insert(item);
    }

    suspend fun actualizarItem(item: Item): PostgrestResult {
        return client.from("items").update(item) {
            filter {
                eq("id", item.id)
            }

        }
    }

    suspend fun eliminarItem(id: Int): PostgrestResult {
        return client.from("items").delete {
            filter {
                eq("id", id)
            }
        }
    }

}