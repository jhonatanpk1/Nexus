package com.nexus.nexus.modelo.servicio;

import com.nexus.nexus.modelo.entidad.Cliente;
import com.nexus.nexus.modelo.entidad.Pago;
import com.nexus.nexus.modelo.entidad.Pago.EstadoPago;
import com.nexus.nexus.modelo.entidad.Pago.TipoPago;
import com.nexus.nexus.modelo.entidad.Reserva;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface IPagoServicio {

    List<Pago> listarTodos();

    Pago buscarPorId(Long idPago);

    Pago realizarPago(Long idReserva, TipoPago tipoPago, BigDecimal monto);
    Pago realizarPagoYObtener(Long idReserva, TipoPago tipoPago, BigDecimal monto);

    Pago guardarPago(Pago pago, Cliente cliente, Reserva reserva);

    void actualizarEstadoPago(Long idPago, String estadoPago);

    void guardar(Pago pago);

    void eliminar(Long id);

    List<Pago> buscarPorReserva(Reserva reserva);

    List<Pago> buscarPorTipoPago(TipoPago tipoPago);

    List<Pago> buscarPorEstadoPago(EstadoPago estadoPago);

    List<Pago> buscarPorRangoFechasPago(LocalDateTime fechaPagoDesde, LocalDateTime fechaPagoHasta);

    String generarInformePagos(List<Pago> pagos);
}