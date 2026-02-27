package com.nexus.nexus.controlador;

import com.nexus.nexus.modelo.servicio.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.mail.MessagingException;
// IMPORTACIONES INNECESARIAS AQUÃ  (MimeMessage, MimeMessageHelper, StandardCharsets)
// Se quitan porque la lÃ³gica ya no estÃ¡ en el controlador:
// import jakarta.mail.internet.MimeMessage;
// import org.springframework.mail.javamail.MimeMessageHelper;
// import java.nio.charset.StandardCharsets; 

@Controller
@RequestMapping("/test/email")
public class EmailTestController {

    @Autowired
    private EmailService emailService;

    // 1. Muestra el formulario simple de prueba
    @GetMapping
    public String showEmailTestForm(Model model) {
        return "test-email"; 
    }
    
    // ⚠️ ELIMINADA: La funciÃ³n enviarTextoSimple que estaba aquÃ­ y causaba errores.
    
    // 2. Procesa el formulario y envÃ­a el correo
    @PostMapping("/send")
    public String sendSimpleEmail(
            @RequestParam("destino") String destino,
            @RequestParam("asunto") String asunto,
            @RequestParam("cuerpo") String cuerpo,
            Model model) {
        
        String remitente = "donchuchopropietario@gmail.com"; // El correo configurado
        
        try {
            // Llamamos al mÃ©todo que ahora estÃ¡ CORRECTAMENTE en el servicio
            emailService.enviarTextoSimple(destino, asunto, cuerpo);
            
            String mensajeExito = String.format("✅ Correo enviado con Ã©xito desde %s a %s. Asunto: %s", remitente, destino, asunto);
            model.addAttribute("resultado", mensajeExito);
            System.out.println(mensajeExito);
            
        } catch (MessagingException e) {
            String mensajeError = "❌ ERROR al enviar el correo: " + e.getMessage();
            model.addAttribute("error", mensajeError);
            System.err.println(mensajeError);
            e.printStackTrace();
        }
        
        // Vuelve a cargar el formulario con el resultado
        return "test-email";
    }
}