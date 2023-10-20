package com.team4.demoauth.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * DTO (Objeto de Transferencia de Datos) se usa para representar las solicitudes de autenticación que llegan a
 * la aplicación
 */

@Data
@Builder //Construccion de objetos
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {

    private String username;
    private String password;
    private String token;
}


