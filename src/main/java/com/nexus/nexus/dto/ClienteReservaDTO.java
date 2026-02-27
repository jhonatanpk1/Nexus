/*
Proyecto Nexus - Sistema de Reportes
Copyright © 2025 Jhonatan A
Todos los derechos reservados.
Licencia de uso privado – Queda prohibida su distribución, modificación o uso sin autorización expresa.
*/
package com.nexus.nexus.dto;

import java.time.LocalDate;

public class ClienteReservaDTO {
    private Long idCliente;
    private String nombreCliente;
    private String apellidoCliente;
    private String tipoDocumentoCliente;
    private String numeroDocumentoCliente;
    private String telefonoCliente;
    private Long idReserva;
    private String destinoViaje;
    private String origenViaje;
    private LocalDate fechaInicioViaje;
    private String estadoReserva;
    private String precioTotalReserva;

    // Constructor
    public ClienteReservaDTO(Long idCliente, String nombreCliente, String apellidoCliente,
                             String tipoDocumentoCliente, String numeroDocumentoCliente,
                             String telefonoCliente, Long idReserva, String destinoViaje,
                             String origenViaje, LocalDate fechaInicioViaje,
                             String estadoReserva, String precioTotalReserva) {
        this.idCliente = idCliente;
        this.nombreCliente = nombreCliente;
        this.apellidoCliente = apellidoCliente;
        this.tipoDocumentoCliente = tipoDocumentoCliente;
        this.numeroDocumentoCliente = numeroDocumentoCliente;
        this.telefonoCliente = telefonoCliente;
        this.idReserva = idReserva;
        this.destinoViaje = destinoViaje;
        this.origenViaje = origenViaje;
        this.fechaInicioViaje = fechaInicioViaje;
        this.estadoReserva = estadoReserva;
        this.precioTotalReserva = precioTotalReserva;
    }

    // Getters y Setters
    public Long getIdCliente() { return idCliente; }
    public void setIdCliente(Long idCliente) { this.idCliente = idCliente; }
    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }
    public String getApellidoCliente() { return apellidoCliente; }
    public void setApellidoCliente(String apellidoCliente) { this.apellidoCliente = apellidoCliente; }
    public String getTipoDocumentoCliente() { return tipoDocumentoCliente; }
    public void setTipoDocumentoCliente(String tipoDocumentoCliente) { this.tipoDocumentoCliente = tipoDocumentoCliente; }
    public String getNumeroDocumentoCliente() { return numeroDocumentoCliente; }
    public void setNumeroDocumentoCliente(String numeroDocumentoCliente) { this.numeroDocumentoCliente = numeroDocumentoCliente; }
    public String getTelefonoCliente() { return telefonoCliente; }
    public void setTelefonoCliente(String telefonoCliente) { this.telefonoCliente = telefonoCliente; }
    public Long getIdReserva() { return idReserva; }
    public void setIdReserva(Long idReserva) { this.idReserva = idReserva; }
    public String getDestinoViaje() { return destinoViaje; }
    public void setDestinoViaje(String destinoViaje) { this.destinoViaje = destinoViaje; }
    public String getOrigenViaje() { return origenViaje; }
    public void setOrigenViaje(String origenViaje) { this.origenViaje = origenViaje; }
    public LocalDate getFechaInicioViaje() { return fechaInicioViaje; }
    public void setFechaInicioViaje(LocalDate fechaInicioViaje) { this.fechaInicioViaje = fechaInicioViaje; }
    public String getEstadoReserva() { return estadoReserva; }
    public void setEstadoReserva(String estadoReserva) { this.estadoReserva = estadoReserva; }
    public String getPrecioTotalReserva() { return precioTotalReserva; }
    public void setPrecioTotalReserva(String precioTotalReserva) { this.precioTotalReserva = precioTotalReserva; }
}