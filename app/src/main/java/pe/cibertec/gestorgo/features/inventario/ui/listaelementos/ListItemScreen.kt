package pe.cibertec.gestorgo.features.inventario.ui.listaelementos

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
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import pe.cibertec.gestorgo.features.inventario.data.model.ItemApiModel


@Composable
fun ListScreen(
    viewModel: ListItemViewModel = hiltViewModel(),
    onCreateClick: () -> Unit,
    onEditClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val items = uiState.listaItems
    var expandedItemId by remember { mutableStateOf<String?>(null) }

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
                        itemApiModel = item,
                        isExpanded = expandedItemId == item.id.toString(),
                        onClick = {
                            expandedItemId =
                                if (expandedItemId == item.id.toString()) null else item.id.toString()
                        },
                        onEditClick = { },
                        onDeleteClick = {  }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun ItemCard(
    itemApiModel: ItemApiModel,
    modifier: Modifier = Modifier,
    isExpanded: Boolean = false,
    onEditClick: () -> Unit = {},
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
                model = itemApiModel.imagenUrl,
                contentDescription = "Imagen de ${itemApiModel.nombre}",
                modifier = Modifier
                    .size(64.dp) // Tamaño cuadrado bonito
                    .clip(RoundedCornerShape(8.dp)) // Bordes redondeados
                    .background(Color.LightGray) // Fondo mientras carga
                    .padding(end = 12.dp), // Espacio a la derecha
            )
            Spacer(modifier = Modifier.width(8.dp))
            // Parte izquierda: contenido
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = itemApiModel.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 18.sp
                )
                Text(
                    text = itemApiModel.descripcion,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }

            // Parte derecha: botones si está expandido

                AnimatedVisibility(visible = isExpanded) {
                    Row(
                        modifier = Modifier
                            .background(Color(0xFFE0F7FA), shape = RoundedCornerShape(8.dp))
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FilledIconButton(
                            onClick = onEditClick,
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
