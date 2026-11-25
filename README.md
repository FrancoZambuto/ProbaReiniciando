# TP Integrador DAOS - UTN FRSF

API REST de la inmobiliaria **HogarYA** (Actividad 02). Proyecto: `02-GarcesNievaReynosoZambuto`.

Integrantes:
- Zambuto, Franco
- Nieva, Florencia Belen
- Reynoso, Ariel
- Garces, Samanta

## Requisitos

- **JDK 17 o superior** (el proyecto usa Spring Boot 4). `JAVA_HOME` debe apuntar a ese JDK.
- **Maven 3.9+** (o el wrapper incluido: `mvnw` / `mvnw.cmd`).
- **MySQL 8** corriendo en `localhost:3306` con usuario `root` y contraseña `root`.
- Opcional: Eclipse STS, MySQL Workbench y Postman.

Si tu usuario o contraseña de MySQL son otros, cambialos en `src/main/resources/application.properties`:

```properties
spring.datasource.username=root
spring.datasource.password=root
```

## 1. Clonar el repositorio

```bash
git clone https://github.com/FrancoZambuto/ProbaReiniciando.git
cd ProbaReiniciando
```

## 2. Levantar la aplicación

**Desde Eclipse STS:** File > Import > Existing Maven Projects, seleccionar la carpeta del repo y luego
click derecho sobre el proyecto > Run As > Spring Boot App.

**Desde la terminal:**

```bash
mvn spring-boot:run
```

En Windows, si `JAVA_HOME` apunta a un JDK anterior al 17, en la misma terminal de PowerShell:

```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-21"
mvn spring-boot:run
```

La aplicación queda disponible en `http://localhost:8081` cuando aparece en consola
`Started GarcesNievaReynosoZambutoApplication`.

No hace falta crear la base de datos: al arrancar se crea el esquema `02-garcesnievareynosozambuto`
y Hibernate genera las tablas automáticamente.

Para compilar y correr los tests: `mvn clean package` (requiere MySQL levantado).

## 3. Cargar los datos de prueba

Con la aplicación levantada al menos una vez (para que existan las tablas), ejecutar
`scripts/datos_prueba.sql` sobre el esquema `02-garcesnievareynosozambuto`
(en MySQL Workbench: File > Open SQL Script, seleccionar el esquema y ejecutar).

El script carga personas, propiedades, contratos y el usuario para autenticarse en la API.

## 4. Autenticación (JWT)

Todos los endpoints requieren un token, salvo `/login` y Swagger.

1. Obtener el token:

   ```
   POST http://localhost:8081/login
   Content-Type: application/json

   { "username": "admin", "pass": "admin123" }
   ```

   Respuesta: `{ "token": "eyJ...", "tipo": "Bearer" }`. El token dura 1 hora.

2. Enviarlo en cada petición en el header:

   ```
   Authorization: Bearer <token>
   ```

   En Postman: pestaña **Authorization** > tipo **Bearer Token**.

Sin token (o con uno inválido o vencido) la API responde `401 Unauthorized`.

## 5. Documentación y pruebas

- **Swagger UI:** http://localhost:8081/swagger-ui/index.html
  Hacer el login desde la sección *Login*, copiar el token y pegarlo en el botón **Authorize** (sin escribir "Bearer").
- **Especificación OpenAPI (JSON):** http://localhost:8081/v3/api-docs
- **Casos de prueba (cURL):** carpeta `scripts/` (`casos_prueba_S01_facturas.txt`, `casos_prueba_S02_pagos.txt`, ...).

## Servicios

| Servicio | Endpoints |
|---|---|
| Login | `POST /login` |
| S01 Facturas de alquiler | `POST /facturas`, `GET /facturas/{id}`, `GET /facturas`, `PATCH /facturas/{id}/estado` |
| S02 Pagos | `POST /pagos`, `GET /pagos/{id}`, `GET /pagos` |
| S03 Incidentes | `POST /incidentes`, `GET /incidentes/{id}`, `GET /incidentes`, `PATCH /incidentes/{id}/estado`, `GET /incidentes/{id}/historial` |
| S04 Informes de resolución | `POST /incidentes/{id}/resolucion`, `GET /informes-resolucion/{id}`, `GET /informes-resolucion` |
| Consulta auxiliar | `GET /contratos/{id}` |

Los errores se devuelven con el formato `{ "message": ..., "status_code": ..., "uri": ... }`
(400 datos inválidos, 401 sin autenticación, 404 recurso inexistente, 409 conflicto con las reglas del negocio).
