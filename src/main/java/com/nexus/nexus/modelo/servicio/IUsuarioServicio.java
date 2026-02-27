package com.nexus.nexus.modelo.servicio;

import java.util.List;
import java.util.Optional;

import com.nexus.nexus.modelo.entidad.Usuario;

public interface IUsuarioServicio {

    List<Usuario> listarTodos();

    Optional<Usuario> buscarPorId(Long id);

    Optional<Usuario> buscarPorUsuario(String usuario);

    void guardar(Usuario usuario);

    void eliminar(Long id);

    Optional<Usuario> autenticarUsuario(String usuario, String contrasena);
}