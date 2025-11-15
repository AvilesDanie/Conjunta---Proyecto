# GUÍA PASO A PASO: DIAGRAMA ER EN POWERDESIGNER
## Sistema Comercializadora de Electrodomésticos - Item 4

---

## 📌 PASO 0: PREPARACIÓN

### Abrir PowerDesigner
1. Ejecutar **Sybase PowerDesigner** desde el menú de Windows
2. Si aparece ventana de inicio, cerrarla (X)

---

## 📌 PASO 1: CREAR NUEVO MODELO

### 1.1 Crear Physical Data Model
```
File (menú superior) 
  → New Model 
  → Physical Data Model
```

### 1.2 Configurar Opciones del Modelo
En la ventana **"New Physical Data Model"**:

| Campo | Valor |
|-------|-------|
| **Model name:** | `SistemaComercializadora` |
| **DBMS:** | Seleccionar `MySQL 8.0` (o `MySQL 5.0` si no aparece 8.0) |
| **Model Code:** | `SistemaComercializadora` (automático) |

Click en **OK**

**Resultado:** Se abrirá un canvas (área de trabajo) vacío en blanco.

---

## 📌 PASO 2: CREAR LA PRIMERA TABLA (ELECTRODOMESTICO)

### 2.1 Activar Herramienta de Tabla
En el **Toolbar (barra de herramientas izquierda)**:
- Click en el ícono **"Table"** (parece una tabla pequeña 📊)
- O presiona tecla **T**

### 2.2 Dibujar la Tabla
- Click en el canvas (área de trabajo)
- Aparece una tabla nueva con nombre "Table_1"

### 2.3 Renombrar la Tabla
- **Double-click** sobre la tabla recién creada
- Se abre ventana **"Table Properties"**

#### Pestaña **"General"**:
| Campo | Valor |
|-------|-------|
| **Name:** | `ELECTRODOMESTICO` |
| **Code:** | `ELECTRODOMESTICO` (se llena automático) |
| **Comment:** | `Catálogo de electrodomésticos disponibles para venta` |

**NO cerrar la ventana aún** ⚠️

---

## 📌 PASO 3: AGREGAR COLUMNAS A ELECTRODOMESTICO

### 3.1 Ir a Pestaña "Columns"
- En la misma ventana "Table Properties"
- Click en pestaña **"Columns"** (segunda pestaña)

### 3.2 Agregar Columnas Una por Una

#### Para cada columna:
1. Click en botón **"Insert a Row"** (ícono con flecha verde apuntando a línea)
2. O presiona tecla **Insert** en el teclado
3. Se agrega una fila vacía

#### Llenar la PRIMERA columna (ID):

| Campo | Valor | Dónde marcarlo |
|-------|-------|----------------|
| **Name** | `id_electrodomestico` | Escribir en celda "Name" |
| **Code** | `id_electrodomestico` | Se llena automático |
| **Data Type** | `INT` | Click en celda "Data Type" → elegir de lista |
| **Length** | (vacío) | Dejar vacío para INT |
| **P** (Primary) | ✅ Marcar | Click en checkbox columna "P" |
| **M** (Mandatory) | ✅ Marcar | Click en checkbox columna "M" |
| **Identity** | ✅ Marcar | Click en checkbox columna "Identity" (AUTO_INCREMENT) |

#### Agregar las demás columnas (repetir proceso Insert Row):

**COLUMNA 2:**
- Name: `codigo`
- Data Type: `VARCHAR(20)`
- M: ✅ (obligatorio)
- No marcar P ni Identity

**COLUMNA 3:**
- Name: `nombre`
- Data Type: `VARCHAR(200)`
- M: ✅

**COLUMNA 4:**
- Name: `descripcion`
- Data Type: `TEXT`
- M: ❌ (opcional, NO marcar)

**COLUMNA 5:**
- Name: `precio_venta`
- Data Type: `DECIMAL(10,2)`
  - Al elegir DECIMAL, aparecerá ventana "Precision"
  - Precision: `10`
  - Scale: `2`
  - Click OK
- M: ✅

**COLUMNA 6:**
- Name: `marca`
- Data Type: `VARCHAR(100)`
- M: ✅

**COLUMNA 7:**
- Name: `categoria`
- Data Type: `VARCHAR(50)`
- M: ✅

**COLUMNA 8:**
- Name: `activo`
- Data Type: `BOOLEAN` (si no existe, usar `TINYINT(1)`)
- M: ✅

**COLUMNA 9:**
- Name: `fecha_registro`
- Data Type: `TIMESTAMP`
- M: ✅

### 3.3 Configurar Valores por Defecto

#### Para columna `activo`:
1. En la tabla de columnas, click en la fila `activo`
2. En la parte inferior de la ventana, buscar sección **"Standard Checks"**
3. Marcar checkbox **"Default value"**
4. En el campo de texto que aparece, escribir: `TRUE`

#### Para columna `fecha_registro`:
1. Click en la fila `fecha_registro`
2. Marcar **"Default value"**
3. Escribir: `CURRENT_TIMESTAMP`

---

## 📌 PASO 4: AGREGAR RESTRICCIONES CHECK

### 4.1 Ir a Pestaña "Checks"
- Dentro de "Table Properties" de ELECTRODOMESTICO
- Click en pestaña **"Checks"**

### 4.2 Agregar Primera Restricción (Precio Positivo)
1. Click en botón **"Add a Row"** (ícono +)
2. Se agrega una fila

Llenar:
| Campo | Valor |
|-------|-------|
| **Name** | `CHK_precio_positivo` |
| **Code** | `CHK_precio_positivo` |
| **Server Expression** | `precio_venta > 0` |

3. Click en checkbox de la izquierda para activar el check

### 4.3 Agregar Segunda Restricción (Categoría Válida)
1. Click en **"Add a Row"** nuevamente
2. Llenar:

| Campo | Valor |
|-------|-------|
| **Name** | `CHK_categoria_valida` |
| **Code** | `CHK_categoria_valida` |
| **Server Expression** | `categoria IN ('Refrigeración', 'Lavado', 'Cocina', 'Pequeños Electrodomésticos', 'Otro')` |

3. Activar checkbox

---

## 📌 PASO 5: AGREGAR ÍNDICES

### 5.1 Ir a Pestaña "Indexes"
- Dentro de "Table Properties" de ELECTRODOMESTICO
- Click en pestaña **"Indexes"**

### 5.2 Crear Índice Único en Código
1. Click en **"Add a Row"**
2. Llenar:

| Campo | Valor |
|-------|-------|
| **Name** | `IDX_codigo` |
| **Code** | `IDX_codigo` |
| **Unique** | ✅ Marcar checkbox |

3. En la sección inferior "Index Columns":
   - Click derecho → **"Add Columns"**
   - Seleccionar columna `codigo`
   - Click OK

### 5.3 Crear Índice en Categoría
1. Click en **"Add a Row"**
2. Name: `IDX_categoria`
3. Unique: ❌ NO marcar
4. Add Columns → seleccionar `categoria`

---

## 📌 PASO 6: FINALIZAR PRIMERA TABLA

- Click en **OK** para cerrar "Table Properties"
- La tabla ELECTRODOMESTICO queda creada en el canvas
- Puedes moverla arrastrando con el mouse

---

## 📌 PASO 7: CREAR TABLA CLIENTE (OPCIONAL)

### Repetir Paso 2 (crear tabla):
1. Click en ícono **Table** (o tecla T)
2. Click en el canvas
3. Double-click en la tabla nueva

### Configurar:
**Pestaña General:**
- Name: `CLIENTE`

**Pestaña Columns:**
| Name | Data Type | Length | P | M |
|------|-----------|--------|---|---|
| cedula | VARCHAR | 10 | ✅ | ✅ |
| nombre | VARCHAR | 200 | | ✅ |
| telefono | VARCHAR | 15 | | |
| email | VARCHAR | 100 | | |
| direccion | VARCHAR | 500 | | |
| fecha_registro | TIMESTAMP | | | ✅ |

- fecha_registro → Default value: `CURRENT_TIMESTAMP`

Click **OK**

---

## 📌 PASO 8: CREAR TABLA FACTURA

### Repetir proceso:
1. Table tool → Click en canvas
2. Double-click en tabla

**Pestaña General:**
- Name: `FACTURA`

**Pestaña Columns:**
| Name | Data Type | Length | P | M | Identity | Default |
|------|-----------|--------|---|---|----------|---------|
| id_factura | INT | | ✅ | ✅ | ✅ | |
| numero_factura | VARCHAR | 20 | | ✅ | | |
| cedula_cliente | VARCHAR | 10 | | ✅ | | |
| nombre_cliente | VARCHAR | 200 | | ✅ | | |
| fecha_emision | TIMESTAMP | | | ✅ | | CURRENT_TIMESTAMP |
| forma_pago | VARCHAR | 20 | | ✅ | | |
| subtotal | DECIMAL | 10,2 | | ✅ | | |
| descuento | DECIMAL | 10,2 | | ✅ | | 0.00 |
| iva | DECIMAL | 10,2 | | ✅ | | 0.00 |
| total | DECIMAL | 10,2 | | ✅ | | |
| id_credito_banco | INT | | | | | |
| estado | VARCHAR | 20 | | ✅ | | 'COMPLETADA' |

**Pestaña Checks:**
1. `CHK_forma_pago` → `forma_pago IN ('EFECTIVO', 'CREDITO_DIRECTO')`
2. `CHK_total_positivo` → `total > 0`
3. `CHK_estado_factura` → `estado IN ('COMPLETADA', 'ANULADA')`

**Pestaña Indexes:**
- `IDX_numero_factura` (columna: numero_factura, UNIQUE ✅)
- `IDX_cedula_cliente` (columna: cedula_cliente)
- `IDX_fecha` (columna: fecha_emision)

Click **OK**

---

## 📌 PASO 9: CREAR TABLA DETALLE_FACTURA

### Repetir proceso:
**Pestaña General:**
- Name: `DETALLE_FACTURA`

**Pestaña Columns:**
| Name | Data Type | Length | P | M | Identity |
|------|-----------|--------|---|---|----------|
| id_detalle | INT | | ✅ | ✅ | ✅ |
| id_factura | INT | | | ✅ | |
| id_electrodomestico | INT | | | ✅ | |
| cantidad | INT | | | ✅ | |
| precio_unitario | DECIMAL | 10,2 | | ✅ | |
| subtotal_linea | DECIMAL | 10,2 | | ✅ | |

**Pestaña Checks:**
- `CHK_cantidad_positiva` → `cantidad > 0`

Click **OK** (no cerrar aún, vamos a crear las relaciones)

---

## 📌 PASO 10: CREAR RELACIONES (FOREIGN KEYS)

### 10.1 Relación: FACTURA → DETALLE_FACTURA

1. En el **Toolbar**, click en ícono **"Reference"** (flecha con línea)
2. O presiona tecla **R**
3. El cursor cambia a una cruz
4. Click en la tabla **FACTURA** (tabla padre)
5. **Mantén presionado** el botón del mouse
6. Arrastra hasta la tabla **DETALLE_FACTURA** (tabla hija)
7. Suelta el botón
8. Aparece una línea conectando ambas tablas

### 10.2 Configurar la Relación
- **Double-click** en la línea de relación
- Se abre ventana **"Reference Properties"**

**Pestaña General:**
| Campo | Valor |
|-------|-------|
| **Name** | `FK_DETALLE_FACTURA` |
| **Code** | `FK_DETALLE_FACTURA` |

**Pestaña "Joins":**
- Debe mostrar automáticamente:
  - Parent: `FACTURA.id_factura`
  - Child: `DETALLE_FACTURA.id_factura` (se crea automáticamente si no existía)

**Pestaña "Referential Integrity":**
| Campo | Valor |
|-------|-------|
| **Update** | No Action (dejar por defecto) |
| **Delete** | **CASCADE** (elegir de lista desplegable) |

Esto significa: si se borra una factura, se borran sus detalles automáticamente.

Click **OK**

### 10.3 Relación: ELECTRODOMESTICO → DETALLE_FACTURA

1. Click en ícono **Reference** (o tecla R)
2. Click en **ELECTRODOMESTICO**
3. Arrastra a **DETALLE_FACTURA**
4. Suelta
5. Double-click en la línea de relación

**Configurar:**
- Name: `FK_DETALLE_ELECTRODOMESTICO`
- Joins: debe mostrar `ELECTRODOMESTICO.id_electrodomestico` → `DETALLE_FACTURA.id_electrodomestico`
- Referential Integrity → Delete: **RESTRICT**

Esto significa: NO se puede borrar un electrodoméstico que tenga ventas.

Click **OK**

### 10.4 Relación: CLIENTE → FACTURA (Opcional)

1. Click en ícono **Reference**
2. Click en **CLIENTE**
3. Arrastra a **FACTURA**
4. Suelta
5. Double-click en línea

**Configurar:**
- Name: `FK_FACTURA_CLIENTE`
- Joins: `CLIENTE.cedula` → `FACTURA.cedula_cliente`
- Delete: **RESTRICT**

Click **OK**

---

## 📌 PASO 11: ORGANIZAR EL DIAGRAMA

### 11.1 Mover Tablas
- Click y arrastra cada tabla para organizarlas
- Sugerencia de disposición:

```
        CLIENTE
           |
           ↓
        FACTURA  ←----- ELECTRODOMESTICO
           |                    |
           ↓                    ↓
     DETALLE_FACTURA ←----------┘
```

### 11.2 Ajustar Vista
- Menú **View** → **Display Preferences**
- Marcar todas las opciones:
  - ✅ Columns
  - ✅ Keys
  - ✅ Indexes
  - ✅ Triggers
  - ✅ Comments
- Click **OK**

### 11.3 Zoom
- Menú **View** → **Zoom**
- Elegir **Fit in Page** para ver todo el diagrama

---

## 📌 PASO 12: GUARDAR EL MODELO

### 12.1 Guardar Archivo PDM
```
File → Save As
```

| Campo | Valor |
|-------|-------|
| **File name** | `SistemaComercializadora.pdm` |
| **Save in** | `C:\Users\Lenovo\Desktop\Evaluacion Arquitectura 1p\04 DOCUMENTACION\` |

Click **Guardar**

---

## 📌 PASO 13: GENERAR SCRIPT SQL

### 13.1 Iniciar Generación
```
Database (menú superior) → Generate Database...
```

### 13.2 Configurar Opciones
En ventana **"Database Generation"**:

**Pestaña "General":**
| Campo | Valor |
|-------|-------|
| **Title** | `Script Comercializadora` |
| **Directory** | `C:\Users\Lenovo\Desktop\Evaluacion Arquitectura 1p\03 BDD\` |
| **File name** | `Comercializadora_Generated.sql` |

**Pestaña "Options":**
Marcar:
- ✅ **Create table**
- ✅ **Primary key**
- ✅ **Alternate key**
- ✅ **Foreign key**
- ✅ **Index**
- ✅ **Check**

NO marcar:
- ❌ Drop statements (para no borrar datos existentes)

**Pestaña "Selection":**
- Asegurar que todas las tablas estén seleccionadas (checkboxes marcados)

Click **OK**

### 13.3 Resultado
- Se genera el archivo SQL en la carpeta especificada
- Aparece mensaje "Generation complete"
- Click **Close**

---

## 📌 PASO 14: EXPORTAR DIAGRAMA COMO IMAGEN

### 14.1 Ajustar Vista
1. Menú **View** → **Zoom** → **Fit in Page**
2. Asegurar que todo el diagrama sea visible

### 14.2 Exportar
```
File → Export → Image...
```

**Configurar:**
| Campo | Valor |
|-------|-------|
| **File name** | `Diagrama_ER_Comercializadora.png` |
| **Save in** | `C:\Users\Lenovo\Desktop\Evaluacion Arquitectura 1p\04 DOCUMENTACION\` |
| **Format** | PNG |
| **Resolution** | 300 DPI (alta calidad) |

Marcar:
- ✅ **Transparent background** (opcional)
- ✅ **Selected objects only** (si solo quieres algunas tablas)

Click **Guardar**

---

## 📌 PASO 15: VERIFICACIÓN FINAL

### Checklist:
- [ ] ✅ 3 o 4 tablas creadas (ELECTRODOMESTICO, FACTURA, DETALLE_FACTURA, CLIENTE opcional)
- [ ] ✅ Todas las columnas con tipos de datos correctos
- [ ] ✅ Primary Keys marcadas (columna P)
- [ ] ✅ Columnas obligatorias marcadas (columna M)
- [ ] ✅ IDs con Identity (AUTO_INCREMENT)
- [ ] ✅ 3 relaciones dibujadas con flechas
- [ ] ✅ Restricciones CHECK agregadas
- [ ] ✅ Índices creados
- [ ] ✅ Valores por defecto configurados
- [ ] ✅ Archivo .pdm guardado
- [ ] ✅ Script SQL generado
- [ ] ✅ Imagen PNG exportada

---

## 🆘 SOLUCIÓN DE PROBLEMAS COMUNES

### Problema 1: No aparece "Identity" en columnas
**Solución:** 
- Verificar que DBMS sea MySQL (no Oracle ni SQL Server)
- Identity solo aparece en columnas tipo INT

### Problema 2: No puedo crear relación
**Solución:**
- La columna en tabla hija debe tener el MISMO tipo de dato que la PK de tabla padre
- Ejemplo: si `id_factura` es INT en FACTURA, debe ser INT en DETALLE_FACTURA

### Problema 3: La relación crea columna duplicada
**Solución:**
- Si ya existe la columna FK en la tabla hija (ej: `id_factura`), PowerDesigner la reconoce
- No crear la columna manualmente antes de la relación, dejar que PowerDesigner la cree

### Problema 4: No puedo poner DECIMAL(10,2)
**Solución:**
- Elegir tipo DECIMAL
- Aparecerá ventana pidiendo Precision y Scale
- Precision = 10, Scale = 2

### Problema 5: El script generado tiene errores
**Solución:**
- Verificar que todas las tablas padre existan antes que las hijas
- PowerDesigner ordena automáticamente, pero verificar con:
  - Database → Check Model (F4)
  - Corregir errores mostrados

---

## 📚 RESUMEN DE TECLAS RÁPIDAS

| Tecla | Acción |
|-------|--------|
| **T** | Crear nueva tabla (Table tool) |
| **R** | Crear relación (Reference tool) |
| **F4** | Check Model (validar modelo) |
| **Ctrl+S** | Guardar |
| **Delete** | Borrar objeto seleccionado |
| **Ctrl+Z** | Deshacer |
| **Ctrl+Y** | Rehacer |

---

## ✅ ENTREGA FINAL

Debes tener listos estos archivos:
1. `SistemaComercializadora.pdm` → Archivo de PowerDesigner
2. `Comercializadora_Generated.sql` → Script SQL generado
3. `Diagrama_ER_Comercializadora.png` → Imagen del diagrama

¡Listo para presentar el Item 4! 🎉
