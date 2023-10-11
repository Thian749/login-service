//El controlador nos permiten recibir peticiones y por ende responderlas

package com.team4.demoauth.Controller;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")//Ruta de peticion
@RequiredArgsConstructor // Requerimiendo de todos los argumentos del constructor

public class AuthController {

    @PostMapping(value = "docente")
    public String docente(){
        return "Opciones para Docentes";
    }

    @PostMapping(value = "admin")
    public String admin(){
        return "Opciones para Admin";
    }

    @PostMapping(value = "coordinador")
    public String coordinador(){
        return "Opciones para coordinador";
    }

    @PostMapping(value = "addNewUser")
    public String addNewUser(){
        return "Registro para el publico endpoint";
    }

    @PostMapping(value = "welcome")
    public String welcome(){
        return "Inicio de pagina";
    }

    @PostMapping(value = "generateToken")
    public String generateToken(){
        return "Generador de tokens";
    }

}
