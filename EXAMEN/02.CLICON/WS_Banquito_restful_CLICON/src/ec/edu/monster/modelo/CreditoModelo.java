package ec.edu.monster.modelo;

import java.math.BigDecimal;

public class CreditoModelo {

    private Long id;
    private String cedulaCliente;
    private String nombreCliente;
    private BigDecimal monto;
    private int plazoMeses;
    private BigDecimal tasaAnual;
    private String fechaAprobacion; // yyyy-MM-dd
    private String estado;
    private String numCuentaAsociada;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCedulaCliente() {
        return cedulaCliente;
    }

    public void setCedulaCliente(String cedulaCliente) {
        this.cedulaCliente = cedulaCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
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

    public String getFechaAprobacion() {
        return fechaAprobacion;
    }

    public void setFechaAprobacion(String fechaAprobacion) {
        this.fechaAprobacion = fechaAprobacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNumCuentaAsociada() {
        return numCuentaAsociada;
    }

    public void setNumCuentaAsociada(String numCuentaAsociada) {
        this.numCuentaAsociada = numCuentaAsociada;
    }
}
