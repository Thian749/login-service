package com.team4.demoauth.repository;

import com.team4.demoauth.DTO.usuarioRegistroDTO;

public interface AdminRepository {
    public admin save(usuarioRegistroDTO registroDTO);
}
