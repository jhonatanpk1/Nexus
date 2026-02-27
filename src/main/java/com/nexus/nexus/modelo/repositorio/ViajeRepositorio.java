package com.nexus.nexus.modelo.repositorio;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nexus.nexus.modelo.entidad.Viaje;

@Repository
public interface ViajeRepositorio extends JpaRepository<Viaje, Long> {

	// Buscar viajes por destino (puede ser parcial)
    List<Viaje> findByDestinoContainingIgnoreCase(String destino);

    // Buscar viajes por rango de fechas de inicio
    List<Viaje> findByFechaInicioBetween(LocalDate fechaInicioDesde, LocalDate fechaInicioHasta);

    // Buscar viajes por rango de precios para adultos
    List<Viaje> findByPrecioAdultoBetween(BigDecimal precioDesde, BigDecimal precioHasta);

    // Buscar viajes disponibles (disponibilidad mayor que 0)
    List<Viaje> findByDisponibilidadGreaterThan(Integer disponibilidad);

    // Buscar viajes por destino y rango de fechas
    List<Viaje> findByDestinoContainingIgnoreCaseAndFechaInicioBetween(String destino, LocalDate fechaInicioDesde, LocalDate fechaInicioHasta);

    // Buscar viajes por destino y rango de precios
    List<Viaje> findByDestinoContainingIgnoreCaseAndPrecioAdultoBetween(String destino, BigDecimal precioDesde, BigDecimal precioHasta);

    // Buscar viajes por rango de fechas y rango de precios
    List<Viaje> findByFechaInicioBetweenAndPrecioAdultoBetween(LocalDate fechaInicioDesde, LocalDate fechaInicioHasta, BigDecimal precioDesde, BigDecimal precioHasta);
}
