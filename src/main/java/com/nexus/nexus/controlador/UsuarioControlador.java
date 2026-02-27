package com.nexus.nexus.controlador;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nexus.nexus.modelo.entidad.Usuario;
import com.nexus.nexus.modelo.servicio.IUsuarioServicio;

@Controller
@RequestMapping("/usuario")
public class UsuarioControlador {

    @Autowired
    private IUsuarioServicio usuarioServicio;

    @GetMapping("/listar")
    public String listarUsuarios(Model modelo) {
        List<Usuario> listadoUsuarios = usuarioServicio.listarTodos();
        modelo.addAttribute("usuarios", listadoUsuarios);
        return "/usuario/listar";
    }

    @GetMapping("/form")
    public String mostrarFormularioCrear(Model modelo) {
        modelo.addAttribute("usuario", new Usuario());
        modelo.addAttribute("roles", Usuario.Rol.values());
        modelo.addAttribute("titulo", "Crear Nuevo Usuario");
        return "/usuario/form";
    }

    @PostMapping("/save")
    public String guardarUsuario(@RequestParam(value = "idUsuario", required = false) Long idUsuario,
                                 @RequestParam String usuario,
                                 @RequestParam String contrasena,
                                 @RequestParam(required = false) String nombre,
                                 @RequestParam Usuario.Rol rol) {
        Usuario usuarioAGuardar = new Usuario();
        usuarioAGuardar.setIdUsuario(idUsuario); 
        usuarioAGuardar.setUsuario(usuario);
        usuarioAGuardar.setContrasena(contrasena);
        usuarioAGuardar.setNombre(nombre);
        usuarioAGuardar.setRol(rol);

        usuarioServicio.guardar(usuarioAGuardar);
        return "redirect:/usuario/listar";
    }

    @GetMapping("/edit/{id}")
    public String mostrarFormularioEditar(@PathVariable(value = "id") Long id, Model modelo) {
        Optional<Usuario> usuario = usuarioServicio.buscarPorId(id);
        if (usuario.isPresent()) {
            modelo.addAttribute("usuario", usuario.get());
            modelo.addAttribute("roles", Usuario.Rol.values());
            modelo.addAttribute("titulo", "Editar Usuario");
            return "/usuario/form";
        }
        return "redirect:/usuario/listar";
    }

    @GetMapping("/delete/{id}")
    public String eliminarUsuario(@PathVariable(value = "id") Long id) {
        usuarioServicio.eliminar(id);
        return "redirect:/usuario/listar";
    }
}