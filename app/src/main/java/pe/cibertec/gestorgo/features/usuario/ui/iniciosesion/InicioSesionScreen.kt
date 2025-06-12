package pe.cibertec.gestorgo.features.usuario.ui.iniciosesion

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import pe.cibertec.gestorgo.R
import pe.cibertec.gestorgo.ui.theme.GestorGoTheme
import pe.cibertec.gestorgo.core.InputField
import pe.cibertec.gestorgo.core.SecurePasswordField

@Composable
fun InicioSesionScreen(
    loginViewModel: InicioSesionViewModel = hiltViewModel(),
    onButtonAccess: () -> Unit,
    onButtonRegister: () -> Unit
) {
    val loginUiState by loginViewModel.uiState.collectAsState()
    val isFormValid = loginUiState.email.isNotBlank() && loginUiState.contrasenha.text.isNotBlank()
    if (loginUiState.loginSuccess) {
        LaunchedEffect(Unit) {
            onButtonAccess()
        }
    }
    val mediumPadding = dimensionResource(id = R.dimen.padding_medium)

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .safeDrawingPadding()
            .padding(mediumPadding)
    ) {
        Text(
            modifier = Modifier
                .padding(30.dp)
                .align(Alignment.CenterHorizontally),
            text = stringResource(R.string.iniciar_sesion),
            style = MaterialTheme.typography.titleLarge,
        )
        Image(
            painter = painterResource(R.drawable.login),
            contentDescription = "Logo de la empresa",
            modifier = Modifier
                .width(200.dp).height(150.dp)
                .align(Alignment.CenterHorizontally)
        )
        InputField(
            label = R.string.correo_electronico,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            value = loginUiState.email,
            onValueChange = { loginViewModel.updateUserEmail(it) },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth(),
            icon = Icons.Default.Email
        )
        SecurePasswordField(
            label = R.string.contrasenha,
            value = loginUiState.contrasenha,
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth(),
            passwordVisible = loginUiState.passwordVisible,
            onToggleVisibility = { loginViewModel.togglePasswordVisibility() }
        )


        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = isFormValid && !loginUiState.isLoading,
            onClick = { loginViewModel.iniciarSesion() }
        ) {
            if (loginUiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = stringResource(R.string.ingresar),
                    fontSize = 16.sp
                )
            }
        }
        loginUiState.errorMessage?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onButtonRegister
        ) {
            Text(
                text = stringResource(R.string.regitrarse),
                fontSize = 16.sp
            )
        }
    }
}




@Preview(
    showSystemUi = true,
    showBackground = true
)
@Composable
fun LoginPreview() {
    GestorGoTheme  {
        InicioSesionScreen(onButtonAccess = {}, onButtonRegister = {})
    }
}
