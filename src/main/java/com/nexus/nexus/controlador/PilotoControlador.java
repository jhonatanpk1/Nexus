package com.nexus.nexus.controlador;

import com.nexus.nexus.modelo.entidad.Piloto;
import com.nexus.nexus.modelo.servicio.IPilotoServicio;
import com.nexus.nexus.modelo.servicio.IViajeServicio;
import com.nexus.nexus.modelo.servicio.IVueloServicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/piloto")
public class PilotoControlador {

    @Autowired
    private IPilotoServicio pilotoServicio;
    
    @Autowired 
    private IVueloServicio vueloServicio;

    @Autowired 
    private IViajeServicio viajeServicio;

    @GetMapping("/listar")
    public String listarPilotos(Model model) {
        List<Piloto> pilotos = pilotoServicio.listarTodos();
        model.addAttribute("pilotos", pilotos);
        return "piloto/listar"; 
    }

    @GetMapping("/vuelo/{id}")
    public String mostrarVueloPiloto(@PathVariable Long id, Model model) {
        Optional<Piloto> pilotoOptional = pilotoServicio.obtenerPilotoConVuelo(id);

        if (pilotoOptional.isPresent()) {
            Piloto piloto = pilotoOptional.get();
            model.addAttribute("piloto", piloto);
            return "piloto/vuelo"; 
        } else {
            model.addAttribute("error", "Piloto no encontrado");
            return "redirect:/piloto/listar"; 
        }
    }

    @GetMapping("/crear")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("piloto", new Piloto()); 
        model.addAttribute("titulo", "Crear Nuevo Piloto");
        model.addAttribute("vuelosDisponibles", vueloServicio.listarTodos());
        model.addAttribute("viajesDisponibles", viajeServicio.listarTodos());
        return "piloto/form"; 
    }

    @PostMapping("/guardar")
    public String guardarPiloto(@ModelAttribute Piloto piloto) {
        pilotoServicio.guardar(piloto);
        return "redirect:/piloto/listar"; 
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Optional<Piloto> pilotoOptional = pilotoServicio.buscarPorId(id);
        if (pilotoOptional.isPresent()) {
            model.addAttribute("piloto", pilotoOptional.get());
            model.addAttribute("titulo", "Editar Piloto");
            model.addAttribute("vuelosDisponibles", vueloServicio.listarTodos());
            model.addAttribute("viajesDisponibles", viajeServicio.listarTodos());
            return "piloto/form"; 
        } else {
            model.addAttribute("error", "Piloto no encontrado");
            return "redirect:/piloto/listar"; 
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarPiloto(@PathVariable Long id) {
        pilotoServicio.eliminar(id);
        return "redirect:/piloto/listar"; 
    }
}