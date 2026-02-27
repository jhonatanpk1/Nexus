package com.nexus.nexus.controlador;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletResponse;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nexus.nexus.modelo.entidad.Reserva;
import com.nexus.nexus.modelo.servicio.TipoReporteServicio;

@Controller
@RequestMapping("/reporte")
public class ReporteControlador {

    @Autowired
    private TipoReporteServicio reporteServicio;

    @GetMapping("/menu")
    public String mostrarMenuReportes() {
        return "/reporte/menu";
    }

    @GetMapping("/ventasPorDestino")
    public String mostrarVentasPorDestino(Model modelo) {
        Map<String, Long> ventasPorDestino = reporteServicio.obtenerVentasPorDestino();
        modelo.addAttribute("ventasPorDestino", ventasPorDestino);
        modelo.addAttribute("titulo", "Ventas por Destino");
        return "/reporte/ventasPorDestino";
    }

    @GetMapping("/ventaPorFecha")
    public String mostrarVentasPorFecha(@RequestParam(value = "fechaInicio", required = false) String fechaInicioStr,
                                         @RequestParam(value = "fechaFin", required = false) String fechaFinStr,
                                         Model modelo) {
        if (fechaInicioStr != null && !fechaInicioStr.isEmpty() && fechaFinStr != null && !fechaFinStr.isEmpty()) {
            LocalDate fechaInicio = LocalDate.parse(fechaInicioStr);
            LocalDate fechaFin = LocalDate.parse(fechaFinStr);
            Map<LocalDate, Long> ventasPorFecha = reporteServicio.obtenerVentasPorFecha(fechaInicio, fechaFin);
            modelo.addAttribute("ventasPorFecha", ventasPorFecha);
            modelo.addAttribute("fechaInicio", fechaInicioStr);
            modelo.addAttribute("fechaFin", fechaFinStr);
            modelo.addAttribute("titulo", "Ventas por Fecha");
        } else {
            modelo.addAttribute("titulo", "Ventas por Fecha");
        }
        return "/reporte/ventaPorFecha";
    }

    @GetMapping("/estadisticas")
    public String mostrarEstadisticas(Model modelo) {
        long totalReservas = reporteServicio.obtenerTotalReservas();
        Map<Reserva.EstadoReserva, Long> reservasPorEstado = reporteServicio.obtenerReservasPorEstado();
        long totalClientes = reporteServicio.obtenerTotalClientes();
        Map<String, Long> destinosPopulares = reporteServicio.obtenerDestinosMasPopulares(5);
        modelo.addAttribute("totalReservas", totalReservas);
        modelo.addAttribute("reservasPorEstado", reservasPorEstado);
        modelo.addAttribute("totalClientes", totalClientes);
        modelo.addAttribute("destinosPopulares", destinosPopulares);
        modelo.addAttribute("titulo", "Estadísticas de la Aerolínea");
        return "/reporte/estadisticas";
    }
    
    @GetMapping("/api/reportes/estadisticas-data")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> obtenerDatosEstadisticas() {
        long totalReservas = reporteServicio.obtenerTotalReservas();
        Map<Reserva.EstadoReserva, Long> reservasPorEstado = reporteServicio.obtenerReservasPorEstado();
        long totalClientes = reporteServicio.obtenerTotalClientes();
        Map<String, Long> destinosPopularesMap = reporteServicio.obtenerDestinosMasPopulares(5);

        // Convertir el Map de destinos populares a una lista de objetos para facilitar el manejo en JS
        List<Map<String, Object>> destinosPopularesList = new ArrayList<>();
        for (Map.Entry<String, Long> entry : destinosPopularesMap.entrySet()) {
            Map<String, Object> destinoData = new HashMap<>();
            destinoData.put("destino", entry.getKey());
            destinoData.put("cantidad", entry.getValue());
            destinosPopularesList.add(destinoData);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("totalReservas", totalReservas);
        response.put("reservasPorEstado", reservasPorEstado);
        response.put("totalClientes", totalClientes);
        response.put("destinosPopulares", destinosPopularesList);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/generar-pdf/ventasPorDestino")
    public void generarPdfVentasPorDestino(HttpServletResponse response) throws IOException {
        Map<String, Long> ventasPorDestino = reporteServicio.obtenerVentasPorDestino();

        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);

            // Dibujar el título
            contentStream.beginText();
            contentStream.newLineAtOffset(100, 750); // Posición del título (x=100, y=750)
            contentStream.drawString("Reporte de Ventas por Destino");
            contentStream.endText();

            contentStream.setFont(PDType1Font.HELVETICA, 10);
            int y = 700;
            for (Map.Entry<String, Long> entry : ventasPorDestino.entrySet()) {
                String linea = "Destino: " + entry.getKey() + ", Ventas: " + entry.getValue();
                contentStream.beginText();
                contentStream.newLineAtOffset(100, y); // Posición de cada línea
                contentStream.drawString(linea);
                contentStream.endText();
                y -= 15;
            }
        }

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"ventas_por_destino.pdf\"");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        document.save(baos);
        document.close();

        response.getOutputStream().write(baos.toByteArray());
    }

    @GetMapping("/generar-csv/ventasPorDestino")
    public void generarCsvVentasPorDestino(HttpServletResponse response) throws IOException {
        Map<String, Long> ventasPorDestino = reporteServicio.obtenerVentasPorDestino();

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"ventas_por_destino.csv\"");

        try (Writer writer = new OutputStreamWriter(response.getOutputStream())) {
            writer.write("Destino,Ventas\n");
            for (Map.Entry<String, Long> entry : ventasPorDestino.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue() + "\n");
            }
        }
    }

    @GetMapping("/generar-pdf/ventaPorFecha")
    public void generarPdfVentasPorFecha(@RequestParam(value = "fechaInicio", required = false) String fechaInicioStr,
                                          @RequestParam(value = "fechaFin", required = false) String fechaFinStr,
                                          HttpServletResponse response) throws IOException {
        if (fechaInicioStr != null && !fechaInicioStr.isEmpty() && fechaFinStr != null && !fechaFinStr.isEmpty()) {
            LocalDate fechaInicio = LocalDate.parse(fechaInicioStr);
            LocalDate fechaFin = LocalDate.parse(fechaFinStr);
            Map<LocalDate, Long> ventasPorFecha = reporteServicio.obtenerVentasPorFecha(fechaInicio, fechaFin);

            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(100, 750);
                contentStream.drawString("Reporte de Ventas por Fecha");
                contentStream.endText();
                contentStream.setFont(PDType1Font.HELVETICA, 10);

                int y = 700;
                for (Map.Entry<LocalDate, Long> entry : ventasPorFecha.entrySet()) {
                    String linea = "Fecha: " + entry.getKey() + ", Ventas: " + entry.getValue();
                    contentStream.beginText();
                    contentStream.newLineAtOffset(100, y);
                    contentStream.drawString(linea);
                    contentStream.endText();
                    y -= 15;
                }
            }

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=\"ventas_por_fecha_" + fechaInicioStr + "_" + fechaFinStr + ".pdf\"");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);
            document.close();

            response.getOutputStream().write(baos.toByteArray());
        }
    }

    @GetMapping("/generar-csv/ventaPorFecha")
    public void generarCsvVentasPorFecha(@RequestParam(value = "fechaInicio", required = false) String fechaInicioStr,
                                          @RequestParam(value = "fechaFin", required = false) String fechaFinStr,
                                          HttpServletResponse response) throws IOException {
        if (fechaInicioStr != null && !fechaInicioStr.isEmpty() && fechaFinStr != null && !fechaFinStr.isEmpty()) {
            LocalDate fechaInicio = LocalDate.parse(fechaInicioStr);
            LocalDate fechaFin = LocalDate.parse(fechaFinStr);
            Map<LocalDate, Long> ventasPorFecha = reporteServicio.obtenerVentasPorFecha(fechaInicio, fechaFin);

            response.setContentType("text/csv");
            response.setHeader("Content-Disposition", "attachment; filename=\"ventas_por_fecha_" + fechaInicioStr + "_" + fechaFinStr + ".csv\"");

            try (Writer writer = new OutputStreamWriter(response.getOutputStream())) {
                writer.write("Fecha,Ventas\n");
                for (Map.Entry<LocalDate, Long> entry : ventasPorFecha.entrySet()) {
                    writer.write(entry.getKey() + "," + entry.getValue() + "\n");
                }
            }
        }
    }

    @GetMapping("/generar-pdf/estadisticas")
    public void generarPdfEstadisticas(HttpServletResponse response) throws IOException {
        long totalReservas = reporteServicio.obtenerTotalReservas();
        Map<Reserva.EstadoReserva, Long> reservasPorEstado = reporteServicio.obtenerReservasPorEstado();
        long totalClientes = reporteServicio.obtenerTotalClientes();
        Map<String, Long> destinosPopulares = reporteServicio.obtenerDestinosMasPopulares(5);

        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(100, 750);
            contentStream.drawString("Estadísticas de la Aerolínea");
            contentStream.endText();
            contentStream.setFont(PDType1Font.HELVETICA, 10);

            int y = 700;
            contentStream.beginText();
            contentStream.newLineAtOffset(100, y);
            contentStream.drawString("Total de Reservas: " + totalReservas);
            contentStream.endText();
            y -= 15;
            contentStream.beginText();
            contentStream.newLineAtOffset(100, y);
            contentStream.drawString("Reservas por Estado:");
            contentStream.endText();
            y -= 15;
            for (Map.Entry<Reserva.EstadoReserva, Long> entry : reservasPorEstado.entrySet()) {
                contentStream.beginText();
                contentStream.newLineAtOffset(120, y);
                contentStream.drawString("- " + entry.getKey() + ": " + entry.getValue());
                contentStream.endText();
                y -= 15;
            }
        }

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"estadisticas_aerolinea.pdf\"");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        document.save(baos);
        document.close();

        response.getOutputStream().write(baos.toByteArray());
    }

    @GetMapping("/generar-csv/estadisticas")
    public void generarCsvEstadisticas(HttpServletResponse response) throws IOException {
        long totalReservas = reporteServicio.obtenerTotalReservas();
        Map<Reserva.EstadoReserva, Long> reservasPorEstado = reporteServicio.obtenerReservasPorEstado();
        long totalClientes = reporteServicio.obtenerTotalClientes();
        Map<String, Long> destinosPopulares = reporteServicio.obtenerDestinosMasPopulares(5);

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"estadisticas_aerolinea.csv\"");

        try (Writer writer = new OutputStreamWriter(response.getOutputStream())) {
            writer.write("Reporte,Valor\n");
            writer.write("Total de Reservas," + totalReservas + "\n");
            writer.write("Estado Reserva,Cantidad\n");
            for (Map.Entry<Reserva.EstadoReserva, Long> entry : reservasPorEstado.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue() + "\n");
            }
            writer.write("Total de Clientes," + totalClientes + "\n");
            writer.write("Destino Popular,Cantidad\n");
            for (Map.Entry<String, Long> entry : destinosPopulares.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue() + "\n");
            }
        }
    }
}