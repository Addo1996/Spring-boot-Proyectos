

package com.krakedev.proyectos.security;

import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

public class JwtUtil {

    private static final String CLAVE_SECRETA = "MiCl@ve123_EstaVa_A_SerUna_Clavelarga";
    private static final long TIEMPO_EXPIRACION = 1000 * 60 * 60;

    private static final Algorithm ALGORITMO =
            Algorithm.HMAC256(CLAVE_SECRETA);

    public static String generarToken(String nombreUsuario, String rol) {
        return JWT.create()
                .withSubject(nombreUsuario)
                .withClaim("rol", rol)
                .withIssuedAt(new Date())
                .withExpiresAt(
                        new Date(System.currentTimeMillis() + TIEMPO_EXPIRACION))
                .sign(ALGORITMO);
    }

    public static DecodedJWT verificarToken(String token) {
        JWTVerifier verificador = JWT.require(ALGORITMO).build();
        return verificador.verify(token);
    }
}

