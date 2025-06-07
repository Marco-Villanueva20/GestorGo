package pe.cibertec.gestorgo.data.repository

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.result.PostgrestResult
import pe.cibertec.gestorgo.data.model.DetalleItem
import javax.inject.Inject

class DetalleItemsRepository @Inject constructor(private val client: SupabaseClient) {

    suspend fun listarDetalleItems(): List<DetalleItem> {
        return  client.from("detalle_items").select().decodeList<DetalleItem>()
    }
    suspend fun crearDetalleItem(detalleItem: DetalleItem): DetalleItem {
        return client.from("detalle_items").insert(detalleItem).decodeSingle<DetalleItem>()
    }
    suspend fun actualizarDetalleItem(detalleItem: DetalleItem): PostgrestResult {
        return client.from("detalle_items").update(detalleItem)
    }
    suspend fun eliminarDetalleItem(id: Int): DetalleItem {
        return client.from("detalle_items").delete {
            filter {
                eq("id", id)
            }
        }.decodeSingle<DetalleItem>()
    }
}