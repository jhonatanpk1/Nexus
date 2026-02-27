package com.nexus.nexus.modelo.servicio;

import com.nexus.nexus.modelo.entidad.Piloto;
import java.util.List;
import java.util.Optional;

public interface IPilotoServicio {
    List<Piloto> listarTodos();
    Optional<Piloto> buscarPorId(Long id);
    Piloto guardar(Piloto piloto);
    void eliminar(Long id);

    Optional<Piloto> obtenerPilotoConVuelo(Long id);
}