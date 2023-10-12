package com.team4.demoauth.controller;

import com.team4.demoauth.DTO.usuarioRegistroDTO;
import com.team4.demoauth.service.AdminService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/registro")
public class RegistroUsuarioControlador {

    private AdminService adminService;

    public RegistroUsuarioControlador(AdminService adminService) {
        this.adminService = adminService;
    }

    @ModelAttribute("usuario")
    public usuarioRegistroDTO retortnarNuevoUsuarioRegistroDTO(){
        return new usuarioRegistroDTO();
    }
    @GetMapping
    public String mostrarFormularioDeRegistro(){
        return "registro";//esto debe retornar mas aelante a un html con el formulario
    }

    @PostMapping
    public String RegistrarCuentaDeUsuario(@ModelAttribute("usuario") usuarioRegistroDTO registroDTO){
        adminService.guardar(registroDTO);
        return "redirect:/registro?exitoAlGuardarElUsuario"
    }
}
