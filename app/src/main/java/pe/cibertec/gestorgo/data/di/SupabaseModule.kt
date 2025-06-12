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
import pe.cibertec.gestorgo.features.usuario.domain.repository.UsuariosRepository
import pe.cibertec.gestorgo.features.usuario.data.repository.UsuariosRepositorySupabase
import pe.cibertec.gestorgo.features.usuario.data.remote.UsuariosService
import pe.cibertec.gestorgo.features.usuario.data.remote.UsuariosRemoteDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SupabaseModule {

    @Provides
    @Singleton
    fun proveerSupabaseClient():  SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = "https://jfkhkwtbnkgpwzddybus.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Impma2hrd3RibmtncHd6ZGR5YnVzIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDkyNzI5MzgsImV4cCI6MjA2NDg0ODkzOH0.kFrXROJmpxYThfv3O8e7ohSMZATT7Cml_eHv2KHbjpU"
        ){
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
        repo: UsuariosRepositorySupabase
    ): UsuariosRepository

    @Binds
    @Singleton
    fun bindUsuariosService(
        service: UsuariosRemoteDataSource
    ): UsuariosService

}