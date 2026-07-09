# 🛒 404Store — Arquitectura de Microservicios

## 📋 Descripción del Proyecto

**404Store** es un sistema de comercio electrónico desarrollado con una arquitectura de microservicios. La tienda ofrece una variedad de productos y gestiona todo el flujo de compra: desde el registro de clientes y catálogo de productos, hasta el procesamiento de pedidos, pagos, envíos y devoluciones. Cada microservicio es independiente, tiene su propia base de datos y se comunica con los demás a través de un API Gateway centralizado.

---

## 👥 Integrantes del Equipo

| Nombre |
|---|
| Lucas Ramírez |
| Javier Alvarado |
| Franco Pacheco |
| Lucas Pizarro |
| Nicolas Aravena |

---

## 🧩 Microservicios Implementados

| Microservicio | Puerto | Responsabilidad |
|---|---|---|
| Eureka Server | 8761 | Registro y descubrimiento de servicios |
| API Gateway | 8080 | Enrutamiento centralizado de peticiones |
| Cliente | 8081 | Gestión de clientes registrados |
| Producto | 8082 | Catálogo y gestión de productos |
| Pedido | 8083 | Creación y seguimiento de pedidos |
| Pago | 8084 | Procesamiento y registro de pagos |
| Envío | 8085 | Gestión de envíos y seguimiento |
| Notificación | 8086 | Envío de notificaciones a clientes |
| Promoción | 8087 | Administración de promociones y descuentos |
| Inventario | 8088 | Control de ubicación de productos en bodega |
| Reseña | 8089 | Gestión de reseñas de productos |
| Devolución | 8090 | Procesamiento de devoluciones |

---

## 🛠️ Herramientas y Tecnologías

| Herramienta | Descripción |
|---|---|
| **Java 21 + Spring Boot 4.0.7** | Lenguaje y framework base de todos los microservicios |
| **Spring Cloud Gateway** | API Gateway que centraliza y enruta todas las peticiones entrantes |
| **Netflix Eureka** | Servidor de registro que permite a los servicios descubrirse entre sí |
| **Spring Data JPA** | Abstracción para el acceso y persistencia de datos con la base de datos |
| **PostgreSQL** | Base de datos relacional en la nube para el entorno de producción (Render) |
| **MySQL** | Base de datos relacional para el entorno de desarrollo local |
| **Flyway** | Gestiona y versiona las migraciones del esquema de base de datos |
| **Spring HATEOAS** | Agrega hipervínculos a las respuestas REST para navegabilidad |
| **WebClient** | Cliente reactivo para la comunicación HTTP entre microservicios |
| **Springdoc OpenAPI** | Genera automáticamente la documentación Swagger de cada microservicio |
| **JUnit 5 + Mockito** | Framework de pruebas unitarias con mocking de dependencias |
| **Datafaker** | Genera datos de prueba realistas al iniciar cada microservicio |
| **Docker** | Empaqueta cada microservicio en un contenedor para su despliegue |
| **Docker Compose** | Orquesta todos los microservicios para ejecución local con un solo comando |
| **Render** | Plataforma cloud donde están desplegados todos los microservicios |
| **GitHub** | Control de versiones y colaboración del equipo |

---

## 🗄️ Base de Datos en la Nube

Todos los microservicios en producción comparten una única base de datos **PostgreSQL** alojada en **Render**. Cada microservicio gestiona sus propias tablas de forma independiente, las cuales son creadas automáticamente por Hibernate al arrancar el servicio por primera vez (`ddl-auto: update`).

Las credenciales de conexión se inyectan de forma segura mediante variables de entorno definidas en el `render.yaml`:

| Variable | Descripción |
|---|---|
| `DB_URL` | URL de conexión interna de PostgreSQL en Render |
| `DB_USER` | Usuario de la base de datos |
| `DB_PASSWORD` | Contraseña de la base de datos |

La separación por perfiles garantiza que cada entorno use su propia base de datos:

| Perfil | Base de datos |
|---|---|
| `dev` | MySQL local (Laragon o Docker Compose) |
| `test` | H2 en memoria (aislada por prueba) |
| `prod` | PostgreSQL en Render |

---

## 🌐 URLs de Producción

| Servicio | URL |
|---|---|
| API Gateway | https://api-gateway-gc06.onrender.com |
| Eureka Server | https://eureka-server-mg8f.onrender.com |
| Cliente | https://microservicio-cliente.onrender.com |
| Producto | https://microservicio-producto-ji1e.onrender.com |
| Pedido | https://microservicio-pedido.onrender.com |
| Pago | https://microservicio-pago.onrender.com |
| Envío | https://microservicio-envio.onrender.com |
| Promoción | https://microservicio-promocion.onrender.com |
| Notificación | https://microservicio-notificacion.onrender.com |
| Inventario | https://microservicio-inventario.onrender.com |
| Reseña | https://microservicio-resena.onrender.com |
| Devolución | https://microservicio-devolucion.onrender.com |

---

## 🔀 Rutas del API Gateway

| Microservicio | URL completa |
|---|---|
| Cliente | https://api-gateway-gc06.onrender.com/microservicio-cliente/api/store/clientes/listar |
| Producto | https://api-gateway-gc06.onrender.com/microservicio-producto/api/store/productos/listar |
| Pedido | https://api-gateway-gc06.onrender.com/microservicio-pedido/api/store/pedidos/listar |
| Pago | https://api-gateway-gc06.onrender.com/microservicio-pago/api/store/pagos/listar |
| Envío | https://api-gateway-gc06.onrender.com/microservicio-envio/api/store/envios/listar |
| Promoción | https://api-gateway-gc06.onrender.com/microservicio-promocion/api/store/promociones/listar |
| Notificación | https://api-gateway-gc06.onrender.com/microservicio-notificacion/api/store/notificaciones/listar |
| Inventario | https://api-gateway-gc06.onrender.com/microservicio-inventario/api/store/inventarios/listar |
| Reseña | https://api-gateway-gc06.onrender.com/microservicio-resena/api/store/resenas/listar |
| Devolución | https://api-gateway-gc06.onrender.com/microservicio-devolucion/api/store/devoluciones/listar |

---

## 📖 Documentación Swagger

Cada microservicio expone su documentación en `/doc/swagger-ui.html`:

| Microservicio | Swagger UI |
|---|---|
| Cliente | https://microservicio-cliente.onrender.com/doc/swagger-ui.html |
| Producto | https://microservicio-producto-ji1e.onrender.com/doc/swagger-ui.html |
| Pedido | https://microservicio-pedido.onrender.com/doc/swagger-ui.html |
| Pago | https://microservicio-pago.onrender.com/doc/swagger-ui.html |
| Envío | https://microservicio-envio.onrender.com/doc/swagger-ui.html |
| Promoción | https://microservicio-promocion.onrender.com/doc/swagger-ui.html |
| Notificación | https://microservicio-notificacion.onrender.com/doc/swagger-ui.html |
| Inventario | https://microservicio-inventario.onrender.com/doc/swagger-ui.html |
| Reseña | https://microservicio-resena.onrender.com/doc/swagger-ui.html |
| Devolución | https://microservicio-devolucion.onrender.com/doc/swagger-ui.html |

---