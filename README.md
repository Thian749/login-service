# Auth: Autenticacion con tokens JWT en Spring Boot con Spring Security

esta aplicacion es un proyecto integrador de todo el curso en el cual utiliza spring boot y spring security para asi implementar autenticacion y autorizacion en la pagina web. incluye acceso seguro registros de usuario y genera tokens JWT para la autenticacion.

## Características

- Registrar usuarios con sus respectivos roles (ROLE_USER, ROLE_ADMIN).
- Generación de tokens JWT para la autenticación.
- puntos de acceso seguros basados en roles.

## Tecnologías Utilizadas

- Spring Boot
- Spring Security
- Spring Data JPA
- JWT (JSON Web Tokens)

## Requisitos

- Java Development Kit (JDK) 8 o superior
- Apache Maven (para compilar y administrar el proyecto)
- Un entorno de desarrollo Java (como IntelliJ IDEA)

## Configuración

1. Clona este repositorio en tu máquina local:

   ```bash
   git clone https://github.com/Thian749/login-service.git
   ```  

2. Abre el proyecto en tu entorno de desarrollo.

3. Configura tu entorno de desarrollo para utilizar Java 8 o superior.

4. Compila el proyecto con Maven:

   ```bash
   mvn clean install
    ```
5. Ejecuta el proyecto con Maven:

   ```bash
   mvn spring-boot:run
   ```
6. Abre tu navegador web y accede a la URL

## Uso
Para registrarte como nuevo usuario, visita el punto de acceso /auth/addNewUser usando una solicitud POST y proporciona los detalles del usuario en el cuerpo de la solicitud.

Para autenticarte y obtener un token JWT, visita el punto de acceso /auth/generateToken usando una solicitud POST y proporciona las credenciales de usuario.

Accede a los puntos de acceso seguros como /auth/user/userProfile o /auth/admin/adminProfile incluyendo el token JWT en la cabecera de autorización.

Puedes usar la herramienta Postman para probar los puntos de acceso. Importa las colecciones en postman [Colecciones](Prueba%20de%20autenticacion.postman_collection.json) y las [variables](variables.postman_environment.json).

## Contribución
Si deseas contribuir a este proyecto, no dudes en crear un pull request, tu ayuda es muy apreciada.

## Licencia
Este proyecto está bajo la Licencia MIT. Consulta el archivo [LICENSE](LICENSE) para obtener más detalles.
