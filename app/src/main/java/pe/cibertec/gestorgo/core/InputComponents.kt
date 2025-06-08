package pe.cibertec.gestorgo.core

import androidx.annotation.StringRes
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedSecureTextField
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun InputField(
    @StringRes label: Int,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions,
    value: String,
    icon: ImageVector,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        singleLine = true,
        onValueChange = onValueChange,
        label = { Text(text = stringResource(id = label)) },
        leadingIcon = { Icon(imageVector = icon, null) },
        keyboardOptions = keyboardOptions,
        modifier = modifier
    )
}
@Composable
fun SecurePasswordField(
    @StringRes label: Int,
    modifier: Modifier = Modifier,
    value: TextFieldState,
    passwordVisible: Boolean,
    onToggleVisibility: () -> Unit,

    ) {
    OutlinedSecureTextField(
        modifier = modifier,
        state = value,
        label = { Text(stringResource(id = label)) },
        leadingIcon = { Icon(imageVector = Icons.Filled.Lock, null) },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        textObfuscationMode =
        if (passwordVisible)
            TextObfuscationMode.Visible
        else
            TextObfuscationMode.RevealLastTyped,
        trailingIcon = {
            val visibilityIcon =
                if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
            val description = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
            IconButton(onClick = onToggleVisibility) {
                Icon(imageVector = visibilityIcon, contentDescription = description)
            }
        },
    )
}
