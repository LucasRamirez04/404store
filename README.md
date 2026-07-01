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
| **H2 / MySQL** | H2 en memoria para producción y test; MySQL local para desarrollo |
| **Flyway** | Gestiona y versiona las migraciones del esquema de base de datos |
| **Spring HATEOAS** | Agrega hipervínculos a las respuestas REST para navegabilidad |
| **WebClient** | Cliente reactivo para la comunicación HTTP entre microservicios |
| **Springdoc OpenAPI** | Genera automáticamente la documentación Swagger de cada microservicio |
| **JUnit 5 + Mockito** | Framework de pruebas unitarias con mocking de dependencias |
| **Datafaker** | Genera datos de prueba realistas al iniciar cada microservicio |
| **Docker** | Empaqueta cada microservicio en un contenedor para su despliegue |
| **Render** | Plataforma cloud donde están desplegados todos los microservicios |
| **GitHub** | Control de versiones y colaboración del equipo |

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

Base URL: `https://api-gateway-gc06.onrender.com`

| Microservicio | Ejemplo de ruta |
|---|---|
| Cliente | `/microservicio-cliente/api/store/clientes/listar` |
| Producto | `/microservicio-producto/api/store/productos/listar` |
| Pedido | `/microservicio-pedido/api/store/pedidos/listar` |
| Pago | `/microservicio-pago/api/store/pagos/listar` |
| Envío | `/microservicio-envio/api/store/envios/listar` |
| Promoción | `/microservicio-promocion/api/store/promociones/listar` |
| Notificación | `/microservicio-notificacion/api/store/notificaciones/listar` |
| Inventario | `/microservicio-inventario/api/store/inventarios/listar` |
| Reseña | `/microservicio-resena/api/store/resenas/listar` |
| Devolución | `/microservicio-devolucion/api/store/devoluciones/listar` |

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
