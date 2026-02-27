package com.nexus.nexus.modelo.repositorio;

import com.nexus.nexus.modelo.entidad.TipoReporte;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoReporteRepositorio extends JpaRepository<TipoReporte, Integer> {

	List<TipoReporte> findAllByOrderByIdAsc();
	
}