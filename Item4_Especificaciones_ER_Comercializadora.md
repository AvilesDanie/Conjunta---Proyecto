# ITEM 4: DIAGRAMA ENTIDAD-RELACIÓN - SISTEMA COMERCIALIZADORA DE ELECTRODOMÉSTICOS
## Especificaciones para PowerDesigner (1.0 punto)

---

## 📐 TIPO DE DIAGRAMA EN POWERDESIGNER

**Crear:** Physical Data Model (PDM) o Conceptual Data Model (CDM)
- File → New Model → Physical Data Model
- DBMS: MySQL 8.0 (o el que uses)

---

## 🗄️ ENTIDADES DEL SISTEMA

### **1. ELECTRODOMESTICO**

**Descripción:** Catálogo de productos disponibles para la venta (NO es inventario, solo listado).

**Atributos:**

| Nombre Campo | Tipo Dato | Longitud | Restricciones | Descripción |
|--------------|-----------|----------|---------------|-------------|
| **id_electrodomestico** | INT | - | **PK, AUTO_INCREMENT** | Identificador único |
| codigo | VARCHAR | 20 | NOT NULL, UNIQUE | Código del producto (ej: "REF-001") |
| nombre | VARCHAR | 200 | NOT NULL | Nombre descriptivo |
| descripcion | TEXT | - | NULL | Características del producto |
| precio_venta | DECIMAL | (10,2) | NOT NULL, CHECK > 0 | Precio de venta al público |
| marca | VARCHAR | 100 | NOT NULL | Marca del electrodoméstico |
| categoria | VARCHAR | 50 | NOT NULL | Tipo: Refrigeración, Lavado, Cocina, etc. |
| activo | BOOLEAN | - | DEFAULT TRUE | Estado del producto |
| fecha_registro | TIMESTAMP | - | DEFAULT CURRENT_TIMESTAMP | Fecha de alta |

**Restricciones adicionales:**
- CHECK (precio_venta > 0)
- CHECK (categoria IN ('Refrigeración', 'Lavado', 'Cocina', 'Pequeños Electrodomésticos', 'Otro'))

---

### **2. FACTURA**

**Descripción:** Encabezado de la factura de venta.

**Atributos:**

| Nombre Campo | Tipo Dato | Longitud | Restricciones | Descripción |
|--------------|-----------|----------|---------------|-------------|
| **id_factura** | INT | - | **PK, AUTO_INCREMENT** | Identificador único |
| numero_factura | VARCHAR | 20 | NOT NULL, UNIQUE | Número de factura (001-001-0000001) |
| cedula_cliente | VARCHAR | 10 | NOT NULL | Cédula del comprador |
| nombre_cliente | VARCHAR | 200 | NOT NULL | Nombre del comprador |
| fecha_emision | TIMESTAMP | - | DEFAULT CURRENT_TIMESTAMP | Fecha y hora de emisión |
| forma_pago | VARCHAR | 20 | NOT NULL | 'EFECTIVO' o 'CREDITO_DIRECTO' |
| subtotal | DECIMAL | (10,2) | NOT NULL, CHECK >= 0 | Suma de precios sin descuento |
| descuento | DECIMAL | (10,2) | DEFAULT 0.00 | Descuento aplicado (33% si efectivo) |
| iva | DECIMAL | (10,2) | DEFAULT 0.00 | Impuesto (si aplica) |
| total | DECIMAL | (10,2) | NOT NULL, CHECK > 0 | Total a pagar |
| id_credito_banco | INT | - | NULL | ID del crédito otorgado (si forma_pago=CREDITO) |
| estado | VARCHAR | 20 | DEFAULT 'COMPLETADA' | COMPLETADA, ANULADA |

**Restricciones adicionales:**
- CHECK (forma_pago IN ('EFECTIVO', 'CREDITO_DIRECTO'))
- CHECK (estado IN ('COMPLETADA', 'ANULADA'))
- Si forma_pago='EFECTIVO' → descuento = subtotal * 0.33
- Si forma_pago='CREDITO_DIRECTO' → id_credito_banco NOT NULL

---

### **3. DETALLE_FACTURA**

**Descripción:** Líneas de productos de cada factura.

**Atributos:**

| Nombre Campo | Tipo Dato | Longitud | Restricciones | Descripción |
|--------------|-----------|----------|---------------|-------------|
| **id_detalle** | INT | - | **PK, AUTO_INCREMENT** | Identificador único |
| **id_factura** | INT | - | **FK → FACTURA, NOT NULL** | Referencia a la factura |
| **id_electrodomestico** | INT | - | **FK → ELECTRODOMESTICO, NOT NULL** | Referencia al producto |
| cantidad | INT | - | NOT NULL, CHECK > 0 | Cantidad vendida (usualmente 1) |
| precio_unitario | DECIMAL | (10,2) | NOT NULL, CHECK >= 0 | Precio al momento de la venta |
| subtotal_linea | DECIMAL | (10,2) | NOT NULL, CHECK >= 0 | cantidad * precio_unitario |

**Restricciones adicionales:**
- CHECK (cantidad > 0)
- subtotal_linea = cantidad * precio_unitario

---

### **4. CLIENTE (Opcional - Para almacenar histórico)**

**Descripción:** Registro de clientes que han comprado (opcional, no obligatorio según documento).

**Atributos:**

| Nombre Campo | Tipo Dato | Longitud | Restricciones | Descripción |
|--------------|-----------|----------|---------------|-------------|
| **cedula** | VARCHAR | 10 | **PK** | Cédula de identidad |
| nombre | VARCHAR | 200 | NOT NULL | Nombre completo |
| telefono | VARCHAR | 15 | NULL | Teléfono de contacto |
| email | VARCHAR | 100 | NULL | Correo electrónico |
| direccion | VARCHAR | 500 | NULL | Dirección de entrega |
| fecha_registro | TIMESTAMP | - | DEFAULT CURRENT_TIMESTAMP | Primera compra |

**Nota:** Esta tabla es opcional. Si se incluye, FACTURA.cedula_cliente sería FK a CLIENTE.cedula

---

## 🔗 RELACIONES ENTRE ENTIDADES

### **Relación 1: FACTURA ←→ DETALLE_FACTURA**

- **Tipo:** 1 a N (One to Many)
- **Cardinalidad:** 
  - Una FACTURA tiene uno o más DETALLE_FACTURA (1,N)
  - Un DETALLE_FACTURA pertenece a una sola FACTURA (1,1)
- **Integridad Referencial:**
  - FK: DETALLE_FACTURA.id_factura → FACTURA.id_factura
  - ON DELETE: CASCADE (si se elimina factura, se eliminan detalles)
- **Etiqueta en diagrama:** "contiene" o "tiene"

---

### **Relación 2: ELECTRODOMESTICO ←→ DETALLE_FACTURA**

- **Tipo:** 1 a N (One to Many)
- **Cardinalidad:**
  - Un ELECTRODOMESTICO puede estar en muchos DETALLE_FACTURA (1,N)
  - Un DETALLE_FACTURA referencia a un solo ELECTRODOMESTICO (1,1)
- **Integridad Referencial:**
  - FK: DETALLE_FACTURA.id_electrodomestico → ELECTRODOMESTICO.id_electrodomestico
  - ON DELETE: RESTRICT (no permitir eliminar electrodoméstico con ventas)
- **Etiqueta en diagrama:** "se vende en"

---

### **Relación 3 (Opcional): CLIENTE ←→ FACTURA**

- **Tipo:** 1 a N (One to Many)
- **Cardinalidad:**
  - Un CLIENTE puede tener muchas FACTURA (1,N)
  - Una FACTURA pertenece a un solo CLIENTE (0,1) - opcional porque puede no estar registrado
- **Integridad Referencial:**
  - FK: FACTURA.cedula_cliente → CLIENTE.cedula
  - ON DELETE: RESTRICT
- **Etiqueta en diagrama:** "compra"

---

## 📊 DIAGRAMA CONCEPTUAL (Notación Crow's Foot)

```
┌─────────────────────┐
│   ELECTRODOMESTICO  │
│ ──────────────────  │
│ id_electrodomestico │──┐
│ codigo              │  │
│ nombre              │  │
│ descripcion         │  │ 1
│ precio_venta        │  │
│ marca               │  │
│ categoria           │  │
│ activo              │  │
│ fecha_registro      │  │
└─────────────────────┘  │
                         │
                         │ "se vende en"
                         │
                         │ N
                         │
┌─────────────────────┐  │     ┌──────────────────┐
│      FACTURA        │  │     │ DETALLE_FACTURA  │
│ ───────────────────│  │     │ ──────────────── │
│ id_factura          │──┼────→│ id_detalle       │
│ numero_factura      │  1  N  │ id_factura (FK)  │
│ cedula_cliente      │◄───────│ id_electrodom.(FK)│
│ nombre_cliente      │        │ cantidad         │
│ fecha_emision       │        │ precio_unitario  │
│ forma_pago          │        │ subtotal_linea   │
│ subtotal            │        └──────────────────┘
│ descuento           │
│ iva                 │
│ total               │
│ id_credito_banco    │
│ estado              │
└─────────────────────┘
        △
        │ 0..N
        │ "compra"
        │
┌─────────────────────┐
│      CLIENTE        │ (OPCIONAL)
│ ───────────────────│
│ cedula (PK)         │
│ nombre              │
│ telefono            │
│ email               │
│ direccion           │
│ fecha_registro      │
└─────────────────────┘
```

---

## 🎨 INSTRUCCIONES PASO A PASO EN POWERDESIGNER

### **1. Crear Nuevo Modelo:**
- File → New Model → Physical Data Model
- Name: "SistemaComercializadoraElectrodomesticos"
- DBMS: MySQL 8.0 (o tu motor de BD)

### **2. Crear Entidades (Tablas):**

**Para cada entidad:**
1. Click en "Table" tool (icono de tabla en toolbar)
2. Click en canvas para crear
3. Double-click en la tabla → pestaña "General"
4. Name: nombre de la tabla (ej: ELECTRODOMESTICO)
5. Code: mismo nombre (o versión sin espacios)

### **3. Agregar Columnas:**

**Para cada tabla, pestaña "Columns":**
- Click "Add a Row" (icono +)
- Configurar cada columna con los datos de las tablas de arriba
- Marcar "P" (Primary Key) en columnas PK
- Marcar "M" (Mandatory) en columnas NOT NULL
- Marcar "Identity" en columnas AUTO_INCREMENT

**Ejemplo ELECTRODOMESTICO:**
```
Name                    Code                    Data Type    Length  P  M  Identity
id_electrodomestico     id_electrodomestico     INT          -       ✓  ✓  ✓
codigo                  codigo                  VARCHAR      20         ✓
nombre                  nombre                  VARCHAR      200        ✓
descripcion             descripcion             TEXT         -
precio_venta            precio_venta            DECIMAL      10,2       ✓
marca                   marca                   VARCHAR      100        ✓
categoria               categoria               VARCHAR      50         ✓
activo                  activo                  BOOLEAN      -          ✓
fecha_registro          fecha_registro          TIMESTAMP    -          ✓
```

### **4. Crear Relaciones (References):**

**Relación FACTURA → DETALLE_FACTURA:**
1. Click en "Reference" tool (flecha en toolbar)
2. Click en tabla padre (FACTURA)
3. Arrastrar a tabla hija (DETALLE_FACTURA)
4. PowerDesigner crea automáticamente FK
5. Double-click en la relación → pestaña "General"
   - Name: FK_DETALLE_FACTURA
   - Cardinality: One to Many (1,n)
6. Pestaña "Referential Integrity"
   - Update: No Action
   - Delete: CASCADE

**Relación ELECTRODOMESTICO → DETALLE_FACTURA:**
1. Reference tool
2. De ELECTRODOMESTICO a DETALLE_FACTURA
3. Name: FK_DETALLE_ELECTRODOMESTICO
4. Delete: RESTRICT

**Relación CLIENTE → FACTURA (opcional):**
1. Reference tool
2. De CLIENTE a FACTURA
3. Name: FK_FACTURA_CLIENTE
4. Delete: RESTRICT
5. Cardinality: Zero or One to Many (0,n)

### **5. Agregar Restricciones CHECK:**

**Para cada tabla, pestaña "Checks":**
- Click "Add a Row"
- Name: CHK_nombre_descriptivo
- Server Expression: la condición SQL

**Ejemplos:**
- ELECTRODOMESTICO:
  - CHK_precio_positivo: `precio_venta > 0`
  - CHK_categoria_valida: `categoria IN ('Refrigeración', 'Lavado', 'Cocina', 'Pequeños Electrodomésticos', 'Otro')`
  
- FACTURA:
  - CHK_forma_pago: `forma_pago IN ('EFECTIVO', 'CREDITO_DIRECTO')`
  - CHK_total_positivo: `total > 0`

- DETALLE_FACTURA:
  - CHK_cantidad_positiva: `cantidad > 0`

### **6. Agregar Índices (Opcional pero recomendado):**

**Pestaña "Indexes" de cada tabla:**
- ELECTRODOMESTICO:
  - IDX_codigo: columna 'codigo' (UNIQUE)
  - IDX_categoria: columna 'categoria'
  
- FACTURA:
  - IDX_numero_factura: columna 'numero_factura' (UNIQUE)
  - IDX_cedula_cliente: columna 'cedula_cliente'
  - IDX_fecha: columna 'fecha_emision'

### **7. Configurar Valores por Defecto:**

**Para columnas con DEFAULT, pestaña "Columns" → columna específica:**
- Click en columna
- Standard Checks: marcar "Default value"
- Default value: escribir el valor
  - activo: TRUE
  - fecha_registro: CURRENT_TIMESTAMP
  - estado: 'COMPLETADA'
  - descuento: 0.00

### **8. Generar Script SQL:**

Una vez completado el diagrama:
- Database → Generate Database...
- Selection: seleccionar todas las tablas
- Options: marcar "Create table", "Create index", "Create constraint"
- Directory: elegir carpeta (carpeta 03 BDD)
- File name: `Comercializadora_Schema.sql`
- Generate

---

## 📝 NOTAS IMPORTANTES PARA EL DOCUMENTO

### **Según el enunciado del examen:**

1. **NO tiene inventario:** 
   - Solo tabla de productos disponibles
   - No hay control de stock
   - No hay tabla de entradas/salidas de inventario

2. **Formas de pago obligatorias:**
   - EFECTIVO: descuento 33% automático
   - CREDITO_DIRECTO: invoca WS BanQuito

3. **Relación con Banco:**
   - Campo `id_credito_banco` en FACTURA almacena el ID del crédito otorgado
   - Este campo se llena cuando WS BanQuito aprueba y otorga el crédito
   - NO es FK física (bases de datos separadas)

4. **Mínimo de entidades:**
   - ELECTRODOMESTICO (obligatorio - catálogo)
   - FACTURA (obligatorio - ventas)
   - DETALLE_FACTURA (obligatorio - ítems)
   - CLIENTE (opcional - si quieres almacenar histórico)

---

## ✅ CHECKLIST DE EVALUACIÓN (Item 4)

- [ ] Diagrama PDM creado en PowerDesigner
- [ ] Entidad ELECTRODOMESTICO con todos sus atributos
- [ ] Entidad FACTURA con forma_pago y descuento
- [ ] Entidad DETALLE_FACTURA con FKs correctas
- [ ] Relaciones 1:N correctamente dibujadas
- [ ] PKs y FKs correctamente definidas
- [ ] Cardinalidades mostradas (Crow's Foot notation)
- [ ] Restricciones CHECK agregadas
- [ ] Tipos de datos apropiados (DECIMAL para dinero)
- [ ] Script SQL generado y funcional

---

## 🖼️ EXPORTAR DIAGRAMA PARA DOCUMENTACIÓN

**Para incluir en tu documentación:**
1. PowerDesigner → View → Display Preferences
   - Marcar todas las opciones de visualización
2. Zoom para que se vea todo el diagrama
3. File → Export → Image
   - Format: PNG (alta resolución) o PDF
   - Resolution: 300 DPI
4. Guardar en: `04 DOCUMENTACION/Diagrama_ER_Comercializadora.png`

---

Con estas especificaciones puedes crear el diagrama completo en PowerDesigner y cumplir el **Item 4 (1.0 punto)** de la rúbrica.
