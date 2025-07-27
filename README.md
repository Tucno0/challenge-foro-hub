# 🌐 ForoHub - API REST

Una API REST completa para gestionar un foro en línea donde los usuarios pueden crear tópicos, responder preguntas y participar en discusiones organizadas por cursos.

## 📋 Tabla de Contenidos

- [Características](#-características)
- [Tecnologías](#️-tecnologías)
- [Requisitos Previos](#-requisitos-previos)
- [Instalación](#-instalación)
- [Configuración](#️-configuración)
- [Uso](#-uso)
- [Documentación de la API](#-documentación-de-la-api)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Autenticación](#-autenticación)
- [Base de Datos](#️-base-de-datos)
- [Ejemplos de Uso](#-ejemplos-de-uso)
- [Contribuciones](#-contribuciones)

## ✨ Características

- 🔐 **Autenticación JWT** - Sistema seguro de autenticación con tokens
- 👥 **Gestión de Usuarios** - Registro, actualización y administración de usuarios
- 🏷️ **Sistema de Perfiles** - Roles de usuario (ADMIN, USER)
- 📝 **Gestión de Tópicos** - Crear, leer, actualizar y eliminar tópicos
- 💬 **Sistema de Respuestas** - Responder a tópicos y marcar soluciones
- 📚 **Cursos Categorizados** - Organización por cursos y categorías
- 📖 **Documentación Interactive** - Swagger/OpenAPI integrado
- 🛡️ **Seguridad Avanzada** - Spring Security con encriptación BCrypt
- 🗄️ **Base de Datos PostgreSQL** - Persistencia robusta con migraciones Flyway

## 🛠️ Tecnologías

- **Java 21** - Lenguaje de programación
- **Spring Boot 3.5.3** - Framework principal
- **Spring Security** - Seguridad y autenticación
- **Spring Data JPA** - Persistencia de datos
- **PostgreSQL** - Base de datos
- **Flyway** - Migraciones de base de datos
- **JWT (Java-JWT 4.5.0)** - Autenticación basada en tokens
- **Lombok** - Reducción de código boilerplate
- **SpringDoc OpenAPI** - Documentación automática
- **Maven** - Gestión de dependencias

## 📋 Requisitos Previos

- Java 21 o superior
- Maven 3.6 o superior
- PostgreSQL 12 o superior
- Git

## 🚀 Instalación

1. **Clonar el repositorio**

   ```bash
   git clone <url-del-repositorio>
   cd challenge-foro-hub
   ```

2. **Configurar PostgreSQL**

   - Crear base de datos: `alura_foro_hub`
   - Configurar usuario y contraseña en `application.properties`

3. **Instalar dependencias**

   ```bash
   mvn clean install
   ```

4. **Ejecutar la aplicación**
   ```bash
   mvn spring-boot:run
   ```

La aplicación estará disponible en: `http://localhost:8080/api`

## ⚙️ Configuración

### Variables de Entorno

| Variable     | Descripción            | Valor por Defecto                  |
| ------------ | ---------------------- | ---------------------------------- |
| `JWT_SECRET` | Clave secreta para JWT | `12345678901234567890123456789012` |

### Base de Datos

Configura la conexión en `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/alura_foro_hub
spring.datasource.username=postgres
spring.datasource.password=tu_password
```

## 🎯 Uso

### Usuario por Defecto

La aplicación crea automáticamente un usuario administrador:

- **Email:** `admin@forohub.com`
- **Contraseña:** `admin123`
- **Perfil:** ADMIN

### Endpoints Principales

| Método | Endpoint          | Descripción       | Autenticación |
| ------ | ----------------- | ----------------- | ------------- |
| POST   | `/api/auth/login` | Iniciar sesión    | No            |
| GET    | `/api/topicos`    | Listar tópicos    | Sí            |
| POST   | `/api/topicos`    | Crear tópico      | Sí            |
| GET    | `/api/usuarios`   | Listar usuarios   | Sí (ADMIN)    |
| POST   | `/api/usuarios`   | Registrar usuario | No            |

## 📚 Documentación de la API

### Swagger UI (Recomendado)

```
http://localhost:8080/api/swagger-ui.html
```

### OpenAPI JSON

```
http://localhost:8080/api/v3/api-docs
```

## 📁 Estructura del Proyecto

```
src/main/java/com/jhampier/forohub/
├── 📁 configuration/     # Configuraciones (CORS)
├── 📁 controllers/       # Controladores REST
├── 📁 domain/           # Entidades de dominio
│   ├── 📁 curso/        # Gestión de cursos
│   ├── 📁 perfil/       # Perfiles de usuario
│   ├── 📁 respuesta/    # Respuestas a tópicos
│   ├── 📁 topico/       # Tópicos del foro
│   └── 📁 usuario/      # Gestión de usuarios
├── 📁 infra/           # Infraestructura
│   ├── 📁 exceptions/  # Manejo de errores
│   ├── 📁 security/    # Configuración de seguridad
│   └── 📁 springdoc/   # Documentación OpenAPI
└── Application.java     # Clase principal
```

## 🔐 Autenticación

### Flujo de Autenticación

1. **Login**: Enviar credenciales a `/api/auth/login`
2. **Token**: Recibir JWT token en la respuesta
3. **Autorización**: Incluir token en header: `Authorization: Bearer {token}`

### Ejemplo de Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "correoElectronico": "admin@forohub.com",
    "contrasena": "admin123"
  }'
```

## 🗄️ Base de Datos

### Modelo de Datos

- **Usuarios**: Información de usuarios con autenticación
- **Perfiles**: Roles del sistema (ADMIN, USER)
- **Tópicos**: Preguntas y discusiones del foro
- **Respuestas**: Respuestas a los tópicos
- **Cursos**: Categorización de tópicos

### Migraciones

Las migraciones se ejecutan automáticamente con Flyway:

- `V1__create-db.sql` - Estructura inicial
- `V2__insert-default-data.sql` - Datos por defecto

## 💡 Ejemplos de Uso

### Crear un Tópico

```bash
curl -X POST http://localhost:8080/api/topicos \
  -H "Authorization: Bearer {tu-token}" \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "¿Cómo usar Spring Security?",
    "mensaje": "Necesito ayuda con la configuración...",
    "cursoId": 1
  }'
```

### Listar Tópicos

```bash
curl -X GET http://localhost:8080/api/topicos \
  -H "Authorization: Bearer {tu-token}"
```

### Crear Respuesta

```bash
curl -X POST http://localhost:8080/api/respuestas \
  -H "Authorization: Bearer {tu-token}" \
  -H "Content-Type: application/json" \
  -d '{
    "mensaje": "Puedes usar Spring Boot Starter Security...",
    "topicoId": 1
  }'
```

## 🤝 Contribuciones

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver `LICENSE` para más detalles.

## 👨‍💻 Desarrollador

**Jhampier** - Desarrollador Principal

---

⭐ Si este proyecto te resultó útil, ¡no olvides darle una estrella!
