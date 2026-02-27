package com.nexus.nexus.modelo.repositorio;

import com.nexus.nexus.modelo.entidad.Piloto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PilotoRepositorio extends JpaRepository<Piloto, Long> {
    
    List<Piloto> findByVueloIsNull();
    List<Piloto> findByNombreIgnoreCaseContaining(String nombre);
    List<Piloto> findByApellidoIgnoreCaseContaining(String apellido);
    List<Piloto> findByNombreIgnoreCaseContainingAndApellidoIgnoreCaseContaining(String nombre, String apellido);
}