package ec.edu.monster.modelo;

import java.math.BigDecimal;

public class MovimientoRequest {

    public String tipo;              // DEP, RET, TRA
    public BigDecimal valor;
    public String fecha;             // opcional yyyy-MM-dd

    // Para DEP / RET
    public String numCuenta;

    // Para TRA
    public String numCuentaOrigen;
    public String numCuentaDestino;
}
