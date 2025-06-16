package pe.cibertec.gestorgo.features.inventario.ui.historial.crear

import androidx.lifecycle.ViewModel
import pe.cibertec.gestorgo.features.inventario.domain.repository.DetalleItemsRepository
import javax.inject.Inject

class CrearDetalleViewModel @Inject constructor(private val repository: DetalleItemsRepository) :
    ViewModel() {

}