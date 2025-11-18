package ec.edu.monster.dto;

public class MovimientoDTO {

    public Long id;
    public String numCuenta;
    public String tipo;               // DEP / RET / TRA
    public String naturaleza;         // INGRESO / EGRESO
    public boolean internoTransferencia;
    public double valor;
    public String fecha;              // "2024-01-10"
    public double saldoCuenta;
}
