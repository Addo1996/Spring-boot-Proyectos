
package com.krakedev.proyectos.security;

import java.io.IOException;
import java.util.Collections;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.krakedev.proyectos.services.TokenBlacklistService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenBlacklistService servicioListaNegra;

    public JwtAuthenticationFilter(TokenBlacklistService servicioListaNegra) {
        this.servicioListaNegra = servicioListaNegra;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest solicitud,
            HttpServletResponse respuesta,
            FilterChain cadenaFiltros)
            throws ServletException, IOException {

        // Obtener el encabezado de autorización
        String encabezadoAutorizacion = solicitud.getHeader("Authorization");

        if (encabezadoAutorizacion != null
                && encabezadoAutorizacion.startsWith("Bearer ")) {

            String token = encabezadoAutorizacion.substring(7);

            // Rechazar los tokens incluidos en la lista negra
            if (servicioListaNegra.estaInvalidado(token)) {
                respuesta.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                respuesta.setContentType("application/json");
                respuesta.getWriter().write(
                        "{\"mensaje\":\"El token fue invalidado\"}");
                return;
            }

            try {
                // Verificar el token y obtener los datos del usuario
                DecodedJWT tokenDecodificado = JwtUtil.verificarToken(token);

                // Comprobar que el token sea válido
                if (tokenDecodificado == null) {
                    respuesta.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    respuesta.setContentType("application/json");
                    respuesta.getWriter().write(
                            "{\"mensaje\":\"Token inválido o expirado\"}");
                    return;
                }

                String nombreUsuario = tokenDecodificado.getSubject();
                String rolUsuario = tokenDecodificado.getClaim("rol").asString();

                // Registrar la autenticación en Spring Security
                UsernamePasswordAuthenticationToken autenticacion =
                        new UsernamePasswordAuthenticationToken(
                                nombreUsuario,
                                null,
                                Collections.singletonList(
                                        new SimpleGrantedAuthority("ROLE_" + rolUsuario)));

                SecurityContextHolder.getContext()
                        .setAuthentication(autenticacion);

            } catch (Exception e) {
                respuesta.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                respuesta.setContentType("application/json");
                respuesta.getWriter().write(
                        "{\"mensaje\":\"Error al validar el token\"}");
                return;
            }
        }

        cadenaFiltros.doFilter(solicitud, respuesta);
    }
}
