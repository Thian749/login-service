package com.team4.demoauth.config;
import com.team4.demoauth.filter.JwtAuthFilter;
import com.team4.demoauth.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


    @Configuration // Indica que esta clase de configuracion tendra metodos con Bean que son objetos que requiere la aplicacion
    @EnableWebSecurity
    @EnableMethodSecurity
    @RequiredArgsConstructor
    public class SecurityConfig {

        @Autowired
        private JwtAuthFilter authFilter; // jwt auth filter to validate the token and extract the username from the token

        // This method is used to create a user and save it in the database
        @Bean
        public UserDetailsService userDetailsService() {
            return new UserInfoService();
        }

        // Configuring HttpSecurity to allow access to different endpoints
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            /*return http.csrf().disable()
                    .authorizeHttpRequests()
                    .requestMatchers("/auth/welcome", "/auth/addNewUser", "/auth/generateToken").permitAll()
                    .and()
                    .authorizeHttpRequests().requestMatchers("/auth/user/**").authenticated()
                    .and()
                    .authorizeHttpRequests().requestMatchers("/auth/admin/**").authenticated()
                    .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .authenticationProvider(authenticationProvider())
                    .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                    .build();*/
            return http
                    .csrf(csrf ->
                            csrf
                            .disable()) //Cross-site request forgery, con esto desabilitaremos la peticion de un token que no es creado por nosotros
                    .authorizeHttpRequests(authRequest ->
                            authRequest
                                    .requestMatchers("/auth/welcome","/auth/addNewUser","/auth/generateToken").permitAll() //Como es publico se da permiso total
                                    .requestMatchers("auth/admin/**", "auth/docente/**","auth/coordinador/**").authenticated()//Esto causa que se pida para los usuarios de acceso publico que se autentifiquen
                    )
                    .formLogin(Customizer.withDefaults()) //Formulario de Login de Sprint Security por defecto
                    .build();


        }

        // Password Encoding using BCryptPasswordEncoder
        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        // This method is used to authenticate the user using DaoAuthenticationProvider
        @Bean
        public AuthenticationProvider authenticationProvider() {
            DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
            authenticationProvider.setUserDetailsService(userDetailsService());
            authenticationProvider.setPasswordEncoder(passwordEncoder());
            return authenticationProvider;
        }

        // This method is used to create an AuthenticationManager bean which is used to authenticate the user
        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
            return config.getAuthenticationManager();
        }
    }

