package com.nexus.nexus.modelo.servicio;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nexus.nexus.modelo.entidad.Usuario;
import com.nexus.nexus.modelo.repositorio.UsuarioRepositorio;



@Service
public class UsuarioServicio implements IUsuarioServicio {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Override
    public List<Usuario> listarTodos() {
        return usuarioRepositorio.findAll();
    }

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepositorio.findById(id);
    }

    @Override
    public Optional<Usuario> buscarPorUsuario(String usuario) {
        return usuarioRepositorio.findByUsuario(usuario);
    }

    @Override
    public void guardar(Usuario usuario) {
        usuarioRepositorio.save(usuario);
    }

    @Override
    public void eliminar(Long id) {
        usuarioRepositorio.deleteById(id);
    }

    @Override
	public Optional<Usuario> autenticarUsuario(String usuario, String contrasena) {
        Optional<Usuario> usuarioOptional = usuarioRepositorio.findByUsuario(usuario);
        if (usuarioOptional.isPresent()) {
            Usuario usuarioEncontrado = usuarioOptional.get();
            if (usuarioEncontrado.getContrasena().equals(contrasena)) {
                return usuarioOptional;
            }
        }
        return Optional.empty();
    }
}