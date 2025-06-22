import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

object Fecha {
    fun formatearFecha(fechaString: String?): String {
        if (fechaString.isNullOrBlank()) return "Fecha no disponible"
        return try {
            val zonaLima = ZoneId.of("America/Lima")
            val dateTime = OffsetDateTime.parse(fechaString)
            val dateTimeLima = dateTime.atZoneSameInstant(zonaLima)
            val outputFormatter = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy, hh:mm a", Locale("es"))
            dateTimeLima.format(outputFormatter)
        } catch (e: Exception) {
            "Formato inválido: ${e.message}"
        }
    }
}
