/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.monster.modelo;

/**
 * Modelo de Amortización que coincide con CuotaResponse del servidor BanQuito
 * @author CLICON
 */
public class Amortizacion {
    private Long id;
    private Long idCredito;
    private int numeroCuota;
    private double valorCuota;
    private double interesPagado;
    private double capitalPagado;
    private double saldo;
    private String fechaVencimiento;
    private String estado;

    public Amortizacion() {
    }

    public Amortizacion(int numeroCuota, double valorCuota, double interesPagado, double capitalPagado, double saldo, String fechaVencimiento, String estado) {
        this.numeroCuota = numeroCuota;
        this.valorCuota = valorCuota;
        this.interesPagado = interesPagado;
        this.capitalPagado = capitalPagado;
        this.saldo = saldo;
        this.fechaVencimiento = fechaVencimiento;
        this.estado = estado;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdCredito() {
        return idCredito;
    }

    public void setIdCredito(Long idCredito) {
        this.idCredito = idCredito;
    }

    public int getNumeroCuota() {
        return numeroCuota;
    }

    public void setNumeroCuota(int numeroCuota) {
        this.numeroCuota = numeroCuota;
    }

    public double getValorCuota() {
        return valorCuota;
    }

    public void setValorCuota(double valorCuota) {
        this.valorCuota = valorCuota;
    }

    public double getInteresPagado() {
        return interesPagado;
    }

    public void setInteresPagado(double interesPagado) {
        this.interesPagado = interesPagado;
    }

    public double getCapitalPagado() {
        return capitalPagado;
    }

    public void setCapitalPagado(double capitalPagado) {
        this.capitalPagado = capitalPagado;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public String getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(String fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Amortizacion{" +
                "id=" + id +
                ", idCredito=" + idCredito +
                ", numeroCuota=" + numeroCuota +
                ", valorCuota=" + valorCuota +
                ", interesPagado=" + interesPagado +
                ", capitalPagado=" + capitalPagado +
                ", saldo=" + saldo +
                ", fechaVencimiento='" + fechaVencimiento + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}
