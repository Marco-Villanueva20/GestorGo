package pe.cibertec.gestorgo.features.inventario.data.repository

import io.github.jan.supabase.postgrest.result.PostgrestResult
import kotlinx.coroutines.flow.Flow
import pe.cibertec.gestorgo.features.inventario.data.model.ItemApiModel
import pe.cibertec.gestorgo.features.inventario.data.remote.ItemRemoteDataSource
import pe.cibertec.gestorgo.features.inventario.domain.repository.ItemsRepository
import javax.inject.Inject

class ItemsRepositoryImpl @Inject constructor(private val itemsRemoteDataSource: ItemRemoteDataSource) :
    ItemsRepository {


    override suspend fun getItems(): Flow<List<ItemApiModel>> {
        return itemsRemoteDataSource.getItems()
    }

    override suspend fun crearItem(itemApiModel: ItemApiModel): ItemApiModel {
        return itemsRemoteDataSource.crearItem(itemApiModel)
    }

    override suspend fun actualizarItem(itemApiModel: ItemApiModel): ItemApiModel {
        return itemsRemoteDataSource.actualizarItem(itemApiModel)
    }

    override suspend fun eliminarItem(id: Int): PostgrestResult {
        return itemsRemoteDataSource.eliminarItem(id)
    }

    override suspend fun obtenerItemPorId(id: Int): ItemApiModel {
        return itemsRemoteDataSource.getItemWithDetails(id)
    }

    override suspend fun obtenerItemsConDetalles(): List<ItemApiModel> {
        return itemsRemoteDataSource.getItemsWithDetails()
    }

    override suspend fun obtenerItem(id: Int): ItemApiModel {
        return itemsRemoteDataSource.getItem(id)
    }

    override suspend fun createInBucketItem(name: String, data: ByteArray): String {
        return itemsRemoteDataSource.createInBucketItem(name, data)
    }
}