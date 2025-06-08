package pe.cibertec.gestorgo.core

import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter


fun ahoraEnPeruComoString(): String {
    val limaZone = ZoneId.of("America/Lima")
    val ahoraEnLima = ZonedDateTime.ofInstant(Instant.now(), limaZone)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX") // Ej: 2025-05-19T17:48:12-05:00
    return ahoraEnLima.format(formatter)
}