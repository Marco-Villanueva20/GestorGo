package pe.cibertec.gestorgo.features.inventario.data.repository

import io.github.jan.supabase.postgrest.result.PostgrestResult
import pe.cibertec.gestorgo.features.inventario.data.model.DetalleItemApiModel
import pe.cibertec.gestorgo.features.inventario.data.remote.DetalleItemRemoteDataSource
import pe.cibertec.gestorgo.features.inventario.domain.repository.DetalleItemsRepository
import javax.inject.Inject

class DetalleItemsRepositoryImpl @Inject constructor(private val detalleItemRemoteDataSource: DetalleItemRemoteDataSource) :
    DetalleItemsRepository {

    override suspend fun listarDetalleItems(): List<DetalleItemApiModel> {
        return detalleItemRemoteDataSource.listarDetalleItems()
    }

    override suspend fun crearDetalleItem(detalleItemApiModel: DetalleItemApiModel): DetalleItemApiModel {
        return detalleItemRemoteDataSource.crearDetalleItem(detalleItemApiModel)
    }

    override suspend fun actualizarDetalleItem(detalleItemApiModel: DetalleItemApiModel): PostgrestResult {
        return detalleItemRemoteDataSource.actualizarDetalleItem(detalleItemApiModel)
    }

    override suspend fun eliminarDetalleItem(id: Int): DetalleItemApiModel {
        return detalleItemRemoteDataSource.eliminarDetalleItem(id)
    }
}