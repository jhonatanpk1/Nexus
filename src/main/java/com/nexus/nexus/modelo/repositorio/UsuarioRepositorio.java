package com.nexus.nexus.modelo.repositorio;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nexus.nexus.modelo.entidad.Usuario;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {

	// Buscar usuario por nombre de usuario
    Optional<Usuario> findByUsuario(String usuario);
}
