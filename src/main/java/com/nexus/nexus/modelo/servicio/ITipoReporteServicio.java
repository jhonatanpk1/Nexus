package com.nexus.nexus.modelo.servicio;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.nexus.nexus.modelo.entidad.Reserva;
import com.nexus.nexus.modelo.entidad.TipoReporte;

public interface ITipoReporteServicio {
    List<TipoReporte> listarTiposReporte();

    Map<String, Long> obtenerVentasPorDestino();

    Map<LocalDate, Long> obtenerVentasPorFecha(LocalDate fechaInicio, LocalDate fechaFin);

    long obtenerTotalReservas();

    Map<Reserva.EstadoReserva, Long> obtenerReservasPorEstado();

    long obtenerTotalClientes();

    Map<String, Long> obtenerDestinosMasPopulares(int limit);
}