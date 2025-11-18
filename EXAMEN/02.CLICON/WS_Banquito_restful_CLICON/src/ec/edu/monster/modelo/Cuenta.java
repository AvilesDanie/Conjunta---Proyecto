package ec.edu.monster.modelo;

/**
 * Modelo para Cuenta Bancaria
 * @author CLICON
 */
public class Cuenta {
    private String numCuenta;
    private String cedula;
    private String tipoCuenta;
    private double saldo;
    private String estado;

    public Cuenta() {
    }

    public Cuenta(String numCuenta, String cedula, String tipoCuenta, double saldo, String estado) {
        this.numCuenta = numCuenta;
        this.cedula = cedula;
        this.tipoCuenta = tipoCuenta;
        this.saldo = saldo;
        this.estado = estado;
    }

    public String getNumCuenta() {
        return numCuenta;
    }

    public void setNumCuenta(String numCuenta) {
        this.numCuenta = numCuenta;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Cuenta{" +
                "numCuenta='" + numCuenta + '\'' +
                ", cedula='" + cedula + '\'' +
                ", tipoCuenta='" + tipoCuenta + '\'' +
                ", saldo=" + saldo +
                ", estado='" + estado + '\'' +
                '}';
    }
}
