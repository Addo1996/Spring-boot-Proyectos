
package com.krakedev.proyectos.security;

import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

public class JwtUtil {

    private static final String SECRET = "MiClave123";
    private static final long EXPIRACION = 1000 * 60 * 60;

    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET);

    public static String generarToken(String username, String rol) {
        return JWT.create()
                .withSubject(username)
                .withClaim("rol", rol)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRACION))
                .sign(ALGORITHM);
    }

    public static DecodedJWT verificarToken(String token) {
        JWTVerifier verifier = JWT.require(ALGORITHM).build();
        return verifier.verify(token);
    }
}
