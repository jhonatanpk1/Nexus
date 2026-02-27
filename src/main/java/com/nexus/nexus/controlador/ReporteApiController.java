/*
Proyecto Nexus - Sistema de Reportes
Copyright © 2025 Jhonatan A
Todos los derechos reservados.
Licencia de uso privado – Queda prohibida su distribución, modificación o uso sin autorización expresa.
*/
package com.nexus.nexus.controlador; 

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nexus.nexus.dto.ClienteReservaDTO;
import com.nexus.nexus.modelo.entidad.Reserva;
import com.nexus.nexus.modelo.servicio.TipoReporteServicio;

@RestController
@RequestMapping("/api/reportes")
public class ReporteApiController {

    @Autowired
    private TipoReporteServicio reporteServicio;

    @GetMapping("/estadisticas-data")
    public ResponseEntity<Map<String, Object>> obtenerDatosEstadisticas() {
        //System.out.println("DEBUG: Se ha llamado al endpoint /api/reportes/estadisticas-data");

        long totalReservas = reporteServicio.obtenerTotalReservas();
        Map<Reserva.EstadoReserva, Long> reservasPorEstado = reporteServicio.obtenerReservasPorEstado();
        long totalClientes = reporteServicio.obtenerTotalClientes();
        Map<String, Long> destinosPopularesMap = reporteServicio.obtenerDestinosMasPopulares(5);

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
    
    @GetMapping("/ventas-por-destino")
    public ResponseEntity<Map<String, Long>> obtenerVentasPorDestinoData() {
        System.out.println("DEBUG: Se ha llamado al endpoint /api/reportes/ventas-por-destino");
        Map<String, Long> ventasPorDestino = reporteServicio.obtenerVentasPorDestino();
        return ResponseEntity.ok(ventasPorDestino);
    }
    
    @GetMapping("/clientes-data") 
    public ResponseEntity<List<ClienteReservaDTO>> obtenerClientesData(
            @RequestParam(required = false) String filtro) {
        System.out.println("DEBUG: Se ha llamado al endpoint /api/reportes/clientes-data con filtro: " + filtro);
        List<ClienteReservaDTO> clientesConReservas = reporteServicio.obtenerClientesConReservas(filtro);
        return ResponseEntity.ok(clientesConReservas);
    }
}