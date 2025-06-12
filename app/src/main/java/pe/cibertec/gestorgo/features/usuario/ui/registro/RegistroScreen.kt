package pe.cibertec.gestorgo.features.usuario.ui.registro

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.Difference
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import pe.cibertec.gestorgo.R
import pe.cibertec.gestorgo.core.InputField
import pe.cibertec.gestorgo.core.SecurePasswordField

@Composable
fun RegistroScreen(
    paddingValues: PaddingValues,
    registerViewModel: RegistroViewModel = hiltViewModel(),
    onRegisterSuccess: () -> Unit
) {
    val uiState by registerViewModel.uiState.collectAsState()
    val isFormValid = uiState.email.isNotBlank() && uiState.password.text.isNotBlank()
    val isLoading by registerViewModel.isLoading.collectAsState()
    val errorMsg by registerViewModel.errorMsg.collectAsState()

    val mediumPadding = dimensionResource(id = R.dimen.padding_medium)

    if (uiState.registerSuccess) {
        LaunchedEffect(Unit) { onRegisterSuccess() }
    }

    Column(
        Modifier
            .statusBarsPadding()
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(mediumPadding),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.registrarse),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally)
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
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
            value = uiState.email,
            onValueChange = { registerViewModel.onEmailChange(it) },
            modifier = Modifier.fillMaxWidth(),
            icon = Icons.Default.Email
        )

        SecurePasswordField(
            label = R.string.contrasenha,
            modifier = Modifier.fillMaxWidth(),
            value = uiState.password,
            passwordVisible = uiState.passwordVisible,
            onToggleVisibility = { registerViewModel.togglePasswordVisibility() }
        )

        InputField(
            label = R.string.nombre,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            value = uiState.nombre,
            onValueChange = { registerViewModel.onNombreChange(it) },
            modifier = Modifier.fillMaxWidth(),
            icon = Icons.Default.Person
        )

        InputField(
            label = R.string.apellido,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            value = uiState.apellido,
            onValueChange = { registerViewModel.onApellidoChange(it) },
            modifier = Modifier.fillMaxWidth(),
            icon = Icons.Default.Person
        )

        InputField(
            label = R.string.dni,
            keyboardOptions = KeyboardOptions.Default,
            value = uiState.dni,
            onValueChange = { registerViewModel.onDniChange(it) },
            modifier = Modifier.fillMaxWidth(),
            icon = Icons.Default.Difference
        )

        if (errorMsg != null) {
            Text(
                text = errorMsg ?: "",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Button(
            onClick = { registerViewModel.register() },
            enabled = isFormValid && !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text(stringResource(R.string.crear_cuenta))
            }
        }
    }
}

