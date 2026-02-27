package com.nexus.nexus.controlador;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import com.nexus.nexus.modelo.entidad.Viaje;
import com.nexus.nexus.modelo.servicio.IViajeServicio;

@Controller
@RequestMapping("/viaje")
public class ViajeControlador {

    @Autowired
    private IViajeServicio viajeServicio;
    
    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("imagenPrincipal");
    }

    @GetMapping("/listar")
    public String listarViajes(
            @RequestParam(name = "origen", required = false) String origen,
            @RequestParam(name = "destino", required = false) String destino,
            @RequestParam(name = "fechaIda", required = false) String fechaIda,
            @RequestParam(name = "fechaVuelta", required = false) String fechaVuelta,
            @RequestParam(name = "adultos", required = false) Integer adultos,
            @RequestParam(name = "ninos", required = false) Integer ninos,
            Model modelo) {

        if (origen != null && !origen.isEmpty() && destino != null && !destino.isEmpty() && fechaIda != null && !fechaIda.isEmpty() && fechaVuelta != null && !fechaVuelta.isEmpty()) {
            return "redirect:/viaje/resultados?destino=" + destino + "&fechaInicioDesde=" + fechaIda + "&fechaInicioHasta=" + fechaVuelta;
        } else {
            List<Viaje> Viajes = viajeServicio.listarTodos();
            modelo.addAttribute("viajes", Viajes);
            return "viaje/listar";
        }
    }
    @GetMapping("/form")
    public String mostrarFormularioCrear(Model modelo) {
        Viaje viaje = new Viaje();
        modelo.addAttribute("viaje", viaje);
        modelo.addAttribute("titulo", "Crear Nuevo Viaje");
        return "/viaje/form";
    }

    @PostMapping("/save")
    public String guardarViaje(@ModelAttribute Viaje viaje,
                               @RequestParam("imagenPrincipal") MultipartFile imagenFile) {
        if (imagenFile != null && !imagenFile.isEmpty()) {
            try {
                String nombreArchivo = StringUtils.cleanPath(imagenFile.getOriginalFilename());
                
                Path rutaAbsoluta = Paths.get("src/main/resources/static/images/viajes/", nombreArchivo); 

                Files.copy(imagenFile.getInputStream(), rutaAbsoluta, StandardCopyOption.REPLACE_EXISTING);

                viaje.setImagenPrincipal(nombreArchivo);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (viaje.getIdViaje() != null && viaje.getImagenPrincipal() == null) {
            Viaje viajeExistente = viajeServicio.buscarPorId(viaje.getIdViaje());
            if (viajeExistente != null && viajeExistente.getImagenPrincipal() != null) {
                viaje.setImagenPrincipal(viajeExistente.getImagenPrincipal());
            }
        }
        /*System.out.println("Destino de viaje.getDestino() antes de guardar: " + viaje.getDestino());
        System.out.println("Descripcion de viaje.getDescripcion() antes de guardar: " + viaje.getDescripcion());
        System.out.println("FechaInicio de viaje.getFechaInicio() antes de guardar: " + viaje.getFechaInicio());
        System.out.println("FechaFin de viaje.getFechaFin() antes de guardar: " + viaje.getFechaFin());
        System.out.println("DuracionDias de viaje.getDuracionDias() antes de guardar: " + viaje.getDuracionDias());
        System.out.println("PrecioAdulto de viaje.getPrecioAdulto() antes de guardar: " + viaje.getPrecioAdulto());
        System.out.println("PrecioNino de viaje.getPrecioNino() antes de guardar: " + viaje.getPrecioNino());
        System.out.println("Disponibilidad de viaje.getDisponibilidad() antes de guardar: " + viaje.getDisponibilidad());
        System.out.println("Itinerario de viaje.getItinerario() antes de guardar: " + viaje.getItinerario());
        System.out.println("ImagenPrincipal de viaje.getImagenPrincipal() antes de guardar: " + viaje.getImagenPrincipal());*/
        viajeServicio.guardar(viaje);
        return "redirect:/viaje/listar";
    }

    @GetMapping("/edit/{id}")
    public String mostrarFormularioEditar(@PathVariable(value = "id") Long id, Model modelo) {
        Viaje viaje = viajeServicio.buscarPorId(id);
        if (viaje == null) {
            return "redirect:/viaje/";
        }
        modelo.addAttribute("viaje", viaje);
        modelo.addAttribute("titulo", "Editar Viaje");
        return "/viaje/form";
    }

    @GetMapping("/delete/{id}")
    public String eliminarViaje(@PathVariable(value = "id") Long id) {
        viajeServicio.eliminar(id);
        return "redirect:/viaje/listar";
    }

    // Métodos para las búsquedas
    @GetMapping("/buscar")
    public String mostrarFormularioBuscar() {
        return "/viaje/buscar";
    }

    @GetMapping("/resultados")
    public String mostrarResultadosBusqueda(@RequestParam(required = false) String destino,
                                             @RequestParam(required = false) String fechaInicioDesde,
                                             @RequestParam(required = false) String fechaInicioHasta,
                                             @RequestParam(required = false) String precioDesde,
                                             @RequestParam(required = false) String precioHasta,
                                             Model modelo) {

        List<Viaje> resultados = viajeServicio.listarTodos();

        if (destino != null && !destino.isEmpty()) {
            resultados = viajeServicio.buscarPorDestino(destino);
        } else if ((fechaInicioDesde != null && !fechaInicioDesde.isEmpty()) && (fechaInicioHasta != null && !fechaInicioHasta.isEmpty())) {
            resultados = viajeServicio.buscarPorRangoFechas(LocalDate.parse(fechaInicioDesde), LocalDate.parse(fechaInicioHasta));
        } else if ((precioDesde != null && !precioDesde.isEmpty()) && (precioHasta != null && !precioHasta.isEmpty())) {
            resultados = viajeServicio.buscarPorRangoPrecios(new BigDecimal(precioDesde), new BigDecimal(precioHasta));
        }

        modelo.addAttribute("viajes", resultados);
        modelo.addAttribute("criterios", "Resultados de la búsqueda");
        return "/viaje/listar";
    }
}