package com.nexus.nexus.controlador;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nexus.nexus.modelo.entidad.Usuario;
import com.nexus.nexus.modelo.servicio.IUsuarioServicio;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private IUsuarioServicio usuarioServicio;

    @GetMapping("/login")
    public String mostrarFormularioLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam("username") String username,
                                 @RequestParam("password") String password,
                                 HttpSession session,
                                 Model model) {
        Optional<Usuario> usuarioOptional = usuarioServicio.autenticarUsuario(username, password);
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            session.setAttribute("usuarioId", usuario.getIdUsuario());
            session.setAttribute("rol", usuario.getRol().toString());
            return "redirect:/"; 
        } else {
            model.addAttribute("error", true);
            return "login"; 
        }
    }

    @PostMapping("/logout")  
    public String cerrarSesion(HttpSession session) {
        session.invalidate(); 
        return "redirect:/"; 
    }
}