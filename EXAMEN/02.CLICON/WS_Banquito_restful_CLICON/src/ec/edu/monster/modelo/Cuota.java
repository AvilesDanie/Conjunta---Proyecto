package ec.edu.monster.modelo;

/**
 * Modelo para Cuota de Crédito
 * @author CLICON
 */
public class Cuota {
    private Long id;
    private Long idCredito;
    private int numeroCuota;
    private double valorCuota;
    private String fechaVencimiento;
    private String estado;
    private double saldoPendiente;

    public Cuota() {
    }

    public Cuota(Long id, Long idCredito, int numeroCuota, double valorCuota, String fechaVencimiento, String estado, double saldoPendiente) {
        this.id = id;
        this.idCredito = idCredito;
        this.numeroCuota = numeroCuota;
        this.valorCuota = valorCuota;
        this.fechaVencimiento = fechaVencimiento;
        this.estado = estado;
        this.saldoPendiente = saldoPendiente;
    }

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

    public double getSaldoPendiente() {
        return saldoPendiente;
    }

    public void setSaldoPendiente(double saldoPendiente) {
        this.saldoPendiente = saldoPendiente;
    }

    @Override
    public String toString() {
        return "Cuota{" +
                "id=" + id +
                ", idCredito=" + idCredito +
                ", numeroCuota=" + numeroCuota +
                ", valorCuota=" + valorCuota +
                ", fechaVencimiento='" + fechaVencimiento + '\'' +
                ", estado='" + estado + '\'' +
                ", saldoPendiente=" + saldoPendiente +
                '}';
    }
}
