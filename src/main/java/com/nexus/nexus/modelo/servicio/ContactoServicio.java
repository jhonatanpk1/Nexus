package com.nexus.nexus.modelo.servicio;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nexus.nexus.modelo.entidad.Cliente;
import com.nexus.nexus.modelo.repositorio.ClienteRepositorio;

@Service
public class ContactoServicio implements IClienteServicio {

	@Autowired
	private ClienteRepositorio clienteRepositorio;

	@Override
    @Transactional(readOnly = true)
    public List<Cliente> listarTodos() {
        return (List<Cliente>)clienteRepositorio.findAll();
    }

	@Override
	@Transactional 
	public void guardar(Cliente clientes) {
		clienteRepositorio.save(clientes);
	}

	@Override
	@Transactional(readOnly = true)
	public Cliente buscarPorId(long id) {
		return clienteRepositorio.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void eliminar(long id) {
		clienteRepositorio.deleteById(id);
	}
	
	@Override
    @Transactional(readOnly = true)
    public Optional<Cliente> buscarPorNumeroDocumentoYTipoDocumento(String numeroDocumento, String tipoDocumento) {
        return clienteRepositorio.findByNumeroDocumentoAndTipoDocumento(numeroDocumento, tipoDocumento);
    }
}