package com.nexus.nexus.modelo.servicio;

import com.nexus.nexus.modelo.entidad.Vuelo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface IVueloServicio {

    List<Vuelo> listarTodos();

    Vuelo buscarPorId(Long id);

    void guardar(Vuelo vuelo);

    void eliminar(Long id);

    List<Vuelo> buscarPorNumeroVuelo(String numeroVuelo);

    List<Vuelo> buscarPorOrigen(String origen);

    List<Vuelo> buscarPorDestino(String destino);

    List<Vuelo> buscarPorOrigenYDestino(String origen, String destino);

    List<Vuelo> buscarPorFechaSalida(LocalDate fechaSalida);

    List<Vuelo> buscarPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin);

    List<Vuelo> buscarPorHoraSalida(LocalTime horaSalida);

    List<Vuelo> buscarPorRangoHoras(LocalTime horaInicio, LocalTime horaFin);

    List<Vuelo> buscarPorOrigenDestinoYFecha(String origen, String destino, LocalDate fechaSalida);

    List<Vuelo> buscarPorRangoFechasYHoras(LocalDate fechaInicio, LocalDate fechaFin, LocalTime horaInicio, LocalTime horaFin);

    List<Vuelo> buscarPorRangoPrecios(BigDecimal precioDesde, BigDecimal precioHasta);

    List<Vuelo> buscarPorPrecioMayorOIgual(BigDecimal precioDesde);

    List<Vuelo> buscarPorPrecioMenorOIgual(BigDecimal precioHasta);

    // ¡Asegúrate de tener esta declaración!
    List<Vuelo> buscarPorOrigenIgnoreCaseContainingAndDestinoIgnoreCaseContainingAndFechaSalidaBetween(
            String origen, String destino, LocalDate fechaInicio, LocalDate fechaFin);
}