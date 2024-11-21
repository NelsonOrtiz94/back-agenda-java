# Agendamiento de Eventos - Backend

## Descripción
Este proyecto es el backend de una aplicación de gestión de eventos que permite crear, editar, eliminar y notificar eventos a los usuarios. Está desarrollado con Java y Spring Boot, siguiendo buenas prácticas de arquitectura en capas y utilizando una base de datos PostgreSQL para el almacenamiento de datos.

El backend expone una API REST para interactuar con el frontend y realizar operaciones CRUD, además de notificar eventos importantes por correo electrónico utilizando OAuth2 para Gmail.

## Características
- CRUD de Eventos: Crear, leer, actualizar y eliminar eventos.
- Notificaciones por Correo: Envío de notificaciones a los usuarios mediante la API de Gmail con autenticación OAuth2.
- Documentación: Integración con Swagger para documentar y probar los endpoints.
- Manejo de Errores: Implementación de controladores de errores para respuestas consistentes.
- Seguridad: Configuración básica para proteger los endpoints con un archivo ```.env```

## Tecnologías Utilizadas
- **Java**: Lenguaje principal.
- **Spring Boot**: Framework para el desarrollo del backend.
  - **Spring Data JPA**: Manejo de datos.
  - **Spring Web**: Creación de la API REST.
  - **Spring Mail**: Envío de correos electrónicos.
- **PostgreSQL**: Base de datos relacional.
- **Swagger**: Documentación y pruebas de la API.
- **Docker**: Contenedorización opcional.
- **Maven**: Gestión de dependencias.
- **Google Gmail API: Para el envío de notificaciones.

## Estructura del Proyecto
```plaintext
src/
├── main/
│   ├── java/
│   │   └── com/agendaeventos/
│   │       ├── controller/         # Controladores REST
│   │       ├── service/            # Lógica de negocio
│   │       ├── repository/         # Repositorios JPA
│   │       ├── model/              # Entidades (clases de dominio)
│   │       └── config/             # Configuraciones (Swagger, Mail)
│   └── resources/
│       ├── application.properties  # Configuración de la aplicación
│       └── credentials.json        # Credenciales para OAuth2
└── test/                           # Pruebas unitarias
```
## Configuración de Seguridad con .env

Para proteger la información sensible como las credenciales de Google y las configuraciones del correo electrónico, se utiliza un archivo ```.env``` Este archivo debe incluir las siguientes variables de entorno:

```
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/agendaeventos
SPRING_DATASOURCE_USERNAME=tu_usuario
SPRING_DATASOURCE_PASSWORD=tu_contraseña
GOOGLE_CREDENTIALS={"installed":{"client_id":"tu_client_id","project_id":"tu_project_id","auth_uri":"https://accounts.google.com/o/oauth2/auth","token_uri":"https://oauth2.googleapis.com/token","auth_provider_x509_cert_url":"https://www.googleapis.com/oauth2/v1/certs","client_secret":"tu_client_secret","redirect_uris":["http://localhost:8080/oauth2/callback"]}}
SPRING_MAIL_USERNAME=correo@example.com
SPRING_MAIL_PASSWORD=tu_contraseña

```

## Configuración de Autenticación OAuth2 para Gmail

Paso 1: Configurar el Proyecto en Google Cloud Console

- Ve a la Google Cloud Console.
- Habilita la API de Gmail desde la sección de "APIs y servicios".
- Crea credenciales OAuth2:
   - Tipo: ID de cliente de OAuth.
    - URI de redirección autorizado: ```http://localhost:8080/oauth2/callback```.
- Descarga el archivo ```GOOGLE_CREDENTIALS``` y colócalo en ```resources```.

Paso 2: Configuración en el Código

- Modifica la clase ```GmailService``` para usar ```GOOGLE_CREDENTIALS```.

  ```
  private static final String REDIRECT_URI = "http://localhost:8080/oauth2/callback";

  private static Credential authorize(final NetHttpTransport httpTransport) throws IOException {
    GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new StringReader(System.getenv("GOOGLE_CREDENTIALS")));
    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
            httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
            .setAccessType("offline")
            .build();
    LocalServerReceiver receiver = new LocalServerReceiver.Builder()
            .setPort(8080)
            .setCallbackPath("/oauth2/callback")
            .build();
    return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
}

  ```

- Asegúrate de que el ```Redirect URI``` configurado coincida con Google Cloud Console:

```
private static final String CREDENTIALS_FILE_PATH = "GOOGLE_CREDENTIALS";
private static final String REDIRECT_URI = "http://localhost:8080/oauth2/callback";
```

Configura el receptor local para la autenticación:

```
LocalServerReceiver receiver = new LocalServerReceiver.Builder()
    .setPort(8080)
    .setCallbackPath("/oauth2/callback")
    .build();
```

## Instalación y Configuración

Requisitos

- Java 17
- Maven
- PostgreSQL
- Docker (opcional)
- Google Cloud Console API configurada para OAuth2
  
## Configuración

Clona el repositorio:

```plaintext
git clone https://github.com/NelsonOrtiz94/back-agenda-java.git
```

## Configura las variables de entorno o modifica application.properties para la conexión con PostgreSQL:

```plaintext
spring.datasource.url=jdbc:postgresql://localhost:5432/agendaeventos
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
spring.jpa.hibernate.ddl-auto=update
spring.mail.host=smtp.example.com
spring.mail.port=587
spring.mail.username=correo@example.com
spring.mail.password=tu_contraseña
```

## Ejecuta la aplicación:

```plaintext
mvn spring-boot:run
```
La API estará disponible en http://localhost:8080.

Accede a la documentación de Swagger en:
```plaintext
http://localhost:8080/swagger-ui/index.html
```

## Endpoints

Gestión de Eventos

- GET /api/eventos: Obtener todos los eventos.
- GET /api/eventos/{id}: Obtener un evento por ID.
- POST /api/eventos: Crear un nuevo evento.
- PUT /api/eventos/{id}: Actualizar un evento existente.
- DELETE /api/eventos/{id}: Eliminar un evento.
  
## Notificaciones

- POST /api/eventos/{id}/notificar: Enviar una notificación sobre un evento.

## Documentación
- GET /swagger-ui/index.html: Documentación interactiva de los endpoints.
  
## Base de Datos

El esquema principal incluye las siguientes tablas:

- eventos:
    id: Identificador único.
    tipo: Tipo del evento.
    encargado: Persona responsable del evento.
    fechaHora: Fecha y hora del evento.
    ubicacion: Ubicación del evento.
  
## Script de inicialización

```plaintext
CREATE TABLE eventos (
    id SERIAL PRIMARY KEY,
    tipo VARCHAR(255) NOT NULL,
    encargado VARCHAR(255) NOT NULL,
    fechaHora TIMESTAMP NOT NULL,
    ubicacion VARCHAR(255) NOT NULL
);
```

## Pruebas
Ejecuta las pruebas unitarias con:

```plaintext
mvn test
```

## Docker
Puedes utilizar Docker para ejecutar la aplicación fácilmente:

Crea el archivo Dockerfile:

```plaintext
FROM openjdk:17-jdk-slim
COPY target/agenda-eventos-backend.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

Construye la imagen:

```plaintext
docker build -t agenda-eventos-backend .
```

Ejecuta el contenedor:

```plaintext
docker run -p 8080:8080 --env-file .env agenda-eventos-backend
```

## Licencia
Este proyecto está bajo la Licencia MIT. Consulta el archivo LICENSE para más detalles.
