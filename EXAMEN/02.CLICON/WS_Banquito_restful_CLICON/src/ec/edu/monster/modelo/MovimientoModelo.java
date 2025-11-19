package ec.edu.monster.modelo;

import java.math.BigDecimal;

public class MovimientoModelo {

    private Long id;
    private String numCuenta;
    private String tipo;                 // DEP, RET, TRA
    private String naturaleza;           // INGRESO, EGRESO
    private boolean internoTransferencia;
    private BigDecimal valor;
    private String fecha;                // yyyy-MM-dd
    private BigDecimal saldoCuenta;      // saldo después del movimiento

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumCuenta() {
        return numCuenta;
    }

    public void setNumCuenta(String numCuenta) {
        this.numCuenta = numCuenta;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNaturaleza() {
        return naturaleza;
    }

    public void setNaturaleza(String naturaleza) {
        this.naturaleza = naturaleza;
    }

    public boolean isInternoTransferencia() {
        return internoTransferencia;
    }

    public void setInternoTransferencia(boolean internoTransferencia) {
        this.internoTransferencia = internoTransferencia;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getSaldoCuenta() {
        return saldoCuenta;
    }

    public void setSaldoCuenta(BigDecimal saldoCuenta) {
        this.saldoCuenta = saldoCuenta;
    }
}
