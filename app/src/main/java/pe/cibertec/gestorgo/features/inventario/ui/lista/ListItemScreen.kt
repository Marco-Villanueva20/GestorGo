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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import pe.cibertec.gestorgo.features.inventario.domain.model.Item


@Composable
fun ListScreen(
    viewModel: ListItemViewModel = hiltViewModel(),
    onCreateClick: () -> Unit,
    onEditClick: (Item) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val items = uiState.listaItems
    var expandedItemId by remember { mutableStateOf<String?>(null) }

    // --- Nuevos estados del ViewModel para el diálogo ---
    val showDeleteDialog by viewModel.showDeleteConfirmationDialog.collectAsState()
    val itemToDelete by viewModel.itemToDelete.collectAsState()
    // ----------------------------------------------------

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onCreateClick) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Añadir nuevo"
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { paddingValues ->
        items.let { itemList ->
            LazyColumn(
                modifier = Modifier.padding(paddingValues),
                contentPadding = PaddingValues(horizontal = 12.dp)
            ) {
                items(itemList) { item ->
                    ItemCard(
                        item = item,
                        isExpanded = expandedItemId == item.id.toString(),
                        onClick = {
                            expandedItemId =
                                if (expandedItemId == item.id.toString()) null else item.id.toString()
                        },
                        onEditClick = onEditClick,
                        // --- Cambio aquí: Pasar la función del ViewModel para confirmar eliminación ---
                        onDeleteClick = { viewModel.confirmDeleteItem(item) }
                        // -------------------------------------------------------------------------
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }

    // --- Mostrar el diálogo de confirmación si el estado lo indica ---
    if (showDeleteDialog) {
        itemToDelete?.let { item ->
            DeleteConfirmationDialog(
                itemName = item.nombre,
                onConfirm = {
                    viewModel.deleteConfirmed {
                        // Opcional: callback para acciones después de eliminar, como un Toast.
                        // onDeleteClick(item.id!!) // Si aun quieres que se notifique al navegar
                    }
                },
                onDismiss = { viewModel.dismissDeleteConfirmationDialog() }
            )
        }
    }
    // -----------------------------------------------------------------
}

@Composable
fun ItemCard(
    item: Item,
    modifier: Modifier = Modifier,
    isExpanded: Boolean = false,
    onEditClick: (Item) -> Unit = {},
    onDeleteClick: () -> Unit = {}, // Esta ahora es para disparar la confirmación
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
                        onClick = onDeleteClick, // Este es el que dispara el diálogo
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
            dismissOnBackPress = true, // Permite cerrar con el botón de atrás
            dismissOnClickOutside = true // Permite cerrar haciendo clic fuera del diálogo
        )
    )
}