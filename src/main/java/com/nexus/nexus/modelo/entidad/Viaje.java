package com.nexus.nexus.modelo.entidad;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "viajes")
public class Viaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_viaje")
    private Long idViaje;

    @Column(name = "numero_vuelo", length = 50) 
    private String numeroVuelo;

    @Column(nullable = false, length = 100)
    private String origen;
    
    @Column(nullable = false, length = 100)
    private String destino;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @Column(name = "duracion_dias")
    private Integer duracionDias;

    @Column(name = "precio_adulto", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioAdulto;

    @Column(name = "precio_nino", precision = 10, scale = 2)
    private BigDecimal precioNino;

    @Column(nullable = false, columnDefinition = "INT UNSIGNED DEFAULT 0")
    private Integer disponibilidad;

    @Column(columnDefinition = "TEXT")
    private String itinerario;

    @Column(name = "imagen_principal", length = 255)
    private String imagenPrincipal;

    @Column(name = "fecha_creacion", 
            insertable = false, 
            updatable = false, 
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime fechaCreacion;
    
    @Column(name = "fecha_actualizacion", 
            insertable = false, 
            updatable = false, 
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime fechaActualizacion;

    // Constructor
    public Viaje() {
    }
    
    public Viaje(String numeroVuelo, String origen, String destino, LocalDate fechaInicio, LocalDate fechaFin, BigDecimal precioAdulto, Integer disponibilidad) {
        this.numeroVuelo = numeroVuelo;
        this.origen = origen;
        this.destino = destino;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.precioAdulto = precioAdulto;
        this.disponibilidad = disponibilidad;
    }

    // Getters y setters
    public Long getIdViaje() {
        return idViaje;
    }

    public void setIdViaje(Long idViaje) {
        this.idViaje = idViaje;
    }
    
    public String getNumeroVuelo() {
        return numeroVuelo;
    }

    public void setNumeroVuelo(String numeroVuelo) {
        this.numeroVuelo = numeroVuelo;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Integer getDuracionDias() {
        return duracionDias;
    }

    public void setDuracionDias(Integer duracionDias) {
        this.duracionDias = duracionDias;
    }

    public BigDecimal getPrecioAdulto() {
        return precioAdulto;
    }

    public void setPrecioAdulto(BigDecimal precioAdulto) {
        this.precioAdulto = precioAdulto;
    }

    public BigDecimal getPrecioNino() {
        return precioNino;
    }

    public void setPrecioNino(BigDecimal precioNino) {
        this.precioNino = precioNino;
    }

    public Integer getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(Integer disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public String getItinerario() {
        return itinerario;
    }

    public void setItinerario(String itinerario) {
        this.itinerario = itinerario;
    }

    public String getImagenPrincipal() {
        return imagenPrincipal;
    }

    public void setImagenPrincipal(String imagenPrincipal) {
        this.imagenPrincipal = imagenPrincipal;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    @Override
    public String toString() {
        return "Viajes{" +
                "idViaje=" + idViaje +
                ", numeroVuelo='" + numeroVuelo + '\'' +
                ", origen='" + origen + '\'' +
                ", destino='" + destino + '\'' +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                ", precioAdulto=" + precioAdulto +
                ", disponibilidad=" + disponibilidad +
                '}';
    }
}