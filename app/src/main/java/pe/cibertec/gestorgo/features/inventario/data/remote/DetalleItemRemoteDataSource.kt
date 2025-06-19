package pe.cibertec.gestorgo.features.inventario.data.remote

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import pe.cibertec.gestorgo.features.inventario.data.model.DetalleItemApiModel
import pe.cibertec.gestorgo.features.inventario.domain.service.DetalleItemService
import javax.inject.Inject

class DetalleItemRemoteDataSource @Inject constructor(private val client: SupabaseClient): DetalleItemService {
    override suspend fun listarDetalleItems(): List<DetalleItemApiModel> {
        return  client.from("detalle_items").select().decodeList<DetalleItemApiModel>()
    }
    override suspend fun crearDetalleItem(detalleItemApiModel: DetalleItemApiModel): DetalleItemApiModel {
        return client.from("detalle_items").insert(detalleItemApiModel){
            select()
        }.decodeSingle<DetalleItemApiModel>()
    }

    override suspend fun actualizarDetalleItem(detalleItemApiModel: DetalleItemApiModel) : DetalleItemApiModel {
        return client.from("detalle_items").update(detalleItemApiModel) {
            select()
            filter {
                eq("id", detalleItemApiModel.id!!)
            }
        }.decodeSingle<DetalleItemApiModel>()
    }
    override suspend fun eliminarDetalleItem(id: Int): DetalleItemApiModel {
        return client.from("detalle_items").delete {
            select()
            filter {
                eq("id", id)
            }
        }.decodeSingle<DetalleItemApiModel>()
    }
}