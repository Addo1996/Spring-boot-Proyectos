
package com.krakedev.proyectos.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.krakedev.proyectos.entidades.Empleado;
import com.krakedev.proyectos.entidades.Proyecto;
import com.krakedev.proyectos.entidades.Tarea;
import com.krakedev.proyectos.repositories.EmpleadoRepository;
import com.krakedev.proyectos.repositories.ProyectoRepository;
import com.krakedev.proyectos.repositories.TareaRepository;

@Service
public class TareaService {

	private final TareaRepository repository;
	private final ProyectoRepository proyectoRepository;
	private final EmpleadoRepository empleadoRepository;

	public TareaService(TareaRepository repository,
			ProyectoRepository proyectoRepository,
			EmpleadoRepository empleadoRepository) {
		super();
		this.repository = repository;
		this.proyectoRepository = proyectoRepository;
		this.empleadoRepository = empleadoRepository;
	}

	public Tarea guardar(Tarea tarea) {

		Proyecto proyecto = proyectoRepository
				.findById(tarea.getProyecto().getId())
				.orElseThrow(() -> new RuntimeException("Proyecto no existe"));

		List<Empleado> empleadosDB = new ArrayList<>();

		for (Empleado empleado : tarea.getEmpleados()) {

			Empleado empleadoReal = empleadoRepository
					.findById(empleado.getId())
					.orElseThrow(() -> new RuntimeException("Empleado no existe"));

			empleadosDB.add(empleadoReal);
		}

		tarea.setProyecto(proyecto);
		tarea.setEmpleados(empleadosDB);

		return repository.save(tarea);
	}

	public List<Tarea> listarTareas() {
		return repository.findAll();
	}

	public Tarea buscar(int id) {
		Optional<Tarea> resultado = repository.findById(id);
		return resultado.orElse(null);
	}
	
	public Optional<Tarea> buscarTareaPorId(int id) {
		return Optional.ofNullable(repository.findById(id).orElse(null));
	}
	
	public void eliminarTarea(int id) {
		repository.deleteById(id);
	}
}
