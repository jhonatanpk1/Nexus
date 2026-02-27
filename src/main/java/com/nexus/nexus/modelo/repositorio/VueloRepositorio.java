package com.nexus.nexus.modelo.repositorio;

import com.nexus.nexus.modelo.entidad.Vuelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface VueloRepositorio extends JpaRepository<Vuelo, Long> {

    // Busca vuelos por número de vuelo (exacto, ignorando mayúsculas/minúsculas)
    List<Vuelo> findByNumeroVueloIgnoreCase(String numeroVuelo);

    // Busca vuelos por origen (parcial, ignorando mayúsculas/minúsculas)
    List<Vuelo> findByOrigenIgnoreCaseContaining(String origen);

    // Busca vuelos por destino (parcial, ignorando mayúsculas/minúsculas)
    List<Vuelo> findByDestinoIgnoreCaseContaining(String destino);

    // Busca vuelos por origen y destino (exacto, ignorando mayúsculas/minúsculas)
    List<Vuelo> findByOrigenIgnoreCaseAndDestinoIgnoreCase(String origen, String destino);

    // Busca vuelos que salen en una fecha específica
    List<Vuelo> findByFechaSalida(LocalDate fechaSalida);

    // Busca vuelos que salen después de una fecha específica
    List<Vuelo> findByFechaSalidaAfter(LocalDate fechaSalida);

    // Busca vuelos que salen antes de una fecha específica
    List<Vuelo> findByFechaSalidaBefore(LocalDate fechaSalida);

    // Busca vuelos que salen en un rango de fechas
    List<Vuelo> findByFechaSalidaBetween(LocalDate fechaInicio, LocalDate fechaFin);

    // Busca vuelos que salen a una hora específica
    List<Vuelo> findByHoraSalida(LocalTime horaSalida);

    // Busca vuelos que salen después de una hora específica
    List<Vuelo> findByHoraSalidaAfter(LocalTime horaSalida);

    // Busca vuelos que salen antes de una hora específica
    List<Vuelo> findByHoraSalidaBefore(LocalTime horaSalida);

    // Busca vuelos que salen en un rango de horas
    List<Vuelo> findByHoraSalidaBetween(LocalTime horaInicio, LocalTime horaFin);

    // Puedes combinar criterios, por ejemplo:
    List<Vuelo> findByOrigenIgnoreCaseContainingAndDestinoIgnoreCaseContainingAndFechaSalida(
            String origen, String destino, LocalDate fechaSalida);

    // O con rangos:
    List<Vuelo> findByFechaSalidaBetweenAndHoraSalidaBetween(
            LocalDate fechaInicio, LocalDate fechaFin, LocalTime horaInicio, LocalTime horaFin);

    // Busca por criterios de dinero desde, hasta
    List<Vuelo> findByPrecioBetween(BigDecimal precioDesde, BigDecimal precioHasta);
    
    // Buscar por precio base
    List<Vuelo> findByPrecioGreaterThanEqual(BigDecimal precioDesde);
    
    // Buscar por precio desde 
    List<Vuelo> findByPrecioLessThanEqual(BigDecimal precioHasta);
    
    List<Vuelo> findByOrigenIgnoreCaseContainingAndDestinoIgnoreCaseContainingAndFechaSalidaBetween(
            String origen, String destino, LocalDate fechaInicio, LocalDate fechaFin);
}