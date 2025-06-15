package pe.cibertec.gestorgo.features.inventario.ui.historial

import pe.cibertec.gestorgo.features.inventario.domain.model.HistorialItem

data class HistorialItemUIState(
    val listaItems: List<HistorialItem> = emptyList(),
)
