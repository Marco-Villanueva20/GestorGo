package pe.cibertec.gestorgo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import kotlinx.serialization.json.jsonPrimitive
import pe.cibertec.gestorgo.navigation.NavigationScreen
import pe.cibertec.gestorgo.ui.theme.GestorGoTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var supabaseClient: SupabaseClient

    override fun onCreate(savedInstanceState: Bundle?) {
        // Splash screen primero
        val splashScreen = installSplashScreen()

        // IMPORTANTE: Hilt inyecta los @Inject después de esto
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            GestorGoTheme {
                // 🔐 Ya es seguro acceder al supabaseClient
                val usuarioInfo = supabaseClient.auth.currentUserOrNull()

                val nombre = usuarioInfo?.identities?.firstOrNull()
                    ?.identityData?.get("nombres")?.jsonPrimitive?.content.orEmpty()
                NavigationScreen(
                    nombre = nombre,
                    usuarioInfo = usuarioInfo
                )
            }
        }

        splashScreen.setKeepOnScreenCondition {
            // Aquí puedes poner lógica real si tienes carga, pero por ahora:
            false
        }

    }
}
