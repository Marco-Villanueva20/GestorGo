import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object Fecha {
    fun formatearFecha(fechaString: String?): String {
        if (fechaString.isNullOrBlank()) return "Fecha no disponible"
        return try {
            val dateTime = OffsetDateTime.parse(fechaString, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
            val outputFormatter = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy, hh:mm a", Locale("es"))
            dateTime.format(outputFormatter)
        } catch (e: Exception) {
            "Formato inválido: ${e.message}"
        }
    }
}
