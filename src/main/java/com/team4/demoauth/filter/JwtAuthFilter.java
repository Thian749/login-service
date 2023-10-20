package com.team4.demoauth.filter;

import com.team4.demoauth.entity.UserInfo;
import com.team4.demoauth.service.UserInfoService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService; // este Servicio de token jwt es para validar el token y poder extraer el nombre de usuario del mismo.

    @Autowired
    private UserInfoService userInfoService;// este servicio sirve cargar los detalles del usuario.

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        }

    }

