package com.team4.demoauth.service;

import com.team4.demoauth.DTO.usuarioRegistroDTO;
import com.team4.demoauth.entity.Rol;
import com.team4.demoauth.entity.admin;
import com.team4.demoauth.repository.AdminRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class AdminService implements AdminRepository {
    private AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.AdminRepository = adminRepository;
    }
    @Override
    public Admin guardar(usuarioRegistroDTO registroDTO){
        admin admin =new admin(registroDTO.getNombre(), registroDTO.getApellido(), registroDTO.getEmail(), registroDTO.getPassword(), Arrays.asList(new Rol("ROLE_USER")));
        return AdminRepository.save("Usuario");
    }
}
