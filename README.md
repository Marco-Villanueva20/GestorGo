# GestorGo

GestorGo es una aplicación de gestión diseñada para ayudarte a organizar tus tareas y proyectos de manera eficiente. La aplicación se integra con Supabase para ofrecer una experiencia de usuario fluida y en tiempo real, permitiendo la gestión de datos, autenticación de usuarios y almacenamiento de archivos. Es una herramienta versátil que puede adaptarse tanto para uso personal como para la gestión de pequeños equipos.

## Características Principales

GestorGo ofrece un conjunto de funcionalidades pensadas para una gestión integral:

*   **Autenticación de Usuarios:** Sistema seguro de registro e inicio de sesión gestionado por Supabase Auth.
*   **Gestión de Datos:** Creación, lectura, actualización y eliminación (CRUD) de información relevante para tus proyectos, utilizando Supabase PostgREST.
*   **Actualizaciones en Tiempo Real:** Sincronización instantánea de datos entre dispositivos gracias a Supabase Realtime.
*   **Almacenamiento de Archivos:** Posibilidad de subir y gestionar archivos asociados a tus tareas o proyectos mediante Supabase Storage.
*   **Visualización de Datos:** Gráficos y representaciones visuales para un mejor seguimiento del progreso y análisis de datos.
*   **Generación de Informes:** Capacidad para exportar datos o generar informes, posiblemente en formato Excel (utilizando Apache POI).

## Stack Tecnológico

La aplicación está construida utilizando tecnologías modernas y robustas:

*   **Lenguaje de Programación:** Kotlin
*   **Interfaz de Usuario (UI):** Jetpack Compose
*   **Inyección de Dependencias:** Hilt
*   **Backend (BaaS):** Supabase
    *   Autenticación: Supabase Auth
    *   Base de Datos: Supabase PostgREST
    *   Tiempo Real: Supabase Realtime
    *   Almacenamiento: Supabase Storage
*   **Carga de Imágenes:** Coil
*   **Manejo de Archivos Excel:** Apache POI
*   **Cliente HTTP:** Ktor (utilizado por las librerías de Supabase)

## Configuración e Instalación

Para ejecutar GestorGo en tu entorno local, sigue estos pasos:

1.  **Clonar el Repositorio:**
    ```bash
    git clone https://github.com/Marco-Villanueva20/GestorGo.git
    cd GestorGo
    ```

2.  **Android Studio:**
    *   Asegúrate de tener instalada una versión reciente de Android Studio.
    *   Abre el proyecto clonado con Android Studio.

3.  **Credenciales de Supabase:**
    *   La configuración de Supabase se gestiona a través de un archivo `secrets.properties` en el directorio raíz del proyecto.
    *   Crea un archivo `secrets.properties` (o renombra `local.defaults.properties` si existe uno de ejemplo) y añade tus credenciales de Supabase:
        ```properties
        SUPABASE_URL=TU_URL_DE_SUPABASE
        SUPABASE_ANON_KEY=TU_ANON_KEY_DE_SUPABASE
        ```
    *   *(Nota: Estas claves son ejemplos. Debes usar las que correspondan a tu proyecto en Supabase. Consulta la sección `secrets` en el archivo `app/build.gradle.kts` para más detalles sobre cómo se gestionan los secretos).*

4.  **Construir y Ejecutar:**
    *   Android Studio debería sincronizar el proyecto automáticamente. Si no, realiza una sincronización manual de Gradle.
    *   Selecciona un emulador o dispositivo físico y ejecuta la aplicación.

## Contribuciones

Las contribuciones son bienvenidas. Si deseas mejorar GestorGo, por favor considera abrir un *issue* para discutir los cambios propuestos o enviar un *pull request*.
