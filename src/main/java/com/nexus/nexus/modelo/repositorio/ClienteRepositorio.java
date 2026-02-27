package com.nexus.nexus.modelo.repositorio;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.nexus.nexus.modelo.entidad.Cliente;

@Repository
public interface ClienteRepositorio extends CrudRepository<Cliente, Long> {
	Optional<Cliente> findByNumeroDocumentoAndTipoDocumento(String numeroDocumento, String tipoDocumento);
}