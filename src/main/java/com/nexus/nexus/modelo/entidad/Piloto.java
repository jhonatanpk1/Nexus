package com.nexus.nexus.modelo.entidad;

import jakarta.persistence.*;

@Entity
@Table(name = "piloto")
public class Piloto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_piloto")
    private Long idPiloto;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String apellido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_vuelo")
    private Vuelo vuelo;

    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "id_viaje") 
    private Viaje viaje;
    
    // Constructor
    public Piloto() {
    }

    public Piloto(String nombre, String apellido) {
        this.nombre = nombre;
        this.apellido = apellido;
    }

    // Getters y setters
    public Long getIdPiloto() {
        return idPiloto;
    }

    public void setIdPiloto(Long idPiloto) {
        this.idPiloto = idPiloto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Vuelo getVuelo() {
        return vuelo;
    }

    public void setVuelo(Vuelo vuelo) {
        this.vuelo = vuelo;
    }
    
    public Viaje getViaje() {
        return viaje;
    }

    public void setViaje(Viaje viaje) {
        this.viaje = viaje;
    }
    
    @Override
    public String toString() {
        return "Piloto{" +
               "idPiloto=" + idPiloto +
               ", nombre='" + nombre + '\'' +
               ", apellido='" + apellido + '\'' +
               ", vuelo=" + (vuelo != null ? vuelo.getNumeroVuelo() : "N/A") +
               ", viaje=" + (viaje != null ? viaje.getIdViaje() : "N/A") +
               '}';
    }
}