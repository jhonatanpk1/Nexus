package com.nexus.nexus.modelo.servicio;

import java.util.List;
import java.util.Optional;

import com.nexus.nexus.modelo.entidad.Cliente;

public interface IClienteServicio {
	
	List<Cliente> listarTodos();
	void guardar(Cliente cliente);
	Cliente buscarPorId(long idCliente);
	void eliminar(long id);
	
	Optional<Cliente> buscarPorNumeroDocumentoYTipoDocumento(String numeroDocumento, String tipoDocumento);
}