package com.nexus.nexus.controlador;

import com.nexus.nexus.modelo.entidad.Vuelo;
import com.nexus.nexus.modelo.servicio.IVueloServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;

@Controller
@RequestMapping("/vuelo")
public class VueloControlador {

    @Autowired
    private IVueloServicio vueloServicio;

    @GetMapping("/listar")
    public String listarVuelos(
            @RequestParam(name = "origen", required = false) String origen,
            @RequestParam(name = "destino", required = false) String destino,
            @RequestParam(name = "fechaIda", required = false) String fechaIda,
            @RequestParam(name = "adultos", required = false) Integer adultos,
            @RequestParam(name = "ninos", required = false) Integer ninos,
            Model modelo) {

        if (origen != null && !origen.isEmpty() && destino != null && !destino.isEmpty() && fechaIda != null && !fechaIda.isEmpty()) {
            return "redirect:/vuelo/resultados?" +
                   "origen=" + (origen != null ? origen : "") +
                   "&destino=" + (destino != null ? destino : "") +
                   "&fechaSalidaDesde=" + (fechaIda != null ? fechaIda : "");
        } else {
            List<Vuelo> listadoVuelos = vueloServicio.listarTodos();
            modelo.addAttribute("vuelos", listadoVuelos);
            modelo.addAttribute("titulo", "Listado de Vuelos");
            return "/vuelo/listar";
        }
    }

    @GetMapping("/form")
    public String mostrarFormularioCrear(Model modelo) {
        Vuelo vuelo = new Vuelo();
        modelo.addAttribute("vuelo", vuelo);
        modelo.addAttribute("titulo", "Crear Nuevo Vuelo");
        return "/vuelo/form";
    }

    @PostMapping("/save")
    public String guardarVuelo(@ModelAttribute Vuelo vuelo) {
        vueloServicio.guardar(vuelo);
        return "redirect:/vuelo/listar";
    }

    @GetMapping("/edit/{id}")
    public String mostrarFormularioEditar(@PathVariable(value = "id") Long id, Model modelo) {
        Vuelo vuelo = vueloServicio.buscarPorId(id);
        if (vuelo == null) {
            return "redirect:/vuelo/listar";
        }
        modelo.addAttribute("vuelo", vuelo);
        modelo.addAttribute("titulo", "Editar Vuelo");
        return "/vuelo/form";
    }

    @GetMapping("/delete/{id}")
    public String eliminarVuelo(@PathVariable(value = "id") Long id) {
        vueloServicio.eliminar(id);
        return "redirect:/vuelo/listar";
    }

    @GetMapping("/buscar")
    public String mostrarFormularioBuscar() {
        return "/vuelo/buscar";
    }

    @GetMapping("/resultados")
    public String mostrarResultadosBusqueda(@RequestParam(required = false) String numeroVuelo,
                                             @RequestParam(required = false) String origen,
                                             @RequestParam(required = false) String destino,
                                             @RequestParam(required = false) String fechaSalidaDesde,
                                             @RequestParam(required = false) String fechaSalidaHasta,
                                             @RequestParam(required = false) String horaSalidaDesde,
                                             @RequestParam(required = false) String horaSalidaHasta,
                                             @RequestParam(required = false) String precioDesde,
                                             @RequestParam(required = false) String precioHasta,
                                             Model modelo) {
        List<Vuelo> resultados = vueloServicio.listarTodos();

        if (numeroVuelo != null && !numeroVuelo.isEmpty()) {
            resultados = vueloServicio.buscarPorNumeroVuelo(numeroVuelo);
        } else if (origen != null && !origen.isEmpty() && destino != null && !destino.isEmpty() && fechaSalidaDesde != null && !fechaSalidaDesde.isEmpty()) {
            
            LocalDate fechaInicio = LocalDate.parse(fechaSalidaDesde);
            LocalDate fechaFin = YearMonth.from(fechaInicio).atEndOfMonth();
            resultados = vueloServicio.buscarPorOrigenIgnoreCaseContainingAndDestinoIgnoreCaseContainingAndFechaSalidaBetween(
                    origen, destino, fechaInicio, fechaFin
            );
        } else if (fechaSalidaDesde != null && !fechaSalidaDesde.isEmpty() && fechaSalidaHasta != null && !fechaSalidaHasta.isEmpty() && horaSalidaDesde != null && !horaSalidaDesde.isEmpty() && horaSalidaHasta != null && !horaSalidaHasta.isEmpty()) {
            resultados = vueloServicio.buscarPorRangoFechasYHoras(
                    LocalDate.parse(fechaSalidaDesde), LocalDate.parse(fechaSalidaHasta),
                    LocalTime.parse(horaSalidaDesde), LocalTime.parse(horaSalidaHasta)
            );
        } else if (fechaSalidaDesde != null && !fechaSalidaDesde.isEmpty() && fechaSalidaHasta != null && !fechaSalidaHasta.isEmpty()) {
            resultados = vueloServicio.buscarPorRangoFechas(LocalDate.parse(fechaSalidaDesde), LocalDate.parse(fechaSalidaHasta));
        } else if (horaSalidaDesde != null && !horaSalidaDesde.isEmpty() && horaSalidaHasta != null && !horaSalidaHasta.isEmpty()) {
            resultados = vueloServicio.buscarPorRangoHoras(LocalTime.parse(horaSalidaDesde), LocalTime.parse(horaSalidaHasta));
        } else if (origen != null && !origen.isEmpty()) {
            resultados = vueloServicio.buscarPorOrigen(origen);
        } else if (destino != null && !destino.isEmpty()) {
            resultados = vueloServicio.buscarPorDestino(destino);
        } else if (precioDesde != null && !precioDesde.isEmpty() && precioHasta != null && !precioHasta.isEmpty()) {
            resultados = vueloServicio.buscarPorRangoPrecios(new BigDecimal(precioDesde), new BigDecimal(precioHasta));
        } else if (precioDesde != null && !precioDesde.isEmpty()) {
            resultados = vueloServicio.buscarPorPrecioMayorOIgual(new BigDecimal(precioDesde));
        } else if (precioHasta != null && !precioHasta.isEmpty()) {
            resultados = vueloServicio.buscarPorPrecioMenorOIgual(new BigDecimal(precioHasta));
        }

        modelo.addAttribute("vuelos", resultados);
        modelo.addAttribute("criterios", "Resultados de la búsqueda");
        return "/vuelo/listar";
    }
}