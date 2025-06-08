package pe.cibertec.gestorgo.core

fun isValidEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun isValidPhone(telefono: String): Boolean {
    return telefono.length in 7..9 && telefono.all { it.isDigit() }
}

fun isValidDni(dni: String): Boolean {
    return dni.length == 8 && dni.all { it.isDigit() }
}