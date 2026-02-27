package com.nexus.nexus.modelo.servicio;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nexus.nexus.modelo.entidad.Cliente;
import com.nexus.nexus.modelo.entidad.Reserva;
import com.nexus.nexus.modelo.entidad.Viaje;
import com.nexus.nexus.modelo.entidad.Vuelo;
import com.nexus.nexus.modelo.repositorio.ReservaRepositorio;


@Service
public class ReservaServicio implements IReservaServicio {

    @Autowired
    private ReservaRepositorio reservasRepositorio;

    @Override
    @Transactional(readOnly = true)
    public List<Reserva> listarTodos() {
        return reservasRepositorio.findAll();
    }

    @Override
    public Reserva buscarPorId(Long id) {
        return reservasRepositorio.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Reserva guardar(Reserva reserva) {
        return reservasRepositorio.save(reserva);
    }

    @Override
    public void eliminar(Long id) {
        reservasRepositorio.deleteById(id);
    }

    @Override
    public List<Reserva> buscarPorCliente(Cliente cliente) {
        return reservasRepositorio.findByCliente(cliente);
    }

    @Override
    public List<Reserva> buscarPorViaje(Viaje viaje) {
        return reservasRepositorio.findByViaje(viaje);
    }

    @Override
    public List<Reserva> buscarPorRangoFechasViaje(LocalDate fechaInicioDesde, LocalDate fechaInicioHasta) {
        return reservasRepositorio.findByFechaInicioBetween(fechaInicioDesde, fechaInicioHasta);
    }

    @Override
    public List<Reserva> buscarPorEstado(Reserva.EstadoReserva estadoReserva) {
        return reservasRepositorio.findByEstadoReserva(estadoReserva);
    }

    @Override
    public BigDecimal calcularPrecioTotal(Viaje viaje, int cantidadAdultos, int cantidadNinos) {
        BigDecimal precioTotal = viaje.getPrecioAdulto().multiply(new BigDecimal(cantidadAdultos));
        if (viaje.getPrecioNino() != null) {
            precioTotal = precioTotal.add(viaje.getPrecioNino().multiply(new BigDecimal(cantidadNinos)));
        }
        return precioTotal;
    }

    @Override
    public String generarConfirmacionReserva(Reserva reserva) {
        return "Confirmación de Reserva para el cliente " + reserva.getCliente().getNombre() +
               " para el viaje a " + reserva.getViaje().getDestino() +
               " con fecha de inicio " + reserva.getFechaInicio();
    }

    @Override
    public Double calcularPrecioTotalVuelo(Vuelo vuelo, Integer cantidadAdultos, Integer cantidadNinos) {
        BigDecimal precioBase = vuelo.getPrecio();
        BigDecimal cantidadTotal = new BigDecimal(cantidadAdultos + cantidadNinos);
        return precioBase.multiply(cantidadTotal).doubleValue();
    }
}