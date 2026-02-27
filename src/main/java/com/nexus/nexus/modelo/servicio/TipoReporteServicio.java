package com.nexus.nexus.modelo.servicio;

import com.nexus.nexus.modelo.entidad.Reserva;
import com.nexus.nexus.modelo.entidad.TipoReporte;
import com.nexus.nexus.modelo.entidad.Viaje;
import com.nexus.nexus.modelo.repositorio.ClienteRepositorio;
import com.nexus.nexus.modelo.repositorio.ReservaRepositorio;
import com.nexus.nexus.modelo.repositorio.TipoReporteRepositorio;
import com.nexus.nexus.modelo.repositorio.ViajeRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TipoReporteServicio implements ITipoReporteServicio {

    @Autowired
    private TipoReporteRepositorio tipoReporteRepositorio;

    @Autowired
    private ReservaRepositorio reservaRepositorio;

    @Autowired
    private ViajeRepositorio viajeRepositorio;

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    @Override
    public List<TipoReporte> listarTiposReporte() {
        return tipoReporteRepositorio.findAllByOrderByIdAsc();
    }

    @Override
    public Map<String, Long> obtenerVentasPorDestino() {
        return reservaRepositorio.findAll().stream()
                .filter(reserva -> reserva.getViaje() != null)
                .collect(Collectors.groupingBy(reserva -> reserva.getViaje().getDestino(), Collectors.counting()));
    }

    @Override
    public Map<LocalDate, Long> obtenerVentasPorFecha(LocalDate fechaInicio, LocalDate fechaFin) {
        return reservaRepositorio.findByFechaReservaBetween(fechaInicio.atStartOfDay(), fechaFin.plusDays(1).atStartOfDay())
                .stream()
                .collect(Collectors.groupingBy(reserva -> reserva.getFechaReserva().toLocalDate(), Collectors.counting()));
    }

    @Override
    public long obtenerTotalReservas() {
        return reservaRepositorio.count();
    }

    @Override
    public Map<Reserva.EstadoReserva, Long> obtenerReservasPorEstado() {
        return reservaRepositorio.findAll().stream().collect(Collectors.groupingBy(reserva -> (Reserva.EstadoReserva) reserva.getEstadoReserva(), Collectors.counting()));
    }

    @Override
    public long obtenerTotalClientes() {
        return clienteRepositorio.count();
    }

    @Override
    public Map<String, Long> obtenerDestinosMasPopulares(int limit) {
        return reservaRepositorio.findAll().stream()
                .filter(reserva -> reserva.getViaje() != null)
                .collect(Collectors.groupingBy(reserva -> reserva.getViaje().getDestino(), Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(limit)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}