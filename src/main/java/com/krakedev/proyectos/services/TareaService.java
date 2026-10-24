
package com.krakedev.proyectos.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.krakedev.proyectos.entidades.Tarea;
import com.krakedev.proyectos.repositories.TareaRepository;

@Service
public class TareaService {

	private final TareaRepository tareaRepository;

	public TareaService(TareaRepository tareaRepository) {
		super();
		this.tareaRepository = tareaRepository;
	}

	public List<Tarea> listarTareas() {
		return tareaRepository.findAll();
	}

	public Optional<Tarea> buscarTareaPorId(int id) {
		return tareaRepository.findById(id);
	}

	public Tarea guardarTarea(Tarea tarea) {
		return tareaRepository.save(tarea);
	}

	public Tarea actualizarTarea(Tarea tarea) {
		return tareaRepository.save(tarea);
	}

	public void eliminarTarea(int id) {
		tareaRepository.deleteById(id);
	}
}
