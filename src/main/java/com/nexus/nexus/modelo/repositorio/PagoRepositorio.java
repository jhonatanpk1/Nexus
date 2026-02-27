package com.nexus.nexus.modelo.repositorio;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nexus.nexus.modelo.entidad.Pago;
import com.nexus.nexus.modelo.entidad.Pago.EstadoPago;
import com.nexus.nexus.modelo.entidad.Pago.TipoPago;
import com.nexus.nexus.modelo.entidad.Reserva;

@Repository
public interface PagoRepositorio extends JpaRepository<Pago, Long> {
	// Buscar pagos por reserva
    List<Pago> findByReserva(Reserva reserva);

    // Buscar pagos por tipo de pago
    List<Pago> findByTipoPago(TipoPago tipoPago);

    // Buscar pagos por estado de pago
    List<Pago> findByEstadoPago(EstadoPago estadoPago);

    // Buscar pagos por rango de fechas de pago
    List<Pago> findByFechaPagoBetween(LocalDateTime fechaPagoDesde, LocalDateTime fechaPagoHasta);

}
