
package com.krakedev.proyectos.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.krakedev.proyectos.entidades.Usuario;
import com.krakedev.proyectos.services.UsuarioService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/registrar")
    public ResponseEntity<Usuario> registrar(@RequestBody Usuario usuario) {
        Usuario usuarioGuardado = usuarioService.registrar(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioGuardado);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Usuario datos) {
        Usuario usuario = usuarioService.login(
                datos.getUsername(),
                datos.getPassword()
        );

        if (usuario == null) {
            Map<String, String> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Usuario o contraseña incorrectos");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
        }

        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Autenticación correcta");
        respuesta.put("username", usuario.getUsername());
        respuesta.put("rol", usuario.getRol());

        return ResponseEntity.ok(respuesta);
    }
}
