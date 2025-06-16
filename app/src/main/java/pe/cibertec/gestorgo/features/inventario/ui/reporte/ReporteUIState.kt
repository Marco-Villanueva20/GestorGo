package pe.cibertec.gestorgo.features.inventario.ui.reporte

import pe.cibertec.gestorgo.features.inventario.domain.model.Reporte

data class ReporteUIState(
    val listaItems: List<Reporte> = emptyList()
)