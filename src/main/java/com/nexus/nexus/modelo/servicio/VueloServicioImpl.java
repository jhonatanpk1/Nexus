package com.nexus.nexus.modelo.servicio;

import com.nexus.nexus.modelo.entidad.Vuelo;
import com.nexus.nexus.modelo.repositorio.VueloRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class VueloServicioImpl implements IVueloServicio {

    @Autowired
    private VueloRepositorio vueloRepositorio;

    @Override
    @Transactional(readOnly = true)
    public List<Vuelo> listarTodos() {
        return vueloRepositorio.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Vuelo buscarPorId(Long id) {
        return vueloRepositorio.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void guardar(Vuelo vuelo) {
        vueloRepositorio.save(vuelo);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        vueloRepositorio.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vuelo> buscarPorNumeroVuelo(String numeroVuelo) {
        return vueloRepositorio.findByNumeroVueloIgnoreCase(numeroVuelo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vuelo> buscarPorOrigen(String origen) {
        return vueloRepositorio.findByOrigenIgnoreCaseContaining(origen);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vuelo> buscarPorDestino(String destino) {
        return vueloRepositorio.findByDestinoIgnoreCaseContaining(destino);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vuelo> buscarPorOrigenYDestino(String origen, String destino) {
        return vueloRepositorio.findByOrigenIgnoreCaseAndDestinoIgnoreCase(origen, destino);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vuelo> buscarPorFechaSalida(LocalDate fechaSalida) {
        return vueloRepositorio.findByFechaSalida(fechaSalida);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vuelo> buscarPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        return vueloRepositorio.findByFechaSalidaBetween(fechaInicio, fechaFin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vuelo> buscarPorHoraSalida(LocalTime horaSalida) {
        return vueloRepositorio.findByHoraSalida(horaSalida);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vuelo> buscarPorRangoHoras(LocalTime horaInicio, LocalTime horaFin) {
        return vueloRepositorio.findByHoraSalidaBetween(horaInicio, horaFin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vuelo> buscarPorOrigenDestinoYFecha(String origen, String destino, LocalDate fechaSalida) {
        return vueloRepositorio.findByOrigenIgnoreCaseContainingAndDestinoIgnoreCaseContainingAndFechaSalida(origen, destino, fechaSalida);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vuelo> buscarPorRangoFechasYHoras(LocalDate fechaInicio, LocalDate fechaFin, LocalTime horaInicio, LocalTime horaFin) {
        return vueloRepositorio.findByFechaSalidaBetweenAndHoraSalidaBetween(fechaInicio, fechaFin, horaInicio, horaFin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vuelo> buscarPorRangoPrecios(BigDecimal precioDesde, BigDecimal precioHasta) {
        return vueloRepositorio.findByPrecioBetween(precioDesde, precioHasta);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vuelo> buscarPorPrecioMayorOIgual(BigDecimal precioDesde) {
        return vueloRepositorio.findByPrecioGreaterThanEqual(precioDesde);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vuelo> buscarPorPrecioMenorOIgual(BigDecimal precioHasta) {
        return vueloRepositorio.findByPrecioLessThanEqual(precioHasta);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vuelo> buscarPorOrigenIgnoreCaseContainingAndDestinoIgnoreCaseContainingAndFechaSalidaBetween(String origen, String destino, LocalDate fechaInicio, LocalDate fechaFin) {
        return vueloRepositorio.findByOrigenIgnoreCaseContainingAndDestinoIgnoreCaseContainingAndFechaSalidaBetween(origen, destino, fechaInicio, fechaFin);
    }
}