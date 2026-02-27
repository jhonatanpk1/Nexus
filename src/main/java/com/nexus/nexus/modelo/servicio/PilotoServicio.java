package com.nexus.nexus.modelo.servicio;

import com.nexus.nexus.modelo.entidad.Piloto;
import com.nexus.nexus.modelo.entidad.Vuelo;
import com.nexus.nexus.modelo.repositorio.PilotoRepositorio;
import com.nexus.nexus.modelo.repositorio.VueloRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PilotoServicio implements IPilotoServicio {

    @Autowired
    private PilotoRepositorio pilotoRepositorio;

    @Autowired
    private VueloRepositorio vueloRepositorio; 

    @Override
    public List<Piloto> listarTodos() {
        return pilotoRepositorio.findAll();
    }

    @Override
    public Optional<Piloto> buscarPorId(Long id) {
        return pilotoRepositorio.findById(id);
    }

    @Override
    public Piloto guardar(Piloto piloto) {
        return pilotoRepositorio.save(piloto);
    }

    @Override
    public void eliminar(Long id) {
        pilotoRepositorio.deleteById(id);
    }

    @Transactional
    public boolean asignarVuelo(Long pilotoId, Long vueloId) {
        Optional<Piloto> pilotoOptional = pilotoRepositorio.findById(pilotoId);
        Optional<Vuelo> vueloOptional = vueloRepositorio.findById(vueloId);

        if (pilotoOptional.isPresent() && vueloOptional.isPresent()) {
            Piloto piloto = pilotoOptional.get();
            Vuelo vuelo = vueloOptional.get();
            piloto.setVuelo(vuelo);
            pilotoRepositorio.save(piloto);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean desasignarVuelo(Long pilotoId) {
        Optional<Piloto> pilotoOptional = pilotoRepositorio.findById(pilotoId);
        if (pilotoOptional.isPresent()) {
            Piloto piloto = pilotoOptional.get();
            piloto.setVuelo(null);
            pilotoRepositorio.save(piloto);
            return true;
        }
        return false;
    }

    public List<Piloto> listarPilotosSinVuelo() {
        return pilotoRepositorio.findByVueloIsNull();
    }

    public List<Piloto> buscarPilotos(String nombre, String apellido) {
        if ((nombre == null || nombre.isEmpty()) && (apellido == null || apellido.isEmpty())) {
            return listarTodos(); 
        } else if (nombre == null || nombre.isEmpty()) {
            return pilotoRepositorio.findByApellidoIgnoreCaseContaining(apellido);
        } else if (apellido == null || apellido.isEmpty()) {
            return pilotoRepositorio.findByNombreIgnoreCaseContaining(nombre);
        } else {
            return pilotoRepositorio.findByNombreIgnoreCaseContainingAndApellidoIgnoreCaseContaining(nombre, apellido);
        }
    }

    public Optional<Piloto> obtenerPilotoConVuelo(Long id) {
        return pilotoRepositorio.findById(id);
    }
}