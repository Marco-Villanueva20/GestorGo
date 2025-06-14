package pe.cibertec.gestorgo.data.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage
import pe.cibertec.gestorgo.BuildConfig
import pe.cibertec.gestorgo.features.inventario.data.remote.DetalleItemRemoteDataSource
import pe.cibertec.gestorgo.features.inventario.data.remote.ItemRemoteDataSource
import pe.cibertec.gestorgo.features.inventario.data.repository.DetalleItemsRepositoryImpl
import pe.cibertec.gestorgo.features.inventario.data.repository.ItemsRepositoryImpl
import pe.cibertec.gestorgo.features.inventario.domain.repository.DetalleItemsRepository
import pe.cibertec.gestorgo.features.inventario.domain.repository.ItemsRepository
import pe.cibertec.gestorgo.features.inventario.domain.service.DetalleItemService
import pe.cibertec.gestorgo.features.inventario.domain.service.ItemService
import pe.cibertec.gestorgo.features.usuario.data.remote.UsuariosRemoteDataSource
import pe.cibertec.gestorgo.features.usuario.data.repository.UsuariosRepositoryImpl
import pe.cibertec.gestorgo.features.usuario.domain.repository.UsuariosRepository
import pe.cibertec.gestorgo.features.usuario.domain.service.UsuariosService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SupabaseModule {

    @Provides
    @Singleton
    fun proveerSupabaseClient(): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = BuildConfig.SUPABASE_API_URL,
            supabaseKey = BuildConfig.SUPABASE_API_KEY
        ) {
            install(Auth)
            install(Postgrest)
            install(Realtime)
            install(Storage)
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
interface UsuarioRepositoryModule {

    @Binds
    @Singleton
    fun bindUsuariosRepository(
        repo: UsuariosRepositoryImpl
    ): UsuariosRepository

    @Binds
    @Singleton
    fun bindUsuariosService(
        service: UsuariosRemoteDataSource
    ): UsuariosService
}

@Module
@InstallIn(SingletonComponent::class)
interface ItemRepositoryAndService {
    @Binds
    @Singleton
    fun bindItemRepository(
        repo: ItemsRepositoryImpl
    ): ItemsRepository

    @Binds
    @Singleton
    fun bindItemService(
        service: ItemRemoteDataSource
    ): ItemService

}

@Module
@InstallIn(SingletonComponent::class)
interface DetalleItemRepositoryAndService {
    @Binds
    @Singleton
    fun bindDetalleItemRepository(
        repo: DetalleItemsRepositoryImpl
    ): DetalleItemsRepository

    @Binds
    @Singleton
    fun bindDetalleItemService(
        service: DetalleItemRemoteDataSource
    ): DetalleItemService
}