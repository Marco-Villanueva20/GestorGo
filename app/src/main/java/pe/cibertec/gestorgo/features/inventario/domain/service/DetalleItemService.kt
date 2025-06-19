package pe.cibertec.gestorgo.features.inventario.domain.service

import io.github.jan.supabase.postgrest.result.PostgrestResult
import pe.cibertec.gestorgo.features.inventario.data.model.DetalleItemApiModel

interface DetalleItemService {
    suspend fun listarDetalleItems(): List<DetalleItemApiModel>
    suspend fun crearDetalleItem(detalleItemApiModel: DetalleItemApiModel): DetalleItemApiModel
    suspend fun actualizarDetalleItem(detalleItemApiModel: DetalleItemApiModel): DetalleItemApiModel
    suspend fun eliminarDetalleItem(id: Int): DetalleItemApiModel

    //suspend fun listarDetalleItemsConItem(): List<DetalleItemApiModel>
}