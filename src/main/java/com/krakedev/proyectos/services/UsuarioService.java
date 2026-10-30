
package com.krakedev.proyectos.services;

import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.krakedev.proyectos.entidades.Usuario;
import com.krakedev.proyectos.repositories.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario registrar(Usuario usuario) {

        // Asignar siempre el rol USER al registrarse
        usuario.setRol("USER");

        String claveCifrada = BCrypt.hashpw(
                usuario.getPassword(),
                BCrypt.gensalt(10)
        );

        usuario.setPassword(claveCifrada);

        return usuarioRepository.save(usuario);
    }

    public Usuario login(String username, String password) {
        Optional<Usuario> resultado =
                usuarioRepository.findByUsername(username);

        if (resultado.isPresent()) {
            Usuario usuario = resultado.get();

            if (BCrypt.checkpw(password, usuario.getPassword())) {
                return usuario;
            }
        }

        return null;
    }
}
