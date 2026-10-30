package com.krakedev.proyectos.security;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

public class JwtUtil {

    // Registrar errores relacionados con el token
    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);

    // Clave utilizada para firmar y validar los tokens
    private static final String CLAVE_SECRETA = "EstaEsUnaClaveSuperSecretaMuyLarga123456789..";

    // Nombre de la aplicación que emite el token
    private static final String EMISOR = "krakeDevBackend";

    // Tiempo de expiración del token: una hora
    private static final long TIEMPO_EXPIRACION = 3600000;

    // Generar un token para el usuario autenticado
    public static String generarToken(String nombreUsuario, String rol) {

        // Crear el algoritmo de firma
        Algorithm algoritmo = Algorithm.HMAC256(CLAVE_SECRETA);

        // Calcular la fecha actual y la fecha de expiración
        long tiempoActual = System.currentTimeMillis();

        Date fechaExpiracion = new Date(tiempoActual + TIEMPO_EXPIRACION);

        // Crear el token con el usuario, las fechas y el rol
        String tokenGenerado = JWT.create()
                .withIssuer(EMISOR)
                .withSubject(nombreUsuario)
                .withIssuedAt(new Date(tiempoActual))
                .withExpiresAt(fechaExpiracion)
                .withClaim("rol", rol)
                .sign(algoritmo);

        return tokenGenerado;
    }

    // Validar la firma, el emisor y la fecha de expiración
    public static DecodedJWT verificarToken(String token) {

        try {
            // Configurar el verificador del token
            Algorithm algoritmo = Algorithm.HMAC256(CLAVE_SECRETA);

            JWTVerifier verificador = JWT.require(algoritmo)
                    .withIssuer(EMISOR)
                    .build();

            // Verificar el token y devolver sus datos
            DecodedJWT tokenDecodificado = verificador.verify(token);

            return tokenDecodificado;

        } catch (Exception e) {

            // Registrar el error si el token no es válido
            log.error("Error de validación del Token: {}", e.getMessage());

            return null;
        }
    }
}