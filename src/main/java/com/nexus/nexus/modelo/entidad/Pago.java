package com.nexus.nexus.modelo.entidad;

import java.math.BigDecimal;
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
@Table(name = "pagos")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Long idPago;

    @ManyToOne
    @JoinColumn(name = "id_reserva", nullable = false)
    private Reserva reserva;

    @Column(name = "fecha_pago", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime fechaPago;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_pago", nullable = false, columnDefinition = "ENUM('Efectivo', 'Tarjeta_de_Crédito', 'Tarjeta_de_Débito', 'Transferencia') DEFAULT 'Efectivo'")
    private TipoPago tipoPago;

    @Column(length = 100)
    private String referencia;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_pago", nullable = false, columnDefinition = "ENUM('Pendiente', 'Aprobado', 'Rechazado') DEFAULT 'Pendiente'")
    private EstadoPago estadoPago;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    // Constructor
    public Pago() {
    }

    // Getters y setters
    public Long getIdPago() {
        return idPago;
    }

    public void setIdPago(Long idPago) {
        this.idPago = idPago;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }

    public LocalDateTime getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDateTime fechaPago) {
        this.fechaPago = fechaPago;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public TipoPago getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(TipoPago tipoPago) {
        this.tipoPago = tipoPago;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public EstadoPago getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(EstadoPago estadoPago) {
        this.estadoPago = estadoPago;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    // Enumeraciones internas
    public enum TipoPago {
        Efectivo,
        Tarjeta_de_Crédito,
        Tarjeta_de_Débito,
        Transferencia
    }

    public enum EstadoPago {
        Pendiente,
        Aprobado,
        Rechazado
    }

    @Override
    public String toString() {
        return "Pagos{" +
                "idPago=" + idPago +
                ", reserva=" + (reserva != null ? reserva.getIdReserva() : null) +
                ", fechaPago=" + fechaPago +
                ", monto=" + monto +
                ", tipoPago=" + tipoPago +
                ", estadoPago=" + estadoPago +
                '}';
    }
}