# Actualización: Soporte de Imágenes en Electrodomésticos

## Cambios Realizados

Se ha actualizado la aplicación Android para soportar la carga y visualización de imágenes en los electrodomésticos, siguiendo la nueva API del servidor.

### 1. **Modelos de Datos** (`ComercializadoraModels.kt`)
- ✅ Agregado campo `imagenUrl: String?` a `ElectrodomesticoResponse`
- ✅ El campo es nullable para compatibilidad con datos existentes

### 2. **API Service** (`ComercializadoraApiService.kt`)
- ✅ Cambiado endpoint de creación de electrodomésticos a **multipart/form-data**
- ✅ Ahora acepta:
  - `codigo`: RequestBody
  - `nombre`: RequestBody
  - `precioVenta`: RequestBody
  - `imagen`: MultipartBody.Part (archivo de imagen)
- ✅ Agregado endpoint para obtener imágenes: `GET /electrodomesticos/imagen/{nombreArchivo}`

### 3. **ViewModel** (`ElectrodomesticoViewModel.kt`)
- ✅ Actualizado método `crearElectrodomestico()` para recibir un `File` opcional
- ✅ Construcción automática de multipart request con los campos necesarios
- ✅ Soporte para imágenes en formatos: PNG, JPEG, JPG, GIF

### 4. **Pantalla de Creación** (`CrearElectrodomesticoScreen.kt`)
- ✅ **Selector de imagen**: Botón para seleccionar imagen de la galería
- ✅ **Previsualización**: Muestra la imagen seleccionada antes de guardar
- ✅ **Validación**: Obliga a seleccionar una imagen antes de crear el producto
- ✅ **Feedback visual**: 
  - Preview con borde verde cuando hay imagen seleccionada
  - Muestra el nombre del archivo seleccionado
  - Botón para eliminar la imagen seleccionada
- ✅ **Conversión automática**: Convierte Uri a File para enviar al servidor

### 5. **Pantalla de Listado** (`ElectrodomesticosScreen.kt`)
- ✅ **Visualización de imágenes**: Muestra la imagen del producto en cards
- ✅ **Carga desde URL**: Usa Coil para cargar imágenes desde el servidor
- ✅ **Fallback**: Muestra icono de electrodoméstico si no hay imagen
- ✅ **Diseño mejorado**: Cards más grandes (80x80dp) con imagen en esquina redondeada

### 6. **Pantalla de Facturación** (`FacturarScreen.kt`)
- ✅ **Diálogo de selección mejorado**: Muestra imágenes de productos en el selector
- ✅ **Preview de 64x64dp**: Imágenes más grandes para mejor visualización
- ✅ **Consistencia visual**: Mismo diseño que la pantalla de listado

### 7. **Permisos** (`AndroidManifest.xml`)
- ✅ Agregado `READ_EXTERNAL_STORAGE` (para Android <= 12)
- ✅ Agregado `READ_MEDIA_IMAGES` (para Android >= 13)

### 8. **Configuración** (`RetrofitClient.kt`)
- ✅ Expuesta constante `BASE_URL_COMERCIALIZADORA` para construir URLs de imágenes
- ✅ Formato: `http://192.168.100.50:8080/WS_JAVA_REST_Comercializadora`

## Cómo Usar

### Crear un Nuevo Producto con Imagen

1. **Navegar a "Nuevo Producto"** desde el menú principal
2. **Completar los campos**:
   - Código del producto
   - Nombre del producto
   - Precio de venta
3. **Seleccionar imagen**:
   - Tocar el área gris con el icono de cámara
   - Seleccionar imagen de la galería
   - Verificar la previsualización
4. **Guardar**: El producto se crea con la imagen en el servidor

### Visualizar Productos

- **Pantalla de Productos**: Muestra todas las imágenes automáticamente
- **Selector en Facturación**: Las imágenes aparecen en el diálogo de selección

## Detalles Técnicos

### Estructura de URLs de Imágenes

Las imágenes se sirven desde:
```
GET http://192.168.100.50:8080/WS_JAVA_REST_Comercializadora/api/electrodomesticos/imagen/{nombreArchivo}
```

El campo `imagenUrl` en la respuesta contiene solo el path relativo:
```json
{
  "id": 1,
  "codigo": "E001",
  "nombre": "Refrigeradora Samsung",
  "precioVenta": 1200.00,
  "imagenUrl": "/api/electrodomesticos/imagen/uuid-aleatorio.jpg"
}
```

La app construye la URL completa automáticamente:
```kotlin
val imageUrl = "${RetrofitClient.BASE_URL_COMERCIALIZADORA}${electrodomestico.imagenUrl}"
```

### Librerías Utilizadas

- **Coil 2.5.0**: Carga de imágenes desde URLs
- **OkHttp MultipartBody**: Construcción de requests multipart
- **Activity Result API**: Selección de imágenes de la galería

### Formatos Soportados

- PNG
- JPEG/JPG
- GIF

### Límites y Consideraciones

- ✅ Las imágenes se guardan en `uploads/imagenes/electrodomesticos/` en el servidor
- ✅ El servidor genera nombres únicos (UUID) para evitar colisiones
- ✅ La app cachea las imágenes automáticamente (Coil)
- ⚠️ **Importante**: Asegúrate de que la URL del servidor sea accesible desde el dispositivo

## Configuración del Servidor

Verifica que en `RetrofitClient.kt` la URL sea correcta:

```kotlin
const val BASE_URL_COMERCIALIZADORA = "http://TU_IP_SERVIDOR:8080/WS_JAVA_REST_Comercializadora"
```

Cambia `TU_IP_SERVIDOR` por:
- IP local del servidor (ej: `192.168.100.50`)
- `10.0.2.2` si usas emulador Android
- IP pública si el servidor está en internet

## Pruebas Recomendadas

1. ✅ Crear producto sin imagen (debería fallar con validación)
2. ✅ Crear producto con imagen PNG
3. ✅ Crear producto con imagen JPEG
4. ✅ Verificar que la imagen se muestre en el listado
5. ✅ Verificar que la imagen se muestre en el selector de facturación
6. ✅ Probar con imágenes de diferentes tamaños
7. ✅ Verificar comportamiento sin conexión a internet

## Troubleshooting

### La imagen no se carga
- Verifica que la URL del servidor sea correcta
- Verifica que el servidor esté ejecutándose
- Verifica los permisos en AndroidManifest.xml
- Revisa los logs en Logcat para errores de Coil

### Error al seleccionar imagen
- Verifica que los permisos estén otorgados en la configuración del dispositivo
- En Android 13+, asegúrate de tener `READ_MEDIA_IMAGES`

### Error al crear producto
- Verifica que el archivo de imagen exista
- Verifica que el servidor acepte multipart/form-data
- Revisa los logs del servidor para errores de validación

## Próximas Mejoras Sugeridas

- [ ] Opción de tomar foto con la cámara
- [ ] Recorte/edición de imagen antes de subir
- [ ] Compresión de imagen para reducir tamaño
- [ ] Caché personalizado de imágenes
- [ ] Placeholder mientras carga la imagen
- [ ] Animaciones de carga
