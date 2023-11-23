package com.team4.demoauth.Controller;

import com.team4.demoauth.entity.AuthRequest;
import com.team4.demoauth.entity.UserInfo;
import com.team4.demoauth.service.JwtService;
import com.team4.demoauth.service.TokenBlacklistService;
import com.team4.demoauth.service.UserInfoDetails;
import com.team4.demoauth.service.UserInfoService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;

import java.sql.*;


/**
 * Esta clase controla las acciones relacionadas con la autenticación de usuarios, la creación de perfiles y la
 * generación de tokens JWT para la autenticación en la aplicación. Los métodos están protegidos por anotaciones de
 * autorización para garantizar que solo los usuarios con los roles adecuados puedan acceder a ciertas rutas.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class UserController {

    private UserInfoService service; // inyecta una instancia del servicio UserInfoService, que se utiliza para gestionar la información del usuario en la base de datos.

    private JwtService jwtService; // Inyecta una instancia del servicio JwtService, que se utiliza para generar tokens JWT y gestionar la autenticación.

    private AuthenticationManager authenticationManager; // Inyecta el AuthenticationManager, que se utiliza para autenticar a los usuarios.

    /**
     * Este es el constructor de la clase UserController. Se utiliza para inyectar las dependencias de la clase.
     * @param service Es el servicio UserInfoService que se utiliza para gestionar la información del usuario en la base de datos
     * @param jwtService Es el servicio JwtService que se utiliza para generar tokens JWT y gestionar la autenticación
     * @param authenticationManager Es el AuthenticationManager que se utiliza para autenticar a los usuarios
     */
    @Autowired
    public UserController(UserInfoService service, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.service = service;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Este método maneja las solicitudes GET a "/auth/welcome" y simplemente devuelve un mensaje de bienvenida.
     * @return Un mensaje de bienvenida
     */
    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome this endpoint is not secure";
    }

    /**
     * Este método maneja las solicitudes POST a "/auth/addNewUser" y crea un nuevo usuario en la base de datos.
     * @param userInfo Es el objeto UserInfo que se va a guardar en la base de datos
     * @return Un mensaje de confirmación de que el usuario se ha creado correctamente
     */
    @PostMapping("/addNewUser")
    public String addNewUser(@RequestBody UserInfo userInfo) {
        return service.addUser(userInfo);
    }

    /**
     * Este método maneja las solicitudes GET a "/auth/userProfile" y devuelve un mensaje de bienvenida si el usuario
     * está autenticado. De lo contrario, devuelve un mensaje de error.
     * Este método está protegido por la anotación @PreAuthorize, que garantiza que solo los usuarios con el rol
     * "ROLE_USER" puedan acceder a este método.
     * @return Un mensaje de bienvenida si el usuario está autenticado
     */
    @GetMapping("/user/userProfile")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String userProfile() {
        return "Welcome to User Profile";
    }

    /**
     * Este método maneja las solicitudes GET a "/auth/adminProfile" y devuelve un mensaje de bienvenida si el usuario
     * está autenticado. De lo contrario, devuelve un mensaje de error.
     * Este método está protegido por la anotación @PreAuthorize, que garantiza que solo los usuarios con el rol
     * "ROLE_ADMIN" puedan acceder a este método.
     * @return Un mensaje de bienvenida si el usuario está autenticado
     */
    @GetMapping("/admin/adminProfile")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String adminProfile() {
        return "Welcome to Admin Profile";
    }

    /**
     * Este método maneja las solicitudes POST a "/auth/generateToken". Recibe un objeto AuthRequest en el cuerpo de la
     * solicitud (mediante la anotación @RequestBody). Luego, utiliza el AuthenticationManager para autenticar al
     * usuario mediante un nombre de usuario y contraseña. Si la autenticación es exitosa, se llama al servicio
     * JwtService para generar un token JWT y se devuelve ese token como respuesta. Si la autenticación falla, se lanza
     * una excepción UsernameNotFoundException.
     * @param authRequest Es el objeto AuthRequest que contiene el nombre de usuario y la contraseña del usuario
     * @return El token JWT generado
     */
    @PostMapping("/generateToken")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getUsername());
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }
    @GetMapping("/cerrar")
    public String cerrarSesion(HttpServletRequest request, HttpServletResponse response){
        /*
        Este metodo puede cerrar la sesion e invalidarla una vez la cierra para lograr una mejor seguridad y
        restriccion de datos este proceso se realiza por medio de setInvalidateHttpSession(true),
        al realizar el request y response se estan requiriendo cabeceras que se encuentran en el
        link sujeto al usuario para su cierre de session
        */
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.setInvalidateHttpSession(true); // Invalida la sesión actual
        logoutHandler.logout(request, response, SecurityContextHolder.getContext().getAuthentication());

        return "redirect:/welcome";
    }


    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
    HttpSession session = request.getSession(); // Obtiene la sesión actual del usuario a través del objeto request.
    session.invalidate(); // Invalida la sesión para cerrar la sesión actual del usuario
        Cookie cookie = new Cookie("myCookie", null);
        cookie.setMaxAge(0);  // Establece el tiempo de vida de la cookie a cero para eliminarla
        cookie.setPath("/");  // Asegura que la cookie se elimine en toda la aplicación
    return "redirect:/auth";// Redirige a la página de inicio de sesión o a donde desees después de cerrar la sesión.
    }

    @Autowired
    private TokenBlacklistService blacklistService;
    @Autowired
    private JwtService jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        // Intenta conectar con la base de datos SQLite
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:identifier.sqlite")) {

            // Prepara la consulta SQL
            String sql = "SELECT * FROM registro WHERE user = ? AND password = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);

                // Ejecuta la consulta
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        // Si se encuentra el usuario y la contraseña coinciden
                        String token = jwtService.generateToken(username);
                        return ResponseEntity.ok("Inicio de sesión exitoso. Token: " + token);
                    } else {
                        // Credenciales inválidas
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Nombre de usuario o contraseña incorrectos");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error en la base de datos");
        }
    }

}