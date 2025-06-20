package pe.cibertec.gestorgo.features.inventario.ui.lista

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import pe.cibertec.gestorgo.features.inventario.domain.model.Item


@Composable
fun ListScreen(
    viewModel: ListItemViewModel = hiltViewModel(),
    onEditClick: (Item) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val items = uiState.listaItems // Ahora items contendrá la lista filtrada o completa
    var expandedItemId by remember { mutableStateOf<String?>(null) }

    // --- Estados del ViewModel para el diálogo ---
    val showDeleteDialog by viewModel.showDeleteConfirmationDialog.collectAsState()
    val itemToDelete by viewModel.itemToDelete.collectAsState()

    // --- Estado de la consulta de búsqueda del ViewModel ---
    val searchQuery by viewModel.searchQuery.collectAsState() // <-- Recoge el estado del query de búsqueda

    Column {
        // Pasa el estado y el callback del ViewModel a la BarraBusquedaHistorial
        BarraBusquedaHistorial(
            texto = searchQuery, // <-- Conectado al _searchQuery del ViewModel
            onTextoCambio = { viewModel.onSearchQueryChange(it) }, // <-- Llama a la función del ViewModel
            modifier = Modifier.fillMaxWidth()
        )
        // No es necesario el 'items.let', ya que 'items' ya es la lista del uiState
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 12.dp)
        ) {
            items(items) { item -> // Ahora 'items' ya está filtrado por el ViewModel
                ItemCard(
                    item = item,
                    isExpanded = expandedItemId == item.id.toString(),
                    onClick = {
                        expandedItemId =
                            if (expandedItemId == item.id.toString()) null else item.id.toString()
                    },
                    onEditClick = onEditClick,
                    onDeleteClick = { viewModel.confirmDeleteItem(item) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }


    // --- Mostrar el diálogo de confirmación si el estado lo indica ---
    if (showDeleteDialog) {
        itemToDelete?.let { item ->
            DeleteConfirmationDialog(
                itemName = item.nombre,
                onConfirm = {
                    viewModel.deleteConfirmed {} // Puedes pasar un callback para un Toast si es necesario
                },
                onDismiss = { viewModel.dismissDeleteConfirmationDialog() }
            )
        }
    }
}

// Las siguientes funciones (ItemCard, DeleteConfirmationDialog, BarraBusquedaHistorial)
// no necesitan cambios, ya que ahora reciben los datos y callbacks correctos.

@Composable
fun ItemCard(
    item: Item,
    modifier: Modifier = Modifier,
    isExpanded: Boolean = false,
    onEditClick: (Item) -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onClick: () -> Unit = {},
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = item.imagenUrl,
                contentDescription = "Imagen de ${item.nombre}",
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray)
                    .padding(end = 12.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 18.sp
                )
                Text(
                    text = item.descripcion,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                Text(
                    text = "Cantidad: ${item.cantidad}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W100
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Row(
                    modifier = Modifier
                        .background(Color(0xFFE0F7FA), shape = RoundedCornerShape(8.dp))
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilledIconButton(
                        onClick = { onEditClick(item) },
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    FilledIconButton(
                        onClick = onDeleteClick,
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
                        ),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                    }
                }
            }
        }
    }
}

@Composable
fun DeleteConfirmationDialog(
    itemName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirmar Eliminación") },
        text = { Text("¿Estás seguro de que deseas eliminar el artículo '$itemName'?") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Sí, Eliminar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
        modifier = modifier,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    )
}

@Composable
fun BarraBusquedaHistorial(
    texto: String,
    onTextoCambio: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = texto,
        onValueChange = onTextoCambio,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Buscar"
            )
        },
        placeholder = { Text("Buscar en la lista de artículos") },
        singleLine = true,
        shape = MaterialTheme.shapes.extraLarge,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}