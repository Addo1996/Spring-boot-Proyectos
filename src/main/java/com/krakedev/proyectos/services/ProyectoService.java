
package com.krakedev.proyectos.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.krakedev.proyectos.entidades.Proyecto;
import com.krakedev.proyectos.repositories.ProyectoRepository;

@Service
public class ProyectoService {

	private final ProyectoRepository proyectoRepository;

	public ProyectoService(ProyectoRepository proyectoRepository) {
		super();
		this.proyectoRepository = proyectoRepository;
	}

	public List<Proyecto> listarProyectos() {
		return proyectoRepository.findAll();
	}

	public Optional<Proyecto> buscarProyectoPorId(int id) {
		return proyectoRepository.findById(id);
	}

	public Proyecto guardarProyecto(Proyecto proyecto) {
		return proyectoRepository.save(proyecto);
	}

	public Proyecto actualizarProyecto(Proyecto proyecto) {
		return proyectoRepository.save(proyecto);
	}

	public void eliminarProyecto(int id) {
		proyectoRepository.deleteById(id);
	}
}
