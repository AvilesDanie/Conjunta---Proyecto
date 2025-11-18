
package ec.edu.monster.modelo;

public class Cliente {
    private String cedula;
    private String nombre;
    private String fechaNacimiento;
    private String estadoCivil;
    private String numCuentaInicial;
    private String tipoCuentaInicial;
    private double saldoInicial;

    public Cliente() {
    }

    public Cliente(String cedula, String nombre) {
        this.cedula = cedula;
        this.nombre = nombre;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getNumCuentaInicial() {
        return numCuentaInicial;
    }

    public void setNumCuentaInicial(String numCuentaInicial) {
        this.numCuentaInicial = numCuentaInicial;
    }

    public String getTipoCuentaInicial() {
        return tipoCuentaInicial;
    }

    public void setTipoCuentaInicial(String tipoCuentaInicial) {
        this.tipoCuentaInicial = tipoCuentaInicial;
    }

    public double getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(double saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "cedula='" + cedula + '\'' +
                ", nombre='" + nombre + '\'' +
                ", fechaNacimiento='" + fechaNacimiento + '\'' +
                ", estadoCivil='" + estadoCivil + '\'' +
                ", numCuentaInicial='" + numCuentaInicial + '\'' +
                ", tipoCuentaInicial='" + tipoCuentaInicial + '\'' +
                ", saldoInicial=" + saldoInicial +
                '}';
    }
}
