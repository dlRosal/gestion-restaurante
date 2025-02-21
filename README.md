# 📌 Sistema de Gestión de Pedidos para Restaurantes

Este proyecto es un **sistema de gestión de pedidos para restaurantes**, desarrollado en **Java con JavaFX** y una base de datos **MySQL**. Permite la gestión de **clientes, productos y pedidos**, asegurando un flujo eficiente en la administración del restaurante.

## 🚀 **Características Principales**

### ✅ **Gestión de Clientes**
- Registrar nuevos clientes con nombre y teléfono.
- Buscar, actualizar y eliminar clientes.
- Listado de clientes en una tabla interactiva.

### ✅ **Gestión de Productos**
- Agregar productos al menú con nombre, categoría, precio y disponibilidad.
- Modificar y eliminar productos.
- Filtrar y seleccionar productos disponibles.

### ✅ **Gestión de Pedidos**
- Crear pedidos seleccionando clientes y productos.
- Agregar múltiples productos a un pedido antes de guardarlo.
- Calcular automáticamente el total del pedido en base a los productos agregados.
- Modificar o eliminar productos de un pedido antes de confirmarlo.
- Gestionar el estado del pedido (**Pendiente, En preparación, Entregado**).
- Ver detalles completos de los productos de un pedido seleccionado.

### ✅ **Integración con MySQL**
- Almacena y gestiona la información en una base de datos **MySQL**.
- Actualiza automáticamente los totales del pedido al agregar o eliminar productos.
- Realiza consultas en tiempo real para reflejar los cambios en la interfaz.

### ✅ **Interfaz Gráfica con JavaFX**
- UI moderna y limpia con JavaFX y CSS.
- Uso de **TableView** para visualizar clientes, productos y pedidos.
- Navegación fluida entre diferentes secciones.
- Mensajes de alerta para notificaciones y errores.

---

## 🛠️ **Tecnologías Utilizadas**
- **Java 11+** - Lenguaje principal.
- **JavaFX** - Para la interfaz gráfica.
- **MySQL** - Base de datos relacional.
- **JDBC** - Conexión entre Java y MySQL.
- **FXML** - Diseño de la interfaz gráfica.

---

## ⚙️ **Instalación y Configuración**

### 🔹 **Requisitos Previos**
1. Tener instalado **Java JDK 11+**.
2. Tener instalado **MySQL Server** y **MySQL Workbench**.
3. Tener **JavaFX** configurado en el IDE (IntelliJ IDEA o Eclipse).
4. Instalar **XAMPP** si deseas gestionar MySQL localmente.

### 🔹 **Configuración del Proyecto**
1. **Clona este repositorio:**
   ```bash
   git clone https://github.com/tu-usuario/gestion-restaurante.git
   cd gestion-restaurante
   ```

2. **Importa el proyecto en tu IDE preferido.**
3. **Configura la base de datos MySQL:**
   - Crea una base de datos llamada `gestionpedidorestaurante`.
   - Usa los scripts SQL en `database.sql` para crear las tablas necesarias.

4. **Configura la conexión a la base de datos en `DatabaseConnection.java`:**
   ```java
   private static final String URL = "jdbc:mysql://localhost:3306/gestionpedidorestaurante";
   private static final String USER = "root";
   private static final String PASSWORD = ""; // Agregar tu contraseña si es necesario
   ```

5. **Ejecuta la aplicación** desde tu IDE.

---

## 📄 **Estructura del Proyecto**
```
📂 gestion-restaurante
│── 📂 src/main/java/org/example/pedidosrestaurantes
│   ├── 📂 controllers  # Controladores de la UI
│   ├── 📂 modelos      # Clases de modelo (Pedido, Cliente, Producto)
│   ├── 📂 database     # Configuración de la base de datos
│   ├── DatabaseConnection.java  # Clase de conexión con MySQL
│── 📂 resources        # Archivos FXML y CSS
│── README.md           # Documentación del proyecto
│── database.sql        # Script SQL para la base de datos
```

---

## 📌 **Funcionalidades Futuras**
🔹 Reportes de ventas en PDF usando JasperReports.  
🔹 Implementación de autenticación de usuarios.  
🔹 Mejora en el diseño de UI con estilos personalizados.  

---

## 💡 **Contribuciones**
Si deseas contribuir, abre un **Pull Request** o crea un **Issue** con tu propuesta de mejora.

---

