
package com.krakedev.proyectos.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter filtroAutenticacion;

    public SecurityConfig(JwtAuthenticationFilter filtroAutenticacion) {
        this.filtroAutenticacion = filtroAutenticacion;
    }

    @Bean
    public SecurityFilterChain configurarSeguridad(HttpSecurity http)
            throws Exception {

        http
            // Desactivar CSRF para esta API REST
            .csrf(csrf -> csrf.disable())

            // No mantener sesiones en el servidor
            .sessionManagement(sesion ->
                sesion.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // Definir las rutas públicas y protegidas
            .authorizeHttpRequests(autorizacion -> autorizacion
                .requestMatchers("/auth/registrar", "/auth/login").permitAll()
                .anyRequest().authenticated()
            )

            // Ejecutar el filtro JWT antes del filtro de autenticación
            .addFilterBefore(
                filtroAutenticacion,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}
