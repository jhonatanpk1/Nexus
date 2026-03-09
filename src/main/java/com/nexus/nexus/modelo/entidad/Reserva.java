package com.nexus.nexus.modelo.entidad;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva")
    private Long idReserva;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_viaje", nullable = true)
    private Viaje viaje;

    @ManyToOne
    @JoinColumn(name = "id_vuelo", nullable = true)
    private Vuelo vuelo;

    @Column(name = "fecha_reserva", insertable = false, updatable = false)
    private LocalDateTime fechaReserva;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = true)
    private LocalDate fechaFin;

    @Column(name = "cantidad_adultos", nullable = false, columnDefinition = "INT UNSIGNED DEFAULT 1")
    private Integer cantidadAdultos;

    @Column(name = "cantidad_ninos", nullable = false, columnDefinition = "INT UNSIGNED DEFAULT 0")
    private Integer cantidadNinos;

    @Column(name = "precio_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioTotal;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_reserva", nullable = false, columnDefinition = "ENUM('Pendiente', 'Confirmada', 'Cancelada', 'Finalizada') DEFAULT 'Pendiente'")
    private EstadoReserva estadoReserva;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    // Constructor
    public Reserva() {
    }

    // Getters y setters
    public Long getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(Long idReserva) {
        this.idReserva = idReserva;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Viaje getViaje() {
        return viaje;
    }

    public void setViaje(Viaje viaje) {
        this.viaje = viaje;
    }

    public Vuelo getVuelo() {
        return vuelo;
    }

    public void setVuelo(Vuelo vuelo) {
        this.vuelo = vuelo;
    }

    public LocalDateTime getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(LocalDateTime fechaReserva) {
        this.fechaReserva = fechaReserva;
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

    public Integer getCantidadAdultos() {
        return cantidadAdultos;
    }

    public void setCantidadAdultos(Integer cantidadAdultos) {
        this.cantidadAdultos = cantidadAdultos;
    }

    public Integer getCantidadNinos() {
        return cantidadNinos;
    }

    public void setCantidadNinos(Integer cantidadNinos) {
        this.cantidadNinos = cantidadNinos;
    }

    public BigDecimal getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(BigDecimal precioTotal) {
        this.precioTotal = precioTotal;
    }

    public EstadoReserva getEstadoReserva() {
        return estadoReserva;
    }

    public void setEstadoReserva(EstadoReserva estadoReserva) {
        this.estadoReserva = estadoReserva;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    // Enumeración para el estado de la reserva
    public enum EstadoReserva {
        Pendiente,
        Confirmada,
        Cancelada,
        Finalizada
    }

    @Override
    public String toString() {
        return "Reservas{" +
                "idReserva=" + idReserva +
                ", cliente=" + (cliente != null ? cliente.getIdCliente() : null) +
                ", viaje=" + (viaje != null ? viaje.getIdViaje() : null) +
                ", vuelo=" + (vuelo != null ? vuelo.getIdVuelo() : null) + // Agrega vuelo al toString
                ", fechaReserva=" + fechaReserva +
                ", fechaInicioViaje=" + fechaInicio +
                ", fechaFinViaje=" + fechaFin +
                ", cantidadAdultos=" + cantidadAdultos +
                ", cantidadNinos=" + cantidadNinos +
                ", precioTotal=" + precioTotal +
                ", estadoReserva=" + estadoReserva +
                '}';
    }
}