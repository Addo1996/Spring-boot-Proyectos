
package com.krakedev.proyectos.controllers;

import java.util.List;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

import com.krakedev.proyectos.entidades.Proyecto;
import com.krakedev.proyectos.services.ProyectoService;

@RestController
@RequestMapping("/proyectos")
public class ProyectoController {

	private final ProyectoService proyectoService;

	public ProyectoController(ProyectoService proyectoService) {
		super();
		this.proyectoService = proyectoService;
	}
    
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	@GetMapping
	public ResponseEntity<?> listar() {
		try {
			List<Proyecto> proyectos = proyectoService.listarProyectos();
			return ResponseEntity.ok(proyectos);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> buscar(@PathVariable int id) {
		try {
			Optional<Proyecto> resultado = proyectoService.buscarProyectoPorId(id);

			if (resultado.isEmpty()) {
				return ResponseEntity.notFound().build();
			}

			return ResponseEntity.ok(resultado.get());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<?> guardar(@RequestBody Proyecto proyecto) {
		try {
			Proyecto guardado = proyectoService.guardarProyecto(proyecto);
			return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> actualizar(@PathVariable int id, @RequestBody Proyecto datos) {
		try {
			Optional<Proyecto> resultado = proyectoService.buscarProyectoPorId(id);

			if (resultado.isEmpty()) {
				return ResponseEntity.notFound().build();
			}

			Proyecto proyecto = resultado.get();
			proyecto.setNombre(datos.getNombre());
			proyecto.setDescripcion(datos.getDescripcion());
			proyecto.setFechaInicio(datos.getFechaInicio());

			Proyecto actualizado = proyectoService.actualizarProyecto(proyecto);
			return ResponseEntity.ok(actualizado);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> eliminar(@PathVariable int id) {
		try {
			Optional<Proyecto> resultado = proyectoService.buscarProyectoPorId(id);

			if (resultado.isEmpty()) {
				return ResponseEntity.notFound().build();
			}

			proyectoService.eliminarProyecto(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
}
