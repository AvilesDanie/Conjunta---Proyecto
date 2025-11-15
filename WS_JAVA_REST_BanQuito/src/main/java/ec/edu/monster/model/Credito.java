/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.monster.model;

/**
 *
 * @author danie
 */

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "credito")
public class Credito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cedula_cliente")
    private Cliente cliente;

    @ManyToOne(optional = false)
    @JoinColumn(name = "num_cuenta_asociada")
    private Cuenta cuentaAsociada;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Column(name = "plazo_meses", nullable = false)
    private int plazoMeses;

    @Column(name = "tasa_anual", nullable = false, precision = 5, scale = 2)
    private BigDecimal tasaAnual; // 16.00

    @Column(name = "fecha_aprobacion")
    private LocalDate fechaAprobacion;

    @Column(length = 15)
    private String estado; // APROBADO, RECHAZADO, ACTIVO, CANCELADO

    // Getters y setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Cuenta getCuentaAsociada() {
        return cuentaAsociada;
    }

    public void setCuentaAsociada(Cuenta cuentaAsociada) {
        this.cuentaAsociada = cuentaAsociada;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public int getPlazoMeses() {
        return plazoMeses;
    }

    public void setPlazoMeses(int plazoMeses) {
        this.plazoMeses = plazoMeses;
    }

    public BigDecimal getTasaAnual() {
        return tasaAnual;
    }

    public void setTasaAnual(BigDecimal tasaAnual) {
        this.tasaAnual = tasaAnual;
    }

    public LocalDate getFechaAprobacion() {
        return fechaAprobacion;
    }

    public void setFechaAprobacion(LocalDate fechaAprobacion) {
        this.fechaAprobacion = fechaAprobacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
