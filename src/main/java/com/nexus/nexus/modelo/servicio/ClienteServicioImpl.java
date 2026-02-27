package com.nexus.nexus.modelo.servicio;

import com.nexus.nexus.modelo.entidad.Cliente;
import com.nexus.nexus.modelo.repositorio.ClienteRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.ArrayList; 
import java.util.Optional;

@Service
@Primary
public class ClienteServicioImpl implements IClienteServicio {

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    @Override
    @Transactional(readOnly = true)
    public Cliente buscarPorId(long idCliente) {
        return clienteRepositorio.findById(idCliente).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> listarTodos() {
        List<Cliente> clientes = new ArrayList<>();
        clienteRepositorio.findAll().forEach(clientes::add);
        return clientes;
    }

    @Override
    @Transactional
    public void eliminar(long idCliente) {
        clienteRepositorio.deleteById(idCliente);
    }
    
    @Override
    @Transactional
    public void guardar(Cliente cliente) { 
        clienteRepositorio.save(cliente);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Cliente> buscarPorNumeroDocumentoYTipoDocumento(String numeroDocumento, String tipoDocumento) {
        return clienteRepositorio.findByNumeroDocumentoAndTipoDocumento(numeroDocumento, tipoDocumento);
    }
}