package pe.cibertec.gestorgo.di

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
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModulo {

    @Provides
    @Singleton
    fun proveerSupabaseClient():  SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = "https://jfkhkwtbnkgpwzddybus.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Impma2hrd3RibmtncHd6ZGR5YnVzIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDkyNzI5MzgsImV4cCI6MjA2NDg0ODkzOH0.kFrXROJmpxYThfv3O8e7ohSMZATT7Cml_eHv2KHbjpU"
        ){
            install(Auth){
                scheme = "softmaps"
                host = "login-callback"
            }
            install(Postgrest)
            install(Realtime)
            install(Storage)
        }
    }
}