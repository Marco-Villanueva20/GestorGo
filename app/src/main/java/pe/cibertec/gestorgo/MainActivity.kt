package pe.cibertec.gestorgo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import pe.cibertec.gestorgo.navigation.NavigationScreen
import pe.cibertec.gestorgo.ui.theme.GestorGoTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GestorGoTheme {
                    NavigationScreen()
            }
        }
    }
}

