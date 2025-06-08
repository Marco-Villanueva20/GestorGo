package pe.cibertec.gestorgo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import pe.cibertec.gestorgo.features.usuario.registro.ui.RegistroScreen
import pe.cibertec.gestorgo.ui.theme.GestorGoTheme
import pe.cibertec.gestorgo.ui.viewmodel.ElementosViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GestorGoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    RegistroScreen(paddingValues=innerPadding , onRegisterSuccess = {})
                }
            }
        }
    }
}

