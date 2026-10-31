# EventPass — Microservicio de Usuarios

Microservicio REST de **EventPass** destinado a gestionar **usuarios, comunas y regiones**, desarrollado con **Java 21**, **Spring Boot 4.1.1**, **Spring Data JPA** y una base de datos relacional **H2 persistente en archivo**.

Este repositorio forma parte del trabajo académico de la asignatura **Java: Diseño y Construcción de Soluciones Nativas en Nube (JVY0101)**.

> **Alcance de esta versión:** el proyecto implementa operaciones CRUD para usuarios, comunas y regiones, y almacena hashes de las contraseñas mediante BCrypt antes de guardarlas. Aunque el `pom.xml` incluye una dependencia de OAuth2 Resource Server, **el código entregado no incluye endpoints de login/registro mediante JWT ni un flujo completo de autenticación JWT**. No se debe interpretar la dependencia por sí sola como una autenticación JWT ya implementada.

## 1. Funcionalidades

- Crear, consultar, actualizar y eliminar **regiones**.
- Crear, consultar, actualizar y eliminar **comunas**, asociadas a regiones existentes.
- Crear, consultar, actualizar y eliminar **usuarios**, asociados a comunas existentes.
- Registrar usuarios con roles `COMPRADOR` o `STAFF_DE_PUERTA`.
- Generar hashes de contraseñas de usuarios con `BCryptPasswordEncoder` al crearlos o modificar una contraseña.
- Asignar automáticamente la fecha de creación de un usuario mediante `@PrePersist`.
- Persistir los datos en una base de datos local H2.
- Exponer endpoints HTTP para pruebas manuales con Postman.

## 2. Tecnologías

| Componente | Uso |
|---|---|
| Java 21 | Lenguaje y versión objetivo del proyecto |
| Spring Boot 4.1.1 | Framework principal |
| Spring Web MVC | Exposición de endpoints REST |
| Spring Data JPA / Hibernate | Acceso a datos y mapeo de entidades |
| H2 Database | Base de datos relacional en archivo |
| Spring Security Crypto | Hash seguro de contraseñas mediante BCrypt |
| Spring Security OAuth2 Resource Server | Dependencia declarada; integración JWT no completada en el código adjunto |
| Maven / Maven Wrapper | Gestión de dependencias, pruebas y empaquetado |
| JUnit / Spring Boot Test | Prueba básica de carga de contexto |

Las dependencias están declaradas en el archivo `pom.xml`.

## 3. Arquitectura y organización del código

Se utiliza una organización por capas:

```text
src/
├── main/
│   ├── java/com/alonsoravanalrubenvelazquez/ep_ms_java_autenticacion_usuarios/
│   │   ├── controllers/
│   │   │   ├── RegionRestController.java
│   │   │   ├── ComunaRestController.java
│   │   │   └── UsuarioRestController.java
│   │   ├── services/
│   │   │   ├── RegionService.java
│   │   │   ├── RegionServiceImpl.java
│   │   │   ├── ComunaService.java
│   │   │   ├── ComunaServiceImpl.java
│   │   │   ├── UsuarioService.java
│   │   │   └── UsuarioServiceImpl.java
│   │   ├── repositories/
│   │   │   ├── RegionRepository.java
│   │   │   ├── ComunaRepository.java
│   │   │   └── UsuarioRepository.java
│   │   ├── entities/
│   │   │   ├── Region.java
│   │   │   ├── Comuna.java
│   │   │   └── Usuario.java
│   │   ├── security/
│   │   │   └── SecurityConfig.java
│   │   └── EpMsJavaAutenticacionUsuariosApplication.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/com/alonsoravanalrubenvelazquez/ep_ms_java_autenticacion_usuarios/
        └── EpMsJavaAutenticacionUsuariosApplicationTests.java
```

**Responsabilidades de cada capa:**

- **Controller:** recibe las solicitudes HTTP y devuelve respuestas REST.
- **Service:** implementa las reglas de negocio, comprobaciones y transacciones.
- **Repository:** utiliza `JpaRepository` para acceder a la base de datos.
- **Entity:** define las entidades y sus relaciones JPA.
- **Security:** proporciona un bean `PasswordEncoder` basado en BCrypt.

### Modelo relacional

- **Región:** `id`, `nombre`. Una región contiene múltiples comunas (`@OneToMany`).
- **Comuna:** `id`, `nombre`, `region`. Cada comuna pertenece a una región (`@ManyToOne`) y tiene múltiples usuarios (`@OneToMany`).
- **Usuario:** `id`, `rut`, `pnombre`, `snombre`, `apellidos`, `email`, `contrasena`, `rol`, `activo`, `fechaCreacion`, `fechaNacimiento`, `direccion`, `comuna`.

Cada usuario puede estar asociado a una comuna mediante `@ManyToOne`; el campo `rol` es una enumeración almacenada como texto. La entidad declara `email` como único en la base de datos.

## 4. Requisitos previos

- **JDK 21** instalado y disponible en `PATH` (y `JAVA_HOME` configurado, si corresponde).
- **Git** para clonar el código.
- Acceso a Internet para descargar dependencias de Maven en la primera ejecución.
- **Postman** u otro cliente REST para realizar las pruebas.

El repositorio incluye **Maven Wrapper** (`mvnw.cmd` para Windows y `mvnw` para Linux/macOS), por lo que no es obligatorio instalar Maven globalmente.

Comprueba la versión de Java:

```cmd
java -version
```

## 5. Clonar e instalar el proyecto

Desde **Windows CMD**:

```cmd
git clone https://github.com/AlonsoRavanal/ep-ms-java-autenticacion-usuarios.git
cd ep-ms-java-autenticacion-usuarios
```

Para descargar dependencias y comprobar la configuración Maven:

```cmd
mvnw.cmd validate
```

Para compilar, ejecutar las pruebas y generar el archivo JAR:

```cmd
mvnw.cmd clean package
```

> `BUILD SUCCESS` debe confirmarse durante la ejecución en el equipo del evaluador. El archivo JAR se genera en `target/` si el empaquetado se completa correctamente.

## 6. Configuración de la base de datos

El archivo `src/main/resources/application.properties` contiene:

```properties
spring.application.name=ep-ms-java-autenticacion-usuarios
server.port=8081

spring.datasource.url=jdbc:h2:file:./data/bdusuarios
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

**Configuración:**

| Propiedad | Valor |
|---|---|
| Puerto HTTP | `8081` |
| Base de datos | H2 en archivo |
| URL JDBC | `jdbc:h2:file:./data/bdusuarios` |
| Usuario H2 | `sa` |
| Contraseña H2 | Vacía, según la configuración local adjunta |
| Gestión del esquema | `spring.jpa.hibernate.ddl-auto=update` |
| Ruta configurada de H2 Console | `/h2-console` |

Antes de iniciar la aplicación, crea la carpeta donde se guardará la base de datos:

```cmd
if not exist data mkdir data
```

La base de datos se crea o reutiliza en `data/bdusuarios.mv.db`, de acuerdo con la configuración H2 utilizada. **No es necesario distribuir el archivo `.mv.db` para ejecutar el servicio desde cero.**

> La consola web H2 está habilitada por propiedades, pero su acceso puede verse afectado por la configuración activa de Spring Security. No se ha incluido una configuración específica que garantice acceso a `/h2-console`.

## 7. Ejecutar el microservicio

Desde la raíz del proyecto, en **Windows CMD**:

```cmd
mvnw.cmd spring-boot:run
```

Alternativamente, después de un empaquetado exitoso:

```cmd
java -jar target/ep-ms-java-autenticacion-usuarios-0.0.1-SNAPSHOT.jar
```

El puerto definido para este servicio es `8081`. La URL base de la API es:

```text
http://localhost:8081
```

Para Linux/macOS, utiliza `./mvnw spring-boot:run` (si el archivo tiene permiso de ejecución).

**Nota de reproducibilidad y seguridad:** la presencia de la dependencia `spring-boot-starter-security-oauth2-resource-server` puede influir en la configuración de seguridad al arrancar y acceder a las rutas. En esta versión no aparece una configuración explícita de `SecurityFilterChain` ni de validación de JWT. Si el arranque o las peticiones reciben errores de seguridad, revisa primero la configuración de Spring Security; no des por hecho que los endpoints están públicos ni que ya existe un login funcional.

## 8. Endpoints REST

Los controladores implementan los siguientes endpoints. Los códigos indicados corresponden al flujo programado de las respuestas; restricciones de seguridad u otros errores de persistencia podrían producir respuestas diferentes.

### Regiones

| Método | Ruta | Operación |
|---|---|---|
| `GET` | `/api/regiones` | Listar regiones |
| `GET` | `/api/regiones/{id}` | Obtener región por ID |
| `POST` | `/api/regiones` | Crear región |
| `PUT` | `/api/regiones/{id}` | Actualizar región |
| `DELETE` | `/api/regiones/{id}` | Eliminar región |

### Comunas

| Método | Ruta | Operación |
|---|---|---|
| `GET` | `/api/comunas` | Listar comunas |
| `GET` | `/api/comunas/{id}` | Obtener comuna por ID |
| `POST` | `/api/comunas` | Crear comuna |
| `PUT` | `/api/comunas/{id}` | Actualizar comuna |
| `DELETE` | `/api/comunas/{id}` | Eliminar comuna |

### Usuarios

| Método | Ruta | Operación |
|---|---|---|
| `GET` | `/api/usuarios` | Listar usuarios |
| `GET` | `/api/usuarios/{id}` | Obtener usuario por ID |
| `POST` | `/api/usuarios` | Crear usuario |
| `PUT` | `/api/usuarios/{id}` | Actualizar usuario |
| `DELETE` | `/api/usuarios/{id}` | Eliminar usuario |

En los métodos implementados, los resultados habituales son `200 OK` para consultas y actualizaciones, `201 Created` para creaciones y `204 No Content` para eliminaciones correctas. Las búsquedas por ID inexistente en los `GET` devuelven `404 Not Found`; las operaciones sobre relaciones inexistentes también pueden responder `404`.

## 9. Pruebas manuales con Postman

En Postman, configura `Body → raw → JSON` y la cabecera `Content-Type: application/json` para las solicitudes `POST` y `PUT`.

Los IDs utilizados a continuación son **ejemplos**: reemplázalos por los IDs que entregue tu base de datos. Sigue el orden región → comuna → usuario, para que existan las relaciones requeridas.

### Prueba 1: registrar una región

**POST** `http://localhost:8081/api/regiones`

```json
{
  "nombre": "Región Metropolitana de Santiago"
}
```

**Resultado esperado del controlador:** `201 Created` con la región creada y su ID.

Verifica después con **GET** `http://localhost:8081/api/regiones`.

### Prueba 2: registrar una comuna

**POST** `http://localhost:8081/api/comunas`

```json
{
  "nombre": "Maipú",
  "region": {
    "id": 1
  }
}
```

Sustituye `1` por el ID real de la región. El servicio comprueba que la región indicada exista.

**Resultado esperado:** `201 Created`.

Verifica después con **GET** `http://localhost:8081/api/comunas`.

### Prueba 3: registrar un usuario

**POST** `http://localhost:8081/api/usuarios`

```json
{
  "rut": "11111111-1",
  "pnombre": "Usuario",
  "snombre": "De",
  "apellidos": "Prueba",
  "email": "usuario.prueba@example.com",
  "contrasena": "ClaveDePrueba123!",
  "rol": "COMPRADOR",
  "activo": true,
  "fechaNacimiento": "2000-05-15",
  "direccion": "Dirección de prueba",
  "comuna": {
    "id": 1
  }
}
```

Sustituye `1` por el ID real de la comuna. El correo debe ser distinto de uno ya registrado, porque la columna `email` tiene una restricción de unicidad.

**Resultado esperado del controlador:** `201 Created`. Antes de almacenar la entidad, el servicio transforma la contraseña con BCrypt, asigna `activo = true` cuando llega nulo y `@PrePersist` asigna la fecha de creación.

**Precaución:** usa solo información y contraseñas ficticias en las pruebas. En el código adjunto, `Usuario` incluye un getter de `contrasena` sin una anotación que la oculte del JSON; la respuesta podría incluir el **hash** de contraseña. Esto debe corregirse antes de exponer la API a usuarios reales.

### Prueba 4: consultar usuarios

- **GET** `http://localhost:8081/api/usuarios`
- **GET** `http://localhost:8081/api/usuarios/1`

Sustituye `1` por el ID existente del usuario. Para un ID inexistente, el controlador programa `404 Not Found`.

### Prueba 5: actualizar usuario

**PUT** `http://localhost:8081/api/usuarios/1`

```json
{
  "rut": "11111111-1",
  "pnombre": "Usuario",
  "snombre": "De",
  "apellidos": "Prueba Actualizada",
  "email": "usuario.prueba@example.com",
  "rol": "COMPRADOR",
  "activo": true,
  "fechaNacimiento": "2000-05-15",
  "direccion": "Nueva dirección de prueba",
  "comuna": {
    "id": 1
  }
}
```

Este `PUT` funciona como actualización de los campos indicados en el servicio. **Si se omite `contrasena` o se entrega vacía, `UsuarioServiceImpl` conserva la contraseña anterior**; si se entrega una contraseña no vacía, genera su hash con BCrypt antes de actualizar.

**Resultado esperado del controlador:** `200 OK` para un usuario existente con comuna válida.

### Prueba 6: eliminar un usuario de prueba

**DELETE** `http://localhost:8081/api/usuarios/1`

Para un usuario existente, el controlador devuelve `204 No Content`. Después, consulta nuevamente ese ID y comprueba que el `GET` devuelva `404 Not Found`.

### Pruebas de errores previstos

| Caso | Resultado previsto por el servicio |
|---|---|
| Crear comuna sin `region.id` | `400 Bad Request` |
| Crear comuna con una región inexistente | `404 Not Found` |
| Crear usuario sin `comuna.id` | `400 Bad Request` |
| Crear usuario con comuna inexistente | `404 Not Found` |
| Crear usuario sin contraseña o con contraseña en blanco | `400 Bad Request` |
| Buscar usuario por ID inexistente | `404 Not Found` |
| Eliminar usuario por ID inexistente | `404 Not Found` |

**Observación:** el código recibido no incluye anotaciones `@Valid` ni un conjunto completo de validaciones de formato de RUT, correo o fechas. Por ello, estas comprobaciones no deben presentarse como implementadas.

## 10. Compilación, pruebas y empaquetado con Maven

Desde Windows CMD, en la carpeta raíz del repositorio:

```cmd
mvnw.cmd clean
mvnw.cmd test
mvnw.cmd package
```

Para realizar una compilación y empaquetado en una sola instrucción:

```cmd
mvnw.cmd clean package
```

Si se completa correctamente, comprueba el artefacto:

```cmd
dir target\*.jar
```

La clase de prueba incluida, `EpMsJavaAutenticacionUsuariosApplicationTests`, contiene una prueba `contextLoads()`; **no constituye por sí sola una suite de pruebas funcionales de todos los endpoints**. Las operaciones REST se deben demostrar adicionalmente mediante Postman.

## 11. Organización del repositorio y control de versiones

Se sigue una estructura de ramas orientada a organizar las funcionalidades:

- `main`: versión estable destinada a entrega.
- `develop`: integración de cambios.
- `feature/entidades-usuarios`: entidades y relaciones JPA.
- `feature/repositorios-usuarios`: repositorios de acceso a datos.
- `feature/servicios-usuarios`: lógica de negocio.
- `feature/controllers-usuarios`: controladores REST, cuando se integren.
- `feature/autenticacion-jwt`: rama sugerida únicamente si se implementa efectivamente esa funcionalidad.

El equipo incorpora las funcionalidades mediante **Pull Requests** hacia `develop` y, tras probar la versión integrada, hacia `main`. Se utilizan mensajes de commit descriptivos, por ejemplo `feat: implementar servicios de usuarios regiones y comunas`.

> La enumeración de ramas describe el flujo de trabajo; el README no certifica cuáles ramas o Pull Requests siguen existentes en GitHub al momento de la evaluación. Comprobarlos en la pestaña **Branches** y en el historial de **Pull requests**.

## 12. Consideraciones de seguridad y mejoras pendientes

El código adjunto permite identificar estos puntos que deben tratarse antes de utilizar el servicio como solución de autenticación completa:

1. **Contraseñas:** se almacenan como hashes de BCrypt en `UsuarioServiceImpl`, pero el campo `contrasena` tiene un getter público en `Usuario`. Hay que ocultar ese dato de las respuestas JSON, por ejemplo con un DTO de respuesta que no incluya contraseña ni hash.
2. **JWT:** hay dependencia de OAuth2 Resource Server, pero **no existen en el código adjunto un controlador de login, un emisor de tokens JWT ni una configuración completa de su validación**. No presentar estas funcionalidades como terminadas.
3. **Permisos y roles:** los valores `COMPRADOR` y `STAFF_DE_PUERTA` se almacenan en la entidad, pero esto no demuestra que estén implementadas reglas de autorización por rol en los endpoints.
4. **Base de datos:** el archivo `data/bdusuarios.mv.db` es local y puede contener registros de usuarios; no debe publicarse junto al código fuente.
5. **Validación de datos:** faltan validaciones declarativas completas de algunos campos de usuario.

### Exclusiones recomendadas en `.gitignore`

Asegúrate de incorporar al `.gitignore` del repositorio, como mínimo:

```gitignore
target/
data/
*.mv.db
*.trace.db
.env
.env.*
!.env.example
```

Si algún archivo sensible **ya fue incluido en Git**, agregarlo a `.gitignore` no lo elimina automáticamente del historial. Revisa el historial antes de hacer público el repositorio.

## 13. Guion de demostración para la evaluación

Para el video solicitado en la Evaluación Parcial N.º 2 (entre **3 y 8 minutos**):

1. Presentar EventPass, la finalidad de este microservicio y las capas de su código.
2. Mostrar `pom.xml` y `application.properties`, explicando Java, Spring Boot, JPA, H2 y puerto `8081`.
3. Mostrar las entidades `Region`, `Comuna` y `Usuario`, y sus relaciones JPA.
4. Ejecutar comandos Maven y mostrar el resultado del empaquetado `.jar`.
5. Explicar cómo clonar y arrancar el proyecto siguiendo este README.
6. Demostrar desde Postman las operaciones CRUD y al menos los casos de error previstos; mostrar los códigos HTTP y los datos persistidos.
7. Cerrar explicando el versionado Git y los límites de seguridad de la versión presentada.

El video debe emplear la voz de los integrantes, explicar las operaciones con orden técnico y mostrar resultados reales de las pruebas. No se deben presentar respuestas o pruebas que no se hayan ejecutado correctamente.

---

**Proyecto:** EventPass · **Microservicio:** Usuarios · **Asignatura:** Java: Diseño y Construcción de Soluciones Nativas en Nube (JVY0101).
