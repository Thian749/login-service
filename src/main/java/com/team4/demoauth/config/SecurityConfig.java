package com.team4.demoauth.config;
import com.team4.demoauth.filter.JwtAuthFilter;
import com.team4.demoauth.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
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


    @Configuration
    @EnableWebSecurity
    @EnableMethodSecurity
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
            return http.csrf().disable()
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
        protected void configure(HttpSecurity http) throws Exception {
            //eliminacion de secciones
            http
                    .authorizeRequests()
                    .antMatchers("/seccion-eliminar").hasRole("ADMIN") // Configura la URL de la sección que permite la eliminación y requiere un rol ADMIN.
                    .anyRequest().authenticated() // Todas las demás solicitudes requieren autenticación.
                    .and()
                    .formLogin() // Configura el formulario de inicio de sesión.
                    .loginPage("/login") // Página de inicio de sesión personalizada (opcional).
                    .permitAll() // Permite a todos acceder a la página de inicio de sesión.
                    .and()
                    .logout() // Configura la funcionalidad de cierre de sesión.
                    .permitAll(); // Permite a todos cerrar sesión.
           //manenejo de secciones

            https
                    .authorizeRequests()
                    .antMatchers("/seccion-privada").access("hasRole('USER') and isAccountNonExpired()")
                    .anyRequest().authenticated()
                    .and()
                    .formLogin()
                    .loginPage("/login")
                    .permitAll()
                    .and()
                    .logout()
                    .permitAll();
        }

        // Define una fuente de autenticación personalizada (puede ser una base de datos, LDAP, etc.).
        // @Override
        // protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //     auth.authenticationProvider(customAuthenticationProvider);
        // }
    }
}
