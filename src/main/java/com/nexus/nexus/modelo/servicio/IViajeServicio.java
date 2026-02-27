package com.nexus.nexus.modelo.servicio;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.nexus.nexus.modelo.entidad.Viaje;


public interface IViajeServicio {

    List<Viaje> listarTodos();

    Viaje buscarPorId(Long id);

    void guardar(Viaje viaje);

    void eliminar(Long id);

    List<Viaje> buscarPorDestino(String destino);

    List<Viaje> buscarPorRangoFechas(LocalDate fechaInicioDesde, LocalDate fechaInicioHasta);

    List<Viaje> buscarPorRangoPrecios(BigDecimal precioDesde, BigDecimal precioHasta);

    List<Viaje> buscarDisponibles();

    List<Viaje> buscarPorDestinoYFechas(String destino, LocalDate fechaInicioDesde, LocalDate fechaInicioHasta);

    List<Viaje> buscarPorDestinoYPrecios(String destino, BigDecimal precioDesde, BigDecimal precioHasta);

    List<Viaje> buscarPorFechasYPrecios(LocalDate fechaInicioDesde, LocalDate fechaInicioHasta, BigDecimal precioDesde, BigDecimal precioHasta);
}