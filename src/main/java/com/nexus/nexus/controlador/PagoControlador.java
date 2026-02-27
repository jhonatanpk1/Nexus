package com.nexus.nexus.controlador;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional; 
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nexus.nexus.modelo.entidad.Cliente;
import com.nexus.nexus.modelo.entidad.Pago;
import com.nexus.nexus.modelo.entidad.Pago.TipoPago;
import com.nexus.nexus.modelo.entidad.Reserva;
import com.nexus.nexus.modelo.servicio.IPagoServicio;
import com.nexus.nexus.modelo.servicio.IReservaServicio;
import com.nexus.nexus.modelo.servicio.EmailService;
import jakarta.mail.MessagingException;


@Controller
@RequestMapping("/pago")
public class PagoControlador {

    @Autowired
    private IPagoServicio pagoServicio;

    @Autowired
    private IReservaServicio reservaServicio;
    
    @Autowired
    private EmailService emailService;

    @GetMapping("/listar")
    public String listarPagos(Model modelo) {
        List<Pago> listadoPago = pagoServicio.listarTodos();
        modelo.addAttribute("pagos", listadoPago);
        return "/pago/listar";
    }

    @GetMapping("/formulario/{idReserva}")
    public String iniciarPago(@PathVariable Long idReserva, Model modelo, RedirectAttributes redirectAttributes) {
        Reserva reserva = reservaServicio.buscarPorId(idReserva);

        if (reserva == null) {
            redirectAttributes.addFlashAttribute("mensajeError", "La reserva no existe.");
            return "redirect:/reserva/listar";
        }

        System.out.println("DEBUG INICIAR PAGO: Cargando formulario de pago para Reserva ID: " + idReserva);

        modelo.addAttribute("reserva", reserva);
        modelo.addAttribute("reservaId", idReserva);
        modelo.addAttribute("pago", new Pago());
        return "pago/form";
    }

    @PostMapping("/procesar/{idReserva}")
    public String procesarPago(@PathVariable Long idReserva,
                               @RequestParam Map<String, String> allParams,
                               @ModelAttribute Pago pago,
                               Model modelo,
                               RedirectAttributes redirectAttributes) {

        System.out.println("DEBUG PROCESAR PAGO: Procesando pago para Reserva ID: " + idReserva);
        System.out.println("DEBUG PROCESAR PAGO: Todos los parámetros recibidos: " + allParams);

        TipoPago tipoPagoRecibido = null;
        if (allParams.containsKey("tipoPago") && !allParams.get("tipoPago").isEmpty()) {
            try {
                tipoPagoRecibido = TipoPago.valueOf(allParams.get("tipoPago"));
            } catch (IllegalArgumentException e) {
                redirectAttributes.addFlashAttribute("mensajeError", "Método de pago inválido.");
                return "redirect:/pago/formulario/" + idReserva;
            }
        } else {
            redirectAttributes.addFlashAttribute("mensajeError", "Por favor, selecciona un método de pago.");
            return "redirect:/pago/formulario/" + idReserva;
        }

        if (pago.getTipoPago() == null) {
            pago.setTipoPago(tipoPagoRecibido);
        }
        Reserva reserva = reservaServicio.buscarPorId(idReserva);
        if (reserva == null) {
            redirectAttributes.addFlashAttribute("mensajeError", "La reserva no existe para procesar el pago.");
            return "redirect:/reserva/listar";
        }
        pago.setMonto(reserva.getPrecioTotal());


        try {
            // **AsegÃºrate de que este mÃ©todo devuelva el estado de pago APREBADO en la simulaciÃ³n**
        	Pago pagoGuardado = pagoServicio.realizarPagoYObtener(idReserva, tipoPagoRecibido, pago.getMonto()); 

            // 3. LÃ³GICA DE ENVÃ O DE CORREO
            if (pagoGuardado != null && pagoGuardado.getEstadoPago() == Pago.EstadoPago.Aprobado) {
                try {
                    // Necesitas los objetos Cliente y Reserva
                    Reserva reservaParaCorreo = pagoGuardado.getReserva(); 
                    Cliente cliente = reservaParaCorreo.getCliente(); 
                    
                    if (cliente != null && cliente.getCorreoElectronico() != null && !cliente.getCorreoElectronico().isEmpty()) {
                        String destino = cliente.getCorreoElectronico(); 
                        String nombreCliente = cliente.getNombre() + " " + cliente.getApellido();
                        
                        // Si no tienes un generador de PDF, esto es NULL.
                        byte[] archivoAdjuntoBytes = null; 
                        String nombreArchivo = "Tiquete_" + reservaParaCorreo.getIdReserva() + ".pdf";
                        
                        // Llamada al servicio con los 6 parÃ¡metros
                        emailService.enviarConfirmacionPago(destino, nombreCliente, reservaParaCorreo, pagoGuardado, archivoAdjuntoBytes, nombreArchivo);
                        
                        System.out.println("✅ Correo de confirmación enviado a: " + destino);
                    } else {
                        System.err.println("❌ ERROR: Cliente o Correo no encontrado para la reserva #" + idReserva);
                    }
                } catch (MessagingException e) {
                    System.err.println("❌ ERROR al enviar el correo de confirmación (MessagingException): " + e.getMessage());
                } catch (Exception e) {
                    System.err.println("❌ ERROR al obtener datos del cliente/Reserva (otra Exception): " + e.getMessage());
                }
            }
            redirectAttributes.addFlashAttribute("mensajeExito", "Pago realizado con éxito!");
            return "redirect:/pago/confirmacion?idPago=" + pagoGuardado.getIdPago();
        } catch (Exception e) {
            System.err.println("Error EXCEPCION al procesar el pago: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("mensajeError", "Error al procesar el pago: " + e.getMessage());
            return "redirect:/pago/formulario/" + idReserva;
        }
    }

    @GetMapping("/id/{idPago}")
    public ResponseEntity<?> obtenerPago(@PathVariable Long idPago) {
        Pago pago = pagoServicio.buscarPorId(idPago);
        if (pago == null) {
            return new ResponseEntity<>("Pago no encontrado", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(pago, HttpStatus.OK);
    }

    @PutMapping("/{idPago}/estado")
    public ResponseEntity<?> actualizarEstado(@PathVariable Long idPago, @RequestParam String estado) {
        pagoServicio.actualizarEstadoPago(idPago, estado);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/confirmacion")
    public String confirmacionPago(@RequestParam(name = "idReserva", required = false) Long idReserva,
                                   @RequestParam(name = "idPago", required = false) Long idPago, 
                                   Model model) {
        Reserva reserva = null;
        Pago pago = null;

        if (idReserva != null) {
            reserva = reservaServicio.buscarPorId(idReserva);
            model.addAttribute("reserva", reserva);List<Pago> pagosDeReserva = pagoServicio.buscarPorReserva(reserva);
            if (!pagosDeReserva.isEmpty()) {pagosDeReserva.sort((p1, p2) -> p2.getFechaPago().compareTo(p1.getFechaPago()));
                pago = pagosDeReserva.get(0); 
            }
        } else if (idPago != null) {
            pago = pagoServicio.buscarPorId(idPago);
            if (pago != null) {
                reserva = pago.getReserva();
            }
        }

        if (reserva == null && pago == null) {
            model.addAttribute("mensajeError", "Información de confirmación no disponible.");
        }

        model.addAttribute("pago", pago);
        model.addAttribute("reserva", reserva);
        return "pago/confirmacion";
    }

    @GetMapping("/reserva/listar")
    public String listarReservas(Model model) {
        List<Reserva> reservas = reservaServicio.listarTodos();
        model.addAttribute("reservas", reservas);
        return "reserva/listar";
    }
}