package com.nexus.nexus.modelo.servicio;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nexus.nexus.modelo.entidad.Cliente;
import com.nexus.nexus.modelo.entidad.Pago;
import com.nexus.nexus.modelo.entidad.Pago.EstadoPago;
import com.nexus.nexus.modelo.entidad.Pago.TipoPago;
import com.nexus.nexus.modelo.entidad.Reserva;
import com.nexus.nexus.modelo.repositorio.PagoRepositorio;
import com.nexus.nexus.modelo.repositorio.ReservaRepositorio;

import org.springframework.transaction.annotation.Transactional;


@Service
public class PagoServicioImpl implements IPagoServicio {

    @Autowired
    private PagoRepositorio pagoRepositorio;

    @Autowired
    private ReservaRepositorio reservaRepositorio;

    @Override
    public List<Pago> listarTodos() {
        return pagoRepositorio.findAll();
    }

    @Override
    @Transactional
    public Pago realizarPago(Long idReserva, Pago.TipoPago tipoPago, BigDecimal monto) {
        Reserva reserva = reservaRepositorio.findById(idReserva)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con ID: " + idReserva));

        Pago nuevoPago = new Pago();
        nuevoPago.setReserva(reserva);
        nuevoPago.setTipoPago(tipoPago);
        nuevoPago.setMonto(monto);
        nuevoPago.setFechaPago(LocalDateTime.now());
        
        nuevoPago.setEstadoPago(Pago.EstadoPago.Aprobado); 

        Pago pagoGuardado = pagoRepositorio.save(nuevoPago);

        reserva.setEstadoReserva(Reserva.EstadoReserva.Confirmada); 
        reservaRepositorio.save(reserva);

        return pagoGuardado;
    }

    @Override
    @Transactional
    public Pago realizarPagoYObtener(Long idReserva, Pago.TipoPago tipoPago, BigDecimal monto) {
        
        return realizarPago(idReserva, tipoPago, monto); 
    }


    @Override
    @Transactional(readOnly = true)
    public Pago buscarPorId(Long idPago) {
        return pagoRepositorio.findById(idPago).orElse(null);
    }

    @Override
    @Transactional
    public Pago guardarPago(Pago pago, Cliente cliente, Reserva reserva) {
        pago.setFechaPago(LocalDateTime.now());
        pago.setReserva(reserva);
        return pagoRepositorio.save(pago);
    }

    @Override
    @Transactional
    public void actualizarEstadoPago(Long idPago, String estadoPago) {
        Pago pago = pagoRepositorio.findById(idPago).orElse(null);
        if (pago != null) {
            try {
                pago.setEstadoPago(EstadoPago.valueOf(estadoPago));
                pagoRepositorio.save(pago);
            } catch (IllegalArgumentException e) {

                System.err.println("Estado de pago inválido: " + estadoPago);

            }
        }
    }

    @Override
    public void eliminar(Long id) {
        pagoRepositorio.deleteById(id);
    }

    @Override
    public List<Pago> buscarPorReserva(Reserva reserva) {
        return pagoRepositorio.findByReserva(reserva);
    }

    @Override
    public List<Pago> buscarPorTipoPago(TipoPago tipoPago) {
        return pagoRepositorio.findByTipoPago(tipoPago);
    }

    @Override
    public List<Pago> buscarPorEstadoPago(EstadoPago estadoPago) {
        return pagoRepositorio.findByEstadoPago(estadoPago);
    }

    @Override
    public List<Pago> buscarPorRangoFechasPago(LocalDateTime fechaPagoDesde, LocalDateTime fechaPagoHasta) {
        return pagoRepositorio.findByFechaPagoBetween(fechaPagoDesde, fechaPagoHasta);
    }

    @Override
    public String generarInformePagos(List<Pago> pagos) {
        StringBuilder informe = new StringBuilder("Informe de Pagos:\n");
        for (Pago pago : pagos) {
            informe.append("ID Pago: ").append(pago.getIdPago())
                    .append(", Reserva ID: ").append(pago.getReserva().getIdReserva())
                    .append(", Monto: ").append(pago.getMonto())
                    .append(", Tipo: ").append(pago.getTipoPago())
                    .append(", Estado: ").append(pago.getEstadoPago())
                    .append(", Fecha: ").append(pago.getFechaPago())
                    .append("\n");
        }
        return informe.toString();
    }

    @Override
    public void guardar(Pago pago) {
        pagoRepositorio.save(pago);
    }
}