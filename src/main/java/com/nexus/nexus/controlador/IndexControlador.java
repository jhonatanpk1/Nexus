package com.nexus.nexus.controlador;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.nexus.nexus.modelo.entidad.Viaje;
import com.nexus.nexus.modelo.entidad.Vuelo;
import com.nexus.nexus.modelo.servicio.IViajeServicio;
import com.nexus.nexus.modelo.servicio.IVueloServicio;



@Controller
public class IndexControlador {

	@Autowired
    private IVueloServicio vueloServicio;

    @Autowired
    private IViajeServicio viajeServicio;

    @GetMapping("/")
    public String home(Model model) {
        List<Vuelo> vuelos = vueloServicio.listarTodos();
        List<Viaje> viajes = viajeServicio.listarTodos();

        model.addAttribute("vuelos", vuelos);
        model.addAttribute("viajes", viajes);
        return "index";
    }

}