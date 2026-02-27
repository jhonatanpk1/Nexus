package com.nexus.nexus.modelo.repositorio;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nexus.nexus.modelo.entidad.Cliente;
import com.nexus.nexus.modelo.entidad.Reserva;
import com.nexus.nexus.modelo.entidad.Viaje;


@Repository
public interface ReservaRepositorio extends JpaRepository<Reserva, Long> {

	// Buscar reservas por cliente
    List<Reserva> findByCliente(Cliente cliente);

    // Buscar reservas por viaje
    List<Reserva> findByViaje(Viaje viaje);

    // Buscar reservas por rango de fechas de viaje
    List<Reserva> findByFechaInicioBetween(LocalDate fechaInicioDesde, LocalDate fechaInicioHasta);

    //Buscarreservas por rengo de fechas y horas de inicio y fin del viaje
    List<Reserva> findByFechaReservaBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);
    
    // Buscar reservas por estado
    List<Reserva> findByEstadoReserva(Reserva.EstadoReserva estadoReserva);
    
    
}
