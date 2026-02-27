package com.nexus.nexus.modelo.servicio;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nexus.nexus.modelo.entidad.Viaje;
import com.nexus.nexus.modelo.repositorio.ViajeRepositorio;

import jakarta.transaction.Transactional;


@Service
public class ViajeServicio implements IViajeServicio {

    @Autowired
    private ViajeRepositorio viajeRepositorio;

    @Override
    public List<Viaje> listarTodos() {
        return viajeRepositorio.findAll();
    }

    @Override
    public Viaje buscarPorId(Long id) {
        return viajeRepositorio.findById(id).orElse(null);
    }

    @Override
    public void guardar(Viaje viaje) {
        viajeRepositorio.save(viaje);
    }

    @Override
    public void eliminar(Long id) {
        viajeRepositorio.deleteById(id);
    }

    @Override
    public List<Viaje> buscarPorDestino(String destino) {
        return viajeRepositorio.findByDestinoContainingIgnoreCase(destino);
    }

    @Override
    public List<Viaje> buscarPorRangoFechas(LocalDate fechaInicioDesde, LocalDate fechaInicioHasta) {
        return viajeRepositorio.findByFechaInicioBetween(fechaInicioDesde, fechaInicioHasta);
    }

    @Override
    public List<Viaje> buscarPorRangoPrecios(BigDecimal precioDesde, BigDecimal precioHasta) {
        return viajeRepositorio.findByPrecioAdultoBetween(precioDesde, precioHasta);
    }

    @Override
    public List<Viaje> buscarDisponibles() {
        return viajeRepositorio.findByDisponibilidadGreaterThan(0);
    }

    @Override
    public List<Viaje> buscarPorDestinoYFechas(String destino, LocalDate fechaInicioDesde, LocalDate fechaInicioHasta) {
        return viajeRepositorio.findByDestinoContainingIgnoreCaseAndFechaInicioBetween(destino, fechaInicioDesde, fechaInicioHasta);
    }

    @Override
    public List<Viaje> buscarPorDestinoYPrecios(String destino, BigDecimal precioDesde, BigDecimal precioHasta) {
        return viajeRepositorio.findByDestinoContainingIgnoreCaseAndPrecioAdultoBetween(destino, precioDesde, precioHasta);
    }

    @Override
    public List<Viaje> buscarPorFechasYPrecios(LocalDate fechaInicioDesde, LocalDate fechaInicioHasta, BigDecimal precioDesde, BigDecimal precioHasta) {
        return viajeRepositorio.findByFechaInicioBetweenAndPrecioAdultoBetween(fechaInicioDesde, fechaInicioHasta, precioDesde, precioHasta);
    }
}