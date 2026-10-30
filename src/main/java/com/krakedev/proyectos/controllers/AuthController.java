
package com.krakedev.proyectos.controllers;

import java.util.HashMap;


import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

import com.krakedev.proyectos.entidades.Usuario;
import com.krakedev.proyectos.security.JwtUtil;
import com.krakedev.proyectos.services.TokenBlacklistService;
import com.krakedev.proyectos.services.UsuarioService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final UsuarioService usuarioService;
	private final TokenBlacklistService blacklistService;

	public AuthController(UsuarioService usuarioService, TokenBlacklistService blacklistService) {
		this.usuarioService = usuarioService;
		this.blacklistService = blacklistService;
	}

	@PostMapping("/registrar")
	public ResponseEntity<Map<String, String>> registrar(@RequestBody Usuario usuario) {

	    Usuario usuarioGuardado = usuarioService.registrar(usuario);

	    Map<String, String> respuesta = new HashMap<>();

	    respuesta.put("mensaje", "Usuario registrado correctamente");
	    respuesta.put("username", usuarioGuardado.getUsername());
	    respuesta.put("rol", usuarioGuardado.getRol());

	    return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
	}

	@PostMapping("/login")
	public ResponseEntity<Map<String, String>> login(@RequestBody Usuario datos) {
		Usuario usuario = usuarioService.login(datos.getUsername(), datos.getPassword());

		if (usuario == null) {
			Map<String, String> respuesta = new HashMap<>();
			respuesta.put("mensaje", "Usuario o contraseña incorrectos");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
		}

		String token = JwtUtil.generarToken(usuario.getUsername(), usuario.getRol());

		Map<String, String> respuesta = new HashMap<>();
		respuesta.put("mensaje", "Autenticación correcta");
		respuesta.put("username", usuario.getUsername());
		respuesta.put("rol", usuario.getRol());
		respuesta.put("token", token);

		return ResponseEntity.ok(respuesta);
	}

	@PostMapping("/logout")
	public ResponseEntity<Map<String, String>> logout(
			@RequestHeader(value = "Authorization", required = false) String authHeader) {

		Map<String, String> respuesta = new HashMap<>();

		if (authHeader == null || !authHeader.startsWith("Bearer ") || authHeader.length() <= 7) {
			respuesta.put("mensaje", "Token no proporcionado");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
		}

		String token = authHeader.substring(7);
		blacklistService.invalidarToken(token);

		respuesta.put("mensaje", "Sesión cerrada correctamente");
		return ResponseEntity.ok(respuesta);
	}

	// Consultar los datos del usuario autenticado
	@GetMapping("/perfil")
	public ResponseEntity<Map<String, String>> obtenerPerfil(Authentication autenticacion) {

	    Map<String, String> respuesta = new HashMap<>();

	    respuesta.put("username", autenticacion.getName());
	    respuesta.put("mensaje", "Perfil obtenido correctamente");

	    return ResponseEntity.ok(respuesta);
	}

}
