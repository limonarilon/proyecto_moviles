# EcoMarket Móvil  Desarrollo de aplicaciones móviles

## 1. Sobre el proyecto
Este proyecto consiste en el desarrollo de una aplicación móvil basada en el patrón de arquitectura **Modelo–Vista–Modelo de Vista (MVVM)**.  
Su objetivo es facilitar la gestión interna de información dentro de la empresa ficticia EcoMarket, permitiendo administrar **usuarios, productos y pedidos** desde dispositivos móviles autorizados.

---

## 2. Sobre el equipo
El equipo de desarrollo está integrado por:

- **Carolina Garrido (moonsline)**
- **Ariel Silva (limonarilon)**

Ambos participaron en todo el proceso: planificación, levantamiento de requisitos, desarrollo funcional y pruebas correspondientes para esta primera versión.

---

## 3. Funcionalidades

La aplicación cuenta con:

- Sistema de **inicio de sesión con JWT**
- Comunicación con el backend mediante **HTTP REST**
- Integración con API externa para clima
- Módulos CRUD para usuarios, productos y pedidos

### 3.1 Funcionalidades de gestión

---

### 3.1.1 Gestión de usuarios
Incluye operaciones CRUD:

- Crear nuevos usuarios
- Editar usuarios existentes (rol Administrador)
- Ver lista de usuarios
- Búsqueda en tiempo real
- Eliminar usuarios
- Manejo seguro (contraseña no visible)

---

### 3.1.2 Gestión de productos
Permite:

- Crear productos con validación de campos
- Editar productos, incluyendo imágenes
- Almacenar imágenes en el servidor
- Eliminar productos
- Ver precio, stock, nombre, id e imagen

---

### 3.1.3 Gestión de pedidos
Incluye CRUD completo:

- Crear pedidos
- Editar pedidos
- Listar pedidos
- Eliminar pedidos

La edición permite:

- Ver productos asociados
- Agregar/quitar productos
- Cambiar estado: **PENDIENTE**, **ENVIADO**, **ENTREGADO**

---

### 3.2 Funcionalidades secundarias

#### 3.2.1 Linterna
Activación/desactivación de la linterna del dispositivo (útil para logística).

#### 3.2.2 Indicador climático
Integra **Meteosource API**, mostrando la temperatura mediante geolocalización.

---

## 4. Endpoints utilizados (API externa y microservicios)

### 4.1 API externa – Indicador climático
Proveedor: **Meteosource API**  
URL base: `https://www.meteosource.com/api/v1/free/`

**Endpoint:**
- `GET /point`

---

### 4.2 API interna de microservicios
Backend desarrollado en **Java Spring Boot**, con arquitectura MVC, Swagger, HATEOAS y despliegue planificado en AWS.

URL base (emulador Android):  
`http://10.0.2.2:8080/`

URL base (LAN local):  
`http://<IP_MAQUINA_LOCAL>:8080/`

---

### Endpoints principales

#### Usuarios
- `GET /usuarios`
- `GET /usuarios/{id}`
- `POST /usuarios`
- `PUT /usuarios/{id}`
- `DELETE /usuarios/{id}`

#### Productos
- `GET /productos`
- `GET /productos/{id}`
- `POST /productos`
- `PUT /productos/{id}`
- `DELETE /productos/{id}`

#### Pedidos
- `GET /pedidos`
- `GET /pedidos/{id}`
- `POST /pedidos`
- `PUT /pedidos/{id}`
- `DELETE /pedidos/{id}`
- `POST /pedidos/{idPedido}/productos/{idProducto}`
- `DELETE /pedidos/{idPedido}/productos/{idProducto}`

#### Autenticación
- `POST /auth/login` — Retorna JWT según rol.

---

## 5. **Flujo de uso de la aplicación**

### 5.1 Inicio de la aplicación
1. El usuario abre la app.
2. Visualiza la pantalla de **login**.
3. Si no tiene cuenta, selecciona:  
   **"¿No tienes cuenta? Regístrate aquí"**.
4. En el registro ingresa:
    - Nombre
    - Email
    - Contraseña
5. Luego inicia sesión con email + contraseña.

---

### 5.2 Menú principal
Una vez autenticado, accede al menú con los botones:

- **Gestión de Usuarios**
- **Gestión de Productos**
- **Gestión de Pedidos**
- **Botón de Linterna (ON/OFF)**
- **Indicador del clima según geolocalización**

En la barra superior hay un menú desplegable con:

- **Misión**
- **Visión**

---

### 5.3 Gestión de Usuarios
Opciones:

- Agregar usuario (formulario)
- Ver usuarios existentes
- Editar usuario (desde la lista)
- Eliminar usuario (desde la lista)
- Volver al menú principal

Los usuarios muestran id, nombre, email y botones de acción.

---

### 5.4 Gestión de Productos
Flujo equivalente al de usuarios:

- Agregar producto
- Ver productos
- Editar producto
- Eliminar producto
- Volver

Cada producto muestra nombre, stock, precio e imagen.

---

### 5.5 Gestión de Pedidos
Incluye:

- Ver pedidos
- Ver productos asociados a cada pedido
- Editar pedido
- Agregar o quitar productos
- Cambiar estado del pedido
- Crear nuevo pedido
- Eliminar pedido
- Volver

---

### 5.6 Barra superior Misión y Visión
Accesible desde cualquier pantalla. Muestra información institucional:

- Misión de EcoMarket
- Visión de la empresa

---

## 6. Anexos


