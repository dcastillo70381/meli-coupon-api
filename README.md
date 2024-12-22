# meli-coupon-api
Servicio que permite calcular la lista de ítems favoritos que maximizan el gasto sin exceder su monto total.

Este proyecto es un servicio RESTful construido con **Spring Boot** que calcula los ítems óptimos para comprar con un cupón, dada una lista de ítems y un monto disponible. Utiliza un algoritmo de programación dinámica para encontrar la mejor combinación de ítems que maximiza el uso del cupón.

La API está diseñada sin necesidad de una base de datos, ya que los precios de los ítems se obtienen de un cliente simulado a través de una API externa.

## Tabla de Contenidos

1. [Requisitos](#requisitos)
2. [Instalación](#instalación)
3. [Uso](#uso)
4. [Estructura del Proyecto](#estructura-del-proyecto)
5. [Documentación de la API](#documentación-de-la-api)
6. [Manejo de Errores](#manejo-de-errores)
7. [Diagramas](#diagramas)
8. [Licencia](#licencia)

---

## Requisitos

Antes de ejecutar este proyecto, asegúrate de tener instalados los siguientes requisitos:

- **Java 17** o superior.
- **Gradle** para la gestión de dependencias y construcción del proyecto.
- **Docker** (opcional, si deseas ejecutar el proyecto en un contenedor Docker).

---

## Instalación

1. **Clonar el repositorio**:

   Clona el proyecto desde GitHub y navega a la carpeta del proyecto:

   ```bash
   git clone https://github.com/dcastillo70381/meli-coupon-api.git
   cd meli_coupon_api
   ```

2. **Instalar dependencias**:

   Si no tienes **Gradle** instalado, puedes instalarlo desde [aquí](https://gradle.org/install/). Luego, ejecuta:

   ```bash
   gradle build
   ```

   Este comando descargará todas las dependencias necesarias y compilará el proyecto.

3. **Compilar y ejecutar el proyecto**:

   Para ejecutar la aplicación, usa el siguiente comando:

   ```bash
   gradle bootRun
   ```

   El servidor se iniciará en el puerto **8080** por defecto.

---

## Uso

La API expone el siguiente endpoint para calcular los ítems óptimos a comprar con el cupón:

### POST `/coupon`

#### Descripción:
Este endpoint recibe un conjunto de ítems y un monto disponible para calcular la combinación óptima de ítems que maximiza el uso del cupón.

#### Parámetros de entrada:
- **item_ids**: Un conjunto de identificadores de los ítems (ej. `["item1", "item2", "item3"]`).
- **amount**: El monto del cupón (ej. `50.0`).

#### Ejemplo de solicitud:

```json
{
  "item_ids": ["item1", "item2", "item3"],
  "amount": 50.0
}
```

#### Respuesta exitosa (200 OK):

Si se calcula una combinación óptima de ítems, la respuesta será una lista de ítems seleccionados con su precio total:

```json
{
  "item_ids": ["item1", "item2"],
  "total_price": 45.0
}
```

#### Respuesta de error:

Si no se encuentra una combinación válida, la respuesta será un error `404 Not Found` con una descripción:

```json
{
  "code": 404,
  "message": "No se puede comprar ningún ítem con el monto proporcionado."
}
```

Si los datos de entrada son inválidos, la respuesta será un error `400 Bad Request`:

```json
{
  "code": 400,
  "message": "Los datos de entrada son inválidos."
}
```

---

## Estructura del Proyecto

La estructura del proyecto sigue un patrón común en aplicaciones basadas en **Spring Boot** y **Gradle**:

```
meli_coupon_api/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── com/
│   │   │   │   ├── example/
│   │   │   │   │   ├── meli_coupon_api/
│   │   │   │   │   │   ├── controller/
│   │   │   │   │   │   ├── model/
│   │   │   │   │   │   ├── service/
│   │   │   │   │   │   ├── restclient/
│   │   │   │   │   ├── Application.java   # Clase principal de Spring Boot
│   ├── resources/
│   │   ├── application.properties          # Configuración de la aplicación
│   │   ├── static/
│   │   ├── templates/
│
└── build.gradle                            # Configuración de Gradle
```

### Descripción de las Carpetas:

- **controller**: Contiene los controladores REST, como el `CouponController`.
- **model**: Define las clases de modelo, como `CouponRequest`, `CouponResponse`, y `ErrorResponse`.
- **service**: Contiene la lógica de negocio, como el servicio `CouponService`.
- **restclient**: Define los clientes HTTP, como `ItemClient`, para obtener los precios de los ítems.

---

## Documentación de la API

Puedes consultar la documentación interactiva de la API usando **Swagger**, que está integrada en la aplicación a través de **Springdoc OpenAPI**.

Una vez que el servidor esté corriendo, visita:

```
http://localhost:8080/swagger-ui.html
```

Allí podrás ver y probar los endpoints disponibles de la API.

---

## Manejo de Errores

El sistema está diseñado para manejar los siguientes tipos de errores:

1. **400 Bad Request**: Ocurre cuando los datos de entrada no son válidos o están incompletos.
2. **404 Not Found**: Ocurre cuando no se puede encontrar una combinación válida de ítems para el monto proporcionado.
3. **500 Internal Server Error**: Ocurre cuando hay un error inesperado en el servidor.

Las respuestas de error siguen la estructura `ErrorResponse`:

```json
{
  "code": 400,
  "message": "Los datos de entrada son inválidos."
}
```

---

## Diagramas

### Diagrama de Secuencia: Cálculo de Ítems Óptimos

```plaintext
Usuario -> API Rest (CouponController) : Realiza POST /coupon
API Rest (CouponController) -> CouponService : Llama a calculateOptimalItems()
CouponService -> ItemClient : Obtiene precios de ítems
ItemClient -> CouponService : Retorna precios de ítems
CouponService -> API Rest (CouponController) : Retorna combinación óptima de ítems
API Rest (CouponController) -> Usuario : Responde con la combinación de ítems
```

### Diagrama de Clases

```plaintext
+-------------------+     +---------------------+     +-------------------+
| CouponController  |<--->| CouponService       |<--->| ItemClient        |
+-------------------+     +---------------------+     +-------------------+
| - couponService   |     | - itemClient        |     |                   |
|                   |     | - priceCache        |     |                   |
| +getCouponItems() |     | +calculateOptimalItems() |                   |
+-------------------+     +---------------------+     +-------------------+

+-------------------+
| CouponRequest     |
+-------------------+
| - item_ids        |
| - amount          |
+-------------------+

+-------------------+
| CouponResponse    |
+-------------------+
| - item_ids        |
| - total_price     |
+-------------------+

+-------------------+
| ErrorResponse     |
+-------------------+
| - code            |
| - message         |
| - details         |
+-------------------+
```
---

### Configuración de Gradle

Si no tienes configurado **Gradle** en tu proyecto, asegúrate de tener un archivo `build.gradle` con el siguiente contenido básico:

```gradle
plugins {
    id 'org.springframework.boot' version '3.1.0'
    id 'io.spring.dependency-management' version '1.1.0'
    id 'java'
}

group = 'com.example'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = '17'

repositories {
    mavenCentral()
}

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-validation'
    implementation 'org.springdoc:springdoc-openapi-ui:2.1.0'
    implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
}

test {
    useJUnitPlatform()
}
```

Con esta configuración, puedes ejecutar las tareas básicas de Gradle como `gradle build` o `gradle bootRun` para construir y ejecutar la aplicación.

---