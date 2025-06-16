package pe.cibertec.gestorgo.features.inventario.ui.item.editaritem

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.rememberAsyncImagePainter // Usas coil3, esto es correcto
import pe.cibertec.gestorgo.features.inventario.domain.model.Item


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarScreen(
    viewModel: EditarItemViewModel = hiltViewModel(),
    // Estos parámetros se obtendrán de la navegación
    itemId: Int? = null,
    nombre: String,
    descripcion: String,
    imagenUrl: String? = null, // La URL de la imagen que viene de la base de datos
    cantidad: Int? = null,
    usuarioId: String? = null,
    onBackPressed: () -> Unit,
    onUpdate: () -> Unit,
    onCancel: () -> Unit
) {
    // !!!!!!!!!!!!!!!!!! CAMBIO IMPORTANTE AQUI !!!!!!!!!!!!!!!!!!!!!!!
    // Usar LaunchedEffect para inicializar el ViewModel con los datos del ítem
    // Esto asegura que la inicialización ocurra una vez cuando el Composable entra en la composición.
    LaunchedEffect(itemId, nombre, descripcion, imagenUrl, cantidad, usuarioId) {
        viewModel.setItemToEdit(
            Item(
                id = itemId,
                nombre = nombre,
                descripcion = descripcion,
                imagenUrl = imagenUrl,
                cantidad = cantidad,
                usuarioId = usuarioId
            )
        )
    }
    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.updateImage(uri)
        }
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Inventory2,
                            contentDescription = "Icono de Paquete",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text("Formulario de Artículos")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .imePadding()
        ) {
            OutlinedTextField(
                value = uiState.nombre,
                onValueChange = { viewModel.onNombreChange(it) },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.descripcion,
                onValueChange = { viewModel.onDescripcionChange(it) },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Imagen*",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // El ImageInputBox ya está listo para Uri
            ImageInputBox(
                imageUri = uiState.uri, // Pasa la Uri del UIState
                onImageClick = {
                    launcher.launch("image/*")
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.cantidad.toString(),
                onValueChange = { newValue ->
                    viewModel.onCantidadChange(newValue.filter { it.isDigit() })
                },
                label = { Text("Stock Total Disponible") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = onCancel,
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text("Cancelar")
                }
                Button(
                    onClick = {
                        viewModel.editarItem(context, onUpdate)
                    },
                    enabled = uiState.nombre.isNotBlank() && uiState.descripcion.isNotBlank(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Actualizar")
                }
            }
        }
    }
}

// ImageInputBox permanece igual, ya que acepta Uri? y Coil la maneja bien.
@Composable
fun ImageInputBox(
    imageUri: Uri?,
    onImageClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(120.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onImageClick)
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        if (imageUri != null) {
            androidx.compose.foundation.Image(
                painter = rememberAsyncImagePainter(model = imageUri),
                contentDescription = "Imagen seleccionada",
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Seleccionar imagen",
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Toca para agregar imagen",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}