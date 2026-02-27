package com.nexus.nexus.modelo.servicio;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.nexus.nexus.modelo.entidad.Cliente;
import com.nexus.nexus.modelo.entidad.Reserva;
import com.nexus.nexus.modelo.entidad.Viaje;
import com.nexus.nexus.modelo.entidad.Vuelo;

public interface IReservaServicio {
	List<Reserva> listarTodos();

    Reserva buscarPorId(Long id);

    Reserva guardar(Reserva reserva);
    
    void eliminar(Long id);

    List<Reserva> buscarPorCliente(Cliente cliente);

    List<Reserva> buscarPorViaje(Viaje viaje);

    List<Reserva> buscarPorRangoFechasViaje(LocalDate fechaInicioDesde, LocalDate fechaInicioHasta);

    List<Reserva> buscarPorEstado(Reserva.EstadoReserva estadoReserva);

    // Métodos adicionales para la lógica de negocio 
    BigDecimal calcularPrecioTotal(Viaje viaje, int cantidadAdultos, int cantidadNinos);
    
    public Double calcularPrecioTotalVuelo(Vuelo vuelo, Integer cantidadAdultos, Integer cantidadNinos);
    
    // Método para generar la confirmación de reserva 
    String generarConfirmacionReserva(Reserva reserva);
}
