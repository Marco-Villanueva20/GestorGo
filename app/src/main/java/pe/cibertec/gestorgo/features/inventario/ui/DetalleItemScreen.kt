package pe.cibertec.gestorgo.features.inventario.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun DetalleItemScreen(){
    var tipoMovimiento by remember { mutableStateOf("Ingreso") }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        listOf("Ingreso", "Salida").forEach { tipo ->
            FilterChip(
                selected = tipoMovimiento == tipo,
                onClick = { tipoMovimiento = tipo },
                label = { Text(tipo) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetalleItemScreenPreview() {
    DetalleItemScreen()
}