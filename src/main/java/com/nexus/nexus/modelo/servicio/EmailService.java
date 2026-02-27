package com.nexus.nexus.modelo.servicio;

import com.nexus.nexus.modelo.entidad.Reserva;
import com.nexus.nexus.modelo.entidad.Pago;
import com.nexus.nexus.modelo.entidad.Cliente;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    private final String REMITENTE = "nxxnsikakal@gmail.com"; 

    /**
     * Envía un correo de confirmación de pago.
     */
    public void enviarConfirmacionPago(
            String destino,
            String nombreCliente,
            Reserva reserva,
            Pago pago,
            byte[] archivoAdjuntoBytes, // PDF de tiquetes
            String nombreArchivo) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        // El 'true' despuÃ©s de message indica que es un mensaje multipart (permite adjuntos y HTML)
        MimeMessageHelper helper = new MimeMessageHelper(
            message,
            true, 
            StandardCharsets.UTF_8.name()
        );

        helper.setFrom(REMITENTE);
        helper.setTo(destino);
        helper.setSubject("✅ Confirmación de Reserva y Pago #" + reserva.getIdReserva());

        // 2. Procesamiento de la Plantilla Thymeleaf (email/confirmacion-pago.html)
        Context context = new Context(new Locale("es", "ES"));
        context.setVariable("nombreCliente", nombreCliente);
        context.setVariable("reserva", reserva);
        context.setVariable("pago", pago);

        String contenidoHTML = templateEngine.process("email/confirmacion-pago", context);
        
        // El segundo 'true' indica que el contenido es HTML
        helper.setText(contenidoHTML, true); 

        // 3. Adjuntar Archivo (Si existe)
        if (archivoAdjuntoBytes != null && nombreArchivo != null) {
            helper.addAttachment(nombreArchivo, new ByteArrayResource(archivoAdjuntoBytes));
        }

        mailSender.send(message);
    }
}