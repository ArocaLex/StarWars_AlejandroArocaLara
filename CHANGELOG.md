# Historial de Cambios (Changelog)

Aquí voy apuntando todo lo que le voy haciendo a la app para no perderme.

## [v2.1.0] -(Versión Actual)
**"Arreglo bugs y crashes de Hilt y Room"**


### Arreglos (Bug Fixes)
* **CRASH DE HILT SOLUCIONADO:** La app se cerraba con un error `IllegalStateException`. 
* **Navegación:** Corregido un error al pasar el objeto `planet` entre pantallas. Antes me daba error de tipos, tuve que especificar explícitamente el tipo en la lambda de navegación.
* **Guardado:** Cambié la función `savePlanet` a `suspend`. Antes usaba un callback dentro de un `viewModelScope` que no funcionaba bien. Ahora devuelve un `BaseResult` limpio y la UI espera a que termine.

### Novedades
* **Previews Avanzados:** He creado unos `@Preview` super chulos para probar la app en Modo Oscuro y con letra gigante (Accesibilidad) sin tener que ejecutar el emulador todo el rato.
* **Checkbox de Colonizado:** El campo `isColonized` ahora es un checkbox de verdad en lugar de texto.
* **TopBar Dinámica:** La barra de arriba ahora cambia el título y los botones (como el de guardar o volver atrás) dependiendo de la pantalla gracias a un estado compartido en el Main.

---

## [v2.0.0] 
**"Migracion a Base de Datos (Room)"**

Eliminada la lista estática (la que se borraba al cerrar la app) y añadimos persistencia real de datos.

* **Room:** Implementada la base de datos local. Ahora los planetas se guardan para siempre.
* **Repository:** Creé una clase repositorio para separar la lógica de datos del ViewModel.
* **Inyección de Dependencias (Hilt):** Para no tener que instanciar todo a mano.
* **Navigation Drawer:** Añadí el menú lateral (hamburguesa) para poder ir a "Listado" y "Acerca de".
* **ViewModel:** Cambiar casi todo el ViewModel para usar `Flow` y recoger los datos de la base de datos en tiempo real.

---
## [v1.5.0] 
**"Mejorando la navegación y el formulario"**

Después de las vacaciones de navidad me puse a mejorar cómo se mueve el usuario por la app.

* **Formulario completo:** He añadido Gravedad, Terreno, Población y Diámetro.
---
## [v1.0.0] 
**"Primera version funcional para la 3º entrega"**

Primera versión funcional de la práctica.

* **Listado:** Una `LazyColumn` simple que muestra planetas hardcodeados (Tatooine, Alderaan...).
* **Detalle:** Al pulsar en un planeta navega a una pantalla de detalle muy básica.
* **UI:** Uso de `Scaffold` básico de Material 3.
* **Modelo de datos:** Clase `Planet` sencilla con Strings.