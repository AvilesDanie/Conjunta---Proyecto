package ec.edu.monster.modelo;

/**
 * Modelo para Movimiento Bancario
 * @author CLICON
 */
public class Movimiento {
    private Long id;
    private String numCuenta;
    private String tipo; // DEPOSITO, RETIRO, TRANSFERENCIA
    private double monto;
    private String fecha;
    private String descripcion;

    public Movimiento() {
    }

    public Movimiento(Long id, String numCuenta, String tipo, double monto, String fecha, String descripcion) {
        this.id = id;
        this.numCuenta = numCuenta;
        this.tipo = tipo;
        this.monto = monto;
        this.fecha = fecha;
        this.descripcion = descripcion;
    }

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

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "Movimiento{" +
                "id=" + id +
                ", numCuenta='" + numCuenta + '\'' +
                ", tipo='" + tipo + '\'' +
                ", monto=" + monto +
                ", fecha='" + fecha + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
