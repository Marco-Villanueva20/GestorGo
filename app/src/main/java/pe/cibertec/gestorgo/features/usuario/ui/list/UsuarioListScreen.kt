package pe.cibertec.gestorgo.features.usuario.ui.list

// pe.cibertec.gestorgo.features.usuarios.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import pe.cibertec.gestorgo.features.usuario.data.model.Usuario


@Composable
fun UsuarioScreen(
    viewModel: UsuarioViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

//    val pullRefreshState = rememberPullToRefreshState(
//        refreshing = uiState.isLoading,
//        onRefresh = { viewModel.listarUsuarios() }
//    )

    Scaffold(
        topBar = {
            // Puedes añadir un TopAppBar si lo deseas
            // TopAppBar(title = { Text("Lista de Usuarios") })
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                //.pullToRefresh(pullRefreshState) // Habilitar pull-to-refresh
        ) {
            if (uiState.isLoading && uiState.usuarios.isEmpty()) {
                // Muestra un indicador de carga solo si la lista está vacía
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (uiState.errorMessage != null) {
                // Muestra mensaje de error
                Text(
                    text = uiState.errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (uiState.usuarios.isEmpty()) {
                // Mensaje si no hay usuarios (y no está cargando ni hay error)
                Text(
                    text = "No hay usuarios registrados.",
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.Gray
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(uiState.usuarios, key = { it.id ?: it.email }) { usuario ->
                        TarjetaUsuario(usuario = usuario)
                    }
                }
            }

        }
    }
}

@Composable
fun TarjetaUsuario(usuario: Usuario) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Icono de persona
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Icono de Usuario",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    // Nombres y Apellidos
                    Text(
                        text = "${usuario.nombres} ${usuario.apellidos}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    // Email
                    Text(
                        text = usuario.email,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    // DNI y Rol
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "DNI: ${usuario.dni}",
                            fontSize = 14.sp,
                            color = Color.DarkGray
                        )
                        Text(
                            text = "Rol: ${usuario.rol}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                    // Fecha de creación (si existe)
                    usuario.creado?.let {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Creado: $it",
                            fontSize = 12.sp,
                            color = Color.LightGray
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UsuarioScreenPreview() {
    MaterialTheme {
        Column {
            TarjetaUsuario(
                usuario = Usuario(
                    id = "1",
                    nombres = "Juan",
                    apellidos = "Pérez Gómez",
                    email = "juan.perez@example.com",
                    dni = "12345678",
                    rol = "ADMIN",
                    creado = "2024-01-15T10:30:00Z"
                )
            )
            TarjetaUsuario(
                usuario = Usuario(
                    id = "2",
                    nombres = "María",
                    apellidos = "López Diaz",
                    email = "maria.lopez@example.com",
                    dni = "87654321",
                    rol = "CLIENTE",
                    creado = "2024-03-20T14:00:00Z"
                )
            )
        }
    }
}