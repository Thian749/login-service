//El controlador nos permiten recibir peticiones y por ende responderlas

package com.team4.demoauth.Controller;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/control")//Ruta de peticion
@RequiredArgsConstructor // Requerimiendo de todos los argumentos del constructor

public class AuthController {

    @PostMapping(value = "login")
    public String login(){
        return "Login para el publico endpoint";
    }

    @PostMapping(value = "register")
    public String register(){
        return "Registro para el publico endpoint";
    }
}
