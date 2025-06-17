package pe.cibertec.gestorgo.features.inventario.ui.historial.crear

import android.util.Log // Asegúrate de tener esta importación
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import pe.cibertec.gestorgo.features.inventario.domain.model.Item
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearDetalleScreen(
    viewModel: CrearDetalleViewModel = hiltViewModel(),
    onBackPressed: () -> Unit,
    onSuccess: () -> Unit // Callback para cuando se registra el movimiento
) {
    val uiState by viewModel.uiState.collectAsState()

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
                            contentDescription = "Icono de Inventario",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text("Formulario de Movimiento")
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
                colors = TopAppBarDefaults.topAppBarColors(
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
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Sección de Tipo de Movimiento (Ingreso/Salida)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                listOf("Ingreso", "Salida").forEach { tipo ->
                    FilterChip(
                        selected = uiState.tipoMovimiento == tipo,
                        onClick = { viewModel.onTipoMovimientoChange(tipo) },
                        label = { Text(tipo) },
                        modifier = Modifier.weight(1f).padding(horizontal = 4.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            // Selector de Ítems (Drop-down simulado con un cuadro de texto y un diálogo)
            Text(
                text = "Seleccionar Ítem*",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )
            // *** CAMBIO AQUÍ: ENVOLVER OutlinedTextField en un Box o Column separado con clickable ***
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp)) // Asegura que el clic sea dentro de la forma
                    .clickable {
                        Log.d("CrearDetalleScreen", "Click en OutlinedTextField container. Mostrando diálogo...")
                        viewModel.showItemSelectionDialog()
                    }
            ) {
                OutlinedTextField(
                    value = uiState.itemSeleccionado?.nombre ?: "Toca para seleccionar",
                    onValueChange = { /* No permitir edición manual */ },
                    readOnly = true, // No editable directamente
                    label = { Text("Artículo") },
                    trailingIcon = {
                        Icon(Icons.Default.ExpandMore, contentDescription = "Seleccionar Ítem")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false // Deshabilita la interacción interna del TextField
                    // Esto es clave para que el `clickable` del padre funcione sin interferencias.
                    // El valor aún se muestra, pero el campo no es editable.
                )
            }
            // *** FIN DEL CAMBIO ***

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de Cantidad
            OutlinedTextField(
                value = uiState.cantidad,
                onValueChange = { viewModel.onCantidadChange(it) },
                label = { Text("Cantidad*") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Campo de Fecha (Solo visual, no editable)
            OutlinedTextField(
                value = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
                onValueChange = { /* No editable */ },
                label = { Text("Fecha y Hora") },
                readOnly = true,
                trailingIcon = {
                    Icon(Icons.Default.CalendarToday, contentDescription = "Fecha")
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))

            // Mensaje de error
            uiState.errorMessage?.let { message ->
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                )
            }

            // Botones de acción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = onBackPressed, // Asume que onBackPressed es para cancelar y volver
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text("Cancelar")
                }
                Button(
                    onClick = { viewModel.registrarMovimiento(onSuccess) },
                    enabled = !uiState.isLoading && uiState.itemSeleccionado != null && uiState.cantidad.toIntOrNull() != null && uiState.cantidad.toIntOrNull()!! > 0,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Guardar")
                    }
                }
            }
        }
    }

    // Diálogo de selección de ítems
    if (uiState.showItemSelectionDialog) {
        ItemSelectionDialog(
            items = uiState.listaItems,
            onItemSelected = { item -> viewModel.onItemSeleccionado(item) },
            onDismiss = { viewModel.dismissItemSelectionDialog() }
        )
    }
}

// -------------------------------------------------------------------------------------------------
// Nuevo Composable para el diálogo de selección de ítems
// -------------------------------------------------------------------------------------------------
@Composable
fun ItemSelectionDialog(
    items: List<Item>,
    onItemSelected: (Item) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Seleccionar Artículo") },
        text = {
            Column {
                Spacer(modifier = Modifier.height(8.dp))

                if (items.isEmpty()) {
                    Text("No hay artículos disponibles.", modifier = Modifier.padding(16.dp))
                } else {
                    LazyColumn {
                        items(items) { item ->
                            ItemSelectionRow(item = item, onItemSelected = {
                                onItemSelected(it)
                                onDismiss() // Cierra el diálogo al seleccionar
                            })
                            // Puedes añadir un Divider() aquí si quieres separadores entre ítems
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar")
            }
        }
    )
}

@Composable
fun ItemSelectionRow(
    item: Item,
    onItemSelected: (Item) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemSelected(item) }
            .padding(vertical = 8.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icono o Imagen del ítem
        item.imagenUrl?.let { url ->
            AsyncImage(
                model = url,
                contentDescription = "Imagen de ${item.nombre}",
                modifier = Modifier
                    .size(48.dp) // Tamaño de la imagen en el selector
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray)
            )
        } ?: Icon( // Placeholder si no hay imagen
            imageVector = Icons.Default.Inventory2,
            contentDescription = "Sin imagen",
            modifier = Modifier.size(48.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = item.nombre, style = MaterialTheme.typography.titleMedium)
            // Puedes añadir más detalles aquí si quieres, como stock actual
            // Text(text = "Stock: ${item.cantidad ?: 0}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
    }
}

// Asegúrate de que este archivo DetalleMovimientoUIState esté en el mismo paquete
// o importado correctamente si está en otro lado.
// package pe.cibertec.gestorgo.features.inventario.ui.historial.crear

// import pe.cibertec.gestorgo.features.inventario.domain.model.Item

// data class DetalleMovimientoUIState(
//     val tipoMovimiento: String = "Ingreso", // "Ingreso" o "Salida"
//     val cantidad: String = "",
//     val listaItems: List<Item> = emptyList(),
//     val itemSeleccionado: Item? = null,
//     val showItemSelectionDialog: Boolean = false, // Para controlar la visibilidad del diálogo
//     val isLoading: Boolean = false,
//     val errorMessage: String? = null
// )


// Asegúrate de que este ViewModel esté en el mismo paquete
// o importado correctamente si está en otro lado.
// package pe.cibertec.gestorgo.features.inventario.ui.historial.crear

// import androidx.lifecycle.ViewModel
// import androidx.lifecycle.viewModelScope
// import dagger.hilt.android.lifecycle.HiltViewModel
// import kotlinx.coroutines.flow.MutableStateFlow
// import kotlinx.coroutines.flow.StateFlow
// import kotlinx.coroutines.flow.asStateFlow
// import kotlinx.coroutines.flow.update
// import kotlinx.coroutines.launch
// import pe.cibertec.gestorgo.features.inventario.data.model.DetalleItemApiModel
// import pe.cibertec.gestorgo.features.inventario.data.model.ItemApiModel
// import pe.cibertec.gestorgo.features.inventario.domain.model.Item
// import pe.cibertec.gestorgo.features.inventario.domain.repository.DetalleItemsRepository
// import pe.cibertec.gestorgo.features.inventario.domain.repository.ItemsRepository
// import javax.inject.Inject

// @HiltViewModel
// class CrearDetalleViewModel @Inject constructor(
//     private val detalleItemsRepository: DetalleItemsRepository,
//     private val itemsRepository: ItemsRepository
// ) : ViewModel() {

//     private val _uiState = MutableStateFlow(DetalleMovimientoUIState())
//     val uiState: StateFlow<DetalleMovimientoUIState> = _uiState.asStateFlow()

//     init {
//         cargarItemsDisponibles()
//     }

//     private fun cargarItemsDisponibles() {
//         viewModelScope.launch {
//             _uiState.update { it.copy(isLoading = true, errorMessage = null) }
//             try {
//                 val itemApiList = itemsRepository.obtenerItemsConDetalles()
//                 val items = itemApiList.map { it.toItemDomain() }
//                 _uiState.update { it.copy(listaItems = items, isLoading = false) }
//             } catch (e: Exception) {
//                 _uiState.update { it.copy(isLoading = false, errorMessage = "Error al cargar ítems: ${e.message}") }
//             }
//         }
//     }

//     fun onTipoMovimientoChange(tipo: String) {
//         _uiState.update { it.copy(tipoMovimiento = tipo) }
//     }

//     fun onCantidadChange(cantidad: String) {
//         _uiState.update { it.copy(cantidad = cantidad.filter { char -> char.isDigit() }) }
//     }

//     fun onItemSeleccionado(item: Item) {
//         _uiState.update { it.copy(itemSeleccionado = item, showItemSelectionDialog = false) }
//     }

//     fun showItemSelectionDialog() {
//         _uiState.update { it.copy(showItemSelectionDialog = true) }
//     }

//     fun dismissItemSelectionDialog() {
//         _uiState.update { it.copy(showItemSelectionDialog = false) }
//     }

//     fun registrarMovimiento(onSuccess: () -> Unit) {
//         viewModelScope.launch {
//             _uiState.update { it.copy(isLoading = true, errorMessage = null) }
//             val selectedItem = _uiState.value.itemSeleccionado
//             val cantidad = _uiState.value.cantidad.toIntOrNull()

//             if (selectedItem == null || cantidad == null || cantidad <= 0) {
//                 _uiState.update { it.copy(errorMessage = "Selecciona un ítem y una cantidad válida.", isLoading = false) }
//                 return@launch
//             }

//             try {
//                 val nuevaCantidadItem = if (_uiState.value.tipoMovimiento == "Ingreso") {
//                     (selectedItem.cantidad ?: 0) + cantidad
//                 } else {
//                     val stockActual = selectedItem.cantidad ?: 0
//                     if (cantidad > stockActual) {
//                         _uiState.update { it.copy(errorMessage = "La cantidad de salida excede el stock disponible.", isLoading = false) }
//                         return@launch
//                     }
//                     stockActual - cantidad
//                 }

//                 val itemToUpdate = ItemApiModel(
//                     id = selectedItem.id,
//                     nombre = selectedItem.nombre,
//                     descripcion = selectedItem.descripcion,
//                     imagenUrl = selectedItem.imagenUrl,
//                     cantidad = nuevaCantidadItem,
//                     usuarioId = selectedItem.usuarioId
//                 )
//                 itemsRepository.actualizarItem(itemToUpdate)

//                 val detalleItemApiModel = DetalleItemApiModel(
//                     cantidad = if (_uiState.value.tipoMovimiento == "Ingreso") cantidad else -cantidad,
//                     itemId = selectedItem.id!!
//                 )
//                 detalleItemsRepository.crearDetalleItem(detalleItemApiModel)

//                 _uiState.update { it.copy(isLoading = false) }
//                 onSuccess()
//             } catch (e: Exception) {
//                 _uiState.update { it.copy(isLoading = false, errorMessage = "Error al registrar movimiento: ${e.message}") }
//             }
//         }
//     }

//     private fun ItemApiModel.toItemDomain(): Item {
//         return Item(
//             id = this.id,
//             nombre = this.nombre,
//             descripcion = this.descripcion,
//             imagenUrl = this.imagenUrl,
//             cantidad = this.cantidad,
//             usuarioId = this.usuarioId
//         )
//     }
// }