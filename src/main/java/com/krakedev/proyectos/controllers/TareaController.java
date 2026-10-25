
package com.krakedev.proyectos.controllers;

import java.util.List;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

import com.krakedev.proyectos.entidades.Tarea;
import com.krakedev.proyectos.services.TareaService;

@RestController
@RequestMapping("/tareas")
public class TareaController {

	private final TareaService tareaService;

	public TareaController(TareaService tareaService) {
		super();
		this.tareaService = tareaService;
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	@GetMapping
	public ResponseEntity<?> listar() {
		try {
			List<Tarea> tareas = tareaService.listarTareas();
			return ResponseEntity.ok(tareas);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(e.getMessage());
		}
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<?> guardar(@RequestBody Tarea tarea) {
		try {
			Tarea guardada = tareaService.guardar(tarea);
			return ResponseEntity.status(HttpStatus.CREATED).body(guardada);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(e.getMessage());
		}
	}
}
