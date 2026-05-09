#  Sistema de Tienda Microservicios (404Store)

Este proyecto es una solución escalable basada en **Spring Boot**, que implementa una arquitectura de microservicios para gestionar ventas, pagos y logística.

##  Arquitectura del Sistema
El sistema se compone de 5 microservicios que se comunican de forma síncrona mediante **WebClient**:

* **Cliente (puerto:8081):** Gestión de perfiles y seguridad (JWT).
* **Producto (puerto:8082):** Inventario y catálogo.
* **Pedido (puerto:8083):** Orquestador de la venta.
* **Pago (puerto:8084):** Procesamiento de transacciones.
* **Envío (puerto:8085):** Logística y seguimiento de entregas.

##  Tecnologías Utilizadas
* Java 21 / Maven
* Spring Boot 3.x (Web, Data JPA, Security)
* MySQL / Oracle XE
* JSON Web Token (JWT) (Microsericio Cliente)

##  Configuración de Seguridad
El microservicio de **Cliente** actúa como el núcleo de seguridad:
* **Autenticación:** Basada en Bearer Token.

##  Flujo de Trabajo
1. Se ingresan **Clientes** al sistema.
2. Se ingresan **Productos** al sistema.
3. Se intenta crear un **Pedido**
4. Se valida que los datos del pedido sean validos
    1. Que el idCliente pertenezca a un cliente valido
    2. Que el idProducto pertenezca a un producto valido
    3. Que el producto tenga stock suficiente
5. Si los datos son validos se registra el **Pedido**
6. **Pedido** genera un registro en **Pago** (Estado: PENDIENTE).
7. Al confirmar el **Pago** -> se recupera la dirección desde **Cliente**.
8. Se calcula el **5% de costo de envío** y se crea el registro en **Envío**.
9. Se confirma el **Envio** terminando asi el flujo de una compra.

## Endpoints Usados en el proyecto
###  Cliente (http://localhost:8081)
| Método | Endpoint | Descripción |
| :--- | :--- | :--- |
| POST | `/auth/login` | Se ejecuta para recibir el token de autentificacion |
| GET | `/api/store/clientes` | Lista todos los clientes registrados en el sistema|
| POST | `/api/store/clientes` | Se registra un nuevo cliente |
| GET | `/api/store/clientes/{id}` | Se busca un cliente por su id |
| PUT | `/api/store/clientes/{id}` | Se edita un cliente por su id |
| DELETE | `/api/store/clientes/{id}` | Se elimina un cliente por su id |

### Producto (http://localhost:8082)
| Método | Endpoint | Descripción |
| :--- | :--- | :--- |
| GET | `/api/store/productos` | Lista todos los productos registrados en el sistema |
| POST | `/api/store/productos` | Se regista un nuevo producto |
| GET | `/api/store/productos/{id}` | Se busca un producto por su id |
| GET | `/api/store/productos/categoria/{nombreCategoria}` | Se filtran los productos por una categoria especifica |
| PUT | `/api/store/productos/{id}` | Se actualiza un producto por su id |
| DELETE | `/api/store/productos/{id}` | Se elimina un producto del sistema por su id |
| GET | `/api/store/productos/{id}/descontar` | EndPoint que ocupa el microservicio Pedido para descontar el stock al producto |

###  Pedido (http://localhost:8083)
| Método | Endpoint | Descripción |
| :--- | :--- | :--- |
| GET | `/api/store/pedidos` | Lista todos los pedidos registrados en el sistema |
| POST | `/api/store/pedidos` | Crea un pedido (Valida stock y crea pago pendiente) |
| GET | `/api/store/pedidos/{id}` | Se busca un pedido por su id |
| GET | `/api/store/pedidos/{id}/detalle-envio` | Obtiene dirección del cliente para logística |

###  Pago (http://localhost:8084)
| Método | Endpoint | Descripción |
| :--- | :--- | :--- |
| GET | `/api/store/pagos` | Lista todos los pagos registrados en el sistema |
| POST | `/api/store/pagos` | EndPoint que ocupa el microservicio Pedido para crear un pago luego de registrar un pedido |
| PATCH | `/api/store/pagos/{id}/{metodo}/confirmar` | Confirma el pago, registra el metodo de pago y dispara la creación del envío |
| DELETE | `/api/store/pagos` | Se elimina un pago del sistema por su id |

###  Envío (http://localhost:8085)
| Método | Endpoint | Descripción |
| :--- | :--- | :--- |
| GET | `/api/store/envios` | Lista todos los envíos registrados en el sistema |
| POST | `/api/store/envios` | EndPoint que ocupa el microservicio Pago para crear un envio luego de confirmar un pago |
| DELETE | `/api/store/envios` | Se elimina un envio del sistema por su id |
| PATCH | `/api/store/envios/{id}/confirmar` | Registra fecha de recibido y cambia estado a ENTREGADO|

## JSON DE EJEMPLOS

### Cliente
{
     "run": "", 
     "nombre": "", 
     "apellido": "",
     "correo": "@gmail.com", 
     "telefono" : "+569",
     "direccion" : ""
}

### Producto
{
     "nombre": "", 
     "descripcion": "", 
     "precio":,
     "stock": , 
     "categoria" : "",
     "marca" : ""
}

### Pedido 
{
     "productoId": , 
     "clienteId": , 
     "cantidad": 
}

## Base de datos en servidor local

### Cliente
* db_404store_cliente

### Producto
* db_404store_producto

### Pedido
* db_404store_pedido

### Pago
* db_404store_pago

### Envio
* db_404store_envio