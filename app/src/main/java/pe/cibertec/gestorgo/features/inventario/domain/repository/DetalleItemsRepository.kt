package pe.cibertec.gestorgo.features.inventario.domain.repository

import io.github.jan.supabase.postgrest.result.PostgrestResult
import pe.cibertec.gestorgo.features.inventario.data.model.DetalleItemApiModel

interface DetalleItemsRepository {
    suspend fun listarDetalleItems(): List<DetalleItemApiModel>
    suspend fun crearDetalleItem(detalleItemApiModel: DetalleItemApiModel): DetalleItemApiModel
    suspend fun actualizarDetalleItem(detalleItemApiModel: DetalleItemApiModel): PostgrestResult
    suspend fun eliminarDetalleItem(id: Int): DetalleItemApiModel
}