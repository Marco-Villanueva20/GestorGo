package pe.cibertec.gestorgo.di

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage

object SupabaseClient {
    val client = createSupabaseClient(
        supabaseUrl = "https://nzyrrdqmyvprtexwmmyv.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im56eXJyZHFteXZwcnRleHdtbXl2Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDA1MjUwNzMsImV4cCI6MjA1NjEwMTA3M30.XvADfO5JljeJTmIjvAKvtO33sDSHLDKB6MRBzIZm9vo"
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