package com.nexus.nexus.controlador;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nexus.nexus.modelo.entidad.Cliente;
import com.nexus.nexus.modelo.entidad.Reserva;
import com.nexus.nexus.modelo.entidad.Viaje;
import com.nexus.nexus.modelo.entidad.Vuelo;
import com.nexus.nexus.modelo.servicio.IClienteServicio;
import com.nexus.nexus.modelo.servicio.IReservaServicio;
import com.nexus.nexus.modelo.servicio.IViajeServicio;
import com.nexus.nexus.modelo.servicio.IVueloServicio;

@Controller
@RequestMapping("/reserva")
public class ReservaControlador {

    @Autowired
    private IReservaServicio reservaServicio;

    @Autowired
    private IClienteServicio clienteServicio;

    @Autowired
    private IViajeServicio viajeServicio;
    
    @Autowired
    private IVueloServicio vueloServicio;

    @GetMapping("/listar")
    public String listarReservas(Model modelo) {
        modelo.addAttribute("titulo", "Listado de Reservas");
        modelo.addAttribute("reservas", reservaServicio.listarTodos());
        return "/reserva/listar";
    }

    @GetMapping("/comprar/{idViaje}")
    public String comprarViaje(@PathVariable Long idViaje, Model modelo) {
        Viaje viaje = viajeServicio.buscarPorId(idViaje);
        if (viaje == null) {
            modelo.addAttribute("mensaje", "Viaje no encontrado.");
            return "error/general";
        }
        modelo.addAttribute("titulo", "Información del Pasajero para Viaje");
        modelo.addAttribute("cliente", new Cliente());
        modelo.addAttribute("viaje", viaje);
        return "/reserva/informacion-cliente-form";
    }

    @GetMapping("/comprar/vuelo/{idVuelo}")
    public String comprarVuelo(@PathVariable Long idVuelo, Model modelo) {
        Vuelo vuelo = vueloServicio.buscarPorId(idVuelo);
        if (vuelo == null) {
            modelo.addAttribute("mensaje", "Vuelo no encontrado.");
            return "error/general";
        }
        modelo.addAttribute("titulo", "Información del Pasajero para Vuelo");
        modelo.addAttribute("cliente", new Cliente());
        modelo.addAttribute("vuelo", vuelo);
        return "/reserva/informacion-cliente-form";
    }
    
    @PostMapping({"/guardar-cliente/{idViaje}", "/guardar-cliente/vuelo/{idVuelo}"})
    public String guardarInformacionCliente(
            @ModelAttribute Cliente clienteForm,
            @PathVariable(name = "idViaje", required = false) Long idViaje,
            @PathVariable(name = "idVuelo", required = false) Long idVuelo,
            Model modelo,
            RedirectAttributes flash) {

        String numeroDocumentoForm = clienteForm.getNumeroDocumento().trim();
        String tipoDocumentoForm = clienteForm.getTipoDocumento().trim();
        String nombreForm = clienteForm.getNombre().trim().toLowerCase();
        String apellidoForm = clienteForm.getApellido().trim().toLowerCase();

        Optional<Cliente> clienteExistenteOpt = clienteServicio.buscarPorNumeroDocumentoYTipoDocumento(
                                                    numeroDocumentoForm, 
                                                    tipoDocumentoForm);

        Cliente clienteParaReserva;

        if (clienteExistenteOpt.isPresent()) {
            Cliente clienteExistente = clienteExistenteOpt.get();

            String nombreExistente = clienteExistente.getNombre().trim().toLowerCase();
            String apellidoExistente = clienteExistente.getApellido().trim().toLowerCase();

            if (nombreForm.equals(nombreExistente) && apellidoForm.equals(apellidoExistente)) {
                clienteExistente.setCorreoElectronico(clienteForm.getCorreoElectronico());
                clienteExistente.setDireccion(clienteForm.getDireccion());
                clienteExistente.setFechaNacimiento(clienteForm.getFechaNacimiento());
                clienteExistente.setTelefono(clienteForm.getTelefono());

                clienteServicio.guardar(clienteExistente);
                clienteParaReserva = clienteExistente;
                flash.addFlashAttribute("success", "Información del cliente actualizada (ya existía).");
            } else {
                flash.addFlashAttribute("error", 
                    "El " + clienteForm.getTipoDocumento() + " con número " + clienteForm.getNumeroDocumento() + 
                    " ya está registrado a nombre de '" + clienteExistente.getNombre() + 
                    "'. Por favor, verifique sus datos o contacte a soporte.");
                
                if (idViaje != null) {
                    modelo.addAttribute("viaje", viajeServicio.buscarPorId(idViaje));
                } else if (idVuelo != null) {
                    modelo.addAttribute("vuelo", vueloServicio.buscarPorId(idVuelo));
                }
                modelo.addAttribute("cliente", clienteForm);
                modelo.addAttribute("titulo", "Información del Pasajero");
                return "/reserva/informacion-cliente-form";
            }
        } else {
            // clienteForm.setFechaRegistro(LocalDate.now());
            clienteServicio.guardar(clienteForm);
            clienteParaReserva = clienteForm;
            flash.addFlashAttribute("success", "Información del cliente guardada correctamente (nuevo cliente).");
        }

        Reserva reserva = new Reserva();
        reserva.setCliente(clienteParaReserva);
        reserva.setEstadoReserva(Reserva.EstadoReserva.Pendiente);

        if (idViaje != null) {
            Viaje viaje = viajeServicio.buscarPorId(idViaje);
            if (viaje == null) {
                flash.addFlashAttribute("error", "Viaje no encontrado.");
                return "redirect:/viaje/listar";
            }
            reserva.setViaje(viaje);
            reserva.setFechaInicio(viaje.getFechaInicio());
            reserva.setFechaFin(viaje.getFechaFin());
            modelo.addAttribute("viaje", viaje);
            modelo.addAttribute("titulo", "Detalles de la Reserva de Viaje");
        } else if (idVuelo != null) {
            Vuelo vuelo = vueloServicio.buscarPorId(idVuelo);
            if (vuelo == null) {
                flash.addFlashAttribute("error", "Vuelo no encontrado.");
                return "redirect:/vuelo/listar";
            }
            reserva.setVuelo(vuelo);
            reserva.setFechaInicio(vuelo.getFechaSalida());
            reserva.setFechaFin(null);
            modelo.addAttribute("vuelo", vuelo);
            modelo.addAttribute("titulo", "Detalles de la Reserva de Vuelo");
        } else {
            flash.addFlashAttribute("error", "No se especificó un viaje o vuelo para la reserva.");
            return "redirect:/viaje/listar"; 
        }

        modelo.addAttribute("reserva", reserva);
        return "/reserva/detalles-reserva-form";
    }

    @PostMapping({"/guardar-detalles/{idViaje}", "/guardar-detalles/vuelo/{idVuelo}"})
    public String guardarDetallesReserva(
            @ModelAttribute Reserva reserva,
            @PathVariable(name = "idViaje", required = false) Long idViaje,
            @PathVariable(name = "idVuelo", required = false) Long idVuelo,
            Model modelo,
            RedirectAttributes flash) {

        Cliente clienteGuardado = clienteServicio.buscarPorId(reserva.getCliente().getIdCliente());
        if (clienteGuardado == null) {
             flash.addFlashAttribute("error", "Cliente no encontrado para la reserva.");
             return "redirect:/viaje/listar";
        }
        reserva.setCliente(clienteGuardado);

        if (idViaje != null) {
            Viaje viaje = viajeServicio.buscarPorId(idViaje);
            if (viaje == null) {
                flash.addFlashAttribute("error", "Viaje no encontrado para finalizar la reserva.");
                return "redirect:/viaje/listar";
            }
            reserva.setViaje(viaje);
            reserva.setFechaInicio(viaje.getFechaInicio());
            reserva.setFechaFin(viaje.getFechaFin());
            BigDecimal adultosBD = new BigDecimal(reserva.getCantidadAdultos());
            BigDecimal ninosBD = new BigDecimal(reserva.getCantidadNinos());
            BigDecimal precioAdultoBD = viaje.getPrecioAdulto(); 
            BigDecimal precioNinoBD = viaje.getPrecioNino();
            BigDecimal precioTotalViaje = 
                adultosBD.multiply(precioAdultoBD).add(ninosBD.multiply(precioNinoBD));
            reserva.setPrecioTotal(precioTotalViaje); 

        } else if (idVuelo != null) {
            Vuelo vuelo = vueloServicio.buscarPorId(idVuelo);
            if (vuelo == null) {
                flash.addFlashAttribute("error", "Vuelo no encontrado para finalizar la reserva.");
                return "redirect:/vuelo/listar";
            }
            reserva.setVuelo(vuelo);
            reserva.setFechaInicio(vuelo.getFechaSalida());
            reserva.setFechaFin(null);
            BigDecimal totalPasajerosBD = BigDecimal.valueOf(reserva.getCantidadAdultos() + reserva.getCantidadNinos());
            BigDecimal precioVueloBD = vuelo.getPrecio();
            BigDecimal precioTotalVuelo = totalPasajerosBD.multiply(precioVueloBD);
            reserva.setPrecioTotal(precioTotalVuelo); 

        } else {
            flash.addFlashAttribute("error", "No se pudo determinar si es un viaje o vuelo para finalizar la reserva.");
            return "redirect:/viaje/listar";
        }
        
        if (reserva.getEstadoReserva() == null) {
            reserva.setEstadoReserva(Reserva.EstadoReserva.Pendiente);
        }
        
        reserva.setEstadoReserva(Reserva.EstadoReserva.Pendiente);
        Reserva reservaGuardada = reservaServicio.guardar(reserva);
        flash.addFlashAttribute("success", "Reserva realizada con éxito!");
        return "redirect:/pago/formulario/" + reservaGuardada.getIdReserva();
    }

    @GetMapping("/confirmacion/{idReserva}")
    public String mostrarConfirmacionReserva(@PathVariable Long idReserva, Model modelo) {
        Reserva reserva = reservaServicio.buscarPorId(idReserva);
        if (reserva == null) {
            modelo.addAttribute("mensaje", "Reserva no encontrada.");
            return "error/general";
        }
        modelo.addAttribute("titulo", "Confirmación de Reserva");
        modelo.addAttribute("reserva", reserva);
        
        if (reserva.getViaje() != null) {
            modelo.addAttribute("viaje", reserva.getViaje());
        } else if (reserva.getVuelo() != null) {
            modelo.addAttribute("vuelo", reserva.getVuelo());
        }

        return "/reserva/confirmacion-reserva";
    }
    
    @GetMapping("/create")
    public String mostrarFormularioCrear(Model modelo) {
        Reserva reserva = new Reserva();
        List<Cliente> listaClientes = clienteServicio.listarTodos();
        List<Viaje> listaViajes = viajeServicio.listarTodos();
        List<Vuelo> listaVuelos = vueloServicio.listarTodos();

        modelo.addAttribute("reserva", reserva);
        modelo.addAttribute("clientes", listaClientes);
        modelo.addAttribute("viajes", listaViajes);
        modelo.addAttribute("vuelos", listaVuelos);
        modelo.addAttribute("titulo", "Crear Nueva Reserva");
        return "/reserva/form";
    }

    @PostMapping("/save")
    public String guardarReserva(@ModelAttribute Reserva reserva,
                                     @RequestParam("clienteId") Long clienteId,
                                     @RequestParam(value = "viajeId", required = false) Long viajeId,
                                     @RequestParam(value = "vueloId", required = false) Long vueloId) {
        Cliente cliente = clienteServicio.buscarPorId(clienteId.intValue());

        if (cliente != null) {
            reserva.setCliente(cliente);
            if (viajeId != null) {
                Viaje viaje = viajeServicio.buscarPorId(viajeId);
                if (viaje != null) {
                    reserva.setViaje(viaje);
                    reserva.setPrecioTotal(reservaServicio.calcularPrecioTotal(viaje, reserva.getCantidadAdultos(), reserva.getCantidadNinos()));
                    reserva.setFechaInicio(viaje.getFechaInicio());
                    reserva.setFechaFin(viaje.getFechaFin());
                } 
            } else if (vueloId != null) {
                Vuelo vuelo = vueloServicio.buscarPorId(vueloId);
                if (vuelo != null) {
                    reserva.setVuelo(vuelo);
                    Double precioTotalDouble = reservaServicio.calcularPrecioTotalVuelo(vuelo, reserva.getCantidadAdultos(), reserva.getCantidadNinos());
                    reserva.setPrecioTotal(BigDecimal.valueOf(precioTotalDouble));
                    reserva.setFechaInicio(vuelo.getFechaSalida());
                    reserva.setFechaFin(null);
                }
            }
            if (reserva.getEstadoReserva() == null) {
                reserva.setEstadoReserva(Reserva.EstadoReserva.Pendiente);
            }
            
            reservaServicio.guardar(reserva);
            return "redirect:/reserva/";
        } else {
            return "redirect:/reserva/create?error=notfound";
        }
    }

    @GetMapping("/edit/{id}")
    public String mostrarFormularioEditar(@PathVariable(value = "id") Long id, Model modelo) {
        Reserva reserva = reservaServicio.buscarPorId(id);
        List<Cliente> listaClientes = clienteServicio.listarTodos();
        List<Viaje> listaViajes = viajeServicio.listarTodos();
        List<Vuelo> listaVuelos = vueloServicio.listarTodos();

        if (reserva == null) {
            return "redirect:/reserva/";
        }

        modelo.addAttribute("reserva", reserva);
        modelo.addAttribute("clientes", listaClientes);
        modelo.addAttribute("viajes", listaViajes);
        modelo.addAttribute("vuelos", listaVuelos);
        modelo.addAttribute("titulo", "Editar Reserva");
        return "/reserva/form";
    }

    @PostMapping("/update")
    public String actualizarReserva(@ModelAttribute Reserva reserva,
                                         @RequestParam("clienteId") Long clienteId,
                                         @RequestParam(value = "viajeId", required = false) Long viajeId,
                                         @RequestParam(value = "vueloId", required = false) Long vueloId) {
        Cliente cliente = clienteServicio.buscarPorId(clienteId.intValue());

        if (cliente != null && reserva.getIdReserva() != null) {
            reserva.setCliente(cliente);
            if (viajeId != null) {
                Viaje viaje = viajeServicio.buscarPorId(viajeId);
                if (viaje != null) {
                    reserva.setViaje(viaje);
                    reserva.setPrecioTotal(reservaServicio.calcularPrecioTotal(viaje, reserva.getCantidadAdultos(), reserva.getCantidadNinos()));
                    reserva.setVuelo(null);
                    reserva.setFechaInicio(viaje.getFechaInicio());
                    reserva.setFechaFin(viaje.getFechaFin());
                }
            } else if (vueloId != null) {
                Vuelo vuelo = vueloServicio.buscarPorId(vueloId);
                if (vuelo != null) {
                    reserva.setVuelo(vuelo);
                    Double precioTotalDouble = reservaServicio.calcularPrecioTotalVuelo(vuelo, reserva.getCantidadAdultos(), reserva.getCantidadNinos());
                    reserva.setPrecioTotal(BigDecimal.valueOf(precioTotalDouble));
                    reserva.setFechaInicio(vuelo.getFechaSalida());
                    reserva.setFechaFin(null);
                    reserva.setViaje(null);
                }
            }
            if (reserva.getEstadoReserva() == null) {
                reserva.setEstadoReserva(Reserva.EstadoReserva.Pendiente); 
            }
            
            reservaServicio.guardar(reserva);
            return "redirect:/reserva/";
        } else {
            return "redirect:/reserva/edit/" + reserva.getIdReserva() + "?error=notfound";
        }
    }

    @GetMapping("/cancelar/{id}")
    public String cancelarReserva(@PathVariable(value = "id") Long id) {
        Reserva reserva = reservaServicio.buscarPorId(id);
        if (reserva != null) {
            reserva.setEstadoReserva(Reserva.EstadoReserva.Cancelada);
            reservaServicio.guardar(reserva);
        }
        return "redirect:/reserva/";
    }

    @GetMapping("/ver/{id}")
    public String verDetallesReserva(@PathVariable(value = "id") Long id, Model modelo) {
        Reserva reserva = reservaServicio.buscarPorId(id);
        if (reserva == null) {
            return "redirect:/reserva/listar";
        }
        modelo.addAttribute("reserva", reserva);
        return "/reserva/confirmacion-reserva";
    }
}